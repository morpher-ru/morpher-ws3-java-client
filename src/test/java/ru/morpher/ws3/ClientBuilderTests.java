package ru.morpher.ws3;

import org.junit.Test;
import ru.morpher.ws3.communicator.CommunicatorStub;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ClientBuilderTests {

    @Test
    public void CallingBuildTwiceWorks() throws IOException, AccessDeniedException {
        String baseUrl = "https://ws3.morpher.ru";
        CommunicatorStub communicator = new CommunicatorStub();
        ClientBuilder builder = new ClientBuilder().use(communicator).useToken("token");
        builder.build();
        Client client = builder.build();
        communicator.writeNextResponse("1");
        client.queriesLeftForToday();
        assertEquals(baseUrl + "/get_queries_left_for_today?token=token", communicator.readLastUrlPassed());
    }
}
