package proj.abigo.coco.cocoapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class setGoalActivitiy extends AppCompatActivity {

    TextView txtName;
    Spinner spinner_goal;
    EditText editMoney;
    Button btnStart;

    private String user_id, user_name, user_img;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal_activitiy);
        
        initView();
        setView();
        setEvent();
        
    }

    private void setEvent() {

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*todo
                * 디비에 사용자 정보 저장
                * 어떻게 나눠서 저장할지 ?
                * */
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setView() {

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        user_name = intent.getStringExtra("user_name");
        user_img = intent.getStringExtra("user_img");

        txtName.setText(user_name + " 님,");

    }

    private void initView() {
        txtName = (TextView)findViewById(R.id.txtName);
        spinner_goal = (Spinner)findViewById(R.id.spinner_goal);
        editMoney = (EditText)findViewById(R.id.editMoney);
        btnStart = (Button)findViewById(R.id.btnStart);
    }
}
