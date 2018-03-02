package inc.ahmedmourad.popularmovies.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.bluelinelabs.conductor.ChangeHandlerFrameLayout;
import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.ahmedmourad.popularmovies.R;
import inc.ahmedmourad.popularmovies.view.controllers.MainController;

/**
 * This's the single Activity of our app
 */
public class MainActivity extends AppCompatActivity {

    private Router router;

    @BindView(R.id.controller_container)
    ChangeHandlerFrameLayout container;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        router = Conductor.attachRouter(this, container, savedInstanceState);

        if (!router.hasRootController())
            router.setRoot(RouterTransaction.with(new MainController()));
    }

    @Override
    public void onBackPressed() {

        if (!router.handleBack())
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
