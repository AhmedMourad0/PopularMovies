package inc.ahmedmourad.popularmovies.model.entities;

import android.content.ContentValues;

public interface Persistable {

    default ContentValues toContentValues() {
        return null;
    }

    default ContentValues toContentValues(final long movieId) {
        return null;
    }
}
