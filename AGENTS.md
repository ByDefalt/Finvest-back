# AGENTS.md — Finvest back-end

Context for AI coding agents (and humans) working on this repository. Read it before changing code:
the architecture rules below are **enforced by tests** and the build fails when they are broken.

## 1. What the project is

Finvest is the back-end of a personal finance application: users register, log in (JWT) and manage
their **financial accounts** (current accounts, savings, PEA, life insurance...), possibly **co-owned**
with other users. The front-end (separate repository, `http://localhost:5173` in dev) translates the
error codes returned by the API.

## 2. Tech stack

| | |
|---|---|
| Language | Kotlin 2.3 (JVM toolchain 25) |
| Framework | Spring Boot 4.1 (Spring MVC, Spring JDBC, Spring Security for the filter chain only) |
| JSON | **Jackson 3** (`tools.jackson.*` packages; annotations stay in `com.fasterxml.jackson.annotation`) |
| Database | MariaDB 11.8, schema managed by Flyway (H2 is on the classpath but not used) |
| Persistence | `NamedParameterJdbcTemplate` + hand-written SQL — **no JPA, no Spring Data repositories** |
| Auth | JWT (jjwt 0.13), BCrypt passwords |
| Build | Gradle 9.7 (Kotlin DSL), version catalog `gradle/libs.versions.toml` |
| Tests | JUnit 5 + `kotlin.test`, ArchUnit, Testcontainers (MariaDB), Spring MockMvc |
| Quality | ktlint, JaCoCo, Dokka |

## 3. Commands

```bash
./gradlew build                 # compile + tests + ktlint
./gradlew test                  # all tests (Docker must run: integration tests use Testcontainers)
./gradlew :modules:account:application:test   # tests of one module
./gradlew :app:test --tests "defalt.arch.*"    # architecture rules only
./gradlew ktlintCheck           # lint (ktlintFormat to fix)
./gradlew jacocoRootReport      # tests + coverage of the whole project -> build/reports/jacoco/root/html/index.html
./gradlew dokkaGenerate         # API documentation -> build/dokka/html/index.html (fails on undocumented public API)
./gradlew :app:bootRun          # run the application (profile dev, see below)
```

Running locally:
- **profile `dev`** (default, `application.properties`): Spring Boot Docker Compose starts
  `docker/docker-compose-dev.yaml` (MariaDB on **port 3307**). Flyway migrates, then
  `db/dev/reset.sql` + `db/dev/dev_data.sql` **wipe and reload the data at every start**.
- **profile `dev-persistent`**: `docker/docker-compose-dev-persistent.yaml` (port 3306, Docker volume), data kept.
- Swagger UI (springdoc): `/swagger-ui.html`, OpenAPI document: `/v3/api-docs`.

## 4. Architecture: modules and layers

Clean Architecture with **one Gradle module per layer**. The layer of a class is its Gradle module,
**not its package**: every package is `defalt.*` and the same package exists in several modules
(`defalt.usecase` is in auth and account). Never assume a package tells you the layer.

```
app/                         Spring Boot application: composition root (configs, SecurityConfig, WebConfig...)
core/                        shared kernel, used by every module
  domain/                    CustomException, ErrorCode, CommonErrorCode, Email, UserId
  application/               TransactionManager (port), AuthenticatedUser
  infrastructure/            SpringTransactionManager (+ test fixtures: Testcontainers, JDBC slice base class)
  presentation/              GlobalExceptionHandler, @AuthenticationRequired, @PublicEndpoint, @CurrentUser
  utils/                     Logger abstraction (ConsoleLogger, NullLogger)
modules/
  auth/   api | domain | application | infrastructure | presentation
  account/      domain | application | infrastructure | presentation
```

`:core` and `:modules` (and `:modules:auth`, `:modules:account`) are aggregator projects without code.

### Dependency rule (enforced by `app/src/test/kotlin/defalt/arch/MyArchitectureTest.kt`)

