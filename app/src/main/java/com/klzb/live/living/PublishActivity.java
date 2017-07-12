package com.klzb.live.living;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.ksyun.media.rtc.kit.KSYRtcStreamer;
import com.ksyun.media.rtc.kit.RTCClient;
import com.ksyun.media.rtc.kit.RTCConstants;
import com.ksyun.media.streamer.capture.camera.CameraTouchHelper;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautyProFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.OnAudioRawDataListener;
import com.ksyun.media.streamer.kit.OnPreviewFrameListener;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.media.streamer.logstats.StatsLogReport;
import com.ksyun.media.streamer.util.audio.KSYBgmPlayer;
import com.meetme.android.horizontallistview.HorizontalListView;
import com.klzb.live.base.BaseActivity;
import com.smart.androidutils.images.GlideCircleTransform;
import com.smart.androidutils.utils.LogUtils;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.SoftKeyboardUtils;
import com.smart.androidutils.utils.StringUtils;
import com.smart.loginsharesdk.share.OnShareStatusListener;
import com.smart.loginsharesdk.share.ThirdShare;
import com.smart.loginsharesdk.share.onekeyshare.Type;

import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import me.wcy.lrcview.LrcView;
import com.klzb.live.LocationService;
import com.klzb.live.MyApplication;
import com.klzb.live.intf.OnCustomClickListener;
import com.klzb.live.intf.OnRequestDataListener;
import com.klzb.live.lean.Chat;
import com.klzb.live.lean.ConversationListActivity;
import com.klzb.live.login.LoginActivity;
import com.klzb.live.music.MusicActivity;
import com.klzb.live.music.MusicItem;
import com.klzb.live.music.MusicListAdapter;
import com.klzb.live.own.UserMainActivity;
import com.klzb.live.own.WebviewActivity;
import com.klzb.live.own.authorize.AuthorizeActivity;
import com.klzb.live.own.userinfo.ContributionActivity;
import com.klzb.live.utils.AVImClientManager;
import com.klzb.live.utils.Api;
import com.klzb.live.utils.DialogEnsureUtiles;
import com.klzb.live.utils.FunctionUtile;
import com.klzb.live.utils.Utile;
import com.klzb.live.view.LoveAnimView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.event.LCIMIMTypeMessageEvent;
import cn.leancloud.chatkit.event.LCIMInputBottomBarEvent;
import cn.leancloud.chatkit.event.LCIMInputBottomBarTextEvent;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.utils.LCIMNotificationUtils;
import cn.sharesdk.framework.Platform;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import com.klzb.live.R;

public class PublishActivity extends BaseActivity implements AdapterView.OnItemClickListener, ActivityCompat.OnRequestPermissionsResultCallback,OnShareStatusListener {

    private final static int PERMISSION_REQUEST_CAMERA_AUDIOREC = 1;
    public List<GiftSendModel> giftSendModelList = new ArrayList<GiftSendModel>();
    public GiftSendModel mGiftSendModel1;
    public GiftSendModel mGiftSendModel2;
    @Bind(R.id.sb_valume)
    SeekBar mSbValume;
    @Bind(R.id.yinxiao_container)
    FrameLayout mYinxiaoContainer;
    @Bind(R.id.sb_hongrun)
    SeekBar mSbHongrun;
    @Bind(R.id.sb_meibai)
    SeekBar mSbMeibai;
    @Bind(R.id.sb_mopi)
    SeekBar mSbMopi;
    @Bind(R.id.meiyan_container)
    FrameLayout mMeiyanContainer;
    @Bind(R.id.add_living_location)
    TextView addLivingLocation;
    @Bind(R.id.gift_layout1)
    GiftFrameLayout giftFrameLayout1;
    @Bind(R.id.gift_layout2)
    GiftFrameLayout giftFrameLayout2;
    @Bind(R.id.live_viewflipper)
    FrameLayout mLiveViewFlipper;
    DanmuAdapter mDanmuadapter;
    OnlineUserAdapter mOnlineUserAdapter;
    @Bind(R.id.live_share)
    ImageButton mLiveShare;
    @Bind(R.id.live_lianmai_num)
    TextView mLiveLianmaiNum;
    @Bind(R.id.HorizontalListView)
    HorizontalListView mLiveOnlineUsers;
    @Bind(R.id.live_gift)
    ImageButton mLiveGift;
    @Bind(R.id.live_meiyan)
    ImageButton mLiveMeiyan;
    @Bind(R.id.danmu_container)
    RelativeLayout mDanmuContainer;
    @Bind(R.id.living_danmu)
    ListView mLiveingDanmu;
    @Bind(R.id.live_user_avatar)
    CircleImageView mLiveUserAvatar;
    @Bind(R.id.live_user_nicename)
    TextView mLiveUserNicename;
    @Bind(R.id.input_live_title)
    EditText mInputLiveTitle;
    @Bind(R.id.live_user_online_num)
    TextView mLiveUserOnlineNum;
    @Bind(R.id.live_user_total)
    TextView mLiveUserTotal;
    @Bind(R.id.live_user_id)
    TextView mLiveUserId;
    @Bind(R.id.radio_share_wechat_moment)
    CheckBox mRadioShareWechatMoment;
    @Bind(R.id.radio_share_wechat)
    CheckBox mRadioShareWechat;
    @Bind(R.id.radio_share_sina)
    CheckBox mRadioShareSina;
    @Bind(R.id.radio_share_qq)
    CheckBox mRadioShareqq;
    @Bind(R.id.radio_share_zone)
    CheckBox mRadioShareZone;
    @Bind(R.id.live_bottom_btn)
    LinearLayout mLiveBottomBtn;
    @Bind(R.id.live_bottom_send)
    LinearLayout mLiveBottomSend;
    @Bind(R.id.live_edit_input)
    EditText mLiveEditInput;
    @Bind(R.id.live_btn_send)
    Button mLiveBtnSend;
    @Bind(R.id.live_top_layer)
    FrameLayout mLiveTopLayer;
    @Bind(R.id.music_stop)
    TextView mMusicStop;
    @Bind(R.id.music_value)
    TextView mMusicValue;
    @Bind(R.id.live_gift_container)
    LinearLayout mLiveGiftContainer;
    @Bind(R.id.living_gift_big)
    ImageView mLivingGiftBig;
    @Bind(R.id.live_gift_scroll)
    ScrollView mLiveGiftScroll;
    LinearLayout mUserDialogControlContainer;
    @Bind(R.id.image_own_message)
    ImageView mImageOwnMessage;
    @Bind(R.id.btn_follow)
    Button mBtnFollow;
    @Bind(R.id.image_own_unread)
    ImageView mImageOwnUnread;
    @Bind(R.id.camera_reverse)
    ImageView mCameraReverse;
    @Bind(R.id.live_music)
    ImageView mLiveMusic;
    @Bind(R.id.add_living)
    LinearLayout mAddLiving;
    @Bind(R.id.publish_more)
    LinearLayout mPublishMore;
    @Bind(R.id.text_topic)
    TextView mTextTopic;
    @Bind(R.id.lrc_small)
    LrcView mLrcSmall;
    @Bind(R.id.shanguan_container)
    FrameLayout mShanGuanContainer;
    @Bind(R.id.publish_shop_icon)
    ImageView mPublishShopIcon;
    CircleImageView mDialogUserAvatar;
    TextView mDialogUserNicename;
    ImageView mDialogClose;
    CircleImageView mDialogUserAvatarSmall;
    ImageView mDialogUserSex;
    TextView mDialogUserLevel;
    TextView mDialogUserId;
    TextView mDialogUserLocation;
    TextView mDialogUserSignature;
    TextView mDialogUserSpend;
    TextView mDialogUserAttentionNum;
    TextView mDialogUserFansNum;
    TextView mDialogPrivateMessage;
    TextView mDialogUserTotal;
    ImageView mDialogUserReal;
    TextView mDialogUserAttention;
    TextView mDialogUserMain;
    TextView mDialogCaozuo;
    TextView dialogUserChangkong;
    ImageView dialogAttentionImage;
    ImageView dialogChangkongImageOff;
    private final int MUSIC_CODE = 1;
    private final int TOPIC_CODE = 2;
    //live music

    @Bind(R.id.add_living_type)
    TextView mAddLivingType;
    //live end
    //实时数据
    Handler dataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    Boolean bgm = false;
    ArrayList<MusicItem> localMusic;
    MusicListAdapter localMusicList1;
    ArrayList<MusicItem> netMusic;
    MusicListAdapter netMusicList1;
    //礼物
    GiftSendModel mTempGiftSendModel1;
    GiftSendModel mTempGiftSendModel2;
    ArrayList<GiftSendModel> bigAnim = new ArrayList();
    AnimatorSet animatorSet1;
    AnimatorSet animatorSet2;
    private PopupWindow mPopupShareWindow;//类型选择
    private PopupWindow mPopupCaozuoWindow;//操作类型
    private String payMoney = "";
    private String minute_charge = "";
    private String privacy = "0";
    private String pass = "";
    private String shopUrl = "";
    private ThirdShare mThirdShare;
    private LocationService locationService;
    private LocationClient locationClient;
    private double mLatitude;
    private double mLongitude;
    private String mLocation;
    private Boolean ifLocation = true;
    //live start
    private boolean meiyan = false;
    private boolean slight = false;
    private GLSurfaceView mCameraPreviewView;
    private CameraTouchHelper cameraTouchHelper;
    //private TextureView mCameraPreviewView;
    private CameraHintView mCameraHintView;
    private KSYRtcStreamer mStreamer;
    private Handler mMainHandler;
    private Timer mTimer;
    private boolean mAutoStart = false;
    private boolean mIsLandscape;
    private boolean mPrintDebugInfo = false;
    private boolean mRecording = false;
    private boolean isFlashOpened = false;
    private String mUrl;
    private String mDebugInfo = "";
    private String mBgmPath = "/sdcard/test.mp3";
    private String mLogoPath = "file:///sdcard/test.png";
    private ArrayList<DanmuModel> mDanmuItems;
    private ArrayList<UserModel> mUserItems;
    private AVIMConversation mSquareConversation;
    private String TAG = LivingActivity.class.getName();
    private JSONObject liveInfo;
    private String user_nicename;
    private String user_level;
    private String avatar;
    private String otherUserId;
    private String otherUserName;
    private int onlineNum = 0;
    private boolean mIsTorchOn = false;
    private AlertDialog userInfoDialog;
    private String token;

