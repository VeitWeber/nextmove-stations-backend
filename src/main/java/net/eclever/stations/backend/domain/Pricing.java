package net.eclever.stations.backend.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@GraphQLType(name = "Pricing", description = "Pricing infos")
public class Pricing {
	private Boolean free;
	private String type;
	private String value;

	@JsonPropertyDescription("If charging at this chargepoint is completely free")
	public Boolean getPricingFree() {
		return free;
	}

	@JsonPropertyDescription("Pricing type")
	public String getPricingType() {
		return type;
	}

	@JsonPropertyDescription("Actual priceage value")
	public String getPricingValue() {
		return value;
	}
}
