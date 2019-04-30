package net.eclever.stations.backend.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@GraphQLType(name = "Chargepoint", description = "Chargepoint")
public class Chargepoint {
	private String id;
	private Boolean privateStation;
	private Integer phases;
	private Double power;
	private String type;
	private String status;
	private String problem;
	private Double ampere;
	private Double volt;
	private String plugCable;
	private Boolean pricingFree;
	private String pricingType;
	private String pricingValue;

	@JsonPropertyDescription("Chargepoint ID")
	public String getId() {
		return id;
	}

	@JsonPropertyDescription("Private chargepoint indicator")
	public Boolean isPrivate() {
		return privateStation;
	}

	@JsonPropertyDescription("Chargepoint phases")
	public Integer getPhases() {
		return phases;
	}

	@JsonPropertyDescription("Chargepoint power")
	public Double getPower() {
		return power;
	}

	@JsonPropertyDescription("Chargepoint type (plug type id)")
	public String getType() {
		return type;
	}

	@JsonPropertyDescription("Chargepoint status")
	public String getStatus() {
		return status;
	}

	@JsonPropertyDescription("Cgargepoint problem (Enum ['', innoperative])")
	public String getProblem() {
		return problem;
	}

	@JsonPropertyDescription("Chargepoint ampere")
	public Double getAmpere() {
		return ampere;
	}

	@JsonPropertyDescription("Chargepoint volt")
	public Double getVolt() {
		return volt;
	}

	@JsonPropertyDescription("Chargepoint plug or cable (Enum ['plug', 'cable']")
	public String getPlugCable() {
		return plugCable;
	}

	@JsonPropertyDescription("If charging at this chargepoint is completely free")
	public Boolean getPricingFree() {
		return pricingFree;
	}

	@JsonPropertyDescription("Pricing type")
	public String getPricingType() {
		return pricingType;
	}

	@JsonPropertyDescription("Actual priceage value")
	public String getPricingValue() {
		return pricingValue;
	}
}
