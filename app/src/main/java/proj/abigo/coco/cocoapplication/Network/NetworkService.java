package proj.abigo.coco.cocoapplication.Network;

import com.kakao.usermgmt.response.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import proj.abigo.coco.cocoapplication.MySaving.mySaving;
import proj.abigo.coco.cocoapplication.Users;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {

    @POST("/userInfo/{user_id}")
    Call<Users> post_users(@Body Users users);

    @GET("/userInfo/{user_id}")
    Call<ResponseBody> get_users(@Path("user_id") int user_id);

    @GET("/savings/")
    Call<List<mySaving>> get_savings();

}
