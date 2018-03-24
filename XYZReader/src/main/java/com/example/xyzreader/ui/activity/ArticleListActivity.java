package com.example.xyzreader.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.service.UpdaterService;
import com.example.xyzreader.ui.adapter.ArticleListAdapter;
import com.example.xyzreader.utility.Costants;
import com.example.xyzreader.utility.PrefManager;
import com.example.xyzreader.utility.Utility;


import butterknife.BindView;

import static com.example.xyzreader.service.UpdaterService.EXTRA_REFRESHING;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends BaseActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {


    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.swipe_refresh_layout)
    public SwipeRefreshLayout mSwipeRefreshLayout;

    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    private ArticleListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLayoutResource(R.layout.activity_article_list);
        super.onCreate(savedInstanceState);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        getSupportLoaderManager().initLoader(0, null, this);

        Utility.applyColorBar(getApplicationContext(), getSupportActionBar());

        mAdapter = new ArticleListAdapter(mRecyclerView);

        int columnCount;
        switch (PrefManager.getIntPref(getApplicationContext(), R.string.pref_type_mode)) {
            case Costants.NAV_MODE_SINGLE:
                columnCount = getResources().getInteger(R.integer.list_column_count_single);
                break;
            case Costants.NAV_MODE_MULTI:
                columnCount = getResources().getInteger(R.integer.list_column_count_multi);
                break;
            default:
                columnCount = getResources().getInteger(R.integer.list_column_count_single);
        }

        mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), columnCount);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState == null) {
            refresh();
        }
        PrefManager.clearPref(getApplicationContext());
    }


    private void refresh() {
        startService(new Intent(this, UpdaterService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mRefreshingReceiver,
                new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mRefreshingReceiver);
    }

    private boolean mIsRefreshing = false;

    private final BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                mIsRefreshing = intent.getBooleanExtra(
                        EXTRA_REFRESHING, false);
                updateRefreshingUI();
            }

        }
    };

    private void updateRefreshingUI() {
        mSwipeRefreshLayout.setRefreshing(mIsRefreshing);
        ArticleListAdapter.setEnableClick(!mIsRefreshing);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return ArticleLoader.newAllArticlesInstance(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    @Override
    public void onRefresh() {
        startService(new Intent(this, UpdaterService.class));
    }


}
