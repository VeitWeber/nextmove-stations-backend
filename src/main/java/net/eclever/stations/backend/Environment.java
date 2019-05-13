package net.eclever.stations.backend;

public interface Environment {
	interface MongoDbProperties {
		String DB_NAME = System.getenv("STATION_MONGODB_NAME");
		String COLLECTION_NAME = "stations";
	}
}
