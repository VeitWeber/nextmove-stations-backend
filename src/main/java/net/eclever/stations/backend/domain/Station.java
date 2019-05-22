package net.eclever.stations.backend.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@GraphQLType(name = "Station", description = "Chargestation")
@Data
public class Station {
	private String id;
	private String name;
	private String author;
	private List<String> allowed;
	private Date createdat;
	private Date editedat;
	private StationAddress address;
	private Location coordinates;
	private Location approach;
	private String approachDescription;
	private String network;
	private String operator;
	private Boolean restricted;
	private Boolean verified;
	private Boolean predelete;
	private String description;
	private List<String> images;


	//	private String manufacturer;
//	private ArrayList<Chargepoint> chargepoints;
//	private String buildType;
//	private String buildConfig;
//	private Double rating;
//	private Boolean barrierFree;
//	private Boolean freecharging;
//	private Boolean freeparking;
//	private Boolean pricingFree;
//	private Boolean pricingContractfree;
//	private Boolean pricingBarrierfree;
//	private Integer parkingCapacity;
//	private Boolean parkingFree;
//
//
	@JsonPropertyDescription("Station ID.")
	public String getId() {
		return id;
	}

	@JsonPropertyDescription("Station name.")
	public String getName() {
		return name;
	}

	@JsonPropertyDescription("Author of the station. If scraped -> 'scraper' otherwise UserId.")
	public String getAuthor() {
		return author;
	}

	@JsonPropertyDescription("")
	public List<String> getAllowed() {
		return allowed;
	}

	@JsonPropertyDescription("Station creation date.")
	public Date getCreatedat() {
		return createdat;
	}

	@JsonPropertyDescription("Station last edit date.")
	public Date getEditedat() {
		return editedat;
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

	@JsonPropertyDescription("Station approach (text)")
	public String getApproachDescription() {
		return approachDescription;
	}

	public String getNetwork() {
		return network;
	}

	@JsonPropertyDescription("Station operator")
	public String getOperator() {
		return operator;
	}

	@JsonPropertyDescription("")
	public Boolean getRestricted() {
		return restricted;
	}

	@JsonPropertyDescription("")
	public Boolean getVerified() {
		return verified;
	}

	@JsonPropertyDescription("")
	public Boolean getPredelete() {
		return predelete;
	}

	@JsonPropertyDescription("")
	public String getDescription() {
		return description;
	}

	@JsonPropertyDescription("")
	public List<String> getImages() {
		return images;
	}


//	@JsonPropertyDescription("Station manufacturer")
//	public String getManufacturer() {
//		return manufacturer;
//	}
//
//	public ArrayList<Chargepoint> getChargepoints() {
//		return chargepoints;
//	}
//
//	public String getBuildType() {
//		return buildType;
//	}
//
//	public String getBuildConfig() {
//		return buildConfig;
//	}
//
//	public String getNetwork() {
//		return network;
//	}
//
//	public Boolean isRestricted() {
//		return restricted;
//	}
//
//	public Double getRating() {
//		return rating;
//	}
//
//	public Boolean getRestricted() {
//		return restricted;
//	}
//
//	public Boolean getVerified() {
//		return verified;
//	}
//
//	@JsonPropertyDescription("\"Rollstuhlgerrecht\"")
//	public Boolean getBarrierFree() {
//		return barrierFree;
//	}
//
//	@JsonPropertyDescription("DEPRECATED in favor of \"pricingFree\"")
//	public Boolean getFreecharging() {
//		return freecharging;
//	}
//
//	@JsonPropertyDescription("DEPRECATED in favor of \"parkingFree\"")
//	public Boolean getFreeparking() {
//		return freeparking;
//	}
//
//	@JsonPropertyDescription("Set to true if DELETE was called on this station")
//	public Boolean getPredelete() {
//		return predelete;
//	}
//
//	@JsonPropertyDescription("If charging at this chargepoint is completely free, but you need an accessmethod thing")
//	public Boolean getPricingFree() {
//		return pricingFree;
//	}
//
//	@JsonPropertyDescription("If you don't need an accessmethod thing")
//	public Boolean getPricingContractfree() {
//		return pricingContractfree;
//	}
//
//	@JsonPropertyDescription("If charging at this chargepoint is completely free and you don't need an accessmethod thing")
//	public Boolean getPricingBarrierfree() {
//		return pricingBarrierfree;
//	}
//
//	public Integer getParkingCapacity() {
//		return parkingCapacity;
//	}
//
//	@JsonPropertyDescription("If parking is for free with charging")
//	public Boolean getParkingFree() {
//		return parkingFree;
//	}
}
