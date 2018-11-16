package net.eclever.backend.graphql;

import com.coxautodev.graphql.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;

import javax.servlet.annotation.WebServlet;

/**
 * GraphQL endpoint for stations
 *
 * @author Veit Weber, v.weber@nextmove.de, 15.11.2018
 */
@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {
	public GraphQLEndpoint() {
		super(buildSchema());
	}

	private static GraphQLSchema buildSchema() {
		StationRepository stationRepository = new StationRepository();
		return SchemaParser.newParser()
				.file("schema.graphqls")
				.resolvers(new Query(stationRepository), new Mutation(stationRepository))
				.build()
				.makeExecutableSchema();
	}
}