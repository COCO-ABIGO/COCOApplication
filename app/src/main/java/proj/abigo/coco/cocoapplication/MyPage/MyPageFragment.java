package proj.abigo.coco.cocoapplication.MyPage;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import proj.abigo.coco.cocoapplication.GlobalApplication;
import proj.abigo.coco.cocoapplication.MyFeed.MyFeedFragment;
import proj.abigo.coco.cocoapplication.MySaving.mySaving;
import proj.abigo.coco.cocoapplication.SharedPrefereneUtil;
import proj.abigo.coco.cocoapplication.coco;
import proj.abigo.coco.cocoapplication.myFightActivity;
import proj.abigo.coco.cocoapplication.MySaving.mySavingActivity;
import proj.abigo.coco.cocoapplication.Network.NetworkService;
import proj.abigo.coco.cocoapplication.R;
import proj.abigo.coco.cocoapplication.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;


public class MyPageFragment extends Fragment implements View.OnTouchListener {

    private ProgressBar progressBar;
    private TextView txtPurpose, txtMoney, txtGoal, txtPercent, txtDate;
    private ListView list_mypage_menu;

    private NetworkService networkService;

    private MenuAdapter menuAdapter;

    private String saving_purpose;
    private int user_id, saving_goal;

    private int money_plus = 0;
    private int money = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalApplication globalApplication = GlobalApplication.getGlobalApplicationContext();
        globalApplication.buildNetworkService(coco.coco_url);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();

        SharedPrefereneUtil sharedPrefereneUtil = new SharedPrefereneUtil(getActivity().getApplicationContext());
        user_id = sharedPrefereneUtil.getSharedPreferences("user_id", 0);
        saving_purpose = sharedPrefereneUtil.getSharedPreferences("saving_purpose", "");
        saving_goal = sharedPrefereneUtil.getSharedPreferences("saving_goal", 0);

        Log.i("user", String.valueOf(user_id));

    }

    private void setEvent() {

        list_mypage_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ld) {

                Intent intent;

                switch(position){
                    case 0:
                        intent = new Intent(getActivity().getApplicationContext(),mySavingActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity().getApplicationContext(),myFightActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity().getApplicationContext(),myFightActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState == null){
            View view = inflater.inflate(R.layout.fragment_mypage, container, false);
            initView(view);
            return view;
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        menuAdapter = new MenuAdapter();
        list_mypage_menu.setAdapter(menuAdapter);

        menuAdapter.addItem(getActivity().getDrawable(R.drawable.menu), "내 저금 내역");
        menuAdapter.addItem(getActivity().getDrawable(R.drawable.chart), "대결 목록");
        menuAdapter.addItem(getActivity().getDrawable(R.drawable.friends), "친구 관리");
        menuAdapter.addItem(getActivity().getDrawable(R.drawable.setting), "설정");

        Call<List<mySaving>> getCall = networkService.get_savings();
        getCall.enqueue(new Callback<List<mySaving>>() {
            @Override
            public void onResponse(Call<List<mySaving>> call, Response<List<mySaving>> response) {
                if(response.isSuccessful()){
                    List<mySaving> mySavings = response.body();

                    for(mySaving savings: mySavings){
                        money = Integer.valueOf(savings.getSavingmoney());
                        money_plus += money;
                    }


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

        // 날짜 구하기
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Date date = new Date();
        String currentDate = sdf.format(date);
        txtDate.setText(currentDate + " 기준");

        txtGoal .setText("목표금액 " + String.valueOf(saving_goal) + " 원 중");
        txtPurpose.setText(saving_purpose + "   까지   ");
        int money =  saving_goal - money_plus;
        txtMoney.setText(String.valueOf(money) + "   원 남았어요 !");

        int percent = (money_plus / saving_goal) * 100;
        txtPercent.setText(String.valueOf(percent));
        progressBar.setProgress(percent);

        ObjectAnimator ani = ObjectAnimator.ofInt(progressBar, "progress", 0, percent);
        ani.setDuration(1500 * percent / 100);
        ani.start();

        setEvent();
    }

    private void initView(View v){
        progressBar = (ProgressBar)v.findViewById(R.id.MyPagePB);
        list_mypage_menu = (ListView)v.findViewById(R.id.list_mypage_menu);
        txtPurpose = (TextView)v.findViewById(R.id.txtPurpose);
        txtMoney = (TextView)v.findViewById(R.id.txtMoney);
        txtGoal =  (TextView)v.findViewById(R.id.txtGoal);
        txtPercent = (TextView)v.findViewById(R.id.txtPercent);
        txtDate = (TextView)v.findViewById(R.id.txtDate);
    }

//    public  void onStart(){
//        super.onStart();
//        accum =0;
//        progressBar.setMax(100);
//        progressBar.setProgress(0);
//
//        Thread myBackgroundThread = new Thread(backgroundTask, "backAlias1");
//        myBackgroundThread.start();
//    }

//    private Runnable foregroundRunnable =new Runnable() {
//        @Override
//        public void run() {
//            try{
//      progressBar.incrementProgressBy(progressStep);
//      accum += progressStep;
//
//      if(accum>=progressBar.getMax()){
//          progressBar.setVisibility(View.INVISIBLE);
//      }
//            }catch(Exception e){
//                Log.e("<<foregroundTask>>",e.getMessage());
//            }
//        }
//    };
//
//    private Runnable backgroundTask = new Runnable() {
//        @Override
//        public void run() {
//            try{
//               for(int n=0;n<20;n++){
//                   Thread.sleep(1000);
//                   globalVar++;
//                   myHandler.post(foregroundRunnable);
//               }
//            }catch(InterruptedException e){
//                Log.e("<<foregroundTask>>",e.getMessage());
//            }
//        }
//    };
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
