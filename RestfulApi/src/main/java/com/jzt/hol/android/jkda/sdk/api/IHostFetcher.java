package com.jzt.hol.android.jkda.sdk.api;

/**
 * Created by tangkun on 16/9/3.
 */
public interface IHostFetcher {
   //String HOST = "192.168.0.233:10004/";      //测试服务器
    String HOST = "openapi.ngame.cn/";            //生产服务器
    //String HOST = "120.27.200.73:9011/;        //预发布数据
    String AppJson = "application/json";

    String S_PORT = ":8080";
    /**
     * @return
     */
    String getHOST_JKDA();
    /**
     * @return
     */
    String getHOST_MDS();
    /**
     * @return
     */
    String getHOST_MDS_IMG();
    /**
     *
     * @return
     */
    String getHOST_ANCHOR();
    /**
     *
     * @return
     */
    String getHOST_APK();
    /**
     *
     * @return
     */
    String getHOST_BOPS();
    /**
     *
     * @return
     */
    String getHOST_BOPS_REGISTERED();
    /**
     *
     * @return
     */
    String getHDF_REGISTERING();
    /**
     *
     * @return
     */
    String getHOST_BOPS_HEALTH_RECORD();
    /**
     *
     * @return
     */
    String getHOST_BOPS_DEMONSTRINE();
    /**
     *
     * @return
     */
    String getHOST_CMS();
    /**
     *
     * @return
     */
    String getHOST_CMS_H5();
    /**
     *
     * @return
     */
    String getHOST_PZ_HOME();
    /**
     *
     * @return
     */
    String getHOST_PE();
    /**
     *
     * @return
     */
    String getH5_HEAD();
    /**
     *
     * @return
     */
    String getH5_SEARCH();
    /**
     *
     * @return
     */
    String getANYCHAT();
    /**
     *
     * @return
     */
    String getHUANXINIM();
    /**
     *
     * @return
     */
    String getHDF_UPLOAD();
    /**
     *
     * @return
     */
    String getHOST_ANALYZER();
    /**
     *
     * @return
     */
    String getfHOST_JKDA_S_H5();
    /**
     *
     * @return
     */
    String getfHOST_BOPS_S_H5();
    /**
     *
     * @return
     */
    String getHost_OLD();
    /**
     *
     * @return
     */
    String getfHOST_MSG();
}
