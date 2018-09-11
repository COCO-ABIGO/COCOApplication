package proj.abigo.coco.cocoapplication.MyPage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import proj.abigo.coco.cocoapplication.R;

/**
 * Created by DS on 2018-09-04.
 */

public class MenuAdapter extends BaseAdapter {

    private ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

    public MenuAdapter() {}

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItems.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_mypage_menu, parent, false);
        }

        ImageView imgMenu = (ImageView)convertView.findViewById(R.id.imgMenu);
        TextView txtMenu = (TextView)convertView.findViewById(R.id.txtMenu);

        MenuItem menuItem = menuItems.get(position);

        imgMenu.setImageDrawable(menuItem.getMenu_img());
        txtMenu.setText(menuItem.getMenu_name());

        return convertView;
    }

    public void addItem(Drawable img, String name){
        MenuItem item = new MenuItem();

        item.setMenu_img(img);
        item.setMenu_name(name);

        menuItems.add(item);
    }
}
