package com.example.demo.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;


@SpringBootTest
@ActiveProfiles("test")
@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class TestingClubTest {

	@InjectMocks
	TestingClub testingClub;

    @Test
    void testGetAll(CapturedOutput output) {
    	ReflectionTestUtils.setField(testingClub, "number", 9);
    	testingClub.getNumberInfo();
        assertThat(output).contains("current number is 9");
    }
}
