package proj.abigo.coco.cocoapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.auth.Session;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import proj.abigo.coco.cocoapplication.Bluetooth.BluetoothService;
import proj.abigo.coco.cocoapplication.MyFeed.MyFeedFragment;

import static com.kakao.usermgmt.StringSet.nickname;

public class BluetoothActivity extends AppCompatActivity {

    private ImageView img_user;
    private TextView txtName;
    private Button btnBtConnect;

    private String user_id, user_name, user_email, user_img_path;

    private static final boolean D = true;

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    public static final int MESSAGE_STATE_CHANGE = 3; // 블루투스 연결 상태 check

    BluetoothService btService = null;
    private static final String TAG = "Blutooth Connect";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        initView();
        setView();
        setEvent();
    }

    private void setView() {

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        user_name = intent.getStringExtra("user_name");
        user_email = intent.getStringExtra("user_email");
        user_img_path = intent.getStringExtra("user_img_path");

        txtName.setText(user_name);

        Glide
                .with(this)
                .load(user_img_path)
                .fitCenter()
                .centerCrop()
                .crossFade()
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .override(200,200)
                .into(img_user);


        if(btService == null){
            btService = new BluetoothService(this, handler);
        }


    }

    private void initView() {
        img_user = (ImageView)findViewById(R.id.img_user);
        txtName = (TextView) findViewById(R.id.txtName);
        btnBtConnect = (Button)findViewById(R.id.btnBtConnect);
    }

    private final Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case MESSAGE_STATE_CHANGE :
                    if(D) Log.d(TAG, "MESSAGE_STATE_CHANGE" + msg.arg1);

                    switch (msg.arg1){
                        case BluetoothService.STATE_CONNECTED :
                            Toast.makeText(getApplicationContext(), "블루투스 연결에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(BluetoothActivity.this, setGoalActivitiy.class);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("user_name", user_name);
                            intent.putExtra("user_email", user_email);
                            intent.putExtra("user_img_path", user_img_path);
                            startActivity(intent);
                            break;
                        case BluetoothService.STATE_FAIL:
                            Toast.makeText(getApplicationContext(), "블루투스 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_ENABLE_BT :
                if(resultCode == Activity.RESULT_OK){
                    // 기기 접속 요청
                    btService.scanDevice();
                }
                else{
                    Log.d(TAG, "Bluetooth is not enabled");
                }
                break;

            case REQUEST_CONNECT_DEVICE :
                if(resultCode == Activity.RESULT_OK){
                    // 검색된 기기에 접속
                    btService.getDeviceInfo(data);
                }
                break;
        }
    }

    private void setEvent() {

        btnBtConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btService.getDeviceState()){
                    // 블루투스 지원 가능 기기
                    btService.enableBluetooth();
                }else{
                    finish();
                }
            }
        });
    }
}
