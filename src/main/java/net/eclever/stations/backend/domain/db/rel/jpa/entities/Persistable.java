package net.eclever.stations.backend.domain.db.rel.jpa.entities;

public interface Persistable<T> {
	T getId();

	void setId(T id);
}
