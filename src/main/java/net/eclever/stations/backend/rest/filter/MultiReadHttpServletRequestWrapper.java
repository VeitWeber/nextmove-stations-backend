package net.eclever.stations.backend.rest.filter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import graphql.parser.Parser;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MultiReadHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private byte[] body;

	public MultiReadHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		try {
			body = IOUtils.toByteArray(request.getInputStream());

			JsonObject body = new Gson().fromJson(new String(this.body), JsonObject.class);
			Parser parser = new Parser();

			System.out.println(body.getAsJsonPrimitive("query").toString().replaceAll("[\\r\\n]+", ""));

//			Document document = parser.parseDocument(body.getAsJsonPrimitive("query").toString());

		} catch (IOException ex) {
			body = new byte[0];
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new DelegatingServletInputStream(new ByteArrayInputStream(body));
	}

}