| Layer | May depend on |
|---|---|
| domain | domain (own module + core), api of other modules — **no framework** (no Spring, Jakarta, Jackson, JDBC) |
| application | domain, application, api, utils — **no framework** |
| infrastructure | domain, application, infrastructure, api, utils |
| presentation | domain, application (use cases, commands, `service` ports only), presentation, api, utils |
| api | own api + core domain only |
| app | everything |

- **Business modules only talk to each other through their `api` module** (account uses `auth:api` →
  `UserQueryApi`, `UserSummary`, `ExternalUserId`). Never import `auth:domain` from account.
- `core` never depends on a business module; nothing depends on `app`.
- Transactions: only `core:infrastructure` touches `org.springframework.transaction`.
  JDBC: infrastructure only. HTTP and JSON: presentation (and `app`) only.
- A class name must be unique across modules (a test checks it: two classes with the same FQN
  silently shadow each other on the classpath).

## 5. Where things go (package conventions)

| Package | Layer | Content / naming |
|---|---|---|
| `defalt.models` | domain | entities / value-holding data classes (immutable `val`s) |
| `defalt.valueobject` | domain, api | `@JvmInline value class` ids and codes (`AccountId`, `BankBic`...), `OwnerRef` |
| `defalt.repository` | domain | **ports**: interfaces named `*Repository`, at most 7 methods |
| `defalt.policy` | domain | stateless `object`s holding business rules (`AccountOwnershipPolicy`...) |
| `defalt.exception` | domain | `*ErrorCode` enums implementing `ErrorCode` |
| `defalt.usecase` | application | `*UseCase`, final, one public `operator fun invoke` |
| `defalt.command` | application | `*Command` data classes, input of the write use cases |
| `defalt.service` | application | ports implemented by infrastructure (`PasswordHasher`, `TokenGenerator`...) |
| `defalt.solver`, `defalt.resolver` | application | strategy dispatch (see 6.4) and owner resolution |
| `defalt.config` | infrastructure, app | `@Configuration` named `*Config` — the only place where use cases are instantiated |
| `defalt.persistence.repository` | infrastructure | `@Repository` adapters named `*JdbcRepository`, implementing a domain port |
| `defalt.persistence.queries` | infrastructure | `object *Queries` holding SQL constants, used only by the JDBC repositories |
| `defalt.controller` | presentation | `@RestController` named `*Controller` |
| `defalt.mapper` | presentation | request/DTO ↔ domain mapping (extension functions + per-type mappers) |
| `defalt.models.request/response/dto` | presentation | `*Request`, `*Response`, `*Dto` (auth uses `defalt.models` directly) |

## 6. Key patterns

### 6.1 Use cases
- Plain Kotlin classes (no Spring annotation), wired as `@Bean` in the module `*ApplicationConfig`.
- Constructor injection of **interfaces** (or application classes); at most 5 business dependencies
  (`Logger` and `TransactionManager` are not counted).
- Shape: validate with the domain policies **before** opening a transaction, then do all the work in
  `transactionManager.execute { ... }.onFailure { logger.error(...) }.getOrThrow()`.
  Reads use `executeReadOnly`.
- A use case never calls another use case.

### 6.2 Transactions and database errors
`TransactionManager.execute/executeReadOnly` return a `Result`. `SpringTransactionManager` translates
database errors: `DuplicateKeyException` → `CommonErrorCode.DUPLICATE` (409), other
`DataIntegrityViolationException` → `INTEGRITY_VIOLATION` (409). All writes go through it.

### 6.3 Persistence
- One `*JdbcRepository` per port, SQL in the matching `*Queries` object, rows mapped by hand.
- Ids are generated by the database: the id of an object passed to `create*` is ignored (often `0`).
- **No `ON DELETE CASCADE`**: delete children first (owners → details → account).
- The Flyway migrations live in `app/src/main/resources/db/migration` (single `V1__init.sql`).
  Reference data (banks, currencies, account types...) is **not** in the migrations: it is in
  `db/dev/dev_data.sql` (dev) and `core/infrastructure/src/testFixtures/resources/db/test/reference_data.sql` (tests).

