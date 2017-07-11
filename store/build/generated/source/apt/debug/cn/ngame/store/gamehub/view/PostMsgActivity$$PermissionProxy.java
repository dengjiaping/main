// Generated code. Do not modify!
package cn.ngame.store.gamehub.view;

import com.zhy.m.permission.*;

public class PostMsgActivity$$PermissionProxy implements PermissionProxy<PostMsgActivity> {
@Override
 public void grant(PostMsgActivity source , int requestCode) {
switch(requestCode) {case 23:source.requestSdcardReadSuccess();break;}  }
@Override
 public void denied(PostMsgActivity source , int requestCode) {
switch(requestCode) {case 23:source.requestSdcardReadFailed();break;}  }

}
