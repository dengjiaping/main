package com.jzt.hol.android.jkda.sdk.bean.gamehub;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class MsgDetailBean {

    /**
     * code : 0
     * msg : null
     * data : {"id":1,"userCode":"UC0000000000042","postPublisher":"嘿嘿","headPhoto":null,"postTitle":"今日头条","postTagId":1,
     * "watchNum":0,"postContent":"很好看","createTime":1488853680000,"updateTime":null,"appTypeId":0,"commentNum":null,
     * "postImage":null,"isDelete":null,"isReport":null,"orderNo":null,"isPoint":0,"pointCount":1}
     * map : null
     * innerResult : null
     */

    private int code;
    private String msg;
    private DataBean data;
    private Object map;
    private Object innerResult;
    private PostDataBean mPostDataBean;

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
    public PostDataBean getPostData() {
        return mPostDataBean;
    }

    public void setPostData(PostDataBean data) {
        this.mPostDataBean = data;
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

    public static class PostDataBean {

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
         * showPostCategory : {"id":21,"postCategoryName":"网易","postCategoryCount":0,"postCategoryUrl":null,"showPostCategoryList":null}
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

    public static class DataBean {
        /**
         * id : 1
         * userCode : UC0000000000042
         * postPublisher : 嘿嘿
         * headPhoto : null
         * postTitle : 今日头条
         * postTagId : 1
         * watchNum : 0
         * postContent : 很好看
         * createTime : 1488853680000
         * updateTime : null
         * appTypeId : 0
         * commentNum : null
         * postImage : null
         * isDelete : null
         * isReport : null
         * orderNo : null
         * isPoint : 0
         * pointCount : 1
         */

        private int id;
        private String userCode;
        private String postPublisher;
        private String headPhoto;
        private String postTitle;
        private int postTagId;
        private int watchNum;
        private String postContent;
        private long createTime;
        private Object updateTime;
        private int appTypeId;
        private Object commentNum;
        private String postImage;
        private Object isDelete;
        private int isReport;
        private Object orderNo;
        private int isPoint;
        private int pointCount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getPostPublisher() {
            return postPublisher;
        }

        public void setPostPublisher(String postPublisher) {
            this.postPublisher = postPublisher;
        }

        public String getHeadPhoto() {
            return headPhoto;
        }

        public void setHeadPhoto(String headPhoto) {
            this.headPhoto = headPhoto;
        }

        public String getPostTitle() {
            return postTitle;
        }

        public void setPostTitle(String postTitle) {
            this.postTitle = postTitle;
        }

        public int getPostTagId() {
            return postTagId;
        }

        public void setPostTagId(int postTagId) {
            this.postTagId = postTagId;
        }

        public int getWatchNum() {
            return watchNum;
        }

        public void setWatchNum(int watchNum) {
            this.watchNum = watchNum;
        }

        public String getPostContent() {
            return postContent;
        }

        public void setPostContent(String postContent) {
            this.postContent = postContent;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public int getAppTypeId() {
            return appTypeId;
        }

        public void setAppTypeId(int appTypeId) {
            this.appTypeId = appTypeId;
        }

        public Object getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(Object commentNum) {
            this.commentNum = commentNum;
        }

        public String getPostImage() {
            return postImage;
        }

        public void setPostImage(String postImage) {
            this.postImage = postImage;
        }

        public Object getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(Object isDelete) {
            this.isDelete = isDelete;
        }

        public int getIsReport() {
            return isReport;
        }

        public void setIsReport(int isReport) {
            this.isReport = isReport;
        }

        public Object getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(Object orderNo) {
            this.orderNo = orderNo;
        }

        public int getIsPoint() {
            return isPoint;
        }

        public void setIsPoint(int isPoint) {
            this.isPoint = isPoint;
        }

        public int getPointCount() {
            return pointCount;
        }

        public void setPointCount(int pointCount) {
            this.pointCount = pointCount;
        }
    }
}
