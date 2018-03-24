package com.example.xyzreader.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.example.xyzreader.ui.activity.BaseActivity;
import com.example.xyzreader.ui.view.DynamicHeightNetworkImageView;
import com.example.xyzreader.ui.view.EffectView;
import com.example.xyzreader.utility.Costants;
import com.example.xyzreader.ui.view.UtilityBitmap;
import com.example.xyzreader.utility.PrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private final RecyclerView mRecyclerView;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat outputFormat;
    private final GregorianCalendar startOfEpoch;

    public static void setEnableClick(boolean enableClick) {
        mEnableClick = enableClick;
    }

    private static boolean mEnableClick;


    public ArticleListAdapter(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        dateFormat = new SimpleDateFormat(Costants.DATE_ARTICLE_PATTERN, Locale.getDefault());
        outputFormat = new SimpleDateFormat(Costants.DATE_ARTICLE_PATTERN, Locale.ROOT);
        startOfEpoch = new GregorianCalendar(2, 1, 1);
    }

    @Override
    public long getItemId(int position) {
        try {
            if (mCursor != null && mCursor.getCount() > 0) {
                if (position >= 0) {
                    mCursor.moveToPosition(position);
                }
                return mCursor.getLong(ArticleLoader.Query._ID);
            }

        } catch (CursorIndexOutOfBoundsException ex) {
            Timber.e("database error%s", ex.getMessage());
        }
        return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_article, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long itemPosition;
                if (mEnableClick) {
                    itemPosition = getItemId(vh.getAdapterPosition());

                    if (PrefManager.getIntPref(mContext, R.string.pref_type_mode) == Costants.NAV_MODE_MULTI) {
                        EffectView effectView = new EffectView(view);
                        effectView.explode(mRecyclerView);
                    }

                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                            ItemsContract.Items.buildItemUri(itemPosition)));
                }

            }
        });
        return vh;
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

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        int positionAdapter = holder.getAdapterPosition();
        mCursor.moveToPosition(positionAdapter);


        final float aspectRatio = mCursor.getFloat(ArticleLoader.Query.ASPECT_RATIO);
        final String title = mCursor.getString(ArticleLoader.Query.TITLE);
        final String author = mCursor.getString(ArticleLoader.Query.AUTHOR);
        final String thumbUrl = mCursor.getString(ArticleLoader.Query.THUMB_URL);

        holder.mTitleView.setText(title);

        Date publishedDate = parsePublishedDate();

        String htmlString = outputFormat.format(publishedDate);

        if (!publishedDate.before(startOfEpoch.getTime())) {
            htmlString = DateUtils.getRelativeTimeSpanString(
                    publishedDate.getTime(),
                    System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL).toString();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            holder.mSubtitleView.setText(Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.mSubtitleView.setText(Html.fromHtml(htmlString));
        }

        htmlString = "by " + author;
        if (Build.VERSION.SDK_INT >= 24) {
            holder.mAuthorView.setText(Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.mAuthorView.setText(Html.fromHtml(htmlString));
        }


        Glide.with(holder.itemView.getContext().getApplicationContext())
                .setDefaultRequestOptions(new RequestOptions().centerCrop())
                .asBitmap()
                .load(thumbUrl)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        holder.mThumbnailView.setBackgroundResource(R.drawable.no_media);
                        holder.mArticleMain.setBackgroundColor(Costants.GRAYSCALE_BACKGROUND_COLOR);
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        holder.mThumbnailView.setBackgroundResource(R.drawable.download_in_progress);
                    }


                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                        UtilityBitmap util = new UtilityBitmap(resource, aspectRatio);
                        if (holder.getAdapterPosition() == (BaseActivity.getPosition() - 1)) {
                            util.grayImage(holder.mThumbnailView);
                            holder.mArticleMain.setBackgroundColor(Costants.GRAYSCALE_BACKGROUND_COLOR);
                        } else {
                            util.alphaBackground(holder.mThumbnailView);
                            util.changeBackground(holder.mArticleMain);
                        }

                    }
                });
        holder.mThumbnailView.setAspectRatio(aspectRatio);
        holder.mThumbnailView.setContentDescription(title);

    }


    @Override
    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Cursor swapCursor(Cursor c) {
        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
        @BindView(R.id.thumbnail)
        public DynamicHeightNetworkImageView mThumbnailView;

        @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
        @BindView(R.id.article_list_title)
        public TextView mTitleView;

        @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
        @BindView(R.id.article_subtitle)
        public TextView mSubtitleView;

        @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
        @BindView(R.id.article_main)
        public LinearLayout mArticleMain;

        @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
        @BindView(R.id.article_author)
        public TextView mAuthorView;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}
