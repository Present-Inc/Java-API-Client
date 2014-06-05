package tv.present.models;

import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Present User Context Model
 * June 05, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
public class PUserContext extends PObject {

    private static final String TAG = "tv.present.factories.PUserContext";
    private static final Logger PLog = Logger.getLogger(TAG);

    private PUser user;
    private final String sessionToken;

    /**
     * Constructs a PUserContext object.
     * @param id is the String ID of the user context.
     * @param creationDate is the creation date as a Calendar.
     * @param lastUpdateDate is the last update date as a Calendar.
     * @param sessionToken is the session token as a String.
     * @param user is a PUser.
     */
    public PUserContext(String id, Calendar creationDate, Calendar lastUpdateDate, String sessionToken, PUser user) {
        super(id, null, creationDate, lastUpdateDate);
        this.sessionToken = sessionToken;
        this.user = user;
        PLog.info("Construction PUserContext with ID: " + id + " & session token: " + sessionToken);
    }

    /**
     * Gets the ID of the user.
     * @return the ID of the user as a String.
     */
    public String getUserID() {
        return this.user.getID();
    }

    /**
     * Gets the user session token.
     * @return the session token as a string.
     */
    public String getSessionToken() {
        return this.sessionToken;
    }

    /**
     * Gets the user object.
     * @return the user as a PUser object.
     */
    public PUser getUser() {
        return this.user;
    }

    /**
     * Sets the user.
     * @param user is the PUser to set.
     */
    public void setUser(PUser user) {
        this.user = user;
    }

}