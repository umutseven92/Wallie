import Helpers.SavingsHelper;
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

        String cur = "TRY";
        Saving saving = new Saving("Araba", new BigDecimal(350), new Date(), Saving.Period.ThreeMonths, false, cur);
        String desc = "Araba için, 3 ay sonunda 350.00 " + cur + " birikim.";
        String methodDesc = saving.GetDescription();

        //Assert.assertEquals(desc, methodDesc);
    }

    @Test
    public void DailyLimitTest() {
        Saving saving = new Saving("Test", new BigDecimal(50), new Date(), Saving.Period.Week, false, "TRY");
        BigDecimal expectedLimit = new BigDecimal(12.86).setScale(2,BigDecimal.ROUND_DOWN);
        BigDecimal limit = SavingsHelper.CalculateDailyLimit(new BigDecimal(140), saving);

        // Kucuk (0.01) kadar farklar olabiliyor

        Assert.assertEquals(expectedLimit, limit);
    }

}
