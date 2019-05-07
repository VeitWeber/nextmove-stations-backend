package net.eclever.stations.backend.security;

public enum GraphQLOperation {
	QUERY_INTROSPECTION("__schema"),
	QUERY_ALL_STATIONS("stations"),
	QUERY_ALL_STATIONS_NEARBY("stationsNearby"),
	QUERY_STATION("station");


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
