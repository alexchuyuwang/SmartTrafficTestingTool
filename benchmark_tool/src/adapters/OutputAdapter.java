package adapters;

import org.json.JSONObject;

/**
 * 
 * Interface to interact with output adapter.
 * 
 * @author alex
 *
 */
public interface OutputAdapter {

	void connect() throws Exception;
	
	void disconnect() throws Exception;
	
	void send(JSONObject value) throws Exception;
	
	/** Send value with specific key. */
	void send(String key,JSONObject value) throws Exception;
}
