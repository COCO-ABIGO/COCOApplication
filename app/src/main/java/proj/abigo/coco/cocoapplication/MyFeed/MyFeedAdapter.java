package proj.abigo.coco.cocoapplication.MyFeed;

import android.content.Context;
import android.support.v4.util.Pools;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import proj.abigo.coco.cocoapplication.R;

/**
 * Created by User on 2018-02-20.
 */

public class MyFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MAX_POOL_SIZE = 10;

    private Pools.SimplePool<View> mMyViewPool;

    private Context mContext;
    private ArrayList<MyFeed> myFeeds_list;

    public MyFeedAdapter(Context context, ArrayList <MyFeed> myFeeds) {
        mContext = context;
        myFeeds_list = myFeeds;
        mMyViewPool = new Pools.SynchronizedPool< >(MAX_POOL_SIZE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_feed_list, parent, false);
        return new MyFeedItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final MyFeed feed = myFeeds_list.get(position-1);
        MyFeedItemViewHolder viewHolder = (MyFeedItemViewHolder) holder;

        /* todo / feed 데이터 받아와서 보여주는 작업업 */
       viewHolder.txt_user_name.setText(feed.getUser_name());
       viewHolder.txt_savings.setText(feed.getSavings());
    }

    @Override
    public int getItemCount() {
        return myFeeds_list == null ? 0 : myFeeds_list.size();
    }

    private class MyFeedItemViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView img_user;
        TextView txt_user_name;
        TextView txt_savings;
        TextView txt_save_date;

        public MyFeedItemViewHolder(View view) {
            super(view);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            img_user = (ImageView)view.findViewById(R.id.img_user);
            txt_user_name = (TextView)view.findViewById(R.id.txt_user_name);
            txt_savings = (TextView)view.findViewById(R.id.txt_savings);
            txt_save_date = (TextView)view.findViewById(R.id.txt_save_date);
        }
    }
}
