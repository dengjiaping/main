package cn.ngame.store.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 热门推荐信息
 * Created by zeng on 2016/6/15.
 */
public class NecessaryListInfo implements Serializable {

    /**
     * id : 5
     * parentId : 0
     * toolName : 谷歌
     * installDesc : null
     * toolLogo : null
     * toolURL : null
     * md5 : null
     * uploadId : null
     * fileName : null
     * fileSize : null
     * downloadCount : 0
     * appTypeId : 0
     * orderNo : 0
     * isDelete : 0
     * createTime : null
     * updateTime : null
     * auxiliaryTools : [{"id":1,"parentId":5,"toolName":"蓝灯VPN","installDesc":"阿道夫GV阿萨德发的了富华大厦vflgsadfg6546546
     * 、、、、、、打算的放弃","toolLogo":"http://oss.ngame.cn/upload/1466577288833.png","toolURL":"http://oss.ngame
     * .cn/upload/1466577288833.png","md5":"8E992EBA9BF0E6C22E818AD1D011D356","uploadId":null,"fileName":"vpn.zip",
     * "fileSize":null,"downloadCount":50,"appTypeId":0,"orderNo":2,"isDelete":0,"createTime":null,"updateTime":null,"auxiliaryTools":null}]
     */

    private int id;
    private int parentId;
    private String toolName;
    private Object installDesc;
    private Object toolLogo;
    private Object toolURL;
    private Object md5;
    private Object uploadId;
    private Object fileName;
    private Object fileSize;
    private int downloadCount;
    private int appTypeId;
    private int orderNo;
    private int isDelete;
    private Object createTime;
    private Object updateTime;
    private List<AuxiliaryToolsBean> auxiliaryTools;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public Object getInstallDesc() {
        return installDesc;
    }

    public void setInstallDesc(Object installDesc) {
        this.installDesc = installDesc;
    }

    public Object getToolLogo() {
        return toolLogo;
    }

    public void setToolLogo(Object toolLogo) {
        this.toolLogo = toolLogo;
    }

    public Object getToolURL() {
        return toolURL;
    }

    public void setToolURL(Object toolURL) {
        this.toolURL = toolURL;
    }

    public Object getMd5() {
        return md5;
    }

    public void setMd5(Object md5) {
        this.md5 = md5;
    }

    public Object getUploadId() {
        return uploadId;
    }

    public void setUploadId(Object uploadId) {
        this.uploadId = uploadId;
    }

    public Object getFileName() {
        return fileName;
    }

    public void setFileName(Object fileName) {
        this.fileName = fileName;
    }

    public Object getFileSize() {
        return fileSize;
    }

    public void setFileSize(Object fileSize) {
        this.fileSize = fileSize;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public int getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(int appTypeId) {
        this.appTypeId = appTypeId;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public List<AuxiliaryToolsBean> getAuxiliaryTools() {
        return auxiliaryTools;
    }

    public void setAuxiliaryTools(List<AuxiliaryToolsBean> auxiliaryTools) {
        this.auxiliaryTools = auxiliaryTools;
    }

    public static class AuxiliaryToolsBean {
        /**
         * id : 1
         * parentId : 5
         * toolName : 蓝灯VPN
         * installDesc : 阿道夫GV阿萨德发的了富华大厦vflgsadfg6546546    、、、、、、打算的放弃
         * toolLogo : http://oss.ngame.cn/upload/1466577288833.png
         * toolURL : http://oss.ngame.cn/upload/1466577288833.png
         * md5 : 8E992EBA9BF0E6C22E818AD1D011D356
         * uploadId : null
         * fileName : vpn.zip
         * fileSize : null
         * downloadCount : 50
         * appTypeId : 0
         * orderNo : 2
         * isDelete : 0
         * createTime : null
         * updateTime : null
         * auxiliaryTools : null
         */

        private int id;
        private int parentId;
        private String toolName;
        private String installDesc;
        private String toolLogo;
        private String toolURL;
        private String md5;
        private Object uploadId;
        private String fileName;
        private long fileSize;
        private int downloadCount;
        private int appTypeId;
        private int orderNo;
        private int isDelete;
        private Object createTime;
        private Object updateTime;
        private Object auxiliaryTools;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getToolName() {
            return toolName;
        }

        public void setToolName(String toolName) {
            this.toolName = toolName;
        }

        public String getInstallDesc() {
            return installDesc;
        }

        public void setInstallDesc(String installDesc) {
            this.installDesc = installDesc;
        }

        public String getToolLogo() {
            return toolLogo;
        }

        public void setToolLogo(String toolLogo) {
            this.toolLogo = toolLogo;
        }

        public String getToolURL() {
            return toolURL;
        }

        public void setToolURL(String toolURL) {
            this.toolURL = toolURL;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public Object getUploadId() {
            return uploadId;
        }

        public void setUploadId(Object uploadId) {
            this.uploadId = uploadId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public long getFileSize() {
            return fileSize;
        }

        public void setFileSize(long fileSize) {
            this.fileSize = fileSize;
        }

        public int getDownloadCount() {
            return downloadCount;
        }

        public void setDownloadCount(int downloadCount) {
            this.downloadCount = downloadCount;
        }

        public int getAppTypeId() {
            return appTypeId;
        }

        public void setAppTypeId(int appTypeId) {
            this.appTypeId = appTypeId;
        }

        public int getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(int orderNo) {
            this.orderNo = orderNo;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public Object getAuxiliaryTools() {
            return auxiliaryTools;
        }

        public void setAuxiliaryTools(Object auxiliaryTools) {
            this.auxiliaryTools = auxiliaryTools;
        }
    }
}
