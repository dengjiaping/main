/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ngame.store.core.utils;

/**
 * APP中通用常量
 *
 * @author flan
 * @since 2016年5月3日
 */
public class Constant {

    //public static final String WEB_SITE = "http://192.168.0.233:10004";      //测试服务器
   public static final String WEB_SITE = "http://openapi.ngame.cn";
    //生产服务器
    //public static final String WEB_SITE = "http://120.27.200.73:9011";        //预发布数据
    public static final String WEB_SITE2 = "http://192.168.37.252:20004";      //h5测试服务器
    public static final String WEB_SITE3 = "http://files.ngame.cn";      //h5正式服务器
    /**
     * SharedPreferences 配置文件名
     */
    public static final String CONFIG_FILE_NAME = "Store.config";

    /**
     * 配置文件中属性名  是否第一次安装app
     */
    public static final String CONFIG_FIRST_INSTALL = "FirstInstall";
    /**
     * 用户登录成功的Token令牌
     */
    public static final String CONFIG_TOKEN = "Token";
    /**
     * 用户名
     */
    public static final String CONFIG_USER_NAME = "UserName";
    /**
     * 用户昵称
     */
    public static final String CONFIG_NICK_NAME = "NickName";
    /**
     * 用户密码
     */
    public static final String CONFIG_USER_PWD = "PassWord";
    /**
     * 用户头像地址
     */
    public static final String CONFIG_USER_HEAD = "HeadUrl";

    /**
     * 是否接收消息推送
     */
    public static final String CFG_RECEIVE_MSG = "ReceiveMsg";
    /**
     * 是否在安装后删除安装包
     */
    public static final String CFG_DELETE_APK = "DeleteApk";
    /**
     * 是否在有游戏更新时发出提醒
     */
    public static final String CFG_NOTIFY_GAME_UPDATE = "NotifyGameUpdate";
    /**
     * 是否允许使用运营商网络下载
     */
    public static final String CFG_ALLOW_4G_LOAD = "AllowLoadBy4G";

    /**
     * 消息推送的API key
     */
//    public static final String PUSH_API_KEY = "ncoPF1KvUO5CVcbqZkwvlXE1";       //测试key
    public static final String PUSH_API_KEY = "LUQUlTLy7fybX0oZOVeg9Pwh";       //生产key

    /**
     * APP首页 游戏总分类
     */
    public static final String URL_HOME_GAME_CATEGORY = "/game/gameSubTypeListByParentId";
    /**
     * APP首页 视频总分类
     */
    public static final String URL_SUBMIT_FEED_BACK_V4 = "/complaint/submitFeedbackV4";
    public static final String URL_DEL_FAVORITE = "/user/delFavorite";
    public static final String URL_ADD_FAVORITE = "/user/addFavorite";
    /**
     * VR页面列表数据
     */
    public static final String URL_VR_LIST = "/app/queryHotInfo";

    /**
     * 搜索
     */
    public static final String URL_SEARCH = "/app/search";
    public static final String URL_SEARCH_GAME_VIDEO = "/app/queryHotSearchGameAndVideo";

    /**
     * APP更新地址
     */
    public static final String URL_APP_UPDATE = "/app/queryCurrentAppVersion";

    /**
     * 游戏主页，获取游戏分类的URL
     */
    public static final String URL_GAME_CATEGORY = "/game/queryGameCategory";
    /**
     * 游戏lab
     */
    public static final String URL_GAME_LAB = "/game/gameSubTypeListByParentId";
    /**
     * 游戏主页，根据分类ID获取游戏列表的URL
     */
    public static final String URL_GAME_LIST = "/game/queryGameByTypeAndLabel";
    public static final String URL_LABEL_GAME_LIST = "/gameDiscovery/queryDiscoveryGameList";
    /**
     * 游戏排行
     */
    public static final String URL_GAME_RANK = "/game/gameRankList";

    /**
     * 获取首页游戏集合列表
     */
    public static final String URL_RECOMMEND_TOPICS = "/app/queryHomeSpecial";

    /**
     * 获取游戏精选列表
     */
    public static final String URL_GAME_SELECTION = "/game/queryGameSelect";

    /**
     * 获取游戏精选详情列表
     */
    public static final String URL_GAME_SELECTION_MORE = "/game/queryMoreSelection";
    /**
     * 游戏详情页，根据游戏ID获取游戏攻略
     */
    public static final String URL_GAME_STRATEGY = "/game/queryGameStrategy";
    /**
     * 游戏详情页，根据游戏ID获取游戏的详细信息
     */
    public static final String URL_GAME_DETAIL = "/game/queryGamesById";
    /**
     * 获取游戏键位图，及默认键位功能
     */
    public static final String URL_GAME_KEY = "/game/queryGameBitMapAndKeyDesc";
    /**
     * 统计下载次数
     */
    public static final String URL_GAME_CENSUS = "/game/censusGameDownload";

