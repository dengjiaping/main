// Generated code. Do not modify!
package cn.ngame.store.base.activity;

import com.zhy.m.permission.*;

public class FeedbackActivity$$PermissionProxy implements PermissionProxy<FeedbackActivity> {
@Override
 public void grant(FeedbackActivity source , int requestCode) {
switch(requestCode) {case 23:source.requestSdcardReadSuccess();break;}  }
@Override
 public void denied(FeedbackActivity source , int requestCode) {
switch(requestCode) {case 23:source.requestSdcardReadFailed();break;}  }

}
