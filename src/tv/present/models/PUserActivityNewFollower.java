package tv.present.models;

import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Present User Activity New Follower Model
 * June 05, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
public final class PUserActivityNewFollower extends PUserActivity {

    private static final String TAG = "tv.present.factories.PUserActivityNewFollower";
    private static final Logger PLog = Logger.getLogger(TAG);

    /**
     * Constructs a new PUserActivityNewFollower object.
     * @param id is the String ID of the activity.
     * @param subjectiveMeta is the PSubjective meta that describes the activity object.
     * @param creationDate is a Calendar storing the time of creation for this activity.
     * @param lastUpdateDate is a Calendar storing the time of modification for this activity.
     * @param subject is the String subject body of the activity.
     * @param sourceUser is the source PUser object that generated the activity.
     * @param video is the PVideo object that the activity involves.
     * @param targetUserID is the String user ID of the target user for the activity (ie: should be the user with context).
     * @param isUnread is a boolean value indicating whether the activity was read.
     */
    public PUserActivityNewFollower(String id, PSubjectiveMeta subjectiveMeta, Calendar creationDate, Calendar lastUpdateDate, String subject, PUser sourceUser, PVideo video, String targetUserID, boolean isUnread) {
        super(id, subjectiveMeta, creationDate, lastUpdateDate, subject, sourceUser, video, targetUserID, isUnread);
        PLog.info("Constructing PUserActivityNewFollower object with ID: " + id);
    }

}
