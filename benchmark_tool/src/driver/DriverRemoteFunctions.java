package driver;

import org.json.JSONObject;

/**
 * 
 * Interface to interact with a remote driver.
 * 
 * @author alex
 *
 */
public interface DriverRemoteFunctions {

	void load(JSONObject config) throws Exception;

	void start();

	void pause();

	void stop();

}
