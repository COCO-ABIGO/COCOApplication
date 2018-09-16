package proj.abigo.coco.cocoapplication.MyPage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.List;

import proj.abigo.coco.cocoapplication.GlobalApplication;
import proj.abigo.coco.cocoapplication.MyFeed.MyFeedFragment;
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
    private TextView txtPurpose, txtMoney;
    private ListView list_mypage_menu;

    int progressStep =5;

    int globalVar =0;
    int accum =0;

    long startingMills = System.currentTimeMillis();
    boolean isRunning = false;

    Handler myHandler = new Handler();

    private NetworkService networkService;

    private MenuAdapter menuAdapter;

    private String user_id, saving_purpose, saving_goal;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalApplication globalApplication = GlobalApplication.getGlobalApplicationContext();
        globalApplication.buildNetworkService(coco.coco_url);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();

        SharedPrefereneUtil sharedPrefereneUtil = new SharedPrefereneUtil(getActivity().getApplicationContext());
        user_id = sharedPrefereneUtil.getSharedPreferences("user_id", " ");

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


        Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);
        an.setFillAfter(true);

        progressBar.startAnimation(an);

        Call<Users> usersCall = networkService.get_users(user_id);
        usersCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                saving_purpose = response.body().getSaving_purpose();
                saving_goal = response.body().getSaving_goal();

                Log.d("saving_purpose", saving_purpose);
                Log.d("saving_goal", saving_goal);
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {

            }
        });

        setEvent();
    }

    private void initView(View v){
        progressBar = (ProgressBar)v.findViewById(R.id.MyPagePB);
        list_mypage_menu = (ListView)v.findViewById(R.id.list_mypage_menu);
        txtPurpose = (TextView)v.findViewById(R.id.txtPurpose);
        txtMoney = (TextView)v.findViewById(R.id.txtMoney);
    }

    public  void onStart(){
        super.onStart();
        accum =0;
        progressBar.setMax(100);
        progressBar.setProgress(0);

        Thread myBackgroundThread = new Thread(backgroundTask, "backAlias1");
        myBackgroundThread.start();
    }

    private Runnable foregroundRunnable =new Runnable() {
        @Override
        public void run() {
            try{
      progressBar.incrementProgressBy(progressStep);
      accum += progressStep;

      if(accum>=progressBar.getMax()){
          progressBar.setVisibility(View.INVISIBLE);
      }
            }catch(Exception e){
                Log.e("<<foregroundTask>>",e.getMessage());
            }
        }
    };

    private Runnable backgroundTask = new Runnable() {
        @Override
        public void run() {
            try{
               for(int n=0;n<20;n++){
                   Thread.sleep(1000);
                   globalVar++;
                   myHandler.post(foregroundRunnable);
               }
            }catch(InterruptedException e){
                Log.e("<<foregroundTask>>",e.getMessage());
            }
        }
    };
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
