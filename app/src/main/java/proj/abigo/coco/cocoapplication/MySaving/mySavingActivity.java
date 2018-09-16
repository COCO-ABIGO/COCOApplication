package proj.abigo.coco.cocoapplication.MySaving;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import proj.abigo.coco.cocoapplication.GlobalApplication;
import proj.abigo.coco.cocoapplication.Network.NetworkService;
import proj.abigo.coco.cocoapplication.R;
import proj.abigo.coco.cocoapplication.Users;
import proj.abigo.coco.cocoapplication.coco;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class mySavingActivity extends AppCompatActivity {

    private Button btnBack;
    private TextView txt_user_name, txtName, txtPurpose;
    private ImageView img_user;
    private ListView list_mysaving;

    private mySavingAdapter mySavingAdapter;
    private NetworkService networkService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_saving);

        initVIew();
        setView();
        setEvent();
    }

    private void setView() {

        GlobalApplication globalApplication = GlobalApplication.getGlobalApplicationContext();
        globalApplication.buildNetworkService(coco.coco_url);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();

        mySavingAdapter = new mySavingAdapter();
        list_mysaving.setAdapter(mySavingAdapter);



        Call<List<mySaving>> getCall = networkService.get_savings();
        getCall.enqueue(new Callback<List<mySaving>>() {
            @Override
            public void onResponse(Call<List<mySaving>> call, Response<List<mySaving>> response) {
                if(response.isSuccessful()){
                    List<mySaving> mySavings = response.body();

                    for(mySaving savings: mySavings){
                        String money = savings.getSavingmoney();
                        String date = savings.getSavingdate();

                        mySavingAdapter.addItem(money, date);
                    }

                    mySavingAdapter.notifyDataSetChanged();

                }else{
                    int StatusCode = response.code();
                    Log.i("Status Code :", String.valueOf(StatusCode));
                }
            }

            @Override
            public void onFailure(Call<List<mySaving>> call, Throwable t) {
                Log.i ("Fail Messange : ",t.getMessage());

            }
        });
//
//        Glide
//                .with(this)
//                .load(IMAGE_URL + user_img_path)
//                .fitCenter()
//                .centerCrop()
//                .crossFade() // 이미지 로딩 시 페이드 효과
//                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
//                .override(200, 200)
//                .into(img_user);

    }

    private void setEvent() {

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySavingActivity.this.finish();
            }
        });
    }

    private void initVIew() {
        btnBack = (Button)findViewById(R.id.btnBack);
        txt_user_name = (TextView)findViewById(R.id.txt_user_name);
        txtName = (TextView)findViewById(R.id.txtName);
        txtPurpose = (TextView)findViewById(R.id.txtPurpose);
        img_user = (ImageView)findViewById(R.id.img_user);
        list_mysaving = (ListView)findViewById(R.id.list_mysaving);
    }
}
