package inc.ahmedmourad.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class PreferencesUtils {

    /**
     * Is our job dispatcher on or off?
     */
    public static final String KEY_IS_SYNC_SCHEDULED = "isi";

    /**
     * Data is in the database?
     */
    public static final String KEY_IS_DATA_INITIALIZED = "idi";

    /**
     * The last selected tab by the user
     */
    public static final String KEY_SELECTED_TAB = "st";

    public static final int TAB_POPULAR = 0;
//    public static final int TAB_TOP_RATED = 1;
//    public static final int TAB_FAVOURITES = 2;

    /**
     * The user's favourite item
     */
    public static final String KEY_ITEM = "i";

    public static final int ITEM_GRID = 0;
    public static final int ITEM_LINEAR = 1;

    /**
     * whether to play trailer in app or using the Youtube app
     */
    public static final String KEY_PLAY_VIDEOS_IN_APP = "pvia";

    /**
     * default shared preferences object
     * @param context i ran out of jokes
     * @return default shared preferences object
     */
    public static SharedPreferences defaultPrefs(final Context context){

        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * cool way to edit our preferences
     * @param context not context
     * @param operation edit preferences here
     */
    public static void edit(final Context context, final PreferencesEditor operation) {

        final SharedPreferences.Editor editor = defaultPrefs(context).edit();

        operation.edit(editor);

        editor.apply();
    }

    /**
     * I miss Kotlin
     */
    @FunctionalInterface
    public interface PreferencesEditor {
        void edit(SharedPreferences.Editor e);
    }
}
