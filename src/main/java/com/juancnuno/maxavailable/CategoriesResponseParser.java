package com.juancnuno.maxavailable;

import java.io.InputStream;
import java.util.Comparator;
import java.util.prefs.Preferences;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParser.Event;

final class CategoriesResponseParser {

    private final JsonParser parser;

    CategoriesResponseParser(InputStream in) {
        parser = Json.createParser(in);
    }

    Object getMaxAvailable() {
        next(Event.START_OBJECT);
        nextData();
        nextCategoryGroups();

        return findCategoryGroup().getJsonArray("categories").stream()
                .map(JsonValue::asJsonObject)
                .map(Category::new)
                .max(Comparator.naturalOrder())
                .orElseThrow();
    }

    private void nextData() {
        nextKeyName("data");
        next(Event.START_OBJECT);
    }

    private void nextCategoryGroups() {
        nextKeyName("category_groups");
        next(Event.START_ARRAY);
    }

    private JsonObject findCategoryGroup() {
        JsonObject group;
        var id = Preferences.userNodeForPackage(MaxAvailable.class).get("category_group", null);

        for (group = getCategoryGroup(); !group.getString("id").equals(id);
                group = getCategoryGroup()) {
        }

        return group;
    }

    private JsonObject getCategoryGroup() {
        next(Event.START_OBJECT);
        return parser.getObject();
    }

    private void nextKeyName(String expectedName) {
        next(Event.KEY_NAME);

        var actualName = parser.getString();
        assert actualName.equals(expectedName) : actualName;
    }

    private void next(Event expectedEvent) {
        var actualEvent = parser.next();
        assert actualEvent.equals(expectedEvent) : actualEvent;
    }
}
