import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Created by Kraken on 11.09.2017.
 */
public class MorpherClientTest {

    /**
     * Тестирование сборки клиента.
     * Без обработки исключений
     */
    @Test
    public void testBuildClient() {
        //Тестовые данные
        String url = "some url";
        String token = "some token";

        SoftAssert softAssert = new SoftAssert();

        MorpherClient morpherClient = new MorpherClient.ClientBuilder()
                .useUrl(url)
                .useToken(token)
                .build();

        softAssert.assertEquals(morpherClient.getHttpClient(), null);
        softAssert.assertEquals(morpherClient.getUrl(), url);
        softAssert.assertEquals(morpherClient.getToken(), token);

        softAssert.assertAll();

    }
}
