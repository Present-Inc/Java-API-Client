package tv.present.api;

import org.json.JSONException;
import org.json.JSONObject;
import tv.present.exceptions.APIRequestPrereqException;
import tv.present.models.PUserContext;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Present API Bridge -- Provides a connection to the Present API.
 * June 04, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
@SuppressWarnings("unused")
public final class PAPIBridge {
	
	public enum HTTPRequestMethod {
		GET,
		POST,
		POST_MULTIPART
	}

    private static final String TAG = "tv.present.api.APIBridge";
    private static final Logger PLog = Logger.getLogger(TAG);
	
	public static final int MAX_SUCCESS_CODE = 300;

    private static final String BOUNDARY = "!PP!!PP!";
    private static final String CHARSET = "UTF-8";
    private static final String LINE_FEED = "\r\n";
    private static final String TWO_DASHES = "--";
    
	
	private String route = null;
	private JSONObject payload = null;
	private URL url = null;
	private HttpsURLConnection connection = null;
	private HTTPRequestMethod requestMethod = HTTPRequestMethod.POST;
	private int responseCode = 0;
	private PUserContext userContext = null;
	
	private HashMap<String, File> multipartFileData = null;
	private HashMap<String, String> multipartTextualData = null;


	
	/* #################### CONSTRUCTORS #################### */

    /**
     * Constructs an APIBridge object using a request method and a route.
     * @param requestMethod is the HTTPRequestMethod to use when making the request (ie: GET, POST, POST_MULTIPART).
     * @param route is route that the call should be made to as a String.
     */
    public PAPIBridge(HTTPRequestMethod requestMethod, String route) {
        this.route = route;
        this.setRequestMethod(requestMethod);
    }

    /**
     * Constructs an APIBridge object using a request method and a route.
     * @param requestMethod is the HTTPRequestMethod to use when making the request (ie: GET, POST, POST_MULTIPART).
     * @param route is route that the call should be made to as a String.
     * @param userContext is a UserContext that should be used when making the request.
     */
    public PAPIBridge(HTTPRequestMethod requestMethod, String route, PUserContext userContext) {
        this.route = route;
        this.userContext = userContext;
        this.setRequestMethod(requestMethod);
    }

    /**
     * Constructs an APIBridge object using a request method and a route.
     * @param requestMethod is the HTTPRequestMethod to use when making the request (ie: GET, POST, POST_MULTIPART).
     * @param route is route that the call should be made to as a String.
     * @param userContext is a UserContext that should be used when making the request.
     * @param payload is a JSONObject that contains key-value pairs of data to submit with the request.
     */
    public PAPIBridge(HTTPRequestMethod requestMethod, String route, PUserContext userContext, JSONObject payload) {
		this.payload = payload;
		this.route = route;
		this.userContext = userContext;
		this.setRequestMethod(requestMethod);
	}

    /**
     * Constructs an APIBridge object using a request method and a route.
     * @param requestMethod is the HTTPRequestMethod to use when making the request (ie: GET, POST, POST_MULTIPART).
     * @param route is route that the call should be made to as a String.
     * @param payload is a JSONObject that contains key-value pairs of data to submit with the request.
     */
	public PAPIBridge(HTTPRequestMethod requestMethod, String route, JSONObject payload) {
		this.payload = payload;
		this.route = route;
		this.setRequestMethod(requestMethod);
	}
	
	/* #################### GETTERS #################### */
	
	/**
	 * Returns the request method as a String for use when setting the connection headers.
	 * @return the request method as a String.
	 */
	private String getRequestMethodAsString() {
        return (this.requestMethod == HTTPRequestMethod.POST || this.requestMethod == HTTPRequestMethod.POST_MULTIPART) ? "POST" : "GET";
	}
	
	/* #################### SETTERS #################### */
	
	/**
	 * Sets the request method that this connector will use (ie: GET or POST)
	 * @param method is the method to use as a HTTPRequestMethod enumerated type.
	 */
	private void setRequestMethod(HTTPRequestMethod method) {
		this.requestMethod = method;
		if (this.requestMethod == HTTPRequestMethod.POST_MULTIPART) {
			this.multipartFileData = new HashMap<String, File>();
			this.multipartTextualData = new HashMap<String, String>();
		}
	}
	
	/* #################### HELPING #################### */
	
	public void addMultipartFile(String key, File value) {
		this.multipartFileData.put(key, value);
	}

    public void addMultipartTextualData(String key, String value) {
        this.multipartTextualData.put(key, value);
    }
	
