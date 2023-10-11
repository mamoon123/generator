package akirolab.service.generator.controller;

import akirolab.service.generator.dtos.TokenRequestDto;
import akirolab.service.generator.dtos.TokenResponseDto;
import akirolab.service.generator.service.ExecutorTokenGeneratorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class GenerateTokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExecutorTokenGeneratorService executorTokenGeneratorService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        when(executorTokenGeneratorService.generateToken("1344")).thenReturn("1234-5678-2541-0000");
    }
    @Test
    public void testValidTokenFormatAndValidLuhn() throws Exception {
        String requestBody = "{ \"digits\": \"1344\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/generateToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tokenId").value("token"))
                .andExpect(jsonPath("$.tokenValue").value("1234-5678-2541-0000"));
    }

    @Test
    public void testInvalidTokenFormat() throws Exception {
        String requestBody = "{ \"digits\": \"1234567812345678\" }";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/generateToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tokenValue").value("Invalid"));
    }
}
