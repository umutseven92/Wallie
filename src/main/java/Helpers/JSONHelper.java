package Helpers;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Umut Seven on 13.2.2015, for Graviton.
 */
public class JSONHelper {

    public static JSONObject CreateStartingJSON(String firstName, String lastName) throws JSONException {

        String userSettings = String.format("{\n" +
                "\t\"user\": {\n" +
                "\t\t\"birthDate\": \"1992-08-05\",\n" +
                "\t\t\"name\": \"%s\",\n" +
                "\t\t\"lastName\": \"%s\",\n" +
                "\t\t\"city\": \"Istanbul\",\n" +
                "\t\t\"email\": \"umutseven92@gmail.com\",\n" +
                "\n" +
                "\t\t\"incomes\": [],\n" +
                "\t\t\"expenses\": [],\n" +
                "\t\t\"savings\": []\n" +
                "\t}\n" +
                "}\n", firstName, lastName);

        return new JSONObject(userSettings);
    }
}
