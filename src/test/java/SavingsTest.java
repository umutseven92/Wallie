import org.junit.Test;
import org.junit.Assert;
import Helpers.Saving;

import java.math.BigDecimal;

/**
 * Created by Umut Seven on 5.2.2015, for Graviton.
 */

public class SavingsTest {

    @Test
    public void DescriptionTest()
    {
        Saving saving = new Saving();
        String desc = "Araba için, 1 yıl 4 ay 4 gün sonunda 350.00 TL birikim."; // 489 gün
        String methodDesc = saving.CreateDescription("Araba", 489, new BigDecimal(350));

        Assert.assertEquals(desc, methodDesc);
    }
}
