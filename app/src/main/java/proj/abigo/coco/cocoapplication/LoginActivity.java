package proj.abigo.coco.cocoapplication;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends AppCompatActivity {

    SessionCallback callback; //callback class
    LoginButton com_kakao_login; //xml에서 login button 가져옴

    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Add code to print out the key hash

        initView();//callback class 초기화
    }


    private void initView() {
        //callback class 초기화
        callback = new LoginActivity.SessionCallback();
        Session.getCurrentSession().addCallback(callback);
    }

    //재로그인요청
    private void redirectLoginActivity(){
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    //SessionCallback 클래스 구현
    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() { //세션 연결 성공시
            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult){
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if(result == ErrorCode.CLIENT_ERROR_CODE){
                        finish();
                    }else{
                        redirectLoginActivity();//재로그인
                    }
                }
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {

                }
                @Override
                public void onSuccess(UserProfile result) {
                    if(result != null){
                        result.saveUserToCache();
                    }
                    Logger.e("succeeded to update user profile", result, "\n");

                    final String nickName = result.getNickname();
                    final long userID = result.getId();
                    final String pImage = result.getProfileImagePath();//사용자 프로필 경로
                    Log.e("user_profile", result.toString());
                    Log.d("user_name", nickName);
                    Log.d("user_id", String.valueOf(userID));
                    Log.d("user_img", pImage);

                    Intent intent = new Intent(LoginActivity.this, BluetoothActivity.class);
                    intent.putExtra("user_name",nickName);
                    intent.putExtra("user_id",String.valueOf(userID));
                    intent.putExtra("user_img",pImage);
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e("Failed"+exception);
            }
        }

    }


}



