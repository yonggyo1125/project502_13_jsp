package org.choongang.pokemon.api.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;

public class ApiTest {
    @Test
    @DisplayName("Resource Lists/Pagination API 테스트")
    void resourceListsTest() throws Exception {
        //String url = "https://pokeapi.co/api/v2/ability/";
        //String url = "https://pokeapi.co/api/v2/evolution-chain";
        String url = "https://pokeapi.co/api/v2/pokemon-species";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                    .newBuilder(URI.create(url))
                    .build();
        HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString(Charset.forName("UTF-8")));

        String body = res.body();
        System.out.println(body);
    }


}
