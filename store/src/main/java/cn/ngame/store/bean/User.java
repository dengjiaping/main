package cn.ngame.store.bean;

import java.io.Serializable;

/**
 * 用户的实体类
 * Created by zeng on 2016/6/12.
 */
public class User implements Serializable {

    public long id;
    public String userCode;
    public String nickName;
    public String mobile;
    public String email;
    public String birthday;
    public String gender;
    public String headPhoto;
    public String idCard;

    public int status;

    public long updateTime;
    public long createTime;
}
