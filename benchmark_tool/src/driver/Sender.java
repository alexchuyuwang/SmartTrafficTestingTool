package driver;

import org.json.JSONObject;

import adapters.DataSourceAdapter;
import adapters.OutputAdapter;

/**
 * @author alex
 *
 */
public class Sender extends Thread {

	private OutputAdapter outputAdapter;

	private DataSourceAdapter dataSourceAdapter;

	private Scheduler scheduler;

	private String driverName;

	private String mode;

	private Step step = Step.STOPPED;

	/**
	 * Highest resolution for Thread.sleep() method, in nanoseconds. Configured
	 * value: 1ms.
	 */
	private static final long SLEEP_TIME_RESOLUTION = (long) 1E6;

	public Sender(String driverName, String mode, OutputAdapter outputAdapter, DataSourceAdapter dataSourceAdapter,
			Scheduler scheduler) {
		this.driverName = driverName;
		this.mode = mode;
		this.outputAdapter = outputAdapter;
		this.dataSourceAdapter = dataSourceAdapter;
		this.scheduler = scheduler;
	}

	@Override
	public void run() {

		switch (mode) {
		case "feedback":
			onLineRun();
			break;
		case "scheduled":
			scheduledRun();
			break;
		case "timestamped":
			timestampedRun();
			break;
		}

	}

	private void onLineRun() {

		try {
			setStep(Step.RUNNING);
			System.out.println("running..");
			while (getStep() == Step.RUNNING) {
				JSONObject data = dataSourceAdapter.getNextData();
				if (data != null) {
					sendEvent(data.put("source", driverName));
					//System.out.println(data);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("stopped..:");
		}

	}

	private void scheduledRun() {

		try {
			setStep(Step.RUNNING);
			System.out.println("running..");

			long interTime; // inter-arrival time, in nanoseconds
			long sleepTime; // in milliseconds

			long expectedElapsedTime = 0; // in nanoseconds
			long now;
			long t0 = System.currentTimeMillis();
			long firstTimestamp = t0;
			long scheduledTime = 0;

			while (getStep() == Step.RUNNING) {

				JSONObject data = dataSourceAdapter.getNextData();
				if (data != null) {
					interTime = this.scheduler.getInterArrivalTime();
					expectedElapsedTime += interTime;
					scheduledTime = firstTimestamp + expectedElapsedTime / SLEEP_TIME_RESOLUTION;
					now = System.currentTimeMillis();
					sleepTime = scheduledTime - now;
					if (sleepTime > 0) {
						Thread.sleep(sleepTime);
					}
					sendEvent(data.append("source", driverName));
				}
			}
			System.out.println("Stopped!");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Stopped by exception.");
		}
	}

	private void timestampedRun() {
		// TODO
	}

	private void sendEvent(JSONObject event) throws Exception {
		outputAdapter.send(event);
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
