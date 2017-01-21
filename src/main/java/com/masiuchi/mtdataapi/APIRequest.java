package com.masiuchi.mtdataapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;

public class APIRequest {

    private Endpoint endpoint;

    public APIRequest(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public String send(String accessToken, CallOptions options) throws IOException {
        URL uri = new URL(endpoint.requestUrl(options));

        String json = "";
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) uri.openConnection();
            connection.setRequestMethod(endpoint.verb.toString());

            if (accessToken != null && !accessToken.isEmpty()) {
                connection.setRequestProperty("X-MT-Authorization", "MTAuth accessToken=" + accessToken);
            }
            if (endpoint.verb == Endpoint.Verb.POST || endpoint.verb == Endpoint.Verb.PUT) {
                String parameterString = "";
                for (String key : options.keySet()) {
                    if (!parameterString.isEmpty()) {
                        parameterString += "&";
                    }
                    parameterString += key + "=" + options.get(key);
                }

                PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
                printWriter.print(parameterString);
                printWriter.close();
            }

            int code = connection.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                return "";
            }

            InputStreamReader isr = new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8"));
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(isr);

                String line;
                while ((line = reader.readLine()) != null) {
                    json += line;
                }
            } catch (IOException e) {
                throw(e);
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    throw(e);
                } finally {
                    isr.close();
                }
            }
        } catch (IOException e) {
            throw (e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return json;
    }
}
