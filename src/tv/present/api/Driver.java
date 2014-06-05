package tv.present.api;

import tv.present.exceptions.APIRequestStateException;
import tv.present.models.PUserContext;

import java.io.IOException;

public class Driver {
	
	public static void main(String[] args) throws IOException, APIRequestStateException {

        PAPIInteractionManager apiInteractionManager = new PAPIInteractionManager();


        PUserContext context = apiInteractionManager.getUserContext("k", "abc123");

        if (context != null ) {
            System.out.println("The user's name is: " + context.getUser().getProfile().getFullName());
        }
        else {
            System.out.println("The user context could not be got!");
        }

    }
	
}
