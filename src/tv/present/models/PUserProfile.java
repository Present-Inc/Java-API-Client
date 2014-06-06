package tv.present.models;

import tv.present.enumerations.PGender;

import java.util.logging.Logger;

/**
 * Present User Profile Model
 * June 05, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
public final class PUserProfile extends PObject {

    private static final String TAG = "tv.present.factories.PUserProfile";
    private static final Logger PLog = Logger.getLogger(TAG);

	private String description;
	private String fullName;
	private String pictureURL;
	private String websiteURL;
	private PGender gender;

	/**
	 * Constructs a PUserProfile.
	 * @param fullName is the user's full name as a String.
	 * @param description is the user description as a String.
	 * @param pictureURL is the URL to the profile picture as a String.
	 * @param websiteURL is the URL of the user's website as a String.
	 */
	public PUserProfile(String fullName, String description, String pictureURL, String websiteURL) {
        super(null, null, null, null);
		this.fullName = fullName;
		this.description = description;
		this.pictureURL = pictureURL;
		this.websiteURL = websiteURL;
        PLog.info("Constructing PUserProfile for user: " + fullName);
	}
	
	/**
	 * Constructs a PUserProfile.
	 * @param fullName is the user's full name as a String.
	 * @param gender is the user's gender as a Gender enumerated type.
	 * @param description is the user description as a String.
	 * @param pictureURL is the URL to the profile picture as a String.
	 * @param websiteURL is the URL of the user's website as a String.
	 */
    @SuppressWarnings("unused")
	public PUserProfile(String fullName, PGender gender, String description, String pictureURL, String websiteURL) {
        super(null, null, null, null);
        this.gender = gender;
		this.fullName = fullName;
		this.description = description;
		this.pictureURL = pictureURL;
		this.websiteURL = websiteURL;
	}

	/**
	 * Gets the user's description from the profile instance.
	 * @return the description as a String.
	 */
    @SuppressWarnings("unused")
	public final String getDescription() {
		return this.description;
	}
	
	/**
	 * Gets the user's full name from this profile instance.
	 * @return the full name as a String.
	 */
    @SuppressWarnings("unused")
	public final String getFullName() {
		return this.fullName;
	}
	
	/**
	 * Gets the user's gender from the profile instance.
	 * @return the gender as a Gender enumerated type.
	 */
    @SuppressWarnings("unused")
	public final PGender getGender() {
		return this.gender;
	}
	
	/**
	 * Gets the user's profile picture URL from the profile instance.
	 * @return the profile picture URL as a String.
	 */
    @SuppressWarnings("unused")
	public final String getProfilePictureURL() {
		return this.pictureURL;
	}
	
	/**
	 * Gets the user's website URL from the profile instance.
	 * @return the website URL as a String.
	 */
    @SuppressWarnings("unused")
	public final String getWebsiteURL() {
		return this.websiteURL;
	}
	
	/**
	 * Sets the profile description.
	 * @param description is a String.
	 */
    @SuppressWarnings("unused")
	public final void setDescription(final String description) {
		this.description = description;
	}
	
	/**
	 * Sets the users friendly name.
	 * @param name is a String.
	 */
    @SuppressWarnings("unused")
	public final void setFullName(final String name) {
		this.fullName = name;
	}
	
	/**
	 * Sets the URL of the profile picture.
	 * @param url is a String.
	 */
    @SuppressWarnings("unused")
	public final void setPictureURL(final String url) {
		this.pictureURL = url;
	}
	
	/**
	 * Sets the URL of the website.
	 * @param url is a String.
	 */
    @SuppressWarnings("unused")
	public final void setWebsiteURL(final String url) {
		this.websiteURL = url;
	}
	
	/**
	 * Sets the user's gender in this profile instance.
	 * @param gender is as Gender enumerated type.
	 */
    @SuppressWarnings("unused")
	public final void setGender(final PGender gender) {
		this.gender = gender;
	}

}
