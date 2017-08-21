package adapters;

import org.json.JSONObject;

/**
 * 
 * Interface to interact with data source adapter.
 * 
 * @author alex
 *
 */
public interface DataSourceAdapter {

	void connect() throws Exception;

	void disconnect();
	
	/** Set return type to Object temporarily. */
	JSONObject getNextData() throws Exception;
}
