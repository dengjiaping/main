package cn.ngame.store.game.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.game.bean.GameStrategy;

/**
 * 必读
 *
 * @author zeng
 * @since 2016/12/15
 */
public class GameReadFragment extends Fragment {

    public static final String TAG = GameReadFragment.class.getSimpleName();

    private static GameReadFragment gameStrategyFragment = null;
    private TextView titleTv;
    private TextView contentTv;

    public static GameReadFragment newInstance(GameInfo gameInfo) {
        android.util.Log.d(TAG, "newInstance: "+gameInfo);
        GameReadFragment fragment = new GameReadFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GameInfo.TAG, gameInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Activity context;
    private GameInfo gameInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.util.Log.d(TAG, "onCreate000 : ");
        gameInfo = (GameInfo) getArguments().getSerializable(GameInfo.TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_game_strategy, container, false);
        titleTv = (TextView) view.findViewById(R.id.strategy_title_tv);
        contentTv = (TextView) view.findViewById(R.id.strategy_content_tv);
        if (gameInfo != null && gameInfo.gameStrategy != null) {
            GameStrategy gameStrategy = gameInfo.gameStrategy;
            titleTv.setText("");
            String strategyContent = gameStrategy.getStrategyContent();
            contentTv.setText(strategyContent == null ? "" : strategyContent);
            //getData();
        } else {
            contentTv.setText("暂无数据~");
        }
        return view;
    }

    /**
     * 获取攻略
     */
    private void getData() {
        String url = Constant.WEB_SITE + Constant.URL_GAME_STRATEGY;
        Response.Listener<JsonResult<GameStrategy>> successListener = new Response.Listener<JsonResult<GameStrategy>>() {
            @Override
            public void onResponse(JsonResult<GameStrategy> result) {

                if (result == null || result.code != 0) {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                }
                GameStrategy strategy = result.data;

                if (strategy != null) {
                    Log.d(TAG, "请求成功");

                } else {
                    Log.d(TAG, "正在整理中...");
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

        Request<JsonResult<GameStrategy>> request = new GsonRequest<JsonResult<GameStrategy>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<GameStrategy>>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.GAME_ID, gameInfo.id + "");
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);

    }


}
