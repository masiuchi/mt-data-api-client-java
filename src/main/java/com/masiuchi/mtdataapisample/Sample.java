package com.masiuchi.mtdataapisample;

import com.masiuchi.mtdataapi.APIResponse;
import com.masiuchi.mtdataapi.CallOptions;
import com.masiuchi.mtdataapi.Client;
import com.masiuchi.mtdataapi.ClientOptions;

public class Sample {
    public static String baseUrl = "http://localhost:5000/mt-data-api.cgi";

    public static void main(String args[]) {
        ClientOptions clientOptions = new ClientOptions();
        clientOptions.baseUrl = baseUrl;
        clientOptions.clientId = "mt-data-api-java";
        Client client = new Client(clientOptions);

        CallOptions callOptions = null;
        APIResponse res;
        try {
            res = client.call("list_sites", callOptions);
        } catch (Exception e) {
            e.printStackTrace();
            res = null;
        }
        if (res != null) {
            System.out.println(res.items());
        }
    }
}
