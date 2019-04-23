package net.eclever.stations.backend.rest.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * CORS Feature for JAX-RS
 *
 * @author Veit Weber, v.weber@nextmove.de, 12.03.2018
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {
	// region -- CONSTANTS --

	// endregion

	// region -- FIELDS --

	// endregion

	// region -- CONSTRUCTION --

	// endregion

	// region -- METHODS --

	@Override
	public void filter(final ContainerRequestContext requestContext,
	                   final ContainerResponseContext cres)  {
		cres.getHeaders().add("Access-Control-Allow-Origin", "*");
		cres.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
		cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
		cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		cres.getHeaders().add("Access-Control-Max-Age", "1209600");
		cres.getHeaders().add("Access-Control-Expose-Headers", "authorization");

	}


	// endregion

	// region -- GETTER / SETTER --

	// endregion
}
