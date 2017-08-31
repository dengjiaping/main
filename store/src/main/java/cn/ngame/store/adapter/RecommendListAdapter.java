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
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBean;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;

import cn.ngame.store.R;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.utils.ImageUtil;

public class RecommendListAdapter extends BaseAdapter {

    private Context context;
    private FragmentManager fm;
    private List<RecommendListBean.DataBean> list;
    private int type;

    private static Handler uiHandler = new Handler();

    public RecommendListAdapter(Context context, FragmentManager fm, List<RecommendListBean.DataBean> list, int type) {
        super();
        this.context = context;
        this.fm = fm;
        this.list = list;
        this.type = type;
    }

    public void setList(List<RecommendListBean.DataBean> list) {
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
        final RecommendListBean.DataBean gameInfo = (list == null) ? null : list.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recommend_list_item, parent, false);
            holder = new ViewHolder(context, fm);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.img_1);
            holder.recommend_game_pic = (ImageView) convertView.findViewById(R.id.recommend_game_pic);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
            holder.tv_from = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (gameInfo != null) {
            holder.update(gameInfo, type, position);
        }

        return convertView;
    }

    public static class ViewHolder {
        private Context context;
        private RecommendListBean.DataBean gameInfo;
        private SimpleDraweeView img;
        private TextView tv_title, tv_summary, tv_from;
        private IFileLoad fileLoad;
        private FragmentManager fm;
        private Timer timer = new Timer();
        public ImageView recommend_game_pic;

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
        public void update(final RecommendListBean.DataBean gameInfo, int type, int position) {
            this.gameInfo = gameInfo;
            String imgUrl = gameInfo.getGameRecommendImg();
            String fromUrl = gameInfo.getGameLogo();
            if (imgUrl != null && imgUrl.trim().equals("")) {
                imgUrl = null;
            }
            int screenWidth = ImageUtil.getScreenWidth((Activity) context);
            Picasso.with(context).load(imgUrl).placeholder(R.drawable.ic_def_logo_720_288)
                    .error(R.drawable.ic_def_logo_720_288)
                    //.resize(screenWidth,150)
                    .into(recommend_game_pic);
            Picasso.with(context).load(fromUrl).placeholder(R.drawable.ic_def_logo_720_288)
                    .error(R.drawable.ic_def_logo_720_288)
                    //.resize(screenWidth,150)
                    .into(img);

            String gameName = gameInfo.getGameName();
            if (!"".equals(gameName)) {
                tv_title.setText(gameName);
            }

            String gameDesc = gameInfo.getRecommend();
            if (gameDesc != null && !"".equals(gameDesc)) {
                tv_summary.setText(gameDesc);
            } else {
                tv_summary.setText("");
            }

            String gameRecommender = gameInfo.getRecommender();
            //String gameSizeStr = TextUtil.formatFileSize(gameSize);
            tv_from.setText("来自" + gameRecommender);
        }
    }
}