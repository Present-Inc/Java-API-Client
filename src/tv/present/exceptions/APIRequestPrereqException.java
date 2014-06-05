package tv.present.exceptions;

/**
 * The APIRequestPrereqException is generally thrown when the APIConnector has missing, invalid, or uninitialized
 * connection parameters (ie: the connector is not in the proper state to make a connection).
 * 
 * @author Kyle Weisel (kyle@present.tv)
 */
public class APIRequestPrereqException extends Exception {

	private static final long serialVersionUID = -6428824286600646466L;

	public APIRequestPrereqException(String message) {
		super(message);
	}
}