    //rtc
    private AuthHttpTask mRTCAuthTask;
    private AuthHttpTask.KSYOnHttpResponse mRTCAuthResponse;
    private boolean mIsRegisted;
    private boolean mIsConnected;
    private boolean mHWEncoderUnsupported;
    private boolean mSWEncoderUnsupported;
    private final String RTC_AUTH_SERVER = "http://rtc.vcloud.ks-live.com:6002/rtcauth";
    private final String RTC_AUTH_URI = "https://rtc.vcloud.ks-live.com:6001";
    private final String RTC_UINIQUE_NAME = "apptest";
    @Bind(R.id.lianmai_stop)
    TextView mLianmaiStop;
    Runnable dataRunnable = new Runnable() {
        @Override
        public void run() {
            JSONObject params = new JSONObject();
            params.put("token", token);
            params.put("room_id", liveInfo.getString("room_id"));
            Api.getLiveRealTimeNum(PublishActivity.this, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data1) {
                    if (isActive) {
                        JSONObject data = data1.getJSONObject("data");
                        if (null != mLiveUserOnlineNum && null != mLiveUserTotal) {
                            //mLiveUserTotal.setText(data.getString("total_earn"));
                            // mLiveUserOnlineNum.setText(onlineNum + "");
                            //mLiveUserOnlineNum.setText(data.getString("online_num"));
                            dataHandler.postDelayed(dataRunnable, 10000);
                        }
                    }
                }

                @Override
                public void requestFailure(int code, String msg) {
                    switch (code) {
                        case 506:
                            toast("直播被封禁");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (isActive) {
                                        openActivity(PublishStopActivity.class);
                                        PublishActivity.this.finish();
                                    }
                                }
                            }, 2000);
                            break;
                        case 500:
                            toast(msg);
                            break;
                        default:
                            dataHandler.postDelayed(dataRunnable, 10000);
                            break;
                    }

                }
            });

        }
    };
    private String userId;//用户id
    private String channel_creater;//主播id
    private JSONArray sysMessage;
    private Boolean danmuChecked = false;
    private ArrayList<DanmuModel> lianmaiList;
    private KSYStreamer.OnInfoListener mOnInfoListener = new KSYStreamer.OnInfoListener() {
        @Override
        public void onInfo(int what, int msg1, int msg2) {
            switch (what) {
                case StreamerConstants.KSY_STREAMER_CAMERA_INIT_DONE:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_INIT_DONE");

                    break;
                case StreamerConstants.KSY_STREAMER_OPEN_STREAM_SUCCESS:
                    Log.d(TAG, "KSY_STREAMER_OPEN_STREAM_SUCCESS");
                    pushCallBack();
                    break;
                case StreamerConstants.KSY_STREAMER_FRAME_SEND_SLOW:
                    Log.d(TAG, "KSY_STREAMER_FRAME_SEND_SLOW " + msg1 + "ms");
                    toast("网络状态不好!");
                    break;
                case StreamerConstants.KSY_STREAMER_EST_BW_RAISE:
                    Log.d(TAG, "BW raise to " + msg1 / 1000 + "kbps");
                    break;
                case StreamerConstants.KSY_STREAMER_EST_BW_DROP:
                    Log.d(TAG, "BW drop to " + msg1 / 1000 + "kpbs");
                    break;
                default:
                    Log.d(TAG, "OnInfo: " + what + " msg1: " + msg1 + " msg2: " + msg2);
                    break;
            }
        }
    };
    private KSYStreamer.OnErrorListener mOnErrorListener = new KSYStreamer.OnErrorListener() {
        @Override
        public void onError(int what, int msg1, int msg2) {
            switch (what) {
                case StreamerConstants.KSY_STREAMER_ERROR_DNS_PARSE_FAILED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_DNS_PARSE_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_FAILED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_CONNECT_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_PUBLISH_FAILED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_PUBLISH_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_BREAKED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_CONNECT_BREAKED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_AV_ASYNC:
                    Log.d(TAG, "KSY_STREAMER_ERROR_AV_ASYNC " + msg1 + "ms");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
                    Log.d(TAG, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_SERVER_DIED");
                    break;
                default:
                    Log.d(TAG, "what=" + what + " msg1=" + msg1 + " msg2=" + msg2);
                    break;
            }
            switch (what) {
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    mStreamer.stopCameraPreview();
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startCameraPreviewWithPermCheck();
                        }
                    }, 5000);
                    break;
                default:
                    stopStream();
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startStream();
                        }
                    }, 3000);
                    break;
            }
        }
    };
    private StatsLogReport.OnLogEventListener mOnLogEventListener =
            new StatsLogReport.OnLogEventListener() {
                @Override
                public void onLogEvent(StringBuilder singleLogContent) {
                    Log.i(TAG, "***onLogEvent : " + singleLogContent.toString());
                    if("fail".equals(JSON.parseObject(singleLogContent.toString()).getString("streaming_stat")) && null != liveInfo){
                        toast("网络出现问题，请退出重试");
                    }
                }
            };
    private OnAudioRawDataListener mOnAudioRawDataListener = new OnAudioRawDataListener() {
        @Override
        public short[] OnAudioRawData(short[] data, int count) {
            Log.d(TAG, "OnAudioRawData data.length=" + data.length + " count=" + count);
            //audio pcm data
            return data;
        }
    };
    private OnPreviewFrameListener mOnPreviewFrameListener = new OnPreviewFrameListener() {
        @Override
        public void onPreviewFrame(byte[] data, int width, int height, boolean isRecording) {
            Log.d(TAG, "onPreviewFrame data.length=" + data.length + " " +
                    width + "x" + height + " isRecording=" + isRecording);
        }
    };
    private long lastClickTime = 0;
    private MyLocationListener mListener =  new MyLocationListener();
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                mLocation = location.getCountry() + "-" + location.getProvince() + "-" + location.getCity();
                if(StringUtils.isNotEmpty(location.getCity()))
                    addLivingLocation.setText(location.getCity());
                if (null != locationClient) {
                    locationClient.stop(); //停止定位服务
                }

            }

        }
    }

    private void pushCallBack(){
        JSONObject params = new JSONObject();
        params.put("token",token);
        params.put("action","push");
        Api.pushCallback(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                initData();
            }

            @Override
            public void requestFailure(int code, String msg) {
                initData();
            }
        });
    }
    int radioId = 0 ;
    @OnClick({R.id.radio_share_wechat_moment,R.id.radio_share_wechat,R.id.radio_share_sina,R.id.radio_share_qq,R.id.radio_share_zone} )
    public void shareRadio(CheckBox v){
        radioId = 0;
        switch (v.getId()){
            case R.id.radio_share_wechat:
                if(v.isChecked()){
                    radioId = v.getId();
                    setCheckByid(v);
                }
                break;
            case R.id.radio_share_sina:
                if(v.isChecked()){
                    radioId = v.getId();
                    setCheckByid(v);
                }
                break;
            case R.id.radio_share_qq:
                if(v.isChecked()){
                    radioId = v.getId();
                    setCheckByid(v);
                }
                break;
            case R.id.radio_share_wechat_moment:
                if(v.isChecked()){
                    radioId = v.getId();
                    setCheckByid(v);
                }
                break;
            case R.id.radio_share_zone:
                if(v.isChecked()){
                    radioId = v.getId();
                    setCheckByid(v);
                }
                break;
        }
    }
    private void setCheckByid(CheckBox c){
        mRadioShareWechatMoment.setChecked(false);
        mRadioShareWechat.setChecked(false);
        mRadioShareqq.setChecked(false);
        mRadioShareSina.setChecked(false);
        mRadioShareZone.setChecked(false);
        c.setChecked(true);
    }
    JSONObject info ;

    @OnClick(R.id.btn_start_living)
    public void btnStartLiveing(){
        String title = mInputLiveTitle.getText().toString();
//        if (StringUtils.isEmpty(title)) {
//            toast("先输入一个标题吧");
//            return;
//        }

        if (mThirdShare != null && radioId != 0) {
            JSONObject params = new JSONObject();
            params.put("token", token);
            params.put("room_id", token);
            Api.getShareInfo(this, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    info = data.getJSONObject("data");
                    switch (radioId) {
                        case R.id.radio_share_zone:
                            //toast("qzone");
                            mThirdShare.setTitle(info.getString("content"));
                            mThirdShare.setText(info.getString("content"));
                            mThirdShare.setTitleUrl(info.getString("shareUrl"));
                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
                            mThirdShare.setImageUrl(info.getString("pic"));
                            mThirdShare.share2QZone();
                            break;
                        case R.id.radio_share_qq:
                            //toast("qq");
                            mThirdShare.setTitle(info.getString("content"));
                            mThirdShare.setText(info.getString("content"));
                            mThirdShare.setTitleUrl(info.getString("shareUrl"));
                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
                            mThirdShare.setImageUrl(info.getString("pic"));
                            mThirdShare.share2QQ();
                            break;
                        case R.id.radio_share_sina:
                            //toast("weibo");
                            mThirdShare.setText(info.getString("content"));
                            mThirdShare.setImageUrl(info.getString("pic"));
                            mThirdShare.share2SinaWeibo(false);
                            break;
                        case R.id.radio_share_wechat:
                            //toast("wechat");
                            mThirdShare.setTitle(info.getString("content"));
                            mThirdShare.setText(info.getString("content"));
                            mThirdShare.setShareType(Type.SHARE_WEBPAGE);
                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
                            mThirdShare.setImageUrl(info.getString("pic"));
                            mThirdShare.setUrl(info.getString("shareUrl"));
                            mThirdShare.share2Wechat();
                            break;
                        case R.id.radio_share_wechat_moment:
                            //toast("wechat moment");
                            mThirdShare.setTitle(info.getString("content"));
                            mThirdShare.setText(info.getString("content"));
                            mThirdShare.setShareType(Type.SHARE_WEBPAGE);
                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
                            mThirdShare.setImageUrl(info.getString("pic"));
                            mThirdShare.setUrl(info.getString("shareUrl"));
                            mThirdShare.share2WechatMoments();
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                }
            });
        }else{
            startLive(title);
        }
    }

    @OnClick(R.id.lianmai_stop)
    public void lianmaiStop(View v){
        if(null != mStreamer){
            if(mIsConnected){
                mStreamer.getRtcClient().stopCall();
            }
            v.setVisibility(View.GONE);
        }
    }
    private void uploadLocation(){
        if (!StringUtils.isEmpty(token)) {
            JSONObject params = new JSONObject();
            params.put("token", token);
            if (mLongitude != 0 && mLatitude != 0) {
                params.put("longitude", mLongitude);
                params.put("latitude", mLatitude);
                params.put("location", mLocation);
                Api.setLocation(this, params, new OnRequestDataListener() {
                    @Override
                    public void requestSuccess(int code, JSONObject data) {

                    }

                    @Override
                    public void requestFailure(int code, String msg) {

                    }
                });
            }
        }
    }

    @OnClick(R.id.publish_shop_icon)
    public void shop(View v) {
        Bundle data1 = new Bundle();
        data1.putString("jump",shopInfo.getString("url"));
        data1.putString("title","");
        openActivity(WebviewActivity.class,data1);
    }

    @OnClick(R.id.live_user_avatar)
    public void getUserInfo(View view) {
        ImageView temp = (ImageView) view;
        String uid = (String) temp.getTag(R.id.image_live_avatar);
        otherUserId = uid;
        showUserInfoDialogById(uid);
    }

    @OnClick(R.id.text_topic)
    public void textTopic(View v) {
        Intent it = new Intent(this,TopicActivity.class);
        startActivityForResult(it,TOPIC_CODE);
        //openActivity(TopicActivity.class);
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        locationClient.setLocOption(option);
    }
    @OnClick(R.id.add_living_location)
    public void openLocation() {
        if (ifLocation) {
            addLivingLocation.setText("请稍后...");
            locationClient = ((MyApplication) getApplication()).locationClient;
            initLocation();
            locationClient.registerLocationListener(mListener);
            locationClient.start();
        } else {
            addLivingLocation.setText("开启定位");
            mLongitude = 0;
            locationClient.stop();
        }

        ifLocation = !ifLocation;
    }

    @OnCheckedChanged(R.id.danmu_check_box)
    public void danmuCheckChangerd(CompoundButton buttonView, boolean isChecked) {
        CheckBox temp = (CheckBox) buttonView;
        danmuChecked = temp.isChecked();
//        if (danmuChecked) {
//            mLiveEditInput.setHint("开启大喇叭，1金币/条");
//        } else {
            mLiveEditInput.setHint("我也要发言...");
//        }
    }
    private Handler lycHandler = new Handler();
    private Runnable lycRunnable = new Runnable() {
        @Override
        public void run() {
            if (!bgm && isActive) {
                long time = mStreamer.getAudioPlayerCapture().getBgmPlayer().getPosition();
                mLrcSmall.updateTime(time);
            }
            lycHandler.postDelayed(this, 100);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case MUSIC_CODE:
                    mBgmPath = data.getStringExtra("path");
                    if(StringUtils.isNotEmpty(mBgmPath)){
                        mStreamer.getAudioPlayerCapture().getBgmPlayer()
                                .setOnCompletionListener(new KSYBgmPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(KSYBgmPlayer bgmPlayer) {
                                        Log.d(TAG, "End of the currently playing music");
                                        mLrcSmall.setVisibility(View.GONE);
                                    }
                                });
                        mStreamer.getAudioPlayerCapture().getBgmPlayer()
                                .setOnErrorListener(new KSYBgmPlayer.OnErrorListener() {
                                    @Override
                                    public void onError(KSYBgmPlayer bgmPlayer, int what, int extra) {
                                        Log.e(TAG, "onBgmError: " + what);
                                    }
                                });
                        mStreamer.getAudioPlayerCapture().getBgmPlayer().setVolume(0.3f);
                        mStreamer.getAudioPlayerCapture().getBgmPlayer().setMute(false);
                        mStreamer.stopBgm();
                        mStreamer.startBgm(mBgmPath, true);
                        mLiveMusic.setImageResource(R.drawable.bgm_on);
                        mMusicStop.setVisibility(View.VISIBLE);
                        mMusicValue.setVisibility(View.VISIBLE);
//                        File lyc = new File(mBgmPath.replace("mp3","lrc"));
//                        if (lyc.exists()){
//                            mLrcSmall.loadLrc(new File(mBgmPath));
//                            lycHandler.post(lycRunnable);
//                        }
                        String aa = getLrcText1(mBgmPath);
                        mLrcSmall.loadLrc(aa);
                        lycHandler.post(lycRunnable);
                        mLrcSmall.setVisibility(View.VISIBLE);
                    }
                    break;
                case TOPIC_CODE:
                    TermModel term = (TermModel) data.getSerializableExtra("term");
                    mTextTopic.setText(term.getName());
                    mTextTopic.setTag(term.getTerm_id());
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getLrcText(String fileName) {
        String lrcText = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            lrcText = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcText;
    }

    private String getLrcText1(String fileName) {
        String fileName1 = fileName.replace("mp3","lrc");
        String lrcText = null;
        try {
            FileInputStream is = new FileInputStream(fileName1);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            lrcText = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcText;
    }

    @OnClick(R.id.music_stop)
    public void musicStop(){
        if(null != mStreamer){
            mStreamer.stopBgm();
            mMusicStop.setVisibility(View.GONE);
            mMusicValue.setVisibility(View.GONE);
            mLiveMusic.setImageResource(R.drawable.bgm_off);
            lycHandler.removeCallbacks(lycRunnable);
            mLrcSmall.setVisibility(View.GONE);
        }
    }
    //音效调节
    @OnClick(R.id.music_value)
    public void musicValue(){
        if(mYinxiaoContainer.getVisibility() == View.VISIBLE){
            mYinxiaoContainer.setVisibility(View.GONE);
        }else {
            mYinxiaoContainer.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.music_value_close)
    public void musicValueClose(){
        mYinxiaoContainer.setVisibility(View.GONE);
    }
    @OnClick(R.id.meiyan_value_close)

    public void meiyanValueClose(){
        mMeiyanContainer.setVisibility(View.GONE);
    }

    public void setBGM(String path) {
        bgm = !bgm;
        mStreamer.getAudioPlayerCapture().getBgmPlayer()
                .setOnCompletionListener(new KSYBgmPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(KSYBgmPlayer bgmPlayer) {
                        Log.d(TAG, "End of the currently playing music");
                    }
                });
        mStreamer.getAudioPlayerCapture().getBgmPlayer()
                .setOnErrorListener(new KSYBgmPlayer.OnErrorListener() {
                    @Override
                    public void onError(KSYBgmPlayer bgmPlayer, int what, int extra) {
                        Log.e(TAG, "onBgmError: " + what);
                    }
                });
        mStreamer.getAudioPlayerCapture().getBgmPlayer().setVolume(1.0f);
        mStreamer.getAudioPlayerCapture().getBgmPlayer().setMute(false);
        mStreamer.startBgm(path, true);
        mLiveMusic.setImageResource(R.drawable.bgm_on);
    }

    @OnClick(R.id.add_living_type)
    public void addLivingType() {
        int locX = 0;
        int locY = 0;
        if (mPopupShareWindow == null || !mPopupShareWindow.isShowing()) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.layout_shape_dialog_livetype, null);
            mPopupShareWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupShareWindow.setBackgroundDrawable(new ColorDrawable(0x50000000));
            mPopupShareWindow.setFocusable(true);
            mPopupShareWindow.setAnimationStyle(R.anim.bottom_in);
            mPopupShareWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, locX, locY);
            ImageView imagePublic = (ImageView) inflate.findViewById(R.id.image_live_public);
            ImageView imagePass = (ImageView) inflate.findViewById(R.id.image_live_pass);
            ImageView imagePay = (ImageView) inflate.findViewById(R.id.image_live_pay);
            ImageView imagePrivate = (ImageView) inflate.findViewById(R.id.image_live_private);
            ImageView imageMinute = (ImageView) inflate.findViewById(R.id.image_live_minute);
            TextView textView = (TextView) inflate.findViewById(R.id.tv_cancel);
            ClickListener clickListener = new ClickListener();
            textView.setOnClickListener(clickListener);
            imagePublic.setOnClickListener(clickListener);
            imagePass.setOnClickListener(clickListener);
            imagePay.setOnClickListener(clickListener);
            imagePrivate.setOnClickListener(clickListener);
            imageMinute.setOnClickListener(clickListener);
        } else {
            mPopupShareWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, locX, locY);
        }
    }

    @OnClick(R.id.live_music)
    public void livMusic(View view) {
        //openActivity(MusicActivity.class);
        Intent i = new Intent(this,MusicActivity.class);
        startActivityForResult(i,MUSIC_CODE);
    }

    @OnClick(R.id.live_more)
    public void liveMore(){
        if(mPublishMore.getVisibility() == View.VISIBLE){
            mPublishMore.setVisibility(View.GONE);
        }else {
            mPublishMore.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.my_changkong)
    public void myChangkong() {
        JSONObject params1 = new JSONObject();
        params1.put("token", token);
        Api.getManagerList(PublishActivity.this, params1, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                ArrayList<ManagerModel> managerList = new ArrayList<ManagerModel>();
                JSONArray list1 = data.getJSONArray("data");
                for (int i = 0; i < list1.size(); i++) {
                    ManagerModel aa = new ManagerModel();
                    JSONObject tempUser = list1.getJSONObject(i);
                    aa.setUserId(tempUser.getString("id"));
                    aa.setUserName(tempUser.getString("user_nicename"));
                    aa.setAvatar(tempUser.getString("avatar"));
                    managerList.add(aa);
                }
                LinearLayout linearLayoutMain = (LinearLayout) getLayoutInflater().inflate(R.layout.list_item_dialog_manager, null);
                ListView list = (ListView) linearLayoutMain.findViewById(R.id.manager_list);
                ManagerAdapter managerAdapter = new ManagerAdapter(PublishActivity.this, managerList);
                list.setAdapter(managerAdapter);
                AlertDialog dialog1 = new AlertDialog.Builder(PublishActivity.this)
                        .setTitle("我的场控").setView(linearLayoutMain)//在这里把写好的这个listview的布局加载dialog中
                        .create();
                dialog1.setCanceledOnTouchOutside(true);//使除了dialog以外的地方不能被点击
                dialog1.show();
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }
    PopupWindow mPopupShareWindow1;
    @OnClick(R.id.my_share)
    public void mgShare() {
        int locX = 0;
        int locY = 0;
        if (mPopupShareWindow1 == null || !mPopupShareWindow1.isShowing()) {
            View inflate = LayoutInflater.from(PublishActivity.this).inflate(R.layout.layout_shape_dialog, null);
            mPopupShareWindow1 = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupShareWindow1.setBackgroundDrawable(new ColorDrawable(0x50000000));
            mPopupShareWindow1.setFocusable(true);
            mPopupShareWindow1.showAtLocation(mLiveShare, Gravity.BOTTOM, locX, locY);
            ImageView shareQZone = (ImageView) inflate.findViewById(R.id.image_live_share_qzone);
            ImageView shareQQ = (ImageView) inflate.findViewById(R.id.image_live_share_qq);
            ImageView shareSina = (ImageView) inflate.findViewById(R.id.image_live_share_sina);
            ImageView shareWechat = (ImageView) inflate.findViewById(R.id.image_live_share_wechat);
            ImageView shareWechatMoment = (ImageView) inflate.findViewById(R.id.image_live_share_wechat_moment);
            TextView textView = (TextView) inflate.findViewById(R.id.tv_cancel);
            PublishActivity.ClickListener clickListener = new PublishActivity.ClickListener();
            textView.setOnClickListener(clickListener);
            shareQZone.setOnClickListener(clickListener);
            shareQQ.setOnClickListener(clickListener);
            shareSina.setOnClickListener(clickListener);
            shareWechat.setOnClickListener(clickListener);
            shareWechatMoment.setOnClickListener(clickListener);
        } else {
            mPopupShareWindow1.showAtLocation(mLiveShare, Gravity.BOTTOM, locX, locY);
        }
    }

    @OnClick(R.id.live_sidou)
    public void showUserContribution() {
        Bundle data = new Bundle();
        data.putString("id", channel_creater);
        openActivity(ContributionActivity.class, data);
    }

    public void showUserInfoDialogById(String uid) {
        if (StringUtils.isEmpty(uid)) {
            toast("uid数据有误");
            return;
        }
        if (userInfoDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PublishActivity.this);
            View inflate = LayoutInflater.from(this).inflate(R.layout.layout_userinfo_tips, null);
            mUserDialogControlContainer = (LinearLayout) inflate.findViewById(R.id.user_dialog_control_container);
            dialogUserChangkong = (TextView) inflate.findViewById(R.id.dialog_user_changkong);
//            dialogChangkongImageOff = (ImageView) inflate.findViewById(R.id.changkong_image_off);
            mDialogCaozuo = (TextView) inflate.findViewById(R.id.dialog_caozuo);
            mDialogUserAvatar = (CircleImageView) inflate.findViewById(R.id.dialog_user_avatar);
            mDialogUserAvatarSmall = (CircleImageView) inflate.findViewById(R.id.dialog_user_avatar_small);
            mDialogUserNicename = (TextView) inflate.findViewById(R.id.dialog_user_nicename);
            mDialogUserSex = (ImageView) inflate.findViewById(R.id.dialog_user_sex);
            mDialogUserLevel = (TextView) inflate.findViewById(R.id.dialog_user_level);
            mDialogUserId = (TextView) inflate.findViewById(R.id.dialog_user_id);
            mDialogUserLocation = (TextView) inflate.findViewById(R.id.dialog_user_location);
            mDialogUserSignature = (TextView) inflate.findViewById(R.id.dialog_user_signature);
            mDialogUserSpend = (TextView) inflate.findViewById(R.id.dialog_user_spend);
            mDialogUserAttentionNum = (TextView) inflate.findViewById(R.id.dialog_user_attention_num);
            mDialogUserFansNum = (TextView) inflate.findViewById(R.id.dialog_user_fans_num);
            mDialogUserTotal = (TextView) inflate.findViewById(R.id.dialog_user_total);
            mDialogUserReal = (ImageView) inflate.findViewById(R.id.dialog_user_real);
            mDialogUserAttention = (TextView) inflate.findViewById(R.id.dialog_user_attention);
//            dialogAttentionImage = (ImageView) inflate.findViewById(R.id.dialog_attention_image);
            mDialogPrivateMessage = (TextView) inflate.findViewById(R.id.dialog_user_private_message);
            mDialogUserMain = (TextView) inflate.findViewById(R.id.dialog_user_main);
            mDialogClose = (ImageView) inflate.findViewById(R.id.dialog_close);
            ClickListener clickListener = new ClickListener();
            mDialogClose.setOnClickListener(clickListener);
            mDialogUserAttention.setOnClickListener(clickListener);
            mDialogUserMain.setOnClickListener(clickListener);
            mDialogCaozuo.setOnClickListener(clickListener);
            mDialogPrivateMessage.setOnClickListener(clickListener);
            dialogUserChangkong.setOnClickListener(clickListener);
            userInfoDialog = builder.setView(inflate)
                    .create();
//            Window window = userInfoDialog.getWindow();
//            int dip2px = DensityUtils.dip2px(this, 40);
//            window.getDecorView().setPadding(dip2px, 0, dip2px, 0);
            Window window = userInfoDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        //dialogAttentionImage.setVisibility(View.VISIBLE);
        mUserDialogControlContainer.setVisibility(View.VISIBLE);
//        mDialogUserAttention.setVisibility(View.VISIBLE);
//        mDialogUserMain.setVisibility(View.VISIBLE);
//        mDialogPrivateMessage.setVisibility(View.VISIBLE);
        mDialogCaozuo.setVisibility(View.VISIBLE);
        mDialogUserAttention.setVisibility(View.VISIBLE);
        mDialogPrivateMessage.setVisibility(View.VISIBLE);
        //dialogChangkongImageOff.setImageResource(R.drawable.icon_changkong);
       // dialogUserChangkong.setTextColor(Color.BLACK);
        if (uid.equals(channel_creater)) {
            //mUserDialogControlContainer.setVisibility(View.INVISIBLE);
            mDialogCaozuo.setVisibility(View.GONE);
            mDialogUserAttention.setVisibility(View.GONE);
            //mDialogUserMain.setVisibility(View.INVISIBLE);
            mDialogPrivateMessage.setVisibility(View.GONE);
           // dialogUserChangkong.setVisibility(View.INVISIBLE);

        }
//        mDialogUserAttention.setText("关注");
//        mDialogJubao.setText("禁言");
//        dialogUserChangkong.setText("设为场控");
        userInfoDialog.show();
        getInfo(token, uid);
        //getFirstContribution(token, uid);
    }

    public void getFirstContribution(String token, String uid) {
        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("id", uid);
        params.put("limit_num", 1);
        Api.getUserContributionList(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                if(isActive){
                    JSONArray list = data.getJSONArray("data");
                    JSONObject item = list.getJSONObject(0);
                    Glide.with(PublishActivity.this).load(item.getString("avatar"))
                            .error(R.drawable.icon_avatar_default)
                            .transform(new GlideCircleTransform(PublishActivity.this))
                            .into(mDialogUserAvatarSmall);
                }

            }

            @Override
            public void requestFailure(int code, String msg) {
                //toast(msg);
                if(isActive){
                    Glide.with(PublishActivity.this).load(R.drawable.icon_avatar_default).into(mDialogUserAvatarSmall);
                }
            }
        });
    }

    public void getInfo(String token, String uid) {
        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("id", uid);
        Api.getUserInfo(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONObject userInfo = data.getJSONObject("data");
                Glide.with(PublishActivity.this).load(userInfo.getString("avatar"))
                        .error(R.drawable.icon_avatar_default)
                        .transform(new GlideCircleTransform(PublishActivity.this))
                        .into(mDialogUserAvatar);
                Glide.with(PublishActivity.this).load(userInfo.getString("avatar"))
                        .error(R.drawable.icon_avatar_default)
                        .transform(new GlideCircleTransform(PublishActivity.this))
                        .into(mDialogUserAvatarSmall);
                mDialogUserNicename.setText(userInfo.getString("user_nicename"));
                if ("1".equals(userInfo.getString("sex"))) {
                    mDialogUserSex.setImageResource(R.drawable.userinfo_male);
                }
                mDialogUserLevel.setText(userInfo.getString("user_level"));
                int level = Integer.parseInt(userInfo.getString("user_level"));
                if (level > 4 && level < 9) {
                    Drawable levelMoon = PublishActivity.this.getResources().getDrawable(R.drawable.moon);
                    levelMoon.setBounds(0, 0, levelMoon.getMinimumWidth(), levelMoon.getMinimumHeight());
                    mDialogUserLevel.setCompoundDrawables(levelMoon, null, null, null);
                }
                if (level > 8 && level < 13) {
                    Drawable levelMoon = PublishActivity.this.getResources().getDrawable(R.drawable.sun);
                    levelMoon.setBounds(0, 0, levelMoon.getMinimumWidth(), levelMoon.getMinimumHeight());
                    mDialogUserLevel.setCompoundDrawables(levelMoon, null, null, null);
                }
                if (level > 12) {
                    Drawable levelMoon = PublishActivity.this.getResources().getDrawable(R.drawable.top);
                    levelMoon.setBounds(0, 0, levelMoon.getMinimumWidth(), levelMoon.getMinimumHeight());
                    mDialogUserLevel.setCompoundDrawables(levelMoon, null, null, null);
                }
                mDialogUserId.setText(userInfo.getString("id"));
                mDialogUserLocation.setText(userInfo.getString("location"));
                if (StringUtils.isNotEmpty(userInfo.getString("signature")))
                    mDialogUserSignature.setText(userInfo.getString("signature"));
                mDialogUserSpend.setText(userInfo.getString("total_spend"));
                mDialogUserAttentionNum.setText(userInfo.getString("attention_num"));
                mDialogUserFansNum.setText(userInfo.getString("fans_num"));
                mDialogUserTotal.setText(userInfo.getString("sidou"));
                if ("1".equals(userInfo.getString("is_truename"))) {
                    mDialogUserReal.setVisibility(View.VISIBLE);
                }
                if ("1".equals(userInfo.getString("attention_status"))) {
                    mDialogUserAttention.setText("已关注");
                    mDialogUserAttention.setEnabled(false);
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {

                    if (!fromUser) {
                        return;
                    }
                    float val = progress / 100.f;
                    if (seekBar == mSbMopi) {
                        filter.setGrindRatio(val);
                    } else if (seekBar == mSbMeibai) {
                        filter.setWhitenRatio(val);
                    } else if (seekBar == mSbHongrun) {
                        if (filter instanceof ImgBeautyProFilter) {
                            val = progress / 50.f - 1.0f;
                        }
                        filter.setRuddyRatio(val);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            };

    @OnClick({R.id.live_meiyan, R.id.dl_image_meiyan})
    public void meiyan(View view) {
        if(mMeiyanContainer.getVisibility() == View.VISIBLE){
            mMeiyanContainer.setVisibility(View.GONE);
        }else {
            mMeiyanContainer.setVisibility(View.VISIBLE);
        }

//        meiyan = !meiyan;
//        if (meiyan) {
//            Drawable drawable_meiyan_off = PublishActivity.this.getResources().getDrawable(R.drawable.meiyan_on);
//            mLiveMeiyan.setImageDrawable(drawable_meiyan_off);
//            mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
//                    ImgTexFilterMgt.KSY_FILTER_BEAUTY_DENOISE);
//            mStreamer.setEnableImgBufBeauty(true);
//        } else {
//            Drawable drawable_meiyan_off = PublishActivity.this.getResources().getDrawable(R.drawable.meiyan_off);
//            mLiveMeiyan.setImageDrawable(drawable_meiyan_off);
//            mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
//                    ImgTexFilterMgt.KSY_FILTER_BEAUTY_DISABLE);
//            mStreamer.setEnableImgBufBeauty(false);
//        }
    }

    @OnClick(R.id.living_danmu_container)
    public void start(View view) {


    }

    @OnClick({R.id.live_close, R.id.dl_image_close})
    public void close(View view) {
        DialogEnsureUtiles.showConfirm(this, "确定退出直播间吗", new OnCustomClickListener() {
            @Override
            public void onClick(String value) {
                if(mRecording){
                    openActivity(PublishStopActivity.class);
                }
                PublishActivity.this.finish();
            }
        });
    }

    @OnClick(R.id.live_send)
    public void liveSend(View view) {
        mLiveBottomBtn.setVisibility(View.GONE);
        mLiveBottomSend.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) mLiveEditInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        mLiveEditInput.setFocusable(true);
        mLiveEditInput.setFocusableInTouchMode(true);
        mLiveEditInput.findFocus();
        mLiveEditInput.requestFocus();
    }

    @OnClick(R.id.image_own_message)
    public void imageOwnMessage(View v) {
        openActivity(ConversationListActivity.class);
    }

    @OnClick({R.id.camera_reverse, R.id.dl_image_camera})
    public void cameraReverse() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                onSwitchCamClick();
            }
        }).start();
    }

    @OnClick(R.id.camera_onoff)
    public void cameraOnOff() {
        if (mStreamer.isFrontCamera()) return;
        slight = !slight;
        if (slight) {

        } else {
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                onFlashClick();
            }
        }).start();
    }

    @OnClick(R.id.live_btn_send)
    public void liveBtnSend(final View view) {

        final String content = mLiveEditInput.getText().toString();
        if (StringUtils.isEmpty(content)) {
            toast("请先输入内容");
            return;
        }

        final DanmuModel model = new DanmuModel();
        if (danmuChecked) {
            view.setEnabled(false);
            JSONObject params = new JSONObject();
            params.put("token", token);
            params.put("room_id", liveInfo.getString("room_id"));
            params.put("giftid", "19");
            params.put("number", "1");
            Api.sendDanmu(this, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    view.setEnabled(true);
                    JSONObject result = data.getJSONObject("data");
                    MyApplication app = (MyApplication) getApplication();
                    app.setBalance((result.getString("balance")));
                    model.setUserName(user_nicename);
                    model.setUserLevel(user_level);
                    model.setUserId(userId);
                    model.setType("7");//弹幕
                    model.setAvatar(avatar);
                    model.setContent(content);
                    sendMessage(model);
                    showDanmuAnim(PublishActivity.this, model);
                }

                @Override
                public void requestFailure(int code, String msg) {
                    view.setEnabled(true);
                    toast(msg);
                }
            });
        } else {
            model.setType("1");
            model.setUserName(user_nicename);
            model.setUserLevel(user_level);
            model.setContent(content);
            model.setUserId(userId);
            model.setAvatar(avatar);
            sendMessage(model);
        }
    }

    public void sendMessage(DanmuModel model) {
        if (isActive && null != mDanmuItems) {
            if(!"100".equals(model.getType()) && !"21".equals(model.getType()) && !"22".equals(model.getType())){
                mDanmuItems.add(model);
                mDanmuadapter.notifyDataSetChanged();
                mLiveingDanmu.setSelection(mDanmuadapter.getCount() - 1);
                mLiveEditInput.setText("");
                mLiveBottomBtn.setVisibility(View.VISIBLE);
                mLiveBottomSend.setVisibility(View.GONE);
                SoftKeyboardUtils.closeSoftInputMethod(mLiveEditInput);
            }
        }
        String message = JSONObject.toJSONString(model);
        EventBus.getDefault().post(
                new LCIMInputBottomBarTextEvent(LCIMInputBottomBarEvent.INPUTBOTTOMBAR_SEND_TEXT_ACTION, message, liveInfo.getString("leancloud_room")));

    }

    public void showDanmuAnim(final PublishActivity temp, DanmuModel model) {
        final View giftPop = View.inflate(temp, R.layout.item_danmu_pop, null);
        ImageView giftAvatar = (ImageView) giftPop.findViewById(R.id.gift_pop_avatar);
        TextView giftUserName = (TextView) giftPop.findViewById(R.id.gift_pop_username);
        TextView giftContent = (TextView) giftPop.findViewById(R.id.gift_pop_content);
        Glide.with(temp).load(model.getAvatar())
                .error(R.drawable.icon_avatar_default)
                .into(giftAvatar);
        giftUserName.setText(model.getUserName());
        giftContent.setText(model.getContent());
        temp.mDanmuContainer.addView(giftPop);
        Animation anim = AnimationUtils.loadAnimation(temp, R.anim.danmu_enter);
        giftPop.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(isActive){
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            temp.mDanmuContainer.removeView(giftPop);
                        }
                    });

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    List<ImgFilterBase> filters ;
    ImgFilterBase filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = (String) SharePrefsUtils.get(this, "user", "token", "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //live start
        mCameraPreviewView = (GLSurfaceView) findViewById(R.id.camera_preview);
        mCameraHintView = (CameraHintView) findViewById(R.id.camera_hint);
        mMainHandler = new Handler();
        mStreamer = new KSYRtcStreamer(this);
        mStreamer.setPreviewFps(15);
        mStreamer.setTargetFps(15);
        int videoBitrate = 800;
        videoBitrate *= 1000;
        mStreamer.setVideoBitrate(videoBitrate * 3 / 4, videoBitrate, videoBitrate / 4);
        mStreamer.setAudioBitrate(48 * 1000);
        mStreamer.setPreviewResolution(StreamerConstants.VIDEO_RESOLUTION_480P);
        mStreamer.setTargetResolution(StreamerConstants.VIDEO_RESOLUTION_480P);
        mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mStreamer.setDisplayPreview(mCameraPreviewView);
        //mStreamer.setEnableStreamStatModule(true);
        mStreamer.enableDebugLog(true);
        mStreamer.setFrontCameraMirror(true);
        mStreamer.setMuteAudio(false);
        mStreamer.setEnableAudioPreview(false);
        mStreamer.setOnInfoListener(mOnInfoListener);
        mStreamer.setOnErrorListener(mOnErrorListener);
        mStreamer.setOnLogEventListener(mOnLogEventListener);
        //mStreamer.setOnAudioRawDataListener(mOnAudioRawDataListener);
        //mStreamer.setOnPreviewFrameListener(mOnPreviewFrameListener);
        mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                ImgTexFilterMgt.KSY_FILTER_BEAUTY_PRO);
        //mStreamer.setEnableImgBufBeauty(true);
        mStreamer.getImgTexFilterMgt().setOnErrorListener(new ImgTexFilterBase.OnErrorListener() {
            @Override
            public void onError(ImgTexFilterBase filter, int errno) {
                toast("当前机型不支持该滤镜");
                mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                        ImgTexFilterMgt.KSY_FILTER_BEAUTY_DISABLE);
            }
        });
        filters = mStreamer.getImgTexFilterMgt().getFilter();
        filter = filters.get(0);
        // touch focus and zoom support
        cameraTouchHelper = new CameraTouchHelper();
        cameraTouchHelper.setCameraCapture(mStreamer.getCameraCapture());
        mCameraPreviewView.setOnTouchListener(cameraTouchHelper);
        // set CameraHintView to show focus rect and zoom ratio
        cameraTouchHelper.setCameraHintView(mCameraHintView);
        //for rtc sub screen
        cameraTouchHelper.addTouchListener(mRTCSubScreenTouchListener);
        //live end

        //set rtc info
        mStreamer.getRtcClient().setRTCErrorListener(mRTCErrorListener);
        mStreamer.getRtcClient().setRTCEventListener(mRTCEventListener);
