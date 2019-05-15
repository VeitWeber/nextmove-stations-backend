package net.eclever.stations.backend;

public interface Environment {
	interface MongoDbProperties {
		String DB_URI = System.getenv("STATION_MONGODB_URI");
		String DB_NAME = System.getenv("STATION_MONGODB_NAME");
		String COLLECTION_NAME = "stations";
	}

	interface UserAgents {
		String IOS_V_1_0 = "eclever/1.0";
}

	interface Platform {
		String IOS = "aW9zX3N0YXRpb25zXzc3NjVmZjFjLWE2OWMtNDE3Zi1iMjI0LTUyMjhlOWY0OGY4NA==";
}
}
