---
name: project-context
description: Core facts about the blog-api-project-starter codebase — stack, structure, and recurring issues
metadata:
  type: project
---

Spring Boot 4.0.5 blog API starter (Java 25). Single REST controller (BlogController) with no service or repository layer. State stored in a static in-memory ArrayList — no database, no persistence. Lombok is configured as an annotation processor but has no actual dependency declared in pom.xml (dead config). No validation library (spring-boot-starter-validation absent). No Actuator. No OpenAPI/Swagger.

**Why:** This appears to be a teaching scaffold — the project is intentionally minimal. Improvements should be framed as "next learning step" rather than "production fix".

**How to apply:** Suggest layered architecture (Controller → Service → Repository) as the primary structural improvement. Frame database/persistence as a natural next step. Note that the static list is a concurrency hazard in a multi-threaded servlet container.

Recurring issues found in first review (2026-05-23):
- Static mutable collection used for shared state (thread safety)
- No proper domain model — posts stored as raw "title:content" strings
- ArrayIndexOutOfBoundsException possible on getPost/deletePost with bad id
- String concatenation in a loop (getTotalWordCount) instead of StringBuilder/streams
- getTotalWordCount is dead/stub logic — hardcoded list, not real data
- HTTP semantics wrong: createPost returns 200 OK instead of 201 Created; deletePost returns String instead of void/204
- No @RequestBody usage — form params used for structured data
- Wildcard import (org.springframework.web.bind.annotation.*)
- Lombok excluded from fat-jar but not declared as a dependency anywhere
- No controller tests — only a context-loads smoke test
- application.properties nearly empty (no logging level, no server port docs)
