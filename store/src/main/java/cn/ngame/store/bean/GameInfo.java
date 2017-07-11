package cn.ngame.store.bean;

import java.io.Serializable;
import java.util.List;

import cn.ngame.store.game.bean.GameStrategy;
import cn.ngame.store.view.DownLoadProgressBar;

/**
 * 游戏软件的实体类
 * Created by zeng on 2016/5/16.
 */
public class GameInfo implements Serializable {
    public static final String TAG = GameInfo.class.getSimpleName();
    public long id;
    /**名称*/
    public String gameName;
    /**预览图片地址*/
    public String gameLogo;
    /**文件下载地址*/
    public String gameLink;
    /**文件大小*/
    public long gameSize;
    /**下载量*/
    public int downloadCount;
    /**评级分数*/
    public int scoreLevel;
    /**是否是精选*/
    public int gameSelected;
    /**简介*/
    public String gameDesc;
    /**版本号*/
    public String versionCode;
    /**版本名称*/
    public String versionName;

    /** 游戏开发商*/
    public List<GameAgent> gameAgentList;
    /**文件的MD5*/
    public String md5;
    /**游戏文件名*/
    public String filename;
    /**排序ID*/
    public String orderNo;
    /**程序包名*/
    public String packages;

    /**操作类型*/
    public String operation;

    /** 游戏所属类型*/
    public List<GameType> gameTypeList;
    /**更新时间*/
    public long updateTime;
    /**游戏总评分*/
    public float percentage;
    /** 总评论人数*/
    public int commentPeople;
    /** 游戏攻略 */
    public GameStrategy gameStrategy;
    /** 游戏详情图片*/
    public List<GameImage> gameDetailsImages;
    /** 游戏键位图片*/
    public List<GameImage> gameKeyMapsImages;
    /** 游戏键位描述 */
    public List<GameKey> gameKeyDescList;

    /**
     * 游戏评分汇总
     */
    public List<QuestionResult> questionResults;

}
