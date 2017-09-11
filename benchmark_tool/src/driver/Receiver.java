package driver;

import org.json.JSONObject;

import adapters.DataSourceAdapter;
import adapters.OutputAdapter;

/**
 * @author alex
 *
 */
public class Receiver extends Thread {

	private OutputAdapter feedbackAdapter;

	private DataSourceAdapter inputAdapter;

	private String driverName;

	private String mode;

	private Step step = Step.STOPPED;


	public Receiver(String driverName, String mode, OutputAdapter feedbackAdapter, DataSourceAdapter inputAdapter) {
		this.driverName = driverName;
		this.mode = mode;
		this.feedbackAdapter = feedbackAdapter;
		this.inputAdapter = inputAdapter;
	}

	@Override
	public void run() {

		switch (mode) {
		case "feedback":
			onLineRun();
			break;
		}

	}

	private void onLineRun() {

		try {
			setStep(Step.RUNNING);
			System.out.println("Receiver is running..");
			while (getStep() == Step.RUNNING) {
				
				JSONObject data = inputAdapter.getNextData();
				if (data != null) {
					
					sendEvent(data.put("source", driverName));
					
				}
				Thread.sleep(100);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("stopped..:");
		}

	}


	private void sendEvent(JSONObject event) throws Exception {
		feedbackAdapter.send(event);
	}

	public void stopLoad() {
		setStep(Step.STOPPED);
		// TODO
	}

	private Step getStep() {
		return step;
	}

	private void setStep(Step step) {
		this.step = step;
	}
}
