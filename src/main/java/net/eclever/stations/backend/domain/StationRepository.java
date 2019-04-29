package net.eclever.stations.backend.domain;

import com.google.common.base.Throwables;
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
import java.util.*;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Sorts.ascending;


@ApplicationScoped
@Data
@Log
public class StationRepository {

	private Station[] cachedStations = new Station[0];


	public Station[] getStationsSinceLastUpdate(Date lastUpdate) {
		LinkedList<Station> cachedStationList = new LinkedList<>();
		try {
			MongoClientURI uri = new MongoClientURI(System.getenv("STATION_MONGODB_URI"));
			MongoClient mongoClient = new MongoClient(uri);

			MongoDatabase mongoDatabase = mongoClient.getDatabase(Environment.MongoDbProperties.DB_NAME);
			MongoCollection collection = mongoDatabase.getCollection(Environment.MongoDbProperties.COLLECTION_NAME);

			FindIterable mongoCollection = collection
					.find(or(gte("createdat", lastUpdate), gte("editedat", lastUpdate)))
					.sort(ascending("name"));


			mongoCollection.forEach((Consumer<Document>) document -> {
				Station station = createStationFromDoc(document);
				if (station != null)
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
				Station station = createStationFromDoc(document);
				if (station != null)
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
				Station station = createStationFromDoc(document);
				if (station != null)
					cachedStationList.add(station);
			});

			mongoClient.close();
			return cachedStationList.toArray(new Station[0]);

		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(ex));
			throw ex;
		}
	}


	static Station createStationFromDoc(Document document) {
		try {
			Gson gson = new Gson();
			ArrayList<Chargepoint> chargepoints = new ArrayList<>();
			document.get("chargepoints", ArrayList.class).forEach(chargepoint -> {
				Document chargepointDoc = (Document) chargepoint;
				chargepoints.add(new Chargepoint(chargepointDoc.getString("id"), chargepointDoc.getBoolean("private"), chargepointDoc.getInteger("phases"), chargepointDoc.getInteger("power"),
						chargepointDoc.get("type") != null ? ((Document) chargepointDoc.get("type")).getString("id") : null, chargepointDoc.getString("status"), chargepointDoc.getString("problem"),
						chargepointDoc.getDouble("ampere"), chargepointDoc.getDouble("volt"), chargepointDoc.getString("plugCable"),
						chargepointDoc.getBoolean("pricingFree"), chargepointDoc.getString("pricingType"), chargepointDoc.getString("pricingValue")));
			});

			return new Station(document.get("_id").toString(), document.getString("author"), document.getString("name"), document.getString("operator"),
					document.get("address") != null ? gson.fromJson(((Document) document.get("address")).toJson(), StationAddress.class) : null,
					document.get("coordinates") != null ? gson.fromJson(((Document) document.get("coordinates")).toJson(), Location.class) : null,
					document.get("approach") != null ? gson.fromJson(((Document) document.get("approach")).toJson(), Location.class) : null,
					document.get("createdat") != null ? document.getDate("createdat").getTime() : null,
					document.get("editedat") != null ? document.getDate("editedat").getTime() : null,
					document.getString("approachDescription"),
					document.getString("manufacturer"),
					document.get("chargepoints") != null ? chargepoints : null);
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(ex));
		}
		return null;
	}
}
