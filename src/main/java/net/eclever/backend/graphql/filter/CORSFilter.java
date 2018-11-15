package net.eclever.backend.graphql.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter for CORS removal, needs to be enabled in web.xml
 *
 * @author Veit Weber, v.weber@nextmove.de, 15.11.2018
 */
public class CORSFilter implements Filter {


	@Override
	public void init(FilterConfig filterConfig) {

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;

		// Authorize (allow) all domains to consume the content
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "*");
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST");
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers", "Content-Type, token, Access-Control-Allow-Headers, Authorization, X-Requested-With");

		HttpServletResponse resp = (HttpServletResponse) servletResponse;

		// For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
		if (request.getMethod().equals("OPTIONS")) {
			resp.setStatus(HttpServletResponse.SC_ACCEPTED);
			return;
		}

		// pass the request along the filter chain
		chain.doFilter(request, servletResponse);
	}

	@Override
	public void destroy() {

	}

}