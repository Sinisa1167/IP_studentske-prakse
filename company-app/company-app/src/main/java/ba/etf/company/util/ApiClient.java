package ba.etf.company.util;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static String get(String endpoint, String token) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + endpoint)
                .get();
        if (token != null) builder.header("Authorization", "Bearer " + token);
        try (Response response = client.newCall(builder.build()).execute()) {
            return response.body() != null ? response.body().string() : "";
        }
    }

    public static String post(String endpoint, Object body, String token) throws IOException {
        String json = mapper.writeValueAsString(body);
        RequestBody requestBody = RequestBody.create(json, JSON);
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(requestBody);
        if (token != null) builder.header("Authorization", "Bearer " + token);
        try (Response response = client.newCall(builder.build()).execute()) {
            return response.body() != null ? response.body().string() : "";
        }
    }

    public static String postForm(String endpoint, String json, String token) throws IOException {
        RequestBody requestBody = RequestBody.create(json, JSON);
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(requestBody);
        if (token != null) builder.header("Authorization", "Bearer " + token);
        try (Response response = client.newCall(builder.build()).execute()) {
            return response.body() != null ? response.body().string() : "";
        }
    }

    public static String put(String endpoint, Object body, String token) throws IOException {
        String json = mapper.writeValueAsString(body);
        RequestBody requestBody = RequestBody.create(json, JSON);
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + endpoint)
                .put(requestBody);
        if (token != null) builder.header("Authorization", "Bearer " + token);
        try (Response response = client.newCall(builder.build()).execute()) {
            return response.body() != null ? response.body().string() : "";
        }
    }

    public static String patch(String endpoint, String token) throws IOException {
        RequestBody requestBody = RequestBody.create("", JSON);
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + endpoint)
                .patch(requestBody);
        if (token != null) builder.header("Authorization", "Bearer " + token);
        try (Response response = client.newCall(builder.build()).execute()) {
            return response.body() != null ? response.body().string() : "";
        }
    }

    public static String delete(String endpoint, String token) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + endpoint)
                .delete();
        if (token != null) builder.header("Authorization", "Bearer " + token);
        try (Response response = client.newCall(builder.build()).execute()) {
            return response.body() != null ? response.body().string() : "";
        }
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }
}