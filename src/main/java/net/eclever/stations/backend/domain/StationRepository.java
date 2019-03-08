package net.eclever.stations.backend.domain;

import lombok.Data;

import javax.ejb.Singleton;
import java.util.Vector;

/**
 * @author Veit Weber, , $(DATE)
 */
@Singleton
@Data
public class StationRepository {

	private Vector<Station> cachedStations = new Vector<>();

}