    /**
     * 视频label地址
     */
    public static final String URL_VIDEO_LABEL = "/video/queryVideoLabel";
    /**
     * 视频分类地址
     */
    public static final String URL_VIDEO_TYPE = "/video/queryVideoType";
    /**
     * 视频列表地址
     */
    public static final String URL_VIDEO_LIST = "/video/queryVideoByTypeAndLabel";
    /**
     * 视频排行榜列表地址
     */
    public static final String URL_VIDEO_RANK_LIST = "/video/videoRankList";
    /**
     * 视频详情
     */
    public static final String URL_VIDEO_DETAIL = "/video/queryVideoById";

    /**
     * 用户登录,获取Token值
     */
    public static final String URL_USER_LOGIN = "/user/userLogin";
    /**
     * 用户登录,获取用户信息
     */
    public static final String URL_USER_INFO = "/user/queryUserByToken";
    /**
     * 用户注册
     */
    public static final String URL_USER_REGISTER = "/user/userRegistration";
    /**
     * 注册时 获取短信验证码
     */
    public static final String URL_CAPTCHA = "/user/registerAuthCode";
    /**
     * 找回密码时 获取短信验证码
     */
    public static final String URL_FIND_CAPTCHA = "/user/findPasswordAuthCode";
    /**
     * 找回密码
     */
    public static final String URL_FIND_PWD = "/user/promFindPassword";
    /**
     * 修改昵称
     */
    public static final String URL_CHANGE_NICKNAME = "/user/modifyNickName";
    /**
     * 上传用户头像
     */
    public static final String URL_IMAGE_UPLOAD = "/user/uploadHeadPhoto";

    /**
     * 添加评论
     */
    public static final String URL_COMMENT_ADD_COMMENT = "/comment/addComment";
    /**
     * 获取评论数据
     */
    public static final String URL_COMMENT_LIST = "/comment/queryCommentList";
    /**
     * 意见反馈
     */
    public static final String URL_FEEDBACK = "/complaint/submitFeedback";
    /**
     * 意见反馈-图片上传
     */
    public static final String URL_FEEDBACK_FILE = "/complaint/uploadFeedbackPhoto";
    /**
     * 获取轮播广告
     */
    public static final String URL_BANNER = "/app/queryCarousel";
    //发现页头部轮播图
    public static final String URL_DISCOVER_BANNER = "/app/querySpecialCarousel";
    /**
     * 获取轮播广告2
     */
    public static final String URL_BANNER2 = "/app/queryAdvCarousel";

    /**
     * 查询推送消息的详情
     */
    public static final String URL_PUSH_MSG_DETAIL = "/message/queryMessageById";

    /**
     * 查询视频观看记录
     */
    public static final String URL_WATCH_RECORD_QUERY = "/video/queryVideoPlayRecordList";
    /**
     * 添加视频观看记录
     */
    public static final String URL_WATCH_RECORD_ADD = "/video/insertVideoPlayRecord";
    /**
     * 删除视频观看记录
     */
    public static final String URL_WATCH_RECORD_DELETE = "/video/deletePlayRecord";

    /**
     * game圈 删除浏览记录
     */
    public static final String URL_DELETE_BROWSE = "/gameCircle/deletePostBrowseRecord";
    /**
     * game圈 图片上传
     */
    public static final String URL_UP_LOAD_FILE = "/gameCircle/uploadPostPhoto";

    /**
     * 帮助与反馈 问题详情
     */
    public static final String URL_QUESTION_DETAIL = "/views/appHelp.html";
    /**
     * 系统消息 详情
     */
    public static final String URL_SYSTEM_DETAIL = "/views/systemMessage.html";

    /**
     * 各种map bundle的键值的key等标志
     * 是否存在 value = isexist
     */
    public static final String ISEXIST = "isexist";

    /**
     * 各种map bundle的键值的key等标志
     * 文件的存在状态 value = status
     */
    public static final String STATUS = "status";


    /**
     * 网络未连接
     */
    public static final int NET_STATUS_DISCONNECT = 0x0010;
    /**
     * 4G状态连接
     */
    public static final int NET_STATUS_4G = 0x0011;
    /**
     * wifi状态连接
     */
    public static final int NET_STATUS_WIFI = 0x0012;
    public static final String APP_TYPE_ID_0_ANDROID = "0";
    public static final String URL_FORGOT_REGIST_SMS_CODE = "/user/SMSAuthenticationCode";
    public static final String URL_MODIFY_USER_DATA = "/user/modifyUserData";
    public static final String CONFIG_LOGIN_TYPE = "loginType";
    public static final String CONFIG_USER_CODE = "config_user_code";
    public static final String PHONE = "1";
    public static final String QQ = "2";
    public static final String WEIXIN = "3";
    public static final String SINA = "4";//（1手机，2QQ，3微信，4新浪微博）
    public static final String FILE_NAME_SD_CRAD_APP_PKGNAME = "file_name_sd_crad_app_pkgname";
}
