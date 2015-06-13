package Helpers;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Umut Seven on 13.2.2015, for Graviton.
 */
public class JSONHelper {

    public static JSONObject CreateStartingJSON(String firstName, String lastName, String currency, String pro, String notification, String remNotification, String savNotHour, String remNotHour, String statusNot, String autoBackup, String locale) throws JSONException {

        String userSettings = String.format("{\n" +
                "\t\"user\": {\n" +
                "\t\t\"name\": \"%s\",\n" +
                "\t\t\"lastName\": \"%s\",\n" +
                "\t\t\"currency\": \"%s\",\n" +
                "\t\t\"pro\": \"%s\",\n" +
                "\t\t\"notifications\": \"%s\",\n" +
                "\t\t\"remNotifications\": \"%s\",\n" +
                "\t\t\"savNotHour\": \"%s\",\n" +
                "\t\t\"remNotHour\": \"%s\",\n" +
                "\t\t\"statusNot\": \"%s\",\n" +
                "\t\t\"autoBackup\": \"%s\",\n" +
                "\t\t\"locale\": \"%s\",\n" +
                "\n" +
                "\t\t\"incomes\": [],\n" +
                "\t\t\"expenses\": [],\n" +
                "\t\t\"savings\": [],\n" +
                "\t\t\"incomeCustoms\": [],\n" +
                "\t\t\"expenseCustoms\": []\n" +
                "\t}\n" +
                "}\n", firstName, lastName, currency, pro, notification, remNotification, savNotHour, remNotHour, statusNot, autoBackup, locale);

        return new JSONObject(userSettings);
    }
}
