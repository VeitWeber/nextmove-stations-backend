package net.eclever.stations.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Veit Weber, , $(DATE)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationAddress {
	private String street;
	private String city;
	private String postcode;
	private String country;
}
