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
