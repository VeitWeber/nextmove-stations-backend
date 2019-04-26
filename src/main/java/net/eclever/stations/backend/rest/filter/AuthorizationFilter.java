package net.eclever.stations.backend.rest.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;


@WebFilter(filterName = "AuthorizationFilter", urlPatterns = "/graphql/*")
public class AuthorizationFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
	                     FilterChain chain) throws IOException, ServletException {
		MultiReadHttpServletRequestWrapper multiReadRequest = new MultiReadHttpServletRequestWrapper((HttpServletRequest) request);

		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		if (Strings.isNullOrEmpty(authorizationHeader)) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), "No token in authorization header provided.").build());
			return;
		}

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
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), "Malformed id or access token in authorization header.").build());
		} catch (JwkException ex) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), "Invalid id or access token signature in authorization header.").build());
		} catch (JWTVerificationException exception) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), "Id or access token in authorization header could not be verified.").build());
		}
	}


		chain.doFilter(multiReadRequest, response);


	}

	@Override
	public void destroy() {
	}

}
