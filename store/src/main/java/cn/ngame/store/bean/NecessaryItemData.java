package cn.ngame.store.bean;

/**
 * 用户登录的Token信息
 * Created by zeng on 2016/6/12.
 */
public class NecessaryItemData {

    private String parentId;
    private String parentText;
    private String itemTitle;
    private String itemPosition;
    private String itemDesc;

    public NecessaryItemData(String parentId, String parentText, String itemPosition, String itemTitle, String itemDesc) {
        this.parentId = parentId;
        this.parentText = parentText;
        this.itemPosition = itemPosition;
        this.itemTitle = itemTitle;
        this.itemDesc = itemDesc;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentText() {
        return parentText;
    }

    public void setParentText(String parentText) {
        this.parentText = parentText;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(String itemPosition) {
        this.itemPosition = itemPosition;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    @Override
    public String toString() {
        return "LangyaSimple{" +
                "parentId='" + parentId + '\'' +
                ", parentText='" + parentText + '\'' +
                ", itemTitle='" + itemTitle + '\'' +
                ", itemPosition='" + itemPosition + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                '}';
    }
}
