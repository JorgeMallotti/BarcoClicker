package com.example.pildoraclicker;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ApiClient {
    public interface SessionCallback {
        void onSuccess(String sessionToken);
        void onError(String errorMessage);
    }

    public interface LeaderboardCallback {
        void onSuccess(List<LeaderboardEntry> entries);
        void onError(String errorMessage);
    }

    public interface SubmitScoreCallback {
        void onSuccess(double storedTimeSeconds);
        void onError(String errorMessage);
    }

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static final int CONNECT_TIMEOUT_MS = 10_000;
    private static final int READ_TIMEOUT_MS = 10_000;

    private ApiClient() {
    }

    public static void startSession(Context context, SessionCallback callback) {
        EXECUTOR.execute(() -> {
            HttpURLConnection connection = null;

            try {
                connection = openConnection("/api/start-session", "POST");
                int statusCode = connection.getResponseCode();
                String body = readBody(connection, statusCode);

                if (statusCode < 200 || statusCode >= 300) {
                    callback.onError(extractError(body, context.getString(R.string.error_start_session)));
                    return;
                }

                JSONObject json = new JSONObject(body);
                callback.onSuccess(json.optString("session_token", ""));
            } catch (IOException | JSONException error) {
                callback.onError(context.getString(R.string.error_network_generic));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    public static void fetchLeaderboard(Context context, LeaderboardCallback callback) {
        EXECUTOR.execute(() -> {
            HttpURLConnection connection = null;

            try {
                connection = openConnection("/api/leaderboard", "GET");
                int statusCode = connection.getResponseCode();
                String body = readBody(connection, statusCode);

                if (statusCode < 200 || statusCode >= 300) {
                    callback.onError(extractError(body, context.getString(R.string.error_fetch_leaderboard)));
                    return;
                }

                JSONObject json = new JSONObject(body);
                JSONArray leaderboard = json.optJSONArray("leaderboard");
                List<LeaderboardEntry> entries = new ArrayList<>();

                if (leaderboard != null) {
                    for (int index = 0; index < leaderboard.length(); index++) {
                        JSONObject item = leaderboard.getJSONObject(index);
                        entries.add(new LeaderboardEntry(
                                item.optInt("id"),
                                item.optString("player_name", ""),
                                item.optDouble("time_seconds", 0.0)
                        ));
                    }
                }

                LeaderboardCache.setEntries(entries);
                callback.onSuccess(entries);
            } catch (IOException | JSONException error) {
                callback.onError(context.getString(R.string.error_network_generic));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    public static void submitScore(
            Context context,
            String playerName,
            int timeSeconds,
            String sessionToken,
            String deviceId,
            SubmitScoreCallback callback
    ) {
        EXECUTOR.execute(() -> {
            HttpURLConnection connection = null;

            try {
                connection = openConnection("/api/submit-score", "POST");
                connection.setDoOutput(true);

                JSONObject payload = new JSONObject();
                payload.put("player_name", playerName);
                payload.put("time_seconds", timeSeconds);
                payload.put("session_token", sessionToken);
                payload.put("device_id", deviceId);

                writeBody(connection, payload.toString());

                int statusCode = connection.getResponseCode();
                String body = readBody(connection, statusCode);

                if (statusCode < 200 || statusCode >= 300) {
                    callback.onError(extractError(body, context.getString(R.string.error_submit_score)));
                    return;
                }

                JSONObject json = new JSONObject(body);
                JSONObject score = json.optJSONObject("score");
                double storedTimeSeconds = score != null ? score.optDouble("time_seconds", 0.0) : 0.0;
                callback.onSuccess(storedTimeSeconds);
            } catch (IOException | JSONException error) {
                callback.onError(context.getString(R.string.error_network_generic));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    private static HttpURLConnection openConnection(String path, String method) throws IOException {
        URL url = new URL(BuildConfig.API_BASE_URL + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
        connection.setReadTimeout(READ_TIMEOUT_MS);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        return connection;
    }

    private static void writeBody(HttpURLConnection connection, String body) throws IOException {
        try (OutputStream outputStream = connection.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            writer.write(body);
            writer.flush();
        }
    }

    private static String readBody(HttpURLConnection connection, int statusCode) throws IOException {
        InputStream stream = statusCode >= 200 && statusCode < 400
                ? connection.getInputStream()
                : connection.getErrorStream();

        if (stream == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }

        return builder.toString();
    }

    private static String extractError(String body, String fallback) {
        if (body == null || body.isEmpty()) {
            return fallback;
        }

        try {
            JSONObject json = new JSONObject(body);
            String error = json.optString("error", "").trim();
            return error.isEmpty() ? fallback : error;
        } catch (JSONException ignored) {
            return fallback;
        }
    }
}