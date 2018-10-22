package proj.abigo.coco.cocoapplication.MySaving;

import android.content.Context;
import android.support.v4.util.Pools;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import proj.abigo.coco.cocoapplication.MyFeed.MyFeed;
import proj.abigo.coco.cocoapplication.MyFeed.MyFeedAdapter;
import proj.abigo.coco.cocoapplication.R;

/**
 * Created by DS on 2018-09-10.
 */

public class mySavingAdapter extends BaseAdapter{


    private Context mContext;
    private ArrayList<mySaving> savingArrayList = new ArrayList<mySaving>();

    public mySavingAdapter() {}


    @Override
    public int getCount() {
        return savingArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return savingArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {

        final Context context = viewGroup.getContext();

        if(convertview == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertview = inflater.inflate(R.layout.item_saving_list, viewGroup, false);
        }

        TextView txt_saving_money = (TextView)convertview.findViewById(R.id.txt_saving_money);
        TextView txt_saving_date = (TextView)convertview.findViewById(R.id.txt_saving_date);
        TextView txt_saving_time = (TextView)convertview.findViewById(R.id.txt_saving_time);

        mySaving mySaving = savingArrayList.get(i);

        txt_saving_money.setText(mySaving.getSavingmoney() );
        txt_saving_date.setText(mySaving.getSavingdate());
        txt_saving_time.setText(mySaving.getSavingtime());

        return convertview;
    }

    public void addItem(String money, String date, String time){
        mySaving saving = new mySaving();

        saving.setSavingmoney(money);
        saving.setSavingdate(date);
        saving.setSavingtime(time);

        savingArrayList.add(saving);
    }
}
