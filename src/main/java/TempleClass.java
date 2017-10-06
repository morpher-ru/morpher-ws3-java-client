import Domain.Russian.DeclensionResult;
import Exceptions.MorpherException;

/**
 * Created by Kraken on 09.09.2017.
 */
public class TempleClass {

    public static void main(String[] argv) {

        MorpherClient morpherClient = new MorpherClient.ClientBuilder()
                .useToken("lol")
                .build();

        try {
            DeclensionResult result = morpherClient.getRussian().declension("<Кладиков Степан Алексеевич>>");
        } catch (MorpherException e) {
            e.printStackTrace();
        }

    }
}
