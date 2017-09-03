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
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.bean.NecessaryItemData;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.view.GameLoadProgressBar;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import static cn.ngame.store.R.id.tv_title;


/**
 * 显示正在下载游戏的ListView控件适配器
 *
 * @author zeng
 * @since 2016-07-4
 */
public class NeccssaryFragmentAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private Timer timer = new Timer();
    private List<TimerTask> timerTasks = new ArrayList<>();
    private List<NecessaryItemData> mPlanDetails;
    private Context context;
    private FragmentManager fm;
    private Handler uiHandler = new Handler();
    private String mPositionGameId = "";

    public NeccssaryFragmentAdapter(Context context, FragmentManager fm, List<TimerTask>
            timerTasks) {
        super();
        this.context = context;
        this.fm = fm;
        this.timerTasks = timerTasks;

    }

    /**
     * 设置ListView中的数据
     *
     * @param fileInfoList 下载文件信息
     */
    public void setDate(List<NecessaryItemData> fileInfoList) {
        //uiHandler = new Handler();
        this.mPlanDetails = fileInfoList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mPlanDetails != null) {
            return mPlanDetails.size();
        }
        return 0;
    }

    public String getItemGameId() {
        return mPositionGameId;
    }

    @Override
    public Object getItem(int position) {
        if (mPlanDetails != null) {
            return mPlanDetails.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clean() {
        if (mPlanDetails != null) {
            mPlanDetails.clear();
            uiHandler = null;
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder(context, fm);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_necessary, parent, false);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.img_1);
            holder.itemTitle = (TextView) convertView.findViewById(tv_title);
            holder.versionTv = (TextView) convertView.findViewById(R.id.tv_version_time);
            holder.tv_size = (TextView) convertView.findViewById(R.id.tv_length);
            holder.tv_desc = (TextView) convertView.findViewById(R.id.becessary_item_desc_tv);
            holder.progressBar = (GameLoadProgressBar) convertView.findViewById(R.id.progress_bar);
            holder.show_more_disc_bt = (ImageView) convertView.findViewById(R.id.show_more_disc_bt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NecessaryItemData planDetail = (mPlanDetails == null) ? null : mPlanDetails.get(position);
        if (planDetail != null) {
            //holder.update(fileInfo);
            holder.itemTitle.setText(planDetail.getItemTitle());
            holder.versionTv.setText(planDetail.getItemPosition());
            holder.tv_size.setText(planDetail.getItemSize());
            holder.tv_desc.setText(planDetail.getItemDesc());
            holder.show_more_disc_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int lineCount = holder.tv_desc.getLineCount();
                    int maxLines = holder.tv_desc.getMaxLines();
                    if (1 == lineCount) {
                        holder.tv_desc.setMaxLines(2);
                    } else {
                        holder.tv_desc.setMaxLines(1);
                    }
                }
            });
        }

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        //指定了Header的View的显示
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.necessary_list_item_header, parent, false);
            holder.itemParentTv = (TextView) convertView.findViewById(R.id.necessary_list_header_item_tv);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set proj_plans_header itemParentTv as first char in name
        String parentText = this.mPlanDetails.get(position).getParentText();
        holder.itemParentTv.setText(parentText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        // getHeaderId决定header出现的时机，如果当前的headerid和前一个headerid不同时，就会显示
        return Long.parseLong(this.mPlanDetails.get(position).getParentId());
    }

    class HeaderViewHolder {
        TextView itemParentTv;
    }

    /**
     * 用于保存ListView中重用的item视图的引用
     *
     * @author flan
     * @date 2015年10月28日
     */
    public class ViewHolder {

        private Context context;
        private FragmentManager fm;
        private GameRankListBean.DataBean gameInfo;
        private ImageView show_more_disc_bt;
        private SimpleDraweeView img;
        private TextView itemTitle, tv_size, versionTv, tv_desc;
        private GameLoadProgressBar progressBar;    //下载进度条
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
                    if (uiHandler == null) {
                        this.cancel();
                        return;
                    }
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("777", "必备  ---   请求数据");
                            progressBar.setLoadState(fileLoad.getGameFileLoadStatus(gameInfo.getFilename(), gameInfo
                                    .getGameLink(), gameInfo.getPackages(), ConvUtil.NI(gameInfo.getVersionCode())));
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });
                }
            };
            timerTasks.add(task);
            timer.schedule(task, 0, 200);
        }

        public void update(final GameRankListBean.DataBean gameInfo) {
            this.gameInfo = gameInfo;
            String gameName = gameInfo.getGameName();
            if (null != gameName) {
                itemTitle.setText(gameName);
            }
            tv_size.setText(Formatter.formatFileSize(context, gameInfo.getGameSize()));
            versionTv.setText("V" + gameInfo.getVersionName());
            progressBar.setVisibility(View.INVISIBLE);
            //设置进度条状态
            progressBar.setLoadState(fileLoad.getGameFileLoadStatus(gameInfo.getFilename(), gameInfo
                    .getGameLink(), gameInfo.getPackages(), ConvUtil.NI(gameInfo.getVersionCode())));
            //必须设置，否则点击进度条后无法进行响应操作
            FileLoadInfo fileLoadInfo = new FileLoadInfo(gameInfo.getFilename(), gameInfo.getGameLink(), gameInfo
                    .getMd5(), gameInfo.getVersionCode(), gameInfo.getGameName(), gameInfo.getGameLogo(), gameInfo
                    .getId(), FileLoadInfo.TYPE_GAME);
            fileLoadInfo.setPackageName(gameInfo.getPackages());
            progressBar.setFileLoadInfo(fileLoadInfo);
            progressBar.setOnStateChangeListener(new ProgressBarStateListener(context, fm));
            progressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.toggle();
                }
            });

            //加载图片
            img.setImageURI(gameInfo.getGameLogo());
        }

    }

}














