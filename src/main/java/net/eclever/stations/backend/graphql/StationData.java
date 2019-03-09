package net.eclever.stations.backend.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.eclever.stations.backend.domain.Station;

/**
 * @author Veit Weber, , $(DATE)
 */
@Data
@AllArgsConstructor
public class StationData {
	public int count;
	public Station[] stationList = new Station[0];
}
