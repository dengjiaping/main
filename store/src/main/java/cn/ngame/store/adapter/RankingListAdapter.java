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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.view.GameLoadProgressBar;

import static cn.ngame.store.R.id.tv_position;
import static cn.ngame.store.R.id.tv_shoubing;
import static cn.ngame.store.R.id.tv_summary;
import static cn.ngame.store.R.id.tv_title;
import static cn.ngame.store.R.id.tv_toukong;
import static cn.ngame.store.R.id.tv_vr;

public class RankingListAdapter extends BaseAdapter {

    private Context context;
    private FragmentManager fm;
    private List<GameRankListBean.DataBean> list;
    private int type;

    private static Handler uiHandler = new Handler();

    public RankingListAdapter(Context context, FragmentManager fm, List<GameRankListBean.DataBean> list, int type) {
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
            holder.img = (ImageView) convertView.findViewById(R.id.img_1);
            holder.tv_position = (TextView) convertView.findViewById(tv_position);
            holder.tv_title = (TextView) convertView.findViewById(tv_title);
            holder.tv_summary = (TextView) convertView.findViewById(tv_summary);
            holder.tv_size = (TextView) convertView.findViewById(R.id.text1);
            holder.tv_count = (TextView) convertView.findViewById(R.id.text2);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
            holder.progressBar = (GameLoadProgressBar) convertView.findViewById(R.id.progress_bar);
            holder.tv_shoubing = (TextView) convertView.findViewById(tv_shoubing);
            holder.tv_vr = (TextView) convertView.findViewById(tv_vr);
            holder.tv_toukong = (TextView) convertView.findViewById(tv_toukong);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (gameInfo != null)
            holder.update(gameInfo, type, position);

        return convertView;
    }

    public static class ViewHolder {
        private Context context;
        private GameRankListBean.DataBean gameInfo;
        private ImageView img;
        private TextView tv_position, tv_title, tv_summary, tv_size, tv_count;
        private RatingBar ratingBar;
        private GameLoadProgressBar progressBar;    //下载进度条
        private TextView tv_shoubing, tv_vr, tv_toukong;
        private IFileLoad fileLoad;
        private FragmentManager fm;
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
                            GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(gameInfo.getFilename(), gameInfo.getGameLink(), gameInfo.getPackages(), ConvUtil.NI(gameInfo.getVersionCode()));
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

            if (type == 0) {
                tv_position.setVisibility(View.VISIBLE);
            } else {
                tv_position.setVisibility(View.INVISIBLE);
            }
            tv_position.setText(position + 4 + "");

            String imgUrl = gameInfo.getGameLogo();
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

            String gameName = gameInfo.getGameName();
            if (!"".equals(gameName)) {
                tv_title.setText(gameName);
            }

            String gameDesc = gameInfo.getGameDesc();
            if (gameDesc != null && !"".equals(gameDesc)) {
                tv_summary.setText(gameDesc);
            } else {
                tv_summary.setText("");
            }

            long gameSize = gameInfo.getGameSize();
            String gameSizeStr = TextUtil.formatFileSize(gameSize);
            tv_size.setText(gameSizeStr);

            long gameCount = gameInfo.getDownloadCount();
            tv_count.setText("/" + gameCount + "次下载");

            ratingBar.setRating(gameInfo.getPercentage());

            //设置进度条状态
            progressBar.setLoadState(fileLoad.getGameFileLoadStatus(gameInfo.getFilename(), gameInfo.getGameLink(), gameInfo.getPackages(), ConvUtil.NI(gameInfo.getVersionCode())));
            //必须设置，否则点击进度条后无法进行响应操作
            FileLoadInfo fileLoadInfo = new FileLoadInfo(gameInfo.getFilename(), gameInfo.getGameLink(), gameInfo.getMd5(), gameInfo.getVersionCode(), gameInfo.getGameName(), gameInfo.getGameLogo(), gameInfo.getId(), FileLoadInfo.TYPE_GAME);
            fileLoadInfo.setPackageName(gameInfo.getPackages());
            progressBar.setFileLoadInfo(fileLoadInfo);
            progressBar.setOnStateChangeListener(new ProgressBarStateListener(context, fm));
            progressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.toggle();
                }
            });
            //是否手柄、vr，头控
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
            if (gameInfo.getIsVR() == 1) {
                tv_vr.setVisibility(View.VISIBLE);
            } else {
                tv_vr.setVisibility(View.GONE);
            }
        }
    }
}