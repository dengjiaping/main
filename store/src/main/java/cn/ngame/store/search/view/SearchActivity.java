package cn.ngame.store.search.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jzt.hol.android.jkda.sdk.bean.search.RequestSearchBean;
import com.jzt.hol.android.jkda.sdk.bean.search.SearchBean;
import com.jzt.hol.android.jkda.sdk.bean.search.SearchBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.search.SearchGameVideoBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.search.SearchClient;
import com.jzt.hol.android.jkda.sdk.services.search.SearchGVClient;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.LvSearchAdapter;
import cn.ngame.store.adapter.SearchAdapter;
import cn.ngame.store.adapter.SearchOtherAdapter;
import cn.ngame.store.adapter.SearchVideoAdapter;
import cn.ngame.store.bean.SearchHistoryBean;
import cn.ngame.store.core.db.DatabaseManager;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.util.StringUtil;
import cn.ngame.store.video.view.VideoDetailActivity;
import cn.ngame.store.view.LoadStateView;


/**
 * 搜索界面
 * Created by gp on 2016/5/12.
 */
public class SearchActivity extends BaseFgActivity implements View.OnClickListener {

    public static final int READ_EXTERNAL_STORAGE = 24;
    public static final int WRITE_EXTERNAL_STORAGE = 25;
    public static final String TAG = SearchActivity.class.getSimpleName();
    public int GAMETYPE_ID = 36;
    public int VIDEOTYPE_ID = 37;
    private LoadStateView loadStateView;
    private RelativeLayout rl_search;
    private TextView tv_search;
    private EditText et_search;
    private String searchName;
    private ImageView bt_fork;
    private ListView listView;

    private LinearLayout ll_show;
    private LinearLayout ll_history;
    private TextView tv_clear;
    List<SearchGameVideoBean.DataBean.HotSearchGameListBean> searchGameList = new ArrayList<>();
    List<SearchGameVideoBean.DataBean.HotSearchVideoListBean> searchVideotList = new ArrayList<>();
    private GridView gridView_history;
    private GridView gridView_game;
    private GridView gridView_video;
    private SearchOtherAdapter gameAdapter;
    private SearchVideoAdapter videoAdapter;

    private List<SearchHistoryBean> historyList;
    private List<SearchBean.DataBean> searchList = new ArrayList<>();
    private DatabaseManager dbManager;
    LvSearchAdapter historyAdapter;
    SearchAdapter searchAdapter;
    private String tvSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_search);
        dbManager = DatabaseManager.getInstance(this);

        listView = (ListView) findViewById(R.id.listView);
        loadStateView = (LoadStateView) findViewById(R.id.loadStateView);
        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        tv_search = (TextView) findViewById(R.id.tv_search);
        et_search = (EditText) findViewById(R.id.et_search);
        bt_fork = (ImageView) findViewById(R.id.but_fork);

        ll_show = (LinearLayout) findViewById(R.id.ll_show);
        ll_history = (LinearLayout) findViewById(R.id.ll_history);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        gridView_history = (GridView) findViewById(R.id.gridView_history);
        gridView_game = (GridView) findViewById(R.id.gridView_game);
        gridView_video = (GridView) findViewById(R.id.gridView_video);

        loadStateView.setReLoadListener(this);
        rl_search.setOnClickListener(this);
        tv_clear.setOnClickListener(this);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    searchName = ConvUtil.NS(s);
                    tv_search.setText("搜索");
                    doSearch();
                } else {
                    ll_show.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    loadStateView.setVisibility(View.GONE);
                    selectHistory(); //查询本地数据库列表
                    tv_search.setText("取消");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = et_search.getText().toString();
                if (!TextUtil.isEmpty(searchText)) {
                    bt_fork.setVisibility(View.VISIBLE);
                } else {
                    bt_fork.setVisibility(View.GONE);
                }
            }
        });

        //编辑完之后点击软键盘上的回车键才会触发
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    searchName = et_search.getText().toString();
                    if (searchName != null && searchName.length() > 0) {

                        // 先隐藏键盘
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_ING);
                        doSearch();
                    } else {
                        Toast.makeText(SearchActivity.this, "请输入您的心愿，亲！", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
        bt_fork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
                ll_show.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                loadStateView.setVisibility(View.GONE);
            }
        });

        gridView_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_search.setText(historyList.get(position).getTitle());
                searchName = historyList.get(position).getTitle();
                doSearch();
            }
        });
        gridView_game.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, GameDetailActivity.class);
                intent.putExtra("id", searchGameList.get(position).getGameId());
                startActivity(intent);
            }
        });
        gridView_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, VideoDetailActivity.class);
                intent.putExtra("id", searchVideotList.get(position).getVideoId());
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (searchList.get(position).getType() == 1) {
                    Intent intent = new Intent(SearchActivity.this, GameDetailActivity.class);
                    intent.putExtra("id", searchList.get(position).getTypeId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SearchActivity.this, VideoDetailActivity.class);
                    intent.putExtra("id", searchList.get(position).getTypeId());
                    startActivity(intent);
                }
            }
        });

