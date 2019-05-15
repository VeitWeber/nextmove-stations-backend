package net.eclever.stations.backend.servlet.filter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Origins {

	private static final List<String> safeOrigins = Collections.unmodifiableList(Arrays.asList(
			"http://localhost:3000",
			"https://beta.eclever.app",
			"https://eclever.app",
			"https://beta.eclever-fleet.app",
			"https://eclever-fleet.app",
			"https://back.eclever.net",
			"https://eclever-tryout.netlify.com",
			"https://fleet.nextmove.de",
			"https://route.nextmove.de",
			//todo: remove altair
			"electron://altair"
	));

	public static List<String> getSafeOrigins() {
		return Origins.safeOrigins;
	}
}
