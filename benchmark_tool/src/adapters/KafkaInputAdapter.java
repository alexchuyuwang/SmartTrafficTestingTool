package adapters;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.json.JSONObject;

/**
 * 
 * Using Kafka client API to consume data.
 * 
 * @author alex
 *
 */
public class KafkaInputAdapter implements DataSourceAdapter {

	private final KafkaConsumer<String, String> consumer;

	private List<String> topics;

	private ConsumerRecords<String, String> records;

	private Iterator<ConsumerRecord<String, String>> iterator;

	public KafkaInputAdapter(String brokerHost, String groupId, List<String> topics) {

		this.topics = topics;

		Properties props = new Properties();
		props.put("bootstrap.servers", brokerHost);
		props.put("group.id", groupId);
		props.put("auto.offset.reset", "latest");
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", StringDeserializer.class.getName());
		consumer = new KafkaConsumer<>(props);

	}

	@Override
	public void connect() throws Exception {
		consumer.subscribe(topics);
		System.out.println("kafka connected!");
	}

	@Override
	public void disconnect() {
		consumer.close();
	}

	/** Poll data from kafka broker. */
	@Override
	public JSONObject getNextData() throws Exception {
		
		if (iterator!=null&&iterator.hasNext()) {
			return new JSONObject(iterator.next().value());
		}else{
			pollData();
			return null;
		}
		
	}

	private void pollData() {
		records = consumer.poll(Long.MAX_VALUE);
		iterator = records.iterator();
	}

}
