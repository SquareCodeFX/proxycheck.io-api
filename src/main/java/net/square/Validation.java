package net.square;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Validation {

    /**
     * Check if a reference is null and throw a NullPointerException
     *
     * @param reference    - Check if this value is null
     * @param errorMessage - If reference is null
     * @param <T>          - reference
     */
    public <T> void checkNotNull(T reference, String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
    }
}
