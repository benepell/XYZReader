
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

@SuppressWarnings("ALL")
public interface Costants {


    public static final String USER_AGENT_CACHE = "CacheDataSourceFactory";
    public static final String CACHE_VIDEO_DIR = "media";
    public static final long EXT_CACHE_SIZE_MAX = 500 * 1024 * 1024;
    public static final long EXT_CACHE_FILE_SIZE_MAX = 40 * 1024 * 1024;
    public static final long CACHE_SIZE_MAX = 100 * 1024 * 1024;
    public static final long CACHE_FILE_SIZE_MAX = 20 * 1024 * 1024;

    public static final float BRIGHTNESS_COLOR_GRAYSCALE = 07f;

//    public static final String BAKING_SYNC_TAG = "baking-sync";

    public static final int TAB_DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 2;
    public static final byte TAB_DEFAULT_BOTTOM_BORDER_COLOR_ALPHA = 0x26;
    public static final int TAB_SELECTED_INDICATOR_THICKNESS_DIPS = 3;
    public static final int COLOR_BLACK = 0xFF000000;
    public static final int COLOR_DKGRAY = 0xFF444444;
    public static final int COLOR_GRAY = 0xFF888888;
    public static final int COLOR_LTGRAY = 0xFFCCCCCC;
    public static final int COLOR_WHITE = 0xFFFFFFFF;
    public static final int COLOR_RED = 0xFFFF0000;
    public static final int COLOR_GREEN = 0xFF00FF00;
    public static final int COLOR_BLUE = 0xFF0000FF;
    public static final int COLOR_YELLOW = 0xFFFFFF00;
    public static final int COLOR_CYAN = 0xFF00FFFF;
    public static final int TAB_DEFAULT_SELECTED_INDICATOR_COLOR = COLOR_YELLOW;

    public static final int TAB_DEFAULT_DIVIDER_THICKNESS_DIPS = 1;
    public static final byte TAB_DEFAULT_DIVIDER_COLOR_ALPHA = 0x20;
    public static final float TAB_DEFAULT_DIVIDER_HEIGHT = 1f;

    public static final String COLOR_BACKGROUND_ACTIONBAR_OFFLINE = "#BDBDBD";

    public static final int GLIDE_IMAGE_WIDTH_RECIPE = 100;
    public static final int GLIDE_IMAGE_HEIGHT_RECIPE = 100;
    public static final float WP_GLIDE_BRIGHTNESS_RECIPE = 0.1f;

    public static final int GLIDE_BITMAP_ALPHA_STEP = 120;
    public static final float WP_GLIDE_BRIGHTNESS_STEP = 0.1f;
    public static final int GLIDE_IMAGE_WIDTH_STEP = 300;
    public static final int GLIDE_IMAGE_HEIGHT_STEP = 200;

    public static final int BITMAT_FONT_SIZE_DP = 30;

    public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 10;

    public static final String PATH_SEPARATOR = "/";

    public static final float ANIMATION_FROM_ALPHA = 0.1f;
    public static final float ANIMATION_TO_ALPHA = 1.0f;
    public static final int ANIMATION_ALPHA_DURATION = 900;
    public static final int ANIMATION_ALPHA_DURATION_SMALL = 100;

    public static final int MAX_COLOR_PALETTE = 16;
    public static final int MUTE_COLOR_DEFAULT = 0xFF333333;
    public static final int GRAYSCALE_BACKGROUND_COLOR = 0xFF757575;

    public static final String DATE_ARTICLE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.sss";

    public static final String EXTRA_MODE_TYPE = "com.example.xyzreader.activity.mode.type";
    public static final String EXTRA_SELECTED_ID = "com.example.xyzreader.activity.detail.selected.id";

    public static final int NAV_MODE_SINGLE = 1;
    public static final int NAV_MODE_MULTI = 2;
    public static final int NAV_MODE_SMALL_TEXT = 3;
    public static final int NAV_MODE_FULL_TEXT = 4;

    public static final int MAX_BODY_LINE = 10000;


}
