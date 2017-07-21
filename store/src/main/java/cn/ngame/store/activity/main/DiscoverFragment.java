package cn.ngame.store.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jzt.hol.android.jkda.sdk.bean.classification.ClassifiHomeBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBodyBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.Classification.ClassifiHomeClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.adapter.ClassifiDongzuoAdapter;
import cn.ngame.store.adapter.ClassifiJiaoseAdapter;
import cn.ngame.store.adapter.ClassifiMaoxianAdapter;
import cn.ngame.store.adapter.ClassifiQiangzhanAdapter;
import cn.ngame.store.adapter.ClassifiSaicheAdapter;
import cn.ngame.store.adapter.ClassifiTiyuAdapter;
import cn.ngame.store.adapter.ClassifiXiuxianAdapter;
import cn.ngame.store.adapter.DiscoverClassifyTopAdapter;
import cn.ngame.store.adapter.DiscoverTvIvAdapter;
import cn.ngame.store.adapter.HomeRaiderAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.HotInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.game.view.SBGameActivity;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.video.view.VideoDetailActivity;
import cn.ngame.store.view.BannerView;
import cn.ngame.store.view.PicassoImageView;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

/**
 * 分类
 * Created by gp on 2017/3/14 0014.
 */

public class DiscoverFragment extends BaseSearchFragment implements View.OnClickListener {

    private static final int SUB_TYPE_ID_VR = 13;
    private static final int SUB_TYPE_ID_STADN = 14;
    private static final int SUB_TYPE_ID_BOY = 15;
    private static final int SUB_TYPE_ID_GIRL = 16;
    private static final int SUB_TYPE_ID = 21;
    private static final String TAG = "111";
    private FragmentActivity context;
    /**
     * 顶部栏
     */
    private PullToRefreshListView pullListView;
    /**
     * headerView
     */
    private RecyclerView mRVClassifyAll;
    private GridView gridView_maoxian;
    private GridView gridView_dongzuo;
    private GridView gridView_jiaose;
    private GridView gridView_xiuxian;
    private GridView gridView_saiche;
    private GridView gridView_tiyu;

    DiscoverClassifyTopAdapter remenAdapter;
    List<ClassifiHomeBean.DataBean.OnlineListBean> remenList = new ArrayList<>();
    ClassifiQiangzhanAdapter qiangzhanAdapter;
    List<ClassifiHomeBean.DataBean.GunFireListBean> qiangzhanList = new ArrayList<>();
    ClassifiMaoxianAdapter maoxianAdapter;
    List<ClassifiHomeBean.DataBean.ParkourListBean> maoxianList = new ArrayList<>();
    ClassifiDongzuoAdapter dongzuoAdapter;
    List<ClassifiHomeBean.DataBean.CombatListBean> dongzuoList = new ArrayList<>();
    ClassifiJiaoseAdapter jiaoseAdapter;
    List<ClassifiHomeBean.DataBean.RoleListBean> jiaoseList = new ArrayList<>();
    ClassifiXiuxianAdapter xiuxianAdapter;
    List<ClassifiHomeBean.DataBean.PuzzleListBean> xiuxianList = new ArrayList<>();
    ClassifiSaicheAdapter saicheAdapter;
    List<ClassifiHomeBean.DataBean.RaceListBean> saicheList = new ArrayList<>();
    ClassifiTiyuAdapter tiyuAdapter;
    List<ClassifiHomeBean.DataBean.SportListBean> tiyuList = new ArrayList<>();
    private BannerView bannerView;
    private List<String> classifyList = new ArrayList<>(Arrays.asList("角色", "动作", "原生", "策略", "模拟", "VR", "枪战", "体育", "格斗"));
    private List<Integer> urlList = new ArrayList();
    private DiscoverTvIvAdapter mTvIvAdapter;
    private RecyclerView mTvIv_everyday_discover_Rv;

    public DiscoverFragment() {
        android.util.Log.d(TAG, "DiscoverFragment: ()");
    }

