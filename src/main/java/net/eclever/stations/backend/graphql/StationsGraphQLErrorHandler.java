package net.eclever.stations.backend.graphql;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.servlet.GraphQLErrorHandler;
import graphql.validation.ValidationError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StationsGraphQLErrorHandler implements GraphQLErrorHandler {

	@Override
	public List<GraphQLError> processErrors(List<GraphQLError> errors) {
		List<GraphQLError> wrappedErrors = new ArrayList<>();
		for (GraphQLError error : errors) {
			if (error instanceof ExceptionWhileDataFetching | error instanceof ValidationError) {
				Throwable t;
				if (error instanceof ValidationError)
					t = new IllegalArgumentException(error.getMessage());
				else
					t = ((ExceptionWhileDataFetching) error).getException();
				wrappedErrors.add(getGraphQLError(t));
			}
		}
		return wrappedErrors;
	}

	private GraphQLError getGraphQLError(Throwable t) {
		if (t instanceof GraphQLError) {
			return (GraphQLError) t;
		} else if (t instanceof IllegalArgumentException) {
			IllegalArgumentException illegalArgumentException = (IllegalArgumentException) t;
			return new StationGraphQLError(illegalArgumentException.getMessage(), 400);
		} else if (t instanceof ConstraintViolationException) {
			ConstraintViolationException constraintViolationException = (ConstraintViolationException) t;
			return new StationGraphQLError(getConstraintViolationMessage(constraintViolationException), 400);
		} else {
			if (t.getCause() != null) {
				return getGraphQLError(t.getCause());
			} else {
				return new StationGraphQLError("Internal Server Error(s) while executing query", 500);
			}
		}
	}

	private String getConstraintViolationMessage(ConstraintViolationException constraintViolationException) {
		StringWriter sw = new StringWriter();
		Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
		for (ConstraintViolation<?> s : constraintViolations) {
			sw.write(s.getMessage());
		}
		return sw.toString();
	}
}
