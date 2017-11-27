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

package cn.ngame.store.activity.hub;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import cn.ngame.store.R;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.IFileLoad;

public class GameHubNewAdapter extends BaseAdapter {

    private Context context;
    private FragmentManager fm;
    private List<GameHubMainBean.DataBean> list;
    private int type;
    private static Handler uiHandler = new Handler();

    public GameHubNewAdapter(Context context, FragmentManager fm, List<GameHubMainBean.DataBean> list, int type) {
        super();
        this.context = context;
        this.fm = fm;
        this.list = list;
        this.type = type;
    }

    public void setList(List<GameHubMainBean.DataBean> list) {
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
        final GameHubMainBean.DataBean gameInfo = (list == null) ? null : list.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.game_hub_new_lv_item, parent, false);
            holder = new ViewHolder(context, fm);
            holder.fromLogoIv = (SimpleDraweeView) convertView.findViewById(R.id.img_1);
            holder.game_logo_Iv = (SimpleDraweeView) convertView.findViewById(R.id.recommend_game_pic_new);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
            holder.timeTv = (TextView) convertView.findViewById(R.id.game_hub_time_tv);
            holder.tv_from = (TextView) convertView.findViewById(R.id.text1);
            holder.lookNub = (TextView) convertView.findViewById(R.id.game_hub_look_nub_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (gameInfo != null) {
            holder.update(gameInfo, type, position);
        }

        return convertView;
    }

    public static final String TAG = GameHubFragment.class.getSimpleName();

    public static class ViewHolder {
        private Context context;
        private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        private GameHubMainBean.DataBean gameInfo;
        private SimpleDraweeView fromLogoIv;
        private TextView tv_title, timeTv, lookNub, tv_summary, tv_from;
        private IFileLoad fileLoad;
        private FragmentManager fm;
        private Timer timer = new Timer();
        public SimpleDraweeView game_logo_Iv;

        public ViewHolder(Context context, FragmentManager fm) {
            this.context = context;
            this.fm = fm;
            fileLoad = FileLoadManager.getInstance(context);
        }

        /**
         * 更新普通数据
         *
         * @param gameInfo 游戏信息
         */
        public void update(final GameHubMainBean.DataBean gameInfo, int type, int position) {
            this.gameInfo = gameInfo;
            String imgUrl = gameInfo.getPostImage();
            if (imgUrl != null && !imgUrl.trim().equals("")) {
            String[] typeNameArray = imgUrl.split("\\,");
            if (typeNameArray != null && typeNameArray.length > 0) {
                game_logo_Iv.setImageURI(typeNameArray[0]);
            }
            }
            //fromLogoIv.setImageURI(gameInfo.getHeadPhoto());
            String gameName = gameInfo.getPostTitle();
            if (!"".equals(gameName)) {
                tv_title.setText(gameName);
            }

            String gameDesc = gameInfo.getPostContent();
            if (gameDesc != null && !"".equals(gameDesc)) {
                tv_summary.setText(gameDesc);
            } else {
                tv_summary.setText("");
            }
            timeTv.setText(df.format(new Date(gameInfo.getUpdateTime())));
            lookNub.setText(gameInfo.getWatchNum() + "");
            tv_from.setText(gameInfo.getPostPublisher());
        }
    }
}