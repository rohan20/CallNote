package com.rohan.callnote.network;

import com.google.gson.GsonBuilder;
import com.rohan.callnote.utils.Constants;
import com.rohan.callnote.utils.SharedPrefsUtil;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rohan on 17-Jan-17.
 */

public class ApiClient {

    private static ApiService apiService;

    private static void setUpRetrofit() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthorizedOKHttpInterceptor())
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS).build();

        apiService = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(
                        GsonConverterFactory.create(new GsonBuilder()
                        .serializeNulls()
                        .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                        .create()))
                .client(client)
                .build()
                .create(ApiService.class);
    }

    public static ApiService getApiService() {
        if (apiService == null)
            setUpRetrofit();

        return apiService;
    }

    /***
     * Request Interceptor to add header in calls before sending final request
     */
    private static class AuthorizedOKHttpInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();

            String accessToken = SharedPrefsUtil.retrieveStringValue(SharedPrefsUtil.USER_GOOGLE_TOKEN, null);
            if (accessToken == null) {
                accessToken = "";
            }

            request = request.newBuilder()
                    .addHeader("Authorization", "Token " + accessToken)
                    .build();

            return chain.proceed(request);
        }
    }

}
