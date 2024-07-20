before running spring boot app make sure db is created on mysql

also you can setup config in environment or in application.yml

then run

On Docker
docker-compose up -d

On Local
mvn spring-boot:run

### To use H2 DB
mvn spring-boot:run -Dspring-boot.run.profiles=test


#### Test cases written
> MVC Integration Test cases in controller
> Unit Test cases, Argument Captor, Logger checker, Private Field Modification in service

