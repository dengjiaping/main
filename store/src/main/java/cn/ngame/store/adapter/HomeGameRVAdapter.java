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
import cn.ngame.store.bean.GameType;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.video.view.VideoDetailActivity;

/**
 * RecycleView 适配器
 * Created by zeng on 2016/7/18.
 */
public class HomeGameRVAdapter extends RecyclerView.Adapter<HomeGameRVAdapter.MyViewHolder> {

    private List<GameType> gameTypeList;
    private Context context;

    public HomeGameRVAdapter(Context context) {
        this.context = context;
    }

    public void setDate(List<GameType> GameTypes){
        if(gameTypeList != null && GameTypes.size() > 0){
            gameTypeList.addAll(GameTypes);
        }else {
            gameTypeList = GameTypes;
        }
    }

    public void clean(){

        if(gameTypeList != null){
            gameTypeList.clear();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_game_rv,parent,false));

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.update(gameTypeList.get(position));
    }

    @Override
    public int getItemCount() {
        if(gameTypeList != null){
            return gameTypeList.size();
        }
        return 0;
    }


    class MyViewHolder extends ViewHolder{

        private TextView tv_title;
        private ImageView img;
        private GameType gameType;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.text1);
            img = (ImageView) itemView.findViewById(R.id.img_1);
        }

        public void update(final GameType info){
            gameType = info;

            String title = gameType.typeName == null ? "" : gameType.typeName;
            //title = title.length() > 10 ? title.substring(0, 10) : title;
            tv_title.setText(title);

            String imgUrl = gameType.logoUrl;
            if(imgUrl != null && imgUrl.trim().equals("")){
                imgUrl = null;
            }
            Picasso.with(context)
                    .load(imgUrl)
                    .placeholder(R.drawable.default_video)
                    .error(R.drawable.default_video)
                    .resize(CommonUtil.dip2px(context,50),CommonUtil.dip2px(context,50))
                    .centerCrop()
                    .tag(context)
                    .into(img);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VideoDetailActivity.class);
                    intent.putExtra("id",info.id);
                    context.startActivity(intent);
                }
            });
        }
    }
}
