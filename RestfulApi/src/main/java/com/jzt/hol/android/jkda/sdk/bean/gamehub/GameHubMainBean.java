package com.jzt.hol.android.jkda.sdk.bean.gamehub;

import java.util.List;

/**
 * Created by Administrator on 2017/3/6 0006.
 */

public class GameHubMainBean {

    /**
     * code : 0
     * msg : null
     * data : [{"id":3,"userCode":"UC0000000000069","postPublisher":"新用户76920","headPhoto":"http://oss.ngame.cn/upload/userHead/1488614496280.jpg","postTitle":"我不会玩","postTagId":1,"watchNum":0,"postContent":"有哦宽松估摸着下午说GPS咯哦哦","createTime":1488614643000,"updateTime":1488614643000,"appTypeId":0,"commentNum":0,"postImage":"http://oss.ngame.cn/upload/postImage/1488614642772.png,http://oss.ngame.cn/upload/postImage/1488614645034.png,http://oss.ngame.cn/upload/postImage/1488614647262.png","pointNum":0,"voteNum":0,"isDelete":0,"isReport":0,"orderNo":0,"isPoint":null},{"id":2,"userCode":"UC0000000000045","postPublisher":"13035114192","headPhoto":"http://oss.ngame.cn/upload/userHead/1474265729013.jpg","postTitle":"今日头条","postTagId":1,"watchNum":0,"postContent":"挺好的一个头条","createTime":1488595458000,"updateTime":1488595458000,"appTypeId":0,"commentNum":0,"postImage":"http://oss.ngame.cn/upload/postImage/1488595457992.png","pointNum":0,"voteNum":0,"isDelete":0,"isReport":0,"orderNo":0,"isPoint":null},{"id":1,"userCode":"UC0000000000045","postPublisher":"13035114192","headPhoto":"http://oss.ngame.cn/upload/userHead/1474265729013.jpg","postTitle":"今日头条","postTagId":1,"watchNum":0,"postContent":"挺好的一个头条","createTime":1488593747000,"updateTime":1488593747000,"appTypeId":0,"commentNum":0,"postImage":"http://oss.ngame.cn/upload/postImage/1488593746965.png","pointNum":0,"voteNum":0,"isDelete":0,"isReport":1,"orderNo":0,"isPoint":null}]
     * map : null
     * innerResult : null
     * page : 1
     * pageSize : 10
     * totals : 3
     */

    private int code;
    private String msg;
    private Object map;
    private Object innerResult;
    private int page;
    private int pageSize;
    private int totals;
    private List<DataBean> data;

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * userCode : UC0000000000069
         * postPublisher : 新用户76920
         * headPhoto : http://oss.ngame.cn/upload/userHead/1488614496280.jpg
         * postTitle : 我不会玩
         * postTagId : 1
         * watchNum : 0
         * postContent : 有哦宽松估摸着下午说GPS咯哦哦
         * createTime : 1488614643000
         * updateTime : 1488614643000
         * appTypeId : 0
         * commentNum : 0
         * postImage : http://oss.ngame.cn/upload/postImage/1488614642772.png,http://oss.ngame.cn/upload/postImage/1488614645034.png,http://oss.ngame.cn/upload/postImage/1488614647262.png
         * pointNum : 0
         * voteNum : 0
         * isDelete : 0
         * isReport : 0
         * orderNo : 0
         * isPoint : null
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
        private long updateTime;
        private int appTypeId;
        private int commentNum;
        private String postImage;
        private int pointNum;
        private int voteNum;
        private int isDelete;
        private int isReport;
        private int orderNo;
        private Object isPoint;

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

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getAppTypeId() {
            return appTypeId;
        }

        public void setAppTypeId(int appTypeId) {
            this.appTypeId = appTypeId;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public String getPostImage() {
            return postImage;
        }

        public void setPostImage(String postImage) {
            this.postImage = postImage;
        }

        public int getPointNum() {
            return pointNum;
        }

        public void setPointNum(int pointNum) {
            this.pointNum = pointNum;
        }

        public int getVoteNum() {
            return voteNum;
        }

        public void setVoteNum(int voteNum) {
            this.voteNum = voteNum;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public int getIsReport() {
            return isReport;
        }

        public void setIsReport(int isReport) {
            this.isReport = isReport;
        }

        public int getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(int orderNo) {
            this.orderNo = orderNo;
        }

        public Object getIsPoint() {
            return isPoint;
        }

        public void setIsPoint(Object isPoint) {
            this.isPoint = isPoint;
        }
    }
}
