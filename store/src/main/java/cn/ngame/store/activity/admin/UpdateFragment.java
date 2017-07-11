package cn.ngame.store.activity.admin;

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

public class UpdateFragment extends BaseSearchFragment {

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

    public static UpdateFragment newInstance(String type, GameRankListBean bean) {
        UpdateFragment fragment = new UpdateFragment();
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
        gameInfoBean = (GameRankListBean) getArguments().getSerializable("typeValue");

        listView = (ListView) view.findViewById(R.id.listView);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
//        initListView();
//        initPop(typeValue);
        if (gameInfoBean != null) {
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

//    public void initListView() {
//        listView.setOnItemClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                itemType = 3;
//                itemPosition = v.getId();
//                //显示弹出框消失
//                mItemClickQuickAction.show(v);
//            }
//        });
//        loadIngLvAdapter = new GameUpdate2Adapter(getActivity(), getSupportFragmentManager());
//        listView.setAdapter(loadIngLvAdapter);
//        fileLoad = FileLoadManager.getInstance(getActivity());
//}

    @Override
    public void onResume() {
        super.onResume();
//        List<FileLoadInfo> loadingList = fileLoad.getUpdateFileInfo();
//        loadIngLvAdapter.setDate(gameInfoBean.getData());
//        listView.notifyDataSetChanged();
    }

//    private void initPop(final int typeValue) {
//        // 设置Action
//        mItemClickQuickAction = new QuickAction(getActivity(), QuickAction.VERTICAL);
//        ActionItem pointItem = new ActionItem(1, "删除任务", null);
//        mItemClickQuickAction.addActionItem(pointItem);
//
//        mItemClickQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
//            @Override
//            public void onItemClick(QuickAction source, int pos, int actionId) {
//                if (pos == 0) {
//                    //删除文件下载任务
//                    FileLoadInfo fileInfo = null;
//                    fileInfo = (FileLoadInfo) loadIngLvAdapter.getItem(itemPosition);
//                    //删除下载任务
//                    fileLoad.delete(fileInfo.getUrl());
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    List<FileLoadInfo> loadingList = fileLoad.getUpdateFileInfo();
//                    loadIngLvAdapter.setDate(loadingList);
//                    listView.notifyDataSetChanged();
//                }
//                //取消弹出框
//                mItemClickQuickAction.dismiss();
//            }
//        });
//    }

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
