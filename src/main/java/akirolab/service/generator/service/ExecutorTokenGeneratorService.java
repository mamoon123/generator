package akirolab.service.generator.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ExecutorTokenGeneratorService extends TokenGeneratorService<String>{

    public ExecutorTokenGeneratorService() {
        tokenGeneratorFunction = (input) -> input;
    }

    @Override
    public String generateToken(String input) {
        if (input.length() >= 16 || !input.matches("[0-9]+")) {
            throw new IllegalArgumentException("Input must be numeric and less than 16 characters.");
        }

        Random rand = new Random();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            token.append(input.charAt(rand.nextInt(input.length())));
            if ((i + 1) % 4 == 0 && i != 15) {
                token.append("-");
            }
        }
        return tokenGeneratorFunction.apply(token.toString());
    }
}
