package net.eclever.backend.graphql;


import com.coxautodev.graphql.tools.GraphQLRootResolver;

public class Mutation implements GraphQLRootResolver {

	private final StationRepository stationRepository;

	public Mutation(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	public Station createStation(String url, String description) {
		Station newStation = new Station(url, description);
		stationRepository.saveStation(newStation);
		return newStation;
	}
}