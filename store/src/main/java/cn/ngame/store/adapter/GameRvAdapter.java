package cn.ngame.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.video.view.VideoDetailActivity;

/**
 * RecycleView 适配器
 * Created by zeng on 2016/7/18.
 */
public class GameRvAdapter extends RecyclerView.Adapter<GameRvAdapter.MyViewHolder> {

    private List<GameInfo> gameInfoList;
    private Context context;

    public GameRvAdapter(Context context) {
        this.context = context;
    }

    public void setDate(List<GameInfo> gameInfos){
        /*if(gameInfoList != null && gameInfos.size() > 0){
            gameInfoList.addAll(gameInfos);
        }else {
            gameInfoList = gameInfos;
        }*/

        gameInfoList = gameInfos;
    }

    public void clean(){

        if(gameInfoList != null){
            gameInfoList.clear();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_game,parent,false));

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.update(gameInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        if(gameInfoList != null){
            return gameInfoList.size();
        }
        return 0;
    }


    class MyViewHolder extends ViewHolder{

        private TextView tv_title,tv_count;
        private ImageView img;
        private GameInfo gameInfo;

        MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            img = (ImageView) itemView.findViewById(R.id.img_1);
        }

        public void update(final GameInfo info){
            gameInfo = info;

            String title = gameInfo.gameName == null ? "" : gameInfo.gameName;
            tv_title.setText(title);

            int count = gameInfo.downloadCount;
            tv_count.setText(TextUtil.formatCount(count)+"人下载");

            String imgUrl = gameInfo.gameLogo;
            if(imgUrl != null && imgUrl.trim().equals("")){
                imgUrl = null;
            }
            Picasso.with(context)
                    .load(imgUrl)
                    .placeholder(R.drawable.default_video)
                    .error(R.drawable.default_video)
                    //.resizeDimen(R.dimen.game_image_size, R.dimen.game_image_size)
                    //.resize(640,360)
                    .fit()
                    .centerCrop()
                    .tag(context)
                    .into(img);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GameDetailActivity.class);
                    intent.putExtra("id",info.id);
                    context.startActivity(intent);
                }
            });
        }
    }
}
