package adapters;

import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class MqttInputAdapter implements DataSourceAdapter, MqttCallback {
	private MqttClient Client;

	private String broker;

	private String topic;
	
	private Queue<JSONObject> events = new LinkedList<>();

	// initialize
	public MqttInputAdapter(String broker, String topic) throws MqttException {
		this.broker = broker;
		this.topic = topic;
		this.Client = new MqttClient(broker, MqttClient.generateClientId());
	}

	// disconnect
	public void disconnect() {
		try {
			this.Client.disconnect();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// callback for messageArrived
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {

		JSONObject event = new JSONObject(new String(message.getPayload()));
		events.offer(event);

	}

	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub

	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void connect() throws Exception {
		this.Client.connect();
		this.Client.setCallback(this);
		this.Client.subscribe(topic);

	}

	@Override
	public JSONObject getNextData() throws Exception {
		if (!events.isEmpty()) {
			return events.poll();
		}else{
			return null;
		}
	}

}