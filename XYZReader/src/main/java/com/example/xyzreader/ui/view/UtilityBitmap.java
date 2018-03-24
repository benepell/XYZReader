package com.example.xyzreader.ui.view;

import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.example.xyzreader.utility.Costants;

public class UtilityBitmap {

    private final Bitmap mBitmap;
    private final float mAspectRatio;

    public UtilityBitmap(Bitmap bitmap, float aspectRatio) {
        mBitmap = bitmap;
        mAspectRatio = aspectRatio;
    }

    public void changeAsyncBackground(final View view) {
        Bitmap bitmap;
        if(mAspectRatio >0){
            int width = mBitmap.getWidth();
            int height = (int) (width / mAspectRatio);
            bitmap = Bitmap.createScaledBitmap(mBitmap, width, height, false);

        }else {
            bitmap = mBitmap;
        }

        if ((bitmap != null) && (view != null)) {
            Palette.from(mBitmap).maximumColorCount(Costants.MAX_COLOR_PALETTE).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@NonNull Palette palette) {
                    int muteColor = palette.getDarkMutedColor(Costants.MUTE_COLOR_DEFAULT);
                    view.setBackgroundColor(muteColor);
                    view.setVisibility(View.VISIBLE);
                }
            });
        }
    }


    public void changeBackground(final View view) {
        Bitmap bitmap;
        if(mAspectRatio >0){
            int width = mBitmap.getWidth();
            int height = (int) (width / mAspectRatio);
            bitmap = Bitmap.createScaledBitmap(mBitmap, width, height, false);

        }else {
            bitmap = mBitmap;
        }

        if ((bitmap != null) && (view != null)) {
            Palette palette = Palette.from(bitmap).maximumColorCount(Costants.MAX_COLOR_PALETTE).generate();
            view.setBackgroundColor(palette.getMutedColor(Costants.MUTE_COLOR_DEFAULT));
            view.setVisibility(View.VISIBLE);
        }
    }

    public void alphaBackground(View view) {

        Bitmap bitmap;
        if(mAspectRatio >0){
            int width = mBitmap.getWidth();
            int height = (int) (width / mAspectRatio);
            bitmap = Bitmap.createScaledBitmap(mBitmap, width, height, false);

        }else {
            bitmap = mBitmap;
        }

        Drawable drawable = new BitmapDrawable(view.getResources(), bitmap);
        AlphaAnimation alpha = new AlphaAnimation(Costants.ANIMATION_FROM_ALPHA, Costants.ANIMATION_TO_ALPHA);
        alpha.setDuration(Costants.ANIMATION_ALPHA_DURATION);
        alpha.setFillAfter(true);
        view.startAnimation(alpha);
        view.setBackground(drawable);
    }


    public void grayImage(View view) {
        Bitmap bitmap;
        if(mAspectRatio >0){
            int width = mBitmap.getWidth();
            int height = (int) (width / mAspectRatio);
            bitmap = Bitmap.createScaledBitmap(mBitmap, width, height, false);

        }else {
            bitmap = mBitmap;
        }

        Drawable drawable = new BitmapDrawable(view.getResources(), bitmap);
        float[] colorMatrix = {
                0.33f, 0.33f, 0.33f, 0, Costants.BRIGHTNESS_COLOR_GRAYSCALE,
                0.33f, 0.33f, 0.33f, 0, Costants.BRIGHTNESS_COLOR_GRAYSCALE,
                0.33f, 0.33f, 0.33f, 0, Costants.BRIGHTNESS_COLOR_GRAYSCALE,
                0, 0, 0, 1, 0
        };

        drawable.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        view.setBackground(drawable);
    }

    public void resizeView(View view){
        Bitmap bitmap;
        if(mAspectRatio >0){
            int width = mBitmap.getWidth();
            int height = (int) (width / mAspectRatio);
            bitmap = Bitmap.createScaledBitmap(mBitmap, width, height, false);

        }else {
            bitmap = mBitmap;
        }
        view.setBackground(new BitmapDrawable(view.getResources(),bitmap));
    }
}