    public static DiscoverFragment newInstance(String arg) {
        android.util.Log.d(TAG, "discover newInstance: ()");
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle bundle = new Bundle();
        bundle.putString("", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void initViewsAndEvents(View view) {//2
        init(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {//3
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        View headView = View.inflate(this.getActivity(), R.layout.discover_header_view, null);//头部
        initHeadView(headView);
        if (pullListView.getRefreshableView().getHeaderViewsCount() == 0) {
            pullListView.getRefreshableView().addHeaderView(headView);
        }
    }

    //todo 头部
    private void initHeadView(View headView) {
        bannerView = (BannerView) headView.findViewById(R.id.banner_view);


        //获取RecyclerView实例
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(
                this.getActivity(), LinearLayoutManager.HORIZONTAL, false);

        mRVClassifyAll = (RecyclerView) headView.findViewById(R.id.discover_head_rv_classify);//条目
        mRVClassifyAll.setLayoutManager(linearLayoutManager1);
        remenAdapter = new DiscoverClassifyTopAdapter(this.getActivity(), classifyList);
        mRVClassifyAll.setAdapter(remenAdapter);
        onRecyclerViewItemClick(mRVClassifyAll, 1);

        mTvIv_everyday_discover_Rv = (RecyclerView) headView.findViewById(R.id.everyday_discover_recyclerview);

        for (int i = 0; i < 10; i++) {
            urlList.add(R.drawable.ic_def_logo_480_228);
        }
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(
                this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mTvIv_everyday_discover_Rv.setLayoutManager(linearLayoutManager2);
        mTvIvAdapter = new DiscoverTvIvAdapter(this.getActivity(), urlList);
        mTvIv_everyday_discover_Rv.setAdapter(mTvIvAdapter);

        gridView_maoxian = (GridView) headView.findViewById(R.id.gridView_maoxian);
        gridView_dongzuo = (GridView) headView.findViewById(R.id.gridView_dongzuo);
        gridView_jiaose = (GridView) headView.findViewById(R.id.gridView_jiaose);
        gridView_xiuxian = (GridView) headView.findViewById(R.id.gridView_xiuxian);
        gridView_saiche = (GridView) headView.findViewById(R.id.gridView_saiche);
        gridView_tiyu = (GridView) headView.findViewById(R.id.gridView_tiyu);

        /* clickGradView(mRvEverydayDiscover, 2);
        clickGradView(gridView_maoxian, 3);
        clickGradView(gridView_dongzuo, 4);
        clickGradView(gridView_jiaose, 5);
        clickGradView(gridView_xiuxian, 6);
        clickGradView(gridView_saiche, 7);
        clickGradView(gridView_tiyu, 8);*/
    }

    private void init(View view) {
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pullListView);
        pullListView.setPullLoadEnabled(false); //false,不允许上拉加载
        pullListView.setScrollLoadEnabled(false);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //getGameList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });


        //onRecyclerViewItemClick(mRVClassifyAll, 1);
        List<String> list = new ArrayList<>();
        list.add(new String("测试"));
        list.add(new String("测试2"));
        HomeRaiderAdapter adapter = new HomeRaiderAdapter(context, list, "0");
        pullListView.getRefreshableView().setAdapter(adapter);

        //getGameList();
        getBannerData();
        ClassifiHomeBean result = new ClassifiHomeBean();

        //listData(result);
    }

    /**
     * 获取轮播图片数据
     */
    private void getBannerData() {
        String url = Constant.WEB_SITE + Constant.URL_BANNER;
        Response.Listener<JsonResult<List<HotInfo>>> successListener = new Response.Listener<JsonResult<List<HotInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<HotInfo>> result) {

                if (result == null) {
//                    Toast.makeText(getActivity(), "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.code == 0) {
                    List<ImageView> list = createBannerView(result.data);
                    bannerView.setData(list);

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

        Request<JsonResult<List<HotInfo>>> request = new GsonRequest<JsonResult<List<HotInfo>>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<List<HotInfo>>>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("type", String.valueOf(42));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    /**
     * 创建轮播视图
     */
    private List<ImageView> createBannerView(List<HotInfo> hotInfoList) {

        if (hotInfoList == null || hotInfoList.size() <= 0) {
            return null;
        }

        ArrayList<ImageView> list = new ArrayList<>();
        for (int i = 0; i < hotInfoList.size(); i++) {

            final HotInfo info = hotInfoList.get(i);
            PicassoImageView img = new PicassoImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                    .LayoutParams.MATCH_PARENT);
            img.setLayoutParams(params);
            img.setId((int) info.id);
            img.setTag(info.advImageLink);

            img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.type == 1) {
                        Intent intent = new Intent(context, GameDetailActivity.class);
                        intent.putExtra("id", info.gameId);
                        startActivity(intent);
                    } else if (info.type == 2) {
                        Intent intent = new Intent(context, VideoDetailActivity.class);
                        intent.putExtra("id", info.videoId);
                        startActivity(intent);
                    }
                }
            });
            list.add(img);
        }
        return list;
    }

    private void onRecyclerViewItemClick(RecyclerView recyclerView, int i) {
        remenAdapter.setmOnItemClickListener(new DiscoverClassifyTopAdapter.OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position, String text) {
                ToastUtil.show(context, text);
            }
        });
    }

    //传游戏列表id，不传lab查询id
    public void clickGradView(GridView gridView, final int i) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(context, SBGameActivity.class);
                switch (i) {
                    case 1:
                        if (position < remenList.size()) {
                            intent1.putExtra("tagPosition", remenList.get(position).getId());
                            intent1.putExtra("title", "热门网游");
                            intent1.putExtra("labName", remenList.get(position).getTypeName());
                            startActivity(intent1);
                        }
                        break;
                    case 2:
                        if (position < qiangzhanList.size()) {
                            intent1.putExtra("tagPosition", qiangzhanList.get(position).getId());
                            intent1.putExtra("title", "枪战射击");
                            intent1.putExtra("labName", qiangzhanList.get(position).getTypeName());
                            startActivity(intent1);
                        }
                        break;
                    case 3:
                        if (position < maoxianList.size()) {
                            intent1.putExtra("tagPosition", maoxianList.get(position).getId());
                            intent1.putExtra("title", "冒险酷跑");
                            intent1.putExtra("labName", maoxianList.get(position).getTypeName());
                            startActivity(intent1);
                        }
                        break;
                    case 4:
                        if (position < dongzuoList.size()) {
                            intent1.putExtra("tagPosition", dongzuoList.get(position).getId());
                            intent1.putExtra("title", "动作格斗");
                            intent1.putExtra("labName", dongzuoList.get(position).getTypeName());
                            startActivity(intent1);
                        }
                        break;
                    case 5:
                        if (position < jiaoseList.size()) {
                            // TODO: 2017/7/20 0020
                            intent1.putExtra("tagPosition", jiaoseList.get(position).getId());
                            intent1.putExtra("title", "角色扮演");
                            intent1.putExtra("labName", jiaoseList.get(position).getTypeName());
                            startActivity(intent1);
                        }
                        break;
                    case 6:
                        if (position < xiuxianList.size()) {
                            intent1.putExtra("tagPosition", xiuxianList.get(position).getId());
                            intent1.putExtra("title", "休闲益智");
                            intent1.putExtra("labName", xiuxianList.get(position).getTypeName());
                            startActivity(intent1);
                        }
                        break;
                    case 7:
                        if (position < saicheList.size()) {
                            intent1.putExtra("tagPosition", saicheList.get(position).getId());
                            intent1.putExtra("title", "赛车竞赛");
                            intent1.putExtra("labName", saicheList.get(position).getTypeName());
                            startActivity(intent1);
                        }
                        break;
                    case 8:
                        if (position < tiyuList.size()) {
                            intent1.putExtra("tagPosition", tiyuList.get(position).getId());
                            intent1.putExtra("title", "体育球类");
                            intent1.putExtra("labName", tiyuList.get(position).getTypeName());
                            startActivity(intent1);
                        }
                        break;
                }
            }
        });
    }

    private void getGameList() {
        YunduanBodyBean bodyBean = new YunduanBodyBean();
        bodyBean.setMarkId(SUB_TYPE_ID);
        new ClassifiHomeClient(context, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<ClassifiHomeBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(ClassifiHomeBean result) {
                        if (result != null && result.getCode() == 0) {
                            //listData(result);
                        } else {
//                            ToastUtil.show(getActivity(), result.getMsg());
                        }
                    }
                });
    }

    //// TODO: 2017/7/20 0020  设置横着的列表数据
    private void listData(ClassifiHomeBean result) {
        if (result.getData() == null) {
            return;
        }

        if (result.getData().getOnlineList().size() > 0) {
            urlList.clear();
            //urlList.addAll();
            //remenList.addAll(result.getData().getOnlineList());
            //每日新发现
            remenAdapter = new DiscoverClassifyTopAdapter(context, classifyList);
            mRVClassifyAll.setAdapter(remenAdapter);
        }
        if (result.getData().getGunFireList().size() > 0) {
            qiangzhanList.clear();
            qiangzhanList.addAll(result.getData().getGunFireList());
            //qiangzhanAdapter = new ClassifiQiangzhanAdapter(context, qiangzhanList);
            // mRvEverydayDiscover.setAdapter(qiangzhanAdapter);
        }
        if (result.getData().getParkourList().size() > 0) {
            maoxianList.clear();
            maoxianList.addAll(result.getData().getParkourList());
            maoxianAdapter = new ClassifiMaoxianAdapter(context, maoxianList);
            gridView_maoxian.setAdapter(maoxianAdapter);
        }
        if (result.getData().getCombatList().size() > 0) {
            dongzuoList.clear();
            dongzuoList.addAll(result.getData().getCombatList());
            dongzuoAdapter = new ClassifiDongzuoAdapter(context, dongzuoList);
            gridView_dongzuo.setAdapter(dongzuoAdapter);
        }
        if (result.getData().getRoleList().size() > 0) {
            jiaoseList.clear();
            jiaoseList.addAll(result.getData().getRoleList());
            jiaoseAdapter = new ClassifiJiaoseAdapter(context, jiaoseList);
            gridView_jiaose.setAdapter(jiaoseAdapter);
        }
        if (result.getData().getPuzzleList().size() > 0) {
            xiuxianList.clear();
            xiuxianList.addAll(result.getData().getPuzzleList());
            xiuxianAdapter = new ClassifiXiuxianAdapter(context, xiuxianList);
            gridView_xiuxian.setAdapter(xiuxianAdapter);
        }
        if (result.getData().getRaceList().size() > 0) {
            saicheList.clear();
            saicheList.addAll(result.getData().getRaceList());
            saicheAdapter = new ClassifiSaicheAdapter(context, saicheList);
            gridView_saiche.setAdapter(saicheAdapter);
        }
        if (result.getData().getSportList().size() > 0) {
            tiyuList.clear();
            tiyuList.addAll(result.getData().getSportList());
            tiyuAdapter = new ClassifiTiyuAdapter(context, tiyuList);
            gridView_tiyu.setAdapter(tiyuAdapter);
        }
        pullListView.onPullUpRefreshComplete();
        pullListView.onPullDownRefreshComplete();
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
         /*   case R.id.iv_vr:
                Intent intent1 = new Intent(context, SBGameActivity.class);
                intent1.putExtra("title", "VR游戏");
                intent1.putExtra("markId", SUB_TYPE_ID_VR);
                intent1.putExtra("isVr", 1);
                startActivity(intent1);
                break;*/
           /* case iv_stand_alone:
                Intent intent2 = new Intent(getActivity(), SBGameActivity.class);
                intent2.putExtra("title", "精品单机");
                intent2.putExtra("markId", SUB_TYPE_ID_STADN);
                startActivity(intent2);
                break;*/
         /*   case iv_boy:
                Intent intent3 = new Intent(context, SBGameActivity.class);
                intent3.putExtra("title", "男生最爱");
                intent3.putExtra("markId", SUB_TYPE_ID_BOY);
                startActivity(intent3);
                break;*/
          /*  case R.id.iv_girl:
                Intent intent4 = new Intent(context, SBGameActivity.class);
                intent4.putExtra("title", "女生最爱");
                intent4.putExtra("markId", SUB_TYPE_ID_GIRL);
                startActivity(intent4);
                break;*/
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        remenAdapter = null;
        remenList = null;
        qiangzhanAdapter = null;
        qiangzhanList = null;
        maoxianAdapter = null;
        maoxianList = null;
        dongzuoAdapter = null;
        dongzuoList = null;
        jiaoseAdapter = null;
        jiaoseList = null;
        xiuxianAdapter = null;
        xiuxianList = null;
        saicheAdapter = null;
        saicheList = null;
        tiyuAdapter = null;
        tiyuList = null;
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
