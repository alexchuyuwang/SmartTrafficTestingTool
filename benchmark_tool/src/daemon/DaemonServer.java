package daemon;

import static spark.Spark.*;

import org.json.JSONObject;

import driver.Driver;

/**
 * 
 * Java REST server by spark framework.
 * 
 * @author alex
 *
 */
public class DaemonServer {

	public static void main(String[] args) {

		final Driver driver = new Driver();

		post("/load_driver", (request, response) -> {

			JSONObject jsonObj = new JSONObject(request.body());

			try {
				driver.load(jsonObj);
			} catch (Exception e) {
				return "Failed to load driver. " + e.getMessage();
			}

			String driverName = driver.getName();
			return driverName + " is loaded. ";

		});

		get("/start_driver", (request, response) -> {

			if (driver.isLoaded()) {
				try {
					driver.start();
				} catch (Exception e) {
					return "Failed to start driver. " + e.getMessage();
				}
				String driverName = driver.getName();
				return driverName + " is Started.";
			} else {
				return "Driver has not been loaded. ";
			}

		});

		get("/stop_driver", (request, response) -> {

			try {
				driver.stop();
			} catch (Exception e) {
				return "Failed to stop driver. " + e.getMessage();
			}

			String driverName = driver.getName();
			return driverName + " is Stoped.";

		});
		
		System.out.println("Ready.");

	}

}
