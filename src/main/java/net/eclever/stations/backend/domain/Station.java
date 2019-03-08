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
public class Station {
	private String id;
	private String name;
}
