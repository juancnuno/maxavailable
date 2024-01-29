package com.juancnuno.maxavailable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.prefs.Preferences;

final class MaxAvailable {

    private MaxAvailable() {
    }

    public static void main(String[] args) throws Exception {
        try (var client = HttpClient.newBuilder().build()) {
            var budget = Preferences.userNodeForPackage(MaxAvailable.class).get("budget", null);

            var request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.ynab.com/v1/budgets/" + budget + "/categories"))
                    .header("Authorization", "Bearer " + args[0])
                    .build();

            var response = client.send(request, BodyHandlers.ofInputStream());
            System.out.println(CategoriesResponseParser.getMaxAvailable(response.body()));
        }
    }
}
