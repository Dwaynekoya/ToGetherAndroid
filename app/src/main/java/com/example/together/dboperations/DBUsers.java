package com.example.together.dboperations;

import com.example.together.Utils;
import com.example.together.model.User;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.net.MalformedURLException;
import java.net.URL;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DBUsers {

    public static int login(String username, String password) {
        try {
            URL url = new URL(Constants.login);
            String postData = String.format("username=%s&password=%s", username, password);
            String jsonResponse = new DBGeneral.PostTask().execute(url, postData).get();
            return Integer.parseInt(jsonResponse.trim());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return -3;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int registerUser(String username, String password) {
        try {
            URL url = new URL(Constants.registerUser);
            String postData = String.format("username=%s&password=%s", username, password);
            String jsonResponse = new DBGeneral.PostTask().execute(url, postData).get();
            return Integer.parseInt(jsonResponse.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static User getUser(int id) {
        try {
            URL url = new URL(Constants.getUserGivenId + "?id=" + id);
            String response = new DBGeneral.GetTask().execute(url).get();
            Gson gson = new Gson();
            return gson.fromJson(response, User.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<User> searchUsers(String username) {
        try {
            URL url = new URL(Constants.searchUsers);
            String postData = String.format("user_id=%d&username=%s", Utils.loggedInUser.getId(), username);
            String response = new DBGeneral.PostTask().execute(url, postData).get();
            return parseUsers(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void followUser(int userId, int followsId) {
        try {
            URL url = new URL(Constants.followUser);
            String postData = String.format("user_id=%d&follows_id=%d", userId, followsId);
            new DBGeneral.PostTask().execute(url, postData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashSet<User> searchFollowing(User currentUser) {
        try {
            URL url = new URL(Constants.getFollowing);
            String postData = String.format("user_id=%d", currentUser.getId());
            String jsonResponse = new DBGeneral.PostTask().execute(url, postData).get();
            if (jsonResponse.equals("")) return null;

            HashSet<User> userHashSet = new HashSet<>();
            JsonArray jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();
            for (JsonElement jsonElement: jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                int followsId = jsonObject.get("follows_id").getAsInt();
                User user = DBUsers.getUser(followsId);
                if (user != null) userHashSet.add(user);
            }
            return userHashSet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<User> parseUsers(String jsonResponse) {
        Gson gson = new Gson();
        Type userListType = new TypeToken<List<User>>(){}.getType();
        try {
            return gson.fromJson(jsonResponse, userListType);
        } catch (JsonSyntaxException exception) {
            System.out.printf("EXCEPTION PARSING USERS: %s %n", jsonResponse);
            return null;
        }
    }

    public static void updateProfilePicture() {
        try {
            URL url = new URL(Constants.updateIcon);
            String postData = String.format("user_id=%d&icon_url=%s", Utils.loggedInUser.getId(), Utils.loggedInUser.getIcon());
            new DBGeneral.PostTask().execute(url, postData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void editBio(String newBio) {
        try {
            URL url = new URL(Constants.updateUserBio);
            String postData = String.format("user_id=%d&field=%s&new_field=%s", Utils.loggedInUser.getId(), "bio", newBio);
            new DBGeneral.PostTask().execute(url, postData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
