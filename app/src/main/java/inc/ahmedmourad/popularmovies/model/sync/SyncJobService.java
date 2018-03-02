package inc.ahmedmourad.popularmovies.model.sync;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class SyncJobService extends JobService {

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        SyncTask.sync(getBaseContext());

        SyncTask.syncFavourites(getBaseContext());

        jobFinished(jobParameters, false);

        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters jobParameters) {

        return true;
    }
}
