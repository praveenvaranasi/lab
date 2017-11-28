package com.fiorano.esb.testng.rmi.Rest.Server;

import org.testng.annotations.Test;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.io.FileInputStream;
import java.util.Properties;

import static java.lang.Thread.sleep;

/**
 * Created by Meghana on 11/21/17.
 */
public class ChangeTPSDeploymentLabel {

    String userName="admin";
    String password="passwd";
    String context="ESB";
    String keyUri="http://localhost:1980/api/fes/security/apiKey";
    String serverName="fps";
    String envLabel="Testing";

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


            String input = "{\n" + "  \"userName\":\"" + userName + "\",\n" + "  \"password\":\"" + password + "\",\n" + "  \"context\":\"" + context + "\"" + "\n" + "}";
            String output = restCall(keyUri, input);

            //Getting Api security Key from response
            JSONObject jsonObj = new JSONObject(output);
            String key = jsonObj.getString("response");
            //http://localhost:1980/api/fes/servers/peer/deploymentLabel/modification/fps?api_key=dfeZCmskIGtXwptnfMylkDcj&deploymentlabel=Development
            String RunUri = "http://localhost:1980/api/fes/servers/peer/deploymentLabel/modification/"+serverName+"?api_key=" + key + "&deploymentlabel=" + envLabel;
            output = restCall1(RunUri);
            //http://localhost:1980/api/fes/servers/peer/deploymentLabel/fps?api_key=DHXFRJSYP-YNMmqjMwUblJCL
            RunUri = "http://localhost:1980/api/fes/servers/peer/deploymentLabel/" + serverName + "?api_key=" + key;
            output = restCall1(RunUri);
            System.out.println(output);
            assert output.equals(envLabel) : "Error in changing TPS Deployment Label";

            //****When incorrect deployment label is provided
            envLabel="xyz";
            RunUri = "http://localhost:1980/api/fes/servers/peer/deploymentLabel/modification/"+serverName+"?api_key=" + key + "&deploymentlabel=" + envLabel;
            output = restCall1(RunUri);
            System.out.println(output);

            //****When incorrect server name is provided
            envLabel="Testing";
            serverName="xyz";
            RunUri = "http://localhost:1980/api/fes/servers/peer/deploymentLabel/modification/"+serverName+"?api_key=" + key + "&deploymentlabel=" + envLabel;
            output = restCall1(RunUri);
            System.out.println(output);
    }
}
