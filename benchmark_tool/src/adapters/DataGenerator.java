package adapters;


import java.util.Iterator;
import java.util.Random;

import org.json.JSONObject;

/**
 * 
 * 
 * 
 * @author alex
 *
 */
public class DataGenerator implements DataSourceAdapter {

	private JSONObject eventFormat;

	 /** Random number generator. */
    private final Random rnd;
    
	public DataGenerator(JSONObject eventFormat) {
		
		this.eventFormat=eventFormat;
		this.rnd = new Random();
	}

	@Override
	public void connect() throws Exception {
		
	}

	@Override
	public void disconnect() {
		
	}

	
	@Override
	public JSONObject getNextData() throws Exception {
		JSONObject nextData=new JSONObject();
		Iterator<String> temp = eventFormat.keys();
        while (temp.hasNext()) {
            String attribute = temp.next();
            JSONObject domain = eventFormat.getJSONObject(attribute);
            
            nextData.put(attribute, randomNumberGenerator(domain.getString("type"),domain.getString("lower"),domain.getString("upper")));
        }
	return nextData;	
	}
	
	private String randomNumberGenerator(String type,String lower,String upper){
		String value="";
		
		if(type.equals("double")){
			value+=(Double.parseDouble(lower) + rnd.nextDouble() * (Double.parseDouble(upper) - Double.parseDouble(lower)));
		}else if(type.equals("integer")){
			value+=(int)(Double.parseDouble(lower) + rnd.nextDouble() * (Double.parseDouble(upper) - Double.parseDouble(lower)));
		}
		
		return value;
	}


}
