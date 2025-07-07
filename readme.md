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
> Mock Private Field
```java
org.springframework.test.util.ReflectionTestUtils.setField(myService, "dependency", mockDependency);
```
<br />

> Parameterized Test cases ( one in many )
```java
private static Stream<Arguments> provideStringsForIsBlank() {
    return Stream.of(
      Arguments.of(null, true),
      Arguments.of("", true),
      Arguments.of("  ", true),
      Arguments.of("not blank", false)
    );
}
@ParameterizedTest
@MethodSource("provideStringsForIsBlank")
void isBlank_ShouldReturnTrueForNullOrBlankStrings(String input, boolean expected) {
    assertEquals(expected, Strings.isBlank(input));
}
```
<br />

> ArgumentCaptor
```java
@Test
void testGetAll() {
    when(noteRepository.getByUserId(anyInt())).thenReturn(List.of(note));

    List<NoteResponse> list = noteService.list(1);
    
    assertEquals(list.get(0).getContent(), note.getContent());
    ArgumentCaptor<Integer> intArgument = ArgumentCaptor.forClass(Integer.class);
    verify(noteRepository).getByUserId(intArgument.capture());
    assertEquals(note.getUser().getId(), intArgument.getValue());
}
```
<br />

> Captured Output or Log Verification
```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class MyServiceTest {

    @Test
    void testServiceOutput(CapturedOutput output) {
        // Call your service method that produces output
        System.out.println("Hello from service!");

        // Assert on the captured output
        assertThat(output).contains("Hello from service!");
    }
}
```
<br />

> WireMock
```java
// implementation 'org.wiremock.integrations:wiremock-spring-boot:3.10.0'

@SpringBootTest(classes = ExamplesTests.AppConfiguration.class)
@EnableWireMock
class ExampleTests {

  @Value("${wiremock.server.baseUrl}")
  private String wireMockUrl;

  @Test
  void returns_a_ping() {
    stubFor(get("/ping").willReturn(ok("pong")));

    RestClient client = RestClient.create();
    String body = client.get()
            .uri(wireMockUrl + "/ping")
            .retrieve()
            .body(String.class);

    assertThat(body, is("pong"));
  }

  @SpringBootApplication
  static class AppConfiguration {}
}
```
<br />

> Mocked Static Method
```java
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyServiceTest {

    @Test
    void testStaticMethodMocking() {
        try (MockedStatic<MyStaticClass> mockedStatic = Mockito.mockStatic(MyStaticClass.class)) {
            // Configure the mock behavior
            mockedStatic.when(MyStaticClass::staticMethod).thenReturn("Mocked Value");

            // Call the method that uses the static method
            String result = MyStaticClass.staticMethod(); // or a method in your service that calls it

            // Assert the result
            assertEquals("Mocked Value", result);

            // Optionally, verify interactions
            mockedStatic.verify(MyStaticClass::staticMethod);
        }
    }
}
```
<br />

> Mocked Construction
```java
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import static org.mockito.Mockito.*;

class MyServiceTest {

    @Test
    void testMethodWithNewObjectCreation() {
        try (MockedConstruction<MyDependency> mockedConstruction = mockConstruction(MyDependency.class)) {
            // Define behavior for any instance of MyDependency created within this block
            MyDependency mockDependency = mockedConstruction.constructed().get(0); // Get the first constructed instance
            when(mockDependency.someMethod()).thenReturn("mocked result");

            MyService myService = new MyService(); // This will internally create MyDependency
            String result = myService.performAction();

            verify(mockDependency).someMethod();
            // Assertions on 'result'
        }
    }
}

class MyService {
    public String performAction() {
        MyDependency dependency = new MyDependency(); // This is what we are mocking
        return dependency.someMethod();
    }
}

class MyDependency {
    public String someMethod() {
        return "real result";
    }
}
```
<br />

### Test Containers

> pom.xml
```xml
<project>
	<dependencies>
		<!-- Spring Data JPA & PostgreSQL -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<scope>runtime</scope>
		</dependency>
		<!-- Testcontainers -->
		<dependency>
			<groupId>org.testcontainers</groupId>
			<artifactId>junit-jupiter</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.testcontainers</groupId>
			<artifactId>postgresql</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-testcontainers</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
	<dependencyManagement>
		<dependencies>
			<!-- Testcontainers BOM (Recommended for version alignment) -->
			<dependency>
				<groupId>org.testcontainers</groupId>
				<artifactId>testcontainers-bom</artifactId>
				<version>1.19.0</version> <!-- Use latest version -->
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
</project>
```

> Code
```java
@Entity
@Table(name = "update")
public class Update {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
}

@Repository
public interface UpdateRepository extends JpaRepository<Update, Long> {
    Optional<Update> findById(Long id);
}

@Service
public class UpdateService {

	private static final Logger logger = LoggerFactory.getLogger(UpdateService.class)
    private final UpdateRepository UpdateRepository;

    public UpdateService(UpdateRepository UpdateRepository) {
        this.UpdateRepository = UpdateRepository;
    }

    public Update createUpdate(Update Update) {
        Update savedUpdate = UpdateRepository.save(Update);
        logger.info("Update {} saved to database:", savedUpdate);
        return savedUpdate;
    }
}
```

> TestContainers
```java
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class OrderControllerWithServiceConnectionIT {
    private static final Integer TIMEOUT = 120;
    private static final Logger logger = LoggerFactory.getLogger(OrderControllerWithServiceConnectionIT.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Container
    @ServiceConnection
    static PostgresSQLContainer<?> postgres = new PostgresSQLContainer<>(
        DockerImageName.parse("postgres:8.0") // .asCompatibleSubstituteFor("mysql")
    );

    @BeforeAll
    static void startContainers() {
        Awaitility.await().atMost(Duration.ofSeconds(TIMEOUT)).until(postgres::isRunning);
        logger.info("PostgresSQL is up and running!");
    }

    // Optional to @ServiceConnection, but useful for clarity
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @Test
    public void shouldCreateOrderAndPublishEvent() throws Exception {
        Order order = new Order("DUMMY_STATUS", "Order from Integration Test");
        Order createdOrder = orderService.createOrder(order);
        // Verify order saved in the database
        Order savedOrder = orderRepository.findById(createdOrder.getId()).orElseThrow();
        assertThat(savedOrder.getStatus()).isEqualTo("DUMMY_STATUS");
    }
}
```