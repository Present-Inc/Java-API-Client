package tv.present.models;

/**
 * The IllegalSubjectiveMetaDirection is generally thrown when an incorrect SubjectiveMetaDirection is encountered.
 * @author Kyle Weisel (kyle@present.tv)
 */
public class IllegalSubjectiveMetaDirection extends Exception {

    private static final long serialVersionUID = -4204204204204204204L;

    public IllegalSubjectiveMetaDirection(String message) {
        super(message);
    }

}
