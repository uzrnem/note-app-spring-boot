package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.example.demo.schema.NoteResponse;
import com.example.demo.services.NoteService;


@RunWith(SpringRunner.class)
@WebMvcTest(value = NoteController.class) //, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class NoteControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private NoteService service;

    @Test
    public void whenPostEmployee_thenCreateEmployee() throws Exception {

        NoteResponse note = new NoteResponse(1, "Sample Note", true);

        given(service.list(Mockito.any())).willReturn(List.of(note));

        mvc.perform(get("/note")
        	.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(status().isOk())
        	.andExpect(jsonPath("$.data", hasSize(1))).andExpect(jsonPath("$.data[0].content", is(note.getContent())));
        verify(service, VerificationModeFactory.times(1)).list(Mockito.any());
        reset(service);
    }
    /*
    @Test
    public void whenPostEmployee_thenCreateEmployee() throws Exception {
        Employee alex = new Employee("alex");
        given(service.save(Mockito.any())).willReturn(alex);

        mvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(alex))).andExpect(status().isCreated()).andExpect(jsonPath("$.name", is("alex")));
        verify(service, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(service);
    }

    @Test
    public void givenEmployees_whenGetEmployees_thenReturnJsonArray() throws Exception {
        Employee alex = new Employee("alex");
        Employee john = new Employee("john");
        Employee bob = new Employee("bob");

        List<Employee> allEmployees = Arrays.asList(alex, john, bob);

        given(service.getAllEmployees()).willReturn(allEmployees);

        mvc.perform(get("/api/employees").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[0].name", is(alex.getName()))).andExpect(jsonPath("$[1].name", is(john.getName())))
                .andExpect(jsonPath("$[2].name", is(bob.getName())));
        verify(service, VerificationModeFactory.times(1)).getAllEmployees();
        reset(service);
    }
*/
}