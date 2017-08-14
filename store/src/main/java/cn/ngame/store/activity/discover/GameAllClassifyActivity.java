package cn.ngame.store.activity.discover;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.jzt.hol.android.jkda.sdk.bean.classification.ClassifiHomeBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBodyBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.Classification.ClassifiHomeClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.ClassifiDongzuoAdapter;
import cn.ngame.store.adapter.ClassifiSaicheAdapter;
import cn.ngame.store.adapter.ClassifiTiyuAdapter;
import cn.ngame.store.adapter.ClassifiXiuxianAdapter;
import cn.ngame.store.adapter.HomeRaiderAdapter;
import cn.ngame.store.adapter.GameListAdapter;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.game.view.GameListActivity;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

import static cn.ngame.store.core.utils.ImageUtil.setGridViewHeight;

/**
 * 分类
 * Created by zeng on 2016/6/16.
 */
public class GameAllClassifyActivity extends BaseFgActivity {

    public static final String TAG = GameAllClassifyActivity.class.getSimpleName();
    private PullToRefreshListView pullListView;
    private LoadStateView loadStateView;
    private GameListAdapter adapter;
    private List<GameInfo> gameInfoList;

    private PageAction pageAction;
    public static int PAGE_SIZE = 20;
    private long categoryId;
    private GameAllClassifyActivity content;
    private GridView gridView_remen, gridView_qiangzhan, gridView_maoxian, gridView_jiaose;

