package net.eclever.stations.backend.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.eclever.stations.backend.domain.Station;


@Data
@AllArgsConstructor
public class StationData {
	public int totalCount;
	public Station[] stationList = new Station[0];
}
