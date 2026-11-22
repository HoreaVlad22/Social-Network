package domain.entities;

import java.util.Objects;

/**
 * Base class for all entities in the application.
 * @param <IDType> the generic type of the entity's identifier
 */
public abstract class Entity<IDType> {
    protected  IDType id;

    public Entity(IDType id) { this.id = id; }

    public IDType getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }

    public void setId(IDType id) {
        this.id = id;
    }


}