package Domain.Russian;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Kraken on 26.09.2017.
 */
public interface RussianApi {

    //Для бесплатной версии
    @GET("/russian/declension")
    Call<ResponseBody> getDeclension(@Query("s") String word);
    //Для случая с платной версией
    @GET("/russian/declension")
    Call<ResponseBody> getDeclension(@Query("token") String token, @Query("s") String word);
}
