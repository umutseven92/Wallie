import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Umut Seven on 14.3.2015, for Graviton.
 */
public class CurrencyTest {

    @Test
    public void CurrencyCodeTest() {
        String currency = "Czech Republic Koruna (CZK)";
        String expectedCode = "CZK";

        String code = currency.substring(currency.indexOf("(") + 1, currency.indexOf(")"));

        Assert.assertEquals(code.toCharArray().length, 3);
        Assert.assertEquals(code, expectedCode);
    }
}