### 6.4 Account types (strategy per type)
An account (`Account`) has type-specific **details** (`AccountDetails`: `CompteCourant`, `Livret`, `Pea`,
`CompteTitres`, `AssuranceVie`, `Per`, `Pee`), each stored in its own table. Everything is dispatched on
`AccountTypeCode` so that **adding a type never modifies existing classes**:
- persistence: `DetailsRepositorySolver<T>` per type, dispatched by `DetailsRepositoryResolver`;
- API mapping: `AccountDetailsMapperSolver<T, Dto>` per type (`@Component` in `AccountDetailsMappers.kt`),
  dispatched by `AccountDetailsMapperResolver`;
- JSON: `AccountDetailsJacksonModule` registers each mapper's `dtoType` under its type code; the request
  body carries `accountTypeCode` **at its root**, next to `details`, and Jackson picks the DTO
  (`@JsonTypeInfo(EXTERNAL_PROPERTY)` on `AccountWithDetailsRequest.details`);
- OpenAPI: `AccountDetailsOpenApiCustomizer` documents `details` as a `oneOf` of the DTO of every mapper.

### 6.5 Account owners
- An account has one or several owners, each with an `ownershipPercentage`; the percentages add up to 100.
- `OwnerRef` = `Internal(UserId)` (user with credentials) | `External(ExternalUserId)` (co-owner without
  login) | `Unknown(Email)` (not resolved yet, never persisted).
- Requests send owners by **email**; `AccountOwnerResolver` (via `UserQueryApi`) resolves them,
  internal users first. Only an internal owner can update/delete an account (`AccountAccessPolicy`);
  the creator must be an owner and the type of an account is immutable (`AccountUpdatePolicy`).

### 6.6 Authentication
- `/auth/*` is public (`@PublicEndpoint`): `register`, `register/external`, `login` (sets an HTTP-only
  `refreshToken` cookie, path `/auth/refresh`), `refresh` (returns `{ "token": "<access token>" }`), `logout`.
- Protected endpoints are annotated `@AuthenticationRequired` (class or method) and receive the user with
  `@CurrentUser authenticatedUser: AuthenticatedUser`. `AuthenticationInterceptor` validates the
  `Authorization: Bearer <access token>` header.
- **Every endpoint must declare `@AuthenticationRequired` or `@PublicEndpoint`** (architecture test).

## 7. Error handling (important convention)

- **Only `CustomException` is thrown by production code**, with an `ErrorCode` (an architecture test
  forbids instantiating any other exception, including through `error()`, `check()`, `require()`, `TODO()`).
- The enum name of the code is sent to the front-end in the `code` property and is its **translation
  key**; `CustomException.setProperty(...)` adds the values the message needs (`emails`, `total`, `field`...),
  then `.lock()`. The `message` is for the logs only, never sent to the client.
- Codes: `CommonErrorCode` (core), `AuthErrorCode`, `AccountErrorCode`. Each code carries its HTTP status
  (`ErrorHttp`, mapped to Spring by `ErrorCategoryMapper`).
- `GlobalExceptionHandler` renders every error as a `ProblemDetail` with a `code`: custom exceptions,
  bean validation (`VALIDATION_FAILED` + `errors[]` with `field`/`code`/`params`), unreadable body
  (`INVALID_FORMAT`), Spring MVC errors (404 `NOT_FOUND`, 405, 415, missing value `REQUIRED`...),
  anything else `INTERNAL_ERROR` (500, logged with its stack trace).
- To add an error: add a documented entry to the module's `*ErrorCode` enum, throw it, add a test,
  and tell the front-end team the new code.

## 8. Recipes

**Add an account type** (e.g. `LDDS`): domain model `: AccountDetails` + port `*Repository` (domain) →
`*DetailsRepositorySolver` (application) → `*Queries` + `*JdbcRepository` + solver `@Bean` in
`AccountApplicationConfig` (infrastructure) → `*DetailsDto` with validation + `*DetailsMapper` `@Component`
(presentation) → table in a **new** Flyway migration `V2__...sql` + the type code in the reference data →
tests (add a row to `AccountTypesApiIntegrationTest.accountTypeCases()`).

