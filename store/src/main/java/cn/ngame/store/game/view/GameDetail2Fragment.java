package cn.ngame.store.game.view;

import android.os.Bundle;
import android.view.View;

import cn.ngame.store.R;
import cn.ngame.store.base.fragment.BaseSearchFragment;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class GameDetail2Fragment extends BaseSearchFragment{


    public static GameDetail2Fragment newInstance() {
        GameDetail2Fragment fragment = new GameDetail2Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.game_detail2_fragment;
    }

    @Override
    protected void initViewsAndEvents(View view) {

    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadView(View view) {
        return null;
    }
}
