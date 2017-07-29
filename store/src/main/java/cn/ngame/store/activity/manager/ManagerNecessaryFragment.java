package cn.ngame.store.activity.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.adapter.RankingListAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.view.QuickAction;

/**
 * 下载更新fragment (懒加载-当滑动到当前fragment时，才去加载。而不是进入到activity时，加载所有fragment)
 * Created by gp on 2017/3/3 0003.
 */

public class ManagerNecessaryFragment extends BaseSearchFragment {

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
    private int itemType;
    private int itemPosition;
    GameRankListBean gameInfoBean;

    public static ManagerNecessaryFragment newInstance(String type, int bean) {
        ManagerNecessaryFragment fragment = new ManagerNecessaryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putSerializable("typeValue", bean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.update_fragment;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        type = getArguments().getString("type");

        listView = (ListView) view.findViewById(R.id.listView);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        if (gameInfoBean == null) {
            return;
        } else {
            list = gameInfoBean.getData();
        }
        adapter = new RankingListAdapter(getActivity(), getSupportFragmentManager(), list, 1);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                intent.putExtra("id", gameInfoBean.getData().get(position).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadView(View view) {
        return null;
    }
}
