package driver;

import java.util.Random;


/**
 * 
 * TODO
 * 
 * @author alex
 *
 */

public final class Scheduler {

    /** The event submission rate (in events/nanosec). */
    private double rate;

    /** The arrival process of events (either deterministic or Poisson process). */
    private String arrivalProcess;

    /** Total test duration, in nanoseconds. */
    private long duration;

    /** The "current time" in the scheduled simulation (in nanoseconds). */
    private long currentTime = 0;

    /** A multiplier factor, used to control event submission speed. */
    private double rateFactor = 1.0;

    /** Random number generator. */
    private final Random rnd;


    public Scheduler(Long duration, Double rate, String arrivalProcess) {
        this.duration=duration;
        this.rate=rate/ 1E9;
        this.arrivalProcess=arrivalProcess;
        this.rnd = new Random();
        
    }



    /**
     * Gets the inter-arrival times of events
     * If the arrival process is DETERMINISTIC, T = 1/X.
     * If the arrival process is POISSON, T = -ln(U)/X.
     *
     * @return  The time in nanoseconds
     */
    public long getInterArrivalTime() {
        switch (this.arrivalProcess) {
        case "DETERMINISTIC":
            return this.getDeterministicInterArrivalTime();
        case "POISSON":
            return this.getPoissonInterArrivalTime();
        default:
            return this.getDeterministicInterArrivalTime();
        }
    }

    /**
     * Gets the inter-arrival times of events
     * (deterministic: T = 1/X).
     *
     * @return  The time in nanoseconds
     */
    private long getDeterministicInterArrivalTime() {
        long interTime = Math.round(1 / this.getRate(this.currentTime));
        this.currentTime = this.currentTime + interTime;
        return interTime;
    }

    /**
     * Gets the inter-arrival times of events
     * (Poisson Process: inter-arrival times exponentially distributed).
     *
     * @return The time in nanoseconds
     */
    private long getPoissonInterArrivalTime() {
        long interTime = Math.round(
                -Math.log(rnd.nextDouble())
                /
                this.getRate(this.currentTime)
        );
        this.currentTime = this.currentTime + interTime;
        return interTime;
    }


    private double getRate(long t) {
        return this.rateFactor * (this.rate);
    }

}
