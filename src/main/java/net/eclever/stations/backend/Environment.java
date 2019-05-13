package net.eclever.stations.backend;

/**
 * @author Veit Weber, , $(DATE)
 */
public interface Environment {
	interface MongoDbProperties {
		String DB_NAME = System.getenv("STATION_MONGODB_URI");
		String COLLECTION_NAME = "stations";
	}
}
