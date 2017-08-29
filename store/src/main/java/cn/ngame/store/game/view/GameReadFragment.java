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

    public static GameReadFragment newInstance(GameInfo gameInfo) {
        GameReadFragment fragment = new GameReadFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GameInfo.TAG, gameInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Activity context;
    private GameInfo gameInfo;

    private TextView tv_content;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取初始参数
        //gameInfo = (GameInfo) getArguments().getSerializable(GameInfo.TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();

        View rootLayout = inflater.inflate(R.layout.fragment_game_strategy, container, false);
    /*    tv_content = (TextView) rootLayout.findViewById(R.id.tv_summary);

        if (gameInfo != null && gameInfo.gameStrategy != null) {
            tv_content.setText(gameInfo.gameStrategy.getStrategyContent());
            getData();
        }*/


        return rootLayout;
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

                    tv_content.setText(strategy.getStrategyContent());

                } else {
                    Log.d(TAG, "正在整理中...");
                    tv_content.setText("正在整理中...");
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
