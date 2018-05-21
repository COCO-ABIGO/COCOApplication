package proj.abigo.coco.cocoapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class FriendAddActivity extends AppCompatActivity {

    // Recommened Charset UTF-8
    private String encoding = "UTF-8";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);

        Button btnBTShare = (Button)findViewById(R.id.btnBTShare);

        btnBTShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            shareKakao();
            }
        });
    }

    public void shareKakao(){
        try{
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            kakaoBuilder.addText("kakao link test");
            kakaoBuilder.addAppButton("app excute or download");

            kakaoLink.sendMessage(kakaoBuilder,this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
