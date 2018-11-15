package net.eclever.backend.graphql;

import java.util.ArrayList;
import java.util.List;

public class StationRepository {

	private final List<Station> stations;

	public StationRepository() {
		stations = new ArrayList<>();
		//add some stations to start off with
		stations.add(new Station("http://www.bla.de", "Station 1"));
		stations.add(new Station("http://www.test.de", "Station 2"));
	}

	public List<Station> getAllStations() {
		return stations;
	}

	public void saveStation(Station link) {
		stations.add(link);
	}
}