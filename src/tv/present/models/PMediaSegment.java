package tv.present.models;

import java.util.logging.Logger;

/**
 * Present Media Segment Model
 * June 05, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
public final class PMediaSegment extends PObject {

    private static final String TAG = "tv.present.models.PMediaSegment";
    private static final Logger PLog = Logger.getLogger(TAG);

    private int sequence = 0;
    private int discontinuitySequence = 0;
    private double timeElapsed = 0;
    private double duration = 0;

    /**
     * Constructs a PMediaSegment object.
     * @param sequence is the sequence number of this segment as an integer.
     * @param discontinuitySequence is an integer.
     * @param timeElapsed is a double.
     * @param duration is a double.
     */
    public PMediaSegment(final int sequence, final int discontinuitySequence, final double timeElapsed, final double duration) {
        super(null, null, null, null);
        this.sequence = sequence;
        this.discontinuitySequence = discontinuitySequence;
        this.timeElapsed = timeElapsed;
        this.duration = duration;
        PLog.info("Constructing MediaSegment sequence " + sequence);
    }

    /**
     * Gets the sequence number.
     * @return the sequence number as an integer.
     */
    public final int getSequence() {
        return this.sequence;
    }

    /**
     * Gets the discontinuity sequence.
     * @return the discontinuity sequence as an integer.
     */
    public final int getDiscontinuitySequence() {
        return this.discontinuitySequence;
    }

    /**
     * Gets the time elapsed.
     * @return the time elapsed as a double.
     */
    public final double getTimeElapsed() {
        return this.timeElapsed;
    }

    /**
     * Get the duration of the segment.
     * @return the duration of the segment as a double.
     */
    public final double getDuration() {
        return this.duration;
    }

}
