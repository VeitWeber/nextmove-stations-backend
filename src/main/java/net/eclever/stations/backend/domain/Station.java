package net.eclever.stations.backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Veit Weber, , $(DATE)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Station {
	private String id;
	private String author;
	private String name;
	private String operator;
	private StationAddress address;
	private StationLocation coordinates;
	private StationLocation approach;

}
