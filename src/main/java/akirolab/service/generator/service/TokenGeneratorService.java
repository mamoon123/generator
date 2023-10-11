package akirolab.service.generator.service;

import java.util.function.Function;

public abstract  class TokenGeneratorService<T> {
    protected Function<T, String> tokenGeneratorFunction;

    protected abstract T generateToken(T input);
}
