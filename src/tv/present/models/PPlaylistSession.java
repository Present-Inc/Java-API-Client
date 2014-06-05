package tv.present.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Present Playlist Session Model
 * June 05, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
public final class PPlaylistSession extends PObject {

    private boolean shouldFinish;
    private boolean isFinished;
    private boolean shouldBeAvailable;
    private boolean isAvailable;
    private int windowLength;
    private int maxDuration;
    private ArrayList<PMediaSegment> mediaSegments;

    /**
     * Constructs a PPlaylistSession object.
     * @param id is the String ID of the object.
     * @param mediaSegments is an ArrayList<PMediaSegment> of media segments in this session.
     * @param shouldFinish is a boolean.
     * @param isFinished is a boolean.
     * @param shouldBeAvailable is a boolean.
     * @param isAvailable is a boolean.
     * @param windowLength is an integer.
     * @param maxDuration is an integer that is the max duration in seconds.
     */
    public PPlaylistSession(final String id, final ArrayList<PMediaSegment> mediaSegments, final boolean shouldFinish, final boolean isFinished, final boolean shouldBeAvailable, final boolean isAvailable, final int windowLength, final int maxDuration) {
        super(id, null, null, null);
        this.mediaSegments = mediaSegments;
        this.shouldFinish  = shouldFinish;
        this.isFinished = isFinished;
        this.shouldBeAvailable = shouldBeAvailable;
        this.isAvailable = isAvailable;
        this.windowLength = windowLength;
        this.maxDuration = maxDuration;
    }

    /**
     * Converts this object to JSON.
     * @return a JSONObject that represents this object.
     */
    public final JSONObject toJSON() {

        JSONObject playlistSessionConfigJSON = new JSONObject();
        playlistSessionConfigJSON.put("targetDuration", this.maxDuration);
        playlistSessionConfigJSON.put("windowLength", this.windowLength);

        JSONObject playlistSessionMetaJSON = new JSONObject();
        playlistSessionMetaJSON.put("id", this.id);
        playlistSessionMetaJSON.put("shouldFinish", this.shouldFinish);
        playlistSessionMetaJSON.put("isFinished", this.isFinished);
        playlistSessionMetaJSON.put("shouldBeAvailable", this.shouldBeAvailable);
        playlistSessionMetaJSON.put("isAvailable", this.isAvailable);

        JSONArray playlistSessionMediaSegmentsArrayJSON = new JSONArray();

        for (PMediaSegment mediaSegment : this.mediaSegments) {

            JSONObject mediaSegmentJSON = new JSONObject();

            mediaSegmentJSON.put("mediaSequence", mediaSegment.getSequence());
            mediaSegmentJSON.put("discontinuitySequence", mediaSegment.getDiscontinuitySequence());
            mediaSegmentJSON.put("timeElapsed", mediaSegment.getTimeElapsed());
            mediaSegmentJSON.put("duration", mediaSegment.getDuration());

            playlistSessionMediaSegmentsArrayJSON.put(mediaSegmentJSON);

        }

        JSONObject playlistSessionJSON = new JSONObject();
        playlistSessionJSON.put("mediaSegments", playlistSessionMediaSegmentsArrayJSON);
        playlistSessionJSON.put("config", playlistSessionConfigJSON);
        playlistSessionJSON.put("meta", playlistSessionMetaJSON);

        return playlistSessionJSON;

    }

    /**
     * Gets should finish.
     * @return true if should finish, false otherwise.
     */
    @SuppressWarnings("unused")
    public final boolean isShouldFinish() {
        return this.shouldFinish;
    }

    /**
     * Gets finished.
     * @return true if finished, false otherwise.
     */
    @SuppressWarnings("unused")
    public final boolean isFinished() {
        return this.isFinished;
    }

    /**
     * Gets if the session should be available.
     * @return true if the session should be available, false otherwise.
     */
    @SuppressWarnings("unused")
    public final boolean isShouldBeAvailable() {
        return this.shouldBeAvailable;
    }

    /**
     * Gets if the session is available.
     * @return true if the session is available, false otherwise.
     */
    @SuppressWarnings("unused")
    public final boolean isAvailable() {
        return this.isAvailable;
    }

    /**
     * Gets the window length.
     * @return the window length as an integer.
     */
    @SuppressWarnings("unused")
    public final int getWindowLength() {
        return this.windowLength;
    }

    /**
     * Gets the max duration of the session.
     * @return the max duration as an integer.
     */
    @SuppressWarnings("unused")
    public final int getMaxDuration() {
        return this.maxDuration;
    }

    /**
     * Gets the array of media segments that make up this session.
     * @return an ArrayList<PMediaSegment> of media segments that make up this session.
     */
    @SuppressWarnings("unused")
    public final ArrayList<PMediaSegment> getMediaSegments() {
        return this.mediaSegments;
    }

    /**
     * Gets the number of PMediaSegments that make up this playlist session.
     * @return the integer number of PMediaSegments that make up this playlist session.
     */
    public final int getNumMediaSegments() {
        return this.mediaSegments.size();
    }

}
