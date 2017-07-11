package cn.ngame.store.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBodyBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.main.YunduanClient;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.TopicsListAdapter;

/**
 * 专题精选
 * Created by gp on 2017/4/13 0013.
 */

public class SelectedTopicsActivity extends BaseFgActivity {

    private int ID = 22;
    private LinearLayout ll_back;
    private TextView tv_title;
    private ListView listView;
    private String title;
    List<YunduanBean.DataBean> list = new ArrayList<>();
    TopicsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_topics_activity);
        init();
    }

    private void init() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        listView = (ListView) findViewById(R.id.listView);
        title = getIntent().getStringExtra("title");
        tv_title.setText(title);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedTopicsActivity.this.finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setClass(SelectedTopicsActivity.this, TopicsDetailActivity.class);
                i.putExtra("id", list.get(position).getId());
                i.putExtra("title", list.get(position).getTypeName());
                i.putExtra("desc", list.get(position).getTypeDesc());
                i.putExtra("url", list.get(position).getLogoUrl());
                startActivity(i);
            }
        });
        runService();
    }

    public void runService() {
        YunduanBodyBean bodyBean = new YunduanBodyBean();
        bodyBean.setMarkId(ID);
        new YunduanClient(this, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<YunduanBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(YunduanBean result) {
                        if (result != null && result.getCode() == 0) {
                            setData(result);
                        } else {
//                            ToastUtil.show(getActivity(), result.getMsg());
                        }
                    }
                });
    }

    private void setData(YunduanBean result) {
        if (result.getData() == null) {
            return;
        }
        if (result.getData().size() <= 0) {
            return;
        }
        list.addAll(result.getData());
        if(adapter == null){
            adapter = new TopicsListAdapter(this, list);
            listView.setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
        list = null;
    }
}
