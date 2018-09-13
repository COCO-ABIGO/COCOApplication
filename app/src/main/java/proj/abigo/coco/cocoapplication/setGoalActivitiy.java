package proj.abigo.coco.cocoapplication;

import android.content.Intent;
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

public class setGoalActivitiy extends AppCompatActivity {

    private TextView txtName;
    private Spinner spinner_goal;
    private EditText editMoney;
    private Button btnStart;

    private String user_id, user_name, user_img, purpose;

    private final String[] purposes = {"저축", "기부", "용돈", "기타"};

    
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
        user_id = intent.getStringExtra("user_id");
        user_name = intent.getStringExtra("user_name");
        user_img = intent.getStringExtra("user_img");

        txtName.setText(user_name + " 님,");

        // spinner item 설정
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.item_spinner, purposes);
        spinner_goal.setAdapter(adapter);

    }

    private void setEvent() {

        spinner_goal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i){
                    case 0:
                        purpose = "저축";
                        break;
                    case 1:
                        purpose = "기부";
                        break;
                    case 2:
                        purpose = "용돈";
                        break;
                    case 3:
                        purpose = "기타";
                        break;
                }

                Log.d("purpose : ", purpose);
            }
        });

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



    private void initView() {
        txtName = (TextView)findViewById(R.id.txtName);
        spinner_goal = (Spinner)findViewById(R.id.spinner_goal);
        editMoney = (EditText)findViewById(R.id.editMoney);
        btnStart = (Button)findViewById(R.id.btnStart);
    }
}
