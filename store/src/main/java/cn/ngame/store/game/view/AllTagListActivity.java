package cn.ngame.store.game.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;

/**
 * 游戏精选列表的页面
 * Created by zeng on 2016/8/24.
 */
public class AllTagListActivity extends BaseFgActivity {

    private RecyclerView mRecyclerView;
    private SingeItemAdapter mAdapter;
    private List<String> mDatas;
    private AllTagListActivity content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_singe_item_list);
        content=this;
        Button backBt = (Button) findViewById(R.id.left_bt);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backBt.setText("的标签");
        TextView centerTv = (TextView) findViewById(R.id.center_tv);
        centerTv.setVisibility(View.INVISIBLE);

        initDatas();
        //得到控件
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new SingeItemAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initDatas() {
        mDatas = new ArrayList<>(Arrays.asList("中文", "冒险", "射击", "单机", "原生", "动作", "横版", "收集"));
    }

    public class SingeItemAdapter extends
            RecyclerView.Adapter<SingeItemAdapter.ViewHolder> {
        private LayoutInflater mInflater;
        private List<String> mDatas;

        public SingeItemAdapter(Context context, List<String> datats) {
            mInflater = LayoutInflater.from(context);
            mDatas = datats;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View arg0) {
                super(arg0);
            }
            TextView mTxt;
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        /**
         * 创建ViewHolder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.item_singe_tv_iv_next,
                    viewGroup, false);
            SingeItemAdapter.ViewHolder viewHolder = new SingeItemAdapter.ViewHolder(view);

            viewHolder.mTxt = (TextView) view.findViewById(R.id.singer_item_tv);
            return viewHolder;
        }

        /**
         * 设置值
         */
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            final String itemTitle = mDatas.get(i);
            viewHolder.mTxt.setText(itemTitle);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setClass(content, GameListActivity.class);
                    i.putExtra(KeyConstant.ID, 369 + "");// 动作游戏精选 getId()==369
                    i.putExtra(KeyConstant.TITLE, itemTitle);//list.get(position).getTypeName()
                    startActivity(i);
                }
            });
        }

    }

    /**
     * 标签列表,,可以从服务器,也可能从详情带过来(不用做请求)
     */
    private void getGameList() {

        String url = Constant.WEB_SITE + Constant.URL_GAME_SELECTION_MORE;
        Response.Listener<JsonResult<List<GameInfo>>> successListener = new Response.Listener<JsonResult<List<GameInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<GameInfo>> result) {
                if (result == null) {
                    return;
                }

                if (result.code == 0) {

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<List<GameInfo>>> request = new GsonRequest<JsonResult<List<GameInfo>>>(
                Request.Method.POST, url, successListener,
                errorListener, new TypeToken<JsonResult<List<GameInfo>>>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }
}
