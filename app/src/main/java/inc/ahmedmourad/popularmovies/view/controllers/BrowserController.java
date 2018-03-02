package inc.ahmedmourad.popularmovies.view.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bluelinelabs.conductor.Controller;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import im.delight.android.webview.AdvancedWebView;
import inc.ahmedmourad.popularmovies.R;
import inc.ahmedmourad.popularmovies.view.activity.MainActivity;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * This's where we display our movie's reviews online
 */
public class BrowserController extends Controller {

    static final String KEY_TITLE = "title";
    static final String KEY_URL = "url";

    @BindView(R.id.browser_toolbar)
    Toolbar toolbar;

    @BindView(R.id.browser_progress_bar)
    MaterialProgressBar progressBar;

    @BindView(R.id.browser_refresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.browser_web_view)
    AdvancedWebView webView;

    private String title;
    private String url;

    private Unbinder unbinder;

    @SuppressWarnings("WeakerAccess")
    public BrowserController(@Nullable final Bundle args) {
        super(args);

        if (args != null) {
            title = args.getString(KEY_TITLE);
            url = args.getString(KEY_URL);
        }
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup container) {

        final View view = inflater.inflate(R.layout.controller_browser, container, false);

        setHasOptionsMenu(true);

        unbinder = ButterKnife.bind(this, view);

        toolbar.setTitle(title);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(final WebView view, final int progress) {

                if (progress < 100 && progressBar.getVisibility() == ProgressBar.GONE)
                    progressBar.setVisibility(ProgressBar.VISIBLE);

                progressBar.setProgress(progress);

                if (progress == 100) {

                    progressBar.setVisibility(ProgressBar.GONE);
                    refreshLayout.setRefreshing(false);
                }
            }
        });

        webView.setGeolocationEnabled(true);
        webView.loadUrl(url);

        refreshLayout.setProgressBackgroundColorSchemeResource(R.color.refresh_progress_background);

        refreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);

        refreshLayout.setOnRefreshListener(() -> webView.reload());

        return view;
    }

    @Override
    protected void onAttach(@NonNull final View view) {
        super.onAttach(view);

        final MainActivity activity = (MainActivity) getActivity();

        if (activity == null)
            throw new IllegalStateException("Activity is null");

        activity.setSupportActionBar(toolbar);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_browser, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {

        if (item.getItemId() == R.id.action_close)
            getRouter().popController(this);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean handleBack() {
        return !webView.onBackPressed() || super.handleBack();
    }

    @Override
    protected void onActivityResumed(@NonNull final Activity activity) {
        super.onActivityResumed(activity);
        webView.onResume();
    }

    @Override
    protected void onActivityPaused(@NonNull final Activity activity) {
        webView.onPause();
        super.onActivityPaused(activity);
    }

    @Override
    protected void onDestroyView(@NonNull final View view) {
        webView.onDestroy();
        super.onDestroyView(view);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
