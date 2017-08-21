package cn.ngame.store.game.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.GameAgent;
import cn.ngame.store.bean.GameImage;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.GameLabels;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.fragment.ImageDialogFragment;
import cn.ngame.store.game.presenter.HomeFragmentChangeLayoutListener;
import cn.ngame.store.gamehub.view.ShowViewActivity;
import cn.ngame.store.view.PicassoImageView;

import static cn.ngame.store.R.id.tag_tv_01;
import static cn.ngame.store.R.id.tv_show_all2;

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
    private TextView tv_summary, updateMsgTv, tv_version, tv_time, tv_company, tv_show_all,tv_show_all_2;
    private LinearLayout img_container;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList<String> imgs = new ArrayList<String>();
    private ArrayList<TextView> tagTvList = new ArrayList<>();
    private boolean flag1 = true;
    private boolean flag2 = true;
    HomeFragmentChangeLayoutListener listener;
    private TextView tv_download_count;
    private TextView tv_game_size;
    private Button itemAllNext;
    private TextView labelTv0, labelTv1, labelTv2, labelTv3;
    private List<GameLabels> gameLabels;

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
        updateMsgTv = (TextView) view.findViewById(R.id.tv_update_content);
        tv_version = (TextView) view.findViewById(R.id.tv_version);
        tv_download_count = (TextView) view.findViewById(R.id.download_count_bottom_tv);
        tv_game_size = (TextView) view.findViewById(R.id.game_size_tv);
        tv_time = (TextView) view.findViewById(R.id.text);
        tv_company = (TextView) view.findViewById(R.id.tv_company);

        tv_show_all = (TextView) view.findViewById(R.id.tv_show_all);
        tv_show_all_2 = (TextView) view.findViewById(tv_show_all2);
        itemAllNext = (Button) view.findViewById(R.id.game_detail_next_bt);
        img_container = (LinearLayout) view.findViewById(R.id.img_container);
        ll_detail = (LinearLayout) view.findViewById(R.id.ll_detail);

        labelTv0 = (TextView) view.findViewById(tag_tv_01);
        labelTv1 = (TextView) view.findViewById(R.id.tag_tv_02);
        labelTv2 = (TextView) view.findViewById(R.id.tag_tv_03);
        labelTv3 = (TextView) view.findViewById(R.id.tag_tv_04);
        tagTvList.add(labelTv0);
        tagTvList.add(labelTv1);
        tagTvList.add(labelTv2);
        tagTvList.add(labelTv3);

        itemAllNext.setOnClickListener(this);
        img_container.setOnClickListener(this);
        tv_show_all.setOnClickListener(this);
        tv_show_all_2.setOnClickListener(this);

        if (gameInfo != null && gameInfo.gameDetailsImages != null && gameInfo.gameDetailsImages.size() > 0) {
            img_container.removeAllViews();
            imgs.clear();
            List<GameImage> imagesList = gameInfo.gameDetailsImages;
            int dp250 = CommonUtil.dip2px(context, 250);
            int dp160 = CommonUtil.dip2px(context, 160);
            int dp10 = CommonUtil.dip2px(context, 10);
            for (int i = 0; i < imagesList.size(); i++) {
                GameImage img = imagesList.get(i);
                PicassoImageView imageView = new PicassoImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT);
                params.width = dp250;
                params.height = dp160;
                params.setMargins(0, 0, dp10, 0);
                imageView.setLayoutParams(params);
                //imageView.setTag(img.imageLink);
                String imageLink = img.imageLink;
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setImageUrl(imageLink, R.drawable.ic_def_logo_720_288);
                img_container.addView(imageView);
                //添加图片，查看大图
                imgs.add(imageLink);
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (gameInfo != null) {
            //标签
            gameLabels = gameInfo.gameLabels;
            if (gameLabels != null) {
                int labelSize = gameLabels.size();
                int tvSize = tagTvList.size();
                int size = labelSize > tvSize ? tvSize : labelSize;
                final Intent intent = new Intent();
                intent.setClass(context, LabelGameListActivity.class);
                for (int i = 0; i < size; i++) {
                    TextView labelTv = tagTvList.get(i);
                    final GameLabels gameLabel = this.gameLabels.get(i);
                    final String labelName = gameLabel.labelName;
                    labelTv.setText(labelName);
                    labelTv.setVisibility(View.VISIBLE);
                    labelTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intent.putExtra(KeyConstant.category_Id, String.valueOf(gameLabel.id));
                            intent.putExtra(KeyConstant.TITLE, labelName);
                            startActivity(intent);
                        }
                    });

                }
            }
            tv_summary.setText(gameInfo.gameDesc);
            String s = "";
            for (int i = 0; i < 100; i++) {
                s = s + "更新内容";
            }
            updateMsgTv.setText(s);
            updateMsgTv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //这个回调会调用多次，获取完行数记得注销监听
                    updateMsgTv.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (updateMsgTv.getLineCount() > 5) {
                        tv_show_all_2.setVisibility(View.VISIBLE);
                        updateMsgTv.setMaxLines(5);
                    }
//                    listener.changeLayoutHeight(ll_detail.getMeasuredHeight());
                    return false;
                }
            });
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
            tv_game_size.setText(Formatter.formatFileSize(context, gameInfo.gameSize));
            tv_download_count.setText(gameInfo.downloadCount + "");
            tv_time.setText(df.format(new Date(gameInfo.updateTime)));
            List<GameAgent> gameAgentList = gameInfo.gameAgentList;
            if (gameAgentList != null && gameAgentList.size() > 0) {
                tv_company.setText(gameAgentList.get(0).agentName);
            }
        }
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
                } else {
                    flag1 = true;
                    tv_show_all.setText("显示全部");
                    tv_summary.setMaxLines(5);
                }
                break;
            case R.id.tv_show_all2:
                if (flag2) {
                    flag2 = false;
                    tv_show_all_2.setText("收起");
                    updateMsgTv.setSingleLine(false);
                } else {
                    flag2 = true;
                    tv_show_all_2.setText("显示全部");
                    updateMsgTv.setMaxLines(5);
                }
                break;
            //所有标签页面
            case R.id.game_detail_next_bt:
                if (null != gameInfo) {
                    Intent intent = new Intent(context, LabelsActivity.class);
                    intent.putExtra(KeyConstant.game_Name, gameInfo.gameName);
                    intent.putExtra(KeyConstant.GAME_LABELS, (Serializable) gameLabels);
                    startActivity(intent);
                }
                break;
            case R.id.img_container:
                Intent gv = new Intent();
                gv.setClass(getActivity(), ShowViewActivity.class);
                gv.putStringArrayListExtra("viewImages", imgs);
                gv.putExtra("selectPosition", 0);
                startActivity(gv);
                break;

        }
    }
}
