package tv.present.models;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Present Video Model
 * June 05, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
public class PVideo extends PObject {

    private static final String TAG = "tv.present.factories.PVideo";
    private static final Logger PLog = Logger.getLogger(TAG);

    protected ArrayList<PComment> comments = new ArrayList<PComment>();
    protected boolean isAvailable;
    protected Calendar creationStart;
    protected Calendar creationEnd;
    protected PUser creatorUser;
    private String title;
    protected int likes;
    protected int views;
    protected URL stillImage;
    private URL replay;
    private URL live;
    protected PVisibility visibility;

    /**
     * Constructs a PVideo object.
     * @param id is the String ID of the video.
     * @param subjectiveMeta is PSubjectiveMeta for the video.
     * @param creationDate is the creation date as a Calendar.
     * @param lastUpdateDate is the last update date as a Calendar.
     * @param title is the title of the video as a String.
     * @param creatorUser is the PUser who created the video.
     * @param stillImage is the URL for a still image for this video.
     * @param live is the URL for a live stream to this video.
     * @param replay is the URL for a replay stream to this video.
     * @param numLikes is the integer number of likes.
     * @param numViews os the integer number of views.
     * @param isAvailable is a boolean.
     * @param visibility is a PVisibility object.
     * @param comments is an ArrayList<PComment> of comments.
     * @param creationStart is the creation start time as a Calendar.
     * @param creationEnd is the creation end time as a Calendar.
     */
    public PVideo(String id, PSubjectiveMeta subjectiveMeta, Calendar creationDate, Calendar lastUpdateDate, String title, PUser creatorUser, URL stillImage, URL live, URL replay, int numLikes, int numViews, boolean isAvailable, PVisibility visibility, ArrayList<PComment> comments, Calendar creationStart, Calendar creationEnd) {
        super(id, subjectiveMeta, creationDate, lastUpdateDate);
        this.title = title;
        this.creatorUser = creatorUser;
        this.stillImage = stillImage;
        this.live = live;
        this.replay = replay;
        this.likes = numLikes;
        this.views = numViews;
        this.isAvailable = isAvailable;
        this.visibility = visibility;
        this.comments = comments;
        this.creationStart = creationStart;
        this.creationEnd = creationEnd;
        PLog.info("Constructing PVideo object with ID: " + id);
    }

    /**
     * Gets the title of the video.
     * @return the title of the video as a String.
     */
    @SuppressWarnings("unused")
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets the live link for the video.
     * @return the live link for this video as a String.
     */
    @SuppressWarnings("unused")
    public String getLiveLink() {
        return this.live.toString();
    }

    /**
     * Gets the replay link for the video.
     * @return the replay link for this video as a String.
     */
    @SuppressWarnings("unused")
    public String getReplayLink() {
        if(replay != null) {
            return this.replay.toString();
        }
        else {
            return null;
        }

    }

}
