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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.auth.AuthType;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends AppCompatActivity {

    SessionCallback callback; //callback class
    private Button btn_kakao_login; //xml에서 login button 가져옴

    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null)
            setContentView(R.layout.activity_login);
        else {
            finish();
            return;
        }

        initView();//callback class 초기화
        setEvent();


        /**카카오톡 로그아웃 요청**/
        // 한번 로그인이 성공하면 세션 정보가 남아있어서 로그인창이 뜨지 않고 바로 onSuccess()메서드를 호출
        //  매번 로그아웃 요청을 수행하도록 코드
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후
                Toast.makeText(LoginActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setEvent() {
        btn_kakao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLoginKakao();
            }
        });
    }

    private void isLoginKakao() {

        //callback class 초기화
        callback = new LoginActivity.SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().open(AuthType.KAKAO_TALK, LoginActivity.this);
    }


    private void initView() {
        btn_kakao_login = (Button)findViewById(R.id.btn_kakao_login);
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
                    final String email = result.getEmail();
                    final String pImage = result.getProfileImagePath();//사용자 프로필 경로

                    Log.e("user_profile", result.toString());
                    Log.d("user_name", nickName);
                    Log.d("user_id", String.valueOf(userID));
                    Log.d("user_img", pImage);

                    Intent intent = new Intent(LoginActivity.this, BluetoothActivity.class);
                    intent.putExtra("user_id",String.valueOf(userID));
                    intent.putExtra("user_name",nickName);
                    intent.putExtra("user_email", email);
                    intent.putExtra("user_img_path",pImage);
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



