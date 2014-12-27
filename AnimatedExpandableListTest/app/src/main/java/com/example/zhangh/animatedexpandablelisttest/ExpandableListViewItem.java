package com.example.zhangh.animatedexpandablelisttest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import java.util.List;

/**
 * Created by zhangh on 12/26/2014.
 */
public class ExpandableListViewItem extends LinearLayout {

    private static final int ANIMATION_DURATION = 300;

    private Context context;

    private FrameLayout parentHolder;
    private ToggleButton expandToggle;
    private LinearLayout childHolder;

    private View parentView;
    private List<View> childView;

    public ExpandableListViewItem(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ExpandableListViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ExpandableListViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public ExpandableListViewItem(Context context, View parentView, List<View> childrenView) {
        super(context);
        this.context = context;
        init();
        setParentView(parentView);
        setChildView(childrenView);
    }

    private void init() {
        inflate(context, R.layout.expandable_listview_item, this);
        this.setOrientation(LinearLayout.VERTICAL);
        parentHolder = (FrameLayout) findViewById(R.id.expandable_listview_item_parent);
        expandToggle = (ToggleButton) findViewById(R.id.expandable_listview_expand_toggle);
        childHolder = (LinearLayout) findViewById(R.id.expandable_listview_item_child);
        expandToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (childHolder.getChildCount() > 0) {
                    if (isChecked) {
                        // expand if has child
                        expandChild();
                    } else {
                        // collapse if has child
                        collapseChild();
                    }
                }
            }
        });
    }

    public void expandChild() {
        childHolder.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        childHolder.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                childHolder.getViewTreeObserver().removeOnPreDrawListener(this);

                int height = childHolder.getMeasuredHeight();

                ValueAnimator heightAnimator = ValueAnimator.ofInt(0, height);
                heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ViewGroup.LayoutParams layoutParams = childHolder.getLayoutParams();
                        layoutParams.height = (int) animation.getAnimatedValue();
                        childHolder.setLayoutParams(layoutParams);
                    }
                });
                heightAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        childHolder.setVisibility(View.VISIBLE);
                        // disable views, so that it wont get confused if there's some action in the middle of the animation
                        for (int i = 0; i < ExpandableListViewItem.this.getChildCount(); i++) {
                            View v = ExpandableListViewItem.this.getChildAt(i);
                            v.setHasTransientState(true);
                        }
                        expandToggle.setEnabled(false);
                        expandToggle.setClickable(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        for (int i = 0; i < ExpandableListViewItem.this.getChildCount(); i++) {
                            View v = ExpandableListViewItem.this.getChildAt(i);
                            v.setHasTransientState(false);
                        }
                        expandToggle.setEnabled(true);
                        expandToggle.setClickable(true);
                    }
                });

                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(childHolder, View.ALPHA, 0f, 1f);

                AnimatorSet set = new AnimatorSet();
                set.playTogether(heightAnimator, alphaAnimator);
                set.setDuration(ANIMATION_DURATION);
                set.start();

                return true;
            }
        });
    }

    public void collapseChild() {
        childHolder.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                childHolder.getViewTreeObserver().removeOnPreDrawListener(this);

                int height = childHolder.getMeasuredHeight();

                ValueAnimator heightAnimator = ValueAnimator.ofInt(height, 0);
                heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ViewGroup.LayoutParams layoutParams = childHolder.getLayoutParams();
                        layoutParams.height = (int) animation.getAnimatedValue();
                        childHolder.setLayoutParams(layoutParams);
                    }
                });
                heightAnimator.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        // disable views, so that it wont get confused if there's some action in the middle of the animation
                        for (int i = 0; i < ExpandableListViewItem.this.getChildCount(); i++) {
                            View v = ExpandableListViewItem.this.getChildAt(i);
                            v.setHasTransientState(true);
                        }
                        expandToggle.setEnabled(false);
                        expandToggle.setClickable(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        childHolder.setVisibility(View.GONE);
                        for (int i = 0; i < ExpandableListViewItem.this.getChildCount(); i++) {
                            View v = ExpandableListViewItem.this.getChildAt(i);
                            v.setHasTransientState(false);
                        }
                        expandToggle.setEnabled(true);
                        expandToggle.setClickable(true);
                    }
                });

                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(childHolder, View.ALPHA, 1f, 0f);

                AnimatorSet set = new AnimatorSet();
                set.playTogether(heightAnimator, alphaAnimator);
                set.setDuration(ANIMATION_DURATION);
                set.start();

                return true;
            }
        });
    }

    public void setParentView(View parentView) {
        this.parentView = parentView;
        parentHolder.removeAllViews();
        parentHolder.addView(this.parentView);
    }

    public void setChildView(List<View> childView) {
        this.childView = childView;
        childHolder.removeAllViews();
        for (View v : childView) {
            childHolder.addView(new Divider(context));
            childHolder.addView(v);
        }
    }

    class Divider extends View {

        public Divider(Context context) {
            super(context);
            init();
        }

        public Divider(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public Divider(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.height = 1;
            this.setLayoutParams(layoutParams);
            this.setBackgroundResource(android.R.drawable.divider_horizontal_dark);
        }
    }
}
