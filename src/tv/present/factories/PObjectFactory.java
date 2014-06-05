package tv.present.factories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tv.present.api.PAPIUtilities;
import tv.present.api.PAPIInteractionManager;
import tv.present.models.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Present Object Factory
 * June 04, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
public class PObjectFactory {

    private static final String TAG = "tv.present.factories.PObjectFactory";
    private static final Logger PLog = Logger.getLogger(TAG);

    public final PComment constructCommentFromJSON(JSONObject json) {

        PLog.info("Constructing comment object from source JSON: " + json.toString());

        JSONObject commentObjectJSON = json.getJSONObject("object");

        Calendar creationDate = PAPIUtilities.parseZulu(commentObjectJSON.getString("_creationDate"));
        Calendar lastUpdateDate = PAPIUtilities.parseZulu(commentObjectJSON.getString("_lastUpdateDate"));
        //Video video = Video.create(commentObjectJSON.getJSONObject("video"));
        PUser sourceUser = this.constructUserFromJSON(commentObjectJSON.getJSONObject("sourceUser"));
        String body = commentObjectJSON.getString("body");
        String id = commentObjectJSON.getString("_id");

        return new PComment(id, body, null, sourceUser, creationDate, lastUpdateDate);

    }

    public final PDemand constructDemandFromJSON(JSONObject json) {

        // TODO: Build and submit PSubjectiveMetaObject
        JSONObject demandObjectJSON = json.getJSONObject("object");

        Calendar creationDate = PAPIUtilities.parseZulu(demandObjectJSON.getString("_creationDate"));
        Calendar lastUpdateDate = PAPIUtilities.parseZulu(demandObjectJSON.getString("_lastUpdateDate"));
        PAPIInteractionManager apiInteractionManager = new PAPIInteractionManager();
        PUser source = apiInteractionManager.getUserByID(demandObjectJSON.getString("sourceUser"));
        PUser target = this.constructUserFromJSON(demandObjectJSON.getJSONObject("targetUser"));
        String id = demandObjectJSON.getString("_id");

        return new PDemand(id, null, source, target, creationDate, lastUpdateDate);

    }

    public final PMediaSegment constructMediaSegmentFromJSON(JSONObject json) {

        int sequence = json.getInt("mediaSequence");
        int discontinuitySequence = json.getInt("discontinuitySequence");
        double timeElapsed = json.getDouble("timeElapsed");
        double duration = json.getDouble("duration");

        return new PMediaSegment(sequence, discontinuitySequence, timeElapsed, duration);

    }

    public final PPlaylistSession constructPlaylistSessionFromJSON(JSONObject json) {

        // Get config data
        JSONObject playlistSessionConfigJSON = json.getJSONObject("config");
        final int windowLength = playlistSessionConfigJSON.getInt("windowLength");
        final int maxDuration = playlistSessionConfigJSON.getInt("targetDuration");

        // Get meta data
        JSONObject playlistSessionMetaJSON = json.getJSONObject("meta");
        final String id = playlistSessionMetaJSON.getString("id");
        final boolean shouldFinish = playlistSessionMetaJSON.getBoolean("shouldFinish");
        final boolean isFinished = playlistSessionMetaJSON.getBoolean("isFinished");
        final boolean shouldBeAvailable = playlistSessionMetaJSON.getBoolean("shouldBeAvailable");
        final boolean isAvailable = playlistSessionMetaJSON.getBoolean("isAvailable");


        // Get media segments
        JSONArray mediaSegmentsJSON = json.getJSONArray("mediaSegments");
        ArrayList<PMediaSegment> mediaSegments = new ArrayList<PMediaSegment>();

        if (mediaSegmentsJSON != null) {
            // Loop through the JSON media segment objects and create Java objects

            for (int i = 0; i < mediaSegmentsJSON.length(); i++) {

                PMediaSegment mediaSegment = this.constructMediaSegmentFromJSON(mediaSegmentsJSON.getJSONObject(i));

                // Add comment to the collection product that we will return
                if (mediaSegment != null) {
                    mediaSegments.add(mediaSegment);
                }

            }
        }

        return new PPlaylistSession(id, mediaSegments, shouldFinish, isFinished, shouldBeAvailable, isAvailable, windowLength, maxDuration);

    }

