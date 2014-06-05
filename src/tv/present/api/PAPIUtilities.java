package tv.present.api;

import tv.present.api.PAPIBridge.HTTPRequestMethod;
import tv.present.models.PUserProfile.Gender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PAPIUtilities {
	
	public static Calendar parseZulu(String date) {
		try {
			if (date.equals("")) {
				Date oDate = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(date.replaceAll("Z$", "+0000"));
				Calendar product = Calendar.getInstance();
				product.setTime(oDate);
				return product;
			}
			else {
				return null;
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static String genderToString(Gender gender) {
		if (gender == Gender.Female) {
			return "Female";
		}
		else {
			return "Male";
		}
	}

    @SuppressWarnings("unused")  // Don't care that it's unused.  Sometimes it is.
    public static String requestMethodToString(HTTPRequestMethod method) {

        if (method == HTTPRequestMethod.GET) {
            return "GET";
        }
        else if (method == HTTPRequestMethod.POST) {
            return "POST";
        }
        else if (method == HTTPRequestMethod.POST_MULTIPART) {
            return "POST_MULTIPART";
        }
        else {
            return "UNKNOWN";
        }

    }


}
