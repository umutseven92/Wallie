import Helpers.JSONHelper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Umut Seven on 13.2.2015, for Graviton.
 */
public class JSONTest {

    @Test
    public void CreateJSONValidTest() {
        boolean valid;

        try {
            JSONObject jsonToTest = JSONHelper.CreateStartingJSON("user", "name", "TRY", "false", "false", "false", "8", "14", "true", "false", "tr");
            valid = true;
        } catch (JSONException e) {
            valid = false;
        }

        Assert.assertEquals(valid, true);
    }
}