    /**
     * Creates a PUser object from a JSON representation.
     * @param json is the JSONObject data that describes the user.
     * @return a valid PUser that represents the JSON data.
     */
    public final PUser constructUserFromJSON(JSONObject json) {

        // Separate the user meta and user object objects.
        JSONObject userMetaJSON = json.getJSONObject("subjectiveObjectMeta");
        JSONObject userObjectJSON = json.getJSONObject("object");
        JSONObject userProfileJSON = userObjectJSON.getJSONObject("profile");

        // Set all of the subjective meta to be used by the user object.
        PSubjectiveMeta subjectiveMeta = new PSubjectiveMeta();
        subjectiveMeta.setDemand(PSubjectiveMeta.SubjectiveMetaDirection.Backward, userMetaJSON.getJSONObject("demand").getBoolean("backward"));
        subjectiveMeta.setDemand(PSubjectiveMeta.SubjectiveMetaDirection.Forward, userMetaJSON.getJSONObject("demand").getBoolean("forward"));
        subjectiveMeta.setFriendship(PSubjectiveMeta.SubjectiveMetaDirection.Backward, userMetaJSON.getJSONObject("friendship").getBoolean("backward"));
        subjectiveMeta.setFriendship(PSubjectiveMeta.SubjectiveMetaDirection.Forward, userMetaJSON.getJSONObject("friendship").getBoolean("forward"));

        // All of the other user attributes
        String id = userObjectJSON.getString("_id");
        String username = userObjectJSON.getString("username");
        String vanityUsername = userObjectJSON.getString("displayUsername");
        String emailAddress = userObjectJSON.optString("email");
        PUserProfile profile = this.constructUserProfileFromJSON(userProfileJSON);

        Calendar creationDate = PAPIUtilities.parseZulu(userObjectJSON.getString("_creationDate"));
        Calendar lastUpdate = PAPIUtilities.parseZulu(userObjectJSON.getString("_lastUpdateDate"));

        final int numDemands = userObjectJSON.getJSONObject("demands").getInt("count");
        final int numFollowers = userObjectJSON.getJSONObject("followers").getInt("count");
        final int numFriends = userObjectJSON.getJSONObject("friends").getInt("count");
        final int numVideos = userObjectJSON.getJSONObject("videos").getInt("count");
        final int numLikes = userObjectJSON.getJSONObject("likes").getInt("count");
        final int numViews = userObjectJSON.getJSONObject("views").getInt("count");

        return new PUser(id, subjectiveMeta, creationDate, lastUpdate, username, vanityUsername, emailAddress, profile, numDemands, numFollowers, numFriends, numLikes, numVideos, numViews);

    }

    public final PUserProfile constructUserProfileFromJSON(JSONObject json) {

        String fullName = json.optString("fullName");
        String description = json.optString("description");
        String pictureURL = json.getJSONObject("picture").getString("url");
        String websiteURL = json.optString("website");
        return new PUserProfile(fullName, description, pictureURL, websiteURL);

    }

    public final PUserActivity constructUserActivityFromJSON(JSONObject activityJSON) {

        final String T_NEW_COMMENT = "newComment";
        final String T_NEW_COMMENT_MENTION = "newCommentMention";
        final String T_NEW_DEMAND = "newDemand";
        final String T_NEW_FOLLOWER = "newFollower";
        final String T_NEW_LIKE = "newLike";
        final String T_NEW_VIDEO_BY_DEMANDED_USER = "newVideoByDemandedUser";
        final String T_NEW_VIDEO_BY_FRIEND = "newVideoByFriend";
        final String T_NEW_VIDEO_MENTION = "newVideoMention";
        final String T_NEW_VIEWER = "newViewer";

        JSONObject activityObject = activityJSON.getJSONObject("object");
        String id = activityObject.getString("_id");
        String subject = activityObject.getString("subject");
        String activityType = activityObject.getString("type");
        Calendar lastUpdate = PAPIUtilities.parseZulu(activityObject.getString("_lastUpdateDate"));
        Calendar creationDate = PAPIUtilities.parseZulu(activityObject.getString("_creationDate"));
        boolean isUnread = activityObject.getBoolean("isUnread");
        PUser sourceUser = this.constructUserFromJSON(activityObject.getJSONObject("sourceUser"));
        PObjectFactory objectFactory = new PObjectFactory();
        PVideo sourceVideo = objectFactory.constructVideoFromJSON(activityObject.getJSONObject("video"));
        String targetUserID = activityObject.getString("targetUser");

        if (activityType.equals(T_NEW_COMMENT)) {
            return new PUserActivityNewComment(id, null, creationDate, lastUpdate, subject, sourceUser, sourceVideo, targetUserID, isUnread);
        }
        else if (activityType.equals(T_NEW_DEMAND)) {
            return new PUserActivityNewDemand(id, null, creationDate, lastUpdate, subject, sourceUser, sourceVideo, targetUserID, isUnread);
        }
        else if (activityType.equals(T_NEW_FOLLOWER)) {
            return new PUserActivityNewFollower(id, null, creationDate, lastUpdate, subject, sourceUser, sourceVideo, targetUserID, isUnread);
        }
        else if (activityType.equals(T_NEW_LIKE)) {
            return new PUserActivityNewLike(id, null, creationDate, lastUpdate, subject, sourceUser, sourceVideo, targetUserID, isUnread);
        }
        else if (activityType.equals(T_NEW_VIDEO_BY_DEMANDED_USER)) {
            return new PUserActivityNewVideoByDemandedUser(id, null, creationDate, lastUpdate, subject, sourceUser, sourceVideo, targetUserID, isUnread);
        }
        else if (activityType.equals(T_NEW_VIDEO_BY_FRIEND)) {
            return new PUserActivityNewVideoByFriend(id, null, creationDate, lastUpdate, subject, sourceUser, sourceVideo, targetUserID, isUnread);
        }
        else if (activityType.equals(T_NEW_VIDEO_MENTION)) {
            return new PUserActivityNewVideoMention(id, null, creationDate, lastUpdate, subject, sourceUser, sourceVideo, targetUserID, isUnread);
        }
        else if (activityType.equals(T_NEW_VIEWER)) {
            return new PUserActivityNewViewer(id, null, creationDate, lastUpdate, subject, sourceUser, sourceVideo, targetUserID, isUnread);
        }
        else if (activityType.equals(T_NEW_COMMENT_MENTION)) {
            JSONObject commentJSON = activityObject.getJSONObject("comment");
            PComment comment = objectFactory.constructCommentFromJSON(commentJSON);
            return new PUserActivityNewCommentMention(id, null, creationDate, lastUpdate, subject, comment, sourceUser, sourceVideo, targetUserID, isUnread);
        }
        else {
            return null;
        }
    }

