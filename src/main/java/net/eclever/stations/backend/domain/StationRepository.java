package net.eclever.stations.backend.domain;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Data;
import lombok.extern.java.Log;
import net.eclever.stations.backend.Environment;
import org.bson.Document;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Sorts.ascending;

/**
 * @author Veit Weber, , $(DATE)
 */
@ApplicationScoped
@Data
@Log
public class StationRepository {

	private Station[] cachedStations = new Station[0];


	public Station[] getStationsSinceLastUpdate(Date lastUpdate) {
		LinkedList<Station> cachedStationList = new LinkedList<>();
		try {
			Gson gson = new Gson();

			MongoClientURI uri = new MongoClientURI(System.getenv("STATION_MONGODB_URI"));
			MongoClient mongoClient = new MongoClient(uri);

			MongoDatabase mongoDatabase = mongoClient.getDatabase(Environment.MongoDbProperties.DB_NAME);
			MongoCollection collection = mongoDatabase.getCollection(Environment.MongoDbProperties.COLLECTION_NAME);

			FindIterable mongoCollection = collection
					.find(or(gte("createdat", lastUpdate), gte("editedat", lastUpdate)))
					.sort(ascending("name"));


			mongoCollection.forEach((Consumer<Document>) document -> {
				Station station =
						new Station(document.get("_id").toString(), document.getString("author"), document.getString("name"), document.getString("operator"),
								document.get("address") != null ? gson.fromJson(((Document) document.get("address")).toJson(), StationAddress.class) : null,
								document.get("coordinates") != null ? gson.fromJson(((Document) document.get("coordinates")).toJson(), StationLocation.class) : null,
								document.get("approach") != null ? gson.fromJson(((Document) document.get("approach")).toJson(), StationLocation.class) : null);
				cachedStationList.add(station);
			});

			mongoClient.close();
			return cachedStationList.toArray(new Station[0]);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	public Station[] getAllStationsNearby(Double boundingBoxNeLatitude,
	                                      Double boundingBoxNeLongitude,
	                                      Double boundingBoxSwLatitude,
	                                      Double boundingBoxSwLongitude,
	                                      Integer first,
	                                      Integer offset) {
		LinkedList<Station> cachedStationList = new LinkedList<>();
		try {
			Gson gson = new Gson();

			MongoClientURI uri = new MongoClientURI(System.getenv("STATION_MONGODB_URI"));
			MongoClient mongoClient = new MongoClient(uri);

			MongoDatabase mongoDatabase = mongoClient.getDatabase(Environment.MongoDbProperties.DB_NAME);
			MongoCollection collection = mongoDatabase.getCollection(Environment.MongoDbProperties.COLLECTION_NAME);

			Document query = new Document();
			query.append("coordinates.loc.coordinates", new Document()
					.append("$geoWithin", new Document()
							.append("$box", Arrays.asList(
									Arrays.asList(
											boundingBoxSwLongitude,
											boundingBoxSwLatitude
									),
									Arrays.asList(
											boundingBoxNeLongitude,
											boundingBoxNeLatitude
									)
									)
							)
					)
			);

			FindIterable mongoCollection;

			if (first == null || offset == null)
				mongoCollection = collection
						.find(query)
						.sort(ascending("name"));
			else
				mongoCollection = collection
						.find(query)
						.skip(first)
						.limit(offset)
						.sort(ascending("name"));


			mongoCollection.forEach((Consumer<Document>) document -> {
				Station station =
						new Station(document.get("_id").toString(), document.getString("author"), document.getString("name"), document.getString("operator"),
								document.get("address") != null ? gson.fromJson(((Document) document.get("address")).toJson(), StationAddress.class) : null,
								document.get("coordinates") != null ? gson.fromJson(((Document) document.get("coordinates")).toJson(), StationLocation.class) : null,
								document.get("approach") != null ? gson.fromJson(((Document) document.get("approach")).toJson(), StationLocation.class) : null);
				cachedStationList.add(station);
			});

			mongoClient.close();
			return cachedStationList.toArray(new Station[0]);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	public Station[] getAllStationsNearby(Double latitude,
	                                      Double longitude,
	                                      Double radius,
	                                      Integer first,
	                                      Integer offset) {
		LinkedList<Station> cachedStationList = new LinkedList<>();
		try {
			Gson gson = new Gson();

			MongoClientURI uri = new MongoClientURI(System.getenv("STATION_MONGODB_URI"));
			MongoClient mongoClient = new MongoClient(uri);

			MongoDatabase mongoDatabase = mongoClient.getDatabase(Environment.MongoDbProperties.DB_NAME);
			MongoCollection collection = mongoDatabase.getCollection(Environment.MongoDbProperties.COLLECTION_NAME);

			Document query = new Document();
			query.append("coordinates.loc.coordinates", new Document()
					.append("$geoWithin", new Document()
							.append("$centerSphere", Arrays.asList(
									Arrays.asList(
											longitude,
											latitude
											),
									(radius / 6378.1)
									)
							)
					)
			);

			FindIterable mongoCollection;

			if (first == null || offset == null)
				mongoCollection = collection
						.find(query)
						.sort(ascending("name"));
			else
				mongoCollection = collection
						.find(query)
						.skip(first)
						.limit(offset)
						.sort(ascending("name"));


			mongoCollection.forEach((Consumer<Document>) document -> {
				Station station =
						new Station(document.get("_id").toString(), document.getString("author"), document.getString("name"), document.getString("operator"),
								document.get("address") != null ? gson.fromJson(((Document) document.get("address")).toJson(), StationAddress.class) : null,
								document.get("coordinates") != null ? gson.fromJson(((Document) document.get("coordinates")).toJson(), StationLocation.class) : null,
								document.get("approach") != null ? gson.fromJson(((Document) document.get("approach")).toJson(), StationLocation.class) : null);
				cachedStationList.add(station);
			});

			mongoClient.close();
			return cachedStationList.toArray(new Station[0]);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

}
