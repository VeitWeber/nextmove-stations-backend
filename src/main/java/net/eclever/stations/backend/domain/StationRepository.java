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
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.function.Consumer;

import static com.cloud.mongo.MongoTools.*;
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
			MongoClientURI uri = new MongoClientURI(Environment.MongoDbProperties.DB_URI);
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
			MongoClientURI uri = new MongoClientURI(Environment.MongoDbProperties.DB_URI);
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

			MongoClientURI uri = new MongoClientURI(Environment.MongoDbProperties.DB_URI);
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
//			ArrayList<Chargepoint> chargepoints = new ArrayList<>();
//			document.get("chargepoints", ArrayList.class).forEach(chargepoint -> {
//				Document chargepointDoc = (Document) chargepoint;
//				Pricing pricing = new Pricing(
//						chargepointDoc.get("pricing") != null ? ((Document) chargepointDoc.get("pricing")).getBoolean("free") : null,
//						chargepointDoc.get("pricing") != null ? ((Document) chargepointDoc.get("pricing")).getString("type") : null,
//						chargepointDoc.get("pricing") != null ? ((Document) chargepointDoc.get("pricing")).getString("value") : null);
//				chargepoints.add(new Chargepoint(
//						chargepointDoc.getString("id"),
//						chargepointDoc.getBoolean("restricted"),
//						chargepointDoc.getInteger("phases"),
//						chargepointDoc.get("power") == null ? null : Double.valueOf(chargepointDoc.get("power").toString()),
//						chargepointDoc.get("type") != null ? ((Document) chargepointDoc.get("type")).getObjectId("id").toString() : null,
//						chargepointDoc.getString("status"),
//						chargepointDoc.getString("problem"),
//						chargepointDoc.get("ampere") == null ? null : Double.valueOf(chargepointDoc.get("ampere").toString()),
//						chargepointDoc.get("volt") == null ? null : Double.valueOf(chargepointDoc.get("volt").toString()),
//						chargepointDoc.getString("plugCable"),
//						pricing));
//
//			});

			StationAddress address = null;
			try {
				address = document.get("address") != null ? gson.fromJson(((Document) document.get("address")).toJson(), StationAddress.class) : null;
			} catch (Exception ex) {
				log.severe("Error while casting field 'address' in Document '" + document.get("_id").toString() +
						"' to StationAddress. \n" + Throwables.getRootCause(ex).getMessage());
			}
			Location coordinates = null;
			try {
				coordinates = document.get("coordinates") != null ? gson.fromJson(((Document) document.get("coordinates")).toJson(), Location.class) : null;
			} catch (Exception ex) {
				log.severe("Error while casting field 'coordinates' in Document '" + document.get("_id").toString() +
						"' to Location. \n" + Throwables.getRootCause(ex).getMessage());
			}
			Location approach = null;
			try {
				approach = document.get("approach") != null ? gson.fromJson(((Document) document.get("approach")).toJson(), Location.class) : null;
			} catch (Exception ex) {
				log.severe("Error while casting field 'approach' in Document '" + document.get("_id").toString() +
						"' to Location. \n" + Throwables.getRootCause(ex).getMessage());
			}

			Station station = new Station(
					document.get("_id").toString(),
					getStringFromDoc(document, document, "name"),
					getStringFromDoc(document, document, "author"),
					getListFromDoc(document, document, "allowed", "String"),
					getDateFromDoc(document, document, "createdat"),
					getDateFromDoc(document, document, "editedat"),
					address, coordinates, approach,
					getStringFromDoc(document, document, "approachDescription"),
					getStringFromDoc(document, document, "network"),
					getStringFromDoc(document, document, "operator"),
					getBooleanFromDoc(document, document, "restricted"),
					getBooleanFromDoc(document, document, "verified"),
					getBooleanFromDoc(document, document, "predelete"),
					getStringFromDoc(document, document, "description"),
					getListFromDoc(document, document, "images", "String")
			);

			return station;
		} catch (Exception ex) {
			log.severe(Throwables.getStackTraceAsString(ex));
		}
		return null;
	}
}
