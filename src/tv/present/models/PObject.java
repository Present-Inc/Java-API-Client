package tv.present.models;

import java.util.Calendar;

/**
 * Present Abstract Object Model
 * June 05, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
public abstract class PObject {

    protected final String id;
    protected PSubjectiveMeta subjectiveMeta;
    protected final Calendar creationDate;
    protected Calendar lastUpdateDate;

    /**
     * Constructs the PObject object.
     * @param id is the String ID of the object.
     * @param subjectiveMeta is the SubjeciveMeta of the object.
     * @param creationDate is the Calendar creation of the object.
     * @param lastUpdateDate is the Calendar last update of the object.
     */
    public PObject(final String id, final PSubjectiveMeta subjectiveMeta, final Calendar creationDate, final Calendar lastUpdateDate) {
        this.creationDate = creationDate;
        this.id = id;
        this.lastUpdateDate = lastUpdateDate;
        this.subjectiveMeta = subjectiveMeta;
    }

    /**
     * Gets the creation date of the object.
     * @return the creation date as a Calendar.
     */
    public final Calendar getCreationDate() {
        return this.creationDate;
    }

    /**
     * Gets the last update date of the object.
     * @return is the last update date as a Calendar.
     */
    public final Calendar getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    /**
     * Gets the subjective meta data for the object.
     * @return the SubjectiveMeta object that represents this object.
     */
    public final PSubjectiveMeta getSubjectiveMeta() {
        return this.subjectiveMeta;
    }

    /**
     * Gets the ID of this object.
     * @return the ID of this object as a String.
     */
    public final String getID() {
        return this.id;
    }

}
