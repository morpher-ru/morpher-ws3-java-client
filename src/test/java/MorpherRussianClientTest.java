import Exceptions.MorpherException;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.RealSystem;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Created by Kraken on 26.09.2017.
 */


public class MorpherRussianClientTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(80);

    @Test
    public void test1() {

        stubFor(get(urlEqualTo("http://ws3.morpher.ru/russian/"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("Hello world!")
                .withStatus(407)));

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ws3.morpher.ru/russian/")
                .build();

        try {

            Response response = client.newCall(request).execute();
            String str = response.body().toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
