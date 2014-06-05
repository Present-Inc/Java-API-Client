package tv.present.api;

import org.json.JSONArray;
import org.json.JSONObject;
import tv.present.factories.PObjectFactory;
import tv.present.models.*;
import tv.present.util.PResultSet;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by kbw28 on 6/5/14.
 */
public class PAPIInteractionManager {

    private final String TAG = "tv.present.api.PAPIInteractionManager";
    private final Logger PLog = Logger.getLogger(TAG);

    /* ########## DEMANDS ########## */

    /**
     * Lists demands that a user has made to other users.  Null is valid for
     * all parameters, however userID and username cannot both be null at the
     * same time for a non-null response.
     * @param userID is the String value of the user ID.
     * @param username is the String value of the username.
     * @param limit is an Integer value.
     * @param cursor is an Integer value.
     * @return 	an ArrayList of PDemand objects for the user on a successful
     * 			response, null otherwise.
     */
    public ArrayList<PDemand> listUserForwardDemands(String userID, String username, Integer limit, Integer cursor) {

        if (limit == null) { limit = 20; }
        if (cursor == null) { cursor = 0; }

        PAPIBridge connector;

        if (userID != null) {
            connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, "demands/list_user_forward_demands?user_id=" + userID + "&limit=" + limit.toString() + "&cursor=" + cursor.toString());
        }
        else if (username != null) {
            connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, "demands/list_user_forward_demands?username=" + username + "&limit=" + limit.toString() + "&cursor=" + cursor.toString());
        }
        else {
            return null;
        }

        JSONObject response = connector.makeRequest();

        // Continue only if there is a non-failing result code
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {

            JSONArray resultsJSON = response.getJSONArray("results");
            ArrayList<PDemand> product = new ArrayList<PDemand>();

            final PObjectFactory pObjectFactory = new PObjectFactory();
            // Loop through the JSON user objects and create Java objects
            for (int i = 0; i < resultsJSON.length(); i++) {
                JSONObject demandRootJSON = resultsJSON.getJSONObject(i);
                product.add(pObjectFactory.constructDemandFromJSON(demandRootJSON));
            }

            return product;

        }
        else {
            PLog.warning("Error in request from PDemand->listUserForwardDemands() -- Response is: " + response.toString());
            return null;
        }

    }

    /**
     * Makes a demand for a user.
     * @param userContext is the UserContext from which to make the demand from.
     * @param username is the username of the User whom you're demanding.
     * @return true if the demand was created successfully, false otherwise
     */
    public boolean makeDemand(PUserContext userContext, String username) {

        PLog.info("User " + userContext.getUserID() + " is trying to demand " + username);

        // Create the JSONObject to pass the parameters to the request and then eventually
        // store the response parameters.
        JSONObject requestBundle = new JSONObject();

        // Add data to the request bundle
        requestBundle.put("username", username);

        // Create a connection, pass the data, and get a response string
        PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.POST, "demands/create", userContext, requestBundle);
        JSONObject response = connector.makeRequest();

        // Don't continue if we don't have a valid result code.
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {
            PLog.info("makeDemand() --> Response: " + response.toString());
            return true;
        }

        // Return null if we are unable to get a context
        else {
            PLog.info("makeDemand() --> Unable to make demand");
            return false;
        }
    }

    /**
     * Removes a demand for a user.
     * @param userContext is the UserContext from which to make the demand from.
     * @param username is the username of the User whom you're demanding.
     * @return true if the demand was removed successfully, false otherwise
     */
    public boolean removeDemand(PUserContext userContext, String username) {

        PLog.info("User " + userContext.getUserID() + " is trying to remove demand from " + username);

        // Create the JSONObject to pass the parameters to the request and then eventually
        // store the response parameters.
        JSONObject requestBundle = new JSONObject();

        // Add data to the request bundle
        requestBundle.put("username", username);

        // Create a connection, pass the data, and get a response string
        PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.POST, "demands/destroy", userContext, requestBundle);
        JSONObject response = connector.makeRequest();

        // Don't continue if we don't have a valid result code.
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {
            PLog.info("removeDemand() --> Response: " + response.toString());
            return true;
        }

        // Return null if we are unable to get a context
        else {
            PLog.info("removeDemand() --> Unable to remove demand.  Response: " + response.toString());
            return false;
        }
    }

    /* ########## USERS ########## */
    /**
     * Creates a user on the API.
     * @param username is the username to create as a String.
     * @param password is the password to associate with the username as a String.
     * @param emailAddress is the email address to associate with the username as a String.
     * @return a valid PUser if user creation was successful, null otherwise.
     */
    public PUser addUser(String username, String password, String emailAddress) {

        PAPIBridge connector;
        JSONObject requestBundle = new JSONObject();

        requestBundle.put("username", username);
        requestBundle.put("password", password);
        requestBundle.put("email", emailAddress);

        connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.POST, "users/create", requestBundle);
        JSONObject response = connector.makeRequest();

        // Don't continue if we don't have a valid result code.
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {
            PObjectFactory pObjectFactory = new PObjectFactory();
            PLog.info("add() -> Response from server: " + response.toString());
            return pObjectFactory.constructUserFromJSON(response.getJSONObject("result"));
        }

        // Return null if we are unable to get a context
        else {
            PLog.severe("add() -> Error getting context.  Response is: " + response.toString());
            return null;
        }

    }

    /**
     * Destroys the account that currently has context.  There is no confirmation.  BE CAREFUL!!
     */
    public void destroyUser(PUserContext PUserContext) {

        JSONObject garbage = new JSONObject();
        PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.POST, "users/destroy", PUserContext, garbage);
        JSONObject response = new JSONObject(connector.makeRequest());

        // Don't continue if we don't have a valid result code.
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {
            PLog.info("destroyAccount() -> Success.  Result is: " + response.toString());
        }

        // Return null if we are unable to get a context
        else {
            PLog.severe("destroyAccount -> Error destroying account.  Result is: " + response.toString());
        }

    }

    /**
     * Shows a user associated with a user context.
     * @param PUserContext is the user context as a PPUserContext object.
     * @return a PUser for whom the context represents.
     */
    public PUser getMyself(PUserContext PUserContext) {

        PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, "users/show_me", PUserContext);
        JSONObject response = connector.makeRequest();

        // Continue only if there is a non-failing result code
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {

            JSONObject resultJSON = response.getJSONObject("result");
            PObjectFactory pObjectFactory = new PObjectFactory();
            return pObjectFactory.constructUserFromJSON(resultJSON);

        }
        else {
            // Log the error to somewhere
            PLog.severe("getMe() -> Error.  Response is: " + response.toString());
            return null;
        }
    }

    /**
     * Returns an array of brand new users on Present.
     * @param limit the number of results desired as an integer.
     * @return an ArrayList of PUsers that are new.
     */
    public ArrayList<PUser> getNewUsers(int limit) {

        final String RESOURCE = "list_brand_new_users";
        return this.getUserList(RESOURCE, limit);

    }

    /**
     * Returns an array of popular users on Present.
     * @param limit the number of results desired as an integer.
     * @return an ArrayList of PUsers that are popular.
     */
    public ArrayList<PUser> getPopularUsers(int limit) {

        final String RESOURCE = "list_popular_users";
        return this.getUserList(RESOURCE, limit);

    }

    /**
     * This function is the base for the functions that get lists of users.  It
     * is private and can only be accessed within this class.
     * @param resource is the resource name to access as a String.
     * @param limit is the number of results desired as an integer.
     * @return an ArrayList of PUsers that are consistent with the requested route of size not more than limit.
     */
    private ArrayList<PUser> getUserList(String resource, int limit) {

        PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, "users/" + resource + "?limit=" + limit);
        JSONObject response = connector.makeRequest();

        // Continue only if there is a non-failing result code
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {

            JSONArray resultsJSON = response.getJSONArray("results");
            ArrayList<PUser> product = new ArrayList<PUser>();
            PObjectFactory pObjectFactory = new PObjectFactory();

            // Loop through the JSON user objects and create Java objects
            for (int i = 0; i < resultsJSON.length(); i++) {
                JSONObject userRootJSON = resultsJSON.getJSONObject(i);
                product.add(pObjectFactory.constructUserFromJSON(userRootJSON));
            }

            return product;

        }
        else {
            // Log the error to somewhere
            PLog.severe("getUserList() -> Error.  Response is: " + response.toString());
            System.out.println("Error from PUser->getUserList() -- response is: " + response.toString());
            return null;
        }

    }

    /**
     * This function is a private base function used by the showByUsername() and showByUserID() methods.
     * @param username is the username to look up by as a String.
     * @param id is the user ID to look up by as a String.
     * @return A valid PUser if the lookup was successful, null otherwise.
     */
    private PUser getUser(String username, String id) {

        PAPIBridge connector;
        if(username != null) {
            connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, "users/show?username=" + username);
        }
        else if (id != null) {
            connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, "users/show?user_id=" + id);
        }
        else {
            return null;
        }

        JSONObject response = connector.makeRequest();

        // Continue only if there is a non-failing result code
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {
            JSONObject resultJSON = response.getJSONObject("result");
            PObjectFactory pObjectFactory = new PObjectFactory();
            return pObjectFactory.constructUserFromJSON(resultJSON);
        }
        else {
            // Log the error to somewhere
            PLog.severe("showUser() -> Error.  Response is: " + response.toString());
            return null;
        }

    }

    /**
     * Gets a user by their user ID.
     * @param id is the ID to query for as a String.
     * @return a PUser object that corresponds to that user ID.
     */
    public PUser getUserByID(String id) {
        return this.getUser(null, id);
    }

    /**
     * Gets a user by their username.
     * @param username is the username to query for as a String.
     * @return a PUser object that corresponds to that username.
     */
    public PUser getUserByUsername(String username) {
        return this.getUser(username, null);
    }

    /**
     * Invites a new user to join Present.
     * @param PUserContext is a the user context that is requesting the invite as a PPUserContext.
     * @param emailAddress is an email address to send the invitation to as a String.
     * @return true if the invite was sent successfully, false otherwise.
     */
    public boolean invite(PUserContext PUserContext, String emailAddress) {

        JSONObject requestBundle = new JSONObject();
        requestBundle.put("email", emailAddress);

        PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.POST, "users/invite", PUserContext, requestBundle);;

        JSONObject response = connector.makeRequest();

        // Don't continue if we don't have a valid result code.
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {
            String status = response.getString("status");
            String resultMessage = response.getJSONObject("result").getString("message");
            if(status.equals("OK")) {
                return true;
            }
            else {
                return false;
            }
        }

        // Return null if we are unable do the invite
        else {
            PLog.severe("invite() -> Error.  Response is: " + response.toString());
            return false;
        }

    }

    /**
     * Generates a reset password request (and email) for a given username.
     * @param username is the username that needs their password reset as a String.
     * @return true if the password reset request was sent successfully, false otherwise.
     */
    public boolean requestPasswordReset(String username) {

        JSONObject requestBundle = new JSONObject();
        requestBundle.put("username", username);

        PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.POST, "users/request_password_reset", requestBundle);;

        JSONObject response = connector.makeRequest();

        // Don't continue if we don't have a valid result code.
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {
            PLog.info("requestPasswordReset() -> Result from server: " + response.toString());
            String status = response.getString("status");
            String resultMessage = response.getJSONObject("result").getString("message");
            if(status.equals("OK")) {
                return true;
            }
            else {
                return false;
            }

        }

        // Return null if we are unable to get a context
        else {
            PLog.severe("requestPasswordReset() -> Error requesting password reset.  Result from server: " + response.toString());
            return false;
        }

    }

    /**
     * Searches for a user based on a general query, or a query against a
     * username.  Only one (ie: only a query or a username) needs to be passed
     * Null is valid for all fields, but either a username or a query must be
     * supplied to get a non-null result returned.  This method is private and
     * cannot be called externally.
     *
     * @param query is the String query to search for.
     * @param username is the String query to search for.
     * @param limit is the Integer value to limit the results to.
     * @param cursor is the Integer value to start the results cursor at.
     * @return 	an empty ArrayList of PUser objects if there are no results,
     * 			null if there weren't enough parameters supplied, or there was
     * 			an invalid search response.
     */
    private ArrayList<PUser> search(String query, String username, Integer limit, Integer cursor) {

        if (limit == null || limit <= 0) { limit = 20; }
        if (cursor == null || cursor < 0) { cursor = 0; }

        PAPIBridge connector;

        if (query != null) {
            connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, "users/search?query=" + query + "&limit=" + limit.toString() + "&cursor=" + cursor.toString());
        }
        else if (username != null) {
            connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, "users/search?username=" + username + "&limit=" + limit.toString() + "&cursor=" + cursor.toString());
        }
        else {
            return null;
        }

        JSONObject response = connector.makeRequest();

        // Continue only if there is a non-failing result code
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {

            JSONArray resultsJSON = response.getJSONArray("results");
            ArrayList<PUser> product = new ArrayList<PUser>();

            // Loop through the JSON user objects and create Java objects
            for (int i = 0; i < resultsJSON.length(); i++) {
                JSONObject userRootJSON = resultsJSON.getJSONObject(i);
                PObjectFactory pObjectFactory = new PObjectFactory();
                product.add(pObjectFactory.constructUserFromJSON(userRootJSON));
            }

            return product;

        }
        else {
            PLog.severe("search() -> Error seraching.  Result from server: " + response.toString());
            return null;
        }

    }

    /**
     * Searches for a user by a general query.
     * @param query is the String query to search for.
     * @param limit is the Integer value to limit the results to.
     * @param cursor is the Integer value to start the results cursor at.
     * @return 	an empty ArrayList of PUser objects if there are no results,
     * 			null if there weren't enough parameters supplied, or there was
     * 			an invalid search response.
     */
    public ArrayList<PUser> searchByQuery(String query, Integer limit, Integer cursor) {
        return this.search(query, null, limit, cursor);
    }

    /**
     * Searches for a user by a username.
     * @param username is the String username to search for.
     * @param limit is the Integer value to limit the results to.
     * @param cursor is the Integer value to start the results cursor at.
     * @return 	an empty ArrayList of PUser objects if there are no results,
     * 			null if there weren't enough parameters supplied, or there was
     * 			an invalid search response.
     */
    public ArrayList<PUser> searchByUsername(String username, Integer limit, Integer cursor) {
        return this.search(null, username, limit, cursor);
    }


    /**
     * Updates details for a user for whom we have a context.  Parameters for details that are not to be updated should
     * be null.  This can be any or all of them.
     * @param fullName is the updated full name as a String.
     * @param description is the updated description as a String.
     * @param gender is the updated gender as a Gender enumeration (which is declared inside APIBridge).
     * @param location is the updated location as a String.
     * @param website is the updated website as a String.
     * @param emailAddress is an updated email address as a String.
     * @param phoneNumber is an updated phone number as a String.
     */
    public boolean updateUserDetails(PUserContext PUserContext, String fullName, String description, PUserProfile.Gender gender, String location, String website, String emailAddress, String phoneNumber) {

        PAPIBridge connector;
        JSONObject requestBundle = new JSONObject();

        if (fullName != null) {
            requestBundle.put("full_name", fullName);
        }
        if (description != null) {
            requestBundle.put("description", description);
        }
        if (gender != null) {
            requestBundle.put("gender", PAPIUtilities.genderToString(gender));
        }
        if (location != null) {
            requestBundle.put("location", location);
        }
        if (website != null) {
            requestBundle.put("website", website);
        }
        if (emailAddress != null) {
            requestBundle.put("email", emailAddress);
        }
        if (phoneNumber != null) {
            requestBundle.put("phone_number", phoneNumber);
        }

        connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.POST, "users/update", PUserContext, requestBundle);
        JSONObject response = connector.makeRequest();

        // Don't continue if we don't have a valid result code.
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {
            PObjectFactory pObjectFactory = new PObjectFactory();
            PUserContext.setUser(pObjectFactory.constructUserFromJSON(response.getJSONObject("result")));
            return true;
        }

        // Return null if we are unable to get a context
        else {
            PLog.severe("updateDetails() -> Error updating user.  Response was: " + response.toString());
            return false;
        }

    }

    /* ########## USER ACTIVITIES ########## */

    public PResultSet<PUserActivity> getUserActivities(final PUserContext PUserContext, Integer limit, Integer cursor) {

        if (limit == null || limit == 0) { limit = 20; }
        if (cursor == null || cursor == 0) { cursor = 0; }

        final String requestString = "activities/list_my_activities?cursor=" + cursor + "&limit=" + limit;
        final PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, requestString, PUserContext);
        JSONObject response = connector.makeRequest();

        PLog.info("getActivities -> Response is: " + response.toString());

        int responseCode = connector.getResponseCode();

        // Continue only if there is a non-failing result code
        if (responseCode <= PAPIBridge.MAX_SUCCESS_CODE) {
            JSONArray resultsJSON = response.getJSONArray("results");
            ArrayList<PUserActivity> product = new ArrayList<PUserActivity>();

            // Loop through the JSON user objects and create Java objects
            PObjectFactory pObjectFactory = new PObjectFactory();
            for (int i = 0; i < resultsJSON.length(); i++) {
                JSONObject userActivityJSON = resultsJSON.getJSONObject(i);
                PLog.info("getActivities() --> UserActivityJSON is: " + userActivityJSON.toString());
                product.add(pObjectFactory.constructUserActivityFromJSON(userActivityJSON));
            }

            PLog.info("getActivities() -> There will be " + product.size() + " elements in the results array");
            PResultSet<PUserActivity> resultSet = new PResultSet<PUserActivity>(response.getInt("nextCursor"), product);
            return resultSet;

        }

        return null;

    }

    /* ########## USER CONTEXTS ########## */
    /**
     * Creates a user context from a given username and password.
     * @param username is the username to try as a String.
     * @param password is the password associated with the username as a String.
     * @return a valid PUserContext object if the username and password are valid, null otherwise.
     */
    public PUserContext getUserContext(String username, String password) {

        System.out.println("Trying to get a userContext with username " + username + " and password " + password);

        // Final strings to format the request parameters correctly.
        final int MAX_SUCCESS_CODE = 300;

        PAPIBridge connector;
        JSONObject response;

        // Create the JSONObject to pass the parameters to the request and then eventually
        // store the response parameters.
        JSONObject requestBundle = new JSONObject();

        // Add data to the request bundle
        requestBundle.put("username", username);
        requestBundle.put("password", password);

        // Create a connection, pass the data, and get a response string
        connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.POST, "user_contexts/create", requestBundle);
        response = connector.makeRequest();

        // Don't continue if we don't have a valid result code.
        if (connector.getResponseCode() <= MAX_SUCCESS_CODE) {
            JSONObject resultJSON = response.getJSONObject("result").getJSONObject("object");
            JSONObject userRootJSON = resultJSON.getJSONObject("user");
            PObjectFactory pObjectFactory = new PObjectFactory();
            return new PUserContext(resultJSON.getString("_id"), null, null, resultJSON.getString("sessionToken"), pObjectFactory.constructUserFromJSON(userRootJSON));
        }

        // Return null if we are unable to get a context
        else {
            PLog.warning("Unable to get a user context.  Response was: " + response.toString());
            return null;
        }

    }

    /**
     * Invalidates an existing user context.
     * @param 	userContext is the PUserContext object that will be invalidated
     * 			on the server.
     * @return true on successful invalidation, false otherwise.
     */
    public boolean invalidateUserContext(PUserContext userContext) {

        JSONObject requestBundle = new JSONObject();

        PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.POST, "user_contexts/destroy", userContext, requestBundle);

        JSONObject response = connector.makeRequest();

        // Don't continue if we don't have a valid result code.
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {
            String status = response.getString("status");
            if(status.equals("OK")) {
                return true;
            }
            else {
                return false;
            }
        }

        // Return null if we are unable to get a context
        else {
            System.out.print("Error from PUserContext->invalidate() -- response is: " + response.toString());
            return false;
        }
    }

    /* ########## VIDEOS ########## */
    public PVideo create(PUserContext userContext, String title) {
        JSONObject requestBundle = new JSONObject();
        requestBundle.put("title", title);

        PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.POST, "videos/create", userContext, requestBundle);;

        JSONObject response = connector.makeRequest();

        // Don't continue if we don't have a valid result code.
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {

            System.out.println("Response from server: " + response.toString());
            return null;

        }

        // Return null if we are unable to get a context
        else {
            System.out.print("Error from PVideo->create() -- response is: " + response.toString());
            return null;
        }

    }

    /**
     * Gets an array of video objects that should appear on a user's feed.
     * @param limit is the number of records to return as an integer.
     * @return an ArrayList of PVideo objects of size less than or equal to limit.
     */
    public ArrayList<PVideo> getHomeVideos(PUserContext userContext, Integer limit, Integer cursor) {

        if (limit == null || limit == 0) { limit = 20; }
        if (cursor == null || cursor == 0) { cursor = 0; }

        final String requestString = "videos/list_home_videos?cursor=" + cursor + "&limit=" + limit;
        PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, requestString, userContext);
        JSONObject response = connector.makeRequest();

        // Continue only if there is a non-failing result code
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {

            System.out.println("The response for get videos is: " + response);
            JSONArray resultsJSON = response.getJSONArray("results");
            ArrayList<PVideo> product = new ArrayList<PVideo>();

            // Loop through the JSON user objects and create Java objects
            PObjectFactory objectFactory = new PObjectFactory();
            for (int i = 0; i < resultsJSON.length(); i++) {
                JSONObject videoJSON = resultsJSON.getJSONObject(i);
                System.out.println("VideoJSON is : " + videoJSON.toString());
                product.add(objectFactory.constructVideoFromJSON(videoJSON));
            }

            return product;

        }

        return null;

    }

    /**
     * Gets the most popular videos.
     * @param limit
     * @param cursor
     * @return
     */
    public ArrayList<PVideo> getPopularVideos(Integer limit, Integer cursor) {

        if (limit == null) { limit = 20; }
        if (cursor == null) { cursor = 0; }

        PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, "videos/list_popular_videos?limit=" + limit.toString() + "&cursor=" + cursor.toString());
        JSONObject response = connector.makeRequest();

        // Continue only if there is a non-failing result code
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {

            JSONArray resultsJSON = response.getJSONArray("results");
            ArrayList<PVideo> product = new ArrayList<PVideo>();

            // Loop through the JSON user objects and create Java objects
            PObjectFactory objectFactory = new PObjectFactory();
            for (int i = 0; i < resultsJSON.length(); i++) {
                JSONObject videoRootJSON = resultsJSON.getJSONObject(i);
                System.out.println("Raw JSON: " + videoRootJSON.toString());
                product.add(objectFactory.constructVideoFromJSON(videoRootJSON));
            }

            return product;

        }
        else {
            // Log the error to somewhere
            System.out.println("Error from PVideo->listBrandNew() -- response is: " + response.toString());
            return null;
        }

    }

    public ArrayList<PVideo> getNewVideos(Integer limit, Integer cursor) {

        if (limit == null) { limit = 20; }
        if (cursor == null) { cursor = 0; }

        PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, "videos/list_brand_new_videos?limit=" + limit.toString() + "&cursor=" + cursor.toString());
        JSONObject response = connector.makeRequest();

        // Continue only if there is a non-failing result code
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {

            JSONArray resultsJSON = response.getJSONArray("results");
            ArrayList<PVideo> product = new ArrayList<PVideo>();

            // Loop through the JSON user objects and create Java objects
            PObjectFactory objectFactory = new PObjectFactory();
            for (int i = 0; i < resultsJSON.length(); i++) {
                JSONObject videoRootJSON = resultsJSON.getJSONObject(i);
                System.out.println("Raw JSON: " + videoRootJSON.toString());
                product.add(objectFactory.constructVideoFromJSON(videoRootJSON));
            }

            return product;

        }
        else {
            // Log the error to somewhere
            System.out.println("Error from PVideo->listBrandNew() -- response is: " + response.toString());
            return null;
        }

    }

    private ArrayList<PVideo> getVideosByUser(String id, String username, Integer limit, Integer cursor) {

        if (limit == null) { limit = 20; }
        if (cursor == null) { cursor = 0; }

        PAPIBridge connector;

        if (id != null) {
            connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, "videos/list_user_videos?user_id=" + id + "&limit=" + limit + "&cursor=" + cursor);
        }
        else if (username != null) {
            connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, "videos/list_user_videos?username=" + username + "&limit=" + limit + "&cursor=" + cursor);
        }
        else {
            return null;
        }

        JSONObject response = connector.makeRequest();

        // Continue only if there is a non-failing result code
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {

            JSONArray resultsJSON = response.getJSONArray("results");
            ArrayList<PVideo> product = new ArrayList<PVideo>();

            // Loop through the JSON user objects and create Java objects
            PObjectFactory objectFactory = new PObjectFactory();
            for (int i = 0; i < resultsJSON.length(); i++) {
                JSONObject videoRootJSON = resultsJSON.getJSONObject(i);
                System.out.println("Raw JSON: " + videoRootJSON.toString());
                product.add(objectFactory.constructVideoFromJSON(videoRootJSON));
            }

            return product;

        }
        else {
            // Log the error to somewhere
            System.out.println("Error from PVideo->getVideosForUser() -- response is: " + response.toString());
            return null;
        }

    }

    public ArrayList<PVideo> getVideosByUserID(String id, Integer limit, Integer cursor) {
        return this.getVideosByUser(id, null, limit, cursor);
    }

    public ArrayList<PVideo> getVideosByUsername(String username, Integer limit, Integer cursor) {
        return this.getVideosByUser(null, username, limit, cursor);
    }

    public ArrayList<PVideo> search(String query, Integer limit, Integer cursor) {

        if (cursor == null || cursor == 0) { cursor = 0; }
        if (limit == null || limit == 0) { limit = 20; }

        PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, "videos/search?query=" + query + "&limit=" + limit.toString() + "&cursor=" + cursor.toString());
        JSONObject response = connector.makeRequest();

        // Continue only if there is a non-failing result code
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {

            JSONArray resultsJSON = response.getJSONArray("results");
            ArrayList<PVideo> product = new ArrayList<PVideo>();

            // Loop through the JSON user objects and create Java objects
            PObjectFactory objectFactory = new PObjectFactory();
            for (int i = 0; i < resultsJSON.length(); i++) {
                JSONObject videoRootJSON = resultsJSON.getJSONObject(i);
                System.out.println("Raw JSON: " + videoRootJSON.toString());
                product.add(objectFactory.constructVideoFromJSON(videoRootJSON));
            }

            return product;

        }
        else {
            // Log the error to somewhere
            System.out.println("Error from PVideo->search() -- response is: " + response.toString());
            return null;
        }

    }

    /**
     * Gets a video by it's ID.
     * @param videoID the ID of the video as a String.
     * @return a PVideo object that is the video represented by the String.
     */
    public PVideo show(String videoID) {

        PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.GET, "videos/show?comment_id=" + videoID);
        JSONObject response = connector.makeRequest();
        PObjectFactory objectFactory = new PObjectFactory();
        return objectFactory.constructVideoFromJSON(response.getJSONObject("result"));

    }

}
