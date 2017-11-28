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
 * Created by Meghana on 11/21/17.
 */
public class clearServerLogs {
    String userName="admin";
    String password="passwd";
    String context="ESB";
    String keyUri="http://localhost:1980/api/fes/security/apiKey";
    String serverMode="FES";
    String serverName="fes";
    String operation="ESB_OUT";
    int noOfLines=5;

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
        ClientResponse cr = webResource.type("application/json").post(ClientResponse.class);
        String response = cr.getEntity(String.class);
        sleep(20000);
        return response;
    }

    public String restCall(String uri) throws InterruptedException {
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
            //"http://localhost:1980/api/fes/servers/logs/FES/fes/ESB_ERROR/5?api_key=riWGzALQlHydqEIuy-MKd-Jf"
            String RunUri = "http://localhost:1980/api/fes/servers/logs/" + serverMode + "/" + serverName + "/" + operation + "?api_key=" + key;
            output = restCall1(RunUri);
            //http://localhost:1980/api/fes/servers/logs/FES/fes/ESB_OUT/5?api_key=TLvZlhNeTCnPYZwatswCyerc
            RunUri = "http://localhost:1980/api/fes/servers/logs/" + serverMode + "/" + serverName + "/" + operation + "/" + noOfLines + "?api_key=" + key;
            output = restCall(RunUri);
            System.out.println(output);
            String wrongInput="xyz";

            //***Incorrect server mode provided
        RunUri = "http://localhost:1980/api/fes/servers/logs/" + wrongInput + "/" + serverName + "/" + operation + "?api_key=" + key;
        output = restCall1(RunUri);
        System.out.println(output);

        //***Incorrect server name provided
        RunUri = "http://localhost:1980/api/fes/servers/logs/" + serverMode + "/" + wrongInput + "/" + operation + "?api_key=" + key;
        output = restCall1(RunUri);
        System.out.println(output);

        //***Incorrect operation provided
        RunUri = "http://localhost:1980/api/fes/servers/logs/" + serverMode + "/" + serverName + "/" + wrongInput + "?api_key=" + key;
        output = restCall1(RunUri);
        System.out.println(output);


    }
}
