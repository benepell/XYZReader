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
