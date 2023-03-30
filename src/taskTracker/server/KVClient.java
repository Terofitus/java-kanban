package server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVClient {
    private final URI uri;
    HttpClient client;
    private final String API_TOKEN;

    public KVClient(String anotherUri) throws IOException, InterruptedException {
        uri = URI.create(anotherUri);
        client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri.resolve("register")).GET().build();
        HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        API_TOKEN = httpResponse.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder(uri.resolve("/save/" + key + "?API_TOKEN=" + API_TOKEN))
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();
        client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public String load(String key) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder(uri.resolve("/load/" + key + "?API_TOKEN=" + API_TOKEN))
                .GET().build();
        HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return httpResponse.body();
    }
}
