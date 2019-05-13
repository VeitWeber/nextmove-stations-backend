package net.eclever.stations.backend.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
@GraphQLType(name = "ParkingPricing", description = "Parking pricing")
public class ParkingPricing {


//	@JsonPropertyDescription("Chargepoint ID")
//	public String getId() {
//		return id;
//	}

}
