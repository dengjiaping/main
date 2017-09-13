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

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBean;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.game.view.MoreGameListActivity;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.view.GameLoadProgressBar;

/**
 * 手柄游戏的ListView控件适配器
 *
 * @author zeng
 * @since 2016-05-16
 */
public class MoreGameListAdapter extends BaseAdapter {

    private static String TAG = MoreGameListActivity.class.getSimpleName();
    private List<LikeListBean.DataBean.GameListBean> gameInfoList;
    private ViewHolder holder;
    private Handler uiHandler = new Handler();
    private Timer timer = new Timer();
    private Context context;
    private FragmentManager fm;
    private List<TimerTask> timerTasks;

    public MoreGameListAdapter(Context context, FragmentManager fm, List<TimerTask> timerTasks) {
        super();
        this.fm = fm;
        this.context = context;
        this.timerTasks = timerTasks;
    }

    /**
     * 设置ListView中的数据
     *
     * @param gameInfos 游戏数据
     */
    public void setDate(List<LikeListBean.DataBean.GameListBean> gameInfos) {
        gameInfoList = gameInfos;
    }

    @Override
    public int getCount() {
        if (gameInfoList != null) {
            return gameInfoList.size();
        }
        return 0;
    }

    @Override
    public LikeListBean.DataBean.GameListBean getItem(int position) {
        if (gameInfoList != null) {
            return gameInfoList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clean() {
        if (gameInfoList != null) {
            uiHandler = null;
            if (null != timer) {
                timer.cancel();
                timer = null;
            }
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final LikeListBean.DataBean.GameListBean gameInfo = (gameInfoList == null) ? null : gameInfoList.get(position);
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_game_2, parent, false);

            holder = new ViewHolder(context, fm);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.img_1);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tagTv0 = (TextView) convertView.findViewById(R.id.classify_item_tip_tv_01);
            holder.tagTv1 = (TextView) convertView.findViewById(R.id.classify_item_tip_tv_02);
            holder.tagTv2 = (TextView) convertView.findViewById(R.id.classify_item_tip_tv_03);
            holder.tagTv3 = (TextView) convertView.findViewById(R.id.classify_item_tip_tv_04);
            holder.tv_size = (TextView) convertView.findViewById(R.id.text1);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
            holder.progressBar = (GameLoadProgressBar) convertView.findViewById(R.id.progress_bar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (gameInfo != null) {
            holder.update(gameInfo);
        }

        return convertView;
    }

    /**
     * @author flan
     * @since 2015年10月28日
     */
    public class ViewHolder {

        private Context context;
        private LikeListBean.DataBean.GameListBean gameInfo;
        private SimpleDraweeView img;
        private TextView tv_title, tv_size, tagTv0, tagTv1, tagTv2, tagTv3;
        private RatingBar ratingBar;
        private GameLoadProgressBar progressBar;    //下载进度条
        private FragmentManager fm;
        private IFileLoad fileLoad;

        public ViewHolder(Context context, FragmentManager fm) {
            this.context = context;
            this.fm = fm;
            fileLoad = FileLoadManager.getInstance(context);
            //init();
        }

        private void init() {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (null == uiHandler) {
                        this.cancel();
                        return;
                    }
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "更新下载:" + ViewHolder.this.gameInfo.getGameName());
                            if (null == ViewHolder.this.gameInfo) {
                                return;
                            }
                            GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(ViewHolder.this.gameInfo.getFilename(),
                                    ViewHolder.this.gameInfo.getGameLink(), ViewHolder.this.gameInfo.getPackages(), ConvUtil.NI
                                            (ViewHolder.this
                                                    .gameInfo.getVersionCode()));
                            progressBar.setLoadState(fileStatus);
                        }
                    });
                }
            };
            timerTasks.add(task);
            if (null != timer) {
                timer.schedule(task, 0, 500);
            }
        }

        /**
         * 更新普通数据
         *
         * @param gameInfo 游戏信息
         */
        public void update(final LikeListBean.DataBean.GameListBean gameInfo) {
            this.gameInfo = gameInfo;
            Log.d(TAG, "update: " + gameInfo.getGameName());

            String gameLogo = gameInfo.getGameLogo();
            img.setImageURI(gameLogo);

            String gameName = gameInfo.getGameName();
            if (null != gameName && !"".equals(gameName)) {
                //gameName = gameName.length() > 15 ? gameName.substring(0, 15) : gameName;
                tv_title.setText(gameName);
            }

           /* long gameSize = gameInfo.gameSize;
            String gameSizeStr = TextUtil.formatFileSize(gameSize);*/

            tv_size.setText(gameInfo.getScoreLevel() + "");

            ratingBar.setRating(gameInfo.getScoreLevel());//星星

            //设置进度条状态
            String gameLink = gameInfo.getGameLink();
            GameFileStatus gameFileLoadStatus = fileLoad.getGameFileLoadStatus(gameInfo.getFilename(), gameLink,
                    gameInfo.getPackages(), ConvUtil.NI(gameInfo.getVersionCode()));
            progressBar.setLoadState(gameFileLoadStatus);
            //必须设置，否则点击进度条后无法进行响应操作
            FileLoadInfo fileLoadInfo = new FileLoadInfo(gameInfo.getFilename(), gameLink, gameInfo.getMd5(),
                    ConvUtil.NI(gameInfo.getVersionCode()), gameName, gameLogo, gameInfo.getId(), FileLoadInfo.TYPE_GAME);
            fileLoadInfo.setPackageName(gameInfo.getPackages());
            progressBar.setFileLoadInfo(fileLoadInfo);
            progressBar.setOnStateChangeListener(new ProgressBarStateListener(context, fm));
            progressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.toggle();
                }
            });
            init();
            String simpleLabel = gameInfo.getCName();
            if (simpleLabel != null) {
                String[] typeNameArray = simpleLabel.split("\\,");
                for (int i = 0; i < typeNameArray.length; i++) {
                    switch (i) {
                        case 0:
                            tagTv0.setText(typeNameArray[i]);
                            tagTv0.setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            tagTv1.setText(typeNameArray[i]);
                            tagTv1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            tagTv2.setText(typeNameArray[i]);
                            tagTv2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            tagTv3.setText(typeNameArray[i]);
                            tagTv3.setVisibility(View.VISIBLE);
                            break;
                    }

                }
            }
        }
    }

}














