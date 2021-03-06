package proj.abigo.coco.cocoapplication;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import proj.abigo.coco.cocoapplication.MyFeed.MyFeedFragment;
import proj.abigo.coco.cocoapplication.Network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setGoalActivitiy extends AppCompatActivity {

    private TextView txtName;
    private Spinner spinner_goal;
    private EditText editMoney;
    private Button btnStart;

    private Integer user_id, saving_money;
    private String user_name, user_email, user_img_path, saving_purpose;

    private final String[] purposes = {"저축", "기부", "용돈", "기타"};

    NetworkService networkService;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal_activitiy);
        
        initView();
        setView();
        setEvent();
        
    }

    private void setView() {

        Intent intent = getIntent();
        user_id = intent.getIntExtra("user_id", 0);
        user_name = intent.getStringExtra("user_name");
        user_email = intent.getStringExtra("user_email");
        user_img_path = intent.getStringExtra("user_img_path");

        // test
//        user_id = "918201429";
//        user_name = "홍은비";
//        user_email = "uko02111@naver.com";
//        user_img_path = "http://k.kakaocdn.net/dn/cwyxs7/btqoVIbjkbW/Cot61ksaA5Rk1e2nu9Hg10/profile_640x640s.jpg";

        txtName.setText(user_name + " 님,");

        // spinner item 설정
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.item_spinner, purposes);
        spinner_goal.setAdapter(adapter);

    }

    private void setEvent() {

        spinner_goal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        saving_purpose = "저축";
                        break;
                    case 1:
                        saving_purpose = "기부";
                        break;
                    case 2:
                        saving_purpose = "기타";
                        break;
                }

                Log.d("purpose : ", saving_purpose);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            saving_money = Integer.valueOf(editMoney.getText().toString());

            Log.d("user_id", String.valueOf(user_id));
            Log.d("user_name", user_name);
            Log.d("user_email", user_email);

            Log.d("user_img_path", user_img_path);
            Log.d("saving_purpose", saving_purpose);
            Log.d("saving_goal", String.valueOf(saving_money));

            SharedPrefereneUtil sharedPrefereneUtil = new SharedPrefereneUtil(setGoalActivitiy.this);
            sharedPrefereneUtil.putSharedPreferences("user_id", user_id);
            sharedPrefereneUtil.putSharedPreferences("user_name", user_name);
            sharedPrefereneUtil.putSharedPreferences("saving_purpose", saving_purpose);
            sharedPrefereneUtil.putSharedPreferences("saving_goal", saving_money);

            Toast.makeText(getApplicationContext(), user_name + " 님 반갑습니다:)", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

            /* user_id, user_name, user_img_path, saving_purpose, saving_money REST POST 요청 */

//            Users users = new Users();
//            users.setUser_id(user_id);
//            users.setUser_name(user_name);
//            users.setUser_email(user_email);
//            users.setUser_img_path(user_img_path);
//            users.setSaving_purpose(saving_purpose);
//            users.setSaving_goal(saving_money);


//            Call<Users> usersCall = networkService.post_users(users) {
//                    usersCall.enqueue(new Callback<Users>() {
//                        @Override
//                        public void onResponse(Call<Users> call, Response<Users> response) {
//                            if (response.isSuccessful()) {
//
//                            /*sharedpreferene : user_id 저장 */
//                                SharedPrefereneUtil sharedPrefereneUtil = new SharedPrefereneUtil(setGoalActivitiy.this);
//                                sharedPrefereneUtil.putSharedPreferences("user_id", user_id);
//
//                                Toast.makeText(getApplicationContext(), user_name + " 님 반갑습니다:)", Toast.LENGTH_SHORT).show();
//
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(intent);
//                            } else {
//                                int StatusCode = response.code();
//                                try {
//                                    Log.i("StatusCode: ", response.errorBody().string());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<Users> call, Throwable t) {
//                            Toast.makeText(getApplicationContext(), "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }

            }
        });
    }



    private void initView() {
        txtName = (TextView)findViewById(R.id.txtName);
        spinner_goal = (Spinner)findViewById(R.id.spinner_goal);
        editMoney = (EditText)findViewById(R.id.editMoney);
        btnStart = (Button)findViewById(R.id.btnStart);
    }
}
