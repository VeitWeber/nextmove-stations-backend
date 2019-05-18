package net.eclever.stations.backend.db.postgres.ejb;

import net.eclever.stations.backend.Environment;
import net.eclever.stations.backend.domain.db.rel.jpa.entities.UserEntity;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserBean {

	@PersistenceContext(unitName = Environment.DbProperties.DEFAULT_PERSISTENCE_CONTEXT_NAME)
	private EntityManager entityManager;

	public UserEntity findUserByUserId(UUID userUuid) {
		return this.findUserByUserId(userUuid.toString());
	}

	public UserEntity findUserByUserId(String userId) {
		return this.findUserById(userId);
	}

	private UserEntity findUserById(String userId) {
		return this.entityManager
				.createNamedQuery("User.findById", UserEntity.class).setParameter("userId", UUID.fromString(userId)).getSingleResult();
	}

	public UserEntity findUserByAuth0Id(String auth0UserId) {
		return this.entityManager
				.createNamedQuery("User.findByAuth0Id", UserEntity.class).setParameter("auth0UserId", auth0UserId).getSingleResult();
	}

	public UserEntity findUserByMail(String mail) {
		return this.entityManager.createNamedQuery("User.findByMail", UserEntity.class).setParameter("mail", mail).getSingleResult();
	}
}