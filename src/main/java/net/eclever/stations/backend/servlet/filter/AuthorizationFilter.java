package net.eclever.stations.backend.servlet.filter;

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
import net.eclever.stations.backend.security.GraphQLOperation;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.security.interfaces.RSAPublicKey;


@WebFilter(filterName = "AuthorizationFilter", urlPatterns = "/graphql/*")
public class AuthorizationFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
	                     FilterChain chain) throws IOException, ServletException {
		MultiReadHttpServletRequestWrapper multiReadRequest = new MultiReadHttpServletRequestWrapper((HttpServletRequest) request);
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		GraphQLOperation graphQLOperation = multiReadRequest.getGraphQLOperation();

		servletResponse.setHeader("Access-Control-Allow-Origin", "*");
		servletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");

		if (graphQLOperation == null) {
			servletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		} else {
			String origin = servletRequest.getHeader("Origin");
			String userAgent = servletRequest.getHeader(HttpHeaders.USER_AGENT);
				if (Strings.isNullOrEmpty(authorizationHeader)) {
					servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token in authorization header provided.");
			boolean originValid = this.checkOrigin(origin);
			if (!originValid && !this.checkUserAgent(userAgent)) {
				servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Err_1: Your client is not authorized for this operation.");
					return;
				}
			boolean originValid = this.checkOrigin(origin);

				try {
					String[] tokenBuffer = authorizationHeader.split("#\\|#");
					String accessToken = tokenBuffer[0];
					String idToken = tokenBuffer[1];

					DecodedJWT jwt = JWT.decode(accessToken);

					JwkProvider provider = new UrlJwkProvider("https://nextmove.eu.auth0.com/.well-known/jwks.json");
					Jwk jwk = provider.get(jwt.getKeyId());
					Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

					JWTVerifier verifier = JWT.require(algorithm)
							.withIssuer("https://nextmove.eu.auth0.com/")
							.build();
					verifier.verify(accessToken);

					jwt = JWT.decode(idToken);

					jwk = provider.get(jwt.getKeyId());
					algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

					verifier = JWT.require(algorithm)
							.withIssuer("https://nextmove.eu.auth0.com/")
							.build();
					verifier.verify(idToken);

				} catch (ArrayIndexOutOfBoundsException ex) {
					servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Malformed id or access token in authorization header.");
					return;

				} catch (JwkException ex) {
					servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid id or access token signature in authorization header.");
					return;

				} catch (JWTVerificationException exception) {
					servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Id or access token in authorization header could not be verified.");
					return;

				}
			}
		}
		chain.doFilter(multiReadRequest, response);
	private boolean checkUserAgent(String userAgent) {
		if (userAgent.startsWith(Environment.UserAgents.IOS_V_1_0) && userAgent.contains("iOS"))
			return true;
		//todo remove, for testing purposes only
		if (userAgent.startsWith("Mozilla") && userAgent.contains("Safari"))
			return true;
		return false;
	}


	private boolean checkOrigin(String origin) {
		return (Origins.getSafeOrigins().contains(origin));
	}

	@Override
	public void destroy() {
	}

}
