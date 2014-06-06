package tv.present.api;

import tv.present.exceptions.APIRequestStateException;
import tv.present.models.PUserContext;

import java.io.IOException;

/**
 * Test class
 */
public class Driver {
	
	public static void main(String[] args) throws IOException, APIRequestStateException {

        PAPIInteraction apiInteractionManager = new PAPIInteraction();


        PUserContext context = apiInteractionManager.getUserContext("k", "abc123");

        if (context != null ) {
            System.out.println("The user's name is: " + context.getUser().getProfile().getFullName());
        }
        else {
            System.out.println("The user context could not be got!");
        }
    }
	
}
