package net.eclever.stations.backend.domain.db.rel.jpa.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.eclever.stations.backend.Environment;

import javax.persistence.*;
import java.util.UUID;

/**
 * Role Entity
 *
 * @author Veit Weber, v.weber@nextmove.de, 17.05.2018
 */
@Entity
@Table(name = Environment.DbProperties.TABLE_ROLE, schema = Environment.DbProperties.SCHEMA, catalog = Environment.DbProperties.CATALOG)
@ToString
@NoArgsConstructor
public class RoleEntity extends BaseEntity {
	@Id
	@Column(name = "id", unique = true, nullable = false)
	private UUID id;

	@Getter
	@Setter
	@Basic
	@Column(name = "rolename")
	private String rolename;

	@Getter
	@Setter
	@Basic
	@Column(name = "roleid")
	private Integer roleId;


	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public void setId(UUID id) {
		this.id = id;
	}
}
