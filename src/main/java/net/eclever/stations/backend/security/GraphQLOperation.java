package net.eclever.stations.backend.security;

public enum GraphQLOperation {
	OPEN_QUERY_INTROSPECTION("__schema"),
	OPEN_QUERY_ALL_STATIONS("stations"),
	OPEN_QUERY_ALL_STATIONS_NEARBY("stationsNearby"),
	OPEN_QUERY_STATION("station");


	private String text;

	GraphQLOperation(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public static GraphQLOperation fromString(String text) {
		for (GraphQLOperation b : GraphQLOperation.values()) {
			if (b.text.equals(text)) {
				return b;
			}
		}
		return null;
	}
}
