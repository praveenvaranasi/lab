package com.fiorano.esb.testng.rmi.Rest.Server;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.Test;
import java.io.IOException;
import java.net.URISyntaxException;
import static java.lang.Thread.sleep;

/**
 * Created by Meghana on 11/22/17.
 */
public class GatewayJettyURL {
    String userName="admin";
    String password="passwd";
    String context="API";
    String keyUri="http://localhost:1981/api/ams/security/apiKey";
    String serverName="apigateway";

    public String restCall(String uri,String input) throws InterruptedException {
        Client client = Client.create();
        WebResource webResource = client.resource(uri);
        ClientResponse cr = webResource.type("application/json").post(ClientResponse.class, input);
        String response = cr.getEntity(String.class);
        sleep(20000);
        return response;
    }

    public String restCall1(String uri) throws InterruptedException {
        Client client = Client.create();
        WebResource webResource = client.resource(uri);
        ClientResponse cr = webResource.type("application/json").get(ClientResponse.class);
        String response = cr.getEntity(String.class);
        sleep(20000);
        return response;
    }

    @Test
    public void execute() throws URISyntaxException, JSONException, IOException, InterruptedException {

        String input = "{\n" + "  \"userName\":\""+userName+ "\",\n" + "  \"password\":\""+password+"\",\n" + "  \"context\":\""+context+"\""+"\n" + "}";
        String output=restCall(keyUri,input);

        //Getting Api security Key from response
        JSONObject jsonObj = new JSONObject(output);
        String key=jsonObj.getString("response");
        //http://localhost:1980/api/fes/servers/fps/gatewayJettyURL/ags?api_key=bsAqomiQqMsUcKJd-KHQlanS
        String AppRunUri="http://localhost:1981/api/ams/servers/fps/gatewayJettyURL/"+serverName+"?api_key="+key;
        output = restCall1(AppRunUri);
        System.out.println(output);

        //**Invalid server name
        serverName="adfpt";
        AppRunUri="http://localhost:1981/api/ams/servers/fps/gatewayJettyURL/"+serverName+"?api_key="+key;
        output = restCall1(AppRunUri);
        System.out.println(output);
    }
}
