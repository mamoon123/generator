package akirolab.service.generator.controller;

import akirolab.service.generator.dtos.TokenRequestDto;
import akirolab.service.generator.dtos.TokenResponseDto;
import akirolab.service.generator.service.ExecutorTokenGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GenerateTokenController {

    @Autowired
    ExecutorTokenGeneratorService executorTokenGeneratorService;

    @PostMapping("/api/generateToken")
    public ResponseEntity<TokenResponseDto> generateTokenApi(@RequestBody TokenRequestDto request) {
        String digits = request.getDigits();
        TokenResponseDto responseDto = new TokenResponseDto();
        responseDto.setTokenId("token");
        responseDto.setTokenValue(executorTokenGeneratorService.generateToken(digits));
        return ResponseEntity.ok(responseDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}