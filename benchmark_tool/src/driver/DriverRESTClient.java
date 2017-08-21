package driver;

import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

/**
 * 
 * Use REST client to interact with a REST server to call remote driver.
 * 
 * @author alex
 *
 */
public class DriverRESTClient implements DriverRemoteFunctions {

	private Client client;

	private String resourceUrl;

	/** Initial with a REST server's host ip:port. */
	public DriverRESTClient(String host) {
		client = Client.create();
		resourceUrl = "http://" + host;
	}

	/** Call http post method to load remote driver with config as post data at [resourceUrl]/load_driver. */
	@Override
	public void load(JSONObject config) throws Exception {
		
		System.out.print("Load driver at "+resourceUrl+" ... ");
		
		ClientResponse response = client.resource(resourceUrl + "/load_driver").accept("application/json")
				.type("application/json").post(ClientResponse.class, config.toString());

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String output = response.getEntity(String.class);

		System.out.println(output);

	}

	/** Call http get method to start remote driver at [resourceUrl]/start_driver. */
	@Override
	public void start() {
		
		System.out.print("Start driver at "+resourceUrl+" ... ");
		
		ClientResponse response = client.resource(resourceUrl + "/start_driver").accept("application/json")
				.get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String output = response.getEntity(String.class);

		System.out.println(output);

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	/** Call http get method to stop remote driver at [resourceUrl]/stop_driver. */
	@Override
	public void stop() {
		
		System.out.print("Stop driver at "+resourceUrl+" ... ");
		
		ClientResponse response = client.resource(resourceUrl + "/stop_driver").accept("application/json")
				.get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String output = response.getEntity(String.class);

		System.out.println(output);

	}

}
