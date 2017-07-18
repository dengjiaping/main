package cn.ngame.store.activity.sm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;


/**
 * App设置页面
 * Created by liguoliang on 2016/12/7.
 */
public class SupportedJoypadListActivity extends BaseFgActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_supported_joypad_list);

        Button backBt = (Button) findViewById(R.id.left_bt);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backBt.setText("游戏手柄支持列表");
        TextView centerTv = (TextView) findViewById(R.id.center_tv);
        centerTv.setVisibility(View.INVISIBLE);

    }
}
