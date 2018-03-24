package com.example.xyzreader.remote;

import com.example.xyzreader.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

import timber.log.Timber;

class Config {
    public static final URL BASE_URL;

    static {
        URL url = null;
        try {
            url = new URL(BuildConfig.BASE_URL);
        } catch (MalformedURLException mUrlException) {
            Timber.e(mUrlException.getMessage());
        }

        BASE_URL = url;
    }
}
