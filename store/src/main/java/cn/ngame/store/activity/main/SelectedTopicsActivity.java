package cn.ngame.store.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBodyBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.main.YunduanClient;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.TopicsListAdapter;
import cn.ngame.store.core.utils.KeyConstant;

/**
 * 专题精选
 * Created by gp on 2017/4/13 0013.
 */

public class SelectedTopicsActivity extends BaseFgActivity {

    private int ID = 22;
    private Button tv_title;
    private ListView listView;
    private String title;
    List<YunduanBean.DataBean> list = new ArrayList<>();
    TopicsListAdapter adapter;
    private SelectedTopicsActivity content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_topics_activity);
        content = SelectedTopicsActivity.this;
        init();
    }

    private void init() {
        tv_title = (Button) findViewById(R.id.left_bt);
        tv_title.setText("专题");
        listView = (ListView) findViewById(R.id.listView);
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setClass(SelectedTopicsActivity.this, TopicsDetailActivity.class);
                i.putExtra(KeyConstant.ID, list.get(position).getId());
                i.putExtra(KeyConstant.TITLE, list.get(position).getTypeName());
                i.putExtra(KeyConstant.DESC, list.get(position).getTypeDesc());
                i.putExtra(KeyConstant.URL, list.get(position).getLogoUrl());
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
        if (adapter == null) {
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
