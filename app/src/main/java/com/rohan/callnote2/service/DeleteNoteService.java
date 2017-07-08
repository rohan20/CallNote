package com.rohan.callnote2.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.rohan.callnote2.R;
import com.rohan.callnote2.network.ApiClient;
import com.rohan.callnote2.network.ApiResponse;
import com.rohan.callnote2.utils.Constants;
import com.rohan.callnote2.utils.Contract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rohan on 23-Jan-17.
 */

public class DeleteNoteService extends IntentService{

    static final String TAG = DeleteNoteService.class.getSimpleName();

    public DeleteNoteService() {
        super("DeleteNoteService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        deleteNoteFromAPI(intent);
    }

    private void deleteNoteFromAPI(Intent intent) {

        final int id = intent.getIntExtra(Constants.NOTE_TO_BE_DELETED, -1);
        Log.e(TAG, "id: " + id);

        Call<ApiResponse> call = ApiClient.getApiService().deleteNote(String.valueOf(id));

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    String selection = Contract.NotesEntry.COLUMN_SERVER_ID + " =? ";
                    String[] selectionArgs = {String.valueOf(id)};

                    getApplicationContext().getContentResolver().delete(Contract.NotesEntry.CONTENT_URI,
                            selection, selectionArgs);

                    Toast.makeText(DeleteNoteService.this, getString(R.string.note_deleted), Toast
                            .LENGTH_SHORT)
                            .show();

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string
                            .unable_to_delete_note), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.unable_to_delete_note),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
