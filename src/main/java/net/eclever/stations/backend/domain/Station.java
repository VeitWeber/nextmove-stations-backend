package net.eclever.stations.backend.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@GraphQLType(name = "Station", description = "Chargestation")
@Data
public class Station {
	private String id;
	private String author;
	private String name;
	private String operator;
	private StationAddress address;
	private Location coordinates;
	private Location approach;
	private Long createdat;
	private Long editedat;
	private String approachDescription;
	private String manufacturer;
	private ArrayList<Chargepoint> chargepoints;
	private String buildType;
	private String buildConfig;
	private String network;
	private Boolean restricted;
	private Double rating;
	private Boolean verified;
	private Boolean barrierFree;
	private Boolean freecharging;
	private Boolean freeparking;
	private Boolean predelete;
	private Boolean pricingFree;
	private Boolean pricingContractFree;
	private Boolean pricingBarrierFree;
	


	@JsonPropertyDescription("Station ID")
	public String getId() {
		return id;
	}

	@JsonPropertyDescription("Author of the station. If scraped -> 'scraper' otherwise UserId")
	public String getAuthor() {
		return author;
	}

	@JsonPropertyDescription("Station name")
	public String getName() {
		return name;
	}

	@JsonPropertyDescription("Station operator")
	public String getOperator() {
		return operator;
	}

	@JsonPropertyDescription("Station address")
	public StationAddress getAddress() {
		return address;
	}

	@JsonPropertyDescription("Station coordinates")
	public Location getCoordinates() {
		return coordinates;
	}

	@JsonPropertyDescription("Station approach (coordinates)")
	public Location getApproach() {
		return approach;
	}

	@JsonPropertyDescription("Station creation date (timestamp)")
	public Long getCreatedat() {
		return createdat;
	}

	@JsonPropertyDescription("Station last edit date (timestamp)")
	public Long getEditedat() {
		return editedat;
	}

	@JsonPropertyDescription("Station approach (text)")
	public String getApproachDescription() {
		return approachDescription;
	}

	@JsonPropertyDescription("Station manufacturer")
	public String getManufacturer() {
		return manufacturer;
	}

	public ArrayList<Chargepoint> getChargepoints() {
		return chargepoints;
	}

	public String getBuildType() {
		return buildType;
	}

	public String getBuildConfig() {
		return buildConfig;
	}

	public String getNetwork() {
		return network;
	}

	public boolean isRestricted() {
		return restricted;
	}

	public Double getRating() {
		return rating;
	}

	public Boolean getRestricted() {
		return restricted;
	}

	public Boolean getVerified() {
		return verified;
	}

	@JsonPropertyDescription("\"Rollstuhlgerrecht\"")
	public Boolean getBarrierFree() {
		return barrierFree;
	}

	@JsonPropertyDescription("DEPRECATED in favor of \"pricingFree\"")
	public Boolean getFreecharging() {
		return freecharging;
	}

	@JsonPropertyDescription("DEPRECATED in favor of \"parkingFree\"")
	public Boolean getFreeparking() {
		return freeparking;
	}

	@JsonPropertyDescription("Set to true if DELETE was called on this station")
	public Boolean getPredelete() {
		return predelete;
	}

	@JsonPropertyDescription("If charging at this chargepoint is completely free, but you need an accessmethod thing")
	public Boolean getPricingFree() {
		return pricingFree;
	}
}
