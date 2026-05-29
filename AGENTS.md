# Our Java/Spring Boot Standards
- Use constructor injection, no @Autowired on fields
- All REST endpoints must have @Valid on request bodies
- Every public method needs a unit test
- No raw SQL — use JPA or named queries only
- Services must not call repositories from other domains