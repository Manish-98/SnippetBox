package one.june.snippetbox.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UUIDValidator implements ConstraintValidator<UUID, String> {

    private static final String UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    @Override
    public boolean isValid(String uuid, ConstraintValidatorContext context) {
        if (uuid == null) {
            return false;
        }
        return uuid.matches(UUID_PATTERN);
    }
}
