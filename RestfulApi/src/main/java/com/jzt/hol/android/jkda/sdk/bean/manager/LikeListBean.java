package com.jzt.hol.android.jkda.sdk.bean.manager;

import java.io.Serializable;
import java.util.List;

/**
 * 游戏列表
 * Created by gp on 2017/3/18 0018.
 */

public class LikeListBean implements Serializable{

    /**
     * code : 0
     * msg : id表示游戏id，gameName表示游戏名称，gameLogo表示游戏logo，gameLink表示游戏下载链接，scoreLevel表示游戏评分，cName表示游戏分类汇总
     * data : {"gameList":[{"id":146,"cName":"","gameName":"王者荣耀","gameLogo":"http://oss.ngame.cn/upload/1466667978284.png",
     * "scoreLevel":0,"gameLink":"http://oss.ngame.cn/games/hbwy/%E7%8E%8B%E8%80%85%E8%8D%A3%E8%80%80_20170209.apk",
     * "gameSize":785263045,"versionCode":17011509,"packages":"com.tencent.tmgp.sgame","filename":"1469244345731.apk",
     * "md5":"18C7EFBC3105B7B844F728BC969A4C30","versionName":"1.17.1.15","updateTime":1500446138000}]}
     * map : null
     * innerResult : null
     */

    private int code;
    private String msg;
    private DataBean data;
    private Object map;
    private Object innerResult;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getMap() {
        return map;
    }

    public void setMap(Object map) {
        this.map = map;
    }

    public Object getInnerResult() {
        return innerResult;
    }

    public void setInnerResult(Object innerResult) {
        this.innerResult = innerResult;
    }

    public static class DataBean {
        private List<GameListBean> gameList;

        public List<GameListBean> getGameList() {
            return gameList;
        }

        public void setGameList(List<GameListBean> gameList) {
            this.gameList = gameList;
        }

        public static class GameListBean {
            /**
             * id : 146
             * cName :
             * gameName : 王者荣耀
             * gameLogo : http://oss.ngame.cn/upload/1466667978284.png
             * scoreLevel : 0
             * gameLink : http://oss.ngame.cn/games/hbwy/%E7%8E%8B%E8%80%85%E8%8D%A3%E8%80%80_20170209.apk
             * gameSize : 785263045
             * versionCode : 17011509
             * packages : com.tencent.tmgp.sgame
             * filename : 1469244345731.apk
             * md5 : 18C7EFBC3105B7B844F728BC969A4C30
             * versionName : 1.17.1.15
             * updateTime : 1500446138000
             */

            private int id;
            private String cName;
            private String gameName;
            private String gameLogo;
            private int scoreLevel;
            private String gameLink;
            private int gameSize;
            private int versionCode;
            private String packages;
            private String filename;
            private String md5;
            private String versionName;
            private long updateTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCName() {
                return cName;
            }

            public void setCName(String cName) {
                this.cName = cName;
            }

            public String getGameName() {
                return gameName;
            }

            public void setGameName(String gameName) {
                this.gameName = gameName;
            }

            public String getGameLogo() {
                return gameLogo;
            }

            public void setGameLogo(String gameLogo) {
                this.gameLogo = gameLogo;
            }

            public int getScoreLevel() {
                return scoreLevel;
            }

            public void setScoreLevel(int scoreLevel) {
                this.scoreLevel = scoreLevel;
            }

            public String getGameLink() {
                return gameLink;
            }

            public void setGameLink(String gameLink) {
                this.gameLink = gameLink;
            }

            public int getGameSize() {
                return gameSize;
            }

            public void setGameSize(int gameSize) {
                this.gameSize = gameSize;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getPackages() {
                return packages;
            }

            public void setPackages(String packages) {
                this.packages = packages;
            }

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getMd5() {
                return md5;
            }

            public void setMd5(String md5) {
                this.md5 = md5;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
