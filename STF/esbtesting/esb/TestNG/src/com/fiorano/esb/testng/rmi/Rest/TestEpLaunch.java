package com.fiorano.esb.testng.rmi.Rest;


import org.testng.annotations.Test;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URISyntaxException;
import static java.lang.Thread.sleep;
import static org.testng.Assert.assertEquals;

public class TestEpLaunch {
    String userName="admin";
    String password="passwd";
    String context="ESB";

    String appName="SIMPLECHAT";
    String appVersion="1.0";
    String keyUri="http://localhost:1980/api/fes/security/apiKey";

    public ClientResponse restCall(String uri,String input) throws InterruptedException {
        Client client = Client.create();
        WebResource webResource = client.resource(uri);
        ClientResponse cr = webResource.type("application/json").post(ClientResponse.class, input);
        sleep(20000);
        return cr;
    }
    @Test
    public void execute() throws URISyntaxException, JSONException, IOException, InterruptedException {

        String input = "{\n" + "  \"userName\":\""+userName+ "\",\n" + "  \"password\":\""+password+"\",\n" + "  \"context\":\""+context+"\""+"\n" + "}";
        ClientResponse cr=restCall(keyUri,input);
        String output = cr.getEntity(String.class);
        assertEquals(200,cr.getStatusInfo().getStatusCode());

        //Getting Api security Key from response
        JSONObject jsonObj = new JSONObject(output);
        String key=jsonObj.getString("response");

        //Launching EP using the generated key
        String AppRunUri="http://localhost:1980/api/fes/applications/"+appName+"/"+appVersion+"?api_key="+key;
        input="{\n" + "  \"action\": \"start\"\n" + "}";
        cr=restCall(AppRunUri,input);
        assertEquals(200,cr.getStatusInfo().getStatusCode());

        //Stopping Ep
        input="{\n" + "  \"action\": \"stop\"\n" + "}";
        cr=restCall(AppRunUri,input);
        assertEquals(200,cr.getStatusInfo().getStatusCode());
    }
}
