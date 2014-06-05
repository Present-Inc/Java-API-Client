package tv.present.models;

import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Present Comment Model
 * June 05, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
@SuppressWarnings("unused")
public final class PComment extends PObject {

    private static final String TAG = "tv.present.models.PComment";
    private static final Logger PLog = Logger.getLogger(TAG);

    private final PVideo targetVideo;
    private final PUser sourceUser;
    private String body;

    /**
     * Constructs a PComment object.
     * @param id is the String ID of the comment.
     * @param body is the String body of the comment.
     * @param target is the PVideo that the comment is created under.
     * @param source is the PUser who created the comment.
     * @param creationDate is the time of creation as a Java Calendar.
     * @param lastUpdateDate is the time of last update as a Java Calendar.
     */
    public PComment(final String id, final String body, final PVideo target, final PUser source, final Calendar creationDate, final Calendar lastUpdateDate) {
        super(id, null, creationDate, lastUpdateDate);
        this.body = body;
        this.targetVideo = target;
        this.sourceUser = source;
        PLog.info("Constructing PComment object with ID: " + id);
    }

    /**
     * Gets the body of the comment.
     * @return the body of the comment as a String.
     */
    public final String getBody() {
        return this.body;
    }

    /**
     * Gets the user who made the comment.
     * @return the User who made the comment.
     */
    public final PUser getSourceUser() {
        return this.sourceUser;
    }

    /**
     * Gets the video that this comment was made on.
     * @return the Video this comment was made on.
     */
    public final PVideo getTargetVideo() {
        return this.targetVideo;
    }

}