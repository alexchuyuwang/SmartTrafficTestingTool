package driver;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import adapters.DataSourceAdapter;
import adapters.KafkaInputAdapter;
import adapters.KafkaOutputAdapter;
import adapters.MqttOutputAdapter;
import adapters.OutputAdapter;

/**
 * 
 * Driver:
 * 1. Controlled by remote controller.
 * 2. Initial Sender Thread.
 * 3. Initial DataSourceAdapter to get data from other source.
 * 4. Initial OutputAdapter to send data to destination system.
 * 
 * 
 * @author alex
 *
 */
public class Driver implements DriverRemoteFunctions {

	private OutputAdapter outputAdapter;

	private DataSourceAdapter dataSourceAdapter;
	
	private Scheduler scheduler;

	private JSONObject driverConfig;

	private String name;

	private String mode;

	private Sender sender;

	private boolean loaded = false;

	@Override
	public void load(JSONObject config) throws Exception {

		this.driverConfig = config;

		/** Get configurations. */
		this.name = driverConfig.getString("name");
		this.mode = driverConfig.getString("mode");
		String dataSourceAdapterType = driverConfig.getString("dataSourceAdapterType");
		String outputAdapterType = driverConfig.getString("outputAdapterType");
		JSONObject dataSourceConfig = driverConfig.getJSONObject("dataSourceConfig");
		JSONObject timerConfig = driverConfig.getJSONObject("timerConfig");
		JSONObject outputConfig = driverConfig.getJSONObject("outputConfig");

		/**
		 * Different dataSource adapter type with different configuration
		 * detail.
		 */
		switch (dataSourceAdapterType) {
		case "kafka":
			String kafka_host = dataSourceConfig.getString("kafkaBrokerHost");
			String kafka_group_id = dataSourceConfig.getString("kafkaGroupId");
			String kafka_topic = dataSourceConfig.getString("kafkaTopic");
			List<String> kafka_topics = Arrays.asList(kafka_topic);
			dataSourceAdapter = new KafkaInputAdapter(kafka_host, kafka_group_id, kafka_topics);

			break;
		case "synthetic":
			
			break;
		default:
			return;
		}

		/**
		 * Different output adapter type with different configuration detail.
		 */
		switch (outputAdapterType) {
		case "mqtt":
			String mqtt_host = outputConfig.getString("mqttBrokerHost");
			String mqtt_topic = outputConfig.getString("mqttTopic");
			outputAdapter = new MqttOutputAdapter(mqtt_host, mqtt_topic);

			break;
		case "kafka":
			String kafka_host = outputConfig.getString("kafkaBrokerHost");
			String kafka_topic = outputConfig.getString("kafkaTopic");
			outputAdapter = new KafkaOutputAdapter(kafka_host, kafka_topic);
			break;
		default:
			return;

		}
		
		/**
		 * Different mode with different configuration detail.
		 */
		switch (mode) {
		case "scheduled":
			Long duration=Long.parseLong(timerConfig.getString("duration"));
			double rate=Double.parseDouble(timerConfig.getString("rate"));
			String arrivalProcess=timerConfig.getString("arrivalProcess");
			scheduler = new Scheduler(duration,rate,arrivalProcess);
			break;
		}

		/** Connect to adapters and set loaded. */
		dataSourceAdapter.connect();
		outputAdapter.connect();
		loaded = true;
	}

	/** Initial and start sender. */
	@Override
	public void start() {

		sender = new Sender(name, mode, outputAdapter, dataSourceAdapter, scheduler);
		sender.start();

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	/** Stop sender and close adapter resource. */
	@Override
	public void stop() {

		sender.stopLoad();
		try {
			if (outputAdapter != null) {
				outputAdapter.disconnect();
				outputAdapter = null;
			}

			if (dataSourceAdapter != null) {
				dataSourceAdapter.disconnect();
				dataSourceAdapter = null;
			}
		} catch (Exception e) {
			System.out.println("Can't disconnect adapter. " + e.getMessage());
		}

	}

	public boolean isLoaded() {
		return loaded;
	}

	public String getName() {
		return name;
	}

}
