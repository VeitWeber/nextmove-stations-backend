package net.eclever.backend.graphql;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import java.util.List;

public class Query implements GraphQLRootResolver {

	private final StationRepository stationRepository;

	public Query(StationRepository linkRepository) {
		this.stationRepository = linkRepository;
	}

	public List<Station> allStations() {
		return stationRepository.getAllStations();
	}
}