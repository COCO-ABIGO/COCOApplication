package proj.abigo.coco.cocoapplication.Network;

import com.kakao.usermgmt.response.model.User;

import java.util.List;

import proj.abigo.coco.cocoapplication.MySaving.mySaving;
import proj.abigo.coco.cocoapplication.Users;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NetworkService {

    @FormUrlEncoded
    @POST("/userInfo/")
    Call<Users> post_users(@Body Users users);

    @GET("/userInfo/{user_id}/")
    Call<Users> get_users(@Path("user_id") String user_id);

    @GET("/savings/")
    Call<List<mySaving>> get_savings();

}
