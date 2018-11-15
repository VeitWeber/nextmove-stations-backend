package net.eclever.backend.graphql;

public class Station {

	private final String url;
	private final String description;

	public Station(String url, String description) {
		this.url = url;
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public String getDescription() {
		return description;
	}
}