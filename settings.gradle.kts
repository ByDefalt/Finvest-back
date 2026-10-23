rootProject.name = "Finvest"

include("app")

include("core:application")
include("core:domain")
include("core:infrastructure")
include("core:presentation")
include("core:utils")

include("modules:account")
include("modules:auth")

include("modules:auth:application")
include("modules:auth:domain")
include("modules:auth:infrastructure")
include("modules:auth:presentation")

include("modules:account:application")
include("modules:account:domain")
include("modules:account:presentation")
include("modules:account:infrastructure")

include("modules:auth:api")