//        FragmentManager fragmentManager = SearchActivity.this.getFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        Fragment fragment = new SearchResultFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("result", "");
//        fragment.setArguments(bundle);
//        transaction.replace(content_wrapper, fragment);
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        transaction.commit();

        selectHistory(); //查询本地数据库列表
        getResultList(); //请求热搜游戏，视频
    }

    //查询本地搜索历史
    public void selectHistory() {
        try {
            historyList = dbManager.queryAllSearchHistory();
            if (historyList.size() == 0) {
                ll_history.setVisibility(View.GONE);
            } else {
                historyAdapter = new LvSearchAdapter(this, historyList);
                gridView_history.setAdapter(historyAdapter);
                ll_history.setVisibility(View.VISIBLE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //请求热搜游戏，视频
    private void getResultList() {
        RequestSearchBean bean = new RequestSearchBean();
        bean.setGameTypeId(GAMETYPE_ID);
        bean.setVideoTypeId(VIDEOTYPE_ID);
        new SearchGVClient(SearchActivity.this, bean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<SearchGameVideoBean>() {
                    @Override
                    public void onError(Throwable e) {
                        e.toString();
                    }

                    @Override
                    public void onNext(SearchGameVideoBean result) {
                        if (result != null && result.getCode() == 0) {
                            searchGameList.addAll(result.getData().getHotSearchGameList());
                            searchVideotList.addAll(result.getData().getHotSearchVideoList());
                            if (gameAdapter == null) {
                                gameAdapter = new SearchOtherAdapter(SearchActivity.this, searchGameList);
                                gridView_game.setAdapter(gameAdapter);
                            } else {
                                gameAdapter.setList(searchGameList);
                            }
                            if (videoAdapter == null) {
                                videoAdapter = new SearchVideoAdapter(SearchActivity.this, searchVideotList);
                                gridView_video.setAdapter(videoAdapter);
                            } else {
                                videoAdapter.setList(searchVideotList);
                            }
                        }
                    }
                });
    }

//    @Override
//    public void startActivity(Intent intent) {
//        super.startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right); //搜索页面退出动画
    }

    //搜索
    private void doSearch() {
        dbManager.addSearchHistory(searchName);
        SearchBodyBean bean = new SearchBodyBean();
        bean.setKeywords(et_search.getText().toString().trim());
        bean.setAppTypeId(0);
        bean.setIosCompany(1);
        new SearchClient(this, bean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<SearchBean>() {
                    @Override
                    public void onError(Throwable e) {
                        ll_show.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_END);
                    }

                    @Override
                    public void onNext(SearchBean result) {
                        if (result != null && result.getCode() == 0) {
                            listView.setVisibility(View.VISIBLE);
                            ll_show.setVisibility(View.GONE);
                            loadStateView.setVisibility(View.GONE);
                            searchList.clear();
                            searchList.addAll(result.getData());
                            if (searchAdapter == null) {
                                searchAdapter = new SearchAdapter(SearchActivity.this, searchList);
                                listView.setAdapter(searchAdapter);
                            } else {
                                searchAdapter.setSearchResultList(searchList);
                            }
                            if (result.getData().size() == 0) {
                                ll_show.setVisibility(View.GONE);
                                listView.setVisibility(View.GONE);
                                loadStateView.setVisibility(View.VISIBLE);
                                loadStateView.setState(LoadStateView.STATE_END);
                            }
                        } else {
                            ll_show.setVisibility(View.GONE);
                            listView.setVisibility(View.GONE);
                            loadStateView.setVisibility(View.VISIBLE);
                            loadStateView.setState(LoadStateView.STATE_END);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_search:
                searchName = et_search.getText().toString().trim();
                    if (StringUtil.isEmpty(searchName)) {
                        this.finish();
                    } else {
//                        et_search.setText("");
//                        ll_show.setVisibility(View.VISIBLE);
//                        listView.setVisibility(View.GONE);
//                        loadStateView.setVisibility(View.GONE);
                    }

//                    if (searchName != null && searchName.length() > 0) {
//
//                        // 先隐藏键盘
//                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
//                                .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
//                                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//
//                        loadStateView.setVisibility(View.VISIBLE);
//                        loadStateView.setState(LoadStateView.STATE_ING);
//                        doSearch();
//                    } else {
//                        Toast.makeText(SearchActivity.this, "请输入您的心愿，亲！", Toast.LENGTH_SHORT).show();
//                    }

                break;
            case R.id.tv_clear:
                dbManager.deleteAllSearchHistory(); //清除搜索历史
                ll_history.setVisibility(View.GONE);
                break;
        }
    }

    //通过id删除
    public void deleteItemhistory(String title) {
        dbManager.deleteSearchHistoryById(title);
        selectHistory();
    }
}