//        mNetworkStateReceiver = new NetworkStateReceiver();
//        mNetworkStateReceiver.addListener(new WeakReference<NetworkStateReceiver.NetworkStateReceiverListener>(this));
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        intentFilter.addAction(NetworkStateReceiver.CONNECTIVITY_ACTION_LOLLIPOP);
//        this.registerReceiver(mNetworkStateReceiver, intentFilter);
//        registerConnectivityActionLollipop();
        //rtc

        mLiveGift.setVisibility(View.GONE);
        mLiveShare.setVisibility(View.GONE);
        mBtnFollow.setVisibility(View.GONE);
        mLiveTopLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLiveBottomSend.getVisibility() == View.VISIBLE) {
                    mLiveBottomSend.setVisibility(View.GONE);
                    mLiveBottomBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        openLocation();

        mThirdShare = new ThirdShare(this);
        mThirdShare.setOnShareStatusListener(this);
        mSbValume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int ab = seekBar.getProgress();
                float a =(float) ab/100;
                mStreamer.getAudioPlayerCapture().getBgmPlayer().setVolume(a);
            }
        });
        mSbHongrun.setOnSeekBarChangeListener(seekBarChangeListener);
        mSbMeibai.setOnSeekBarChangeListener(seekBarChangeListener);
        mSbMopi.setOnSeekBarChangeListener(seekBarChangeListener);

    }
