package com.bw.lazyarcher.component;

import com.bw.lazyarcher.ResourceTable;
import com.bw.lazyarcher.layout.GameMainPage;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.app.Context;

import java.security.SecureRandom;

public class Bubble extends Image implements Component.BindStateChangedListener {

    public Bubble(Context context) {
        super(context);
        this.setPixelMap(ResourceTable.Media_bubble);
        setRandomPosition();
        this.setBindStateChangedListener(this);
    }

    private SecureRandom random = new SecureRandom();

    public void floatUpAndFade() {
        AnimatorProperty animatorProperty = this.createAnimatorProperty();
        animatorProperty
                .moveFromY(this.getContentPositionY()).moveToY(0)
                .alphaFrom(1).alpha(0.3f)
                .setDuration((long) (this.getContentPositionY()* 30))
                .setLoopedListener(animator -> {
                    ((Bubble) ((AnimatorProperty) animator).getTarget()).setRandomPosition();
                })
                .setLoopedCount(AnimatorValue.INFINITE);
        animatorProperty.start();
    }

    private void setRandomPosition() {
        this.setPosition(Math.abs(random.nextInt()) % GameMainPage.WIDTH, Math.abs(random.nextInt()) % GameMainPage.HEIGHT);
    }

    @Override
    public void onComponentBoundToWindow(Component component) {
        this.floatUpAndFade();
    }

    @Override
    public void onComponentUnboundFromWindow(Component component) {

    }
}
