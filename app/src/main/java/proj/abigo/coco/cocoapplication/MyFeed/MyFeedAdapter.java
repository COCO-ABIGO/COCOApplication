package proj.abigo.coco.cocoapplication.MyFeed;

import android.content.Context;
import android.support.v4.util.Pools;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import proj.abigo.coco.cocoapplication.MySaving.mySaving;
import proj.abigo.coco.cocoapplication.R;

/**
 * Created by User on 2018-02-20.
 */
public class MyFeedAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<MyFeed> myFeeds = new ArrayList<MyFeed>();

    public MyFeedAdapter() {}

    @Override
    public int getCount() {
        return myFeeds.size();
    }

    @Override
    public Object getItem(int i) {
        return myFeeds.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_feed_list, viewGroup, false);
        }

        TextView txt_user_name = (TextView)view.findViewById(R.id.txt_user_name);
        ImageView img_user = (ImageView)view.findViewById(R.id.img_user);
        TextView txt_savingMoney = (TextView)view.findViewById(R.id.txt_savingMoney);
        TextView txt_save_date = (TextView)view.findViewById(R.id.txt_save_date);
        TextView txt_savingPurpose = (TextView)view.findViewById(R.id.txt_savingPurpose);

        MyFeed feeds = myFeeds.get(i);

        txt_user_name.setText(feeds.getUser_name());
        txt_savingMoney.setText(feeds.getSavingMoney() + "원");
        txt_save_date.setText(feeds.getSave_date());
        txt_savingPurpose.setText(feeds.getPurpose());

        Glide
                .with(context)
                .load(feeds.getUser_img_path())
                .fitCenter()
                .centerCrop()
                .crossFade()
                .override(200,200)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(img_user);

        return view;
    }


    public void addItem(String user_img_path, String user_name, String savingMoney, String savingdate, String purpose){
        MyFeed feed = new MyFeed();

        feed.setUser_img_path(user_img_path);
        feed.setUser_name(user_name);
        feed.setSavingMoney(savingMoney);
        feed.setSave_date(savingdate);
        feed.setPurpose(purpose);

        myFeeds.add(feed);
    }
}


