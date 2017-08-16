package com.jzt.hol.android.jkda.sdk.bean.classification;

import java.util.List;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class AllClassifyBean {


    /**
     * code : 0
     * msg : gameStyleList表示游戏特点，gameCategoryList表示游戏类别，gameManufacturerList表示厂商，gameCountyList表示国别
     * data : {"gameCountyList":[{"id":47,"cName":"大陆","cParentId":"47"},{"id":48,"cName":"港澳台","cParentId":"48"},{"id":49,
     * "cName":"美国","cParentId":"49"},{"id":50,"cName":"日本","cParentId":"50"},{"id":51,"cName":"韩国","cParentId":"51"},{"id":52,
     * "cName":"其他","cParentId":"52"}],"gameStyleList":[{"id":1,"cName":"原生手柄","cParentId":"1"},{"id":2,"cName":"云适配手柄",
     * "cParentId":"2"},{"id":3,"cName":"破解变态版","cParentId":"3"},{"id":4,"cName":"汉化游戏","cParentId":"4"},{"id":5,
     * "cName":"云存档游戏","cParentId":"5"},{"id":6,"cName":"特色游戏","cParentId":"6"}],"gameCategoryList":[{"id":7,"cName":"角色",
     * "cParentId":"7"},{"id":8,"cName":"动作","cParentId":"8"},{"id":9,"cName":"冒险","cParentId":"9"},{"id":10,"cName":"卡牌",
     * "cParentId":"10"},{"id":11,"cName":"策略","cParentId":"11"},{"id":12,"cName":"模拟","cParentId":"12"},{"id":13,
     * "cName":"二次元","cParentId":"13"},{"id":14,"cName":"FPS","cParentId":"14"},{"id":15,"cName":"竞速","cParentId":"15"},
     * {"id":16,"cName":"飞行","cParentId":"16"},{"id":17,"cName":"MOBA","cParentId":"17"},{"id":18,"cName":"益智",
     * "cParentId":"18"},{"id":19,"cName":"体育","cParentId":"19"},{"id":20,"cName":"唯美","cParentId":"20"},{"id":21,"cName":"生存",
     * "cParentId":"21"},{"id":22,"cName":"解谜","cParentId":"22"},{"id":23,"cName":"音乐","cParentId":"23"},{"id":24,"cName":"街机",
     * "cParentId":"24"}],"gameManufacturerList":[{"id":25,"cName":"EA","cParentId":"25"},{"id":26,"cName":"Gameloft",
     * "cParentId":"26"},{"id":27,"cName":"腾讯","cParentId":"27"},{"id":28,"cName":"网易","cParentId":"28"},{"id":29,
     * "cName":"英雄互娱","cParentId":"29"},{"id":30,"cName":"Supercell","cParentId":"30"},{"id":31,"cName":"动视暴雪",
     * "cParentId":"31"},{"id":32,"cName":"Mixi","cParentId":"32"},{"id":33,"cName":"Glu","cParentId":"33"},{"id":34,
     * "cName":"MZ","cParentId":"34"},{"id":35,"cName":"Square Enix","cParentId":"35"},{"id":36,"cName":"育碧","cParentId":"36"},
     * {"id":37,"cName":"Nexon","cParentId":"37"},{"id":38,"cName":"触控科技","cParentId":"38"},{"id":39,"cName":"顽石互动",
     * "cParentId":"39"},{"id":40,"cName":"银汉游戏","cParentId":"40"},{"id":41,"cName":"蓝港在线","cParentId":"41"},{"id":42,
     * "cName":"掌上明珠","cParentId":"42"},{"id":43,"cName":"完美世界","cParentId":"43"},{"id":44,"cName":"乐动卓越","cParentId":"44"},
     * {"id":45,"cName":"畅游天下","cParentId":"45"},{"id":46,"cName":"其他","cParentId":"46"}]}
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
        private List<GameCountyListBean> gameCountyList;
        private List<GameStyleListBean> gameStyleList;
        private List<GameCategoryListBean> gameCategoryList;
        private List<GameManufacturerListBean> gameManufacturerList;

        public List<GameCountyListBean> getGameCountyList() {
            return gameCountyList;
        }

        public void setGameCountyList(List<GameCountyListBean> gameCountyList) {
            this.gameCountyList = gameCountyList;
        }

        public List<GameStyleListBean> getGameStyleList() {
            return gameStyleList;
        }

        public void setGameStyleList(List<GameStyleListBean> gameStyleList) {
            this.gameStyleList = gameStyleList;
        }

        public List<GameCategoryListBean> getGameCategoryList() {
            return gameCategoryList;
        }

        public void setGameCategoryList(List<GameCategoryListBean> gameCategoryList) {
            this.gameCategoryList = gameCategoryList;
        }

        public List<GameManufacturerListBean> getGameManufacturerList() {
            return gameManufacturerList;
        }

        public void setGameManufacturerList(List<GameManufacturerListBean> gameManufacturerList) {
            this.gameManufacturerList = gameManufacturerList;
        }

        public static class GameCountyListBean {
            /**
             * id : 47
             * cName : 大陆
             * cParentId : 47
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

        public static class GameStyleListBean {
            /**
             * id : 1
             * cName : 原生手柄
             * cParentId : 1
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

        public static class GameCategoryListBean {
            /**
             * id : 7
             * cName : 角色
             * cParentId : 7
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

        public static class GameManufacturerListBean {
            /**
             * id : 25
             * cName : EA
             * cParentId : 25
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
    }
}
