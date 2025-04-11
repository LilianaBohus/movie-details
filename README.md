## Movie Details
A Spring Boot REST API that retrieves movie information from two external sources: OMDb and TMDb.
It returns a unified JSON response containing movie title, release year, and list of directors.

### Key Features
- Unified API interface over two providers (OMDb / TMDb)
- High-performance fetching using CompletableFuture and parallel page loading
- Caching via Redis to reduce redundant external API calls
- Search history persistence with MySQL + JPA
- Automatic pagination handling across both APIs
- Clean architecture with proper layering and modular design
- Full unit testing with JUnit and Mockito
- Docker support for easy setup and environment consistency
- Modern Java features including records, streams, and functional patterns

### Technologies Used

- Java 17+
- Spring Boot
- Redis
- MySQL
- JPA / Hibernate
- Maven
- JUnit 5, Mockito
- Docker / Docker Compose