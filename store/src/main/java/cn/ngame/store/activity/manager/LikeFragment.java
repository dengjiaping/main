package cn.ngame.store.activity.manager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBean;
import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBody;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.main.LikeListClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.adapter.LikeFragmentAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.ActionItem;
import cn.ngame.store.view.QuickAction;

/**
 * 下载更新fragment (懒加载-当滑动到当前fragment时，才去加载。而不是进入到activity时，加载所有fragment)
 * Created by gp on 2017/3/3 0003.
 */

public class LikeFragment extends BaseSearchFragment {

    private ListView listView;
    private PageAction pageAction;
    public static int PAGE_SIZE = 10;
    private int typeValue;
    private String type;
    protected QuickAction mItemClickQuickAction;
    private LikeFragmentAdapter likeAdapter;
    /**
     * 当前点击的列表 1.下载列表 2.完成列表
     */
    private FragmentActivity content;
    private boolean isShow = true;
    private List<LikeListBean.DataBean.GameListBean> gameList;

    public static LikeFragment newInstance(String type, int arg) {
        LikeFragment fragment = new LikeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putInt("typeValue", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_installed;
    }

    private List<TimerTask> timerTasks = new ArrayList<>();

    @Override
    protected void initViewsAndEvents(View view) {
        content = getActivity();
        typeValue = getArguments().getInt("typeValue", 1);
        type = getArguments().getString("type");
        listView = (ListView) view.findViewById(R.id.listView);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);

        initPop();
        likeAdapter = new LikeFragmentAdapter(content, getSupportFragmentManager(), mItemClickQuickAction, timerTasks);
        listView.setAdapter(likeAdapter);

    }

    private void getLikeList() {
        //tabPosition :0=全部   1=手柄   2=破解   3=汉化  4=特色
        LikeListBody bodyBean = new LikeListBody();
        bodyBean.setUserCode(StoreApplication.userCode);
        bodyBean.setStartRecord(pageAction.getCurrentPage());
        bodyBean.setRecords(PAGE_SIZE);
        new LikeListClient(content, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<LikeListBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                        //ToastUtil.show(content, getString(R.string.pull_to_refresh_network_error));
                    }

                    @Override
                    public void onNext(LikeListBean result) {
                        if (result != null && result.getCode() == 0) {
                            LikeListBean.DataBean data = result.getData();
                            if (data != null) {
                                gameList = data.getGameList();
                                if (null != likeAdapter) {
                                    likeAdapter.setDate(gameList);
                                }
                            }
                        } else {
                            //ToastUtil.show(getActivity(), result.getMsg());
                            Log.d(TAG, "onNext: 请求成功,返回数据失败");
                        }
                    }
                });
    }

    private void initPop() {
        // 设置Action
        mItemClickQuickAction = new QuickAction(content, QuickAction.VERTICAL);
        ActionItem pointItem = new ActionItem(1, "不再喜欢");
        mItemClickQuickAction.addActionItem(pointItem);

        mItemClickQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                if (pos == 0 && null != likeAdapter) {
                    //获取gameId  传给服务器 不再喜欢
                    int gamePosition = likeAdapter.getItemGameId();
                    if (gameList != null && gamePosition < gameList.size()) {
                        cancelFavorite(gamePosition);
                    }
                }
                //取消弹出框
                source.dismiss();
            }
        });
    }

    private void cancelFavorite(final int position) {
        final long gameId = gameList.get(position).getId();
        Log.d(TAG, "cancelFavorite: " + gameId);
        String url = Constant.WEB_SITE + Constant.URL_DEL_FAVORITE;
        Response.Listener<JsonResult> successListener = new Response.Listener<JsonResult>() {
            @Override
            public void onResponse(JsonResult result) {
                if (result == null) {
                    Toast.makeText(content, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.code == 0) {
                    ToastUtil.show(content, "取消成功");
                    gameList.remove(position);
                    if (null != likeAdapter) {
                        likeAdapter.setDate(gameList);
                    }
                } else {
                    ToastUtil.show(content, "取消收藏失败");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "取消喜欢失败：网络连接错误！" + volleyError.getMessage());
            }
        };

        Request<JsonResult> versionRequest = new GsonRequest<JsonResult>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //{"userCode":"UC1500609205627","gameId":146,"appTypeId":0}
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.GAME_ID, String.valueOf(gameId));
                params.put(KeyConstant.USER_CODE, StoreApplication.userCode);
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && null != likeAdapter) {
            likeAdapter.clean();
            likeAdapter = null;
            for (TimerTask timerTask : timerTasks) {
                timerTask.cancel();
            }
            timerTasks.clear();
        }
    }

    @Override
    protected void onFirstUserVisible() {
        getLikeList();
    }

    protected final static String TAG = LikeFragment.class.getSimpleName();

    @Override
    protected void onUserVisible() {
      /*  if (null != likeAdapter) {
            likeAdapter.clean();
        }*/
        getLikeList();
    }


    @Override
    protected void onUserInvisible() {
    }

    @Override
    protected View getLoadView(View view) {
        return null;
    }
}
