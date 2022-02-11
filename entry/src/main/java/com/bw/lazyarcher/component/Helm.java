package com.bw.lazyarcher.component;

import com.bw.lazyarcher.ResourceTable;
import com.bw.lazyarcher.layout.GameMainPage;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.app.Context;

public class Helm extends Image implements Component.BindStateChangedListener {

    private final int originX;

    private final int originY;

    public Helm(Context context) {
        super(context);
        this.setPixelMap(ResourceTable.Media_helm_default);
        int width = getPixelMap().getImageInfo().size.width;
        int height = getPixelMap().getImageInfo().size.height;
        originX = GameMainPage.WIDTH / 2 - width / 2;
        originY = GameMainPage.HEIGHT - height;
        resetPosition();
        this.setBindStateChangedListener(this);
    }


    public void setStyle(int style) {
        this.setPixelMap(style);
    }

    public void startRotate() {
        AnimatorProperty animatorProperty = this.createAnimatorProperty();
        animatorProperty
                .rotate(360)
                .setDuration(5000)
                .setLoopedCount(AnimatorValue.INFINITE);
        animatorProperty.start();
    }

    public void resetPosition() {
        this.setPosition(originX, originY);
    }

    @Override
    public void onComponentBoundToWindow(Component component) {
        this.startRotate();
    }

    @Override
    public void onComponentUnboundFromWindow(Component component) {

    }
}
