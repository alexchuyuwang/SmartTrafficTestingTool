package adapters;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.json.JSONObject;

/**
 * 
 * Using Paho Mqtt client to publish data.
 * 
 * @author alex
 *
 */
public class MqttOutputAdapter implements OutputAdapter {

	private MqttAsyncClient mqttAsyncClient;

	private String mqttBrokerHost;

	private String topic;

	public MqttOutputAdapter(String host, String topic) {

		this.topic = topic;

		/** Initialize mqtt client connection. */
		try {
			this.mqttBrokerHost = host;
			this.mqttAsyncClient = new MqttAsyncClient(mqttBrokerHost, MqttAsyncClient.generateClientId(),null);
			
		} catch (Exception e) {
			System.err.println("Can't initial mqtt client connection!");
		}
	}

	@Override
	public void connect() throws Exception {
		try {
			if (!mqttAsyncClient.isConnected()) {
				mqttAsyncClient.connect();
			}
		} catch (Exception e) {
			System.err.println("Can't connect to mqtt broker!");
		}

	}

	@Override
	public void disconnect() throws Exception {
		try {
			if (mqttAsyncClient.isConnected()) {
				mqttAsyncClient.disconnect();
			}
			mqttAsyncClient.close();
		} catch (Exception e) {
			System.err.println("Can't disconnect from mqtt broker!");
		}

	}

	@Override
	public void send(String key, JSONObject value) throws Exception {
		send(value);
	}

	@Override
	public void send(JSONObject value) throws Exception {
			this.mqttAsyncClient.publish(topic,value.toString().getBytes(),0,false);
	}

}