    /**
     * Creates a PVideo object from a JSON representation.
     * @param videoJSON is the JSON object that represents the video.
     * @return a valid PVideo object if one could be created successfully.
     */
    public final PVideo constructVideoFromJSON(JSONObject videoJSON) {

        try {
            // Separate the video meta and video object objects.
            JSONObject videoMetaJSON = videoJSON.getJSONObject("subjectiveObjectMeta");
            JSONObject videoObjectJSON = videoJSON.getJSONObject("object");
            JSONObject videoMediaURLJSON = videoObjectJSON.optJSONObject("mediaUrls");

            // Set all of the subjective meta to be used by the user object.
            PSubjectiveMeta subjectiveMeta = new PSubjectiveMeta();
            subjectiveMeta.setLike(PSubjectiveMeta.SubjectiveMetaDirection.Backward, videoMetaJSON.getJSONObject("like").getBoolean("backward"));
            subjectiveMeta.setLike(PSubjectiveMeta.SubjectiveMetaDirection.Forward, videoMetaJSON.getJSONObject("like").getBoolean("forward"));

            // Date stamps
            Calendar creationDate = PAPIUtilities.parseZulu(videoObjectJSON.getString("_creationDate"));
            Calendar creationEnd = PAPIUtilities.parseZulu(videoObjectJSON.getJSONObject("creationTimeRange").optString("endDate"));
            Calendar creationStart = PAPIUtilities.parseZulu(videoObjectJSON.getJSONObject("creationTimeRange").getString("startDate"));
            Calendar lastUpdateDate = PAPIUtilities.parseZulu(videoObjectJSON.getString("_lastUpdateDate"));

            // Major video data
            String id = videoObjectJSON.getString("_id");
            String title = videoObjectJSON.optString("title");

            JSONObject creatorUserJSON = videoObjectJSON.optJSONObject("creatorUser");
            PUser creatorUser;

            if (creatorUserJSON == null) {
                PAPIInteractionManager apiInteractionManager = new PAPIInteractionManager();
                creatorUser = apiInteractionManager.getUserByID(videoObjectJSON.getString("creatorUser"));
            } else {
                creatorUser = this.constructUserFromJSON(creatorUserJSON);
            }

            URL live = null, replay = null, stillImage = null;

            if(videoMediaURLJSON != null) {
                // Media URLs
                stillImage = new URL(videoMediaURLJSON.getJSONObject("images").getString("480px"));
                replay = new URL(videoMediaURLJSON.getJSONObject("playlists").getJSONObject("replay").getString("master"));
                live = new URL(videoMediaURLJSON.getJSONObject("playlists").getJSONObject("live").getString("master"));
            }


            // Counts
            int numLikes = videoObjectJSON.getJSONObject("likes").getInt("count");
            int numViews = videoObjectJSON.getJSONObject("views").getInt("count");

            // Visibility
            boolean isAvailable = videoObjectJSON.getBoolean("isAvailable");
            PVisibility visibility = new PVisibility();
            visibility.set(PVisibility.PVisibilityEntity.Everyone, videoObjectJSON.getJSONObject("visibility").getBoolean("everyone"));

            // Comments
            PLog.info("Video object JSON is: " + videoObjectJSON.toString());
            JSONArray commentsJSON = videoObjectJSON.optJSONObject("comments").optJSONArray("results");
            ArrayList<PComment> comments = new ArrayList<PComment>();

            if (commentsJSON != null) {
                // Loop through the JSON user objects and create Java objects
                PObjectFactory pObjectFactory = new PObjectFactory();
                for (int i = 0; i < commentsJSON.length(); i++) {

                    PComment comment = pObjectFactory.constructCommentFromJSON(commentsJSON.getJSONObject(i));

                    // Add comment to the collection product that we will return
                    if (comment != null) {
                        comments.add(comment);
                    }

                }
            }

            return new PVideo(id, subjectiveMeta, creationDate, lastUpdateDate, title, creatorUser, stillImage, live, replay, numLikes, numViews, isAvailable, visibility, comments, creationStart, creationEnd);
        }
        catch (MalformedURLException e) {
            PLog.severe("create() -> Caught MalformedURLException!");
            return null;
        }
        catch (JSONException e) {
            PLog.severe("create() -> Caught JSONException!");
            return null;
        }

    }

}
