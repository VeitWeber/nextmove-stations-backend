package net.eclever.stations.backend.servlet.filter;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import graphql.language.Field;
import graphql.language.OperationDefinition;
import graphql.language.Selection;
import graphql.parser.Parser;
import net.eclever.stations.backend.security.GraphQLOperation;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class MultiReadHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private byte[] body;
	private GraphQLOperation graphQLOperation;


	public MultiReadHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		try {
			body = IOUtils.toByteArray(request.getInputStream());
			JsonObject body;
			String bodyString;

			try {
				body = new Gson().fromJson(new String(this.body), JsonObject.class);
				bodyString = body.getAsJsonPrimitive("query").toString();
			} catch (Exception ex) {
				bodyString = "";
			}

			if (!Strings.isNullOrEmpty(bodyString) && bodyString.length() > 5) {
				bodyString = bodyString.substring(1, bodyString.length() - 1).replace("\\n", "");

				Parser parser = new Parser();
				OperationDefinition operationDefinition = (OperationDefinition) parser.parseDocument(bodyString).getDefinitions().get(0);
				if (operationDefinition.getOperation().equals(OperationDefinition.Operation.QUERY) || operationDefinition.equals(OperationDefinition.Operation.MUTATION)) {
					List<Selection> selectionsList = operationDefinition.getSelectionSet().getSelections();
					if (selectionsList.size() == 1) {
						String operationName = ((Field) selectionsList.get(0)).getName();
						this.graphQLOperation = GraphQLOperation.fromString(operationName);
					}
				}
			}
		} catch (IOException ex) {
			body = new byte[0];
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new DelegatingServletInputStream(new ByteArrayInputStream(body));
	}

	public GraphQLOperation getGraphQLOperation() {
		return graphQLOperation;
	}
}