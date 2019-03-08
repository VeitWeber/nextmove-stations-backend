package net.eclever.stations.backend.graphql;

import io.leangen.graphql.annotations.GraphQLQuery;
import net.eclever.stations.backend.domain.Station;
import net.eclever.stations.backend.domain.StationRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Veit Weber, , $(DATE)
 */
@RequestScoped
public class MembershipGraphQLApi {
	@Inject
	private StationRepository stationRepository;

	@GraphQLQuery(name = "allStations")
	public List<Station> getAllStations() {
		return this.stationRepository.getCachedStations();
	}
}