//    float x1 = 0;
//    float x2 = 0;
//    float y1 = 0;
//    float y2 = 0;
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            x1 = event.getX();
//            y1 = event.getY();
//        }
//        if(event.getAction() == MotionEvent.ACTION_UP) {
//            //当手指离开的时候
//            x2 = event.getX();
//            y2 = event.getY();
//            if(y1 - y2 > 50) {
//
//            } else if(y2 - y1 > 50) {
//
//            } else if(x1 - x2 > 50) {
//                mLiveViewFlipper.setVisibility(View.VISIBLE);
//
//            } else if(x2 - x1 > 50) {
//                mLiveViewFlipper.setVisibility(View.GONE);
//            }
//        }
//        return super.onTouchEvent(event);
//    }

    JSONObject shopInfo;
    private void initData() {
        mDanmuItems = new ArrayList<DanmuModel>();
        mUserItems = new ArrayList<UserModel>();
        if (null != sysMessage) {
            for (int i = 0; i < sysMessage.size(); i++) {
                DanmuModel model = new DanmuModel();
                model.setType("3");
                model.setUserName(sysMessage.getJSONObject(i).getString("title"));
                model.setContent(sysMessage.getJSONObject(i).getString("msg"));
                mDanmuItems.add(model);
            }

        }
        mDanmuadapter = new DanmuAdapter(this, mDanmuItems);
        mLiveingDanmu.setAdapter(mDanmuadapter);
        mOnlineUserAdapter = new OnlineUserAdapter(this, mUserItems);
        mLiveOnlineUsers.setAdapter(mOnlineUserAdapter);
        mLiveOnlineUsers.setOnItemClickListener(this);
        mLiveingDanmu.setAdapter(mDanmuadapter);
        mLiveingDanmu.setOnItemClickListener(this);

        userId = (String) SharePrefsUtils.get(this, "user", "userId", "");
        user_nicename = (String) SharePrefsUtils.get(this, "user", "user_nicename", "");
        user_level = (String) SharePrefsUtils.get(this, "user", "user_level", "");
        avatar = (String) SharePrefsUtils.get(this, "user", "avatar", "");
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(userId)) {
            joinChat(userId, liveInfo.getString("leancloud_room"));
            JSONObject requestParams = new JSONObject();
            requestParams.put("token", token);
            requestParams.put("id", userId);
            Api.getUserInfo(this, requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    if (isActive){
                        JSONObject userInfo = data.getJSONObject("data");
                        mLiveUserNicename.setText(userInfo.getString("user_nicename"));
                        mLiveUserId.setText("ID:" + userInfo.getString("id"));
                        channel_creater = userInfo.getString("id");
                        mLiveUserTotal.setText(userInfo.getString("sidou"));
                        Glide.with(PublishActivity.this).load(userInfo.getString("avatar"))
                                .error(R.drawable.icon_avatar_default)
                                .transform(new GlideCircleTransform(PublishActivity.this))
                                .into(mLiveUserAvatar);
                        getOnlineUsers();
                        mLiveUserAvatar.setTag(R.id.image_live_avatar, userInfo.getString("id"));
                        dataHandler.post(dataRunnable);
                        dataHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onRTCRegisterClick();
                            }
                        },3000);
                    }
                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                }
            });
        } else {
            toast("请重新登录");
            openActivity(LoginActivity.class);
            finish();
        }

        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("room_id", token);
        Api.getShareInfo(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                info = data.getJSONObject("data");
            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });
