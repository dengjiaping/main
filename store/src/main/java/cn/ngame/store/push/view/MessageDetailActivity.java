package cn.ngame.store.push.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.push.model.MessageDetail;
import cn.ngame.store.push.presenter.PushMsgDetailPresenter;

/**
 * 显示消息详情
 * Created by zeng on 2016/11/23.
 */
public class MessageDetailActivity extends BaseFgActivity implements IPushMsgDetailView{

    private PushMsgDetailPresenter presenter;
    private WebView web_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.push_message_detail_activity);

        TextView tv_title_bar = (TextView) findViewById(R.id.tv_title_bar);
        TextView tv_back = (TextView) findViewById(R.id.left_but);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        web_content = (WebView) findViewById(R.id.web_content);
        WebSettings wSet = web_content.getSettings();
        wSet.setDefaultTextEncodingName("utf-8");
        wSet.setJavaScriptEnabled(true);


        final long msgId = getIntent().getLongExtra("msgId",0);
        final int msgType = getIntent().getIntExtra("type",1);

        tv_title_bar.setText("活动");

        presenter = new PushMsgDetailPresenter(this,this);
        presenter.showDetail(msgType,msgId);

        //将消息标记为已读
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.markHasRead(msgType,msgId);
            }
        }).start();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        final long msgId = getIntent().getLongExtra("msgId",0);
        final int msgType = getIntent().getIntExtra("type",1);

        presenter.showDetail(msgType,msgId);

        //将消息标记为已读
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.markHasRead(msgType,msgId);
            }
        }).start();

    }

    @Override
    public void showMsgDetail(MessageDetail detailMsg) {

        if(detailMsg != null){
            String data = "<html><body>"+detailMsg.getMsgContent()+"</body></html>";
            web_content.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
        }
    }
}
