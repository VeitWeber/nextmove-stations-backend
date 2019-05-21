package net.eclever.stations.backend.domain.db.rel.jpa.entities;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.eclever.stations.backend.Environment;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = Environment.DbProperties.TABLE_USER, schema = Environment.DbProperties.SCHEMA, catalog = Environment.DbProperties.CATALOG)
@Cacheable
@NamedQueries({
		@NamedQuery(name = "User.findById", query = "SELECT u FROM UserEntity u WHERE u.id = :userId"),
		@NamedQuery(name = "User.findByAuth0Id", query = "SELECT u FROM UserEntity u WHERE u.auth0UserId = :auth0UserId"),
		@NamedQuery(name = "User.findByMail", query = "SELECT u FROM UserEntity u WHERE u.mail = :mail")})
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserEntity extends BaseEntity {
	@Id
	@Column(name = "id", unique = true, nullable = false)
	private UUID id;

	@Getter
	@Setter
	@Column(name = "auth0_user_id", unique = true, nullable = false)
	private String auth0UserId;

	@Getter
	@Setter
	@Basic
	@Column(name = "mail")
	private String mail;

	@Getter
	@Setter
	@ManyToMany
	@JoinTable(
			name = "user_role",
			joinColumns = {
					@JoinColumn(name = "userid", unique = true)
			},
			inverseJoinColumns = {
					@JoinColumn(name = "roleid")
			})
	private List<RoleEntity> roleList;

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public void setId(UUID id) {
		this.id = id;
	}
}