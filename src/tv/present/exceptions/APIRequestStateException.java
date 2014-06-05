package tv.present.exceptions;

/**
 * The APIRequestStateException is generally thrown when the APIConnector
 * has missing, invalid, or uninitialized connection parameters.
 * 
 * @author Kyle Weisel (kyle@present.tv)
 */
public class APIRequestStateException extends Exception {
	
	private static final long serialVersionUID = -4204204204204204204L;

	public APIRequestStateException(String message) {
		super(message);
	}

}