//        Api.getShop(this, new JSONObject(), new OnRequestDataListener() {
//            @Override
//            public void requestSuccess(int code, JSONObject data) {
//                shopInfo = data.getJSONObject("data");
//                if(StringUtils.isNotEmpty(shopInfo.getString("imgurl"))){
//                    mPublishShopIcon.setVisibility(View.VISIBLE);
//                    Glide.with(PublishActivity.this).load(shopInfo.getString("imgurl"))
//                            .error(R.drawable.icon_avatar_default)
//                            .into(mPublishShopIcon);
//                }
//            }
//
//            @Override
//            public void requestFailure(int code, String msg) {
//
//            }
//        });

    }

    private void startLive(String title){
        JSONObject params = new JSONObject();
        params.put("token",token);
        params.put("title", title);
        params.put("term_id","1");
        if(null == mTextTopic.getTag()){
            params.put("term_id","1");
        }else{
            params.put("term_id",mTextTopic.getTag());
        }

        params.put("price",payMoney);
        params.put("privacy",privacy);
        params.put("room_password",pass);
        params.put("minute_charge",minute_charge);
        Api.startLive(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                if(isActive){
                    mAddLiving.setVisibility(View.GONE);
                    mLiveViewFlipper.setVisibility(View.VISIBLE);
                    liveInfo = data.getJSONObject("data");
                    sysMessage = data.getJSONArray("msg");
                    if (liveInfo != null) {
                        mStreamer.setUrl(liveInfo.getString("push_rtmp"));
                        startStream();
                        uploadLocation();
                    } else {
                        toast("数据异常");
                    }
                }

            }

            @Override
            public void requestFailure(int code, String msg) {
                if(508 == code){
                    openActivity(AuthorizeActivity.class);
                }else{
                    toast(msg);
                }
            }
        });
    }

    private void getOnlineUsers() {
        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("room_id", liveInfo.getString("room_id"));
        Api.getOnlineUsers(PublishActivity.this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONArray users = JSON.parseArray(data.getString("data"));
                for (int i = 0; i < users.size(); i++) {
                    UserModel user = new UserModel();
                    user.setAvatar(users.getJSONObject(i).getString("avatar"));
                    user.setId(users.getJSONObject(i).getString("id"));
                    user.setUser_nicename(users.getJSONObject(i).getString("user_nicename"));
                    user.setLevel(users.getJSONObject(i).getString("user_level"));
                    mUserItems.add(user);
                    Collections.sort(mUserItems);
                    mOnlineUserAdapter.notifyDataSetChanged();
                }
                mLiveUserOnlineNum.setText(mUserItems.size()+"");
            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_publish;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUnread();
        EventBus.getDefault().register(this);
        startCameraPreviewWithPermCheck();
        mStreamer.onResume();
        if (mStreamer.isRecording() && !false) {
            mStreamer.setAudioOnly(false);
        }
        if (false) {
            showWaterMark();
        }
        if(null != mSquareConversation){
            DanmuModel model = new DanmuModel();
            model.setType("6");
            model.setUserName("系统消息");
            model.setAvatar(avatar);
            model.setUserId(userId);
            model.setContent("主播回来啦，视频即将恢复");
            AVIMTextMessage message = new AVIMTextMessage();
            message.setText(JSONObject.toJSONString(model));
            mSquareConversation.sendMessage(message, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {

                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        mStreamer.onPause();
        DanmuModel model = new DanmuModel();
        model.setType("5");
        model.setUserName("系统消息");
        model.setAvatar(avatar);
        model.setUserId(userId);
        model.setContent("主播离开一下，精彩不中断，不要走开哦");
        AVIMTextMessage message = new AVIMTextMessage();
        message.setText(JSONObject.toJSONString(model));
        if (mSquareConversation != null) {
            mSquareConversation.sendMessage(message, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {

                }
            });

        }
        mStreamer.stopCameraPreview();
        if (mStreamer.isRecording() && !false) {
            mStreamer.setAudioOnly(false);
        }
        hideWaterMark();
    }

    @Override
    protected void onStop() {
        if (null != locationClient) {
            locationClient.unRegisterLocationListener(mListener);
            locationClient.stop(); //停止定位服务
        }
        super.onStop();
    }

    protected boolean filterException(Exception e) {
        if (e != null) {
            e.printStackTrace();
            toast(e.getMessage());
            return false;
        } else {
            return true;
        }
    }

    /**
     * 输入事件处理，接收后构造成 AVIMTextMessage 然后发送
     * 因为不排除某些特殊情况会受到其他页面过来的无效消息，所以此处加了 tag 判断
     */
    public void onEvent(LCIMInputBottomBarTextEvent textEvent) {
        if (null != mSquareConversation && null != textEvent) {
            if (!TextUtils.isEmpty(textEvent.sendContent) && mSquareConversation.getConversationId().equals(textEvent.tag)) {
                AVIMTextMessage message = new AVIMTextMessage();
                message.setText(textEvent.sendContent);
                mSquareConversation.sendMessage(message, new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {

                    }
                });
            }
        }
    }

    public void onEvent(LCIMIMTypeMessageEvent event) {
        getUnread();
        if (null != mSquareConversation && null != event &&
                mSquareConversation.getConversationId().equals(event.conversation.getConversationId())) {
            JSONObject temp = JSON.parseObject(((AVIMTextMessage) event.message).getText());
            DanmuModel model = new DanmuModel();
            model.setType(temp.getString("type"));
            model.setUserName(temp.getString("userName"));
            model.setUserLevel(temp.getString("userLevel"));
            model.setContent(temp.getString("content"));
            model.setUserId(temp.getString("userId"));
            model.setAvatar(temp.getString("avatar"));

            //设置金额显示
            if(null != temp.getJSONObject("other")){
                JSONObject in = temp.getJSONObject("other");
                if(null != in.getString("totalEarn")){
                    mLiveUserTotal.setText(in.getString("totalEarn"));
                }
            }

            //拒绝连麦处理，离开
            if(!"101".equals(model.getType()) && !"5".equals(model.getType())){
                mDanmuItems.add(model);
                mDanmuadapter.notifyDataSetChanged();
                mLiveingDanmu.setSelection(mDanmuadapter.getCount() - 1);
            }

            switch (model.getType()) {
                case "4":
                    //clickHeart();
                    break;
                case "7":
                    showDanmuAnim(this, model);
                    break;
                case "8":
                    if (null == lianmaiList) {
                        lianmaiList = new ArrayList<>();
                    }
                    //最多连麦人数限制10
                    if (lianmaiList.size() >= 10) {
                        lianmaiList.remove(0);
                    }
                    for (int i = 0; i < lianmaiList.size(); i++) {
                        if (lianmaiList.get(i).getUserId().equals(model.getUserId())) {
                            lianmaiList.remove(i);
                            mLiveLianmaiNum.setText("" + lianmaiList.size());
                            return;
                        }

                    }
                    lianmaiList.add(model);
                    mLiveLianmaiNum.setText("" + lianmaiList.size());
                    break;
                case "6":
                    //系统消息  -  进入房间
                    UserModel user = new UserModel();
                    user.setAvatar(model.getAvatar());
                    user.setId(model.getUserId());
                    user.setUser_nicename(model.getUserName());
                    user.setLevel(model.getUserLevel());
                    mUserItems.add(user);
                    Collections.sort(mUserItems);
                    mOnlineUserAdapter.notifyDataSetChanged();
                    mLiveUserOnlineNum.setText(mUserItems.size()+"");
                   // showShanguangAnim(model);
                    break;
                case "2":
                    //toast("显示礼物画面");
                    JSONObject giftO = temp.getJSONObject("other");
                    giftO = giftO.getJSONObject("giftInfo");
                    GiftModel gift = new GiftModel();
                    gift.setGifticon(giftO.getString("gifticon"));
                    gift.setGiftname(giftO.getString("giftname"));
                    gift.setGiftid(giftO.getString("giftid"));
                    gift.setContinuous(giftO.getString("continuous"));
                    String num = (null == giftO.getString("continuousNum")) ? "1" : giftO.getString("continuousNum");
                    showGiftAnim1(PublishActivity.this, model, gift, Integer.parseInt(num));
                    break;
                case "5":
                    //系统消息  -  离开房间
                    UserModel utemp = new UserModel();
                    utemp.setId(model.getUserId());
                    mUserItems.remove(utemp);
                    mOnlineUserAdapter.notifyDataSetChanged();
                    mLiveUserOnlineNum.setText(mUserItems.size()+"");
                    break;
                case "101":
                    //拒绝连麦邀请
                    mLianmaiStop.setVisibility(View.GONE);
                    toast("对方拒绝了连麦邀请");
                    break;
            }
        }
    }

    private void showShanguangAnim(DanmuModel danmu){
        final View giftPop = View.inflate(this, R.layout.shangguan, null);
        TextView name = (TextView) giftPop.findViewById(R.id.enter_room_name);
        TextView textview_user_level = (TextView)giftPop.findViewById(R.id.danmu_text_user_level);

        int level = Integer.parseInt(danmu.getUserLevel());
        FunctionUtile.setLevel(this,textview_user_level,level);
//        if(level<5){
//            textview_user_level.setBackgroundResource(R.drawable.level1);
//        }
//        if(level>4 && level<9 ){
//            textview_user_level.setBackgroundResource(R.drawable.level2);
//        }
//        if(level>8 && level<13 ){
//            textview_user_level.setBackgroundResource(R.drawable.level3);
//        }
//        if(level>12 ){
//            textview_user_level.setBackgroundResource(R.drawable.level3);
//        }
        textview_user_level.setText(danmu.getUserLevel());
        name.setText("金光一闪,"+danmu.getContent());
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.in_leftright);
        giftPop.startAnimation(anim);
        mShanGuanContainer.addView(giftPop);
        Runnable giftTimer = new Runnable() {
            @Override
            public void run() {
                mShanGuanContainer.removeView(giftPop);
            }
        };
        giftPop.postDelayed(giftTimer, 2000);
    }

    private void getUnread(){
        int num = 0;
        List<String> convIdList = LCIMConversationItemCache.getInstance().getSortedConversationList();
        for (String id :convIdList){
            AVIMConversation conversation = LCChatKit.getInstance().getClient().getConversation(id);
            if(conversation.getMembers().size() != 2)
                continue;
            num += LCIMConversationItemCache.getInstance().getUnreadCount(conversation.getConversationId());
        }
        mImageOwnUnread.setVisibility(View.GONE);
        if(num > 0){
            mImageOwnUnread.setVisibility(View.VISIBLE);
        }
    }

    public void starGiftAnimation(GiftSendModel model) {
        LogUtils.i("delong", model.getGiftCount() + "");
        switch (model.getGift_id()) {
//            case "1"://黄瓜
//                break;
//            case "2"://啤酒
//                break;
//            case "3"://么么哒
//                break;
//            case "4"://钻戒
//                showBigAnim("gift_anim_jiezhi", model);
//                break;
//            case "5"://项链
//                break;
//            case "6"://小鲜肉
//                break;
//            case "7"://金麦克
//                break;
//            case "8"://跑车
//                showBigAnim("gift_anim_car", model);
//                break;
//            case "9"://岛屿
//                showBigAnim("gift_anim_haidao", model);
//                break;
//            case "12"://飞机
//                showBigAnim("gift_anim_feiji", model);
//                break;
//            case "13"://鲜花
//                showBigAnim("gift_anim_flower", model);
//                break;
//            case "21"://城堡
//                showBigAnim("gift_anim_chengbao", model);
//                break;
//            case "22"://求婚
//                showBigAnim("gift_anim_qiuhun", model);
//                break;
        }
        // layout1正在显示你的礼物动画//并且gift num  大于当前的num
        if (giftFrameLayout1.isShowing()) {
            GiftSendModel temp1 = giftFrameLayout1.getSendModel();
            if (null != temp1 && temp1.getGift_id().equals(model.getGift_id()) && temp1.getUserId().equals(model.getUserId()) && model.getGiftCount() > giftFrameLayout1.getRepeatCount()) {
                LogUtils.i("delong", model.getGiftCount() + "------" + mTempGiftSendModel1.getGiftCount());
                giftFrameLayout1.setRepeatCount(model.getGiftCount() - mTempGiftSendModel1.getGiftCount());
                mGiftSendModel1.setGiftCount(model.getGiftCount());
                return;
            }
        }
        //layout2正在显示你的礼物动画
        if (giftFrameLayout2.isShowing()) {
            GiftSendModel temp2 = giftFrameLayout2.getSendModel();
            if (null != temp2 && temp2.getGift_id().equals(model.getGift_id()) && temp2.getUserId().equals(model.getUserId()) && model.getGiftCount() > giftFrameLayout2.getRepeatCount()) {
                giftFrameLayout2.setRepeatCount(model.getGiftCount() - mTempGiftSendModel1.getGiftCount());
                mGiftSendModel2.setGiftCount(model.getGiftCount());
                return;
            }
        }
        if (!giftFrameLayout1.isShowing()) {

            sendGiftAnimation1(giftFrameLayout1, model);
            mGiftSendModel1 = model;
            mTempGiftSendModel1 = new GiftSendModel(model.getGiftCount());
            mTempGiftSendModel1.setGiftCount(model.getGiftCount());
        } else if (!giftFrameLayout2.isShowing()) {
            mTempGiftSendModel2 = new GiftSendModel(model.getGiftCount());
            mTempGiftSendModel2.setGiftCount(model.getGiftCount());
            sendGiftAnimation2(giftFrameLayout2, model);
            mGiftSendModel2 = model;
        } else {
            //如果两个都在显示 将礼物加入缓存队列  如果缓存队列有 就更新次数
            for (int i = 0; i < giftSendModelList.size(); i++) {
                if (giftSendModelList.get(i).getGift_id() == model.getGift_id() && giftSendModelList.get(i).getUserId() == model.getUserId()) {
                    giftSendModelList.get(i).setGiftCount(model.getGiftCount());
                    return;
                }
            }
            giftSendModelList.add(model);
        }
    }

    private void showBigAnim(String anim, final GiftSendModel model) {
        if (mLivingGiftBig.getVisibility() == View.VISIBLE) {
            model.setBigAnim(anim);
            bigAnim.add(model);
        } else {
            mLivingGiftBig.setVisibility(View.VISIBLE);
            mLivingGiftBig.setBackground(null);
            switch (anim) {
//                case "gift_anim_wallet":
//                    mLivingGiftBig.setImageDrawable(getResources().getDrawable(R.drawable.gift_anim_wallet));
//                    break;
                case "gift_anim_haidao":
                    mLivingGiftBig.setImageDrawable(getResources().getDrawable(R.drawable.gift_anim_haidao));
                    break;
                case "gift_anim_chengbao":
                    mLivingGiftBig.setImageDrawable(getResources().getDrawable(R.drawable.gift_anim_chengbao));
                    break;
                case "gift_anim_qiuhun":
                    mLivingGiftBig.setImageDrawable(getResources().getDrawable(R.drawable.gift_anim_qiuhun));
                    break;
                case "gift_anim_feiji":
                    mLivingGiftBig.setImageDrawable(getResources().getDrawable(R.drawable.gift_anim_feiji));
                    break;
                case "gift_anim_jiezhi":
                    mLivingGiftBig.setImageDrawable(getResources().getDrawable(R.drawable.gift_anim_jiezhi));
                    break;
//                case "gift_anim_dangao":
//                    mLivingGiftBig.setImageDrawable(getResources().getDrawable(R.drawable.gift_anim_dangao));
//                    break;
                case "gift_anim_car":
                    mLivingGiftBig.setImageDrawable(getResources().getDrawable(R.drawable.gift_anim_car));
                    break;
                case "gift_anim_flower":
                    mLivingGiftBig.setImageDrawable(getResources().getDrawable(R.drawable.gift_anim_flower));
                    break;
//                case "gift_anim_qiuhun":
//                    setAnimationDraw("qh000100",24);
//                    break;
            }
            AnimationDrawable animTemp = (AnimationDrawable) mLivingGiftBig.getDrawable();
            animTemp.start();
            int duration = 0;
            for (int i = 0; i < animTemp.getNumberOfFrames(); i++) {
                duration += animTemp.getDuration(i);
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mLivingGiftBig.setVisibility(View.GONE);
                    if (null != bigAnim && bigAnim.size() > 0) {
                        GiftSendModel model1 = bigAnim.get(bigAnim.size() - 1);
                        bigAnim.remove(bigAnim.size() - 1);
                        showBigAnim(model1.getBigAnim(), model1);
                    } else {
                        mLivingGiftBig.setVisibility(View.GONE);
                    }

                }
            }, duration);
        }
    }

    private void sendGiftAnimation1(final GiftFrameLayout view, final GiftSendModel model) {
        view.setModel(model);
        //判断前面一个是否是你送的  并且是同一个礼物
        if (null != mGiftSendModel1 && mGiftSendModel1.getGift_id().equals(model.getGift_id()) && mGiftSendModel1.getUserId().equals(model.getUserId())) {
            //设置开始
            if (model.getGiftCount() > 1)
                view.setStarNum(mGiftSendModel1.getGiftCount() + 1);
            //view.setRepeatCount(model.getGiftCount() - mGiftSendModel1.getGiftCount() -1);
            int tem = model.getGiftCount() - mGiftSendModel1.getGiftCount() - 1;
            view.setRepeatCount(tem > 0 ? tem : 0);
            animatorSet1 = view.startAnimation();
        } else {
            view.setStarNum(1);
            animatorSet1 = view.startAnimation();
        }
        animatorSet1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                synchronized (giftSendModelList) {
                    if (giftSendModelList.size() > 0) {
                        mGiftSendModel1 = giftSendModelList.get(giftSendModelList.size() - 1);
                        sendGiftAnimation1(view, giftSendModelList.get(giftSendModelList.size() - 1));
                        //view.setModel(giftSendModelList.get(giftSendModelList.size() - 1));
                        //view.startAnimation(giftSendModelList.get(giftSendModelList.size() - 1).getGiftCount()-1);
                        giftSendModelList.remove(giftSendModelList.size() - 1);
                    }
                }
            }
        });
    }

    private void sendGiftAnimation2(final GiftFrameLayout view, GiftSendModel model) {
        view.setModel(model);
        //判断前面一个是否是你送的  并且是同一个礼物
        if (null != mGiftSendModel2 && mGiftSendModel2.getGift_id() == model.getGift_id() && mGiftSendModel2.getUserId() == model.getUserId()) {
            //设置开始
            if (model.getGiftCount() > 1)
                view.setStarNum(mGiftSendModel2.getGiftCount() + 1);
            //view.setRepeatCount(model.getGiftCount() - mGiftSendModel1.getGiftCount() -1);
            //view.setRepeatCount(model.getGiftCount() - mGiftSendModel2.getGiftCount() -1);
            int tem = model.getGiftCount() - mGiftSendModel2.getGiftCount() - 1;
            view.setRepeatCount(tem > 0 ? tem : 0);
            animatorSet2 = view.startAnimation();
        } else {
            view.setStarNum(1);
            animatorSet2 = view.startAnimation();
        }
        animatorSet2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                synchronized (giftSendModelList) {
                    if (giftSendModelList.size() > 0) {
                        mGiftSendModel2 = giftSendModelList.get(giftSendModelList.size() - 1);
                        sendGiftAnimation2(view, giftSendModelList.get(giftSendModelList.size() - 1));
                        //view.setModel(giftSendModelList.get(giftSendModelList.size() - 1));
                        //view.startAnimation(giftSendModelList.get(giftSendModelList.size() - 1).getGiftCount()-1);
                        giftSendModelList.remove(giftSendModelList.size() - 1);
                    }
                }
            }
        });
    }

    public void showGiftAnim1(final PublishActivity temp, DanmuModel danmuModel, GiftModel giftModel, int num) {
        GiftSendModel modelTemp = new GiftSendModel(num);
        modelTemp.setGiftCount(num);
        modelTemp.setGift_id(giftModel.getGiftid());
        modelTemp.setUserId(danmuModel.getUserId());
        modelTemp.setNickname(danmuModel.getUserName());
        modelTemp.setSig(danmuModel.getContent());
        modelTemp.setUserAvatarRes(danmuModel.getAvatar());
        modelTemp.setGiftRes(giftModel.getGifticon());
        temp.starGiftAnimation(modelTemp);
    }

    public void showGiftAnim(final PublishActivity temp, DanmuModel danmuModel, GiftModel model, int num) {
        final View giftPop = View.inflate(temp, R.layout.item_gift_pop, null);
        ImageView giftAvatar = (ImageView) giftPop.findViewById(R.id.gift_pop_avatar);
        ImageView giftImage = (ImageView) giftPop.findViewById(R.id.gift_pop_gift);
        TextView giftUserName = (TextView) giftPop.findViewById(R.id.gift_pop_username);
        TextView giftContent = (TextView) giftPop.findViewById(R.id.gift_pop_content);
        TextView giftPopX = (TextView) giftPop.findViewById(R.id.gift_pop_x);
        TextView giftPopNum = (TextView) giftPop.findViewById(R.id.gift_pop_num);
        if (num == 1) {
            giftPopX.setVisibility(View.INVISIBLE);
            giftPopNum.setVisibility(View.INVISIBLE);
        } else {
            giftPopNum.setText(num + "");
        }
        Glide.with(temp).load(danmuModel.getAvatar())
                .error(R.drawable.icon_avatar_default)
                .into(giftAvatar);
        Glide.with(temp).load(model.getGifticon())
                .error(R.drawable.icon_avatar_default)
                .into(giftImage);
        giftUserName.setText(danmuModel.getUserName());
        giftContent.setText("赠送一个" + model.getGiftname());
        Animation anim = AnimationUtils.loadAnimation(temp, R.anim.gift_enter);
        giftPop.startAnimation(anim);
        temp.mLiveGiftContainer.addView(giftPop);
        Runnable giftTimer = new Runnable() {
            @Override
            public void run() {
                temp.mLiveGiftContainer.removeView(giftPop);
            }
        };
        giftPop.postDelayed(giftTimer, 2000);
        temp.mLiveGiftScroll.fullScroll(temp.mLiveGiftScroll.FOCUS_DOWN);
    }

    public void clickHeart() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LoveAnimView loveAnimView = new LoveAnimView(this);
        loveAnimView.setLayoutParams(params);
        loveAnimView.setStartPosition(new Point(530, 712));
        loveAnimView.setEndPosition(new Point(530 - (int) (Math.random() * 200), 712 - ((int) (Math.random() * 500) + 200)));
        loveAnimView.startLoveAnimation();
        mLiveTopLayer.addView(loveAnimView);

    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mLastX = ev.getRawX();
