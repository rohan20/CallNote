package com.rohan.callnote.network;

import com.rohan.callnote.models.Note;
import com.rohan.callnote.models.User;
import com.rohan.callnote.network.response.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Rohan on 17-Jan-17.
 */

public interface APIService {

    @POST("sign_up.json")
    Call<ApiResponse<User>> signUp(@Query("name") String name, @Query("email") String email, @Query("gtoken") String token);

    @POST("add_note.json")
    Call<ApiResponse<Note>> addNote(@Query("contact") String number, @Query("text") String noteText, @Query("note_type") int callType);

    @GET("all_notes.json")
    Call<ApiResponse<List<Note>>> getNotes();

}
