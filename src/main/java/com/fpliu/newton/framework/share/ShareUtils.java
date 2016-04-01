package com.fpliu.newton.framework.share;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Toast;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.framework.util.BitmapUtil;
import com.fpliu.newton.framework.util.LauncherManager;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.open.GameAppOperation;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
/**
 * 
 * 分享
 * 
 * @author 792793182@qq.com 2015-02-04
 */
final class ShareUtils {

	private static final String TAG = ShareUtils.class.getSimpleName();

	/**
	 * 分享到QQ朋友
	 */
	static void shareToQQFriend(Activity activity, ShareContent shareContent) {
		DebugLog.d(TAG, "shareToQQFriend() shareContent = " + shareContent);
		
		Tencent tencent = MyApp.getApp().getTencent();
		
		Bundle bundle = new Bundle();
		
        bundle.putString("appName", MyApp.getApp().getResources().getString(R.string.app_name));
        
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_ SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString("title", shareContent.getTitle());
        
        //分享的消息摘要，最长50个字
        bundle.putString("summary", shareContent.getSummary());
        
        //icon的URL
        bundle.putString("imageUrl", shareContent.getIconUrl());
        
        bundle.putString("targetUrl", shareContent.getLinkUrl());
        
		tencent.shareToQQ(activity, bundle, null);
	}
	
	/**
	 * 分享到QQ空间
	 */
	static void shareToQZone(final Activity activity, final ShareContent shareContent) {
		DebugLog.d(TAG, "shareToQZone() shareContent = " + shareContent);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Bundle params = new Bundle();
		        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		        
		        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareContent.getTitle());
		        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareContent.getSummary());
		        
		        //注意：APP分享不支持传目标链接
		        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareContent.getLinkUrl());
                
                params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, shareContent.getImages());
                
                Tencent tencent = MyApp.getApp().getTencent();
				tencent.shareToQzone(activity, params, null);
			}
		}).start();
	}
	
	/**
	 * 分享到QQ群部落
	 */
	static void shareToTroopBar(Activity activity, ShareContent shareContent) {
		DebugLog.d(TAG, "shareToTroopBar() shareContent = " + shareContent);
		
		Bundle params = new Bundle();
		params.putString(GameAppOperation.TROOPBAR_ID, "");
		params.putString(GameAppOperation.QQFAV_DATALINE_APPNAME, MyApp.getApp().getResources().getString(R.string.app_name));
		params.putString(GameAppOperation.QQFAV_DATALINE_TITLE, shareContent.getTitle());
		params.putString(GameAppOperation.QQFAV_DATALINE_DESCRIPTION, shareContent.getSummary());
		params.putStringArrayList(GameAppOperation.QQFAV_DATALINE_FILEDATA, shareContent.getImages());
		
		Tencent tencent = MyApp.getApp().getTencent();
		tencent.shareToTroopBar(activity, params, new IUiListener() {
			
			@Override
			public void onError(UiError uiError) {
				
			}
			
			@Override
			public void onComplete(Object jsonObject) {
				
			}
			
			@Override
			public void onCancel() {
				
			}
		});
	}
	
	/**
	 * 分享到新浪微博
	 * @param activity      上下文
	 * @param shareContent  分享内容
	 */
	static void shareToXinLangWeibo(Activity activity, ShareContent shareContent) {
		DebugLog.d(TAG, "shareToXinLangWeibo() shareContent = " + shareContent);
		
		IWeiboShareAPI weiboShareAPI = WeiboShareSDK.createWeiboAPI(activity, MyApp.getApp().d());
		weiboShareAPI.registerApp();
		
		if (weiboShareAPI.isWeiboAppSupportAPI()) {
            int supportApi = weiboShareAPI.getWeiboAppSupportAPI();
            if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
            	WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
            	
            	TextObject textObject = new TextObject();
                textObject.text = shareContent.getSummary();
            	weiboMessage.textObject = textObject;
            	
            	ImageObject imageObject = new ImageObject();
                imageObject.setImageObject(((BitmapDrawable)activity.getResources().getDrawable(R.drawable.ic_logo)).getBitmap());
            	weiboMessage.imageObject = imageObject;
            	
            	WebpageObject mediaObject = new WebpageObject();
                mediaObject.identify = Utility.generateGUID();
                mediaObject.title = shareContent.getTitle();
                mediaObject.description = shareContent.getSummary();
                
                // 设置 Bitmap 类型的图片到视频对象里
                mediaObject.setThumbImage(((BitmapDrawable)activity.getResources().getDrawable(R.drawable.ic_logo)).getBitmap());
                mediaObject.actionUrl = shareContent.getLinkUrl();
                mediaObject.defaultText = shareContent.getSummary();
            	weiboMessage.mediaObject = mediaObject;
            	
            	// 2. 初始化从第三方到微博的消息请求
                SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
                // 用transaction唯一标识一个请求
                request.transaction = String.valueOf(System.currentTimeMillis());
                request.multiMessage = weiboMessage;
                
                // 3. 发送请求消息到微博，唤起微博分享界面
                weiboShareAPI.sendRequest(activity, request);
            } else {
                
            }
        } else {
            Toast.makeText(activity, R.string.third_party_weibo_hint, Toast.LENGTH_SHORT).show();
        }
	}
	
	static void shareToWXFriend(Activity activity, ShareContent shareContent) {
		DebugLog.d(TAG, "shareToWXFriend() shareContent = " + shareContent);
		shareToWX(activity, 0, shareContent);
	}
	
	static void shareToWXTimeLine(Activity activity, ShareContent shareContent) {
		DebugLog.d(TAG, "shareToWXTimeLine() shareContent = " + shareContent);
		shareToWX(activity, 1, shareContent);
	}
	
	/**
	 * 分享到微信
	 */
	static void shareToWX(Activity activity, int which, ShareContent shareContent) {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = shareContent.getLinkUrl();
		
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = shareContent.getTitle();
		msg.description = shareContent.getSummary();
		Bitmap thumb = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_logo);
		msg.thumbData = BitmapUtil.compress(thumb, CompressFormat.PNG);
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		
		if (1 == which) {
			msg.title = shareContent.getSummary();
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		} else if (0 == which) {
			req.scene = SendMessageToWX.Req.WXSceneSession;
		}
		
		IWXAPI api = MyApp.getApp().getWXAPI();
		api.sendReq(req);
	}
	
	private static String buildTransaction(String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	static void shareToEmail(Activity activity, ShareContent shareContent) {
		DebugLog.d(TAG, "shareToEmail() shareContent = " + shareContent);
		LauncherManager.sendEmail(activity, shareContent.getTitle(), shareContent.getSummary());
	}
	
	static void shareToMsg(Activity activity, ShareContent shareContent) {
		DebugLog.d(TAG, "shareToMsg() shareContent = " + shareContent);
		LauncherManager.sendMessage(activity, shareContent.getSummary());
	}
	
	static void shareMore(Activity activity, ShareContent shareContent) {
		DebugLog.d(TAG, "shareMore() shareContent = " + shareContent);
		LauncherManager.shareMore(activity, shareContent.getSummary(), shareContent.getImages().get(0));
	}
}
