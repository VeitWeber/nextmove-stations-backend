package net.eclever.stations.backend.domain;

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
			MongoClientURI uri = new MongoClientURI(Environment.MongoDbProperties.DB_URI);
			MongoClient mongoClient = new MongoClient(uri);

			MongoDatabase mongoDatabase = mongoClient.getDatabase(Environment.MongoDbProperties.DB_NAME);
			MongoCollection collection = mongoDatabase.getCollection(Environment.MongoDbProperties.COLLECTION_NAME);

			FindIterable mongoCollection = collection
					.find()
					.limit(5000)
					.sort(ascending("name"));

			mongoCollection.forEach((Consumer<Document>) document -> {
				Station station = StationRepository.createStationFromDoc(document);
				if (station != null)
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
