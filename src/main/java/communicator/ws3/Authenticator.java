package communicator.ws3;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

public class Authenticator {
    static final String HEADER_AUTHORIZATION = "Authorization";

    private final String token;

    public Authenticator(String token) {
        this.token = token;
    }

    void populateAuthHeader(HttpURLConnection con) {
        if(token == null || token.trim().length() == 0){
            return;
        }

        try {
            byte[] encodeBase64 = Base64.encodeBase64((token).getBytes());
            con.setRequestProperty(HEADER_AUTHORIZATION, "Basic " + new String(encodeBase64, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
