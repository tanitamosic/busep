package securityproject.util;

import org.springframework.stereotype.Component;
import securityproject.model.user.StandardUser;

import javax.validation.*;
import java.util.Set;

@Component
public class ModelValidator {

    private final Validator validator;

    public ModelValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    public <T> void validateModelObject(T object, Class<T> tClass) throws ConstraintViolationException {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<?> violation: violations) {
                System.out.println(violation.getMessage());
            }
            // propagiraj poruke do fronta
            throw new ConstraintViolationException(violations);
        }
    }

    public void validatePassword(String rawPassword) {
        String fieldName = "password";
        Class<StandardUser> clazz = StandardUser.class;

        Set<ConstraintViolation<StandardUser>> violations = validator.validateValue(clazz, fieldName, rawPassword);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<?> violation: violations) {
                System.out.println(violation.getMessage());
            }
            // propagiraj poruke do fronta
            throw new ConstraintViolationException(violations);
        }
    }
}
