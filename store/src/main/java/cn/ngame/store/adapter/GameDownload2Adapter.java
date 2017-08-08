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
import android.widget.TextView;

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
import cn.ngame.store.view.ActionItem;
import cn.ngame.store.view.GameLoadProgressBar;
import cn.ngame.store.view.QuickAction;

/**
 * 显示正在下载游戏的ListView控件适配器
 *
 * @author zeng
 * @since 2016-07-4
 */
public class GameDownload2Adapter extends BaseAdapter {

    //private static final String TAG = LoadIngLvAdapter.class.getSimpleName();

    private List<FileLoadInfo> fileInfoList;

    private Context context;
    private FragmentManager fm;
    private static Handler uiHandler = new Handler();
    private QuickAction mItemClickQuickAction;
    private int mPosition;

    public GameDownload2Adapter(Context context, FragmentManager fm, QuickAction mItemClickQuickAction) {
        super();
        this.mItemClickQuickAction = mItemClickQuickAction;
        this.context = context;
        this.fm = fm;
    }

    /**
     * 设置ListView中的数据
     *
     * @param fileInfoList 下载文件信息
     */
    public void setDate(List<FileLoadInfo> fileInfoList) {

        this.fileInfoList = fileInfoList;

    }

    @Override
    public int getCount() {

        if (fileInfoList != null) {
            return fileInfoList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (fileInfoList != null) {
            return fileInfoList.get(position);
        }
        return null;
    }

    public Object getItemInfo() {
        if (fileInfoList != null) {
            return fileInfoList.get(mPosition);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clean() {
        if (fileInfoList != null)
            fileInfoList.clear();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final FileLoadInfo fileInfo = (fileInfoList == null) ? null : fileInfoList.get(position);
        mPosition = position;
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder(context, fm, mItemClickQuickAction);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_game_load_finished, parent, false);
            holder.img = (ImageView) convertView.findViewById(R.id.img_1);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.tv_size = (TextView) convertView.findViewById(R.id.tv_length);
            holder.more_bt = (ImageView) convertView.findViewById(R.id.manager_installed_more_bt);
            holder.progressBar = (GameLoadProgressBar) convertView.findViewById(R.id.progress_bar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (fileInfo != null) {
            holder.update(fileInfo);
        }

        return convertView;
    }

    /**
     * 用于保存ListView中重用的item视图的引用
     *
     * @author flan
     * @date 2015年10月28日
     */
    public static class ViewHolder {

        private Context context;
        private FragmentManager fm;
        private FileLoadInfo fileInfo;

        private ImageView img;
        private ImageView more_bt;
        private TextView tv_title, tv_state, tv_size;
        private GameLoadProgressBar progressBar;    //下载进度条

        private Timer timer = new Timer();
        private IFileLoad fileLoad;
        private QuickAction mItemClickQuickAction;

        public ViewHolder(Context context, FragmentManager fm, QuickAction mItemClickQuickAction) {
            this.context = context;
            this.fm = fm;
            this.mItemClickQuickAction = mItemClickQuickAction;
            fileLoad = FileLoadManager.getInstance(context);
            init();
        }

        private void init() {
            final ViewGroup viewGroup = mItemClickQuickAction.GetActionItemsGroup();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(fileInfo.getName(), fileInfo.getUrl(),
                                    fileInfo.getPackageName(), fileInfo.getVersionCode());

                            progressBar.setLoadState(fileStatus);

                            if (fileStatus == null) {
                                return;
                            }
                            int status = fileStatus.getStatus();
                            if (status == GameFileStatus.STATE_DOWNLOAD) {
                                tv_state.setText("下载中...");
                            } else if (status == GameFileStatus.STATE_PAUSE) {
                                tv_state.setText("暂停中");
                                //打开
                            } else if (status == GameFileStatus.STATE_HAS_INSTALL) {
                                tv_size.setVisibility(View.INVISIBLE);
                                tv_state.setText("");
                                if (0 == viewGroup.getChildCount()) {
                                    ActionItem pointItem = new ActionItem(0, "卸载", null);
                                    mItemClickQuickAction.addActionItem(pointItem);
                                }
                            } else {//安装
                                tv_state.setText("下载完成");
                               // viewGroup.removeAllViews();
                                if (0 == viewGroup.getChildCount()) {
                                    ActionItem pointItem = new ActionItem(0, "删除安装包", null);
                                    mItemClickQuickAction.addActionItem(pointItem);
                                }
                            }
                        }
                    });
                }
            }, 0, 500);
        }

        public void update(final FileLoadInfo fileInfo) {

            this.fileInfo = fileInfo;

            String gameName = fileInfo.getTitle();
            if (null != gameName) {
                tv_title.setText(gameName);
            } else {
                tv_title.setText("");
            }
            tv_size.setText(TextUtil.formatFileSize(fileInfo.getLength()));
            more_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickQuickAction.show(v);
                }
            });
            //加载图片
            if (!TextUtil.isEmpty(fileInfo.getPreviewUrl())) {
                Picasso.with(context)
                        .load(fileInfo.getPreviewUrl())
                        .placeholder(R.drawable.ic_def_logo_720_288)
                        .error(R.drawable.ic_def_logo_720_288)
                        .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                        .centerInside()
                        .tag(context)
                        .into(img);
            }

            //设置进度条状态
            progressBar.setLoadState(fileLoad.getGameFileLoadStatus(fileInfo.getName(), fileInfo.getPreviewUrl(), fileInfo
                    .getPackageName(), fileInfo.getVersionCode()));
            //必须设置，否则点击进度条后无法进行响应操作
            progressBar.setFileLoadInfo(fileInfo);
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














