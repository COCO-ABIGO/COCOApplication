package proj.abigo.coco.cocoapplication.MyPage;

import android.graphics.drawable.Drawable;

/**
 * Created by DS on 2018-09-04.
 */

public class MenuItem {

    private Drawable menu_img;
    private String menu_name;

    public Drawable getMenu_img() {
        return menu_img;
    }

    public void setMenu_img(Drawable menu_img) {
        this.menu_img = menu_img;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }
}
