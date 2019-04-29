package net.eclever.stations.backend.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@GraphQLType(name = "Location", description = "Location")
public class Location {
	private Double lat;
	private Double lng;


	@JsonPropertyDescription("Latitude")
	public Double getLat() {
		return lat;
	}

	@JsonPropertyDescription("Longitude")
	public Double getLng() {
		return lng;
	}
}
