package net.eclever.stations.backend.graphql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

/**
 * @author Veit Weber, , $(DATE)
 */
public class StationGraphQLError implements GraphQLError {
	private final String message;
	private final int errorCode;

	public StationGraphQLError(String message, int errorCode) {
		this.message = message;
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return this.message;
	}

	public int getErrorCode() {
		return this.errorCode;
	}

	@JsonIgnore
	public List<SourceLocation> getLocations() {
		return null;
	}

	@JsonIgnore
	public ErrorType getErrorType() {
		return null;
	}
}