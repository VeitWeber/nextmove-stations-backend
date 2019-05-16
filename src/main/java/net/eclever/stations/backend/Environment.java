package net.eclever.stations.backend;

public interface Environment {
	public static final boolean IS_LOCAL = System.getenv("NEXTMOVE_ENV").equals("local");
	public static final boolean IS_DEV = System.getenv("NEXTMOVE_ENV").equals("dev");
	public static final boolean IS_PROD = System.getenv("NEXTMOVE_ENV").equals("prod");

	interface MongoDbProperties {
		String DB_URI = System.getenv("STATION_MONGODB_URI");
		String DB_NAME = System.getenv("STATION_MONGODB_NAME");
		String COLLECTION_NAME = "stations";
	}

	public interface DbProperties {
		String DEFAULT_PERSISTENCE_CONTEXT_NAME = "stationPU";
		String SCHEMA = "nextmove";
		String CATALOG = "postgres";
		String TABLE_USER = "user";
		String TABLE_ROLE = "role";
	}

	interface UserAgents {
		String IOS_V_1_0 = "eclever/1.0";
	}

	interface Platform {
		String IOS = "aW9zX3N0YXRpb25zXzc3NjVmZjFjLWE2OWMtNDE3Zi1iMjI0LTUyMjhlOWY0OGY4NA==";
	}
}
