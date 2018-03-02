package inc.ahmedmourad.popularmovies.model.sync;

import android.content.Context;
import android.content.Intent;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import inc.ahmedmourad.popularmovies.model.database.MovieDatabase;
import inc.ahmedmourad.popularmovies.utils.PreferencesUtils;

public final class SyncUtils {

    private static final int SYNC_INTERVAL = 60 * 60 * 24;
    private static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    private static final String SYNC_TAG = "sync";

    /**
     * initialize our job dispatcher and sync our db if needed
     * @param context c o n t e x t
     */
    public static synchronized void initialize(final Context context) {

        if (!PreferencesUtils.defaultPrefs(context).getBoolean(PreferencesUtils.KEY_IS_SYNC_SCHEDULED, false)) {

            scheduleSync(context);

            PreferencesUtils.edit(context, e -> e.putBoolean(PreferencesUtils.KEY_IS_SYNC_SCHEDULED, true));
        }

        new Thread(() -> {

            if (MovieDatabase.needsSync(context))
                syncImmediately(context);

        }).start();
    }

    /**
     * Sync or Die, Make your choice!
     * @param context swiss knife
     */
    private static void syncImmediately(final Context context) {

        context.startService(new Intent(context, SyncIntentService.class));
    }

    /**
     * Start our firebase dispatcher
     * @param context context ... again
     */
    private static void scheduleSync(final Context context) {

        final FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        final Job syncJob = dispatcher.newJobBuilder()
                .setService(SyncJobService.class)
                .setTag(SYNC_TAG)
                .setConstraints(Constraint.DEVICE_IDLE, Constraint.ON_UNMETERED_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SYNC_INTERVAL, SYNC_INTERVAL + SYNC_FLEXTIME))
                .setReplaceCurrent(false)
                .build();

        dispatcher.schedule(syncJob);
    }
}