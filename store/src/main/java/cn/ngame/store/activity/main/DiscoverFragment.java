package cn.ngame.store.activity.main;

import android.content.Intent;
import android.os.Bundle;
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
import cn.ngame.store.adapter.ClassifiRemenAdapter;
import cn.ngame.store.adapter.ClassifiSaicheAdapter;
import cn.ngame.store.adapter.ClassifiTiyuAdapter;
import cn.ngame.store.adapter.ClassifiXiuxianAdapter;
import cn.ngame.store.adapter.HomeRaiderAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.HotInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.game.view.SBGameActivity;
import cn.ngame.store.video.view.VideoDetailActivity;
import cn.ngame.store.view.BannerView;
import cn.ngame.store.view.PicassoImageView;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

import static cn.ngame.store.R.id.banner_view;

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
    private static final String TAG = "777";
    /**
     * 顶部栏
     */
    private PullToRefreshListView pullListView;
    /**
     * headerView
     */
    private ImageView iv_vr, iv_stand_alone, iv_boy, iv_girl;
    private GridView gridView_remen, gridView_qiangzhan, gridView_maoxian, gridView_dongzuo, gridView_jiaose, gridView_xiuxian, gridView_saiche, gridView_tiyu;

    ClassifiRemenAdapter remenAdapter;
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

    public static DiscoverFragment newInstance(String arg) {
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
    protected void initViewsAndEvents(View view) {
        init(view);
    }

    private void init(View view) {
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pullListView);

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
        View headView = View.inflate(getActivity(), R.layout.discover_header_view, null);
        initHeadView(headView);
        //头布局放入listView中
        if (pullListView.getRefreshableView().getHeaderViewsCount() == 0) {
            pullListView.getRefreshableView().addHeaderView(headView);
        }
        List<String> list = new ArrayList<>();
        list.add(new String("测试"));
        list.add(new String("测试2"));
        HomeRaiderAdapter adapter = new HomeRaiderAdapter(getActivity(), list, "0");
        pullListView.getRefreshableView().setAdapter(adapter);

        getGameList();
        getBannerData();
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
            PicassoImageView img = new PicassoImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
                        Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                        intent.putExtra("id", info.gameId);
                        startActivity(intent);
                    } else if (info.type == 2) {
                        Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                        intent.putExtra("id", info.videoId);
                        startActivity(intent);
                    }
                }
            });
            list.add(img);
        }
        return list;
    }
    private void initHeadView(View headView) {
        iv_vr = (ImageView) headView.findViewById(R.id.iv_vr);
        iv_stand_alone = (ImageView) headView.findViewById(R.id.iv_stand_alone);
        iv_boy = (ImageView) headView.findViewById(R.id.iv_boy);
        iv_girl = (ImageView) headView.findViewById(R.id.iv_girl);
        gridView_remen = (GridView) headView.findViewById(R.id.gridView_remen);
        gridView_qiangzhan = (GridView) headView.findViewById(R.id.gridView_qiangzhan);
        gridView_maoxian = (GridView) headView.findViewById(R.id.gridView_maoxian);
        gridView_dongzuo = (GridView) headView.findViewById(R.id.gridView_dongzuo);
        gridView_jiaose = (GridView) headView.findViewById(R.id.gridView_jiaose);
        gridView_xiuxian = (GridView) headView.findViewById(R.id.gridView_xiuxian);
        gridView_saiche = (GridView) headView.findViewById(R.id.gridView_saiche);
        gridView_tiyu = (GridView) headView.findViewById(R.id.gridView_tiyu);
        iv_vr.setOnClickListener(this);
        iv_stand_alone.setOnClickListener(this);
        iv_boy.setOnClickListener(this);
        iv_girl.setOnClickListener(this);
        clickGradView(gridView_remen, 1);
        clickGradView(gridView_qiangzhan, 2);
        clickGradView(gridView_maoxian, 3);
        clickGradView(gridView_dongzuo, 4);
        clickGradView(gridView_jiaose, 5);
        clickGradView(gridView_xiuxian, 6);
        clickGradView(gridView_saiche, 7);
        clickGradView(gridView_tiyu, 8);

        bannerView = (BannerView) headView.findViewById(banner_view);
    }

    //传游戏列表id，不传lab查询id
    public void clickGradView(GridView gridView, final int i) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(getActivity(), SBGameActivity.class);
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
        new ClassifiHomeClient(getActivity(), bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<ClassifiHomeBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(ClassifiHomeBean result) {
                        if (result != null && result.getCode() == 0) {
                            listData(result);
                        } else {
//                            ToastUtil.show(getActivity(), result.getMsg());
                        }
                    }
                });
    }

    private void listData(ClassifiHomeBean result) {
        if (result.getData() == null) {
            return;
        }
        if (result.getData().getOnlineList().size() > 0) {
            remenList.clear();
            remenList.addAll(result.getData().getOnlineList());
            remenAdapter = new ClassifiRemenAdapter(getActivity(), remenList);
            gridView_remen.setAdapter(remenAdapter);
        }
        if (result.getData().getGunFireList().size() > 0) {
            qiangzhanList.clear();
            qiangzhanList.addAll(result.getData().getGunFireList());
            qiangzhanAdapter = new ClassifiQiangzhanAdapter(getActivity(), qiangzhanList);
            gridView_qiangzhan.setAdapter(qiangzhanAdapter);
        }
        if (result.getData().getParkourList().size() > 0) {
            maoxianList.clear();
            maoxianList.addAll(result.getData().getParkourList());
            maoxianAdapter = new ClassifiMaoxianAdapter(getActivity(), maoxianList);
            gridView_maoxian.setAdapter(maoxianAdapter);
        }
        if (result.getData().getCombatList().size() > 0) {
            dongzuoList.clear();
            dongzuoList.addAll(result.getData().getCombatList());
            dongzuoAdapter = new ClassifiDongzuoAdapter(getActivity(), dongzuoList);
            gridView_dongzuo.setAdapter(dongzuoAdapter);
        }
        if (result.getData().getRoleList().size() > 0) {
            jiaoseList.clear();
            jiaoseList.addAll(result.getData().getRoleList());
            jiaoseAdapter = new ClassifiJiaoseAdapter(getActivity(), jiaoseList);
            gridView_jiaose.setAdapter(jiaoseAdapter);
        }
        if (result.getData().getPuzzleList().size() > 0) {
            xiuxianList.clear();
            xiuxianList.addAll(result.getData().getPuzzleList());
            xiuxianAdapter = new ClassifiXiuxianAdapter(getActivity(), xiuxianList);
            gridView_xiuxian.setAdapter(xiuxianAdapter);
        }
        if (result.getData().getRaceList().size() > 0) {
            saicheList.clear();
            saicheList.addAll(result.getData().getRaceList());
            saicheAdapter = new ClassifiSaicheAdapter(getActivity(), saicheList);
            gridView_saiche.setAdapter(saicheAdapter);
        }
        if (result.getData().getSportList().size() > 0) {
            tiyuList.clear();
            tiyuList.addAll(result.getData().getSportList());
            tiyuAdapter = new ClassifiTiyuAdapter(getActivity(), tiyuList);
            gridView_tiyu.setAdapter(tiyuAdapter);
        }
        pullListView.onPullUpRefreshComplete();
        pullListView.onPullDownRefreshComplete();
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_vr:
                Intent intent1 = new Intent(getActivity(), SBGameActivity.class);
                intent1.putExtra("title", "VR游戏");
                intent1.putExtra("markId", SUB_TYPE_ID_VR);
                intent1.putExtra("isVr", 1);
                startActivity(intent1);
                break;
            case R.id.iv_stand_alone:
                Intent intent2 = new Intent(getActivity(), SBGameActivity.class);
                intent2.putExtra("title", "精品单机");
                intent2.putExtra("markId", SUB_TYPE_ID_STADN);
                startActivity(intent2);
                break;
            case R.id.iv_boy:
                Intent intent3 = new Intent(getActivity(), SBGameActivity.class);
                intent3.putExtra("title", "男生最爱");
                intent3.putExtra("markId", SUB_TYPE_ID_BOY);
                startActivity(intent3);
                break;
            case R.id.iv_girl:
                Intent intent4 = new Intent(getActivity(), SBGameActivity.class);
                intent4.putExtra("title", "女生最爱");
                intent4.putExtra("markId", SUB_TYPE_ID_GIRL);
                startActivity(intent4);
                break;
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
