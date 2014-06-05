package tv.present.models;

import java.util.logging.Logger;

/**
 * Present Visibility Model
 * June 05, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
public class PVisibility {
	
	public enum PVisibilityEntity {
		Everyone
	}

    private static final String TAG = "tv.present.factories.PVisibility";
    private static final Logger PLog = Logger.getLogger(TAG);
	private boolean everyone = false;

    /**
     * Constructs a PVisibility object.
     */
	public PVisibility() {
		/* empty constructor */
        PLog.info("Constructing new PVisibility object.");
	}
	
	/**
	 * Gets the state of visibility for an entity.
	 * @param entity is the PVisibilityEntity to get the value for.
	 * @return the boolean value of visibility
	 */
    @SuppressWarnings("unused")
	public boolean get(PVisibilityEntity entity) {
		if (entity.equals(PVisibilityEntity.Everyone)) {
			return this.everyone;
		}
		else {
			PLog.warning("get() -> Supplied invalid PVisibilityEntity");
			return false;
		}
	}
	
	/**
	 * Sets the state of visibility for an entity.
	 * @param entity is the PVisibilityEntity to get the value for.
	 * @param value is a boolean value to set.
	 */
    @SuppressWarnings("unused")
	public void set(PVisibilityEntity entity, boolean value) {
		
		if (entity.equals(PVisibilityEntity.Everyone)) {
			this.everyone = value;
		}
		else {
            PLog.warning("set() -> Supplied invalid PVisibilityEntity");
		}
		
	}

}

