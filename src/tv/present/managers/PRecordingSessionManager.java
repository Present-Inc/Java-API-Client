package tv.present.managers;

import org.json.JSONObject;
import tv.present.api.PAPIBridge;
import tv.present.exceptions.APIRequestStateException;
import tv.present.factories.PObjectFactory;
import tv.present.models.PPlaylistSession;
import tv.present.models.PUserContext;
import tv.present.models.PVideo;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Present Recording Session Manager
 * June 04, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
// We will suppress unused warnings on the entire class because we know at some point the class will be used (ie:
// when the app goes to record a Present).
@SuppressWarnings("unused")
public final class PRecordingSessionManager {

    private static final String TAG = "tv.present.managers.PRecordingSessionManager";
    private static final Logger PLog = Logger.getLogger(TAG);

    private PVideo video;
    private PPlaylistSession playlistSession;
    private PUserContext userContext;
    private boolean appendOnly = false;

    /**
     * Constructs a PRecordingSessionManager.
     * @param userContext is the UserContext to use.
     */
    public PRecordingSessionManager(PUserContext userContext) {
        this.userContext = userContext;
    }

    /**
     * Creates a video session.
     * @param title is the String title of the video session.
     * @throws tv.present.exceptions.APIRequestStateException when the session manager falls into the wrong request state (ie: generally this
     *         happens when you call createVideo() twice when, you should really be appending after the first call).
     */
    @SuppressWarnings("unused")
    public final void createVideo(String title) throws APIRequestStateException {

        if (!this.appendOnly) {

            JSONObject requestParams = new JSONObject();
            requestParams.put("title", title);

            PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.POST, "videos/create", this.userContext, requestParams);

            JSONObject response = connector.makeRequest();


            // Don't continue if we don't have a valid result code.
            if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {

                PObjectFactory objectFactory = new PObjectFactory();
                PLog.info("createVideo() -> Response from server: " + response.toString());

                JSONObject videoRootJSON = response.getJSONObject("result");
                JSONObject playlistSessionJSON = videoRootJSON.getJSONObject("playlistSession");
                this.video = objectFactory.constructVideoFromJSON(videoRootJSON);
                this.playlistSession = objectFactory.constructPlaylistSessionFromJSON(playlistSessionJSON);

                // After we create the video, we should only be able to append to it
                if (this.video != null) {
                    this.appendOnly = true;
                }

            }

            // Return null if we are unable to create a video
            else {
                PLog.severe("createVideo() -> Error creating video.  Response is: " + response.toString());
            }

        }

        else {
            PLog.severe("createVideo() -> Function already called for this session.");
            throw new APIRequestStateException("Method createVideo() has already been called for this recording session!");
        }


    }

    /**
     * Appends a segment of video to the end of the existing segments in this session.
     * @param filePath is the String file path of the video to append.
     * @throws IOException when the video does not exist at the given file path.
     */
    @SuppressWarnings("unused")
    public final void appendSegment(String filePath) throws IOException {

        JSONObject requestParams = new JSONObject();
        requestParams.put("video_id", this.video.getID());
        requestParams.put("media_sequence", this.playlistSession.getNumMediaSegments() + 1);
        requestParams.put("playlist_session", this.playlistSession.toJSON().toString());

        PAPIBridge connector = new PAPIBridge(PAPIBridge.HTTPRequestMethod.POST_MULTIPART, "videos/append", this.userContext, requestParams);

        File mediaSegment = new File(filePath);
        connector.addMultipartFile("media_segment", mediaSegment);

        JSONObject response = connector.makeRequest();

        // Don't continue if we don't have a valid result code.
        if (connector.getResponseCode() <= PAPIBridge.MAX_SUCCESS_CODE) {

            PLog.info("append() --> Response from server: " + response.toString());
            PObjectFactory pObjectFactory = new PObjectFactory();
            JSONObject videoRootJSON = response.getJSONObject("result");
            JSONObject playlistSessionJSON = videoRootJSON.getJSONObject("playlistSession");
            PObjectFactory objectFactory = new PObjectFactory();
            this.video = objectFactory.constructVideoFromJSON(videoRootJSON);
            this.playlistSession = pObjectFactory.constructPlaylistSessionFromJSON(playlistSessionJSON);

        }

        // Return null if we are unable to create a video
        else {
            PLog.severe("createVideo() -> Error appending video.  Response is: " + response.toString());
        }

    }

}

