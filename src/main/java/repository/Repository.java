package repository;

import domain.entities.Entity;
import utils.StreamUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Generic interface for CRUD operations.
 * @param <ID> the type of the identifier
 * @param <EType> the type of the entity
 */
public interface Repository<ID, EType extends Entity<ID>> {

    /**
     * Adds the given entity to the repository.
     * @param entity the entity to be added
     * @return an {@code Optional} containing the entity if it already exists, or empty if added successfully
     * @throws IllegalArgumentException if the entity is null
     * @throws domain.exceptions.ValidatorException if the entity is not valid
     */
    Optional<EType> add(EType entity);

    /**
     * Finds an entity by its identifier.
     * @param id the identifier of the entity
     * @return an {@code Optional} containing the found entity, or empty if not found
     * @throws IllegalArgumentException if the id is null
     */
    Optional<EType> findOne(ID id);

    /**
     * Retrieves all entities.
     * @return an iterable collection of all entities
     */
    Iterable<EType> findAll();

    /**
     * Retrieves all entities matching the given filter predicate.
     * @param filter the predicate used to filter entities
     * @return a list of entities satisfying the predicate
     */
    default List<EType> findAll(Predicate<EType> filter) {
        return StreamUtils.toStream(findAll())
                .filter(filter)
                .toList();
    }
    /**
     * Saves the given entity (alias for add).
     * @param entity the entity to be saved
     * @return an {@code Optional} containing the entity if it already exists, or empty if saved successfully
     * @throws IllegalArgumentException if the entity is null
     * @throws domain.exceptions.ValidatorException if the entity is not valid
     */
    Optional<EType> save(EType entity);

    /**
     * Deletes an entity by its identifier.
     * @param id the identifier of the entity to be deleted
     * @return an {@code Optional} containing the deleted entity, or empty if it did not exist
     * @throws IllegalArgumentException if the id is null
     */
    Optional<EType> delete(ID id);

    /**
     * Updates the given entity.
     * @param entity the entity to be updated
     * @return an {@code Optional} containing the given entity if it did not exist in the repository, or empty if updated successfully
     * @throws IllegalArgumentException if the entity is null
     * @throws domain.exceptions.ValidatorException if the entity is not valid
     */
    Optional<EType> update(EType entity);
}