package cn.ngame.store.activity.manager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.jzt.hol.android.jkda.sdk.bean.game.GameListBody;
import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.game.GameCommentListClient;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.adapter.NeccssaryFragmentAdapter;
import cn.ngame.store.adapter.RankingListAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.ActionItem;
import cn.ngame.store.view.QuickAction;

/**
 * 下载更新fragment (懒加载-当滑动到当前fragment时，才去加载。而不是进入到activity时，加载所有fragment)
 * Created by gp on 2017/3/3 0003.
 */

public class NecessaryFragment extends BaseSearchFragment {

    ListView listView;
    private PageAction pageAction;
    public static int PAGE_SIZE = 10;
    private int typeValue;
    private String type;
    protected QuickAction mItemClickQuickAction;
    private IFileLoad fileLoad;
    private RankingListAdapter adapter;
    List<GameRankListBean.DataBean> list = new ArrayList<>();
//    private GameUpdate2Adapter loadIngLvAdapter;
    /**
     * 当前点击的列表 1.下载列表 2.完成列表
     */
    private GameRankListBean gameInfoBean;
    private NeccssaryFragmentAdapter neccssaryAdapter;
    private FragmentActivity content;

    public static NecessaryFragment newInstance(String type, int bean) {
        NecessaryFragment fragment = new NecessaryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putSerializable("typeValue", bean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_necessary;
    }

    private List<TimerTask> timerTasks = new ArrayList<>();

    @Override
    protected void initViewsAndEvents(View view) {
        type = getArguments().getString("type");
        content = getActivity();
        listView = (ListView) view.findViewById(R.id.listView);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        neccssaryAdapter = new NeccssaryFragmentAdapter(getActivity(), getSupportFragmentManager(), mItemClickQuickAction,
                timerTasks);
        listView.setAdapter(neccssaryAdapter);

        initPop();
    }

    private void getLikeList() {
        //tabPosition :0=全部   1=手柄   2=破解   3=汉化  4=特色
        GameListBody bodyBean = new GameListBody();
        bodyBean.setPageIndex(pageAction.getCurrentPage());
        bodyBean.setPageSize(PAGE_SIZE);
        new GameCommentListClient(content, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<GameRankListBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                        ToastUtil.show(content, getString(R.string.pull_to_refresh_network_error));
                    }

                    @Override
                    public void onNext(GameRankListBean result) {
                        if (result != null && result.getCode() == 0) {
                            List<GameRankListBean.DataBean> data = result.getData();
                            if (null != neccssaryAdapter) {
                                neccssaryAdapter.setDate(data);
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
        mItemClickQuickAction = new QuickAction(getActivity(), QuickAction.VERTICAL);
        ActionItem pointItem = new ActionItem(1, "不再喜欢", null);
        mItemClickQuickAction.addActionItem(pointItem);

        mItemClickQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                if (pos == 0) {
                    //获取gameId  传给服务器 不再喜欢
                    String currentGameId = neccssaryAdapter.getItemGameId();

                }
                //取消弹出框
                mItemClickQuickAction.dismiss();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && null != neccssaryAdapter) {
            neccssaryAdapter.clean();
            neccssaryAdapter = null;
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

    protected final static String TAG = NecessaryFragment.class.getSimpleName();

    @Override
    protected void onUserVisible() {
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