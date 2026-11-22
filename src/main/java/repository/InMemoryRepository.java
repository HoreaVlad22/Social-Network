package repository;

import domain.entities.Entity;
import domain.validator.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory implementation of the Repository interface.
 */
public class InMemoryRepository<ID, EType extends Entity<ID>> implements Repository<ID, EType> {
    protected final Map<ID, EType> entities = new HashMap<>();
    private final Validator<ID, EType> validator;

    public InMemoryRepository(Validator<ID, EType> validator) {
        this.validator = validator;
    }

    @Override
    public Optional<EType> add(EType entity) {
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null!");
        validator.validate(entity);
        if (entities.containsKey(entity.getId())) {
            return Optional.of(entity);
        }
        return Optional.ofNullable(entities.put(entity.getId(), entity));
    }

    @Override
    public Optional<EType> save(EType entity) {
        return add(entity);
    }

    @Override
    public Optional<EType> findOne(ID id) {
        if (id == null) throw new IllegalArgumentException("ID cannot be null!");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<EType> findAll() { return entities.values(); }

    @Override
    public Optional<EType> delete(ID id) {
        if (id == null) throw new IllegalArgumentException("ID cannot be null!");
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<EType> update(EType entity) {
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null!");
        validator.validate(entity);
        if (entities.containsKey(entity.getId())) {
            entities.put(entity.getId(), entity);
            return Optional.empty();
        }
        return Optional.of(entity);
    }
}