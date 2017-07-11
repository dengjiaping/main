package com.jzt.hol.android.jkda.sdk.bean.main;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class MainGameTypeListBean {

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
        private List<NewGameListBean> newGameList;
        private List<KUNLUNGameListBean> KUNLUNGameList;
        private List<SHENGLIGameListBean> SHENGLIGameList;
        private List<WANGYIGameListBean> WANGYIGameList;
        private List<HUYUGameListBean> HUYUGameList;
        private List<MOBAGameListBean> MOBAGameList;
        private List<DailyRecommendedListBean> dailyRecommendedList;
        private List<TENCENTGameListBean> TENCENTGameList;
        private List<GunGameListBean> gunGameList;
        private List<ZHANGQUGameListBean> ZHANGQUGameList;

        public List<NewGameListBean> getNewGameList() {
            return newGameList;
        }

        public void setNewGameList(List<NewGameListBean> newGameList) {
            this.newGameList = newGameList;
        }

        public List<KUNLUNGameListBean> getKUNLUNGameList() {
            return KUNLUNGameList;
        }

        public void setKUNLUNGameList(List<KUNLUNGameListBean> KUNLUNGameList) {
            this.KUNLUNGameList = KUNLUNGameList;
        }

        public List<SHENGLIGameListBean> getSHENGLIGameList() {
            return SHENGLIGameList;
        }

        public void setSHENGLIGameList(List<SHENGLIGameListBean> SHENGLIGameList) {
            this.SHENGLIGameList = SHENGLIGameList;
        }

        public List<WANGYIGameListBean> getWANGYIGameList() {
            return WANGYIGameList;
        }

        public void setWANGYIGameList(List<WANGYIGameListBean> WANGYIGameList) {
            this.WANGYIGameList = WANGYIGameList;
        }

        public List<HUYUGameListBean> getHUYUGameList() {
            return HUYUGameList;
        }

        public void setHUYUGameList(List<HUYUGameListBean> HUYUGameList) {
            this.HUYUGameList = HUYUGameList;
        }

        public List<MOBAGameListBean> getMOBAGameList() {
            return MOBAGameList;
        }

        public void setMOBAGameList(List<MOBAGameListBean> MOBAGameList) {
            this.MOBAGameList = MOBAGameList;
        }

        public List<DailyRecommendedListBean> getDailyRecommendedList() {
            return dailyRecommendedList;
        }

        public void setDailyRecommendedList(List<DailyRecommendedListBean> dailyRecommendedList) {
            this.dailyRecommendedList = dailyRecommendedList;
        }

        public List<TENCENTGameListBean> getTENCENTGameList() {
            return TENCENTGameList;
        }

        public void setTENCENTGameList(List<TENCENTGameListBean> TENCENTGameList) {
            this.TENCENTGameList = TENCENTGameList;
        }

        public List<GunGameListBean> getGunGameList() {
            return gunGameList;
        }

        public void setGunGameList(List<GunGameListBean> gunGameList) {
            this.gunGameList = gunGameList;
        }

        public List<ZHANGQUGameListBean> getZHANGQUGameList() {
            return ZHANGQUGameList;
        }

        public void setZHANGQUGameList(List<ZHANGQUGameListBean> ZHANGQUGameList) {
            this.ZHANGQUGameList = ZHANGQUGameList;
        }

        public static class NewGameListBean {

            private int id;
            private String gameName;
            private String gameLogo;
            private String gameVersion;
            private String gameLink;
            private int gameSize;
            private String gameDesc;
            private Object gameInfo;
            private int gameSelected;
            private String packages;
            private long updateTime;
            private int orderNo;
            private String filename;
            private String md5;
            private String uploadId;
            private int scoreLevel;
            private int downloadCount;
            private int versionCode;
            private String versionName;
            private double percentage;
            private int isDelete;
            private long createTime;
            private int commentPeople;
            private int appTypeId;
            private int isHand;
            private int isVR;
            private int isHeadControl;
            private int isTouchScreen;
            private String simpleLabel;
            private String urlSchema;
            private int iosCompany;
            private Object gameAgentList;
            private Object gameDetailsImages;
            private Object gameKeyMapsImages;
            private Object questionResults;
            private Object smallOrderNo;
            private Object smallOrderNo1;
            private Object smallOrderNo2;
            private Object smallOrderNo3;
            private Object gameKeyDescList;
            private Object operation;
            private Object gameTypeList;
            private Object gameStrategy;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(String gameVersion) {
                this.gameVersion = gameVersion;
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

            public String getGameDesc() {
                return gameDesc;
            }

            public void setGameDesc(String gameDesc) {
                this.gameDesc = gameDesc;
            }

            public Object getGameInfo() {
                return gameInfo;
            }

            public void setGameInfo(Object gameInfo) {
                this.gameInfo = gameInfo;
            }

            public int getGameSelected() {
                return gameSelected;
            }

            public void setGameSelected(int gameSelected) {
                this.gameSelected = gameSelected;
            }

            public String getPackages() {
                return packages;
            }

            public void setPackages(String packages) {
                this.packages = packages;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
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

            public String getUploadId() {
                return uploadId;
            }

            public void setUploadId(String uploadId) {
                this.uploadId = uploadId;
            }

            public int getScoreLevel() {
                return scoreLevel;
            }

            public void setScoreLevel(int scoreLevel) {
                this.scoreLevel = scoreLevel;
            }

            public int getDownloadCount() {
                return downloadCount;
            }

            public void setDownloadCount(int downloadCount) {
                this.downloadCount = downloadCount;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public double getPercentage() {
                return percentage;
            }

            public void setPercentage(double percentage) {
                this.percentage = percentage;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getCommentPeople() {
                return commentPeople;
            }

            public void setCommentPeople(int commentPeople) {
                this.commentPeople = commentPeople;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public int getIsHand() {
                return isHand;
            }

            public void setIsHand(int isHand) {
                this.isHand = isHand;
            }

            public int getIsVR() {
                return isVR;
            }

            public void setIsVR(int isVR) {
                this.isVR = isVR;
            }

            public int getIsHeadControl() {
                return isHeadControl;
            }

            public void setIsHeadControl(int isHeadControl) {
                this.isHeadControl = isHeadControl;
            }

            public int getIsTouchScreen() {
                return isTouchScreen;
            }

            public void setIsTouchScreen(int isTouchScreen) {
                this.isTouchScreen = isTouchScreen;
            }

            public String getSimpleLabel() {
                return simpleLabel;
            }

            public void setSimpleLabel(String simpleLabel) {
                this.simpleLabel = simpleLabel;
            }

            public String getUrlSchema() {
                return urlSchema;
            }

            public void setUrlSchema(String urlSchema) {
                this.urlSchema = urlSchema;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }

            public Object getGameAgentList() {
                return gameAgentList;
            }

            public void setGameAgentList(Object gameAgentList) {
                this.gameAgentList = gameAgentList;
            }

            public Object getGameDetailsImages() {
                return gameDetailsImages;
            }

            public void setGameDetailsImages(Object gameDetailsImages) {
                this.gameDetailsImages = gameDetailsImages;
            }

            public Object getGameKeyMapsImages() {
                return gameKeyMapsImages;
            }

            public void setGameKeyMapsImages(Object gameKeyMapsImages) {
                this.gameKeyMapsImages = gameKeyMapsImages;
            }

            public Object getQuestionResults() {
                return questionResults;
            }

            public void setQuestionResults(Object questionResults) {
                this.questionResults = questionResults;
            }

            public Object getSmallOrderNo() {
                return smallOrderNo;
            }

            public void setSmallOrderNo(Object smallOrderNo) {
                this.smallOrderNo = smallOrderNo;
            }

            public Object getSmallOrderNo1() {
                return smallOrderNo1;
            }

            public void setSmallOrderNo1(Object smallOrderNo1) {
                this.smallOrderNo1 = smallOrderNo1;
            }

            public Object getSmallOrderNo2() {
                return smallOrderNo2;
            }

            public void setSmallOrderNo2(Object smallOrderNo2) {
                this.smallOrderNo2 = smallOrderNo2;
            }

            public Object getSmallOrderNo3() {
                return smallOrderNo3;
            }

            public void setSmallOrderNo3(Object smallOrderNo3) {
                this.smallOrderNo3 = smallOrderNo3;
            }

            public Object getGameKeyDescList() {
                return gameKeyDescList;
            }

            public void setGameKeyDescList(Object gameKeyDescList) {
                this.gameKeyDescList = gameKeyDescList;
            }

            public Object getOperation() {
                return operation;
            }

            public void setOperation(Object operation) {
                this.operation = operation;
            }

            public Object getGameTypeList() {
                return gameTypeList;
            }

            public void setGameTypeList(Object gameTypeList) {
                this.gameTypeList = gameTypeList;
            }

            public Object getGameStrategy() {
                return gameStrategy;
            }

            public void setGameStrategy(Object gameStrategy) {
                this.gameStrategy = gameStrategy;
            }
        }

        public static class KUNLUNGameListBean {

            private int id;
            private String gameName;
            private String gameLogo;
            private String gameVersion;
            private String gameLink;
            private int gameSize;
            private String gameDesc;
            private Object gameInfo;
            private int gameSelected;
            private String packages;
            private long updateTime;
            private int orderNo;
            private String filename;
            private String md5;
            private String uploadId;
            private int scoreLevel;
            private int downloadCount;
            private int versionCode;
            private String versionName;
            private int percentage;
            private int isDelete;
            private long createTime;
            private int commentPeople;
            private int appTypeId;
            private int isHand;
            private int isVR;
            private int isHeadControl;
            private int isTouchScreen;
            private String simpleLabel;
            private String urlSchema;
            private int iosCompany;
            private Object gameAgentList;
            private Object gameDetailsImages;
            private Object gameKeyMapsImages;
            private Object questionResults;
            private Object smallOrderNo;
            private Object smallOrderNo1;
            private Object smallOrderNo2;
            private Object smallOrderNo3;
            private Object gameKeyDescList;
            private Object operation;
            private Object gameTypeList;
            private Object gameStrategy;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(String gameVersion) {
                this.gameVersion = gameVersion;
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

            public String getGameDesc() {
                return gameDesc;
            }

            public void setGameDesc(String gameDesc) {
                this.gameDesc = gameDesc;
            }

            public Object getGameInfo() {
                return gameInfo;
            }

            public void setGameInfo(Object gameInfo) {
                this.gameInfo = gameInfo;
            }

            public int getGameSelected() {
                return gameSelected;
            }

            public void setGameSelected(int gameSelected) {
                this.gameSelected = gameSelected;
            }

            public String getPackages() {
                return packages;
            }

            public void setPackages(String packages) {
                this.packages = packages;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
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

            public String getUploadId() {
                return uploadId;
            }

            public void setUploadId(String uploadId) {
                this.uploadId = uploadId;
            }

            public int getScoreLevel() {
                return scoreLevel;
            }

            public void setScoreLevel(int scoreLevel) {
                this.scoreLevel = scoreLevel;
            }

            public int getDownloadCount() {
                return downloadCount;
            }

            public void setDownloadCount(int downloadCount) {
                this.downloadCount = downloadCount;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public int getPercentage() {
                return percentage;
            }

            public void setPercentage(int percentage) {
                this.percentage = percentage;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getCommentPeople() {
                return commentPeople;
            }

            public void setCommentPeople(int commentPeople) {
                this.commentPeople = commentPeople;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public int getIsHand() {
                return isHand;
            }

            public void setIsHand(int isHand) {
                this.isHand = isHand;
            }

            public int getIsVR() {
                return isVR;
            }

            public void setIsVR(int isVR) {
                this.isVR = isVR;
            }

            public int getIsHeadControl() {
                return isHeadControl;
            }

            public void setIsHeadControl(int isHeadControl) {
                this.isHeadControl = isHeadControl;
            }

            public int getIsTouchScreen() {
                return isTouchScreen;
            }

            public void setIsTouchScreen(int isTouchScreen) {
                this.isTouchScreen = isTouchScreen;
            }

            public String getSimpleLabel() {
                return simpleLabel;
            }

            public void setSimpleLabel(String simpleLabel) {
                this.simpleLabel = simpleLabel;
            }

            public String getUrlSchema() {
                return urlSchema;
            }

            public void setUrlSchema(String urlSchema) {
                this.urlSchema = urlSchema;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }

            public Object getGameAgentList() {
                return gameAgentList;
            }

            public void setGameAgentList(Object gameAgentList) {
                this.gameAgentList = gameAgentList;
            }

            public Object getGameDetailsImages() {
                return gameDetailsImages;
            }

            public void setGameDetailsImages(Object gameDetailsImages) {
                this.gameDetailsImages = gameDetailsImages;
            }

            public Object getGameKeyMapsImages() {
                return gameKeyMapsImages;
            }

            public void setGameKeyMapsImages(Object gameKeyMapsImages) {
                this.gameKeyMapsImages = gameKeyMapsImages;
            }

            public Object getQuestionResults() {
                return questionResults;
            }

            public void setQuestionResults(Object questionResults) {
                this.questionResults = questionResults;
            }

            public Object getSmallOrderNo() {
                return smallOrderNo;
            }

            public void setSmallOrderNo(Object smallOrderNo) {
                this.smallOrderNo = smallOrderNo;
            }

            public Object getSmallOrderNo1() {
                return smallOrderNo1;
            }

            public void setSmallOrderNo1(Object smallOrderNo1) {
                this.smallOrderNo1 = smallOrderNo1;
            }

            public Object getSmallOrderNo2() {
                return smallOrderNo2;
            }

            public void setSmallOrderNo2(Object smallOrderNo2) {
                this.smallOrderNo2 = smallOrderNo2;
            }

            public Object getSmallOrderNo3() {
                return smallOrderNo3;
            }

            public void setSmallOrderNo3(Object smallOrderNo3) {
                this.smallOrderNo3 = smallOrderNo3;
            }

            public Object getGameKeyDescList() {
                return gameKeyDescList;
            }

            public void setGameKeyDescList(Object gameKeyDescList) {
                this.gameKeyDescList = gameKeyDescList;
            }

            public Object getOperation() {
                return operation;
            }

            public void setOperation(Object operation) {
                this.operation = operation;
            }

            public Object getGameTypeList() {
                return gameTypeList;
            }

            public void setGameTypeList(Object gameTypeList) {
                this.gameTypeList = gameTypeList;
            }

            public Object getGameStrategy() {
                return gameStrategy;
            }

            public void setGameStrategy(Object gameStrategy) {
                this.gameStrategy = gameStrategy;
            }
        }

        public static class SHENGLIGameListBean {

            private int id;
            private String gameName;
            private String gameLogo;
            private String gameVersion;
            private String gameLink;
            private int gameSize;
            private String gameDesc;
            private Object gameInfo;
            private int gameSelected;
            private String packages;
            private long updateTime;
            private int orderNo;
            private String filename;
            private String md5;
            private String uploadId;
            private int scoreLevel;
            private int downloadCount;
            private int versionCode;
            private String versionName;
            private double percentage;
            private int isDelete;
            private long createTime;
            private int commentPeople;
            private int appTypeId;
            private int isHand;
            private int isVR;
            private int isHeadControl;
            private int isTouchScreen;
            private String simpleLabel;
            private String urlSchema;
            private int iosCompany;
            private Object gameAgentList;
            private Object gameDetailsImages;
            private Object gameKeyMapsImages;
            private Object questionResults;
            private Object smallOrderNo;
            private Object smallOrderNo1;
            private Object smallOrderNo2;
            private Object smallOrderNo3;
            private Object gameKeyDescList;
            private Object operation;
            private Object gameTypeList;
            private Object gameStrategy;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(String gameVersion) {
                this.gameVersion = gameVersion;
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

            public String getGameDesc() {
                return gameDesc;
            }

            public void setGameDesc(String gameDesc) {
                this.gameDesc = gameDesc;
            }

            public Object getGameInfo() {
                return gameInfo;
            }

            public void setGameInfo(Object gameInfo) {
                this.gameInfo = gameInfo;
            }

            public int getGameSelected() {
                return gameSelected;
            }

            public void setGameSelected(int gameSelected) {
                this.gameSelected = gameSelected;
            }

            public String getPackages() {
                return packages;
            }

            public void setPackages(String packages) {
                this.packages = packages;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
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

            public String getUploadId() {
                return uploadId;
            }

            public void setUploadId(String uploadId) {
                this.uploadId = uploadId;
            }

            public int getScoreLevel() {
                return scoreLevel;
            }

            public void setScoreLevel(int scoreLevel) {
                this.scoreLevel = scoreLevel;
            }

            public int getDownloadCount() {
                return downloadCount;
            }

            public void setDownloadCount(int downloadCount) {
                this.downloadCount = downloadCount;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public double getPercentage() {
                return percentage;
            }

            public void setPercentage(double percentage) {
                this.percentage = percentage;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getCommentPeople() {
                return commentPeople;
            }

            public void setCommentPeople(int commentPeople) {
                this.commentPeople = commentPeople;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public int getIsHand() {
                return isHand;
            }

            public void setIsHand(int isHand) {
                this.isHand = isHand;
            }

            public int getIsVR() {
                return isVR;
            }

            public void setIsVR(int isVR) {
                this.isVR = isVR;
            }

            public int getIsHeadControl() {
                return isHeadControl;
            }

            public void setIsHeadControl(int isHeadControl) {
                this.isHeadControl = isHeadControl;
            }

            public int getIsTouchScreen() {
                return isTouchScreen;
            }

            public void setIsTouchScreen(int isTouchScreen) {
                this.isTouchScreen = isTouchScreen;
            }

            public String getSimpleLabel() {
                return simpleLabel;
            }

            public void setSimpleLabel(String simpleLabel) {
                this.simpleLabel = simpleLabel;
            }

            public String getUrlSchema() {
                return urlSchema;
            }

            public void setUrlSchema(String urlSchema) {
                this.urlSchema = urlSchema;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }

            public Object getGameAgentList() {
                return gameAgentList;
            }

            public void setGameAgentList(Object gameAgentList) {
                this.gameAgentList = gameAgentList;
            }

            public Object getGameDetailsImages() {
                return gameDetailsImages;
            }

            public void setGameDetailsImages(Object gameDetailsImages) {
                this.gameDetailsImages = gameDetailsImages;
            }

            public Object getGameKeyMapsImages() {
                return gameKeyMapsImages;
            }

            public void setGameKeyMapsImages(Object gameKeyMapsImages) {
                this.gameKeyMapsImages = gameKeyMapsImages;
            }

            public Object getQuestionResults() {
                return questionResults;
            }

            public void setQuestionResults(Object questionResults) {
                this.questionResults = questionResults;
            }

            public Object getSmallOrderNo() {
                return smallOrderNo;
            }

            public void setSmallOrderNo(Object smallOrderNo) {
                this.smallOrderNo = smallOrderNo;
            }

            public Object getSmallOrderNo1() {
                return smallOrderNo1;
            }

            public void setSmallOrderNo1(Object smallOrderNo1) {
                this.smallOrderNo1 = smallOrderNo1;
            }

            public Object getSmallOrderNo2() {
                return smallOrderNo2;
            }

            public void setSmallOrderNo2(Object smallOrderNo2) {
                this.smallOrderNo2 = smallOrderNo2;
            }

            public Object getSmallOrderNo3() {
                return smallOrderNo3;
            }

            public void setSmallOrderNo3(Object smallOrderNo3) {
                this.smallOrderNo3 = smallOrderNo3;
            }

            public Object getGameKeyDescList() {
                return gameKeyDescList;
            }

            public void setGameKeyDescList(Object gameKeyDescList) {
                this.gameKeyDescList = gameKeyDescList;
            }

            public Object getOperation() {
                return operation;
            }

            public void setOperation(Object operation) {
                this.operation = operation;
            }

            public Object getGameTypeList() {
                return gameTypeList;
            }

            public void setGameTypeList(Object gameTypeList) {
                this.gameTypeList = gameTypeList;
            }

            public Object getGameStrategy() {
                return gameStrategy;
            }

            public void setGameStrategy(Object gameStrategy) {
                this.gameStrategy = gameStrategy;
            }
        }

        public static class WANGYIGameListBean {

            private int id;
            private String gameName;
            private String gameLogo;
            private String gameVersion;
            private String gameLink;
            private int gameSize;
            private String gameDesc;
            private Object gameInfo;
            private int gameSelected;
            private String packages;
            private long updateTime;
            private int orderNo;
            private String filename;
            private String md5;
            private String uploadId;
            private int scoreLevel;
            private int downloadCount;
            private int versionCode;
            private String versionName;
            private double percentage;
            private int isDelete;
            private long createTime;
            private int commentPeople;
            private int appTypeId;
            private int isHand;
            private int isVR;
            private int isHeadControl;
            private int isTouchScreen;
            private String simpleLabel;
            private String urlSchema;
            private int iosCompany;
            private Object gameAgentList;
            private Object gameDetailsImages;
            private Object gameKeyMapsImages;
            private Object questionResults;
            private Object smallOrderNo;
            private Object smallOrderNo1;
            private Object smallOrderNo2;
            private Object smallOrderNo3;
            private Object gameKeyDescList;
            private Object operation;
            private Object gameTypeList;
            private Object gameStrategy;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(String gameVersion) {
                this.gameVersion = gameVersion;
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

            public String getGameDesc() {
                return gameDesc;
            }

            public void setGameDesc(String gameDesc) {
                this.gameDesc = gameDesc;
            }

            public Object getGameInfo() {
                return gameInfo;
            }

            public void setGameInfo(Object gameInfo) {
                this.gameInfo = gameInfo;
            }

            public int getGameSelected() {
                return gameSelected;
            }

            public void setGameSelected(int gameSelected) {
                this.gameSelected = gameSelected;
            }

            public String getPackages() {
                return packages;
            }

            public void setPackages(String packages) {
                this.packages = packages;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
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

            public String getUploadId() {
                return uploadId;
            }

            public void setUploadId(String uploadId) {
                this.uploadId = uploadId;
            }

            public int getScoreLevel() {
                return scoreLevel;
            }

            public void setScoreLevel(int scoreLevel) {
                this.scoreLevel = scoreLevel;
            }

            public int getDownloadCount() {
                return downloadCount;
            }

            public void setDownloadCount(int downloadCount) {
                this.downloadCount = downloadCount;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public double getPercentage() {
                return percentage;
            }

            public void setPercentage(double percentage) {
                this.percentage = percentage;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getCommentPeople() {
                return commentPeople;
            }

            public void setCommentPeople(int commentPeople) {
                this.commentPeople = commentPeople;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public int getIsHand() {
                return isHand;
            }

            public void setIsHand(int isHand) {
                this.isHand = isHand;
            }

            public int getIsVR() {
                return isVR;
            }

            public void setIsVR(int isVR) {
                this.isVR = isVR;
            }

            public int getIsHeadControl() {
                return isHeadControl;
            }

            public void setIsHeadControl(int isHeadControl) {
                this.isHeadControl = isHeadControl;
            }

            public int getIsTouchScreen() {
                return isTouchScreen;
            }

            public void setIsTouchScreen(int isTouchScreen) {
                this.isTouchScreen = isTouchScreen;
            }

            public String getSimpleLabel() {
                return simpleLabel;
            }

            public void setSimpleLabel(String simpleLabel) {
                this.simpleLabel = simpleLabel;
            }

            public String getUrlSchema() {
                return urlSchema;
            }

            public void setUrlSchema(String urlSchema) {
                this.urlSchema = urlSchema;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }

            public Object getGameAgentList() {
                return gameAgentList;
            }

            public void setGameAgentList(Object gameAgentList) {
                this.gameAgentList = gameAgentList;
            }

            public Object getGameDetailsImages() {
                return gameDetailsImages;
            }

            public void setGameDetailsImages(Object gameDetailsImages) {
                this.gameDetailsImages = gameDetailsImages;
            }

            public Object getGameKeyMapsImages() {
                return gameKeyMapsImages;
            }

            public void setGameKeyMapsImages(Object gameKeyMapsImages) {
                this.gameKeyMapsImages = gameKeyMapsImages;
            }

            public Object getQuestionResults() {
                return questionResults;
            }

            public void setQuestionResults(Object questionResults) {
                this.questionResults = questionResults;
            }

            public Object getSmallOrderNo() {
                return smallOrderNo;
            }

            public void setSmallOrderNo(Object smallOrderNo) {
                this.smallOrderNo = smallOrderNo;
            }

            public Object getSmallOrderNo1() {
                return smallOrderNo1;
            }

            public void setSmallOrderNo1(Object smallOrderNo1) {
                this.smallOrderNo1 = smallOrderNo1;
            }

            public Object getSmallOrderNo2() {
                return smallOrderNo2;
            }

            public void setSmallOrderNo2(Object smallOrderNo2) {
                this.smallOrderNo2 = smallOrderNo2;
            }

            public Object getSmallOrderNo3() {
                return smallOrderNo3;
            }

            public void setSmallOrderNo3(Object smallOrderNo3) {
                this.smallOrderNo3 = smallOrderNo3;
            }

            public Object getGameKeyDescList() {
                return gameKeyDescList;
            }

            public void setGameKeyDescList(Object gameKeyDescList) {
                this.gameKeyDescList = gameKeyDescList;
            }

            public Object getOperation() {
                return operation;
            }

            public void setOperation(Object operation) {
                this.operation = operation;
            }

            public Object getGameTypeList() {
                return gameTypeList;
            }

            public void setGameTypeList(Object gameTypeList) {
                this.gameTypeList = gameTypeList;
            }

            public Object getGameStrategy() {
                return gameStrategy;
            }

            public void setGameStrategy(Object gameStrategy) {
                this.gameStrategy = gameStrategy;
            }
        }

        public static class HUYUGameListBean {

            private int id;
            private String gameName;
            private String gameLogo;
            private String gameVersion;
            private String gameLink;
            private int gameSize;
            private String gameDesc;
            private Object gameInfo;
            private int gameSelected;
            private String packages;
            private long updateTime;
            private int orderNo;
            private String filename;
            private String md5;
            private String uploadId;
            private int scoreLevel;
            private int downloadCount;
            private int versionCode;
            private String versionName;
            private double percentage;
            private int isDelete;
            private long createTime;
            private int commentPeople;
            private int appTypeId;
            private int isHand;
            private int isVR;
            private int isHeadControl;
            private int isTouchScreen;
            private String simpleLabel;
            private String urlSchema;
            private int iosCompany;
            private Object gameAgentList;
            private Object gameDetailsImages;
            private Object gameKeyMapsImages;
            private Object questionResults;
            private Object smallOrderNo;
            private Object smallOrderNo1;
            private Object smallOrderNo2;
            private Object smallOrderNo3;
            private Object gameKeyDescList;
            private Object operation;
            private Object gameTypeList;
            private Object gameStrategy;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(String gameVersion) {
                this.gameVersion = gameVersion;
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

            public String getGameDesc() {
                return gameDesc;
            }

            public void setGameDesc(String gameDesc) {
                this.gameDesc = gameDesc;
            }

            public Object getGameInfo() {
                return gameInfo;
            }

            public void setGameInfo(Object gameInfo) {
                this.gameInfo = gameInfo;
            }

            public int getGameSelected() {
                return gameSelected;
            }

            public void setGameSelected(int gameSelected) {
                this.gameSelected = gameSelected;
            }

            public String getPackages() {
                return packages;
            }

            public void setPackages(String packages) {
                this.packages = packages;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
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

            public String getUploadId() {
                return uploadId;
            }

            public void setUploadId(String uploadId) {
                this.uploadId = uploadId;
            }

            public int getScoreLevel() {
                return scoreLevel;
            }

            public void setScoreLevel(int scoreLevel) {
                this.scoreLevel = scoreLevel;
            }

            public int getDownloadCount() {
                return downloadCount;
            }

            public void setDownloadCount(int downloadCount) {
                this.downloadCount = downloadCount;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public double getPercentage() {
                return percentage;
            }

            public void setPercentage(double percentage) {
                this.percentage = percentage;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getCommentPeople() {
                return commentPeople;
            }

            public void setCommentPeople(int commentPeople) {
                this.commentPeople = commentPeople;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public int getIsHand() {
                return isHand;
            }

            public void setIsHand(int isHand) {
                this.isHand = isHand;
            }

            public int getIsVR() {
                return isVR;
            }

            public void setIsVR(int isVR) {
                this.isVR = isVR;
            }

            public int getIsHeadControl() {
                return isHeadControl;
            }

            public void setIsHeadControl(int isHeadControl) {
                this.isHeadControl = isHeadControl;
            }

            public int getIsTouchScreen() {
                return isTouchScreen;
            }

            public void setIsTouchScreen(int isTouchScreen) {
                this.isTouchScreen = isTouchScreen;
            }

            public String getSimpleLabel() {
                return simpleLabel;
            }

            public void setSimpleLabel(String simpleLabel) {
                this.simpleLabel = simpleLabel;
            }

            public String getUrlSchema() {
                return urlSchema;
            }

            public void setUrlSchema(String urlSchema) {
                this.urlSchema = urlSchema;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }

            public Object getGameAgentList() {
                return gameAgentList;
            }

            public void setGameAgentList(Object gameAgentList) {
                this.gameAgentList = gameAgentList;
            }

            public Object getGameDetailsImages() {
                return gameDetailsImages;
            }

            public void setGameDetailsImages(Object gameDetailsImages) {
                this.gameDetailsImages = gameDetailsImages;
            }

            public Object getGameKeyMapsImages() {
                return gameKeyMapsImages;
            }

            public void setGameKeyMapsImages(Object gameKeyMapsImages) {
                this.gameKeyMapsImages = gameKeyMapsImages;
            }

            public Object getQuestionResults() {
                return questionResults;
            }

            public void setQuestionResults(Object questionResults) {
                this.questionResults = questionResults;
            }

            public Object getSmallOrderNo() {
                return smallOrderNo;
            }

            public void setSmallOrderNo(Object smallOrderNo) {
                this.smallOrderNo = smallOrderNo;
            }

            public Object getSmallOrderNo1() {
                return smallOrderNo1;
            }

            public void setSmallOrderNo1(Object smallOrderNo1) {
                this.smallOrderNo1 = smallOrderNo1;
            }

            public Object getSmallOrderNo2() {
                return smallOrderNo2;
            }

            public void setSmallOrderNo2(Object smallOrderNo2) {
                this.smallOrderNo2 = smallOrderNo2;
            }

            public Object getSmallOrderNo3() {
                return smallOrderNo3;
            }

            public void setSmallOrderNo3(Object smallOrderNo3) {
                this.smallOrderNo3 = smallOrderNo3;
            }

            public Object getGameKeyDescList() {
                return gameKeyDescList;
            }

            public void setGameKeyDescList(Object gameKeyDescList) {
                this.gameKeyDescList = gameKeyDescList;
            }

            public Object getOperation() {
                return operation;
            }

            public void setOperation(Object operation) {
                this.operation = operation;
            }

            public Object getGameTypeList() {
                return gameTypeList;
            }

            public void setGameTypeList(Object gameTypeList) {
                this.gameTypeList = gameTypeList;
            }

            public Object getGameStrategy() {
                return gameStrategy;
            }

            public void setGameStrategy(Object gameStrategy) {
                this.gameStrategy = gameStrategy;
            }
        }

        public static class MOBAGameListBean {

            private int id;
            private String gameName;
            private String gameLogo;
            private String gameVersion;
            private String gameLink;
            private int gameSize;
            private String gameDesc;
            private Object gameInfo;
            private int gameSelected;
            private String packages;
            private long updateTime;
            private int orderNo;
            private String filename;
            private String md5;
            private String uploadId;
            private int scoreLevel;
            private int downloadCount;
            private int versionCode;
            private String versionName;
            private int percentage;
            private int isDelete;
            private long createTime;
            private int commentPeople;
            private int appTypeId;
            private int isHand;
            private int isVR;
            private int isHeadControl;
            private int isTouchScreen;
            private String simpleLabel;
            private String urlSchema;
            private int iosCompany;
            private Object gameAgentList;
            private Object gameDetailsImages;
            private Object gameKeyMapsImages;
            private Object questionResults;
            private Object smallOrderNo;
            private Object smallOrderNo1;
            private Object smallOrderNo2;
            private Object smallOrderNo3;
            private Object gameKeyDescList;
            private Object operation;
            private Object gameTypeList;
            private Object gameStrategy;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(String gameVersion) {
                this.gameVersion = gameVersion;
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

            public String getGameDesc() {
                return gameDesc;
            }

            public void setGameDesc(String gameDesc) {
                this.gameDesc = gameDesc;
            }

            public Object getGameInfo() {
                return gameInfo;
            }

            public void setGameInfo(Object gameInfo) {
                this.gameInfo = gameInfo;
            }

            public int getGameSelected() {
                return gameSelected;
            }

            public void setGameSelected(int gameSelected) {
                this.gameSelected = gameSelected;
            }

            public String getPackages() {
                return packages;
            }

            public void setPackages(String packages) {
                this.packages = packages;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
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

            public String getUploadId() {
                return uploadId;
            }

            public void setUploadId(String uploadId) {
                this.uploadId = uploadId;
            }

            public int getScoreLevel() {
                return scoreLevel;
            }

            public void setScoreLevel(int scoreLevel) {
                this.scoreLevel = scoreLevel;
            }

            public int getDownloadCount() {
                return downloadCount;
            }

            public void setDownloadCount(int downloadCount) {
                this.downloadCount = downloadCount;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public int getPercentage() {
                return percentage;
            }

            public void setPercentage(int percentage) {
                this.percentage = percentage;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getCommentPeople() {
                return commentPeople;
            }

            public void setCommentPeople(int commentPeople) {
                this.commentPeople = commentPeople;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public int getIsHand() {
                return isHand;
            }

            public void setIsHand(int isHand) {
                this.isHand = isHand;
            }

            public int getIsVR() {
                return isVR;
            }

            public void setIsVR(int isVR) {
                this.isVR = isVR;
            }

            public int getIsHeadControl() {
                return isHeadControl;
            }

            public void setIsHeadControl(int isHeadControl) {
                this.isHeadControl = isHeadControl;
            }

            public int getIsTouchScreen() {
                return isTouchScreen;
            }

            public void setIsTouchScreen(int isTouchScreen) {
                this.isTouchScreen = isTouchScreen;
            }

            public String getSimpleLabel() {
                return simpleLabel;
            }

            public void setSimpleLabel(String simpleLabel) {
                this.simpleLabel = simpleLabel;
            }

            public String getUrlSchema() {
                return urlSchema;
            }

            public void setUrlSchema(String urlSchema) {
                this.urlSchema = urlSchema;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }

            public Object getGameAgentList() {
                return gameAgentList;
            }

            public void setGameAgentList(Object gameAgentList) {
                this.gameAgentList = gameAgentList;
            }

            public Object getGameDetailsImages() {
                return gameDetailsImages;
            }

            public void setGameDetailsImages(Object gameDetailsImages) {
                this.gameDetailsImages = gameDetailsImages;
            }

            public Object getGameKeyMapsImages() {
                return gameKeyMapsImages;
            }

            public void setGameKeyMapsImages(Object gameKeyMapsImages) {
                this.gameKeyMapsImages = gameKeyMapsImages;
            }

            public Object getQuestionResults() {
                return questionResults;
            }

            public void setQuestionResults(Object questionResults) {
                this.questionResults = questionResults;
            }

            public Object getSmallOrderNo() {
                return smallOrderNo;
            }

            public void setSmallOrderNo(Object smallOrderNo) {
                this.smallOrderNo = smallOrderNo;
            }

            public Object getSmallOrderNo1() {
                return smallOrderNo1;
            }

            public void setSmallOrderNo1(Object smallOrderNo1) {
                this.smallOrderNo1 = smallOrderNo1;
            }

            public Object getSmallOrderNo2() {
                return smallOrderNo2;
            }

            public void setSmallOrderNo2(Object smallOrderNo2) {
                this.smallOrderNo2 = smallOrderNo2;
            }

            public Object getSmallOrderNo3() {
                return smallOrderNo3;
            }

            public void setSmallOrderNo3(Object smallOrderNo3) {
                this.smallOrderNo3 = smallOrderNo3;
            }

            public Object getGameKeyDescList() {
                return gameKeyDescList;
            }

            public void setGameKeyDescList(Object gameKeyDescList) {
                this.gameKeyDescList = gameKeyDescList;
            }

            public Object getOperation() {
                return operation;
            }

            public void setOperation(Object operation) {
                this.operation = operation;
            }

            public Object getGameTypeList() {
                return gameTypeList;
            }

            public void setGameTypeList(Object gameTypeList) {
                this.gameTypeList = gameTypeList;
            }

            public Object getGameStrategy() {
                return gameStrategy;
            }

            public void setGameStrategy(Object gameStrategy) {
                this.gameStrategy = gameStrategy;
            }
        }

        public static class DailyRecommendedListBean {

            private int id;
            private String gameName;
            private String gameLogo;
            private String gameVersion;
            private String gameLink;
            private int gameSize;
            private String gameDesc;
            private Object gameInfo;
            private int gameSelected;
            private String packages;
            private long updateTime;
            private int orderNo;
            private String filename;
            private String md5;
            private String uploadId;
            private int scoreLevel;
            private int downloadCount;
            private int versionCode;
            private String versionName;
            private Float percentage;
            private int isDelete;
            private long createTime;
            private int commentPeople;
            private int appTypeId;
            private int isHand;
            private int isVR;
            private int isHeadControl;
            private int isTouchScreen;
            private String simpleLabel;
            private String urlSchema;
            private int iosCompany;
            private Object gameAgentList;
            private Object gameDetailsImages;
            private Object gameKeyMapsImages;
            private Object questionResults;
            private Object smallOrderNo;
            private Object smallOrderNo1;
            private Object smallOrderNo2;
            private Object smallOrderNo3;
            private Object gameKeyDescList;
            private Object operation;
            private Object gameTypeList;
            private Object gameStrategy;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(String gameVersion) {
                this.gameVersion = gameVersion;
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

            public String getGameDesc() {
                return gameDesc;
            }

            public void setGameDesc(String gameDesc) {
                this.gameDesc = gameDesc;
            }

            public Object getGameInfo() {
                return gameInfo;
            }

            public void setGameInfo(Object gameInfo) {
                this.gameInfo = gameInfo;
            }

            public int getGameSelected() {
                return gameSelected;
            }

            public void setGameSelected(int gameSelected) {
                this.gameSelected = gameSelected;
            }

            public String getPackages() {
                return packages;
            }

            public void setPackages(String packages) {
                this.packages = packages;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
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

            public String getUploadId() {
                return uploadId;
            }

            public void setUploadId(String uploadId) {
                this.uploadId = uploadId;
            }

            public int getScoreLevel() {
                return scoreLevel;
            }

            public void setScoreLevel(int scoreLevel) {
                this.scoreLevel = scoreLevel;
            }

            public int getDownloadCount() {
                return downloadCount;
            }

            public void setDownloadCount(int downloadCount) {
                this.downloadCount = downloadCount;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public Float getPercentage() {
                return percentage;
            }

            public void setPercentage(Float percentage) {
                this.percentage = percentage;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getCommentPeople() {
                return commentPeople;
            }

            public void setCommentPeople(int commentPeople) {
                this.commentPeople = commentPeople;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public int getIsHand() {
                return isHand;
            }

            public void setIsHand(int isHand) {
                this.isHand = isHand;
            }

            public int getIsVR() {
                return isVR;
            }

            public void setIsVR(int isVR) {
                this.isVR = isVR;
            }

            public int getIsHeadControl() {
                return isHeadControl;
            }

            public void setIsHeadControl(int isHeadControl) {
                this.isHeadControl = isHeadControl;
            }

            public int getIsTouchScreen() {
                return isTouchScreen;
            }

            public void setIsTouchScreen(int isTouchScreen) {
                this.isTouchScreen = isTouchScreen;
            }

            public String getSimpleLabel() {
                return simpleLabel;
            }

            public void setSimpleLabel(String simpleLabel) {
                this.simpleLabel = simpleLabel;
            }

            public String getUrlSchema() {
                return urlSchema;
            }

            public void setUrlSchema(String urlSchema) {
                this.urlSchema = urlSchema;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }

            public Object getGameAgentList() {
                return gameAgentList;
            }

            public void setGameAgentList(Object gameAgentList) {
                this.gameAgentList = gameAgentList;
            }

            public Object getGameDetailsImages() {
                return gameDetailsImages;
            }

            public void setGameDetailsImages(Object gameDetailsImages) {
                this.gameDetailsImages = gameDetailsImages;
            }

            public Object getGameKeyMapsImages() {
                return gameKeyMapsImages;
            }

            public void setGameKeyMapsImages(Object gameKeyMapsImages) {
                this.gameKeyMapsImages = gameKeyMapsImages;
            }

            public Object getQuestionResults() {
                return questionResults;
            }

            public void setQuestionResults(Object questionResults) {
                this.questionResults = questionResults;
            }

            public Object getSmallOrderNo() {
                return smallOrderNo;
            }

            public void setSmallOrderNo(Object smallOrderNo) {
                this.smallOrderNo = smallOrderNo;
            }

            public Object getSmallOrderNo1() {
                return smallOrderNo1;
            }

            public void setSmallOrderNo1(Object smallOrderNo1) {
                this.smallOrderNo1 = smallOrderNo1;
            }

            public Object getSmallOrderNo2() {
                return smallOrderNo2;
            }

            public void setSmallOrderNo2(Object smallOrderNo2) {
                this.smallOrderNo2 = smallOrderNo2;
            }

            public Object getSmallOrderNo3() {
                return smallOrderNo3;
            }

            public void setSmallOrderNo3(Object smallOrderNo3) {
                this.smallOrderNo3 = smallOrderNo3;
            }

            public Object getGameKeyDescList() {
                return gameKeyDescList;
            }

            public void setGameKeyDescList(Object gameKeyDescList) {
                this.gameKeyDescList = gameKeyDescList;
            }

            public Object getOperation() {
                return operation;
            }

            public void setOperation(Object operation) {
                this.operation = operation;
            }

            public Object getGameTypeList() {
                return gameTypeList;
            }

            public void setGameTypeList(Object gameTypeList) {
                this.gameTypeList = gameTypeList;
            }

            public Object getGameStrategy() {
                return gameStrategy;
            }

            public void setGameStrategy(Object gameStrategy) {
                this.gameStrategy = gameStrategy;
            }
        }

        public static class TENCENTGameListBean {

            private int id;
            private String gameName;
            private String gameLogo;
            private String gameVersion;
            private String gameLink;
            private int gameSize;
            private String gameDesc;
            private Object gameInfo;
            private int gameSelected;
            private String packages;
            private long updateTime;
            private int orderNo;
            private String filename;
            private String md5;
            private String uploadId;
            private int scoreLevel;
            private int downloadCount;
            private int versionCode;
            private String versionName;
            private int percentage;
            private int isDelete;
            private long createTime;
            private int commentPeople;
            private int appTypeId;
            private int isHand;
            private int isVR;
            private int isHeadControl;
            private int isTouchScreen;
            private String simpleLabel;
            private String urlSchema;
            private int iosCompany;
            private Object gameAgentList;
            private Object gameDetailsImages;
            private Object gameKeyMapsImages;
            private Object questionResults;
            private Object smallOrderNo;
            private Object smallOrderNo1;
            private Object smallOrderNo2;
            private Object smallOrderNo3;
            private Object gameKeyDescList;
            private Object operation;
            private Object gameTypeList;
            private Object gameStrategy;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(String gameVersion) {
                this.gameVersion = gameVersion;
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

            public String getGameDesc() {
                return gameDesc;
            }

            public void setGameDesc(String gameDesc) {
                this.gameDesc = gameDesc;
            }

            public Object getGameInfo() {
                return gameInfo;
            }

            public void setGameInfo(Object gameInfo) {
                this.gameInfo = gameInfo;
            }

            public int getGameSelected() {
                return gameSelected;
            }

            public void setGameSelected(int gameSelected) {
                this.gameSelected = gameSelected;
            }

            public String getPackages() {
                return packages;
            }

            public void setPackages(String packages) {
                this.packages = packages;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
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

            public String getUploadId() {
                return uploadId;
            }

            public void setUploadId(String uploadId) {
                this.uploadId = uploadId;
            }

            public int getScoreLevel() {
                return scoreLevel;
            }

            public void setScoreLevel(int scoreLevel) {
                this.scoreLevel = scoreLevel;
            }

            public int getDownloadCount() {
                return downloadCount;
            }

            public void setDownloadCount(int downloadCount) {
                this.downloadCount = downloadCount;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public int getPercentage() {
                return percentage;
            }

            public void setPercentage(int percentage) {
                this.percentage = percentage;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getCommentPeople() {
                return commentPeople;
            }

            public void setCommentPeople(int commentPeople) {
                this.commentPeople = commentPeople;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public int getIsHand() {
                return isHand;
            }

            public void setIsHand(int isHand) {
                this.isHand = isHand;
            }

            public int getIsVR() {
                return isVR;
            }

            public void setIsVR(int isVR) {
                this.isVR = isVR;
            }

            public int getIsHeadControl() {
                return isHeadControl;
            }

            public void setIsHeadControl(int isHeadControl) {
                this.isHeadControl = isHeadControl;
            }

            public int getIsTouchScreen() {
                return isTouchScreen;
            }

            public void setIsTouchScreen(int isTouchScreen) {
                this.isTouchScreen = isTouchScreen;
            }

            public String getSimpleLabel() {
                return simpleLabel;
            }

            public void setSimpleLabel(String simpleLabel) {
                this.simpleLabel = simpleLabel;
            }

            public String getUrlSchema() {
                return urlSchema;
            }

            public void setUrlSchema(String urlSchema) {
                this.urlSchema = urlSchema;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }

            public Object getGameAgentList() {
                return gameAgentList;
            }

            public void setGameAgentList(Object gameAgentList) {
                this.gameAgentList = gameAgentList;
            }

            public Object getGameDetailsImages() {
                return gameDetailsImages;
            }

            public void setGameDetailsImages(Object gameDetailsImages) {
                this.gameDetailsImages = gameDetailsImages;
            }

            public Object getGameKeyMapsImages() {
                return gameKeyMapsImages;
            }

            public void setGameKeyMapsImages(Object gameKeyMapsImages) {
                this.gameKeyMapsImages = gameKeyMapsImages;
            }

            public Object getQuestionResults() {
                return questionResults;
            }

            public void setQuestionResults(Object questionResults) {
                this.questionResults = questionResults;
            }

            public Object getSmallOrderNo() {
                return smallOrderNo;
            }

            public void setSmallOrderNo(Object smallOrderNo) {
                this.smallOrderNo = smallOrderNo;
            }

            public Object getSmallOrderNo1() {
                return smallOrderNo1;
            }

            public void setSmallOrderNo1(Object smallOrderNo1) {
                this.smallOrderNo1 = smallOrderNo1;
            }

            public Object getSmallOrderNo2() {
                return smallOrderNo2;
            }

            public void setSmallOrderNo2(Object smallOrderNo2) {
                this.smallOrderNo2 = smallOrderNo2;
            }

            public Object getSmallOrderNo3() {
                return smallOrderNo3;
            }

            public void setSmallOrderNo3(Object smallOrderNo3) {
                this.smallOrderNo3 = smallOrderNo3;
            }

            public Object getGameKeyDescList() {
                return gameKeyDescList;
            }

            public void setGameKeyDescList(Object gameKeyDescList) {
                this.gameKeyDescList = gameKeyDescList;
            }

            public Object getOperation() {
                return operation;
            }

            public void setOperation(Object operation) {
                this.operation = operation;
            }

            public Object getGameTypeList() {
                return gameTypeList;
            }

            public void setGameTypeList(Object gameTypeList) {
                this.gameTypeList = gameTypeList;
            }

            public Object getGameStrategy() {
                return gameStrategy;
            }

            public void setGameStrategy(Object gameStrategy) {
                this.gameStrategy = gameStrategy;
            }
        }

        public static class GunGameListBean {

            private int id;
            private String gameName;
            private String gameLogo;
            private String gameVersion;
            private String gameLink;
            private int gameSize;
            private String gameDesc;
            private Object gameInfo;
            private int gameSelected;
            private String packages;
            private long updateTime;
            private int orderNo;
            private String filename;
            private String md5;
            private String uploadId;
            private int scoreLevel;
            private int downloadCount;
            private int versionCode;
            private String versionName;
            private int percentage;
            private int isDelete;
            private long createTime;
            private int commentPeople;
            private int appTypeId;
            private int isHand;
            private int isVR;
            private int isHeadControl;
            private int isTouchScreen;
            private String simpleLabel;
            private String urlSchema;
            private int iosCompany;
            private Object gameAgentList;
            private Object gameDetailsImages;
            private Object gameKeyMapsImages;
            private Object questionResults;
            private Object smallOrderNo;
            private Object smallOrderNo1;
            private Object smallOrderNo2;
            private Object smallOrderNo3;
            private Object gameKeyDescList;
            private Object operation;
            private Object gameTypeList;
            private Object gameStrategy;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(String gameVersion) {
                this.gameVersion = gameVersion;
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

            public String getGameDesc() {
                return gameDesc;
            }

            public void setGameDesc(String gameDesc) {
                this.gameDesc = gameDesc;
            }

            public Object getGameInfo() {
                return gameInfo;
            }

            public void setGameInfo(Object gameInfo) {
                this.gameInfo = gameInfo;
            }

            public int getGameSelected() {
                return gameSelected;
            }

            public void setGameSelected(int gameSelected) {
                this.gameSelected = gameSelected;
            }

            public String getPackages() {
                return packages;
            }

            public void setPackages(String packages) {
                this.packages = packages;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
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

            public String getUploadId() {
                return uploadId;
            }

            public void setUploadId(String uploadId) {
                this.uploadId = uploadId;
            }

            public int getScoreLevel() {
                return scoreLevel;
            }

            public void setScoreLevel(int scoreLevel) {
                this.scoreLevel = scoreLevel;
            }

            public int getDownloadCount() {
                return downloadCount;
            }

            public void setDownloadCount(int downloadCount) {
                this.downloadCount = downloadCount;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public int getPercentage() {
                return percentage;
            }

            public void setPercentage(int percentage) {
                this.percentage = percentage;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getCommentPeople() {
                return commentPeople;
            }

            public void setCommentPeople(int commentPeople) {
                this.commentPeople = commentPeople;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public int getIsHand() {
                return isHand;
            }

            public void setIsHand(int isHand) {
                this.isHand = isHand;
            }

            public int getIsVR() {
                return isVR;
            }

            public void setIsVR(int isVR) {
                this.isVR = isVR;
            }

            public int getIsHeadControl() {
                return isHeadControl;
            }

            public void setIsHeadControl(int isHeadControl) {
                this.isHeadControl = isHeadControl;
            }

            public int getIsTouchScreen() {
                return isTouchScreen;
            }

            public void setIsTouchScreen(int isTouchScreen) {
                this.isTouchScreen = isTouchScreen;
            }

            public String getSimpleLabel() {
                return simpleLabel;
            }

            public void setSimpleLabel(String simpleLabel) {
                this.simpleLabel = simpleLabel;
            }

            public String getUrlSchema() {
                return urlSchema;
            }

            public void setUrlSchema(String urlSchema) {
                this.urlSchema = urlSchema;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }

            public Object getGameAgentList() {
                return gameAgentList;
            }

            public void setGameAgentList(Object gameAgentList) {
                this.gameAgentList = gameAgentList;
            }

            public Object getGameDetailsImages() {
                return gameDetailsImages;
            }

            public void setGameDetailsImages(Object gameDetailsImages) {
                this.gameDetailsImages = gameDetailsImages;
            }

            public Object getGameKeyMapsImages() {
                return gameKeyMapsImages;
            }

            public void setGameKeyMapsImages(Object gameKeyMapsImages) {
                this.gameKeyMapsImages = gameKeyMapsImages;
            }

            public Object getQuestionResults() {
                return questionResults;
            }

            public void setQuestionResults(Object questionResults) {
                this.questionResults = questionResults;
            }

            public Object getSmallOrderNo() {
                return smallOrderNo;
            }

            public void setSmallOrderNo(Object smallOrderNo) {
                this.smallOrderNo = smallOrderNo;
            }

            public Object getSmallOrderNo1() {
                return smallOrderNo1;
            }

            public void setSmallOrderNo1(Object smallOrderNo1) {
                this.smallOrderNo1 = smallOrderNo1;
            }

            public Object getSmallOrderNo2() {
                return smallOrderNo2;
            }

            public void setSmallOrderNo2(Object smallOrderNo2) {
                this.smallOrderNo2 = smallOrderNo2;
            }

            public Object getSmallOrderNo3() {
                return smallOrderNo3;
            }

            public void setSmallOrderNo3(Object smallOrderNo3) {
                this.smallOrderNo3 = smallOrderNo3;
            }

            public Object getGameKeyDescList() {
                return gameKeyDescList;
            }

            public void setGameKeyDescList(Object gameKeyDescList) {
                this.gameKeyDescList = gameKeyDescList;
            }

            public Object getOperation() {
                return operation;
            }

            public void setOperation(Object operation) {
                this.operation = operation;
            }

            public Object getGameTypeList() {
                return gameTypeList;
            }

            public void setGameTypeList(Object gameTypeList) {
                this.gameTypeList = gameTypeList;
            }

            public Object getGameStrategy() {
                return gameStrategy;
            }

            public void setGameStrategy(Object gameStrategy) {
                this.gameStrategy = gameStrategy;
            }
        }

        public static class ZHANGQUGameListBean {

            private int id;
            private String gameName;
            private String gameLogo;
            private String gameVersion;
            private String gameLink;
            private int gameSize;
            private String gameDesc;
            private Object gameInfo;
            private int gameSelected;
            private String packages;
            private long updateTime;
            private int orderNo;
            private String filename;
            private String md5;
            private String uploadId;
            private int scoreLevel;
            private int downloadCount;
            private int versionCode;
            private String versionName;
            private double percentage;
            private int isDelete;
            private long createTime;
            private int commentPeople;
            private int appTypeId;
            private int isHand;
            private int isVR;
            private int isHeadControl;
            private int isTouchScreen;
            private String simpleLabel;
            private String urlSchema;
            private int iosCompany;
            private Object gameAgentList;
            private Object gameDetailsImages;
            private Object gameKeyMapsImages;
            private Object questionResults;
            private Object smallOrderNo;
            private Object smallOrderNo1;
            private Object smallOrderNo2;
            private Object smallOrderNo3;
            private Object gameKeyDescList;
            private Object operation;
            private Object gameTypeList;
            private Object gameStrategy;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(String gameVersion) {
                this.gameVersion = gameVersion;
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

            public String getGameDesc() {
                return gameDesc;
            }

            public void setGameDesc(String gameDesc) {
                this.gameDesc = gameDesc;
            }

            public Object getGameInfo() {
                return gameInfo;
            }

            public void setGameInfo(Object gameInfo) {
                this.gameInfo = gameInfo;
            }

            public int getGameSelected() {
                return gameSelected;
            }

            public void setGameSelected(int gameSelected) {
                this.gameSelected = gameSelected;
            }

            public String getPackages() {
                return packages;
            }

            public void setPackages(String packages) {
                this.packages = packages;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
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

            public String getUploadId() {
                return uploadId;
            }

            public void setUploadId(String uploadId) {
                this.uploadId = uploadId;
            }

            public int getScoreLevel() {
                return scoreLevel;
            }

            public void setScoreLevel(int scoreLevel) {
                this.scoreLevel = scoreLevel;
            }

            public int getDownloadCount() {
                return downloadCount;
            }

            public void setDownloadCount(int downloadCount) {
                this.downloadCount = downloadCount;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public double getPercentage() {
                return percentage;
            }

            public void setPercentage(double percentage) {
                this.percentage = percentage;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getCommentPeople() {
                return commentPeople;
            }

            public void setCommentPeople(int commentPeople) {
                this.commentPeople = commentPeople;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public int getIsHand() {
                return isHand;
            }

            public void setIsHand(int isHand) {
                this.isHand = isHand;
            }

            public int getIsVR() {
                return isVR;
            }

            public void setIsVR(int isVR) {
                this.isVR = isVR;
            }

            public int getIsHeadControl() {
                return isHeadControl;
            }

            public void setIsHeadControl(int isHeadControl) {
                this.isHeadControl = isHeadControl;
            }

            public int getIsTouchScreen() {
                return isTouchScreen;
            }

            public void setIsTouchScreen(int isTouchScreen) {
                this.isTouchScreen = isTouchScreen;
            }

            public String getSimpleLabel() {
                return simpleLabel;
            }

            public void setSimpleLabel(String simpleLabel) {
                this.simpleLabel = simpleLabel;
            }

            public String getUrlSchema() {
                return urlSchema;
            }

            public void setUrlSchema(String urlSchema) {
                this.urlSchema = urlSchema;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }

            public Object getGameAgentList() {
                return gameAgentList;
            }

            public void setGameAgentList(Object gameAgentList) {
                this.gameAgentList = gameAgentList;
            }

            public Object getGameDetailsImages() {
                return gameDetailsImages;
            }

            public void setGameDetailsImages(Object gameDetailsImages) {
                this.gameDetailsImages = gameDetailsImages;
            }

            public Object getGameKeyMapsImages() {
                return gameKeyMapsImages;
            }

            public void setGameKeyMapsImages(Object gameKeyMapsImages) {
                this.gameKeyMapsImages = gameKeyMapsImages;
            }

            public Object getQuestionResults() {
                return questionResults;
            }

            public void setQuestionResults(Object questionResults) {
                this.questionResults = questionResults;
            }

            public Object getSmallOrderNo() {
                return smallOrderNo;
            }

            public void setSmallOrderNo(Object smallOrderNo) {
                this.smallOrderNo = smallOrderNo;
            }

            public Object getSmallOrderNo1() {
                return smallOrderNo1;
            }

            public void setSmallOrderNo1(Object smallOrderNo1) {
                this.smallOrderNo1 = smallOrderNo1;
            }

            public Object getSmallOrderNo2() {
                return smallOrderNo2;
            }

            public void setSmallOrderNo2(Object smallOrderNo2) {
                this.smallOrderNo2 = smallOrderNo2;
            }

            public Object getSmallOrderNo3() {
                return smallOrderNo3;
            }

            public void setSmallOrderNo3(Object smallOrderNo3) {
                this.smallOrderNo3 = smallOrderNo3;
            }

            public Object getGameKeyDescList() {
                return gameKeyDescList;
            }

            public void setGameKeyDescList(Object gameKeyDescList) {
                this.gameKeyDescList = gameKeyDescList;
            }

            public Object getOperation() {
                return operation;
            }

            public void setOperation(Object operation) {
                this.operation = operation;
            }

            public Object getGameTypeList() {
                return gameTypeList;
            }

            public void setGameTypeList(Object gameTypeList) {
                this.gameTypeList = gameTypeList;
            }

            public Object getGameStrategy() {
                return gameStrategy;
            }

            public void setGameStrategy(Object gameStrategy) {
                this.gameStrategy = gameStrategy;
            }
        }
    }
}
