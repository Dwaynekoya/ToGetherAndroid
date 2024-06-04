package com.example.together.dboperations;

import com.example.together.Utils;
import com.example.together.model.Group;
import com.example.together.model.User;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashSet;
import java.util.List;

public class DBGroup {
    /**
     * Creates a new group in the database
     * @param newGroup object containing the name and description to be used
     */
    public static void createGroup(Group newGroup) {
        try {
            URL url = new URL(Constants.createGroup);
            String postData = String.format("name=%s&description=%s&manager_id=%d",
                    newGroup.getName(), newGroup.getDescription(), Utils.loggedInUser.getId());
            String response = new DBGeneral.PostTask().execute(url, postData).get();
            System.out.printf("Response when creating group: %s %n", response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds relationship between user and group
     * @param group group to add the user to
     * @param user user that will become a group member
     */
    public static void putMember(Group group, User user) {
        try {
            URL url = new URL(Constants.putMember);
            String postdata = String.format("group_id=%d&user_id=%d", group.getId(), user.getId());
            String response = new DBGeneral.PostTask().execute(url, postdata).get();
            System.out.printf("Response when adding member to group: %s %n", response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Looks for groups with a LIKE query
     * @param name String to look for group names containing it
     * @return ObservableList with matches
     */
    public static List<Group> searchGroups(String name) {
        try {
            URL url = new URL(Constants.searchGroups);
            String postData = "name=" + name;
            String jsonResponse = new DBGeneral.PostTask().execute(url, postData).get();

            Gson gson = new Gson();
            Type groupListType = new TypeToken<List<Group>>(){}.getType();
            return gson.fromJson(jsonResponse, groupListType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Looks for groups a member belongs to
     * @param user to extract id for the search
     * @return set of groups the user belongs to
     */
    public static HashSet<Group> searchGroupsFromMember(User user) {
        try {
            URL url = new URL(Constants.groupsFromMember + "?user_id=" + user.getId());
            String jsonResponse = new DBGeneral.GetTask().execute(url).get();
            if (jsonResponse.isEmpty()) return null;

            HashSet<Group> groupHashSet = new HashSet<>();
            JsonArray jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                int groupId = jsonObject.get("group_id").getAsInt();
                Group group = DBGroup.getGroup(groupId);
                if (group != null) groupHashSet.add(group);
            }

            for (Group group : groupHashSet) {
                group.setManager(DBGroup.searchManager(group));
            }

            return groupHashSet;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Looks for the user who created/manages a group
     * @param group to extract the manager from
     * @return User manager
     */
    private static User searchManager(Group group) {
        try {
            URL url = new URL(Constants.getManager);
            String postdata = String.format("group_id=%d", group.getId());
            String response = new DBGeneral.PostTask().execute(url, postdata).get();
            Gson gson = new Gson();
            User manager = gson.fromJson(response, User.class);
            return manager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Looks for a group given its ID
     * @param groupId to identify the group
     * @return group object with data from the DB
     */
    private static Group getGroup(int groupId) {
        try {
            URL url = new URL(Constants.getGroupGivenId + "?id=" + groupId);
            String response = new DBGeneral.GetTask().execute(url).get();
            Gson gson = new Gson();
            Group group = gson.fromJson(response, Group.class);
            return group;
        } catch (Exception e) {
            System.out.println("Exception getting group from id");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets all tasks associated with a group
     * @param id group identifier
     */
    public static String getTasks(int id) {
        try {
            URL url = new URL(Constants.getTasksFromGroup + "?id=" + id);
            return new DBGeneral.GetTask().execute(url).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Removes all entries in user_groups for the current user
     */
    public static void leaveAllGroups() {
        try {
            URL url = new URL(Constants.leaveAllGroups);
            String postdata = String.format("id=%d", Utils.loggedInUser.getId());
            new DBGeneral.PostTask().execute(url, postdata);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates group info (name and description)
     * @param group group to edit
     */
    public static void editGroup(Group group) {
        try {
            URL url = new URL(Constants.editGroup);
            String postdata = String.format("id=%d&name=%s&description=%s", group.getId(), group.getName(), group.getDescription());
            new DBGeneral.PostTask().execute(url, postdata);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes group from DB
     * @param selectedGroup to delete from DB
     */
    public static void deleteGroup(Group selectedGroup) {
        try {
            URL url = new URL(Constants.deleteGroup);
            String postdata = String.format("id=%d", selectedGroup.getId());
            new DBGeneral.PostTask().execute(url, postdata);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
