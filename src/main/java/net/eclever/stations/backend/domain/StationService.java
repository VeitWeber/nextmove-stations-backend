package net.eclever.stations.backend.domain;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.java.Log;
import net.eclever.stations.backend.Environment;
import org.bson.Document;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.Vector;
import java.util.function.Consumer;

import static com.mongodb.client.model.Sorts.ascending;

/**
 * @author Veit Weber, , $(DATE)
 */
@Singleton
@Log
public class StationService {

	@Inject
	StationRepository stationRepository;

	@Schedule(minute = "*", hour = "*", second = "*/40", persistent = false)
	private void refreshValues() {
		Vector<Station> cachedStationList = new Vector<>();
		try {
			log.info("[+] Get Stations");

			MongoClientURI uri = new MongoClientURI(System.getenv("MONGODB_URI"));
			MongoClient mongoClient = new MongoClient(uri);

			MongoDatabase mongoDatabase = mongoClient.getDatabase(Environment.MongoDbProperties.DB_NAME);
			MongoCollection collection = mongoDatabase.getCollection(Environment.MongoDbProperties.COLLECTION_NAME);

			FindIterable mongoCollection = collection
					.find()
					.sort(ascending("name"));

			mongoCollection.forEach((Consumer<Document>) document -> {
				cachedStationList.add(new Station(document.get("_id").toString(), document.getString("name")));
			});

			mongoClient.close();
			this.stationRepository.setCachedStations(cachedStationList);

			log.info("[x] Get Stations");


		} catch (Exception ex) {
			ex.printStackTrace();
		}


	}
}
