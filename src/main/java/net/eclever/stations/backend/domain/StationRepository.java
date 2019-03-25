package net.eclever.stations.backend.domain;

import lombok.Data;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author Veit Weber, , $(DATE)
 */
@ApplicationScoped
@Data
public class StationRepository {

	private Station[] cachedStations = new Station[0];

}
