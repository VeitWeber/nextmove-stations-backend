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
import net.eclever.stations.backend.Environment;
import net.eclever.stations.backend.security.GraphQLOperation;
import sun.misc.BASE64Decoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDate;
import java.util.Base64;


@WebFilter(filterName = "AuthorizationFilter", urlPatterns = "/graphql/*")
public class AuthorizationFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
	                     FilterChain chain) throws IOException, ServletException {
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		MultiReadHttpServletRequestWrapper multiReadRequest = new MultiReadHttpServletRequestWrapper((HttpServletRequest) request);
		GraphQLOperation graphQLOperation = multiReadRequest.getGraphQLOperation();

		servletResponse.setHeader("Access-Control-Allow-Origin", "*");
		servletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");


		if (graphQLOperation == null) {
			servletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		} else {
			if (graphQLOperation == GraphQLOperation.OPEN_QUERY_INTROSPECTION) {
				chain.doFilter(multiReadRequest, response);
				return;
			}

			String origin = servletRequest.getHeader("Origin");
			String userAgent = servletRequest.getHeader(HttpHeaders.USER_AGENT);
			String authHeader = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);

			boolean originValid = this.checkOrigin(origin);

			if (!originValid && !this.checkUserAgent(userAgent)) {
				servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Err_1: Your client is not authorized for this operation.");
				return;
			}

			if (Strings.isNullOrEmpty(authHeader) && !originValid) {
				servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Err_2: Your client is not authorized for this operation.");
				return;
			}

			if (originValid || !Strings.isNullOrEmpty(authHeader)) {
				if (authHeader.startsWith("ey")) {
					try {
						String[] tokenBuffer = authHeader.split("#\\|#");
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
					// todo role required and role check
					chain.doFilter(multiReadRequest, response);


				} else {
					if (authHeader.startsWith("$2") && authHeader.length() > 5) {
						try {
							boolean validToken = this.checkCleverToken(authHeader.substring(2), userAgent);
							//todo check token & useragent matching

							if (!validToken) {
								servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Err_1: Token is not valid.");
								return;
							}

							if (graphQLOperation.toString().startsWith("OPEN_")) {
								chain.doFilter(multiReadRequest, response);
								return;
							} else {
								servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Operation is not allowed.");
								return;
							}
						} catch (Exception ex) {
							servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Err_2: Token is not valid.");
							return;
						}
					} else {
						servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unknown token format.");
						return;
					}
				}
			} else {
				servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Err_3: Your client is not authorized for this operation.");
				return;
			}
		}
	}

	private boolean checkUserAgent(String userAgent) {
		if (userAgent.startsWith(Environment.UserAgents.IOS_V_1_0) && userAgent.contains("iOS"))
			return true;

		if (userAgent.startsWith(Environment.UserAgents.ANDROID_V_2_1_0) && userAgent.contains("Android"))
			return true;

		//enable altair for local testing
		if (Environment.IS_LOCAL)
			return userAgent.startsWith("Mozilla") && userAgent.contains("Safari");

		return false;
	}

	private boolean checkOrigin(String origin) {
		return (Origins.getSafeOrigins().contains(origin));
	}

	private boolean checkCleverToken(String encryptedText, String userAgent) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		String localDateString = LocalDate.now().toString();
		SecretKeySpec secretKeySpec = new SecretKeySpec(localDateString.getBytes(), "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish");

		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		String value = new String(cipher.doFinal(new BASE64Decoder().decodeBuffer(encryptedText)));

		if (value.equals(new String(Base64.getDecoder().decode(Environment.Platform.IOS)))) {
			return userAgent.startsWith(Environment.UserAgents.IOS_V_1_0) && userAgent.contains("iOS");
		}

		if (value.equals(new String(Base64.getDecoder().decode(Environment.Platform.ANDROID)))) {
			return userAgent.startsWith(Environment.UserAgents.ANDROID_V_2_1_0) && userAgent.contains("Android");
		}

		return false;
	}

	@Override
	public void destroy() {
	}

}
