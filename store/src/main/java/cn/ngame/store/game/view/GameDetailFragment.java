package cn.ngame.store.game.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.ngame.store.R;
import cn.ngame.store.bean.GameImage;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.fragment.ImageDialogFragment;
import cn.ngame.store.game.presenter.HomeFragmentChangeLayoutListener;
import cn.ngame.store.gamehub.view.ShowViewActivity;
import cn.ngame.store.view.PicassoImageView;

/**
 * 显示游戏详情的Fragment
 *
 * @author flan
 * @since 2016/5/17
 */
public class GameDetailFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = GameDetailFragment.class.getSimpleName();
    private Activity context;
    private LinearLayout ll_detail;
    private GameInfo gameInfo;
    private TextView tv_summary, tv_version, tv_time, tv_company, tv_show_all;
    private LinearLayout img_container;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ArrayList<String> imgs = new ArrayList<String>();
    boolean flag1 = true;
    HomeFragmentChangeLayoutListener listener;

    public static GameDetailFragment newInstance(GameInfo gameInfo) {
        GameDetailFragment fragment = new GameDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GameInfo.TAG, gameInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取初始参数
        gameInfo = (GameInfo) getArguments().getSerializable(GameInfo.TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_game_detail, null);
        tv_summary = (TextView) view.findViewById(R.id.tv_summary);
        tv_version = (TextView) view.findViewById(R.id.tv_version);
        tv_time = (TextView) view.findViewById(R.id.text);
        tv_company = (TextView) view.findViewById(R.id.tv_company);
        tv_show_all = (TextView) view.findViewById(R.id.tv_show_all);
        img_container = (LinearLayout) view.findViewById(R.id.img_container);
        ll_detail = (LinearLayout) view.findViewById(R.id.ll_detail);
        tv_show_all.setOnClickListener(this);
        if (gameInfo != null && gameInfo.gameDetailsImages != null && gameInfo.gameDetailsImages.size() > 0) {
            img_container.removeAllViews();
            imgs.clear();
            for (int i = 0; i < gameInfo.gameDetailsImages.size(); i++) {
                GameImage img = gameInfo.gameDetailsImages.get(i);
                PicassoImageView imageView = new PicassoImageView(getActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.width = CommonUtil.dip2px(context, 290);
                params.height = CommonUtil.dip2px(context, 180);
                if (i > 0) {
                    params.setMargins(CommonUtil.dip2px(context, 5), 0, 0, 0);
                }
                imageView.setLayoutParams(params);
                //imageView.setTag(img.imageLink);
                String imageLink = img.imageLink;
                imageView.setImageUrl(imageLink, 290f, 180f, R.drawable.ic_def_logo_412_200);
                img_container.addView(imageView);
                //添加图片，查看大图
                imgs.add(imageLink);
            }
        }
        //点击查看大图
        img_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gv = new Intent();
                gv.setClass(getActivity(), ShowViewActivity.class);
                gv.putStringArrayListExtra("viewImages", imgs);
                gv.putExtra("selectPosition", 0);
                startActivity(gv);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (gameInfo != null) {
            tv_summary.setText(gameInfo.gameDesc);
            tv_summary.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //这个回调会调用多次，获取完行数记得注销监听
                    tv_summary.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (tv_summary.getLineCount() > 5) {
                        tv_show_all.setVisibility(View.VISIBLE);
                        tv_summary.setMaxLines(5);
                    }
//                    listener.changeLayoutHeight(ll_detail.getMeasuredHeight());
                    return false;
                }
            });
            tv_version.setText(gameInfo.versionName);
            tv_time.setText(df.format(new Date(gameInfo.updateTime)));
            if (gameInfo.gameAgentList != null && gameInfo.gameAgentList.size() > 0) {
                tv_company.setText(gameInfo.gameAgentList.get(0).agentName);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "我被调用了GameDetailFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 加载大图片
     */
    private void imageEnlarge() {

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("imageDialog");
        if (prev != null) {
            transaction.remove(prev);
        }
        transaction.addToBackStack(null);

        final ImageDialogFragment dialogFragment = ImageDialogFragment.newInstance();
        dialogFragment.show(transaction, "imageDialog");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_show_all:
                if (flag1) {
                    flag1 = false;
                    tv_show_all.setText("收起");
                    tv_summary.setSingleLine(false);
//                    listener.changeLayoutHeight(ll_detail.getMeasuredHeight());
                } else {
                    flag1 = true;
                    tv_show_all.setText("显示全部");
                    tv_summary.setMaxLines(5);
//                    listener.changeLayoutHeight(ll_detail.getMeasuredHeight());
                }
                break;
        }
    }
}
