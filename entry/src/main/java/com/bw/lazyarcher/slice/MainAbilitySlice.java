package com.bw.lazyarcher.slice;

import com.bw.lazyarcher.layout.GameMainPage;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbilitySlice extends AbilitySlice {

    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x0001, "LA_LOG");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        DisplayAttributes realAttributes = DisplayManager.getInstance().getDefaultDisplay(getContext()).get().getRealAttributes();
        HiLog.info(LABEL, "Display width: %{public}d, height: %{public}d", realAttributes.width, realAttributes.height);
        GameMainPage.WIDTH = realAttributes.width;
        GameMainPage.HEIGHT = realAttributes.height;
        super.setUIContent(new GameMainPage(getContext()));
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

}