//                mLastY = ev.getRawY();
//                break;
//            case MotionEvent.ACTION_MOVE: {
//                float mX = ev.getRawX();
//                float mY = ev.getRawY();
//                float disX = Math.abs(mX - mLastX);
//                float disY = Math.abs(mY - mLastY);
//                if (disY > disX) {
//                    return super.dispatchTouchEvent(ev);
//                }
//            }
//            break;
//            case MotionEvent.ACTION_UP: {
//                float mX = ev.getRawX();
//                float mY = ev.getRawY();
//                float disX = Math.abs(mX - mLastX);
//                float disY = Math.abs(mY - mLastY);
//                if (!mLiveOnlineUsers.isHasScroll() ) {
//                    if (disX > 100 && disX > disY) {
//                        if (mX - mLastX > 0) {
//                            //向右滑动
//                            mLiveTopLayer.setVisibility(View.GONE);
//                        } else {
//                            //向左滑动
//                            mLiveTopLayer.setVisibility(View.VISIBLE);
//                        }
//                        return super.dispatchTouchEvent(ev);
//                    }
//                }
//                mLiveOnlineUsers.setHasScroll(false);
//            }
//            break;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lycHandler.removeCallbacks(lycRunnable);
        dataHandler.removeCallbacks(dataRunnable);
        DanmuModel model = new DanmuModel();
        model.setType("20");
        model.setUserName("系统消息");
        model.setAvatar(avatar);
        model.setUserId(userId);
        model.setContent(user_nicename + "结束直播");
        AVIMTextMessage message = new AVIMTextMessage();
        message.setText(JSONObject.toJSONString(model));
        if (null != mSquareConversation) {
            mSquareConversation.sendMessage(message, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {

                }
            });
            mSquareConversation.quit(new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {

                }
            });
        }
        cameraTouchHelper.removeAllTouchListener();
        if (mIsConnected) {
            mStreamer.getRtcClient().stopCall();
        }

        if (mIsRegisted) {
            mStreamer.getRtcClient().unRegisterRTC();
        }

        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        mStreamer.release();
    }

    private void startStream() {
        mStreamer.startStream();
        mRecording = true;
    }

    private void stopStream() {
        mStreamer.stopStream();
        mRecording = false;
    }

    private void showWaterMark() {
        if (!mIsLandscape) {
            mStreamer.showWaterMarkLogo(mLogoPath, 0.08f, 0.04f, 0.20f, 0, 0.8f);
            mStreamer.showWaterMarkTime(0.03f, 0.01f, 0.35f, Color.RED, 1.0f);
        } else {
            mStreamer.showWaterMarkLogo(mLogoPath, 0.05f, 0.09f, 0, 0.20f, 0.8f);
            mStreamer.showWaterMarkTime(0.01f, 0.03f, 0.22f, Color.RED, 1.0f);
        }
    }

    private void hideWaterMark() {
        mStreamer.hideWaterMarkLogo();
        mStreamer.hideWaterMarkTime();
    }

    private void onFlashClick() {
        if (isFlashOpened) {
            mStreamer.toggleTorch(false);
            isFlashOpened = false;
        } else {
            mStreamer.toggleTorch(true);
            isFlashOpened = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void startCameraPreviewWithPermCheck() {
        int cameraPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int audioPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (cameraPerm != PackageManager.PERMISSION_GRANTED ||
                audioPerm != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Log.e(TAG, "No CAMERA or AudioRecord permission, please check");
                Toast.makeText(this, "应用需要相机或录音的权限",
                        Toast.LENGTH_LONG).show();
            } else {
                String[] permissions = {Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions,
                        PERMISSION_REQUEST_CAMERA_AUDIOREC);
            }
        } else {
            mStreamer.startCameraPreview();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA_AUDIOREC: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mStreamer.startCameraPreview();
                } else {
                    Log.e(TAG, "No CAMERA or AudioRecord permission");
                    Toast.makeText(this, "应用需要相机或录音的权限",
                            Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                DialogEnsureUtiles.showConfirm(this, "确定退出直播间吗", new OnCustomClickListener() {
                    @Override
                    public void onClick(String value) {
                        //finish();
                        if(mRecording){
                            openActivity(PublishStopActivity.class);
                        }
                        PublishActivity.this.finish();
                    }
                });
            }
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void joinChat(String ClientId, final String conversationId) {
        LCChatKit.getInstance().open(ClientId, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                AVIMClient client = LCChatKit.getInstance().getClient();
                if (null != client) {
                    mSquareConversation = client.getConversation(conversationId);
                    LCIMNotificationUtils.addTag(conversationId);
                    joinSquare();
                } else {
                    toast("Please call open first!");
                }
            }
        });
    }

    /**
     * 加入 conversation
     */
    private void joinSquare() {
        mSquareConversation.join(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (filterException(e)) {

                }
            }
        });
    }

    /**
     * 先查询自己是否已经在该 conversation，如果存在则直接给 chatFragment 赋值，否则先加入，再赋值
     */
    private void queryInSquare(String conversationId) {
        final AVIMClient client = AVImClientManager.getInstance().getClient();
        AVIMConversationQuery conversationQuery = client.getQuery();
        conversationQuery.whereEqualTo("objectId", conversationId);
        conversationQuery.containsMembers(Arrays.asList(AVImClientManager.getInstance().getClientId()));
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
                if (list.size() == 0) {

                } else {
                    joinSquare();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter() instanceof DanmuAdapter) {
            DanmuModel model = (DanmuModel) adapterView.getAdapter().getItem(i);
            if (null != model.getUserId()) {
                //toast(model.getUserId());
                otherUserId = model.getUserId();
                showUserInfoDialogById(otherUserId);
            }
        }
        if (adapterView.getAdapter() instanceof OnlineUserAdapter) {
            UserModel model = (UserModel) adapterView.getAdapter().getItem(i);
            if (null != model.getId()) {
                //toast(model.getId());
                otherUserId = model.getId();
                showUserInfoDialogById(otherUserId);
            }
        }
    }

    private void onSwitchCamClick() {
        long curTime = System.currentTimeMillis();
        if (curTime - lastClickTime < 1000) {
            return;
        }
        lastClickTime = curTime;
        mStreamer.switchCamera();

    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(final View view) {
            switch (view.getId()) {
                case R.id.dialog_close:
                    if (userInfoDialog != null) {
                        userInfoDialog.dismiss();
                    }
                    break;
                case R.id.dialog_user_main:
                    Bundle data = new Bundle();
                    data.putString("id", otherUserId);
                    openActivity(UserMainActivity.class, data);
                    break;
                case R.id.dialog_user_attention:
                    JSONObject requestParams = new JSONObject();
                    requestParams.put("token", token);
                    requestParams.put("userid", otherUserId);
                    Api.addAttention(PublishActivity.this, requestParams, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            TextView v = (TextView) view;
                            v.setText("已关注");
                            v.setEnabled(false);
                        }

                        @Override
                        public void requestFailure(int code, String msg) {
                            toast(msg);
                        }
                    });
                    break;
                case R.id.dialog_caozuo:
                    userInfoDialog.dismiss();
                    int locX = 0;
                    int locY = 0;
                    if (mPopupCaozuoWindow == null || !mPopupCaozuoWindow.isShowing()) {
                        View inflate = LayoutInflater.from(PublishActivity.this).inflate(R.layout.layout_shape_dialog_caozuo, null);
                        mPopupCaozuoWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        mPopupCaozuoWindow.setBackgroundDrawable(new ColorDrawable(0xe5000000));
                        mPopupCaozuoWindow.setFocusable(true);
                        mPopupCaozuoWindow.setAnimationStyle(R.anim.bottom_in);
                        mPopupCaozuoWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, locX, locY);
                        ImageView imageLianmai = (ImageView) inflate.findViewById(R.id.image_live_lianmai);
                        ImageView imageChangkong = (ImageView) inflate.findViewById(R.id.image_live_changkong);
                        ImageView imageJinyan = (ImageView) inflate.findViewById(R.id.image_live_jinyan);
                        TextView textView = (TextView) inflate.findViewById(R.id.caozuo_cancel);
                        ClickListener clickListener = new ClickListener();
                        textView.setOnClickListener(clickListener);
                        imageLianmai.setOnClickListener(clickListener);
                        imageChangkong.setOnClickListener(clickListener);
                        imageJinyan.setOnClickListener(clickListener);
                    } else {
                        mPopupCaozuoWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, locX, locY);
                    }
                    break;
                case R.id.dialog_user_private_message:
                    LCChatKit.getInstance().open(userId, new AVIMClientCallback() {
                        @Override
                        public void done(AVIMClient avimClient, AVIMException e) {
                            if (null == e) {
                                Intent intent = new Intent(PublishActivity.this, Chat.class);
                                intent.putExtra(LCIMConstants.PEER_ID, otherUserId);
                                startActivity(intent);
                            } else {
                                toast(e.toString());
                            }
                        }
                    });
                    break;
                case R.id.camera_reverse:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            onSwitchCamClick();
                        }
                    }).start();
                    break;
                case R.id.camera_onoff:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            onFlashClick();
                        }
                    }).start();
                    break;
                case R.id.manager_jinyan:

                    break;
                case R.id.manager_changkong:
                    final Button a = (Button) view;
                    a.setEnabled(false);
                    JSONObject temp1 = new JSONObject();
                    temp1.put("token", token);
                    temp1.put("id", otherUserId);
                    if ("设为场控".equals(a.getText())) {
                        Api.setManager(PublishActivity.this, temp1, new OnRequestDataListener() {
                            @Override
                            public void requestSuccess(int code, JSONObject data) {
                                a.setEnabled(true);
                                a.setText("取消场控");
                                a.setTextColor(getResources().getColor(R.color.colorPrimary));
                                //dialogChangkongImageOff.setImageResource(R.drawable.icon_changkong_on);
                            }

                            @Override
                            public void requestFailure(int code, String msg) {
                                a.setEnabled(true);
                                toast(msg);
                            }
                        });
                    } else {
                        Api.cancelManager(PublishActivity.this, temp1, new OnRequestDataListener() {
                            @Override
                            public void requestSuccess(int code, JSONObject data) {
                                a.setEnabled(true);
                                a.setText("设为场控");
                                a.setTextColor(Color.BLACK);
                                //dialogChangkongImageOff.setImageResource(R.drawable.icon_changkong);
                            }

                            @Override
                            public void requestFailure(int code, String msg) {
                                a.setEnabled(true);
                                toast(msg);
                            }
                        });
                    }
                    break;
                case R.id.dialog_user_changkong:

                    break;
                case R.id.manager_changkong_list:
                    JSONObject params1 = new JSONObject();
                    params1.put("token", token);
                    Api.getManagerList(PublishActivity.this, params1, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            ArrayList<ManagerModel> managerList = new ArrayList<ManagerModel>();
                            JSONArray list1 = data.getJSONArray("data");
                            for (int i = 0; i < list1.size(); i++) {
                                ManagerModel aa = new ManagerModel();
                                JSONObject tempUser = list1.getJSONObject(i);
                                aa.setUserId(tempUser.getString("id"));
                                aa.setUserName(tempUser.getString("user_nicename"));
                                aa.setAvatar(tempUser.getString("avatar"));
                                managerList.add(aa);
                            }
                            LinearLayout linearLayoutMain = (LinearLayout) getLayoutInflater().inflate(R.layout.list_item_dialog_manager, null);
                            ListView list = (ListView) linearLayoutMain.findViewById(R.id.manager_list);
                            ManagerAdapter managerAdapter = new ManagerAdapter(PublishActivity.this, managerList);
                            list.setAdapter(managerAdapter);
                            AlertDialog dialog1 = new AlertDialog.Builder(PublishActivity.this).setView(linearLayoutMain)//在这里把写好的这个listview的布局加载dialog中
                                    .create();
                            dialog1.setCanceledOnTouchOutside(true);//使除了dialog以外的地方不能被点击
                            dialog1.show();
                        }

                        @Override
                        public void requestFailure(int code, String msg) {
                            toast(msg);
                        }
                    });

                    break;
                case R.id.tv_cancel:
                    if (mPopupShareWindow != null && mPopupShareWindow.isShowing()) {
                        mPopupShareWindow.dismiss();
                    }
                    if (mPopupShareWindow1 != null && mPopupShareWindow1.isShowing()) {
                        mPopupShareWindow1.dismiss();
                    }
                    break;
                case R.id.image_live_share_qzone:
                    mThirdShare.setTitle(info.getString("content"));
                    mThirdShare.setText(info.getString("content"));
                    mThirdShare.setTitleUrl(info.getString("shareUrl"));
                    mThirdShare.setImageType(Type.IMAGE_NETWORK);
                    mThirdShare.setImageUrl(info.getString("pic"));
                    mThirdShare.share2QZone();
                    break;
                case R.id.image_live_share_qq:
                    //toast("qq");
                    mThirdShare.setTitle(info.getString("content"));
                    mThirdShare.setText(info.getString("content"));
                    mThirdShare.setTitleUrl(info.getString("shareUrl"));
                    mThirdShare.setImageType(Type.IMAGE_NETWORK);
                    mThirdShare.setImageUrl(info.getString("pic"));
                    mThirdShare.share2QQ();
                    break;
                case R.id.image_live_share_sina:
                    //toast("weibo");
                    mThirdShare.setText(info.getString("content"));
                    mThirdShare.setImageUrl(info.getString("pic"));
                    mThirdShare.share2SinaWeibo(false);
                    break;
                case R.id.image_live_share_wechat:
                    //toast("wechat");
                    mThirdShare.setTitle(info.getString("content"));
                    mThirdShare.setText(info.getString("content"));
                    mThirdShare.setShareType(Type.SHARE_WEBPAGE);
                    mThirdShare.setImageType(Type.IMAGE_NETWORK);
                    mThirdShare.setImageUrl(info.getString("pic"));
                    mThirdShare.setUrl(info.getString("shareUrl"));
                    mThirdShare.share2Wechat();
                    break;
                case R.id.image_live_share_wechat_moment:
                    //toast("wechat moment");
                    mThirdShare.setTitle(info.getString("content"));
                    mThirdShare.setText(info.getString("content"));
                    mThirdShare.setShareType(Type.SHARE_WEBPAGE);
                    mThirdShare.setImageType(Type.IMAGE_NETWORK);
                    mThirdShare.setImageUrl(info.getString("pic"));
                    mThirdShare.setUrl(info.getString("shareUrl"));
                    mThirdShare.share2WechatMoments();
                    break;
                case R.id.image_live_public:
                    pass = "";
                    payMoney = "";
                    privacy = "0";
                    minute_charge="";
                    mAddLivingType.setText(getText(R.string.live_type_pub));
                    mPopupShareWindow.dismiss();
                    break;
                case R.id.image_live_pass:
                    DialogEnsureUtiles.showInfo(PublishActivity.this, new OnCustomClickListener() {
                        @Override
                        public void onClick(String value) {
                            pass = value;
                            payMoney = "";
                            privacy = "0";
                            minute_charge="";
                            mAddLivingType.setText(getText(R.string.live_type_pass));
                            mPopupShareWindow.dismiss();
                        }
                    }, pass, "请输入密码");
                    break;
                case R.id.image_live_pay:
                    DialogEnsureUtiles.showInfo(PublishActivity.this, new OnCustomClickListener() {
                        @Override
                        public void onClick(String value) {
                            if (Utile.isNumeric(value)) {
                                payMoney = value;
                                pass = "";
                                privacy = "0";
                                minute_charge="";
                                mAddLivingType.setText(getText(R.string.live_type_pay));
                                mPopupShareWindow.dismiss();
                            } else {
                                toast("请输入整数");
                            }

                        }
                    }, payMoney, "请输入整数(金币)");
                    break;
                case R.id.image_live_private:
                    privacy = "1";
                    pass = "";
                    payMoney = "";
                    minute_charge="";
                    mAddLivingType.setText(getText(R.string.live_type_private));
                    mPopupShareWindow.dismiss();
                    break;
                case R.id.image_live_minute:
                    DialogEnsureUtiles.showInfo(PublishActivity.this, new OnCustomClickListener() {
                        @Override
                        public void onClick(String value) {
                            if (Utile.isNumeric(value)) {
                                minute_charge = value;
                                payMoney = "";
                                pass = "";
                                privacy = "0";
                                mAddLivingType.setText(getText(R.string.live_type_minute));
                                mPopupShareWindow.dismiss();
                            } else {
                                toast("请输入整数");
                            }

                        }
                    }, minute_charge, "请输入整数(金币)");
                    break;
                case R.id.image_live_lianmai:
                    if ( mIsRegisted) {
                        if(mIsConnected){
                            toast("只能与一人连麦");
                            break;
                        }
                        //mStreamer.getRtcClient().startCall(otherUserId);
                        //mLianmaiStop.setText("邀请连麦中...");
                        mLianmaiStop.setVisibility(View.VISIBLE);
                        DanmuModel model = new DanmuModel();
                        model.setType("100");
                        model.setContent(otherUserId);
                        sendMessage(model);
                    }else{
                        toast("连麦注册失败");
                    }
