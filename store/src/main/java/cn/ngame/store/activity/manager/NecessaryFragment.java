package cn.ngame.store.activity.manager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.adapter.NeccssaryFragmentAdapter;
import cn.ngame.store.adapter.RankingListAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.NecessaryItemData;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.view.ActionItem;
import cn.ngame.store.view.QuickAction;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


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
//    private GameUpdate2Adapter loadIngLvAdapter;
    /**
     * 当前点击的列表 1.下载列表 2.完成列表
     */
    private GameRankListBean gameInfoBean;
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
    public StickyListHeadersListView mStickyLV;

    private NeccssaryFragmentAdapter langyaAdapter;

    private List<NecessaryItemData> mLangyaDatas;

    @Override
    protected void initViewsAndEvents(View view) {
        type = getArguments().getString("type");
        content = getActivity();
        mStickyLV = (StickyListHeadersListView) view.findViewById(R.id.sticky_list_view);
        mLangyaDatas = new ArrayList<>();
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);


        initDatas();

        mStickyLV.setOnItemClickListener(new OnItemClick());
        mStickyLV.setOnItemLongClickListener(new OnPlanItemLongClick());
        mStickyLV.setDividerHeight(0);
        langyaAdapter = new NeccssaryFragmentAdapter(getActivity(), getSupportFragmentManager(), timerTasks);
        mStickyLV.setAdapter(langyaAdapter);

        langyaAdapter.setDate(mLangyaDatas);
        //initPop();
    }

    private class OnItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String itemPosition = mLangyaDatas.get(position).getItemPosition();
            String getItemTitle = mLangyaDatas.get(position).getItemTitle();
            Log.d("5555",getItemTitle+",点击"+itemPosition);
        }
    }

    private class OnPlanItemLongClick implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            NecessaryItemData oLangyaSimple = mLangyaDatas.get(position);
            mLangyaDatas.remove(oLangyaSimple);
            if (langyaAdapter != null && mLangyaDatas != null) {
                langyaAdapter.setDate(mLangyaDatas);
                langyaAdapter.notifyDataSetChanged();
            }
            return true;
        }
    }

    /**
     * 有个地方需要着重说明，那就是在初始化数据时，数据必须proj_id必须按分组排列，
     * 即，不要将proj_id不同的数据      参差着放在集合中，
     * 否则容易造成列表显示多组相同组名的数据。
     */
    private void initDatas() {
        mLangyaDatas.add(new NecessaryItemData("1", "腾讯", "52", "腾讯助手", "腾讯腾讯腾讯腾讯腾讯腾讯腾讯"));
        mLangyaDatas.add(new NecessaryItemData("1", "腾讯", "22", "腾讯助手", "腾讯腾讯腾讯腾讯腾讯腾讯腾讯"));
        mLangyaDatas.add(new NecessaryItemData("1", "腾讯", "31", "腾讯助手", "腾讯腾讯腾讯腾讯腾讯腾讯腾讯"));
        mLangyaDatas.add(new NecessaryItemData("1", "腾讯", "41", "腾讯助手", "腾讯腾讯腾讯腾讯腾讯腾讯腾讯"));
        mLangyaDatas.add(new NecessaryItemData("1", "腾讯", "45", "腾讯助手", "腾讯腾讯腾讯腾讯腾讯腾讯腾讯"));

        mLangyaDatas.add(new NecessaryItemData("2", "谷歌", "54", "谷歌助手", "谷歌谷歌谷歌谷歌谷歌谷歌谷歌"));
        mLangyaDatas.add(new NecessaryItemData("2", "谷歌", "15", "谷歌助手", "谷歌谷歌谷歌谷歌谷歌谷歌谷歌"));
        mLangyaDatas.add(new NecessaryItemData("2", "谷歌", "8", "谷歌助手", "谷歌谷歌谷歌谷歌谷歌谷歌谷歌"));
        mLangyaDatas.add(new NecessaryItemData("2", "谷歌", "9", "谷歌助手", "谷歌谷歌谷歌谷歌谷歌谷歌谷歌"));
        mLangyaDatas.add(new NecessaryItemData("2", "谷歌", "10", "谷歌助手", "谷歌谷歌谷歌谷歌谷歌谷歌谷歌"));


        mLangyaDatas.add(new NecessaryItemData("5", "百度", "11", "百度助手", "百度助手百度助手百度助手百度助手百度助手"));
        mLangyaDatas.add(new NecessaryItemData("5", "百度", "12", "百度助手", "百度助手百度助手百度助手百度助手百度助手"));
        mLangyaDatas.add(new NecessaryItemData("5", "百度", "13", "百度助手", "百度助手百度助手百度助手百度助手"));
        mLangyaDatas.add(new NecessaryItemData("5", "百度", "14", "百度助手", "百度助手百度助手百度助手百度助手百度助手"));
        mLangyaDatas.add(new NecessaryItemData("5", "百度", "15", "百度助手", "百度助手百度助手百度助手百度助手百度助手"));
        mLangyaDatas.add(new NecessaryItemData("5", "百度", "16", "百度助手", "百度助手百度助手百度助手百度助手百度助手"));

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
                    String currentGameId = langyaAdapter.getItemGameId();

                }
                //取消弹出框
                mItemClickQuickAction.dismiss();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && null != langyaAdapter) {
            langyaAdapter.clean();
            langyaAdapter = null;
            for (TimerTask timerTask : timerTasks) {
                timerTask.cancel();
            }
            timerTasks.clear();
        }
    }


    @Override
    protected void onFirstUserVisible() {
        //getLikeList();
    }

    protected final static String TAG = NecessaryFragment.class.getSimpleName();

    @Override
    protected void onUserVisible() {
        //getLikeList();
    }


    @Override
    protected void onUserInvisible() {
    }

    @Override
    protected View getLoadView(View view) {
        return null;
    }
}