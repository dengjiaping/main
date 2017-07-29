package cn.ngame.store.activity.rank;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.game.GameListBody;
import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.game.GameCommentListClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import cn.ngame.store.R;
import cn.ngame.store.adapter.ProgressBarStateListener;
import cn.ngame.store.adapter.RankingListAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.view.GameLoadProgressBar;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

/**
 * 下载榜
 * Created by gp on 2017/3/22 0022.
 */

public class RankCommentFragment extends BaseSearchFragment implements View.OnClickListener {
    private PullToRefreshListView pullListView;
    /**
     * headerView
     */
    private GameLoadProgressBar day_progress_bar_one, day_progress_bar_two, day_progress_bar_three;
    private SimpleDraweeView sdv_img_1, sdv_img_2, sdv_img_3;
    private TextView tv_rank_name_one, tv_rank_name_two, tv_rank_name_three;
    private TextView tv_download_num_one, tv_download_num_two, tv_download_num_three;

    private Timer timer = new Timer();
    private IFileLoad fileLoad; //文件下载公共类接口

    private RankingListAdapter adapter;

    private PageAction pageAction;
    public static int PAGE_SIZE = 10;
    List<GameRankListBean.DataBean> topList = new ArrayList<>();
    List<GameRankListBean.DataBean> list = new ArrayList<>();
    public static final String ARG_PAGE = "ARG_PAGE";
    private boolean IS_LOADED = false;
    private static int mSerial = 0;
    private int mTabPos = 0;
    private boolean isFirst = true;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (!IS_LOADED) {
                IS_LOADED = true;
                //这里执行加载数据的操作
                getCommentList();
                Log.d("tag", "我是page" + (mTabPos + 1));
            }
            return;
        }
    };

    public RankCommentFragment(int serial) {
        mSerial = serial;
    }
    public void sendMessage(){
        Message message = handler.obtainMessage();
        message.sendToTarget();
    }
    @Override
    protected int getContentViewLayoutID() {
        if (isFirst && mTabPos == mSerial) {
            isFirst = false;
            sendMessage();
        }
        return R.layout.ranking_download_fragment;
    }
    public void setTabPos(int mTabPos) {
        this.mTabPos = mTabPos;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("tag","onDestroyView()方法执行");
    }
    @Override
    protected void initViewsAndEvents(View view) {
        //        typeValue = getArguments().getInt("", 1);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);

        pullListView = (PullToRefreshListView) view.findViewById(R.id.pullListView);

        pullListView.setPullRefreshEnabled(true); //刷新
        pullListView.setPullLoadEnabled(true); //false,不允许上拉加载
        pullListView.setScrollLoadEnabled(false);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullListView.setPullLoadEnabled(true);
                pageAction.setCurrentPage(0);
//                getGameList();
                getCommentList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //少于指定条数不加载
                if (pageAction.getTotal() < pageAction.getPageSize()) {
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                    return;
                }
                if (pageAction.getCurrentPage() * pageAction.getPageSize() < pageAction.getTotal()) {
                    pageAction.setCurrentPage(pageAction.getCurrentPage() == 0 ? pageAction.getCurrentPage() + 2 : pageAction
                            .getCurrentPage() + 1);
//                    getGameList();
                    getCommentList();
                } else {
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                }
            }
        });
        //点击事件
        pullListView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                    intent.putExtra("id", list.get(position - 1).getId());
                    startActivity(intent);
                }
            }
        });
        //添加头布局
        View headView = View.inflate(getActivity(), R.layout.ranking_header_view, null);
        initHeadView(headView);
        //头布局放入listView中
        if (pullListView.getRefreshableView().getHeaderViewsCount() == 0) {
            pullListView.getRefreshableView().addHeaderView(headView);
        }

    }

    private void initHeadView(View view) {
        sdv_img_1 = (SimpleDraweeView) view.findViewById(R.id.sdv_img_1);
        sdv_img_2 = (SimpleDraweeView) view.findViewById(R.id.sdv_img_2);
        sdv_img_3 = (SimpleDraweeView) view.findViewById(R.id.sdv_img_3);
        tv_rank_name_one = (TextView) view.findViewById(R.id.tv_rank_name_one);
        tv_rank_name_two = (TextView) view.findViewById(R.id.tv_rank_name_two);
        tv_rank_name_three = (TextView) view.findViewById(R.id.tv_rank_name_three);
        tv_download_num_one = (TextView) view.findViewById(R.id.tv_download_num_one);
        tv_download_num_two = (TextView) view.findViewById(R.id.tv_download_num_two);
        tv_download_num_three = (TextView) view.findViewById(R.id.tv_download_num_three);
        day_progress_bar_one = (GameLoadProgressBar) view.findViewById(R.id.day_progress_bar_one);
        day_progress_bar_two = (GameLoadProgressBar) view.findViewById(R.id.day_progress_bar_two);
        day_progress_bar_three = (GameLoadProgressBar) view.findViewById(R.id.day_progress_bar_three);

        sdv_img_1.setOnClickListener(this);
        sdv_img_2.setOnClickListener(this);
        sdv_img_3.setOnClickListener(this);
    }

    /**
     * 好评榜列表
     */
    private void getCommentList() {
        GameListBody bodyBean = new GameListBody();
        bodyBean.setPageIndex(pageAction.getCurrentPage());
        bodyBean.setPageSize(PAGE_SIZE);
        new GameCommentListClient(getActivity(), bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<GameRankListBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(GameRankListBean result) {
                        if (result != null && result.getCode() == 0) {
                            listData(result);
                        } else {
//                            ToastUtil.show(getActivity(), result.getMsg());
                        }
                    }
                });
    }

    public void listData(GameRankListBean result) {
        if (result.getData() == null) {
            return;
        }
        if (pageAction.getCurrentPage() == 0) {
            this.list.clear(); //清除数据
            this.topList.clear();
            if (result.getData() == null || result.getData().size() == 0) {
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                return;
            }
        }
        if (result.getData().size() > 0) {
            pageAction.setTotal(result.getTotals());
            this.list.addAll(result.getData());
            this.topList.addAll(result.getData());
        }
        if (result.getData().size() > 0 && pageAction.getCurrentPage() == 0) {
            this.list.clear(); //清除数据
            this.topList.clear();
            pageAction.setTotal(result.getTotals());
            this.list.addAll(result.getData());
            this.topList.addAll(result.getData());
            setHeaderInfo(list);
            list.remove(0);
            list.remove(0);
            list.remove(0);
        }
        if (adapter == null) {
            adapter = new RankingListAdapter(getActivity(), getSupportFragmentManager(), list, 0);
            pullListView.getRefreshableView().setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
        //设置下位列表
        if ((list.size() == 0 && pageAction.getTotal() == 0) || list.size() >= pageAction.getTotal()) {
            pullListView.setPullLoadEnabled(false);
        } else {
            pullListView.setPullLoadEnabled(true);
        }
        if (pageAction.getCurrentPage() > 0 && result.getData().size() > 0) { //设置上拉刷新后停留的地方
            int index = pullListView.getRefreshableView().getFirstVisiblePosition();
            View v = pullListView.getRefreshableView().getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - v.getHeight());
            pullListView.getRefreshableView().setSelectionFromTop(index, top);
        }
        pullListView.onPullUpRefreshComplete();
        pullListView.onPullDownRefreshComplete();
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
    }

    //设置头部数据
    public void setHeaderInfo(List<GameRankListBean.DataBean> list) {
        sdv_img_1.setImageURI(list.get(0).getGameLogo());
        sdv_img_2.setImageURI(list.get(1).getGameLogo());
        sdv_img_3.setImageURI(list.get(2).getGameLogo());
        tv_rank_name_one.setText(list.get(0).getGameName());
        tv_rank_name_two.setText(list.get(1).getGameName());
        tv_rank_name_three.setText(list.get(2).getGameName());
        tv_download_num_one.setText(list.get(0).getDownloadCount() + "次下载");
        tv_download_num_two.setText(list.get(1).getDownloadCount() + "次下载");
        tv_download_num_three.setText(list.get(2).getDownloadCount() + "次下载");
        getThreeGameinfo(list, day_progress_bar_one, 0);
        getThreeGameinfo(list, day_progress_bar_two, 1);
        getThreeGameinfo(list, day_progress_bar_three, 2);
    }

    private void getThreeGameinfo(List<GameRankListBean.DataBean> list, final GameLoadProgressBar view, int i) {
        fileLoad = FileLoadManager.getInstance(getActivity());
        GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(list.get(i).getFilename(), list.get(i).getGameLink(), list
                .get(i).getPackages(), ConvUtil.NI(list.get(i).getVersionCode()));
        view.setLoadState(fileStatus);

        //设置进度条状态
        view.setLoadState(fileLoad.getGameFileLoadStatus(list.get(i).getFilename(), list.get(i).getGameLink(), list.get(i)
                .getPackages(), ConvUtil.NI(list.get(i).getVersionCode())));
        //必须设置，否则点击进度条后无法进行响应操作
        FileLoadInfo fileLoadInfo = new FileLoadInfo(list.get(i).getFilename(), list.get(i).getGameLink(), list.get(i).getMd5()
                , list.get(i).getVersionCode(), list.get(i).getGameName(), list.get(i).getGameLogo(), list.get(i).getId(),
                FileLoadInfo.TYPE_GAME);
        fileLoadInfo.setPackageName(list.get(i).getPackages());
        view.setFileLoadInfo(fileLoadInfo);
        view.setOnStateChangeListener(new ProgressBarStateListener(getActivity(), getSupportFragmentManager()));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.toggle();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sdv_img_1:
                Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                intent.putExtra("id", topList.get(0).getId());
                startActivity(intent);
                break;
            case R.id.sdv_img_2:
                Intent i2 = new Intent(getActivity(), GameDetailActivity.class);
                i2.putExtra("id", topList.get(1).getId());
                android.util.Log.d("777", "游戏id" + topList.get(1).getId());
                startActivity(i2);
                break;
            case R.id.sdv_img_3:
                Intent i3 = new Intent(getActivity(), GameDetailActivity.class);
                i3.putExtra("id", topList.get(2).getId());
                startActivity(i3);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        list = null;

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
