package com.jzt.hol.android.jkda.sdk.bean.main;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class DiscoverListBean {

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
        /**
         * gameCategroyList : [{"id":7,"cName":"角色","cParentId":"2"},{"id":8,"cName":"动作","cParentId":"2"},{"id":9,
         * "cName":"冒险","cParentId":"2"},{"id":10,"cName":"卡牌","cParentId":"2"},{"id":11,"cName":"策略","cParentId":"2"},
         * {"id":12,"cName":"模拟","cParentId":"2"},{"id":13,"cName":"二次元","cParentId":"2"},{"id":14,"cName":"FPS",
         * "cParentId":"2"},{"id":15,"cName":"竞速","cParentId":"2"},{"id":16,"cName":"飞行","cParentId":"2"},{"id":17,
         * "cName":"MOBA","cParentId":"2"},{"id":18,"cName":"益智","cParentId":"2"},{"id":19,"cName":"体育","cParentId":"2"},
         * {"id":20,"cName":"唯美","cParentId":"2"},{"id":21,"cName":"生存","cParentId":"2"},{"id":22,"cName":"解谜",
         * "cParentId":"2"},{"id":23,"cName":"音乐","cParentId":"2"},{"id":24,"cName":"街机","cParentId":"2"}]
         * dailyNewGamesList : {"list":[],"categoryId":-1}
         * weeklyNewGamesList : {"list":[],"categoryId":-2}
         * agentGamePicList : {"list":[],"categoryId":-3}
         * resultList : [{"list":[{"id":1,"cName":"","gameName":"龙之谷","gameLogo":"http://oss.ngame.cn/upload/1488424068534
         * .png","scoreLevel":0,"gameLink":"","gameSize":0,"versionCode":0,"packages":"","filename":"","md5":"",
         * "versionName":"","updateTime":null},{"id":3,"cName":"","gameName":"晶体管","gameLogo":"http://oss.ngame
         * .cn/upload/1496908609174.jpg","scoreLevel":0,"gameLink":"","gameSize":0,"versionCode":0,"packages":"","filename":"",
         * "md5":"","versionName":"","updateTime":null}],"categoryId":7},{"list":[{"id":1,"cName":"","gameName":"龙之谷",
         * "gameLogo":"http://oss.ngame.cn/upload/1488424068534.png","scoreLevel":0,"gameLink":"","gameSize":0,"versionCode":0,
         * "packages":"","filename":"","md5":"","versionName":"","updateTime":null},{"id":3,"cName":"","gameName":"晶体管",
         * "gameLogo":"http://oss.ngame.cn/upload/1496908609174.jpg","scoreLevel":0,"gameLink":"","gameSize":0,"versionCode":0,
         * "packages":"","filename":"","md5":"","versionName":"","updateTime":null}],"categoryId":8},{"list":[{"id":1,
         * "cName":"","gameName":"龙之谷","gameLogo":"http://oss.ngame.cn/upload/1488424068534.png","scoreLevel":0,"gameLink":"",
         * "gameSize":0,"versionCode":0,"packages":"","filename":"","md5":"","versionName":"","updateTime":null},{"id":3,
         * "cName":"","gameName":"晶体管","gameLogo":"http://oss.ngame.cn/upload/1496908609174.jpg","scoreLevel":0,"gameLink":"",
         * "gameSize":0,"versionCode":0,"packages":"","filename":"","md5":"","versionName":"","updateTime":null}],
         * "categoryId":9},{"list":[],"categoryId":10},{"list":[],"categoryId":11},{"list":[],"categoryId":12},
         * {"list":[{"id":1,"cName":"","gameName":"龙之谷","gameLogo":"http://oss.ngame.cn/upload/1488424068534.png",
         * "scoreLevel":0,"gameLink":"","gameSize":0,"versionCode":0,"packages":"","filename":"","md5":"","versionName":"",
         * "updateTime":null}],"categoryId":13},{"list":[],"categoryId":14},{"list":[],"categoryId":15},{"list":[],
         * "categoryId":16},{"list":[{"id":2,"cName":"","gameName":"王者荣耀","gameLogo":"http://oss.ngame.cn/upload/1466667978284
         * .png","scoreLevel":0,"gameLink":"","gameSize":0,"versionCode":0,"packages":"","filename":"","md5":"",
         * "versionName":"","updateTime":null}],"categoryId":17},{"list":[{"id":3,"cName":"","gameName":"晶体管",
         * "gameLogo":"http://oss.ngame.cn/upload/1496908609174.jpg","scoreLevel":0,"gameLink":"","gameSize":0,"versionCode":0,
         * "packages":"","filename":"","md5":"","versionName":"","updateTime":null}],"categoryId":18},{"list":[],
         * "categoryId":19},{"list":[],"categoryId":20},{"list":[],"categoryId":21},{"list":[{"id":3,"cName":"",
         * "gameName":"晶体管","gameLogo":"http://oss.ngame.cn/upload/1496908609174.jpg","scoreLevel":0,"gameLink":"",
         * "gameSize":0,"versionCode":0,"packages":"","filename":"","md5":"","versionName":"","updateTime":null}],
         * "categoryId":22},{"list":[],"categoryId":23},{"list":[],"categoryId":24}]
         */

        private DailyNewGamesListBean dailyNewGamesList;
        private WeeklyNewGamesListBean weeklyNewGamesList;
        private AgentGamePicListBean agentGamePicList;
        private List<GameCategroyListBean> gameCategroyList;
        private List<ResultListBean> resultList;

        public DailyNewGamesListBean getDailyNewGamesList() {
            return dailyNewGamesList;
        }

        public void setDailyNewGamesList(DailyNewGamesListBean dailyNewGamesList) {
            this.dailyNewGamesList = dailyNewGamesList;
        }

        public WeeklyNewGamesListBean getWeeklyNewGamesList() {
            return weeklyNewGamesList;
        }

        public void setWeeklyNewGamesList(WeeklyNewGamesListBean weeklyNewGamesList) {
            this.weeklyNewGamesList = weeklyNewGamesList;
        }

        public AgentGamePicListBean getAgentGamePicList() {
            return agentGamePicList;
        }

        public void setAgentGamePicList(AgentGamePicListBean agentGamePicList) {
            this.agentGamePicList = agentGamePicList;
        }

        public List<GameCategroyListBean> getGameCategroyList() {
            return gameCategroyList;
        }

        public void setGameCategroyList(List<GameCategroyListBean> gameCategroyList) {
            this.gameCategroyList = gameCategroyList;
        }

        public List<ResultListBean> getResultList() {
            return resultList;
        }

        public void setResultList(List<ResultListBean> resultList) {
            this.resultList = resultList;
        }

        public static class DailyNewGamesListBean {
            /**
             * list : []
             * categoryId : -1
             */

            private int categoryId;
            private List<?> list;

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public List<?> getList() {
                return list;
            }

            public void setList(List<?> list) {
                this.list = list;
            }
        }

        public static class WeeklyNewGamesListBean {
            /**
             * list : []
             * categoryId : -2
             */

            private int categoryId;
            private List<?> list;

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public List<?> getList() {
                return list;
            }

            public void setList(List<?> list) {
                this.list = list;
            }
        }

        public static class AgentGamePicListBean {
            /**
             * list : []
             * categoryId : -3
             */

            private int categoryId;
            private List<?> list;

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public List<?> getList() {
                return list;
            }

            public void setList(List<?> list) {
                this.list = list;
            }
        }

        public static class GameCategroyListBean {
            /**
             * id : 7
             * cName : 角色
             * cParentId : 2
             */

            private int id;
            private String cName;
            private String cParentId;

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

            public String getCParentId() {
                return cParentId;
            }

            public void setCParentId(String cParentId) {
                this.cParentId = cParentId;
            }
        }

        public static class ResultListBean {
            /**
             * list : [{"id":1,"cName":"","gameName":"龙之谷","gameLogo":"http://oss.ngame.cn/upload/1488424068534.png",
             * "scoreLevel":0,"gameLink":"","gameSize":0,"versionCode":0,"packages":"","filename":"","md5":"","versionName":"",
             * "updateTime":null},{"id":3,"cName":"","gameName":"晶体管","gameLogo":"http://oss.ngame.cn/upload/1496908609174
             * .jpg","scoreLevel":0,"gameLink":"","gameSize":0,"versionCode":0,"packages":"","filename":"","md5":"",
             * "versionName":"","updateTime":null}]
             * categoryId : 7
             */

            private int categoryId;
            private List<ListBean> list;

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * id : 1
                 * cName :
                 * gameName : 龙之谷
                 * gameLogo : http://oss.ngame.cn/upload/1488424068534.png
                 * scoreLevel : 0
                 * gameLink :
                 * gameSize : 0
                 * versionCode : 0
                 * packages :
                 * filename :
                 * md5 :
                 * versionName :
                 * updateTime : null
                 */

                private long id;
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
                private Object updateTime;

                public long getId() {
                    return id;
                }

                public void setId(long id) {
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

                public Object getUpdateTime() {
                    return updateTime;
                }

                public void setUpdateTime(Object updateTime) {
                    this.updateTime = updateTime;
                }
            }
        }
    }
}
