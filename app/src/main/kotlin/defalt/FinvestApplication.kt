package defalt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/** Spring Boot application of Finvest: the composition root that assembles the core and the business modules. */
@SpringBootApplication
class BackApplication

/**
 * Starts the Finvest back-end.
 *
 * @param args command line arguments, passed to Spring Boot.
 */
fun main(args: Array<String>) {
    runApplication<BackApplication>(*args)
}
