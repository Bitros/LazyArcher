package com.bw.lazyarcher.slice;

import com.bw.lazyarcher.ResourceTable;
import com.bw.lazyarcher.component.Fish;
import com.bw.lazyarcher.layout.GameMainPage;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class TestAbilitySlice extends AbilitySlice {
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x0002, "LA_LOG");

    private final int maxXRange = (int) (GameMainPage.WIDTH * 0.7);

    private final int minXRange = (int) (GameMainPage.WIDTH * 0.1);

    private final int maxYRange = (int) (GameMainPage.HEIGHT * 0.8);

    private final int minYRange = (int) (GameMainPage.HEIGHT * 0.2);
    private boolean started =false;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        DisplayAttributes realAttributes = DisplayManager.getInstance().getDefaultDisplay(getContext()).get().getRealAttributes();
        HiLog.info(LABEL, "Display width: %{public}d, height: %{public}d", realAttributes.width, realAttributes.height);
        GameMainPage.WIDTH = realAttributes.width;
        GameMainPage.HEIGHT = realAttributes.height;
        //创建播放动画的组件
        Fish fish = new Fish(getContext(), ResourceTable.Animation_fish_shark);
        PositionLayout layout = new PositionLayout(getContext());
        layout.addComponent(fish);
        super.setUIContent(layout);
    }
}