package com.example.diogo.discoverytrip.REST;


import com.example.diogo.discoverytrip.Model.Oferta;
import com.example.diogo.discoverytrip.REST.ServerResponses.Market;
import com.example.diogo.discoverytrip.REST.ServerResponses.ResponseAllMarkets;
import com.example.diogo.discoverytrip.REST.ServerResponses.ResponseMarketItems;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Renato on 11/12/2016.
 * Interface da API Rest usada para fazer as requisições ao servidor da aplicação.
 */
public interface ApiInterface {

    @GET("/api/markets")
    Call<ResponseAllMarkets> getAllMarkets();

    @GET("api/markets/{id}/items")
    Call<ResponseMarketItems> getMarketItems(@Path("id") String marketId);

    @GET("api/market/search")
    Call<Market> getMarketByLocation(@Query("lagitude") double latitude, @Query("longitude") double longitude, @Query("distance") int distance);
//
//    @POST("api/facebook/login")
//    Call<LoginResponse> loginFacebook(@Body AccessTokenJson accessToken);
//
//    @POST("api/login")
//    Call<LoginResponse> appLogin(@Body AppLoginJson appLoginJson);
//
//    @POST("api/login")
//    Call<LoginResponse> refreshToken(@Body RefreshTokenJson refreshJson);
//
//    @POST("api/login/pwd_reminder")
//    Call<ReminderResponse> passwordReminder(@Body ReminderJson reminderJson);
//
//    @DELETE("api/login")
//    Call<LogoutResponse> logout(@Header("Authorization") String authorization);
//
//    /**
//     * Cadastra um ponto turístico no servidor
//     * @param token access token
//     * @param parametersMap mapa que contém os nomes dos parâmetros da chamada como chave e seus respectivos valores. Os parâmetros possíveis nessa chamada são:
//     *                      Required:
//     *                      name = [string]
//     *                      description = [string]
//     *                      latitude = [string] <- In ISO 6709 format
//     *                      longitude = [string] <- In ISO 6709 format
//     *                      photos = [blob] <- At least one photo and a maximum of 10 photos
//     * @param fotos parte da request que contém as fotos do ponto turistico
//     * @return objeto contendo a resposta do servidor. Caso seja uma resposta de erro deve-se usar o errorBodyConverter da classe ApiClient.
//     */
//    @Multipart
//    @POST("api/attractions")
//    Call<AttractionResponse> cadastrarPontoTuristico(@Header("Authorization") String token, @PartMap Map<String, RequestBody> parametersMap, @Part MultipartBody.Part... fotos);
//
//    /**
//     * Cadastra um evento no servidor
//     * @param token access token
//     * @param parametersMap mapa que contém os nomes dos parâmetros da chamada como chave e seus respectivos valores. Os parâmetros possíveis nessa chamada são:
//     *                      Required:
//     *                      name = [string]
//     *                      description = [string]
//     *                      endData = [string] <- In ISO Date format
//     *
//     *                      Optional:
//     *                      photo = [file]
//     *                      kind = [String] <- Public|Private
//     *                      price = [Number]
//     *                      keywords = [Array of Strings]
//     *                      startDate = [String] <- In ISO Date format
//     * @param foto
//     * @return objeto contendo a resposta do servidor. Caso seja uma resposta de erro deve-se usar o errorBodyConverter da classe ApiClient.
//     */
//    @Multipart
//    @POST("api/events")
//    Call<AddEventoResponse> cadastrarEvento(@Header("Authorization") String token, @PartMap Map<String, RequestBody> parametersMap, @Part MultipartBody.Part foto);
//
//    @Multipart
//    @POST("api/events")
//    Call<AddEventoResponse> cadastrarEvento(@Header("Authorization") String token, @PartMap Map<String, RequestBody> parametersMap);
//
//    @GET("api/search/points")
//    Call<SearchResponse> searchPontoTuristico(@Header("Authorization") String token, @Query("latitude") double latitude, @Query("longitude") double longitude, @Query("distance") int distance);
//
//    @GET("api/images/{id}/download")
//    Call<ResponseBody> downloadFoto(@Header("Authorization") String token, @Path("id") String photoId);
//
//    @GET("/api/points")
//    Call<SearchResponse> userPoints(@Header("Authorization") String accessToken);
//
//    @DELETE("/api/points/{id}")
//    Call<DeleteEventoResponse> deleteEvento(@Header("Authorization") String accessToken, @Path("id") String id);
//
//    @DELETE("/api/points/{id}")
//    Call<DeleteAttractionResponse> deleteAttraction(@Header("Authorization") String accessToken, @Path("id") String id);
//
//    @GET("/api/events/interestme")
//    Call<SearchResponse> eventsOfDay(@Header("Authorization") String accessToken);
//
//    @POST("/api/events/{id}/interestme")
//    Call<ResponseBody> interestedEvent(@Header("Authorization") String token, @Path("id") String eventId);
//
//    @GET("/api/search/name")
//    Call<SearchResponse> search(@Header("Authorization") String token, @Query("text") String text);

    @GET("/api/market/{id}/product")
    Call<List<Oferta>> ofertas(@Path("id") String markedId);
}