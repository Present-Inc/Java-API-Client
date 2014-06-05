package tv.present.models;

import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Present Demand Model
 * June 05, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
public final class PDemand extends PObject {

    private static final String TAG = "tv.present.models.PDemand";
    private static final Logger PLog = Logger.getLogger(TAG);

    private PUser sourceUser;
    private PUser targetUser;

    /**
     * Constructs a demand object.
     * @param id is the ID of the demand object.
     * @param sourceUser is the source PUser that created the demand.
     * @param targetUser is the target PUser that receives the demand.
     * @param creationDate is the Calendar creation time of the demand.
     * @param lastUpdateDate is the Calendar last update time of the demand.
     */
    public PDemand(final String id, final PSubjectiveMeta subjectiveMeta, final PUser sourceUser, final PUser targetUser, final Calendar creationDate, final Calendar lastUpdateDate) {
        super(id, subjectiveMeta, creationDate, lastUpdateDate);
        this.sourceUser = sourceUser;
        this.targetUser = targetUser;
        PLog.info("Creating demand with ID: " + id);
    }

    /**
     * Gets the user who created this demand.
     * @return the User who created this demand.
     */
    public final PUser getSourceUser() {
        return this.sourceUser;
    }

    /**
     * Gets the user for whom this demand is targeted at.
     * @return the User for whom this demand is targeted at.
     */
    @SuppressWarnings("unused")
    public final PUser getTargetUser() {
        return this.targetUser;
    }

}
