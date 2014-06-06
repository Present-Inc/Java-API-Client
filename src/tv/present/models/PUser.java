package tv.present.models;

import tv.present.enumerations.PGender;

import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Present User Model
 * June 05, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
public class PUser extends PObject {

    private static final String TAG = "tv.present.factories.PUser";
    private static final Logger PLog = Logger.getLogger(TAG);

    private Integer numDemands = 0;
    private Integer numFollowers = 0;
    private Integer numFriends = 0;
    private Integer numLikes = 0;
    private Integer numVideos = 0;
    private Integer numViews = 0;
    private String emailAddress = null;
    private final String username;
    private String vanityUsername = null;
    private PUserProfile profile = null;

    /**
     * Constructs a user object with a given ID, username, and profile.
     * @param id is the ID of the user as a String.
     * @param username is the username as a String.
     */
    public PUser(final String id, final String username, final PUserProfile profile) {
        super(id, null, null, null);
        this.username = username;
        this.profile = profile;
        PLog.info("Constructing PUser object with ID: " + id + " and username: " + username);
    }

    /**
     * Constructs a user object with a given ID, username, and profile.
     * @param id is the ID of the user as a String.
     * @param username is the username as a String.
     */
    public PUser(final String id, final PSubjectiveMeta subjectiveMeta, final Calendar creationDate, final Calendar lastUpdateDate, final String username, final String vanityUsername, final String emailAddress, final PUserProfile profile, Integer numDemands, Integer numFollowers, Integer numFriends, Integer numLikes, Integer numVideos, Integer numViews) {
        super(id, subjectiveMeta, creationDate, lastUpdateDate);
        this.username = username;
        this.vanityUsername = vanityUsername;
        this.emailAddress = emailAddress;
        this.profile = profile;
        this.numDemands = numDemands;
        this.numFollowers = numFollowers;
        this.numFriends = numFriends;
        this.numLikes = numLikes;
        this.numVideos = numVideos;
        this.numViews = numViews;
    }

    /**
     * Gets the email address for this user instance.
     * @return the email address as a String.
     */
    @SuppressWarnings("unused")
    public final String getEmailAddress() {
        return this.emailAddress;
    }

    /**
     * Gets the full name of the user instance.
     * @return the full name of the user as a String.
     */
    @SuppressWarnings("unused")
    public final String getFullName() {
        return this.profile.getFullName();
    }

    /**
     * Gets the number of demands for the user instance.
     * @return the number of demands as an integer.
     */
    @SuppressWarnings("unused")
    public final int getNumDemands() {
        return this.numDemands;
    }

    /**
     * Gets the number of friends for the user instance.
     * @return the number of friends as an integer.
     */
    @SuppressWarnings("unused")
    public final int getNumFriends() {
        return this.numFriends;
    }

    /**
     * Gets the number of followers for the user instance.
     * @return the number of followers as an integer.
     */
    @SuppressWarnings("unused")
    public final int getNumFollowers() {
        return this.numFollowers;
    }

    /**
     * Gets the number of likes for the user instance.
     * @return the number of likes as an integer.
     */
    @SuppressWarnings("unused")
    public final int getNumLikes() {
        return this.numLikes;
    }

    /**
     * Gets the number of videos for the user instance.
     * @return the number of videos as an integer.
     */
    @SuppressWarnings("unused")
    public final int getNumVideos() {
        return this.numVideos;
    }

    /**
     * Gets the number of views for the user instance.
     * @return the number of views as an integer.
     */
    @SuppressWarnings("unused")
    public final int getNumViews() {
        return this.numViews;
    }

    /**
     * Gets the PUserProfileObject for this user instance.
     * @return the profile as a PUserProfile object.
     */
    @SuppressWarnings("unused")
    public final PUserProfile getProfile() {
        return this.profile;
    }

    /**
     * Gets the username for this user instance.
     * @return the username as a String.
     */
    @SuppressWarnings("unused")
    public final String getUsername() {
        return this.username;
    }

    /**
     * Gets the vanity username for this user instance.
     * @return the vanity username as a String.
     */
    @SuppressWarnings("unused")
    public final String getVanityUsername() {
        return this.vanityUsername;
    }

    /**
     * Sets the description of the user instance within the profile.
     * @param description is the new description as a String.
     */
    @SuppressWarnings("unused")
    public final void setDescription(final String description) {
        this.getProfile().setDescription(description);
    }

    /**
     * Sets the email address for the user instance.
     * @param emailAddress is a String.
     */
    @SuppressWarnings("unused")
    public final void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Sets the full name of the user instance.
     * @param fullName is the new full name of the user as a String.
     */
    @SuppressWarnings("unused")
    public final void setFullName(final String fullName) {
        this.getProfile().setFullName(fullName);
    }

    /**
     * Sets the gender of the user instance within the profile.
     * @param gender is the new gender to use as a Gender enumerated type.
     */
    @SuppressWarnings("unused")
    public final void setGender(final PGender gender) {
        this.getProfile().setGender(gender);
    }

    /**
     * Sets the last update of the user.
     * @param lastUpdate is the last udpate date as a Calendar.
     */
    @SuppressWarnings("unused")
    public final void setLastUpdate(final Calendar lastUpdate) {
        this.lastUpdateDate = lastUpdate;
    }

    /**
     * Batch sets the number properties of the user instance.
     * @param demands the number of demands the user has as an integer.
     * @param followers the number of followers the user has as an integer.
     * @param friends the number of friends the user has as an integer.
     * @param likes the number of likes the user has as an integer.
     * @param videos the number of videos the user has as an integer.
     * @param views the number of views the user has as an integer.
     */
    @SuppressWarnings("unused")
    public final void setNumProps(final Integer demands, final Integer followers, final Integer friends, final Integer likes, final Integer videos, final Integer views) {
        if (demands != null) { this.numDemands = demands; }
        if (followers != null) { this.numFollowers = followers; }
        if (friends != null) { this.numFriends = friends; }
        if (likes != null) { this.numLikes = likes; }
        if (videos != null) { this.numVideos = videos; }
        if(views != null) { this.numViews = views; }
    }

    /**
     * Sets the profile of the user.
     * @param profile is a PUserProfile object.
     */
    @SuppressWarnings("unused")
    public final void setProfile(final PUserProfile profile) {
        this.profile = profile;
    }

    /**
     * Sets the subjective meta for the user instance.
     * @param subjectiveMeta is a PSubjectiveMeta object.
     */
    @SuppressWarnings("unused")
    public final void setSubjectiveMeta(final PSubjectiveMeta subjectiveMeta) {
        this.subjectiveMeta = subjectiveMeta;
    }

    /**
     * Sets the vanity username for the user instance.
     * @param username is a String.
     */
    @SuppressWarnings("unused")
    public final void setVanityUsername(final String username) {
        this.vanityUsername = username;
    }

}
