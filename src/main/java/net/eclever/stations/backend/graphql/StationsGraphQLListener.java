package net.eclever.stations.backend.graphql;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLConfiguration;
import graphql.servlet.GraphQLHttpServlet;
import graphql.servlet.GraphQLObjectMapper;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.metadata.strategy.query.BeanResolverBuilder;
import io.leangen.graphql.metadata.strategy.query.FilteredResolverBuilder;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class StationsGraphQLListener implements ServletContextListener {
	@Inject
	private StationsGraphQLApi stationsGraphQLApi;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		FilteredResolverBuilder customBeanForDescMapper = new BeanResolverBuilder()
				.withDescriptionMapper(method -> (method.getAnnotation(JsonPropertyDescription.class) == null) ? "" : method.getAnnotation(JsonPropertyDescription.class).value());


		GraphQLSchema schema = new GraphQLSchemaGenerator()
//				.withResolverBuilders(new AnnotatedResolverBuilder().withDefaultFilters())
				.withNestedResolverBuilders(customBeanForDescMapper)
				.withOperationsFromSingleton(stationsGraphQLApi, StationsGraphQLApi.class)
				.generate();

		GraphQLObjectMapper objectMapper = GraphQLObjectMapper.newBuilder()
				.withGraphQLErrorHandler(new StationsGraphQLErrorHandler())
				.build();

		GraphQLConfiguration configuration = GraphQLConfiguration.with(schema)
				.with(objectMapper)
				.build();

		GraphQLHttpServlet graphQLHttpServlet = GraphQLHttpServlet.with(configuration);

		ServletContext context = sce.getServletContext();
		ServletRegistration.Dynamic servlet = context.addServlet(SERVLET_NAME, graphQLHttpServlet);
		servlet.addMapping(SERVLET_URL);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	private static final String SERVLET_NAME = "StationsGraphQLServlet";
	private static final String[] SERVLET_URL = new String[]{"/graphql/*"};
}

