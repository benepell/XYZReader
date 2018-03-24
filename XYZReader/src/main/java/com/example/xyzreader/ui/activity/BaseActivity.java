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

package com.example.xyzreader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

import com.example.xyzreader.R;
import com.example.xyzreader.utility.Costants;
import com.example.xyzreader.utility.PrefManager;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import butterknife.ButterKnife;
import timber.log.Timber;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int mLayoutResource;

    private static int sPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub mStub;

        setContentView(R.layout.activity_base);

        if (getLayoutResource() > 0) {

            mStub = findViewById(R.id.stub_base_layout);
            mStub.setLayoutResource(getLayoutResource());
            mStub.inflate();

        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            menuNavigation(navigationView);
        }

        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if ((drawer != null) && (drawer.isDrawerOpen(GravityCompat.START))) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        switch (getLayoutResource()) {
            case R.layout.activity_article_list:
                inflater.inflate(R.menu.list_menu, menu);
                break;
            case R.layout.activity_article_detail:
                inflater.inflate(R.menu.detail_menu, menu);
                break;
            default:
                inflater.inflate(R.menu.base_menu, menu);
        }


        return true;
    }

    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {

        MenuItem menuItemShare;
        MenuItem menuItemHome;
        MenuItem menuItemSingle;
        MenuItem menuItemMulti;
        MenuItem menuItemTextShort;
        MenuItem menuItemTextFull;

        if (getLayoutResource() == R.layout.activity_article_list) {

            menuItemHome = menu.findItem(R.id.menu_action_home);
            menuItemSingle = menu.findItem(R.id.menu_action_single);
            menuItemMulti = menu.findItem(R.id.menu_action_multi);


            switch (PrefManager.getIntPref(getApplicationContext(), R.string.pref_type_mode)) {
                case Costants.NAV_MODE_SINGLE:
                    menuItemSingle.setChecked(true);
                    menuItemSingle.setEnabled(false);
                    break;
                case Costants.NAV_MODE_MULTI:
                    menuItemMulti.setChecked(true);
                    menuItemMulti.setEnabled(false);
                    break;
                default:
                    menuItemHome.setChecked(false);
            }


        } else if (getLayoutResource() == R.layout.activity_article_detail) {

            menuItemShare = menu.findItem(R.id.menu_action_share);
            menuItemShare.setIcon(
                    new IconicsDrawable(getApplicationContext(), MaterialDesignIconic.Icon.gmi_share)
                            .colorRes(R.color.white)
                            .sizeDp(24)
                            .respectFontBounds(true));
            menuItemShare.setVisible(true);

            menuItemHome = menu.findItem(R.id.menu_action_home);
            menuItemTextShort = menu.findItem(R.id.menu_action_text_small);
            menuItemTextFull = menu.findItem(R.id.menu_action_text_full);

            switch (PrefManager.getIntPref(getApplicationContext(), R.string.pref_type_mode)) {
                case Costants.NAV_MODE_SMALL_TEXT:
                    menuItemTextShort.setChecked(true);
                    menuItemTextShort.setEnabled(false);
                    break;
                case Costants.NAV_MODE_FULL_TEXT:
                    menuItemTextFull.setChecked(true);
                    menuItemTextFull.setEnabled(false);
                    break;
                default:
                    menuItemHome.setChecked(false);
            }

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (getLayoutResource() == R.layout.activity_article_list) {
            int id = item.getItemId();
            switch (id) {
                case R.id.menu_action_home:
                    openHomeActivity();
                    return true;
                case R.id.menu_action_single:
                    PrefManager.putIntPref(getApplicationContext(), R.string.pref_type_mode, Costants.NAV_MODE_SINGLE);
                    openHomeActivity();
                    return true;
                case R.id.menu_action_multi:
                    PrefManager.putIntPref(getApplicationContext(), R.string.pref_type_mode, Costants.NAV_MODE_MULTI);
                    openHomeActivity();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }

        } else if (getLayoutResource() == R.layout.activity_article_detail) {

            switch (item.getItemId()) {
                case R.id.menu_action_share:
                    String title = PrefManager.getStringPref(getApplicationContext(), R.string.pref_share_title);
                    String text = PrefManager.getStringPref(getApplicationContext(), R.string.pref_share_text);
                    activityShareText(getApplicationContext(), title, text);

                    Toast.makeText(getApplicationContext(), "share", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.menu_action_home:
                    openHomeActivity();
                    return true;
                case R.id.menu_action_text_small:
                    PrefManager.putIntPref(getApplicationContext(), R.string.pref_type_mode, Costants.NAV_MODE_SMALL_TEXT);
                    openHomeActivity();
                    return true;
                case R.id.menu_action_text_full:
                    PrefManager.putIntPref(getApplicationContext(), R.string.pref_type_mode, Costants.NAV_MODE_FULL_TEXT);
                    openHomeActivity();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }

        } else {
            return true;
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                openHomeActivity();
                break;
            case R.id.nav_mode_single:
                PrefManager.putIntPref(getApplicationContext(), R.string.pref_type_mode, Costants.NAV_MODE_SINGLE);
                openHomeActivity();
                break;
            case R.id.nav_mode_multi:
                PrefManager.putIntPref(getApplicationContext(), R.string.pref_type_mode, Costants.NAV_MODE_MULTI);
                openHomeActivity();
                break;
            case R.id.nav_mode_small_text:
                PrefManager.putIntPref(getApplicationContext(), R.string.pref_type_mode, Costants.NAV_MODE_SMALL_TEXT);
                openHomeActivity();
                break;
            case R.id.nav_mode_full_text:
                PrefManager.putIntPref(getApplicationContext(), R.string.pref_type_mode, Costants.NAV_MODE_FULL_TEXT);
                openHomeActivity();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void menuNavigation(NavigationView navigationView) {

        switch (getLayoutResource()) {
            case R.layout.activity_article_detail:
                navigationView.inflateMenu(R.menu.activity_base_drawer_detail);
                menuItemDetail(navigationView.getMenu());
                break;
            case R.layout.activity_article_list:
            default:
                navigationView.inflateMenu(R.menu.activity_base_drawer_main);
                menuItemBase(navigationView.getMenu());
        }

        navigationView.setNavigationItemSelectedListener(this);

    }


    private void menuItemBase(Menu menu) {
        MenuItem itemHome = menu.findItem(R.id.nav_home);

        MenuItem itemModeSingle = menu.findItem(R.id.nav_mode_single);
        MenuItem itemModeMulti = menu.findItem(R.id.nav_mode_multi);

        itemHome.setIcon(new IconicsDrawable(this, MaterialDesignIconic.Icon.gmi_home)
                .respectFontBounds(true));

        itemModeSingle.setIcon(new IconicsDrawable(this, MaterialDesignIconic.Icon.gmi_collection_item_1)
                .respectFontBounds(true));

        itemModeMulti.setIcon(new IconicsDrawable(this, MaterialDesignIconic.Icon.gmi_collection_item)
                .respectFontBounds(true));

        switch (PrefManager.getIntPref(getApplicationContext(), R.string.pref_type_mode)) {
            case Costants.NAV_MODE_SINGLE:
                itemModeSingle.setChecked(true);
                itemModeSingle.setEnabled(false);
                break;
            case Costants.NAV_MODE_MULTI:
                itemModeMulti.setChecked(true);
                itemModeMulti.setEnabled(false);
                break;
            default:
                itemHome.setChecked(false);
        }

    }

    private void menuItemDetail(Menu menu) {
        MenuItem itemHome = menu.findItem(R.id.nav_home);

        MenuItem itemModeSmallText = menu.findItem(R.id.nav_mode_small_text);
        MenuItem itemModeFullText = menu.findItem(R.id.nav_mode_full_text);

        itemHome.setIcon(new IconicsDrawable(this, MaterialDesignIconic.Icon.gmi_home)
                .respectFontBounds(true));

        itemModeSmallText.setIcon(new IconicsDrawable(this, MaterialDesignIconic.Icon.gmi_collection_bookmark)
                .respectFontBounds(true));

        itemModeFullText.setIcon(new IconicsDrawable(this, MaterialDesignIconic.Icon.gmi_collection_text)
                .respectFontBounds(true));

        switch (PrefManager.getIntPref(getApplicationContext(), R.string.pref_type_mode)) {
            case Costants.NAV_MODE_SMALL_TEXT:
                itemModeSmallText.setChecked(true);
                itemModeSmallText.setEnabled(false);
                break;
            case Costants.NAV_MODE_FULL_TEXT:
                itemModeFullText.setChecked(true);
                itemModeFullText.setEnabled(false);
                break;
            default:
                itemHome.setChecked(false);
        }

    }

    void openHomeActivity() {
        startActivity(new Intent(this, ArticleListActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }


    void setLayoutResource(int layoutResource) {
        mLayoutResource = layoutResource;
    }

    private int getLayoutResource() {
        return mLayoutResource;
    }


    static void setPosition(int position) {
        sPosition = position;
    }

    public static int getPosition() {
        return sPosition;
    }

    private void activityShareText(Context context, String title, String text) {
        if ((context == null) && (TextUtils.isEmpty(text))) return;

        if (context != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(shareIntent);
        }
    }


}

