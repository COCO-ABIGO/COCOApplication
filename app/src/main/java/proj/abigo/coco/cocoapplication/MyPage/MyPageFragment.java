package proj.abigo.coco.cocoapplication.MyPage;

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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import proj.abigo.coco.cocoapplication.GlobalApplication;
import proj.abigo.coco.cocoapplication.Network.NetworkService;
import proj.abigo.coco.cocoapplication.R;
import proj.abigo.coco.cocoapplication.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyPageFragment extends Fragment implements View.OnTouchListener {
    ProgressBar progressBar;
    View view;

    Button btn1;
    TextView txtUser;

    int progressStep =5;

    int globalVar =0;
    int accum =0;

    long startingMills = System.currentTimeMillis();
    boolean isRunning = false;

    Handler myHandler = new Handler();

    private NetworkService networkService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalApplication globalApplication = GlobalApplication.getGlobalApplicationContext();
        globalApplication.buildNetworkService("a35ebc9d.ngrok.io");
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();

    }

    private void setEvent() {

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<Users>> getCall = networkService.get_users();
                getCall.enqueue(new Callback<List<Users>>() {
                    @Override
                    public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                        if(response.isSuccessful()){
                            List<Users> userList = response.body();

                            String user_txt = "";
                            for(Users users: userList){
                                user_txt += users.getUsername() +
                                        users.getUrl() +
                                        users.getUseremail() +
                                        "\n";
                            }

                            txtUser.setText(user_txt);

                        }else{
                            int StatusCode = response.code();
                            Log.i("Status Code :", String.valueOf(StatusCode));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Users>> call, Throwable t) {
                        Log.i ("Fail Messange : ",t.getMessage());

                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypage, null);
        progressBar = (ProgressBar)view.findViewById(R.id.MyPagePB);

        btn1 = (Button)view.findViewById(R.id.btn1);
        txtUser = (TextView)view.findViewById(R.id.txtUser);

        Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);
        an.setFillAfter(true);

        progressBar.startAnimation(an);


        setEvent();

        return view;
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
