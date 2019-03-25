package net.eclever.stations.backend.domain;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.java.Log;
import net.eclever.stations.backend.Environment;
import org.bson.Document;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.function.Consumer;

import static com.mongodb.client.model.Sorts.ascending;

/**
 * @author Veit Weber, , $(DATE)
 */
@Startup
@Singleton
@Log
public class StationService {

	@Inject
	StationRepository stationRepository;

	@PostConstruct
	void init() {
		this.refreshValues();
	}

	@Schedule(minute = "*/15", hour = "*", second = "*", persistent = false)
	public void refreshValues() {
		LinkedList<Station> cachedStationList = new LinkedList<>();
		try {
			log.info("[+] Get Stations");
			Gson gson = new Gson();

			MongoClientURI uri = new MongoClientURI(System.getenv("STATION_MONGODB_URI"));
			MongoClient mongoClient = new MongoClient(uri);

			MongoDatabase mongoDatabase = mongoClient.getDatabase(Environment.MongoDbProperties.DB_NAME);
			MongoCollection collection = mongoDatabase.getCollection(Environment.MongoDbProperties.COLLECTION_NAME);

			FindIterable mongoCollection = collection
					.find()
					.sort(ascending("name"));

//			MongoCursor<Document> cursor = mongoCollection.iterator();
//			while (cursor.hasNext()) {
//				Document document = cursor.next();
//
//				Station station =
//						new Station(document.get("_id").toString(), document.getString("author"), document.getString("name"), document.getString("operator"),
//								document.get("address") != null ? gson.fromJson(((Document) document.get("address")).toJson(), StationAddress.class) : null,
//								document.get("coordinates") != null ? gson.fromJson(((Document) document.get("coordinates")).toJson(), StationLocation.class) : null,
//								document.get("approach") != null ? gson.fromJson(((Document) document.get("approach")).toJson(), StationLocation.class) : null);
//				cachedStationList.add(station);
//			}

			mongoCollection.forEach((Consumer<Document>) document -> {
				Station station =
						new Station(document.get("_id").toString(), document.getString("author"), document.getString("name"), document.getString("operator"),
								document.get("address") != null ? gson.fromJson(((Document) document.get("address")).toJson(), StationAddress.class) : null,
								document.get("coordinates") != null ? gson.fromJson(((Document) document.get("coordinates")).toJson(), StationLocation.class) : null,
								document.get("approach") != null ? gson.fromJson(((Document) document.get("approach")).toJson(), StationLocation.class) : null);
				cachedStationList.add(station);
			});

			mongoClient.close();
			this.stationRepository.setCachedStations(cachedStationList.toArray(new Station[0]));

			log.info("[x] Get Stations");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
