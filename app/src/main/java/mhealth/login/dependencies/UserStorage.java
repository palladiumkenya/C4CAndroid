package mhealth.login.dependencies;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import mhealth.login.models.User;

public class UserStorage {

    private static final String PREFS_NAME = "UserPrefs";
    private static final String USER_KEY = "loggedInUser";

    // Method to save User object
    public static void saveUser(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the User object to JSON using Gson
        Gson gson = new Gson();
        String json = gson.toJson(user);

        // Store the JSON string in SharedPreferences
        editor.putString(USER_KEY, json);
        editor.commit();  // Apply changes asynchronously
    }

    // Method to retrieve User object
    public static User getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(USER_KEY, null);

        if (json != null) {
            // Convert JSON string back to User object using Gson
            Gson gson = new Gson();
            return gson.fromJson(json, User.class);
        }
        return null;  // Return null if no user is found
    }
}
