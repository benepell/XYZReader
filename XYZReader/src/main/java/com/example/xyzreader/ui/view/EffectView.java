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

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.transitionseverywhere.Explode;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;


public class EffectView {

    private final View mView;

    public EffectView(View view) {
        mView = view;
    }

    public void explode (RecyclerView recyclerView) {
        // save rect of view in screen coordinated
        final Rect viewRect = new Rect();
        mView.getGlobalVisibleRect(viewRect);

        TransitionSet set = new TransitionSet()
                .addTransition(new Explode().setEpicenterCallback(new com.transitionseverywhere.Transition.EpicenterCallback() {
                    @Override
                    public Rect onGetEpicenter(com.transitionseverywhere.Transition transition) {
                        return viewRect;
                    }
                }).excludeTarget(mView, true))
                .addTransition(new Fade().addTarget(mView))
                .addListener(new com.transitionseverywhere.Transition.TransitionListenerAdapter() {
                    @Override
                    public void onTransitionEnd(com.transitionseverywhere.Transition transition) {
                        transition.removeListener(this);
                        //  onbackpressed
                    }
                });
        TransitionManager.beginDelayedTransition(recyclerView, set);

        // remove all views from Recycler View
        recyclerView.setAdapter(null);
    }


}
