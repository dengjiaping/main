package com.jzt.hol.android.jkda.sdk.bean.gamehub;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class PostDetailBean {
    /**
     * code : 0
     * msg : null
     * data : {"id":1,"postRoleName":"官方推荐8","postRoleHeadPhoto":"http://oss.ngame.cn/upload/1512790187629.png",
     * "postCategoryName":"网易","postTitle":"测试测试2","postContent":"测试测试2","watchNum":0,"pointNum":100,
     * "updateTime":1512377882000,"postImageList":null,"showPostCategory":{"id":21,"postCategoryName":"网易",
     * "postCategoryCount":0,"postCategoryUrl":null,"showPostCategoryList":null}}
     * map : null
     * innerResult : null
     */

    private int code;
    private Object msg;
    private DataBean data;
    private Object map;
    private Object innerResult;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
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
         * id : 1
         * postRoleName : 官方推荐8
         * postRoleHeadPhoto : http://oss.ngame.cn/upload/1512790187629.png
         * postCategoryName : 网易
         * postTitle : 测试测试2
         * postContent : 测试测试2
         * watchNum : 0
         * pointNum : 100
         * updateTime : 1512377882000
         * postImageList : null
         * showPostCategory : {"id":21,"postCategoryName":"网易","postCategoryCount":0,"postCategoryUrl":null,
         * "showPostCategoryList":null}
         */

        private int id;
        private String postRoleName;
        private String postRoleHeadPhoto;
        private String postCategoryName;
        private String postTitle;
        private String postContent;
        private int watchNum;
        private int pointNum;
        private long updateTime;
        private Object postImageList;
        private ShowPostCategoryBean showPostCategory;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPostRoleName() {
            return postRoleName;
        }

        public void setPostRoleName(String postRoleName) {
            this.postRoleName = postRoleName;
        }

        public String getPostRoleHeadPhoto() {
            return postRoleHeadPhoto;
        }

        public void setPostRoleHeadPhoto(String postRoleHeadPhoto) {
            this.postRoleHeadPhoto = postRoleHeadPhoto;
        }

        public String getPostCategoryName() {
            return postCategoryName;
        }

        public void setPostCategoryName(String postCategoryName) {
            this.postCategoryName = postCategoryName;
        }

        public String getPostTitle() {
            return postTitle;
        }

        public void setPostTitle(String postTitle) {
            this.postTitle = postTitle;
        }

        public String getPostContent() {
            return postContent;
        }

        public void setPostContent(String postContent) {
            this.postContent = postContent;
        }

        public int getWatchNum() {
            return watchNum;
        }

        public void setWatchNum(int watchNum) {
            this.watchNum = watchNum;
        }

        public int getPointNum() {
            return pointNum;
        }

        public void setPointNum(int pointNum) {
            this.pointNum = pointNum;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public Object getPostImageList() {
            return postImageList;
        }

        public void setPostImageList(Object postImageList) {
            this.postImageList = postImageList;
        }

        public ShowPostCategoryBean getShowPostCategory() {
            return showPostCategory;
        }

        public void setShowPostCategory(ShowPostCategoryBean showPostCategory) {
            this.showPostCategory = showPostCategory;
        }

        public static class ShowPostCategoryBean {
            /**
             * id : 21
             * postCategoryName : 网易
             * postCategoryCount : 0
             * postCategoryUrl : null
             * showPostCategoryList : null
             */

            private int id;
            private String postCategoryName;
            private int postCategoryCount;
            private Object postCategoryUrl;
            private Object showPostCategoryList;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPostCategoryName() {
                return postCategoryName;
            }

            public void setPostCategoryName(String postCategoryName) {
                this.postCategoryName = postCategoryName;
            }

            public int getPostCategoryCount() {
                return postCategoryCount;
            }

            public void setPostCategoryCount(int postCategoryCount) {
                this.postCategoryCount = postCategoryCount;
            }

            public Object getPostCategoryUrl() {
                return postCategoryUrl;
            }

            public void setPostCategoryUrl(Object postCategoryUrl) {
                this.postCategoryUrl = postCategoryUrl;
            }

            public Object getShowPostCategoryList() {
                return showPostCategoryList;
            }

            public void setShowPostCategoryList(Object showPostCategoryList) {
                this.showPostCategoryList = showPostCategoryList;
            }
        }
    }
}
