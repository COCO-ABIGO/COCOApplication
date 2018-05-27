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
import android.widget.ProgressBar;

import proj.abigo.coco.cocoapplication.R;


public class MyPageFragment extends Fragment implements View.OnTouchListener {
    ProgressBar progressBar;
    View view;

    int progressStep =5;

    int globalVar =0;
    int accum =0;

    long startingMills = System.currentTimeMillis();
    boolean isRunning = false;

    Handler myHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypage, null);
        progressBar = (ProgressBar)view.findViewById(R.id.MyPagePB);
        Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);
        an.setFillAfter(true);

        progressBar.startAnimation(an);
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
