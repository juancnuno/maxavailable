package com.juancnuno.maxavailable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

final class MaxAvailable {

    private MaxAvailable() {
    }

    public static void main(String[] args) throws Exception {
        try (var client = HttpClient.newBuilder().build()) {
            var request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.ynab.com/v1/budgets"))
                    .header("Authorization", "Bearer " + args[0])
                    .build();

            var response = client.send(request, BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(response.body());
        }
    }
}
