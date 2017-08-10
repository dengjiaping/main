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
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.view.GameLoadProgressBar;
import cn.ngame.store.view.QuickAction;

import static cn.ngame.store.R.id.tv_state;

/**
 * 显示正在下载游戏的ListView控件适配器
 *
 * @author zeng
 * @since 2016-07-4
 */
public class GameDownload2Adapter extends BaseAdapter {

    //private static final String TAG = LoadIngLvAdapter.class.getSimpleName();

    private List<FileLoadInfo> fileInfoList;
    private Timer timer = new Timer();
    private Handler uiHandler = new Handler();
    private Context context;
    private FragmentManager fm;
    private QuickAction mItemClickQuickAction;
    private int mPosition;
    private ViewHolder holder;

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
        notifyDataSetChanged();
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
        if (fileInfoList != null) {
            fileInfoList.clear();
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final FileLoadInfo fileInfo = (fileInfoList == null) ? null : fileInfoList.get(position);
        if (convertView == null) {
            holder = new ViewHolder(context, fm);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_game_load_finished, parent, false);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.img_1);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_state = (TextView) convertView.findViewById(tv_state);
            holder.tv_size = (TextView) convertView.findViewById(R.id.tv_length);
            holder.more_bt = (ImageView) convertView.findViewById(R.id.manager_installed_more_bt);
            holder.progressBar = (GameLoadProgressBar) convertView.findViewById(R.id.progress_bar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (fileInfo != null) {
            holder.update(fileInfo);
            holder.more_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPosition = position;
                    if (null != mItemClickQuickAction) {
                        mItemClickQuickAction.show(v);
                    }
                }
            });
        }
        return convertView;
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
        private FileLoadInfo fileInfo;

        private SimpleDraweeView img;
        private ImageView more_bt;
        private TextView tv_title, tv_state, tv_size;
        private GameLoadProgressBar progressBar;    //下载进度条
        private IFileLoad fileLoad;

        public ViewHolder(Context context, FragmentManager fm) {
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
                            GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(fileInfo.getName(), fileInfo.getUrl(),
                                    fileInfo.getPackageName(), fileInfo.getVersionCode());
                            if (fileStatus == null) {
                                return;
                            }
                            int status = fileStatus.getStatus();
                            if (status == GameFileStatus.STATE_HAS_INSTALL) {
                                tv_size.setVisibility(View.INVISIBLE);
                                tv_state.setText("");
                                progressBar.setLoadState(fileStatus);
                                progressBar.setVisibility(View.VISIBLE);
                            } else if (status == GameFileStatus.STATE_HAS_DOWNLOAD) {//安装
                                tv_size.setVisibility(View.VISIBLE);
                                tv_state.setText("下载完成");
                                progressBar.setLoadState(fileStatus);
                                progressBar.setVisibility(View.VISIBLE);
                            } else {
                                tv_size.setVisibility(View.INVISIBLE);
                                tv_state.setText("已安装");
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }, 0, 300);
        }

        public void update(final FileLoadInfo fileInfo) {
            //this.fileInfo = fileInfo;
            String gameName = fileInfo.getTitle();
            if (null != gameName) {
                tv_title.setText(gameName);
            } else {
                tv_title.setText("");
            }
            tv_size.setText(TextUtil.formatFileSize(fileInfo.getLength()));

            //设置进度条状态
            GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(fileInfo.getName(), fileInfo.getUrl(),
                    fileInfo.getPackageName(), fileInfo.getVersionCode());
            int status = fileStatus.getStatus();
            Log.d("777", gameName + "update:status " + status);
            if (status == GameFileStatus.STATE_HAS_INSTALL) {
                tv_size.setVisibility(View.INVISIBLE);
                tv_state.setText("");
                progressBar.setLoadState(fileStatus);
                progressBar.setVisibility(View.VISIBLE);
            } else if (status == GameFileStatus.STATE_HAS_DOWNLOAD) {//安装
                tv_size.setVisibility(View.VISIBLE);
                tv_state.setText("下载完成");
                progressBar.setLoadState(fileStatus);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                tv_size.setVisibility(View.INVISIBLE);
                tv_state.setText("");
                progressBar.setVisibility(View.INVISIBLE);
            }
            //必须设置，否则点击进度条后无法进行响应操作
            progressBar.setFileLoadInfo(fileInfo);
            progressBar.setOnStateChangeListener(new ProgressBarStateListener(context, fm));
            progressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.toggle();
                }
            });

            //加载图片
            img.setImageURI(fileInfo.getPreviewUrl());
        }


    }

}














