package com.bw.lazyarcher.component;

import com.bw.lazyarcher.ResourceTable;
import com.bw.lazyarcher.layout.GameMainPage;
import ohos.agp.components.Image;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class Arrow extends Image {

    private int originX;

    private int originY;

    private int originCenterX;

    private int originCenterY;

    private int width;

    private int height;

    public Arrow(Context context) {
        super(context);
        this.setPixelMap(ResourceTable.Media_arrow_default);
        width = getPixelMap().getImageInfo().size.width;
        height = getPixelMap().getImageInfo().size.height;
        originX = GameMainPage.WIDTH / 2 - width / 2;
        originY = GameMainPage.HEIGHT - height;
        originCenterX = originX + width / 2;
        originCenterY = originY + height / 2;
        setPosition(originX, originY);
    }

    public void setStyle(int style) {
        this.setPixelMap(style);
    }

    public void rotateByTouchPoint(float x, float y) {
        double c = Math.sqrt(Math.pow(y - originCenterY, 2) + Math.pow(x - originCenterX, 2));
        double cosC = (x - originCenterX) / c;
        if (cosC >= 0) {
            if (y - originCenterY <= 0) {
                this.setRotation((float) (90 - Math.toDegrees(Math.acos(cosC))));
            } else {
                this.setRotation((float) (90 + Math.toDegrees(Math.acos(cosC))));
            }
        } else {
            if (y - originCenterY <= 0) {
                this.setRotation((float) (270 + 180 - Math.toDegrees(Math.acos(cosC))));
            } else {
                this.setRotation((float) (90 + Math.toDegrees(Math.acos(cosC))));
            }
        }
    }

    public float[] getCollisionPoint() {
        float degree = getRotation();
        float contentPositionX = getContentPositionX();
        float contentPositionY = getContentPositionY();
        float[] result = new float[2];
        if(degree <= 90){
            double deltaX = this.height / 2  * Math.sin(Math.toRadians(degree));
            double deltaY = this.height / 2  * Math.cos(Math.toRadians(degree));
            result[0] = (float) (contentPositionX + deltaX) + this.width / 2;
            result[1] = (float) (contentPositionY + (this.height / 2 - deltaY));
        }else if(degree <= 180){
            double deltaX = this.height / 2  * Math.cos(Math.toRadians(degree-90));
            double deltaY = this.height / 2  * Math.sin(Math.toRadians(degree-90));
            result[0] = (float) (contentPositionX + deltaX) + this.width / 2;
            result[1] = (float) (contentPositionY + (this.height / 2 + deltaY));
        }else if(degree <= 270){
            double deltaX = this.height / 2  * Math.sin(Math.toRadians(degree-180));
            double deltaY = this.height / 2  * Math.cos(Math.toRadians(degree-180));
            result[0] = (float) (contentPositionX - deltaX) + this.width / 2;
            result[1] = (float) (contentPositionY + (this.height / 2 + deltaY));
        }else if(degree <= 360){
            double deltaX = this.height / 2  * Math.cos(Math.toRadians(degree-270));
            double deltaY = this.height / 2  * Math.sin(Math.toRadians(degree-270));
            result[0] = (float) (contentPositionX - deltaX) + this.width / 2;
            result[1] = (float) (contentPositionY + (this.height / 2 - deltaY));
        }
        return result;
    }
}