	/**
	 * Checks to make sure this object is prepared to perform a request.  Exceptions
	 * will be thrown for the calling function to deal with if the object is not in the 
	 * proper state.
	 * @throws tv.present.exceptions.APIRequestPrereqException when there is a problem that will prevent a request from being completed.
	 */
	private void checkPrereqs() throws APIRequestPrereqException {
		if (this.url == null) {
            PLog.severe("An APIRequestPrereqException will be thrown because the URL object was not initialized.");
			throw new APIRequestPrereqException("URL object was not initialized.");
		}
	}
	
	/**
	 * Creates the connection that will be used to send the data object.
	 * @return true when a connection is created successfully, false otherwise.
	 */
	private boolean createConnection() {
		try {

            /* ########################################

                !!! API CONNECTION URL HERE !!!

            ######################################## */

            String API_BASE_URL = "https://api.present.tv/v1/";

            this.url = new URL(API_BASE_URL + this.route);
            PLog.info("About to create connection to: " + url.toString());
			this.checkPrereqs();
			this.connection = (HttpsURLConnection) this.url.openConnection();
			this.setHeaders();
			// Set the user ID and session token headers if there is a user context
			if (this.userContext != null) {
                PLog.info("This connection is being made with a user context.");
				final String userID = this.userContext.getUserID();
				final String sessionToken = this.userContext.getSessionToken();
				this.connection.setRequestProperty("Present-User-Context-User-Id", userID);
				this.connection.setRequestProperty("Present-User-Context-Session-Token", sessionToken);
			}
		} catch (APIRequestPrereqException e) {
            PLog.severe("Caught an APIRequestPrereqException that says: " + e.getMessage());
			return false;
		} catch (IOException e) {
            PLog.severe("Caught an IOException that says: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	/**
	 * Controls the collection of the error response from the server.
	 * @throws IOException when unable to get an error stream from the connection (ie: the connection
	 * may not exist).
	 * @return a String that is the server's error response to our request.
	 */
	private String getError() throws IOException {
		
		String response;
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(this.connection.getErrorStream()));
		String inputLine;
		StringBuilder buffResponse = new StringBuilder();
		while ((inputLine = inputStream.readLine()) != null) {
			buffResponse.append(inputLine);
		}
		inputStream.close();
		response = buffResponse.toString();
		return response;
		
	}

    /**
     * Controls the collection of the response from the server.
     * @throws IOException when unable to get a response stream from the connection (ie: the connection
     * may not exist).
     * @return a String that is the server's response to our request.
     */
	private String getResponse() throws IOException {
		
		String response;
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
		String inputLine;
		StringBuilder buffResponse = new StringBuilder();
		while ((inputLine = inputStream.readLine()) != null) {
			buffResponse.append(inputLine);
		}
		inputStream.close();
		response = buffResponse.toString();
		return response;
		
	}
	
	/**
	 * Allows access to the response code by external classes.
	 * @return the response code as an integer.
	 */
	public int getResponseCode() {
		return this.responseCode;		
	}
	
	/**
	 * Makes the request to the API via a connection.
	 * @return a regular or error stream response as a JSON object depending on the success
	 * status of the connection.
	 */
	public final JSONObject makeRequest() {
		
		JSONObject response = null;
		
		try {
			
			if(this.createConnection()) {

                PLog.info("A connection has been created, and request data (if any) will now be posted.");

                // If the request is POST, write it to the connection by calling the writeRequest() method
				if(this.requestMethod == HTTPRequestMethod.POST) {
					this.writeRequest();
				}
				else if (this.requestMethod == HTTPRequestMethod.POST_MULTIPART) {
					this.writeMultipart();
				}
				
				this.responseCode = this.connection.getResponseCode();
				
				if (this.responseCode <= PAPIBridge.MAX_SUCCESS_CODE) {
                    // We really make a bold assumption here.  Something should probably be done about this at some
                    // point??? -- KW (06/04/2014)
					response = new JSONObject(this.getResponse());
                    PLog.info("The response from the API was: " + response.toString());
				}
				
				else {
                    PLog.severe("The API responded with error: " + this.getError());
					try {
						response = new JSONObject(this.getError());
					} catch(JSONException e) {
						JSONObject errorJSON = new JSONObject();
						errorJSON.put("status", "ERROR");
						errorJSON.put("subject", this.getError());
						
						response = errorJSON;
					}
					
				}
				
			}
			
		} catch (IOException e) {
            PLog.severe("An IOException was thrown when trying to make the request.  Message was: " + e.getMessage());
		}
		
		return response;
		
	}
	
	/**
	 * Sets the headers and connection details for the connection that this object is
	 * making.
	 * @throws ProtocolException when an invalid request method is specified (ie: not GET or POST).
	 */
	private void setHeaders() throws ProtocolException {
        PLog.info("Setting headers for the connection.");
		this.connection.setRequestMethod(this.getRequestMethodAsString());
		this.connection.setDefaultUseCaches(false);
		this.connection.setUseCaches(false);
		this.connection.setDoInput(true);
        if (!this.requestMethod.equals(HTTPRequestMethod.GET)) {
            PLog.info("setHeaders() -> Setting do output to true.");
            this.connection.setDoOutput(true);
        }
		this.connection.setRequestProperty("Connection", "Keep-Alive");
		this.connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		this.connection.setRequestProperty("User-Agent", "Present API Client v1.1");
		
		// On a multipart upload, set the header accordingly
		if (this.requestMethod == HTTPRequestMethod.POST_MULTIPART) {
            PLog.info("This connection is multipart.  Initialize a few more things...");
			String contentType = "multipart/form-data; boundary=" + BOUNDARY;
			this.connection.setRequestProperty("Content-Type", contentType);
		}
		else {
			this.connection.setRequestProperty("Content-Type", "application/json");
		}
		
	}
	
	/**
	 * Controls the submission of the object to the connection with the server.  This will be used for POSTing methods.
	 * @throws IOException when unable to get an outputStream from the connection (ie: the connection
	 * may not exist).
	 */
	private void writeRequest() throws IOException {
        PLog.info("Writing the request.");
		DataOutputStream outputStream = new DataOutputStream(this.connection.getOutputStream());
		outputStream.writeBytes(this.payload.toString());
		outputStream.flush();
		outputStream.close();
	}
	
	private void writeMultipart() throws IOException {
        PLog.info("Writing multipart request.");
        DataOutputStream outputStream = new DataOutputStream(this.connection.getOutputStream());
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, CHARSET), true);
		
		// Iterate over the JSON and the multipart HashMap and upload
		@SuppressWarnings("unchecked") // The payload is type checked!
		Iterator<String> keys = this.payload.keys();
		
		while (keys.hasNext()) {
			String key = keys.next();
			String value;
			
			try {
				JSONObject temp = this.payload.getJSONObject(key);
				value = temp.toString();
				// Recursively call this function again to go objects deep
				// parse(value, out)
			} catch (JSONException e) {
				try {
					value = this.payload.getString(key);
				}
				catch (JSONException f) {
					try {
						value = ((Integer) this.payload.getInt(key)).toString();
					}
					catch (JSONException g) {
						value = ((Double) this.payload.getDouble(key)).toString();
					}
				}
				
			}
			
			if (value != null) {
				writer.append(TWO_DASHES + BOUNDARY).append(LINE_FEED);
                final String contentDisposition = "\"Content-Disposition: form-data; name=\\\"\" + key + \"\\\"\"";
		        writer.append(contentDisposition).append(LINE_FEED);
		        writer.append("Content-Type: text/plain; charset=" + CHARSET).append(LINE_FEED);
		        writer.append(LINE_FEED);
		        writer.append(value).append(LINE_FEED);
		        writer.flush();
			}
			
		}
		
		// Iterate over multipart Hashmap
		for (String key : this.multipartFileData.keySet()) {
			
			String fileName = this.multipartFileData.get(key).getName();
			
	        writer.append(TWO_DASHES + BOUNDARY).append(LINE_FEED);
            final String contentDisposition = "Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"";
	        writer.append(contentDisposition).append(LINE_FEED);
            final String contentType = "\"Content-Type: \" + URLConnection.guessContentTypeFromName(fileName)";
	        writer.append(contentType).append(LINE_FEED);
	        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
	        writer.append(LINE_FEED);
	        writer.flush();
	 
	        FileInputStream inputStream = new FileInputStream(this.multipartFileData.get(key));
	        byte[] buffer = new byte[4096];
	        int bytesRead;
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outputStream.write(buffer, 0, bytesRead);
	        }
	        outputStream.flush();
	        inputStream.close();
	         
	        writer.append(LINE_FEED);
	        writer.flush();
			
		}
		
		// Closing signature
		writer.append(LINE_FEED).flush();
        writer.append(TWO_DASHES + BOUNDARY + TWO_DASHES).append(LINE_FEED);
        writer.flush();
        writer.close();
        
        // Close the entire output stream
		outputStream.writeBytes(this.payload.toString());
		outputStream.flush();
		outputStream.close();
	}
	
}
