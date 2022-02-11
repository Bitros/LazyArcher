package com.bw.lazyarcher.component;

import com.bw.lazyarcher.layout.GameMainPage;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.element.FrameAnimationElement;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.security.SecureRandom;
import java.util.Optional;

public class Fish extends Image implements Component.BindStateChangedListener, Runnable {

    private int style;

    private SecureRandom random = new SecureRandom();

    private final int maxXRange = (int) (GameMainPage.WIDTH * 0.75);

    private final int minXRange = (int) (GameMainPage.WIDTH * 0.1);

    private final int maxYRange = (int) (GameMainPage.HEIGHT * 0.75);

    private final int minYRange = (int) (GameMainPage.HEIGHT * 0.1);

    private boolean overRange = false;

    private boolean dead = false;

    private int moveDuration;

    public Fish(Context context, int style) {
        super(context);
        this.style = style;
        FrameAnimationElement frameAnimationElement = new FrameAnimationElement(context, style);
        frameAnimationElement.start();
        this.setBackground(frameAnimationElement);
        //TODO fix magic number
        this.setLayoutConfig(new ComponentContainer.LayoutConfig(101, 54));
        setRandomPosition();
        setRandomOrientation();
        setRandomMoveDuration();
        setBindStateChangedListener(this);
        setAlpha(0.2f);
    }

    private void setRandomPosition() {
        this.setPosition(minXRange + random.nextInt(maxXRange - minXRange), minYRange + random.nextInt(maxYRange - minYRange));
    }

    private void setRandomOrientation() {
        this.setRotation(random.nextInt(360));
    }

    private void setRandomMoveDuration() {
        this.moveDuration = 250 + random.nextInt(150);
    }

    @Override
    public void onComponentBoundToWindow(Component component) {
        new Thread((Runnable) component).start();
    }

    @Override
    public void onComponentUnboundFromWindow(Component component) {

    }

    private int step = 3;

    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x0002, "LA_LOG");

    @Override
    public void run() {
        try {
            while (!overRange && !dead) {
                float contentPositionX = this.getContentPositionX();
                float contentPositionY = this.getContentPositionY();
                float degree = this.getRotation();
                if (Math.pow(contentPositionX - GameMainPage.WIDTH / 2, 2) + Math.pow(contentPositionY - GameMainPage.HEIGHT / 2, 2)
                        > Math.pow(GameMainPage.WIDTH / 2, 2)) {
                    overRange = true;
                }
                double tanC = Math.abs(Math.tan(Math.toRadians(degree)));
                if (degree == 0 || degree == 360) {
                    this.setPosition((int) contentPositionX + step, (int) contentPositionY);
                } else if (0 < degree && degree < 90) {
                    this.setPosition((int) contentPositionX + step, (int) (contentPositionY + step * tanC));
                } else if (degree == 90) {
                    this.setPosition((int) contentPositionX, (int) (contentPositionY + step));
                } else if (90 < degree && degree < 180) {
                    this.setPosition((int) contentPositionX - step, (int) (contentPositionY + step * tanC));
                } else if (degree == 180) {
                    this.setPosition((int) contentPositionX - step, (int) contentPositionY);
                } else if (180 < degree && degree < 270) {
                    this.setPosition((int) contentPositionX - step, (int) (contentPositionY - step * tanC));
                } else if (degree == 270) {
                    this.setPosition((int) contentPositionX, (int) (contentPositionY - step));
                } else if (270 < degree && degree < 360) {
                    this.setPosition((int) contentPositionX + step, (int) (contentPositionY - step * tanC));
                }

                this.setAlpha((float) (this.getAlpha() + 0.15));
                Thread.sleep(moveDuration);

                Optional<Arrow> first = GameMainPage.arrowQueue.stream().filter(arrow -> {
                    float[] collisionPoint = arrow.getCollisionPoint();
                    if (getContentPositionX() < collisionPoint[0]
                            && getContentPositionX() + getLayoutConfig().width > collisionPoint[0]
                            && getContentPositionY() < collisionPoint[1]
                            && getContentPositionY() + getLayoutConfig().height > collisionPoint[1]) {
                        HiLog.info(LABEL, "cx %f cy %f", collisionPoint[0], collisionPoint[1]);
                        HiLog.info(LABEL, "cp %f cp %f", getContentPositionX(), getContentPositionY());
                        return true;
                    } else {
                        return false;
                    }
                }).findFirst();
                if(first.isPresent()){
                    Arrow arrow = first.get();
                    if(GameMainPage.arrowQueue.remove(arrow)){
                        dead = true;
                        getContext().getUITaskDispatcher().asyncDispatch(() -> arrow.getComponentParent().removeComponent(arrow));
                    }
                }
            }
            getContext().getUITaskDispatcher().asyncDispatch(() -> {
                ComponentContainer componentContainer = (ComponentContainer) Fish.this.getComponentParent();
                componentContainer.removeComponent(Fish.this);
                componentContainer.addComponent(new Fish(getContext(), style));
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
