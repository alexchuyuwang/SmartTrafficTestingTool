package adapters;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONObject;

/**
 * 
 * Using Kafka client API to produce data.
 * 
 * @author alex
 *
 */
public class KafkaOutputAdapter implements OutputAdapter {

	private String kafkaBrokerHost;

	private String topic;

	private KafkaProducer<String, String> producer;

	public KafkaOutputAdapter(String kafkaBrokerHost, String topic) {

		this.kafkaBrokerHost = kafkaBrokerHost;
		this.topic = topic;

		/** Initialize kafka client connection. */
		try {
			Properties props = new Properties();
			props.put("bootstrap.servers", this.kafkaBrokerHost);
			props.put("acks", "all");
			props.put("key.serializer", StringSerializer.class.getName());
			props.put("value.serializer", StringSerializer.class.getName());

			producer = new KafkaProducer<>(props);

		} catch (Exception e) {
			System.err.println("Can't initial kafka client connection!");
		}
	}

	@Override
	public void connect() throws Exception {
	}

	@Override
	public void disconnect() throws Exception {
		producer.close();
	}

	@Override
	public void send(JSONObject value) throws Exception {
		producer.send(new ProducerRecord<String, String>(topic, value.toString()));
	}

	@Override
	public void send(String key, JSONObject value) throws Exception {
		producer.send(new ProducerRecord<String, String>(topic, key, value.toString()));
	}

}
