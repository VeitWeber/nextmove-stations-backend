package net.eclever.stations.backend;

/**
 * @author Veit Weber, , $(DATE)
 */
public interface Environment {
	interface MongoDbProperties {
		String DB_NAME = "heroku_w8s6lzwm";
		String COLLECTION_NAME = "stations";
	}
}
