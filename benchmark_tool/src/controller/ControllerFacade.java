package controller;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import driver.DriverRESTClient;
import driver.DriverRemoteFunctions;

/**
 * @author alex
 *
 */
public class ControllerFacade {

	/** Singleton instance of the facade. */
	private static ControllerFacade instance;

	/** Driver list. */
	private ArrayList<JSONObject> drivers = new ArrayList<JSONObject>();
	
	/**
	 * Map of remoteDriver's configuration and corresponding
	 * driverRemoteFunctions.
	 */
	private HashMap<JSONObject, DriverRemoteFunctions> remoteDrivers;

	/** Function to get unique instance of ControllerFacade. */
	public static synchronized ControllerFacade getInstance() {
		if (instance == null) {
			instance = new ControllerFacade();
		}
		return instance;
	}

	/** Constructor. */
	private ControllerFacade() {
		remoteDrivers = new HashMap<JSONObject, DriverRemoteFunctions>(1);
	}

	/** Parse .json config file from path. */
	public void parseConfig(String path) throws Exception {

		/** Read .json configuration file as a JSONObject. */
		JSONObject config = new JSONObject(new JSONTokener(new FileReader(path)));

		/** Get JSONArray of drivers by key "drivers". */
		JSONArray driverArray = config.getJSONArray("drivers");

		/** Add all driver's JSONObject config. */
		for (int i = 0; i < driverArray.length(); i++) {
			JSONObject driver = driverArray.getJSONObject(i);
			addDriver(driver);
		}

	}

	/**
	 * Initial remoteDriver as a RESTClient by host ip:port in driverConfig and
	 * load the driverConfig to it.
	 */
	public void loadRemoteDriver(JSONObject driverConfig) throws Exception {

		DriverRemoteFunctions remoteDriver = new DriverRESTClient(driverConfig.getString("host"));

		synchronized (remoteDrivers) {
			remoteDrivers.put(driverConfig, remoteDriver);
		}

		remoteDriver.load(driverConfig);

	}

	public synchronized void addDriver(JSONObject driverConfig) {
		this.remoteDrivers.put(driverConfig, null);
		this.drivers.add(driverConfig);
	}

	public synchronized void startRemoteDriver(JSONObject driverConfig) throws Exception {
		DriverRemoteFunctions remoteDriver = this.lookupRemoteDriver(driverConfig);
		remoteDriver.start();
	}

	public synchronized void pauseRemoteDriver(JSONObject driverConfig) throws Exception {
		DriverRemoteFunctions remoteDriver = this.lookupRemoteDriver(driverConfig);
		remoteDriver.pause();
	}

	public synchronized void stopRemoteDriver(JSONObject driverConfig) throws Exception {
		DriverRemoteFunctions remoteDriver = this.lookupRemoteDriver(driverConfig);
		remoteDriver.stop();
	}

	/** Get DriverRemoteFunctions by corresponding driverConfig. */
	private DriverRemoteFunctions lookupRemoteDriver(JSONObject driverConfig) throws Exception {
		if (remoteDrivers == null || remoteDrivers.isEmpty()) {
			throw new Exception("No remote drivers");
		}
		DriverRemoteFunctions ret = remoteDrivers.get(driverConfig);

		if (ret == null) {
			throw new Exception("Driver not found.");
		} else {
			return ret;
		}
	}

	public synchronized ArrayList<JSONObject> getDriverList() {
		return this.drivers;
	}
}
