package domain.validator;

import domain.entities.Entity;
import domain.exceptions.ValidatorException;
import java.util.Objects;

/**
 * Generic functional interface for entity validation.
 * @param <IDType> the type of the identifier
 * @param <EType> the type of the entity extending Entity
 */
@FunctionalInterface
public interface Validator<IDType, EType extends Entity<IDType>> {

    /**
     * Validates the given entity.
     * @param entity the entity to validate
     * @throws ValidatorException if the entity attributes are invalid
     */
    void validate(EType entity) throws ValidatorException;

    /**
     * Returns a composed validator that performs this validation followed by the {@code other} validation.
     * @param other the validator to execute after this one
     * @return a composed {@code Validator}
     * @throws NullPointerException if {@code other} is null
     */
    default Validator<IDType, EType> andThen(Validator<IDType, EType> other) {
        Objects.requireNonNull(other);
        return entity -> {
            this.validate(entity);
            other.validate(entity);
        };
    }
}