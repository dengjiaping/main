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
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.view.GameLoadProgressBar;

/**
 * 手柄游戏的ListView控件适配器
 *
 * @author zeng
 * @since 2016-05-16
 */
public class SelectGameAdapter extends BaseAdapter {

    private List<GameInfo> gameInfoList;

    private Context context;
    private FragmentManager fm;
    private static Handler uiHandler = new Handler();

    public SelectGameAdapter(Context context, FragmentManager fm) {
        super();
        this.fm = fm;
        this.context = context;
    }

    /**
     * 设置ListView中的数据
     *
     * @param gameInfos 游戏数据
     */
    public void setDate(List<GameInfo> gameInfos) {

        if (gameInfoList == null || gameInfoList.size() <= 0) {
            gameInfoList = gameInfos;
        } else {
            gameInfoList.addAll(gameInfos);
        }

    }

    @Override
    public int getCount() {

        if (gameInfoList != null) {
            return gameInfoList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {

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
        if (gameInfoList != null)
            gameInfoList.clear();
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final GameInfo gameInfo = (gameInfoList == null) ? null : gameInfoList.get(position);

        ViewHolder holder;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.item_select_game, parent, false);

            holder = new ViewHolder(context, fm);
            holder.img = (ImageView) convertView.findViewById(R.id.img_1);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
            holder.tv_size = (TextView) convertView.findViewById(R.id.text1);
            holder.tv_count = (TextView) convertView.findViewById(R.id.text2);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
            holder.progressBar = (GameLoadProgressBar) convertView.findViewById(R.id.progress_bar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (gameInfo != null)
            holder.update(gameInfo);

        return convertView;
    }

    /**
     * 用于保存ListView中重用的item视图的引用
     *
     * @author flan
     * @since 2015年10月28日
     */
    public static class ViewHolder {

        private Context context;
        private GameInfo gameInfo;

        private ImageView img;
        private TextView tv_title, tv_summary, tv_size, tv_count;
        private RatingBar ratingBar;
        private GameLoadProgressBar progressBar;    //下载进度条
        private FragmentManager fm;

        private IFileLoad fileLoad;

        private Timer timer = new Timer();

        public ViewHolder(Context context, FragmentManager fm) {
            this.context = context;
            this.fm = fm;
            fileLoad = FileLoadManager.getInstance(context);
            init();
        }

        private void init() {

            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(gameInfo.filename, gameInfo.gameLink,
                                    gameInfo.packages, ConvUtil.NI(gameInfo.versionCode));
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
        public void update(final GameInfo gameInfo) {

            this.gameInfo = gameInfo;

            String imgUrl = gameInfo.gameLogo;
            if (imgUrl != null && imgUrl.trim().equals("")) {
                imgUrl = null;
            }
            Picasso.with(context)
                    .load(imgUrl)
                    .placeholder(R.drawable.ic_def_logo_720_288)
                    .error(R.drawable.ic_def_logo_720_288)
                    .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                    .centerInside()
                    .tag(context)
                    .into(img);

            String gameName = gameInfo.gameName;
            if (!"".equals(gameName)) {
                //gameName = gameName.length() > 15 ? gameName.substring(0, 15) : gameName;
                tv_title.setText(gameName);
            }

            String gameDesc = gameInfo.gameDesc;
            if (gameDesc != null && !"".equals(gameDesc)) {
                //gameDesc = gameDesc.length() > 30 ? gameDesc.substring(0, 30)+"..." : gameDesc;
                tv_summary.setText(gameDesc);
            } else {
                tv_summary.setText("");
            }

            long gameSize = gameInfo.gameSize;
            String gameSizeStr = Formatter.formatFileSize(context, gameSize);
            tv_size.setText("游戏大小：" + gameSizeStr);

            long gameCount = gameInfo.downloadCount;
//			tv_count.setText("/"+gameCount+"次下载");

            ratingBar.setRating(gameInfo.percentage);

            //设置进度条状态
            progressBar.setLoadState(fileLoad.getGameFileLoadStatus(gameInfo.filename, gameInfo.gameLink, gameInfo.packages, ConvUtil.NI(gameInfo.versionCode)));
            //必须设置，否则点击进度条后无法进行响应操作
            FileLoadInfo fileLoadInfo = new FileLoadInfo(gameInfo.filename, gameInfo.gameLink, gameInfo.md5, ConvUtil.NI(gameInfo.versionCode), gameInfo.gameName, gameInfo.gameLogo, gameInfo.id, FileLoadInfo.TYPE_GAME);
            fileLoadInfo.setPackageName(gameInfo.packages);
            progressBar.setFileLoadInfo(fileLoadInfo);
            progressBar.setOnStateChangeListener(new ProgressBarStateListener(context, fm));
            progressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.toggle();
                }
            });
        }

    }

}














