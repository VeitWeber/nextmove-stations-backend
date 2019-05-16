package net.eclever.stations.backend;

import com.google.common.base.Strings;

public interface Environment {

	String ENV = System.getenv("STATION_ENV");
	boolean IS_LOCAL = !Strings.isNullOrEmpty(ENV) && ENV.equals("local");
	boolean IS_DEV = !Strings.isNullOrEmpty(ENV) && ENV.equals("dev");
	boolean IS_PROD = !Strings.isNullOrEmpty(ENV) && ENV.equals("prod");

	interface MongoDbProperties {
		String DB_URI = System.getenv("STATION_MONGODB_URI");
		String DB_NAME = System.getenv("STATION_MONGODB_NAME");
		String COLLECTION_NAME = "stations";
	}

	interface DbProperties {
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
