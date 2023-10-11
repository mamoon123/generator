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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
    public void whenValidInput_thenReturnsToken() throws Exception {
        String input = "1234";
        String mockGeneratedToken = "1234-5678-9012-3456";

        when(executorTokenGeneratorService.generateToken(input)).thenReturn(mockGeneratedToken);

        TokenRequestDto requestDto = new TokenRequestDto();
        requestDto.setDigits(input);

        TokenResponseDto expectedResponse = new TokenResponseDto();
        expectedResponse.setTokenId("token");
        expectedResponse.setTokenValue(mockGeneratedToken);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/generateToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tokenId").value(expectedResponse.getTokenId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tokenValue").value(expectedResponse.getTokenValue()));

        verify(executorTokenGeneratorService, times(1)).generateToken(any());
    }

    @Test
    public void whenInvalidInput_thenReturnsBadRequest() throws Exception {
        String input = "invalid";

        when(executorTokenGeneratorService.generateToken(input))
                .thenThrow(new IllegalArgumentException("Input must be numeric and less than 16 characters."));

        TokenRequestDto requestDto = new TokenRequestDto();
        requestDto.setDigits(input);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/generateToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verify(executorTokenGeneratorService, times(1)).generateToken(any());
    }
}
