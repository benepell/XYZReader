/*
 *  _    _  _     _  _______     ___                      _
 * ( )  ( )( )   ( )(_____  )   |  _`\                   ( )
 * `\`\/'/'`\`\_/'/'     /'/'   | (_) )   __     _ _    _| |   __   _ __
 *   >  <    `\ /'     /'/'     | ,  /  /'__`\ /'_` ) /'_` | /'__`\( '__)
 *  /'/\`\    | |    /'/'___    | |\ \ (  ___/( (_| |( (_| |(  ___/| |
 * (_)  (_)   (_)   (_______)   (_) (_)`\____)`\__,_)`\__,_)`\____)(_)
 *
 * Copyright (C) 2018 Benedetto Pellerito
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.xyzreader.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;

import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.ui.activity.ArticleListActivity;
import com.example.xyzreader.ui.view.EllipsizingTextView;
import com.example.xyzreader.ui.activity.ArticleDetailActivity;
import com.example.xyzreader.utility.Costants;
import com.example.xyzreader.ui.view.UtilityBitmap;
import com.example.xyzreader.utility.PrefManager;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.photo_container)
    public View mPhotoContainerView;

    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.photo)
    public ImageView mPhotoView;

    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.article_body)
    public EllipsizingTextView mBodyView;

    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.article_byline)
    public TextView mBylineView;

    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.article_title)
    public TextView mTitleView;

    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.scrollview)
    public NestedScrollView mScrollView;


    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.share_fab)
    public FloatingActionButton mFab;

    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.progress_bar_photo)
    public ProgressBar mProgressBarPhoto;

    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.meta_bar)
    public View mLinearLayoutMetaBar;


    @SuppressWarnings("WeakerAccess")
    public static final String ARG_ITEM_ID = "item_id";

    private Unbinder unbinder;

    private Cursor mCursor;
    private long mItemId;
    private View mRootView;
    private String mShareText;

    //    private int mTopInset;
//    private int mScrollY;
    private final SimpleDateFormat dateFormat;

    private final SimpleDateFormat outputFormat;
    private final GregorianCalendar startOfEpoch;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
        dateFormat = new SimpleDateFormat(Costants.DATE_ARTICLE_PATTERN, Locale.getDefault());
        outputFormat = new SimpleDateFormat(Costants.DATE_ARTICLE_PATTERN, Locale.ROOT);
        startOfEpoch = new GregorianCalendar(2, 1, 1);
    }


    public static ArticleDetailFragment newInstance(long itemId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_ITEM_ID, itemId);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getArguments() != null) && (getArguments().containsKey(ARG_ITEM_ID))) {
            mItemId = getArguments().getLong(ARG_ITEM_ID);
        }

        /* TODO NOT USE
        boolean mIsCard = getResources().getBoolean(R.bool.detail_is_card);
        int mStatusBarFullOpacityBottom = getResources().getDimensionPixelSize(
                R.dimen.detail_card_top_margin);
        */
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_article_detail, container, false);

        unbinder = ButterKnife.bind(this, mRootView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mScrollView.setNestedScrollingEnabled(true);
        }

        if (getActivity() != null) {

            switch (PrefManager.getIntPref(getActivity(), R.string.pref_type_mode)) {
                case Costants.NAV_MODE_SMALL_TEXT:
                    mBodyView.setMaxLines(getResources().getInteger(R.integer.detail_body_text_elipsize));
                    break;
                case Costants.NAV_MODE_FULL_TEXT:
                    mBodyView.setMaxLines(Costants.MAX_BODY_LINE);
            }

            mFab.setImageDrawable(
                    new IconicsDrawable(getActivity(), MaterialDesignIconic.Icon.gmi_share)
                            .colorRes(R.color.white)
                            .respectFontBounds(true));


            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(getShareText())) {
                        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                                .setType("text/plain")
                                .setText(getShareText())
                                .getIntent(), getString(R.string.action_share)));
                    }
                }
            });
        }

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private Date parsePublishedDate() {
        try {
            String date = mCursor.getString(ArticleLoader.Query.PUBLISHED_DATE);
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Timber.e(ex.getMessage());
            Timber.i("passing today's date");
            return new Date();
        }
    }

    private void bindViews(final Context context) {

        if (context == null) return;

        mBylineView.setMovementMethod(new LinkMovementMethod());

        if (mCursor != null) {

            final String title = mCursor.getString(ArticleLoader.Query.TITLE);
            final String author = mCursor.getString(ArticleLoader.Query.AUTHOR);
            final String photoUrl = mCursor.getString(ArticleLoader.Query.PHOTO_URL);
            final String body = mCursor.getString(ArticleLoader.Query.BODY);
            final float aspectRatio = mCursor.getFloat(ArticleLoader.Query.ASPECT_RATIO);

            mRootView.setVisibility(View.VISIBLE);
            mTitleView.setText(title);

            Date publishedDate = parsePublishedDate();

            String htmlString = outputFormat.format(publishedDate)
                    + " by " + author;

            if (!publishedDate.before(startOfEpoch.getTime())) {
                htmlString = DateUtils.getRelativeTimeSpanString(
                        publishedDate.getTime(),
                        System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_ALL).toString()
                        + " by " + author;
            }


            if (Build.VERSION.SDK_INT >= 24) {
                mBylineView.setText(Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY));
            } else {
                mBylineView.setText(Html.fromHtml(htmlString));
            }

            Pattern pattern[] = {Pattern.compile("(\r\n|\n)+"),
                    Pattern.compile("(\\[[^,]*])|(\n{2,3})")};

            final String shareTextFilter = pattern[1]
                    .matcher(body).replaceAll(" ");

            final String bodyHtml = pattern[0]
                    .matcher(shareTextFilter).replaceAll(" <br /> ");

            setShareText(shareTextFilter);
            if (!TextUtils.isEmpty(shareTextFilter) && (!TextUtils.isEmpty(title))) {
                PrefManager.putStringPref(getActivity(), R.string.pref_share_title, title);
                PrefManager.putStringPref(getActivity(), R.string.pref_share_text, shareTextFilter);
            }

            if (Build.VERSION.SDK_INT >= 24) {
                mBodyView.setText(Html.fromHtml(bodyHtml, Html.FROM_HTML_MODE_LEGACY));
            } else {
                mBodyView.setText(Html.fromHtml(bodyHtml));
            }

            mScrollView.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(photoUrl)) {
                Glide.with(context)
                        .asBitmap()
                        .load(photoUrl)
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                mPhotoView.setBackground(
                                        new IconicsDrawable(context, MaterialDesignIconic.Icon.gmi_photo_size_select_large)
                                                .colorRes(R.color.white)
                                                .sizeDp(200)
                                                .respectFontBounds(true));
                                mProgressBarPhoto.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onLoadStarted(@Nullable Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                                mProgressBarPhoto.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                                if (isAdded()) {
                                    mProgressBarPhoto.setVisibility(View.GONE);
                                    UtilityBitmap utilityBitmap = new UtilityBitmap(resource, aspectRatio);
                                    utilityBitmap.resizeView(mPhotoView);
                                    utilityBitmap.changeAsyncBackground(mLinearLayoutMetaBar);
                                }
                            }
                        });


            }
        } else {
            mRootView.setVisibility(View.GONE);
            Toast.makeText(context, "No Data available", Toast.LENGTH_LONG).show();
        }

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newInstanceForItemId(getActivity(), mItemId);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> cursorLoader, Cursor cursor) {
        if (!isAdded()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }

        mCursor = cursor;
        if (mCursor != null && !mCursor.moveToFirst()) {
            Timber.e("Error reading item detail cursor");
            mCursor.close();
            mCursor = null;
        }

        bindViews(getActivity());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> cursorLoader) {
        mCursor = null;
    }

    //    private ColorDrawable mStatusBarColorDrawable;

    private String getShareText() {
        return mShareText;
    }

    private void setShareText(String shareText) {
        this.mShareText = shareText;
    }

}
