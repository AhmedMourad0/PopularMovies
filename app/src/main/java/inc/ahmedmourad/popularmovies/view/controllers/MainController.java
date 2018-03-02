package inc.ahmedmourad.popularmovies.view.controllers;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import inc.ahmedmourad.popularmovies.R;
import inc.ahmedmourad.popularmovies.adapters.ConductorPagerAdapter;
import inc.ahmedmourad.popularmovies.model.sync.SyncUtils;
import inc.ahmedmourad.popularmovies.utils.PreferencesUtils;
import inc.ahmedmourad.popularmovies.view.activity.MainActivity;

/**
 * This only handle tabs
 */
public class MainController extends Controller {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tabs_layout)
    SmartTabLayout tabsLayout;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    private Unbinder unbinder;

    @NonNull
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup container) {

        final View view = inflater.inflate(R.layout.controller_main, container, false);

        unbinder = ButterKnife.bind(this, view);

        SyncUtils.initialize(view.getContext());

        setupTabs(view);

        return view;
    }

    @Override
    public void onAttach(@NonNull final View view) {
        super.onAttach(view);

        final MainActivity activity = (MainActivity) getActivity();

        if (activity == null)
            throw new IllegalStateException("Activity is null");

        toolbar.setTitle(activity.getString(R.string.app_name));

        activity.setSupportActionBar(toolbar);
    }

    private void setupTabs(@NonNull final View view) {

        viewPager.setOffscreenPageLimit(2);

        viewPager.setAdapter(new ConductorPagerAdapter(this));

        viewPager.setCurrentItem(
                PreferencesUtils.defaultPrefs(view.getContext())
                        .getInt(PreferencesUtils.KEY_SELECTED_TAB, PreferencesUtils.TAB_POPULAR));

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(final int position) {

                PreferencesUtils.edit(view.getContext(), e ->
                        e.putInt(PreferencesUtils.KEY_SELECTED_TAB, position));
            }
        });

        tabsLayout.setViewPager(viewPager);
    }

    void addOnOffsetChangedListener(final AppBarLayout.OnOffsetChangedListener listener) {
        appBarLayout.addOnOffsetChangedListener(listener);
    }

    void removeOnOffsetChangedListener(final AppBarLayout.OnOffsetChangedListener listener) {
        appBarLayout.removeOnOffsetChangedListener(listener);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}

