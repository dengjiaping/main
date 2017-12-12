package cn.ngame.store.activity.hub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.core.utils.KeyConstant;

/**
 * 圈子
 * Created by liguoliang on 2017/11/23 0023.
 */

public class GameHubActivity extends BaseFgActivity {
    protected static final String TAG = GameHubActivity.class.getSimpleName();
    private LinearLayout ll_back;
    private GameHubActivity mContext;
    private TextView titleTv, postIdTv;
    private int postId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_hub);
        initStatusBar();
        postId = getIntent().getIntExtra(KeyConstant.postId, 0);
        mContext = this;
        init();
    }

    private void init() {
        ll_back = findViewById(R.id.ll_back);
        titleTv = findViewById(R.id.tv_title);
        postIdTv = findViewById(R.id.post_id_tv);
        postIdTv.setText(String.valueOf(postId));
        titleTv.setText("游戏圈子");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}