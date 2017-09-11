package controller;

import java.util.ArrayList;

import org.json.JSONObject;

/**
 * @author alex
 *
 */
public class configurationTool {

	/** Facade instance to interact with lots of controller functions. */
	static ControllerFacade facade;

	public static void main(String[] args) {

		/** Check arguments length value. */
        if(args.length != 1){
           System.out.println("usage:<*.json config path>");
           return;
        }
        
		/** Get facade instance to call controller function. */
		facade = ControllerFacade.getInstance();

		/** Parse arg[0] as .json config path. */
		try {
			facade.parseConfig(args[0]);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// for simple test, just load and start the drivers yet.
		loadDriver();
		startDriver();

	}

	/** Get driver list and load remote driver. */
	static void loadDriver() {
		ArrayList<JSONObject> driverList = facade.getDriverList();
		for (int i = 0; i < driverList.size(); i++) {
			JSONObject driver = driverList.get(i);
			try {
				facade.loadRemoteDriver(driver);
			} catch (Exception e) {
				System.out.println("Error while loading " + driver.getString("name") + " at " + driver.getString("host")
						+ ". " + e.getMessage());
			}
		}
	}

	/** Get driver list and start remote driver. */
	static void startDriver() {
		ArrayList<JSONObject> driverList = facade.getDriverList();
		for (int i = 0; i < driverList.size(); i++) {
			JSONObject driver = driverList.get(i);
			try {
				facade.startRemoteDriver(driver);
			} catch (Exception e) {
				System.out.println("Error while starting " + driver.getString("name") + " at "
						+ driver.getString("host") + ". " + e.getMessage());
			}

		}
	}
}
