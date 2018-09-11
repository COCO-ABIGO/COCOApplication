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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import proj.abigo.coco.cocoapplication.Bluetooth.BluetoothService;
import proj.abigo.coco.cocoapplication.GlobalApplication;
import proj.abigo.coco.cocoapplication.Network.JSONParser;
import proj.abigo.coco.cocoapplication.Network.NetworkService;
import proj.abigo.coco.cocoapplication.R;
import proj.abigo.coco.cocoapplication.coco;

/**
 * Created by User on 2018-02-20.
 */

public class MyFeedFragment extends Fragment implements View.OnTouchListener{

    public static final int MESSAGE_READING = 1; // 블루투스 연결 상태 check

    private static SwipeRefreshLayout myFeed_swipe_Refresh;
    private static RecyclerView myFeed_list_recycler;
    private LinearLayoutManager mLinearLayoutManager;

    private MyFeedAdapter myFeedAdapter;

    private static int scroll_num = 1;
    private static boolean loadingMore = true;

    ArrayList<MyFeed> myFeedsList = null;

    BluetoothService btService = null;
    private NetworkService networkService;

    private JSONObject feed_json;

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

        try{
            feed_json = new GetFeed().execute().get();
            Log.d("feed_test", feed_json.toString());

        } catch (Exception e){
            e.printStackTrace();
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

        myFeedAdapter = new MyFeedAdapter(getContext(), myFeedsList);
        mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        myFeed_list_recycler.setAdapter(myFeedAdapter);
        myFeed_list_recycler.setLayoutManager(mLinearLayoutManager);

        refreshItem();
    }

    private void refreshItem() {

        myFeedsList.clear();
        loadingMore = true;

        scroll_num = 1;
        try {
            feed_json = new GetFeed().execute().get();
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

        myFeed_list_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private static final int HIDE_THRESHOLD = 20;
            private int scrolledDistance = 0;
            private boolean controlsVisible = true;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                /* 마지막 아이템의 위치 계산해서 계속 paging */
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                Log.i("scorll number", "dy : " + dy + ", firstVisible : " + firstVisibleItem + ", visibleItem : " + visibleItemCount +
                        "totalCount : " + totalItemCount);

                int offSetTop = recyclerView.getChildAt(0).getTop();

                //show views if first item is first visible position and views are hidden
                if (firstVisibleItem == 0) {
                    if (!controlsVisible) {
                       // showViews();
                        controlsVisible = true;
                    }
                } else {
                    if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                        //hideViews();
                        controlsVisible = false;
                        scrolledDistance = 0;
                    } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                      //  showViews();
                        controlsVisible = true;
                        scrolledDistance = 0;
                    }

                    if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                        scrolledDistance += dy;
                    }
                }

                if (dy > 0) {
                    if (loadingMore) {
                        if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
                            /* Paging */
                            try {
                                //Toast.makeText(getContext(), "last", Toast.LENGTH_SHORT).show();
                                scroll_num++;
                                feed_json = new GetFeed().execute().get();

                                String response = feed_json.getString("MyAwardList");
                                Log.d("loadList", Integer.toString(scroll_num) + ": initial : " + response);
                                loadList(response);
                            } catch (Exception e) {
                                Log.e("myAward", "error : myAward, onScroll, 결과 값 받아오는 에러");
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    private void loadList(String result) {
        /* 통신 후 받은 결과값을 객체로 변환 후 list에 뿌려주는 함수 */
        try {
            Gson gson = new Gson();
            Log.d("loadList", "func : " + result);
            JSONArray feedListObj = new JSONArray(result);

            if (feedListObj.length() == 0) {
                scroll_num --;
                Log.d("loadList", "func : 비었다");

                loadingMore = false;
            }

            else {

                for(int i = 0; i < feedListObj.length(); i++) {
                    String feedInfo = feedListObj.getJSONObject(i).toString();
                    MyFeed myFeed = gson.fromJson(feedInfo, MyFeed.class);
                    myFeedsList.add(myFeed);

                }
                myFeedAdapter.notifyDataSetChanged();
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private class GetFeed extends AsyncTask<String, String, JSONObject>{

        JSONParser jsonParser = new JSONParser();

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";

        private static final String Feed_URL = "ec2-13-124-250-250.ap-northeast-2.compute.amazonaws.com/savings/";

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("저금 정보를 가져오는 중 입니다.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("scroll_num", args[0]);
                Log.d("myAward", args[0]);

                JSONObject result = jsonParser.makeHttpRequest(
                        Feed_URL, "GET", params);


                if (result != null) {
                    Log.d("feed_list", "result : " + result);
                    return result;
                } else {
                    Log.d("feed_list", "result : null, doInBackground");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject jObj) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            super.onPostExecute(jObj);
        }

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
        myFeed_list_recycler.smoothScrollToPosition(0);
    }

    private void initView(View view) {
        myFeed_swipe_Refresh = (SwipeRefreshLayout)view.findViewById(R.id.myFeed_swipe_Refresh);
        myFeed_list_recycler = (RecyclerView)view.findViewById(R.id.myFeed_list_recycler);
    }
}
