
/*
 *  ____        _    _                  _
 * | __ )  __ _| | _(_)_ __   __ _     / \   _ __  _ __
 * |  _ \ / _` | |/ / | '_ \ / _` |   / _ \ | '_ \| '_ \
 * | |_) | (_| |   <| | | | | (_| |  / ___ \| |_) | |_) |
 * |____/ \__,_|_|\_\_|_| |_|\__, | /_/   \_\ .__/| .__/
 *                           |___/          |_|   |_|
 *
 * Copyright (C) 2017 Benedetto Pellerito
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

package com.example.xyzreader.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.xyzreader.R;


@SuppressWarnings("unused")
public class PrefManager {


    public static void putIntPref(Context context, @SuppressWarnings("SameParameterValue") int key, int value) {
        SharedPreferences prefId = context
                .getSharedPreferences(context.getString(key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefId.edit();
        editor.putInt(context.getString(key), value);
        editor.apply();

    }

    @SuppressWarnings("unused")
    public static void putStringPref(Context context, int key, String value) {
        SharedPreferences prefId = context
                .getSharedPreferences(context.getString(key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefId.edit();
        editor.putString(context.getString(key), value);
        editor.apply();

    }

    @SuppressWarnings("unused")
    public static void putBoolPref(Context context, int key, @SuppressWarnings("SameParameterValue") boolean value) {
        SharedPreferences prefId = context
                .getSharedPreferences(context.getString(key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefId.edit();
        editor.putBoolean(context.getString(key), value);
        editor.apply();

    }

    public static int getIntPref(Context context, @SuppressWarnings("SameParameterValue") int key) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(context.getString(key), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(context.getString(key), 0);
    }

    @SuppressWarnings("unused")
    public static String getStringPref(Context context, int key) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(context.getString(key), Context.MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(key), "");
    }

    @SuppressWarnings("unused")
    public static boolean isPref(Context context, int key) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(context.getString(key), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(context.getString(key), false);
    }

    public static void clearPref(Context context) {
        int[] prefArrays = {R.string.pref_share_text, R.string.pref_share_title};

        for (int pref : prefArrays) {
            context.getSharedPreferences(context.getString(pref), 0).edit().clear().apply();
        }
    }

}

