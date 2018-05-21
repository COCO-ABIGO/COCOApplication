package proj.abigo.coco.cocoapplication.MyFeed;

/**
 * Created by User on 2018-02-20.
 */

public class MyFeed {

    String user_id;
    String user_img_path;
    String user_name;
    String savings;
    String save_date;

    public MyFeed(String user_id, String user_img_path, String user_name, String savings, String save_date) {
        this.user_id = user_id;
        this.user_img_path = user_img_path;
        this.user_name = user_name;
        this.savings = savings;
        this.save_date = save_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_img_path() {
        return user_img_path;
    }

    public void setUser_img_path(String user_img_path) {
        this.user_img_path = user_img_path;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSavings() {
        return savings;
    }

    public void setSavings(String savings) {
        this.savings = savings;
    }

    public String getSave_date() {
        return save_date;
    }

    public void setSave_date(String save_date) {
        this.save_date = save_date;
    }
}
