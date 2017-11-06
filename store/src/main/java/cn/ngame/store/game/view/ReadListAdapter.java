package cn.ngame.store.game.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.game.bean.GameStrategy;

import static cn.ngame.store.R.id.strategy_content_tv;

/**
 * Created by liguoliang
 * on 2017/11/6 0006.
 */
public class ReadListAdapter extends BaseAdapter {
    public static final String TAG = GameReadFragment.class.getSimpleName();
    private List<GameStrategy> gameStrategyList;
    private Activity context;

    public ReadListAdapter(Activity context, List<GameStrategy> gameStrategyList) {
        this.gameStrategyList = gameStrategyList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return gameStrategyList == null ? 0 : gameStrategyList.size();
    }

    @Override
    public Object getItem(int position) {
        return gameStrategyList == null ? null : gameStrategyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ReadListAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new ReadListAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_game_strategy, parent, false);
            holder.tv_title = (TextView) convertView.findViewById(R.id.strategy_title_tv);
            holder.strategy_content_tv = (TextView) convertView.findViewById(strategy_content_tv);
            convertView.setTag(holder);
        } else {
            holder = (ReadListAdapter.ViewHolder) convertView.getTag();
        }

        GameStrategy gameStrategy = gameStrategyList.get(position);
        if (gameStrategy != null) {
            holder.tv_title.setText(gameStrategy.getStrategyTitle());
            holder.strategy_content_tv.setText(gameStrategy.getStrategyContent());
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView tv_title, strategy_content_tv;
    }
}
