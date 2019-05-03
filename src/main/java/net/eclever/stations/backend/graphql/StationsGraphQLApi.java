package net.eclever.stations.backend.graphql;

import com.google.common.base.Throwables;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.extern.java.Log;
import net.eclever.stations.backend.domain.Station;
import net.eclever.stations.backend.domain.StationRepository;
import net.eclever.stations.backend.domain.StationService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

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
			Date lastUpdate = this.transformTimestamp(lastUpdateTimestamp);
			Station[] updatedStations = stationRepository.getStationsSinceLastUpdate(lastUpdate);

			return new StationData(updatedStations.length, updatedStations);
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));

			if (ex instanceof ParseException)
				throw new IllegalArgumentException("Can't parse lastUpdateTimestamp");
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
			this.getAllStations(lastUpdateTimestamp);

			return this.getAllStations();
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));
			if (ex instanceof IllegalArgumentException)
				throw ex;
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

		if (first > stationData.totalCount)
			throw new IllegalArgumentException("First element beyond list size (" + stationData.totalCount + ").");

		if ((first + offset) > stationData.totalCount) {
			throw new IllegalArgumentException("Last element beyond list size (" + stationData.totalCount + ").");
		}

		try {
			stationData.stationList = Arrays.copyOfRange(stationData.stationList, first, first + offset);
			return stationData;
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));

			if (ex instanceof IllegalArgumentException)
				throw ex;

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
		StationData stationData = this.getAllStations(lastUpdateTimestamp);

		if (first > stationData.totalCount)
			throw new IllegalArgumentException("First element beyond list size. (" + stationData.totalCount + ").");

		if ((first + offset) > stationData.totalCount) {
			throw new IllegalArgumentException("Last element beyond list size. (" + stationData.totalCount + ").");
		}
		try {
			stationData.stationList = Arrays.copyOfRange(stationData.stationList, first, first + offset);
			return stationData;
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));

			if (ex instanceof IllegalArgumentException)
				throw ex;

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

			if (ex instanceof IllegalArgumentException)
				throw ex;

			throw new IllegalStateException();
		}
	}

	/**
	 * All stations with paging with force update param since last update
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
			return this.getAllStations(first, offset, lastUpdateTimestampy);
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));

			if (ex instanceof IllegalArgumentException)
				throw ex;

			throw new IllegalStateException();
		}
	}

	@GraphQLQuery(name = "station", description = "Get station details.")
	public Station getStation(@GraphQLArgument(name = "id", description = "Station ID") String stationId) {
		try {
			return null;
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));

			if (ex instanceof IllegalArgumentException)
				throw ex;

			throw new IllegalStateException();
		}
	}

	@GraphQLQuery(name = "stationsNearby", description = "Get stations between bounding box coordinates or by radius.")
	public StationData getAllStationsNearby(
			@GraphQLArgument(name = "bBox_ne_latitude", description = "Bounding box north east latitude.") Double boundingBoxNeLatitude,
			@GraphQLArgument(name = "bBox_ne_longitude", description = "Bounding box north east longitude.") Double boundingBoxNeLongitude,
			@GraphQLArgument(name = "bBox_sw_latitude", description = "Bounding box south west latitude.") Double boundingBoxSwLatitude,
			@GraphQLArgument(name = "bBox_sw_longitude", description = "Bounding box south west longitude.") Double boundingBoxSwLongitude) {
		try {
			Station[] stationsNearby = stationRepository.getAllStationsNearby(boundingBoxNeLatitude, boundingBoxNeLongitude, boundingBoxSwLatitude, boundingBoxSwLongitude, null, null);
			return new StationData(stationsNearby.length, stationsNearby);
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));

			if (ex instanceof IllegalArgumentException)
				throw ex;

			throw new IllegalStateException();
		}
	}

	@GraphQLQuery(name = "stationsNearby", description = "Get stations between bounding box coordinates or by radius.")
	public StationData getAllStationsNearby(
			@GraphQLArgument(name = "bBox_ne_latitude", description = "Bounding box north east latitude.") Double boundingBoxNeLatitude,
			@GraphQLArgument(name = "bBox_ne_longitude", description = "Bounding box north east longitude.") Double boundingBoxNeLongitude,
			@GraphQLArgument(name = "bBox_sw_latitude", description = "Bounding box south west latitude.") Double boundingBoxSwLatitude,
			@GraphQLArgument(name = "bBox_sw_longitude", description = "Bounding box south west longitude.") Double boundingBoxSwLongitude,
			@GraphQLArgument(name = "first", description = "First element") Optional<Integer> first,
			@GraphQLArgument(name = "offset", description = "Offset") Optional<Integer> offset) {
		try {
			Station[] stationsNearby = stationRepository.getAllStationsNearby(boundingBoxNeLatitude, boundingBoxNeLongitude, boundingBoxSwLatitude, boundingBoxSwLongitude, first.get(), offset.get());
			return new StationData(stationsNearby.length, stationsNearby);
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));

			if (ex instanceof IllegalArgumentException)
				throw ex;

			throw new IllegalStateException();
		}
	}


	@GraphQLQuery(name = "stationsNearby", description = "Get stations between bounding box coordinates or by radius.")
	public StationData getAllStationsNearby(
			@GraphQLArgument(name = "latitude", description = "Latitude") Double latitude,
			@GraphQLArgument(name = "longitude", description = "Longitude") Double longitude,
			@GraphQLArgument(name = "radius", description = "Radius in kilometers") Double radius,
			@GraphQLArgument(name = "first", description = "First element") Optional<Integer> first,
			@GraphQLArgument(name = "offset", description = "Offset") Optional<Integer> offset) {
		try {
			Station[] stationsNearby = stationRepository.getAllStationsNearby(latitude, longitude, radius, first.get(), offset.get());
			return new StationData(stationsNearby.length, stationsNearby);
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));

			if (ex instanceof IllegalArgumentException)
				throw ex;

			throw new IllegalStateException();
		}
	}

	@GraphQLQuery(name = "stationsNearby", description = "Get stations between bounding box coordinates or by radius.")
	public StationData getAllStationsNearby(
			@GraphQLArgument(name = "latitude", description = "Latitude") Double latitude,
			@GraphQLArgument(name = "longitude", description = "Longitude") Double longitude,
			@GraphQLArgument(name = "radius", description = "Radius in kilometers") Double radius) {
		try {
			Station[] stationsNearby = stationRepository.getAllStationsNearby(latitude, longitude, radius, null, null);
			return new StationData(stationsNearby.length, stationsNearby);
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(Throwables.getRootCause(ex)));

			if (ex instanceof IllegalArgumentException)
				throw ex;

			throw new IllegalStateException();
		}
	}


	private Date transformTimestamp(String timestamp) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return simpleDateFormat.parse(timestamp);
	}
}