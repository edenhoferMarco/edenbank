package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractEntity<K> implements Serializable {

    private K id;

    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        final AbstractEntity other = (AbstractEntity) o;

        return Objects.equals(getId(), other.getId());
    }

    @Override
    public int hashCode(){
        if (getId() == null)
            return 0;
        else
            return getId().hashCode();
    }
}
