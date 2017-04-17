package com.xcommon.activity;

import android.app.Activity;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import com.xcommon.utils.invoke.ActivityClassUtil;

/**
 * Created by Photostsrs on 2016/8/17.
 */
public class UMActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onEvent(getApplicationContext(), ActivityClassUtil.getRunningActivityName(this)); // 友盟统计
    }
}
