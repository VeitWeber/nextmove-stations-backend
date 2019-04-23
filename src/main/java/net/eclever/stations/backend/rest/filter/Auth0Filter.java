package net.eclever.stations.backend.rest.filter;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Strings;
import org.slf4j.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.security.interfaces.RSAPublicKey;


/**
 * Filter for Auth0 Check
 *
 * @author Veit Weber, v.weber@nextmove.de, 26.09.2018
 */
@Provider
@Auth0Secured
@Priority(Priorities.AUTHENTICATION)
public class Auth0Filter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) {
		// Get the HTTP Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		// Check if the HTTP Authorization header is present and formatted correctly
		if (Strings.isNullOrEmpty(authorizationHeader)) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), "No token in authorization header provided.").build());
			return;
		}

		try {
			// Extract the token from the HTTP Authorization header
			String[] tokenBuffer = authorizationHeader.split("#\\|#");
			String accessToken = tokenBuffer[0];
			String idToken = tokenBuffer[1];

			// Decode and verify access token
			DecodedJWT jwt = JWT.decode(accessToken);

			JwkProvider provider = new UrlJwkProvider("https://nextmove.eu.auth0.com/.well-known/jwks.json");
			Jwk jwk = provider.get(jwt.getKeyId());
			Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

			JWTVerifier verifier = JWT.require(algorithm)
					.withIssuer("https://nextmove.eu.auth0.com/")
					.build();
			verifier.verify(accessToken);

			// Decode and verify id token
			jwt = JWT.decode(idToken);

			jwk = provider.get(jwt.getKeyId());
			algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

			verifier = JWT.require(algorithm)
					.withIssuer("https://nextmove.eu.auth0.com/")
					.build();
			verifier.verify(idToken);
		} catch (ArrayIndexOutOfBoundsException ex) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), "Malformed id or access token in authorization header.").build());
		} catch (JwkException ex) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), "Invalid id or access token signature in authorization header.").build());
		} catch (JWTVerificationException exception) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), "Id or access token in authorization header could not be verified.").build());
		}
	}
}
