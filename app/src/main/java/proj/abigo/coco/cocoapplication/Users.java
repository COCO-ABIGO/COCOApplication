package proj.abigo.coco.cocoapplication;

/**
 * Created by DS on 2018-08-30.
 */

public class Users {

    int user_id;
    String user_name;
    String user_email;
    String user_img_path;
    String saving_purpose;
    int saving_goal;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_img_path() {
        return user_img_path;
    }

    public void setUser_img_path(String user_img_path) {
        this.user_img_path = user_img_path;
    }

    public String getSaving_purpose() {
        return saving_purpose;
    }

    public void setSaving_purpose(String saving_purpose) {
        this.saving_purpose = saving_purpose;
    }

    public int getSaving_goal() {
        return saving_goal;
    }

    public void setSaving_goal(int saving_goal) {
        this.saving_goal = saving_goal;
    }
}
