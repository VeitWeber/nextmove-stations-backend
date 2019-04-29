package net.eclever.stations.backend.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@GraphQLType(name = "StationAddress", description = "Chargestation address")
public class StationAddress {
	private String street;
	private String city;
	private String postcode;
	private String country;

	@JsonPropertyDescription("Station street")
	public String getStreet() {
		return street;
	}

	@JsonPropertyDescription("Station city")
	public String getCity() {
		return city;
	}

	@JsonPropertyDescription("Station postcode")
	public String getPostcode() {
		return postcode;
	}

	@JsonPropertyDescription("Station country")
	public String getCountry() {
		return country;
	}
}
