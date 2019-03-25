package net.eclever.stations.backend.graphql;

import com.google.common.base.Throwables;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.extern.java.Log;
import net.eclever.stations.backend.domain.StationRepository;
import net.eclever.stations.backend.domain.StationService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Optional;

/**
 * Stations Queries and Mutations
 *
 * @author Veit Weber, v.weber@eclever.io, 25.03.2019
 */
@ApplicationScoped
@Log
public class StationsGraphQLApi {
	@Inject
	private StationRepository stationRepository;

	@Inject
	StationService stationService;

	/**
	 * All Stations without params
	 *
	 * @return Stations
	 */
	@GraphQLQuery(name = "stations", description = "Get all stations.")
	public StationData getAllStations() {
		try {
			return new StationData(this.stationRepository.getCachedStations().length, this.stationRepository.getCachedStations());
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));
			throw new IllegalStateException();
		}
	}

	/**
	 * All stations without params since last update
	 *
	 * @param lastUpdateTimestamp Last update
	 * @return Stations
	 */
	@GraphQLQuery(name = "stations", description = "Get all stations.")
	public StationData getAllStations(@GraphQLArgument(name = "lastUpdateTimestamp", description = "Timestamp since last update (yyyy-MM-dd HH:mm). Override 'forceUpdate' to true") String lastUpdateTimestamp) {
		try {
			return new StationData(this.stationRepository.getCachedStations().length, this.stationRepository.getCachedStations());
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));
			throw new IllegalStateException();
		}
	}


	/**
	 * All stations with force update param
	 *
	 * @param optionalForceUpdate Force update from Db
	 * @return Stations
	 */
	@GraphQLQuery(name = "stations", description = "Get all stations.")
	public StationData getAllStations(@GraphQLArgument(name = "forceUpdate", description = "Invalidate cache and get the stations from the database.") Optional<Boolean> optionalForceUpdate) {
		try {
			if (optionalForceUpdate.isPresent() && optionalForceUpdate.get())
				stationService.refreshValues();

			return this.getAllStations();
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));
			throw new IllegalStateException();
		}
	}

	/**
	 * All stations with forceUpdate param since last update
	 *
	 * @param optionalForceUpdate
	 * @param lastUpdateTimestamp
	 * @return
	 */
	@GraphQLQuery(name = "stations", description = "Get all stations.")
	public StationData getAllStations(@GraphQLArgument(name = "forceUpdate", description = "Invalidate cache and get the stations from the database.") Optional<Boolean> optionalForceUpdate,
	                                  @GraphQLArgument(name = "lastUpdateTimestamp", description = "Timestamp since last update (yyyy-MM-dd HH:mm). Override 'forceUpdate' to true") String lastUpdateTimestamp) {
		try {
			if (optionalForceUpdate.isPresent() && optionalForceUpdate.get())
				stationService.refreshValues();

			return this.getAllStations();
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));
			throw new IllegalStateException();
		}
	}


	/**
	 * All stations with paging
	 *
	 * @param first  First result
	 * @param offset Results to show
	 * @return Stations
	 */
	@GraphQLQuery(name = "stations")
	public StationData getAllStations(@GraphQLArgument(name = "first", description = "First element") int first, @GraphQLArgument(name = "offset", description = "Offset") int offset) {
		StationData stationData = this.getAllStations();
		try {
			stationData.stationList = Arrays.copyOfRange(stationData.stationList, first, first + offset);
			return stationData;
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));
			throw new IllegalStateException();
		}
	}

	/**
	 * All stations with paging since last update
	 *
	 * @param first
	 * @param offset
	 * @param lastUpdateTimestamp
	 * @return
	 */
	@GraphQLQuery(name = "stations")
	public StationData getAllStations(@GraphQLArgument(name = "first", description = "First element") int first, @GraphQLArgument(name = "offset", description = "Offset") int offset,
	                                  @GraphQLArgument(name = "lastUpdateTimestamp", description = "Timestamp since last update (yyyy-MM-dd HH:mm). Override 'forceUpdate' to true") String lastUpdateTimestamp) {
		StationData stationData = this.getAllStations();
		try {
			stationData.stationList = Arrays.copyOfRange(stationData.stationList, first, first + offset);
			return stationData;
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));
			throw new IllegalStateException();
		}
	}


	/**
	 * All stations with force update param and paging
	 *
	 * @param optionalForceUpdate Force update from Db
	 * @param first               First result
	 * @param offset              Results to show
	 * @return Stations
	 */
	@GraphQLQuery(name = "stations")
	public StationData getAllStations(@GraphQLArgument(name = "forceUpdate", description = "Invalidate cache and get the stations from the database.") Optional<Boolean> optionalForceUpdate,
	                                  @GraphQLArgument(name = "first", description = "First element") int first, @GraphQLArgument(name = "offset", description = "Offset") int offset) {

		try {
			if (optionalForceUpdate.isPresent() && optionalForceUpdate.get())
				stationService.refreshValues();

			return this.getAllStations(first, offset);
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));
			throw new IllegalStateException();
		}
	}

	/**
	 * All stations with paging with force update param ince last update
	 *
	 * @param optionalForceUpdate
	 * @param first
	 * @param offset
	 * @param lastUpdateTimestampy
	 * @return
	 */
	@GraphQLQuery(name = "stations")
	public StationData getAllStations(@GraphQLArgument(name = "forceUpdate", description = "Invalidate cache and get the stations from the database.") Optional<Boolean> optionalForceUpdate,
	                                  @GraphQLArgument(name = "first", description = "First element") int first, @GraphQLArgument(name = "offset", description = "Offset") int offset,
	                                  @GraphQLArgument(name = "lastUpdateTimestamp", description = "Timestamp since last update (yyyy-MM-dd HH:mm). Override 'forceUpdate' to true") String lastUpdateTimestampy) {

		try {
			if (optionalForceUpdate.isPresent() && optionalForceUpdate.get())
				stationService.refreshValues();

			return this.getAllStations(first, offset);
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));
			throw new IllegalStateException();
		}
	}
}