    List<ClassifiHomeBean.DataBean.OnlineListBean> remenList = new ArrayList<>();
    ClassifiQiangzhanAdapter qiangzhanAdapter;
    List<ClassifiHomeBean.DataBean.GunFireListBean> qiangzhanList = new ArrayList<>();
    ClassifiMaoxianAdapter maoxianAdapter;
    List<ClassifiHomeBean.DataBean.ParkourListBean> maoxianList = new ArrayList<>();
    ClassifiDongzuoAdapter dongzuoAdapter;
    List<ClassifiHomeBean.DataBean.CombatListBean> dongzuoList = new ArrayList<>();
    ClassifyRoleAdapter jiaoseAdapter;
    List<ClassifiHomeBean.DataBean.RoleListBean> jiaoseList = new ArrayList<>();
    ClassifiXiuxianAdapter xiuxianAdapter;
    List<ClassifiHomeBean.DataBean.PuzzleListBean> xiuxianList = new ArrayList<>();
    ClassifiSaicheAdapter saicheAdapter;
    List<ClassifiHomeBean.DataBean.RaceListBean> saicheList = new ArrayList<>();
    ClassifiTiyuAdapter tiyuAdapter;
    List<ClassifiHomeBean.DataBean.SportListBean> tiyuList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_all_classify);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        content = GameAllClassifyActivity.this;

        findViewById(R.id.center_tv).setVisibility(View.GONE);
        Button leftBt = (Button) findViewById(R.id.left_bt);
        leftBt.setText("分类");
        leftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.finish();
            }
        });
        pullListView = (PullToRefreshListView) findViewById(R.id.pullListView);
        pullListView.setPullLoadEnabled(false); //false,不允许上拉加载
        pullListView.setScrollLoadEnabled(false);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getGameList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        //添加头布局
        View headView = View.inflate(content, R.layout.all_classify_header_view, null);
        initHeadView(headView);
        //头布局放入listView中
        if (pullListView.getRefreshableView().getHeaderViewsCount() == 0) {
            pullListView.getRefreshableView().addHeaderView(headView);
        }
        List<String> list = new ArrayList<>();
        list.add(new String("测试"));
        list.add(new String("测试2"));
        HomeRaiderAdapter adapter = new HomeRaiderAdapter(content, list, "0");
        pullListView.getRefreshableView().setAdapter(adapter);

        getGameList();
    }

    private void initHeadView(View headView) {
        gridView_remen = (GridView) headView.findViewById(R.id.gridView_remen);
        gridView_qiangzhan = (GridView) headView.findViewById(R.id.gridView_qiangzhan);
        gridView_maoxian = (GridView) headView.findViewById(R.id.gridView_maoxian);
        gridView_jiaose = (GridView) headView.findViewById(R.id.gridView_dongzuo);
        clickGradView(gridView_remen, 1);
        clickGradView(gridView_qiangzhan, 2);
        clickGradView(gridView_maoxian, 3);
        clickGradView(gridView_jiaose, 4);
    }

    //条目点击事件 传游戏id，不传lab查询id
    public void clickGradView(GridView gridView, final int i) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent classifyIntent = new Intent(content, GameListActivity.class);
                switch (i) {
                    case 1:
                        if (position < remenList.size()) {
                            int typeId = remenList.get(position).getId();
                            String typeName = remenList.get(position).getTypeName();
                            classifyIntent.putExtra(KeyConstant.category_Id, typeId + "");//原生手柄 id 367
                            classifyIntent.putExtra(KeyConstant.TITLE, typeName);
                            content.startActivity(classifyIntent);
                        }
                        break;
                    case 2:
                        if (position < qiangzhanList.size()) {
                            int typeId = qiangzhanList.get(position).getId();
                            String typeName = qiangzhanList.get(position).getTypeName();
                            classifyIntent.putExtra(KeyConstant.category_Id, typeId + "");//原生手柄 id 367
                            classifyIntent.putExtra(KeyConstant.TITLE, typeName);
                            content.startActivity(classifyIntent);
                        }
                        break;
                    case 3:
                        if (position < maoxianList.size()) {
                            int typeId = maoxianList.get(position).getId();
                            String typeName = maoxianList.get(position).getTypeName();
                            classifyIntent.putExtra(KeyConstant.category_Id, typeId + "");//原生手柄 id 367
                            classifyIntent.putExtra(KeyConstant.TITLE, typeName);
                            content.startActivity(classifyIntent);
                        }
                        break;
                    case 4:
                        if (position < jiaoseList.size()) {
                            ClassifiHomeBean.DataBean.RoleListBean roleListBean = jiaoseList.get(position);
                            int typeId = roleListBean.getId();
                            String typeName = roleListBean.getTypeName();
                            classifyIntent.putExtra(KeyConstant.category_Id, typeId + "");//原生手柄 id 367
                            classifyIntent.putExtra(KeyConstant.TITLE, typeName);
                            content.startActivity(classifyIntent);
                        }
                        break;
                }
            }
        });
    }

    /**
     * 获取制定分类下的游戏列表
     */
    private static final int SUB_TYPE_ID = 21;

    private void getGameList() {
        YunduanBodyBean bodyBean = new YunduanBodyBean();
        bodyBean.setMarkId(SUB_TYPE_ID);
        new ClassifiHomeClient(content, bodyBean).observable()
                .subscribe(new ObserverWrapper<ClassifiHomeBean>() {
                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ClassifiHomeBean result) {

                        if (result != null && result.getCode() == 0) {
                            listData(result);//更新数据
                        } else {
                        }
                    }
                });
    }

    ClassifiHotAdapter remenAdapter;

    //更新适配器数据
    private void listData(ClassifiHomeBean result) {
        if (result.getData() == null) {
            return;
        }
        if (result.getData().getOnlineList().size() > 0) {
            remenList.clear();
            remenList.addAll(result.getData().getOnlineList());
            remenAdapter = new ClassifiHotAdapter(content, remenList);
            gridView_remen.setAdapter(remenAdapter);
            setGridViewHeight(gridView_remen);
        }
        if (result.getData().getGunFireList().size() > 0) {
            qiangzhanList.clear();
            qiangzhanList.addAll(result.getData().getGunFireList());
            qiangzhanAdapter = new ClassifiQiangzhanAdapter(content, qiangzhanList);
            gridView_qiangzhan.setAdapter(qiangzhanAdapter);
            setGridViewHeight(gridView_qiangzhan);
        }
        if (result.getData().getParkourList().size() > 0) {
            maoxianList.clear();
            maoxianList.addAll(result.getData().getParkourList());
            maoxianAdapter = new ClassifiMaoxianAdapter(content, maoxianList);
            gridView_maoxian.setAdapter(maoxianAdapter);
            setGridViewHeight(gridView_maoxian);
        }
        if (result.getData().getCombatList().size() > 0) {
            jiaoseList.clear();
            jiaoseList.addAll(result.getData().getRoleList());
            jiaoseAdapter = new ClassifyRoleAdapter(content, jiaoseList);
            gridView_jiaose.setAdapter(jiaoseAdapter);
            setGridViewHeight(gridView_jiaose);
        }
        pullListView.onPullUpRefreshComplete();
        pullListView.onPullDownRefreshComplete();
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
    }

}
