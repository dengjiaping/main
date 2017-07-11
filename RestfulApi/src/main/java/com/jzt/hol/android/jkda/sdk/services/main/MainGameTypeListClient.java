package com.jzt.hol.android.jkda.sdk.services.main;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.main.MainGameTypeListBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBodyBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;
import com.jzt.hol.android.jkda.sdk.services.gamehub.PostMsgBaseClient;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class MainGameTypeListClient extends PostMsgBaseClient<MainGameTypeListBean> {
    YunduanBodyBean bean;

    public MainGameTypeListClient(Context cxt, YunduanBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<MainGameTypeListBean> requestService(GameService askDoctorService) {
        return askDoctorService.queryHomeGame(HostDebug.AppJson, bean);
    }
}
