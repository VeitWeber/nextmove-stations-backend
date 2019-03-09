package net.eclever.stations.backend.graphql;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import net.eclever.stations.backend.domain.Station;
import net.eclever.stations.backend.domain.StationRepository;
import net.eclever.stations.backend.domain.StationService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * @author Veit Weber, , $(DATE)
 */
@RequestScoped
public class StationsGraphQLApi {
	@Inject
	private StationRepository stationRepository;

	@Inject
	StationService stationService;

	@GraphQLQuery(name = "stations", description = "Get all stations.")
	public StationData getAllStations() {
		return new StationData(this.stationRepository.getCachedStations().length, this.stationRepository.getCachedStations());
	}

	@GraphQLQuery(name = "stations", description = "Get all stations.")
	public StationData getAllStations(@GraphQLArgument(name = "forceUpdate", description = "Invalidate cache and get the stations from the database.") Optional<Boolean> optionalForceUpdate) {
		if (optionalForceUpdate.isPresent() && optionalForceUpdate.get())
			stationService.refreshValues();

		return new StationData(this.stationRepository.getCachedStations().length, this.stationRepository.getCachedStations());
	}
}


