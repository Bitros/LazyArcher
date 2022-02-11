package com.bw.lazyarcher.component;

import com.bw.lazyarcher.ResourceTable;
import ohos.agp.components.Image;
import ohos.app.Context;

public class Background extends Image {


    public Background(Context context) {
        super(context);
        this.setPixelMap(ResourceTable.Media_background_default);
    }

    public void setStyle(int style) {
        this.setPixelMap(style);
    }
}
