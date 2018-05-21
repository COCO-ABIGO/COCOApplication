package proj.abigo.coco.cocoapplication;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import proj.abigo.coco.cocoapplication.MyFeed.MyFeedFragment;
import proj.abigo.coco.cocoapplication.MyPage.MyPageFragment;

public class MainActivity extends AppCompatActivity {

    private TabLayout main_tabLayout;
    private boolean mFlag = false;

    MyFeedFragment myFeedFragment = new MyFeedFragment();
    MyPageFragment myPageFragment = new MyPageFragment();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 피드를 기본화면으로 설정*/
        if(savedInstanceState == null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_fragment,myFeedFragment);
            fragmentTransaction.commit();
        }

        initView();
        setEvent();
    }

    private void setEvent() {

        main_tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            int pre = 0;

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeFragment(tab.getPosition(), pre);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                pre = tab.getPosition();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // tab 두 번 눌렀을 시 상단으로 자동 스크롤
                switch (tab.getPosition()){
                    case 0:
                        MyFeedFragment.moveScroll();
                        break;
                    case 1:
                        break;
                }
            }
        });
    }

    private void changeFragment(int position, int pre){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (position){
            case 0:
                fragmentTransaction.replace(R.id.content_fragment,myFeedFragment,"Feed");
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentTransaction.replace(R.id.content_fragment,myPageFragment,"MyPage");
                fragmentTransaction.commit();
                break;
        }
    }


/*    액티비티 종료 시 뒤로가기 두 번을 눌러야 함
    - 뒤로가기 한 번 클릭으로 종료되지 않기 위함 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int depth = getSupportFragmentManager().getBackStackEntryCount();
            if (keyCode == KeyEvent.KEYCODE_BACK && depth == 0) {
                /* 뒤로 버튼을 눌렀을때 해야할 행동 */
                if (!mFlag) {
                    Toast.makeText(MainActivity.this, "'뒤로' 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                    mFlag = true;
                    mKillHandler.sendEmptyMessageDelayed(0, 2000);
                    return false;
                } else {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /* 종료버튼이 한번 더 눌리지 않으면 mFlag 값 복원 */
    Handler mKillHandler = new Handler() {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 0)
            {
                mFlag = false;
            }
        }
    };

    private void initView() {
        main_tabLayout = (TabLayout)findViewById(R.id.main_tabLayout);
    }
}
