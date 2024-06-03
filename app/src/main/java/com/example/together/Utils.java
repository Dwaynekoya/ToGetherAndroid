package com.example.together;


import com.example.together.dboperations.SQLDateAdapter;
import com.example.together.model.Group;
import com.example.together.model.Task;
import com.example.together.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.util.List;


public class Utils {
    public static User loggedInUser;
    public static User selectedUser; //for popups
    public static Group selectedGroup; //for popups
    public static Group groupNewTask; //to add a new task for a group
    //public static View previousView; //to go back to after closing add task view
    /**
     * Used to determine if the user has inputted all the data necessary to create an object
     * @param strings: fields that the user must fill
     * @return false if one or more of the fields have not been filled, true if all have assigned values
     */
    public static boolean checkDataValidity(List<String> strings) {
        for (String str : strings) {
            if (str == null || str.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets listeners for the buttons on the left side of most views
     * @param settingsButton opens the settings menu
     * @param groupButton shows the group view
     * @param listButton shows the tasklist view
     * @param homeButton shows the main feed
     */
    /*public static void sidebarSetup(Button settingsButton, Button groupButton, Button listButton, Button homeButton) {
        settingsButton.setOnAction(actionEvent -> ViewSwitcher.switchView(View.SETTINGS));
        groupButton.setOnAction(actionEvent -> ViewSwitcher.switchView(View.GROUPS));
        listButton.setOnAction(actionEvent -> ViewSwitcher.switchView(View.TASKLIST));
        homeButton.setOnAction(actionEvent -> ViewSwitcher.switchView(View.FEED));
    }*/ //TODO: bottom nav bar


    /**
     * Sets a character limit in a given text input
     * @param textInputControl textfield/textarea to add the limit to
     * @param limit number of max. characters
     */
    /*public static void setCharacterLimit(TextInputControl textInputControl, int limit) {
        TextFormatter<String> textFormatter = new TextFormatter<>(change ->
                change.getControlNewText().length() <= limit ? change : null);
        textInputControl.setTextFormatter(textFormatter);
    }*/ //TODO: set character limit

    /**
     * Transforms a json into a list of Task objects
     * @param json received from the DB
     * @return list of fetched tasks
     */
    public static List<Task> parseTasks(String json) {
        if (json==null) return null;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new SQLDateAdapter())
                .create();
        //System.out.println(json);
        JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();

        List<Task> fetchedTasks = gson.fromJson(jsonObject.getAsJsonArray("tasks"), new TypeToken<List<Task>>() {}.getType());
        return fetchedTasks;
    }
    /**
     * Opens a file chooser that can only pick image files
     * @param event used to extract the current window
     * @return chosen image file
     */
    /*public static File imageFileChooser(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");

        // File chooser can only pick image files
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter(
                "Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(imageFilter);

        Window currentWindow = ((ButtonBase) event.getSource()).getScene().getWindow();
        return fileChooser.showOpenDialog(currentWindow);
    }*/ //todo: image chooser

    /**
     * Makes it so a spinner can only take integer values
     * @param spinner to set the new format to
     */
    /*public static void integerSpinner(Spinner spinner) {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 1);
        spinner.setValueFactory(valueFactory);

        spinner.setEditable(true);
        spinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                spinner.getEditor().setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }*/ //TODO: integer spinner
}
