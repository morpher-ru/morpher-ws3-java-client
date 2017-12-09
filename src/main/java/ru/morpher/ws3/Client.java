package ru.morpher.ws3;

import ru.morpher.ws3.russian.RussianClient;
import ru.morpher.ws3.ukrainian.UkrainianClient;
import com.fasterxml.jackson.core.type.TypeReference;
import ru.morpher.ws3.communicator.Communicator;
import ru.morpher.ws3.communicator.HttpURLConnectionCommunicator;
import ru.morpher.ws3.communicator.LanguagePathCommunicator;
import ru.morpher.ws3.communicator.PrefixAppender;
import ru.morpher.ws3.communicator.UrlAuthCommunicator;
import ru.morpher.ws3.communicator.ConnectionHandler;

import java.io.IOException;
import java.util.Collections;

import static ru.morpher.ws3.communicator.Communicator.METHOD_GET;

/**
 * <p>
 * Базовый класс, предоставляющий доступ к API веб-сервиса
 * <p>
 * token - access-token при использовании платной версии веб-сервиса
 * url -  endpoint url сервиса
 * ru.morpher.ws3.communicator - кастомная имплементация коммуникатора, также может использоваться для проведения unit-тестов
 */
public class Client {

    private RussianClient russianClient;
    private UkrainianClient ukrainianClient;
    private LanguagePathCommunicator communicator;

    Client(LanguagePathCommunicator communicator) {
        this.russianClient = new RussianClient(new PrefixAppender(communicator, "russian"));
        this.ukrainianClient = new UkrainianClient(new PrefixAppender(communicator, "ukrainian"));
        this.communicator = communicator;
    }

    public RussianClient russian() {
        return russianClient;
    }

    public UkrainianClient ukrainian() {
        return ukrainianClient;
    }

    public int queriesLeftForToday() throws IOException, AccessDeniedException {
        TypeReference<Integer> responseType = new TypeReference<Integer>() {
        };

        String operation = "get_queries_left_for_today";
        return communicator.sendRequest(operation, Collections.<String, String>emptyMap(), METHOD_GET, responseType);
    }

}
