before running spring boot app make sure db is created on mysql

also you can setup config in environment or in application.yml

then run

On Docker
docker-compose up -d

On Local
mvn spring-boot:run

### To use H2 DB
mvn spring-boot:run -Dspring-boot.run.profiles=test


git@github.com:Java-Techie-jt/spring-reactive-mongo-crud.git


#### Test cases written
> Mock Private Field
```
org.springframework.test.util.ReflectionTestUtils.setField(myService, "dependency", mockDependency);
```
<br />

> Parameterized Test cases ( one in many )
```
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
```
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
```
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
```
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
```
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

        try (MockedStatic<CompletableFuture> mockedStatic = Mockito.mockStatic(CompletableFuture.class)) {
            // Configure the mock behavior
            mockedStatic.when(() -> CompletableFuture.runAsync(any(Runnable.class)))
                .thenReturn(RuntimeException.class);
        }
    }
}
```
<br />

> Mocked Construction
```
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