//                    if ( mIsRegisted) {
//                        mStreamer.getRtcClient().startCall(otherUserId);
//                        mLianmaiStop.setText("连麦中...");
//                        mLianmaiStop.setVisibility(View.VISIBLE);
//                    }else{
//                        toast("连麦注册失败");
//                    }
                    break;
                case R.id.image_live_jinyan:
                    JSONObject temp = new JSONObject();
                    temp.put("token", token);
                    temp.put("room_id", liveInfo.getString("room_id"));
                    temp.put("id", otherUserId);
                    Api.addJinyan(PublishActivity.this, temp, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            toast(data.getString("descrp"));
                        }

                        @Override
                        public void requestFailure(int code, String msg) {
                            toast(msg);
                        }
                    });
                    break;
                case R.id.image_live_changkong:
                    JSONObject temp3 = new JSONObject();
                    temp3.put("token", token);
                    temp3.put("id", otherUserId);
                    Api.setManager(PublishActivity.this, temp3, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            toast(data.getString("descrp"));
                            DanmuModel model = new DanmuModel();
                            model.setType("21");
                            model.setContent(otherUserId);
                            sendMessage(model);
                        }

                        @Override
                        public void requestFailure(int code, String msg) {
                            toast(msg);
                        }
                    });
                    break;
                case R.id.caozuo_cancel:
                    mPopupCaozuoWindow.dismiss();
                    break;
            }

        }
    }

    private RTCClient.RTCEventChangedListener mRTCEventListener = new RTCClient.RTCEventChangedListener() {
        @Override
        public void onEventChanged(int event, Object arg1) {
            switch (event) {
                case RTCClient.RTC_EVENT_REGISTED:
                    doRegisteredSuccess();
                    break;
                case RTCClient.RTC_EVENT_STARTED:
                    doStartCallSuccess();
                    break;
                case RTCClient.RTC_EVENT_CALL_COMMING:
                    doReceiveRemoteCall(String.valueOf(arg1));
                    break;
                case RTCClient.RTC_EVENT_STOPPED:
                    Log.d(TAG, "stop result:" + arg1);
                    doStopCallResult();
                    break;
                case RTCClient.RTC_EVENT_UNREGISTED:
                    Log.d(TAG, "unregister result:" + arg1);
                    doUnRegisteredResult();
                    break;
                default:
                    break;
            }

        }
    };

    private RTCClient.RTCErrorListener mRTCErrorListener = new RTCClient.RTCErrorListener() {
        @Override
        public void onError(int errorType, int arg1) {
            switch (errorType) {
                case RTCClient.RTC_ERROR_AUTH_FAILED:
                    doAuthFailed();
                    break;
                case RTCClient.RTC_ERROR_REGISTED_FAILED:
                    doRegisteredFailed(arg1);
                    break;
                case RTCClient.RTC_ERROR_SERVER_ERROR:
                case RTCClient.RTC_ERROR_CONNECT_FAIL:
                    doRTCCallBreak();
                    break;
                case RTCClient.RTC_ERROR_STARTED_FAILED:
                    doStartCallFailed(arg1);
                    break;
                default:
                    break;
            }
        }
    };

    private void onRTCRegisterClick() {
        if (mRTCAuthResponse == null) {
            mRTCAuthResponse = new AuthHttpTask.KSYOnHttpResponse() {
                @Override
                public void onHttpResponse(int responseCode, final String response) {
                    if (responseCode == 200) {
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                doRegister(response);
                            }
                        });
                    } else {
                    }
                }
            };
        }
        if (!mIsRegisted) {
            mRTCAuthTask = new AuthHttpTask(mRTCAuthResponse);
            mRTCAuthTask.execute(RTC_AUTH_SERVER + "?uid=" + channel_creater);
        } else {
            doUnRegister();
        }

    }
    private void doRegisteredSuccess() {
        mIsRegisted = true;
    }

    private void doStartCallSuccess() {
        mIsConnected = true;
        //can stop call
        //mStreamer.startRTC();

        mStreamer.setRTCMainScreen(RTCConstants.RTC_MAIN_SCREEN_CAMERA);
        mLianmaiStop.setVisibility(View.VISIBLE);
        //mLianmaiStop.setText("停止连麦");
    }

    private void doStopCallResult() {
        mIsConnected = false;
        //can start call again
        //to stop RTC video/audio
        //mStreamer.stopRTC();
        mStreamer.setRTCMainScreen(RTCConstants.RTC_MAIN_SCREEN_CAMERA);
        mLianmaiStop.setVisibility(View.GONE);
    }

    private void doStartCallFailed(int status) {
        mIsConnected = false;
        //if remote receive visible need hide

//        Toast.makeText(this, "call failed: " + status, Toast
//                .LENGTH_SHORT).show();
        toast("连麦失败");
        mLianmaiStop.setVisibility(View.GONE);

    }

    private void doRTCCallBreak() {
        mIsConnected = false;
        //Toast.makeText(this, "call break", Toast
         //       .LENGTH_SHORT).show();
        //to stop RTC video/audio
        //mStreamer.stopRTC();
        mStreamer.setRTCMainScreen(RTCConstants.RTC_MAIN_SCREEN_CAMERA);
    }

    private void doReceiveRemoteCall(final String remoteUri) {
        //当前版本支持1对1的call
        if (mIsConnected) {
            mStreamer.getRtcClient().rejectCall();
        } else {
            mStreamer.getRtcClient().answerCall();
        }
    }
    private void doAuthFailed() {
        //register failed

        //can register again
        mIsRegisted = false;
    }

    private void doRegisteredFailed(int failed) {
        //register failed

        //can register again
        mIsRegisted = false;
        if (mIsConnected) {
            //mStreamer.stopRTC();
            mIsConnected = false;
        }
    }


    private void doUnRegisteredResult() {
        mIsRegisted = false;
        if (mIsConnected) {
            //mStreamer.stopRTC();
            mIsConnected = false;
        }
        //can register again

        mStreamer.setRTCMainScreen(RTCConstants.RTC_MAIN_SCREEN_CAMERA);
    }
    private void doRegister(String authString) {
        //must set before register
        mStreamer.setRTCSubScreenRect(0.65f, 0.7f, 0.35f, 0.3f, RTCConstants.SCALING_MODE_CENTER_CROP);
        mStreamer.getRtcClient().setRTCAuthInfo(RTC_AUTH_URI, authString,channel_creater);
        mStreamer.getRtcClient().setRTCUniqueName(RTC_UINIQUE_NAME);
        //has default value
        mStreamer.getRtcClient().openChattingRoom(false);
        mStreamer.getRtcClient().setRTCResolutionScale(0.5f);
        mStreamer.getRtcClient().setRTCFps(15);
        mStreamer.getRtcClient().setRTCVideoBitrate(256 * 1024);
        mStreamer.getRtcClient().registerRTC();
    }

    private void doUnRegister() {
        mStreamer.getRtcClient().unRegisterRTC();
    }


    @Override
    public void shareError(Platform platform) {
        toast("分享失败");
    }

    @Override
    public void shareCancel(Platform platform) {
        toast("分享取消");
    }

    @Override
    public void shareSuccess(Platform platform) {
        toast("分享成功");
        startLive(mInputLiveTitle.getText().toString());
    }

    /***********************************
     * for rtc sub move&switch
     ********************************/
    private float mSubTouchStartX;
    private float mSubTouchStartY;
    private float mLastRawX;
    private float mLastRawY;
    private float mLastX;
    private float mLastY;
    private float mSubMaxX = 0;   //小窗可移动的最大X轴距离
    private float mSubMaxY = 0;  //小窗可以移动的最大Y轴距离
    private boolean mIsSubMoved = false;  //小窗是否移动过了，如果移动过了，ACTION_UP时不触发大小窗内容切换
    private int SUB_TOUCH_MOVE_MARGIN = 30;  //触发移动的最小距离

    private CameraTouchHelper.OnTouchListener mRTCSubScreenTouchListener = new CameraTouchHelper.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            //获取相对屏幕的坐标，即以屏幕左上角为原点
            mLastRawX = event.getRawX();
            mLastRawY = event.getRawY();
            // 预览区域的大小
            int width = view.getWidth();
            int height = view.getHeight();
            //小窗的位置信息
            RectF subRect = mStreamer.getRTCSubScreenRect();
            int left = (int) (subRect.left * width);
            int right = (int) (subRect.right * width);
            int top = (int) (subRect.top * height);
            int bottom = (int) (subRect.bottom * height);
            int subWidth = right - left;
            int subHeight = bottom - top;


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //只有在小屏区域才触发位置改变
                    if (isSubScreenArea(event.getX(), event.getY(), left, right, top, bottom)) {
                        //获取相对sub区域的坐标，即以sub左上角为原点
                        mSubTouchStartX = event.getX() - left;
                        mSubTouchStartY = event.getY() - top;
                        mLastX = event.getX();
                        mLastY = event.getY();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    int moveX = (int) Math.abs(event.getX() - mLastX);
                    int moveY = (int) Math.abs(event.getY() - mLastY);
                    if (mSubTouchStartX > 0f && mSubTouchStartY > 0f && (
                            (moveX > SUB_TOUCH_MOVE_MARGIN) ||
                                    (moveY > SUB_TOUCH_MOVE_MARGIN))) {
                        //触发移动
                        mIsSubMoved = true;
                        updateSubPosition(width, height, subWidth, subHeight);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //未移动并且在小窗区域，则触发大小窗切换
                    if (!mIsSubMoved && isSubScreenArea(event.getX(), event.getY(), left, right,
                            top, bottom)) {
                        mStreamer.switchRTCMainScreen();
                    }

                    mIsSubMoved = false;
                    mSubTouchStartX = 0f;
                    mSubTouchStartY = 0f;
                    mLastX = 0f;
                    mLastY = 0f;
                    break;
            }

            return true;
        }
    };

    /**
     * 是否在小窗区域移动
     *
     * @param x      当前点击的相对小窗左上角的x坐标
     * @param y      当前点击的相对小窗左上角的y坐标
     * @param left   小窗左上角距离预览区域左上角的x轴距离
     * @param right  小窗右上角距离预览区域左上角的x轴距离
     * @param top    小窗左上角距离预览区域左上角的y轴距离
     * @param bottom 小窗右上角距离预览区域左上角的y轴距离
     * @return
     */
    private boolean isSubScreenArea(float x, float y, int left, int right, int top, int bottom) {
        if (!mIsConnected) {
            return false;
        }
        if (x > left && x < right &&
                y > top && y < bottom) {
            return true;
        }

        return false;
    }

    /**
     * 触发移动小窗
     *
     * @param screenWidth 预览区域width
     * @param sceenHeight 预览区域height
     * @param subWidth    小窗区域width
     * @param subHeight   小窗区域height
     */
    private void updateSubPosition(int screenWidth, int sceenHeight, int subWidth, int subHeight) {
        mSubMaxX = screenWidth - subWidth;
        mSubMaxY = sceenHeight - subHeight;

        //更新浮动窗口位置参数
        float newX = (mLastRawX - mSubTouchStartX);
        float newY = (mLastRawY - mSubTouchStartY);

        //不能移出预览区域最左边和最上边
        if (newX < 0) {
            newX = 0;
        }

        if (newY < 0) {
            newY = 0;
        }

        //不能移出预览区域最右边和最下边
        if (newX > mSubMaxX) {
            newX = mSubMaxX;
        }

        if (newY > mSubMaxY) {
            newY = mSubMaxY;
        }
        //小窗的width和height不发生变化
        RectF subRect = mStreamer.getRTCSubScreenRect();
        float width = subRect.width();
        float height = subRect.height();

        float left = newX / screenWidth;
        float top = newY / sceenHeight;

        mStreamer.setRTCSubScreenRect(left, top, width, height, RTCConstants.SCALING_MODE_CENTER_CROP);
    }

}
