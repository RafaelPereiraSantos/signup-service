rootProject.name = "signup-service"

include("common")
include("api:ktor", "api:spring", "api:webflux")
include("worker:commonw", "worker:plain-kafka", "worker:plain-rabbit", "worker:spring-kafka")
