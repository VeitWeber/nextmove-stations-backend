package net.eclever.stations.backend.graphql;

import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.metadata.strategy.query.AnnotatedResolverBuilder;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

/**
 * GraphQL endpoint for stations
 *
 * @author Veit Weber, v.weber@nextmove.de, 15.11.2018
 */

@WebListener
public class StationsGraphQLListener implements ServletContextListener {
	@Inject
	private StationsGraphQLApi stationsGraphQLApi;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		GraphQLSchema schema = new GraphQLSchemaGenerator()
				.withResolverBuilders(new AnnotatedResolverBuilder().withDefaultFilters())
				.withOperationsFromSingleton(stationsGraphQLApi, StationsGraphQLApi.class)
				.generate();

		SimpleGraphQLServlet.Builder builder = SimpleGraphQLServlet.builder(schema)
				.withGraphQLErrorHandler(new StationsGraphQLErrorHandler());

		SimpleGraphQLServlet graphQLServlet = builder.build();

		ServletContext context = sce.getServletContext();

		ServletRegistration.Dynamic servlet = context.addServlet(SERVLET_NAME, graphQLServlet);
		servlet.addMapping(SERVLET_URL);

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	private static final String SERVLET_NAME = "StationsGraphQLServlet";
	private static final String[] SERVLET_URL = new String[]{"/graphql/*"};

}
