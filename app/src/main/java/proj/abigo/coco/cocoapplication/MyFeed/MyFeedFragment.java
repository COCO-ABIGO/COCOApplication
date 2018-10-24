package proj.abigo.coco.cocoapplication.MyFeed;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import proj.abigo.coco.cocoapplication.Bluetooth.BluetoothService;
import proj.abigo.coco.cocoapplication.GlobalApplication;
import proj.abigo.coco.cocoapplication.MySaving.mySaving;
import proj.abigo.coco.cocoapplication.Network.JSONParser;
import proj.abigo.coco.cocoapplication.Network.NetworkService;
import proj.abigo.coco.cocoapplication.R;
import proj.abigo.coco.cocoapplication.coco;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 2018-02-20.
 */

public class MyFeedFragment extends Fragment implements View.OnTouchListener{

    public static final int MESSAGE_READING = 1; // 블루투스 연결 상태 check

    private static SwipeRefreshLayout myFeed_swipe_Refresh;
    private ListView myFeed_list_recycler;
    private LinearLayoutManager mLinearLayoutManager;

    private MyFeedAdapter myFeedAdapter;

    private static int scroll_num = 1;
    private static boolean loadingMore = true;

    ArrayList<MyFeed> myFeedsList = null;

    BluetoothService btService = null;
    private NetworkService networkService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalApplication globalApplication = GlobalApplication.getGlobalApplicationContext();
        globalApplication.buildNetworkService(coco.coco_url);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();

        if(btService == null){
            btService = new BluetoothService(getActivity(), handler);
        }


        myFeedsList = new ArrayList<>();


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

        myFeedAdapter = new MyFeedAdapter();
        myFeed_list_recycler.setAdapter(myFeedAdapter);

        GetFeed();

     //   refreshItem();
    }

    private void GetFeed() {
        Call<List<MyFeed>> getCall = networkService.get_feed();
        getCall.enqueue(new Callback<List<MyFeed>>() {
            @Override
            public void onResponse(Call<List<MyFeed>> call, Response<List<MyFeed>> response) {
                if(response.isSuccessful()){
                    List<MyFeed> myFeeds = response.body();

                    for(MyFeed feed: myFeeds){
                        String img_path = feed.getUser_img_path();
                        String name = feed.getUser_name();
                        String purpose = feed.getPurpose();
                        String date = feed.getSave_date();
                        String money = feed.getSavingMoney();

                        myFeedAdapter.addItem(img_path, name, money, date, purpose);
                    }

                    myFeedAdapter.notifyDataSetChanged();

                }else{
                    int StatusCode = response.code();
                    Log.i("Status Code :", String.valueOf(StatusCode));
                }
            }

            @Override
            public void onFailure(Call<List<MyFeed>> call, Throwable t) {
                Log.i ("Fail Messange : ",t.getMessage());

            }
        });
    }

    private void refreshItem() {

        myFeedsList.clear();
        loadingMore = true;

        scroll_num = 1;
        try {
            GetFeed();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load complete
        onItemsLoadComplete();
        setEvent();

    }


    private void onItemsLoadComplete() {

        myFeedAdapter.notifyDataSetChanged();

        // Stop refresh animation
        myFeed_swipe_Refresh.setRefreshing(false);


    }


    private void setEvent() {

        myFeed_swipe_Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }
        });

//        myFeed_list_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            private static final int HIDE_THRESHOLD = 20;
//            private int scrolledDistance = 0;
//            private boolean controlsVisible = true;
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//                /* 마지막 아이템의 위치 계산해서 계속 paging */
//                super.onScrolled(recyclerView, dx, dy);
//
//                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
//                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
//                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
//                Log.i("scorll number", "dy : " + dy + ", firstVisible : " + firstVisibleItem + ", visibleItem : " + visibleItemCount +
//                        "totalCount : " + totalItemCount);
//
//                int offSetTop = recyclerView.getChildAt(0).getTop();
//
//                //show views if first item is first visible position and views are hidden
//                if (firstVisibleItem == 0) {
//                    if (!controlsVisible) {
//                       // showViews();
//                        controlsVisible = true;
//                    }
//                } else {
//                    if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
//                        //hideViews();
//                        controlsVisible = false;
//                        scrolledDistance = 0;
//                    } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
//                      //  showViews();
//                        controlsVisible = true;
//                        scrolledDistance = 0;
//                    }
//
//                    if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
//                        scrolledDistance += dy;
//                    }
//                }
//
//                if (dy > 0) {
//                    if (loadingMore) {
//                        if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
//                            /* Paging */
//                            try {
//                                //Toast.makeText(getContext(), "last", Toast.LENGTH_SHORT).show();
//                                scroll_num++;
//                                feed_json = new GetFeed().execute().get();
//
//                                String response = feed_json.getString("MyAwardList");
//                                Log.d("loadList", Integer.toString(scroll_num) + ": initial : " + response);
//                                loadList(response);
//                            } catch (Exception e) {
//                                Log.e("myAward", "error : myAward, onScroll, 결과 값 받아오는 에러");
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }
//        });
    }



    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MESSAGE_READING :
                    String readMessage = (String) msg.obj;
                    Log.d("readMessage", readMessage);

            }
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    /* 상단 스크롤 */
    public static void moveScroll(){
       // myFeed_list_recycler.smoothScrollToPosition(0);
    }

    private void initView(View view) {
        myFeed_swipe_Refresh = (SwipeRefreshLayout)view.findViewById(R.id.myFeed_swipe_Refresh);
        myFeed_list_recycler = (ListView) view.findViewById(R.id.myFeed_list_recycler);
    }
}
