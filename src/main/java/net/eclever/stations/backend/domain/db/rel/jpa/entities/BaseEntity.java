package net.eclever.stations.backend.domain.db.rel.jpa.entities;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
abstract class BaseEntity implements Serializable, Persistable<UUID> {

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!this.getClass().isInstance(o)) return false;
		if (this.getId() == null) return false;

		Persistable that = (Persistable) o;
		return this.getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return this.getId() == null ? -1 : getId().hashCode();
	}

	public abstract UUID getId();

	public abstract void setId(UUID id);


}