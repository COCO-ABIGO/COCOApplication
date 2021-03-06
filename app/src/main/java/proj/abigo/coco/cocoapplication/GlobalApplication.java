package proj.abigo.coco.cocoapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

import proj.abigo.coco.cocoapplication.Network.NetworkService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GlobalApplication extends Application {

    private static volatile GlobalApplication instance = null;
    private static volatile Activity currentActivity = null;

    private NetworkService networkService;
    private String baseURL;

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalApplication.instance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    // Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야함
    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }


    public static GlobalApplication getGlobalApplicationContext() {
        return instance;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }

    public void buildNetworkService(String ip, int port){
        synchronized (GlobalApplication.class){
            if(networkService == null){
                baseURL = String.format("http://%s:%d", ip, port);
                Log.i("URL : ", baseURL);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                networkService = retrofit.create(NetworkService.class);
            }
        }
    }

    public void buildNetworkService(String ip){
        synchronized (GlobalApplication.class){
            if (networkService == null){
                baseURL = String.format("http://%s/", ip);
                Log.i("URL : ", baseURL);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                networkService = retrofit.create(NetworkService.class);
            }
        }
    }

    private static class KakaoSDKAdapter extends KakaoAdapter {

        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[] {AuthType.KAKAO_ACCOUNT};
                }
                // 로그인시 인증받을 타입을 지정한다. 지정하지 않을 시 가능한 모든 옵션이 지정된다.

                //1.KAKAO_TALK :  kakaotalk으로 login을 하고 싶을때 지정.
                //2.KAKAO_STORY : kakaostory으로 login을 하고 싶을때 지정.
                //3.KAKAO_ACCOUNT :  웹뷰 Dialog를 통해 카카오 계정연결을 제공하고 싶을경우 지정.
                //4.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN : 카카오톡으로만 로그인을 유도하고 싶으면서 계정이 없을때 계정생성을 위한
                //버튼도 같이 제공을 하고 싶다면 지정.KAKAO_TALK과 중복 지정불가.
                //5.KAKAO_LOGIN_ALL : 모든 로그인방식을 사용하고 싶을때 지정.

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }
                // SDK 로그인시 사용되는 WebView에서 pause와 resume시에 Timer를 설정하여 CPU소모를 절약한다.
                // true 를 리턴할경우 webview로그인을 사용하는 화면서 모든 webview에 onPause와 onResume
                // 시에 Timer를 설정해 주어야 한다. 지정하지 않을 시 false로 설정된다.

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }
                //일반 사용자가 아닌 Kakao와 제휴된 앱에서 사용되는 값으로, 값을 채워주지 않을경우 ApprovalType.INDIVIDUAL 값을 사용하게 된다.

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
                // Kakao SDK 에서 사용되는 WebView에서 email 입력폼에서 data를 save할지여부를 결정한다. Default true.
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                public Activity getTopActivity() {
                    return GlobalApplication.getCurrentActivity();
                }
                // 현재 최상단에 위치하고 있는 Activity. topActivity가 아니거나 ApplicationContext를 넣는다면
                // SDK내에서의 Dialog Popup등이 동작하지 않을 수 있습니다.


                @Override
                public Context getApplicationContext() {
                    return GlobalApplication.getGlobalApplicationContext();
                }
            };
        }
    }

}