**Add an endpoint**: use case (+ command) in application, `@Bean` in the module config, controller method
with `@AuthenticationRequired`/`@PublicEndpoint`, request/response DTOs with bean validation, mapper
extension functions, unit test of the use case + end-to-end test in `app`.

**Add a business module**: create `modules/<name>/{api,domain,application,infrastructure,presentation}`,
include them in `settings.gradle.kts` and in `modules/<name>/build.gradle.kts` + `modules/build.gradle.kts`,
and add them to the expected layers in `MyArchitectureTest.Preconditions`.

**Change the schema**: never edit `V1__init.sql` once deployed; add `V<n>__description.sql`.

## 9. Tests

- **Unit tests** (no Spring): domain policies, use cases with in-memory fakes
  (`modules/*/application/src/test/kotlin/defalt/fakes`), mappers. No mocking library: write fakes.
- **Shared test fixtures** (`java-test-fixtures`): `AccountFixtures` / `assertFailsWithCode`
  (`modules/account/domain`), `ImmediateTransactionManager` (`core/application`), Testcontainers config
  and `JdbcRepositoryTest` (`core/infrastructure`). Consume them with `testImplementation(testFixtures(project(...)))`.
- **JDBC integration tests**: extend `JdbcRepositoryTest` (MariaDB container, transaction rolled back per test).
  The modules copy the app migrations into their test resources (`processTestResources` in their build file).
- **End-to-end tests** (`app/src/test`): extend `defalt.ApiIntegrationTest` (`@SpringBootTest` + MockMvc,
  database reset before each test, `token(email)` gives a real access token).
- **Architecture tests**: `defalt.arch.MyArchitectureTest` (48 rules). They identify layers from the class
  file location (`ArchitectureSupport.kt`). If a rule fails, fix the code, not the rule, unless the
  architecture itself is meant to change.
- Coverage is ~99.7 % of lines: keep new code covered (`./gradlew jacocoRootReport`).
- Test names are sentences in backticks: ``fun `rejects an unknown owner email`()``.

## 10. Code conventions

- Kotlin official style, enforced by ktlint. Immutable data (`val`, data classes); value classes for ids/codes.
- Constructor injection only (no field injection); Spring beans are stateless.
- API models (`*Request`, `*Response`, `*Dto`): every non-null field carries a required constraint
  (`@field:NotNull`, or `@field:NotBlank` / `@field:NotEmpty`), otherwise the OpenAPI document marks it optional.
- Log through `defalt.logger.Logger` (injected), never `println` / SLF4J directly.
- **KDoc in English on every declaration**, with `@param`, `@return`, `@throws` (with the error code),
  `@receiver` for extensions, `@property` for data classes. Overrides of an interface declared in another
  module copy the interface KDoc (Dokka does not inherit it across modules). `dokkaGenerate` fails on
  undocumented public API.
- Commit messages: `type(scope): message`, e.g. `feat(account): add PEE details`, `refactor(auth, account): ...`.

## 11. Pitfalls

- Layers are Gradle modules, packages are shared: check the module path of a file, not its package.
- The `app` module compiles with `-Xannotation-default-target=param-property`: annotations on constructor
  parameters (`@Value`...) are also copied on the backing field. This is still constructor injection.
- Kotlin `TODO()` throws an `Error`, which Spring wraps in a `ServletException`; the handler unwraps it (501).
- Do not use JDK-internal classes (a `jdk.jshell` import once broke the application on JDKs without that module).
- `AccountRepository` already has 7 methods, the maximum allowed for a port: split it before adding one.
- Windows development machine: files are LF in the repository, Git converts to CRLF on checkout.

## 12. Known issues / backlog

- Login reveals whether an email exists (`USER_NOT_FOUND` 404 vs `INVALID_PASSWORD` 400).
- `application-prod.properties` contains a hard-coded `jwt.secret` marked TEMPORARY: it must come from the environment.
- Deleting an account fails with `INTEGRITY_VIOLATION` when other data reference it (positions,
  balance snapshots, owner snapshots...): no cascade is implemented yet.
- Only the `auth` and `account` modules exist; tables for assets, positions, trades, transactions,
  exchange rates and snapshots are already in the schema but have no code yet.
