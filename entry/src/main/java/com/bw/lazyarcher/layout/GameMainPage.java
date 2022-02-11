package com.bw.lazyarcher.layout;

import com.bw.lazyarcher.Utils;
import com.bw.lazyarcher.component.*;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.PositionLayout;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameMainPage extends PositionLayout implements Component.TouchEventListener {

    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x0002, "LA_LOG");

    public static int WIDTH;

    public static int HEIGHT;

    private final int fishNumber = 4;

    private final int bubbleNumber = 10;

    private int fishKinds;

    private List<Integer> fishResourceID;

    private Helm helm;

    private Arrow arrow;

    private Background bg;

    public static BlockingQueue<Arrow> arrowQueue = new LinkedBlockingQueue();

    public GameMainPage(Context context) {
        super(context);
        bg = new Background(getContext());
        arrow = new Arrow(getContext());
        helm = new Helm(getContext());
        addComponent(bg);
        initFish();
        initBubble();
        addComponent(helm);
        addComponent(arrow);
        setTouchEventListener(this);
    }

    private void initBubble() {
        for (int i = 0; i < bubbleNumber; i++) {
            addComponent(new Bubble(getContext()));
        }
    }

    private void initFish() {
        fishResourceID = Utils.getResourceByFilePrefixAndType("fish", "Animation");
        fishKinds = fishResourceID.size();
        HiLog.info(LABEL, "Load %{public}d kinds of fish", fishKinds);
        for (Integer id : fishResourceID) {
            for (int i = 0; i < fishNumber; i++) {
                Fish fish = new Fish(getContext(), id);
                addComponent(fish);
            }
        }
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        float touchX = touchEvent.getPointerPosition(0).getX();
        float touchY = touchEvent.getPointerPosition(0).getY();
        switch (touchEvent.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
            case TouchEvent.POINT_MOVE:
                arrow.rotateByTouchPoint(touchX, touchY);
                break;
            case TouchEvent.PRIMARY_POINT_UP:
                shoot(touchX, touchY, arrow.getRotation());
                break;
        }
        return true;
    }

    private void shoot(float touchX, float touchY, float degree) {
        Arrow tempArrow = new Arrow(getContext());
        tempArrow.setRotation(degree);
        AnimatorProperty animatorProperty = tempArrow.createAnimatorProperty();
        animatorProperty
                .moveFromX(tempArrow.getContentPositionX())
                .moveFromY(tempArrow.getContentPositionY())
                .setDuration(1000);

        double tanC = Math.abs(Math.tan(Math.toRadians(degree)));
        if (0 <= degree && degree < 45) {
            animatorProperty.moveToX((float) (tempArrow.getContentPositionX() + tempArrow.getContentPositionY() * tanC)).moveToY(0);
        } else if (45 <= degree && degree < 90) {
            animatorProperty.moveToX(WIDTH).moveToY((float) (tempArrow.getContentPositionY() - (WIDTH - tempArrow.getContentPositionX()) / tanC));
        } else if (degree == 90) {
            animatorProperty.moveToX(WIDTH);
        } else if (90 < degree && degree < 135) {
            animatorProperty.moveToX(WIDTH).moveToY((float) (tempArrow.getContentPositionY() + (WIDTH - tempArrow.getContentPositionX()) / tanC));
        } else if (135 <= degree && degree < 180) {
            animatorProperty.moveToX(tempArrow.getContentPositionX() + (float) ((HEIGHT - tempArrow.getContentPositionY()) * tanC)).moveToY(HEIGHT);
        } else if (180 <= degree && degree < 225) {
            animatorProperty.moveToX(tempArrow.getContentPositionX() - (float) ((HEIGHT - tempArrow.getContentPositionY()) * tanC)).moveToY(HEIGHT);
        } else if (225 <= degree && degree < 270) {
            animatorProperty.moveToX(0).moveToY(tempArrow.getContentPositionY() + (float) (tempArrow.getContentPositionX() / tanC));
        } else if (degree == 270) {
            animatorProperty.moveToX(0);
        } else if (270 <= degree && degree < 315) {
            animatorProperty.moveToX(0).moveToY(tempArrow.getContentPositionY() - (float) (tempArrow.getContentPositionX() / tanC));
        } else if (315 <= degree && degree <= 360) {
            animatorProperty.moveToX(tempArrow.getContentPositionX() - (float) (tempArrow.getContentPositionY() * tanC)).moveToY(0);
        }
        HiLog.info(LABEL, "arrowX: %{public}f, arrowY: %{public}f", tempArrow.getContentPositionX(), tempArrow.getContentPositionY());
        HiLog.info(LABEL, "touchX: %{public}f, touchY: %{public}f, degree: %{public}f, tanC: %{public}f", touchX, touchY, degree, tanC);
        animatorProperty.setStateChangedListener(new Animator.StateChangedListener() {
            @Override
            public void onStart(Animator animator) {
                arrow.setAlpha(0);
            }

            @Override
            public void onStop(Animator animator) {

            }

            @Override
            public void onCancel(Animator animator) {

            }

            @Override
            public void onEnd(Animator animator) {
                Arrow a = (Arrow) ((AnimatorProperty) animator).getTarget();
                removeComponent(a);
                arrow.setAlpha(1);
                arrowQueue.remove(a);
            }

            @Override
            public void onPause(Animator animator) {
                onEnd(animator);
            }

            @Override
            public void onResume(Animator animator) {

            }
        });
        tempArrow.setBindStateChangedListener(new BindStateChangedListener() {
            @Override
            public void onComponentBoundToWindow(Component component) {
                animatorProperty.start();
            }

            @Override
            public void onComponentUnboundFromWindow(Component component) {
            }
        });
        addComponent(tempArrow);
        arrowQueue.add(tempArrow);
    }
}
