package proj.abigo.coco.cocoapplication.MyFeed;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import proj.abigo.coco.cocoapplication.Bluetooth.BluetoothService;
import proj.abigo.coco.cocoapplication.R;

/**
 * Created by User on 2018-02-20.
 */

public class MyFeedFragment extends Fragment implements View.OnTouchListener{

    public static final int MESSAGE_READING = 1; // 블루투스 연결 상태 check

    private static SwipeRefreshLayout myFeed_swipe_Refresh;
    private static RecyclerView myFeed_list_recycler;
    private LinearLayoutManager mLinearLayoutManager;

    private MyFeedAdapter myFeedAdapter;

    ArrayList<MyFeed> myFeedsList = null;

    BluetoothService btService = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(btService == null){
            btService = new BluetoothService(getActivity(), handler);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState == null) {
            View view = inflater.inflate(R.layout.fragment_myfeed, container, false);
            initView(view);
            Log.i("Fragment_feed", "New Feed");
            return view;
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myFeedsList = new ArrayList<>();
        myFeedAdapter = new MyFeedAdapter(getContext(), myFeedsList);
        mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        myFeed_list_recycler.setAdapter(myFeedAdapter);
        myFeed_list_recycler.setLayoutManager(mLinearLayoutManager);
        
    }

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MESSAGE_READING :
                    String readMessage = (String) msg.obj;
                    Log.d("readMessage", readMessage);

                    // list item 추가
                    MyFeed feeditem = new MyFeed("1", "d", "g", readMessage, "2018");
                    myFeedsList.add(feeditem);

            }
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    /* 상단 스크롤 */
    public static void moveScroll(){
        myFeed_list_recycler.smoothScrollToPosition(0);
    }

    private void initView(View view) {
        myFeed_swipe_Refresh = (SwipeRefreshLayout)view.findViewById(R.id.myFeed_swipe_Refresh);
        myFeed_list_recycler = (RecyclerView)view.findViewById(R.id.myFeed_list_recycler);
    }
}
