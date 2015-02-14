import org.junit.Test;
import org.junit.Assert;
import Helpers.Saving;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Umut Seven on 5.2.2015, for Graviton.
 */

public class SavingsTest {

    @Test
    public void DescriptionTest() {
        Saving saving = new Saving("Araba", new BigDecimal(350), new Date(), Saving.Period.ThreeMonths, false);
        String desc = "Araba için, 3 ay sonunda 350.00 TL birikim.";
        String methodDesc = saving.GetDescription();

        Assert.assertEquals(desc, methodDesc);
    }
}
