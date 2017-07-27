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
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.view.GameLoadProgressBar;

import static cn.ngame.store.R.id.tv_title;

public class HomeGameTypeListAdapter extends BaseAdapter {

    private Context context;
    private FragmentManager fm;
    private List<GameInfo> list;
    private IFileLoad fileLoad;

    private Timer timer = new Timer();
    private static Handler uiHandler = new Handler();

    public HomeGameTypeListAdapter(Context context, FragmentManager fm, List<GameInfo> list) {
        super();
        this.context = context;
        this.fm = fm;
        this.list = list;
    }

    public void setList(List<GameInfo> list) {
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.home_game_type_item, parent, false);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img_1);
            holder.tv_title = (TextView) convertView.findViewById(tv_title);
            holder.progressBar = (GameLoadProgressBar) convertView.findViewById(R.id.progress_bar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GameInfo gameInfo = list.get(position);

        fileLoad = FileLoadManager.getInstance(context);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(gameInfo.filename, gameInfo.gameLink,
                                gameInfo.packages, ConvUtil.NI(gameInfo.versionCode));
                        holder.progressBar.setLoadState(fileStatus);
                    }
                });
            }
        }, 0, 500);

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
                .into(holder.img);

        String gameName = gameInfo.gameName;
        if (!"".equals(gameName)) {
            holder.tv_title.setText(gameName);
        }

        //设置进度条状态
        holder.progressBar.setLoadState(fileLoad.getGameFileLoadStatus(gameInfo.filename, gameInfo.gameLink, gameInfo.packages,
                ConvUtil.NI(gameInfo.versionCode)));
        //必须设置，否则点击进度条后无法进行响应操作
        FileLoadInfo fileLoadInfo = new FileLoadInfo(gameInfo.filename, gameInfo.gameLink, gameInfo.md5, ConvUtil.NI(gameInfo
                .versionCode), gameInfo.gameName, gameInfo.gameLogo, gameInfo.id, FileLoadInfo.TYPE_GAME);
        fileLoadInfo.setPackageName(gameInfo.packages);
        holder.progressBar.setFileLoadInfo(fileLoadInfo);
        holder.progressBar.setOnStateChangeListener(new ProgressBarStateListener(context, fm));
        holder.progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.progressBar.toggle();
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        private ImageView img;
        private TextView tv_title;
        private GameLoadProgressBar progressBar;    //下载进度条
    }
}














