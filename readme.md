before running spring boot app make sure db is created on mysql

also you can setup config in environment or in application.yml

then run

On Docker
docker-compose up -d

On Local
mvn spring-boot:run

### To Setup H2 DB
remove mysql confige from respective files
application.yml

spring:
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
  datasource:
    url: jdbc:h2:mem:testdb
    username: ${MYSQL_USERNAME:root}
    password: ${MYSQL_PASSWORD:root}
    driverClassName: org.h2.Driver
  jwt:
    secret: bhagyesh
    exp_time: 3600

pom.xml
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>runtime</scope>
		</dependency>
