package proj.abigo.coco.cocoapplication.Network;

import java.util.List;

import proj.abigo.coco.cocoapplication.MySaving.mySaving;
import proj.abigo.coco.cocoapplication.Users;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NetworkService {

    @GET("/users/")
    Call<List<Users>> get_users();

    @GET("/savings/")
    Call<List<mySaving>> get_savings();

}
