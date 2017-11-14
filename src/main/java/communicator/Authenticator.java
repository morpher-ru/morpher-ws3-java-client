package communicator;

public class Authenticator {

    private final String token;

    public Authenticator(String token) {
        this.token = token;
    }

    public String addAuthDataToUr(String url) {
        //TODO: migrate to Basic auth and avoid logic with ? and &
        if (token != null) {
            url = url + "?" + "token=" + token;
        }

        return url;
    }
}
