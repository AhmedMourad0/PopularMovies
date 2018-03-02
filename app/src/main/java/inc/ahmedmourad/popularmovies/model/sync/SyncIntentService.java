package inc.ahmedmourad.popularmovies.model.sync;

import android.app.IntentService;
import android.content.Intent;

public class SyncIntentService extends IntentService {

    public SyncIntentService() {
        super("SyncIntentService");
    }

    @Override
    public void onHandleIntent(final Intent intent) {
        SyncTask.sync(getBaseContext());
    }
}