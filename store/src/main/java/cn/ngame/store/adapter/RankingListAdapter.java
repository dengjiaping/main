/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ngame.store.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.view.GameLoadProgressBar;

import static cn.ngame.store.R.id.tv_position;
import static cn.ngame.store.R.id.tv_shoubing;
import static cn.ngame.store.R.id.tv_title;

public class RankingListAdapter extends BaseAdapter {
    private Activity context;
    private FragmentManager fm;
    private List<GameRankListBean.DataBean> list;
    private int type;

    private static Handler uiHandler = new Handler();

    public RankingListAdapter(Activity context, FragmentManager fm, List<GameRankListBean.DataBean> list, int type) {
        super();
        this.context = context;
        this.fm = fm;
        this.list = list;
        this.type = type;
    }

    public void setList(List<GameRankListBean.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final GameRankListBean.DataBean gameInfo = (list == null) ? null : list.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ranking_list_item, parent, false);
            holder = new ViewHolder(context, fm);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.img_1);
            holder.tv_position = (TextView) convertView.findViewById(tv_position);
            holder.tv_title = (TextView) convertView.findViewById(tv_title);
            holder.tv_percentage = (TextView) convertView.findViewById(R.id.text1);
            holder.tv_yun_duan = (TextView) convertView.findViewById(R.id.tv_yun_duan);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
            holder.tv_shoubing = (TextView) convertView.findViewById(tv_shoubing);
            holder.tv_vr = (TextView) convertView.findViewById(R.id.tv_vr);
            holder.tv_toukong = (TextView) convertView.findViewById(R.id.tv_toukong);
            holder.moreBt = (ImageView) convertView.findViewById(R.id.rank_more_bt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (gameInfo != null)
            holder.update(gameInfo, type, position);

        return convertView;
    }

    public static class ViewHolder {
        private Activity context;
        private GameRankListBean.DataBean gameInfo;
        private SimpleDraweeView img;
        private TextView tv_position, tv_title, tv_percentage;
        private RatingBar ratingBar;
        private GameLoadProgressBar progressBar;    //下载进度条
        private TextView tv_shoubing, tv_vr, tv_toukong, tv_yun_duan;
        private IFileLoad fileLoad;
        private FragmentManager fm;
        private ImageView moreBt;
        private Timer timer = new Timer();
        private SpannableString textSpan;
        private PopupWindow popupWindow;

        public ViewHolder(Activity context, FragmentManager fm) {
            this.context = context;
            this.fm = fm;
            fileLoad = FileLoadManager.getInstance(context);
            //init();
        }

        private void init() {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(gameInfo.getFilename(), gameInfo
                                    .getGameLink(), gameInfo.getPackages(), ConvUtil.NI(gameInfo.getVersionCode()));
                            progressBar.setLoadState(fileStatus);
                        }
                    });
                }
            }, 0, 500);
        }

        /**
         * 更新普通数据
         *
         * @param gameInfo 游戏信息
         */
        public void update(final GameRankListBean.DataBean gameInfo, int type, int position) {
            this.gameInfo = gameInfo;
            final String gameName = gameInfo.getGameName();
            if (!"".equals(gameName)) {
                tv_title.setText(gameName);
            }
            //更多按钮
            moreBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    moreBt.setSelected(true);

                    View popView = LayoutInflater.from(context).inflate(R.layout.layout_rank_more_popup, null);
                    //===================进度条==========================================
                    progressBar = (GameLoadProgressBar) popView.findViewById(R.id.progress_bar);
                    //设置进度条状态
                    progressBar.setLoadState(fileLoad.getGameFileLoadStatus(gameInfo.getFilename(), gameInfo.getGameLink(),
                            gameInfo
                            .getPackages(), ConvUtil.NI(gameInfo.getVersionCode())));
                    //必须设置，否则点击进度条后无法进行响应操作
                    FileLoadInfo fileLoadInfo = new FileLoadInfo(gameInfo.getFilename(), gameInfo.getGameLink(), gameInfo
                            .getMd5(),
                            gameInfo.getVersionCode(), gameInfo.getGameName(), gameInfo.getGameLogo(), gameInfo.getId(),
                            FileLoadInfo
                            .TYPE_GAME);
                    fileLoadInfo.setPackageName(gameInfo.getPackages());
                    progressBar.setFileLoadInfo(fileLoadInfo);
                    progressBar.setOnStateChangeListener(new ProgressBarStateListener(context, fm));
                    progressBar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.toggle();
                        }
                    });
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(gameInfo.getFilename(), gameInfo
                                            .getGameLink(), gameInfo.getPackages(), ConvUtil.NI(gameInfo.getVersionCode()));
                                    progressBar.setLoadState(fileStatus);
                                }
                            });
                        }
                    }, 0, 500);
                    //============================================================================
                    //版本信息
                    TextView versionTv = (TextView) popView.findViewById(R.id.rank_popupp_vesion);
                    textSpan = new SpannableString("版本\n" + gameInfo.getVersionName());
                    textSpan.setSpan(new TextAppearanceSpan(context, R.style.style_14dp_989999), 0, 2, Spanned
                            .SPAN_EXCLUSIVE_EXCLUSIVE);
                    versionTv.setText(textSpan);

                    //更新的时间
                    TextView updateTimeTv = (TextView) popView.findViewById(R.id.rank_popupp_update_time);
                    String updateTime = new SimpleDateFormat("yyyy-MM").format(new Date(gameInfo.getUpdateTime()));
                    textSpan = new SpannableString("更新时间\n" + updateTime);
                    textSpan.setSpan(new TextAppearanceSpan(context, R.style.style_14dp_989999), 0, 4, Spanned
                            .SPAN_EXCLUSIVE_EXCLUSIVE);
                    updateTimeTv.setText(textSpan);
                    int dp120 = CommonUtil.dip2px(context, 128);
                    int dp132 = CommonUtil.dip2px(context, 140);
                    popupWindow = ImageUtil.showPopupWindow(context, moreBt, popView, dp132, dp120);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            v.setSelected(false);
                            timer.cancel();
                        }
                    });

                }
            });
            if (type == 0) {
                tv_position.setVisibility(View.VISIBLE);
            } else {
                tv_position.setVisibility(View.INVISIBLE);
            }
            tv_position.setText(position + 1 + "");

            String imgUrl = gameInfo.getGameLogo();
            if (imgUrl != null && imgUrl.trim().equals("")) {
                imgUrl = null;
            }
            img.setImageURI(imgUrl);

            //long gameSize = gameInfo.getGameSize();
            //String gameSizeStr = TextUtil.formatFileSize(gameSize);
            tv_percentage.setText(gameInfo.getPercentage() + "");

            ratingBar.setRating(gameInfo.getPercentage());

            //是否手柄、vr，头控,
            if (gameInfo.getIsHeadControl() == 1) {
                tv_toukong.setVisibility(View.VISIBLE);
            } else {
                tv_toukong.setVisibility(View.GONE);
            }
            if (gameInfo.getIsHand() == 1) {
                tv_shoubing.setVisibility(View.VISIBLE);
            } else {
                tv_shoubing.setVisibility(View.GONE);
            }
            if (gameInfo.getIsVR() == 1) {//1=是   0=否
                tv_vr.setVisibility(View.VISIBLE);
            } else {
                tv_vr.setVisibility(View.GONE);
            }
            if (gameInfo.getIsTouchScreen() == 1) {
                tv_yun_duan.setVisibility(View.VISIBLE);
            } else {
                tv_yun_duan.setVisibility(View.GONE);
            }
        }
    }
}