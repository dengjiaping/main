package cn.ngame.store.activity.hub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;

/**
 * 圈子
 * Created by gp on 2017/3/23 0023.
 */

public class HubActivity extends BaseFgActivity {

    private LinearLayout ll_back;
    private HubActivity mContext;
    private TextView titleTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        initStatusBar();
        mContext = this;
        init();
    }

    private void init() {
        ll_back =  findViewById(R.id.ll_back);
        titleTv = findViewById(R.id.tv_title);
        titleTv.setText("圈子");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
