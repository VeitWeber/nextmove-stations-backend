package net.eclever.backend.graphql.filter;

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

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.interfaces.RSAPublicKey;


/**
 * Filter for Auth0 Check, needs to be enabled in web.xml
 *
 * @author Veit Weber, v.weber@nextmove.de, 26.09.2018
 */
public class Auth0Filter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

		// Get the HTTP Authorization header from the request
		String authorizationHeader = ((HttpServletRequest) servletRequest).getHeader(HttpHeaders.AUTHORIZATION);


		// Check if the HTTP Authorization header is present and formatted correctly
		if (Strings.isNullOrEmpty(authorizationHeader)) {
			((HttpServletResponse) servletResponse).sendError(Response.Status.UNAUTHORIZED.getStatusCode(), "No token in authorization header provided.");
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

			chain.doFilter(servletRequest, servletResponse);
		} catch (ArrayIndexOutOfBoundsException ex) {
			((HttpServletResponse) servletResponse).sendError(Response.Status.UNAUTHORIZED.getStatusCode(), "Malformed id or access token in authorization header.");
		} catch (JwkException ex) {
			((HttpServletResponse) servletResponse).sendError(Response.Status.UNAUTHORIZED.getStatusCode(), "Invalid id or access token signature in authorization header.");
		} catch (JWTVerificationException exception) {
			((HttpServletResponse) servletResponse).sendError(Response.Status.UNAUTHORIZED.getStatusCode(), "Id or access token in authorization header could not be verified.");
		}
	}

	@Override
	public void destroy() {

	}
}
