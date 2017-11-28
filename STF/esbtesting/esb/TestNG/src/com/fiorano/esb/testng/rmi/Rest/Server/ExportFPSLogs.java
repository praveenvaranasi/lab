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
 * Created by Meghana on 11/27/17.
 */
public class ExportFPSLogs {
    String userName="admin";
    String password="passwd";
    String context="ESB";
    String keyUri="http://localhost:1980/api/fes/security/apiKey";
    String serverName="fps";
    String zipFilePath="%2Fhome%2FMeghana%2FDocuments%2Fc.zip";

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
        //http://localhost:1980/api/fes/servers/peer/exportLogs/fps?api_key=IqclviulqiISSvsAzlrPFk-f&zipFilePath=%2Fhome%2FMeghana%2FDocuments%2Fa.zip
        String AppRunUri="http://localhost:1980/api/fes/servers/peer/exportLogs/"+serverName+"?api_key="+key+"&zipFilePath="+zipFilePath;
        output = restCall1(AppRunUri);
        System.out.println(output);
        String wrongInput="xyz";

        //***Invalid peer server name provided
        AppRunUri="http://localhost:1980/api/fes/servers/peer/exportLogs/"+wrongInput+"?api_key="+key+"&zipFilePath="+zipFilePath;
        output = restCall1(AppRunUri);
        System.out.println(output);

        //***Invalid path is given
        wrongInput="/home/avc/asdfdf";
        AppRunUri="http://localhost:1980/api/fes/servers/peer/exportLogs/"+serverName+"?api_key="+key+"&zipFilePath="+wrongInput;
        output = restCall1(AppRunUri);
        System.out.println(output);
    }
}
