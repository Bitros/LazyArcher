package com.bw.lazyarcher;

import com.bw.lazyarcher.slice.MainAbilitySlice;
import com.bw.lazyarcher.slice.TestAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
//        super.setMainRoute(TestAbilitySlice.class.getName());
    }
}
