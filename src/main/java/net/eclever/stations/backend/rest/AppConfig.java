package net.eclever.stations.backend.rest;


import net.eclever.stations.backend.rest.filter.Auth0Filter;
import net.eclever.stations.backend.rest.filter.CorsFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Configuration for REST App
 *
 * @author Veit Weber, v.weber@nextmove.de, 27.04.2018
 */
@ApplicationPath("graphql")
public class AppConfig extends Application {

	/**
	 * Constructor
	 */
	public AppConfig() {

	}

	/**
	 * Swagger needed
	 *
	 * @return REST Resources Set
	 */
	@Override
	public Set<Class<?>> getClasses() {
		HashSet<Class<?>> set = new HashSet<>();

		set.add(CorsFilter.class);
		set.add(Auth0Filter.class);

		return set;
	}
}