package com.example.together.dboperations;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import com.example.together.model.Task;
import com.example.together.model.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PhotoUploader extends AsyncTask<Uri, Void, String> {
    private Context context;
    private Uri photoUri;
    private Task task;
    private User user;

    public PhotoUploader(Context context, Uri photoUri, Task task) {
        this.context = context;
        this.photoUri = photoUri;
        this.task = task;
    }

    public PhotoUploader(Context context, Uri photoUri, User user) {
        this.context = context;
        this.photoUri = photoUri;
        this.user = user;
    }

    @Override
    protected String doInBackground(Uri... uris) {
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just a random string
        String CRLF = "\r\n"; // required by multipart/form-data
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) new URL(Constants.uploadPhoto).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try (
                    OutputStream output = connection.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true)
            ) {
                writer.append("--").append(boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"photo\"; filename=\"photo.jpg\"").append(CRLF);
                writer.append("Content-Type: ").append(context.getContentResolver().getType(photoUri)).append(CRLF);
                writer.append("Content-Transfer-Encoding: binary").append(CRLF);
                writer.append(CRLF).flush();

                try (InputStream input = context.getContentResolver().openInputStream(photoUri)) {
                    byte[] buffer = new byte[4096];
                    for (int n; (n = input.read(buffer)) != -1; ) {
                        output.write(buffer, 0, n);
                    }
                    output.flush();
                }

                writer.append(CRLF).flush();
                writer.append("--").append(boundary).append("--").append(CRLF).flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                    if (jsonResponse.has("url")) {
                        return jsonResponse.get("url").getAsString();
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(String url) {
        if (url != null) {
            if (task != null) {
                task.setImage(url);
                task.setFinished(true);
                DBTask.finishTask(task);
            }
            if (user != null) {
                user.setIcon(url);
                DBUsers.updateProfilePicture();
            }
        } else {
            // failed upload
        }
    }
}
