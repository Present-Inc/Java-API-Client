package tv.present.models;

import java.util.logging.Logger;

/**
 * Present Subjective Metadata Model
 * June 05, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
public final class PSubjectiveMeta extends PObject {

	public enum SubjectiveMetaDirection {
		Backward,
		Forward
	}

    private static final String TAG = "tv.present.models.PSubjectiveMeta";
    private static final Logger PLog = Logger.getLogger(TAG);

	private boolean demandBackward = false;
	private boolean demandForward = false;
	private boolean friendshipBackward = false;
	private boolean friendshipForward = false;
	private boolean likeForward = false;
	private boolean likeBackward = false;
	
	public PSubjectiveMeta() {
        super(null, null, null, null);
		/* empty constructor */
	}
	
	/**
	 * Gets the state of the demand relation for a direction.
	 * @param direction is the SubjectiveMetaDirection to get the value for.
	 * @return the boolean value of the demand relation
	 */
    @SuppressWarnings("unused")
	public final boolean getDemand(final SubjectiveMetaDirection direction) {
		if (direction.equals(SubjectiveMetaDirection.Backward)) {
			return this.demandBackward;
		}
		else if (direction.equals(SubjectiveMetaDirection.Forward)) {
			return this.demandForward;
		}
		else {
            PLog.warning("getDemand() -> Subjective meta direction was neither Backward or Forward.");
			return false;
		}
	}
	
	/**
	 * Gets the state of the demand relation for a direction.
	 * @param direction is the SubjectiveMetaDirection to get the value for.
	 * @return the boolean value of the demand relation
	 */
    @SuppressWarnings("unused")
	public final boolean getFriendship(final SubjectiveMetaDirection direction) {
		if (direction.equals(SubjectiveMetaDirection.Backward)) {
			return this.friendshipBackward;
		}
		else if (direction.equals(SubjectiveMetaDirection.Forward)) {
			return this.friendshipForward;
		}
		else {
			PLog.warning("getFriendship() -> Subjective meta direction was neither Backward or Forward.");
			return false;
		}
	}
	
	/**
	 * Gets the state of the like relation for a direction.
	 * @param direction is the SubjectiveMetaDirection to get the value for.
	 * @return the boolean value of the demand relation
	 */
    @SuppressWarnings("unused")
	public final boolean getLike(final SubjectiveMetaDirection direction) {
		if (direction.equals(SubjectiveMetaDirection.Backward)) {
			return this.likeBackward;
		}
		else if (direction.equals(SubjectiveMetaDirection.Forward)) {
			return this.likeForward;
		}
		else {
            PLog.warning("getLike() -> Subjective meta direction was neither Backward or Forward.");
			return false;
		}
	}
	
	/**
	 * Sets the state of the demand relation for a given direction.
	 * @param direction is the SubjectiveMetaDirection to set the value for.
	 * @param value is a boolean value to set.
	 */
	public final void setDemand(final SubjectiveMetaDirection direction, final boolean value) {
		
		if (direction.equals(SubjectiveMetaDirection.Backward)) {
			this.demandBackward = value;
		}
		else if (direction.equals(SubjectiveMetaDirection.Forward)) {
			this.demandForward = value;
		}
		else {
            PLog.warning("setDemand() -> Subjective meta direction was neither Backward or Forward.");
		}
		
	}
	
	/**
	 * Sets the state of the demand relation for a given direction.
	 * @param direction is the SubjectiveMetaDirection to set the value for.
	 * @param value is a boolean value to set.
	 */
	public final void setFriendship(final SubjectiveMetaDirection direction, final boolean value) {
		
		if (direction.equals(SubjectiveMetaDirection.Backward)) {
			this.friendshipBackward= value;
		}
		else if (direction.equals(SubjectiveMetaDirection.Forward)) {
			this.friendshipForward = value;
		}
		else {
            PLog.warning("setFriendship() -> Subjective meta direction was neither Backward or Forward.");
		}
		
	}
	
	/**
	 * Sets the state of the demand relation for a given direction.
	 * @param direction is the SubjectiveMetaDirection to set the value for.
	 * @param value is a boolean value to set.
	 */
	public final void setLike(final SubjectiveMetaDirection direction, final boolean value) {
		
		if (direction.equals(SubjectiveMetaDirection.Backward)) {
			this.likeBackward= value;
		}
		else if (direction.equals(SubjectiveMetaDirection.Forward)) {
			this.likeForward = value;
		}
		else {
            PLog.warning("setLike() -> Subjective meta direction was neither Backward or Forward.");
		}
		
	}

}
