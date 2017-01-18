package com.rohan.callnote.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rohan.callnote.BaseCallNoteActivity;
import com.rohan.callnote.R;
import com.rohan.callnote.models.Note;
import com.rohan.callnote.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.everything.providers.android.calllog.Call;
import me.everything.providers.android.calllog.CallsProvider;
import me.everything.providers.android.contacts.Contact;
import me.everything.providers.android.contacts.ContactsProvider;

/**
 * Created by Rohan on 17-Jan-17.
 */

public class RecyclerViewAdapterNotes extends RecyclerView.Adapter<RecyclerViewAdapterNotes.ViewHolder> {

    Context mContext;
    List<Note> mNotesList;

    public RecyclerViewAdapterNotes(Context context) {
        mContext = context;
        mNotesList = new ArrayList<>();
    }

    public void setRecyclerViewNotesList(List<Note> notesList) {
        mNotesList = notesList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewAdapterNotes.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_note, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterNotes.ViewHolder holder, int position) {

        final Note note = mNotesList.get(position);

        //set contact name
        boolean contactIsUnknown = true;

        CallsProvider callsProvider = new CallsProvider(mContext);
        for (Call call : callsProvider.getCalls().getList()) {
            if (call.number.equals(note.getNumber())) {
                holder.mCallerNameTextView.setText(call.name);
                contactIsUnknown = false;
                break;
            }
        }

        if (contactIsUnknown)
            holder.mCallerNameTextView.setText(note.getNumber());

        //set contact number
        holder.mCallerNumberTextView.setText(note.getNumber());

        //set call type
        if (Integer.parseInt(note.getCallType()) == Constants.CALL_MISSED) {
            holder.mNoteRelativeLayout.setBackgroundColor(Color.parseColor("#FD6140"));
        } else if (Integer.parseInt(note.getCallType()) == Constants.CALL_RECEIVED) {
            holder.mNoteRelativeLayout.setBackgroundColor(Color.parseColor("#81d8d0"));
        } else {
            holder.mNoteRelativeLayout.setBackgroundColor(Color.parseColor("#00ff7f"));
        }

        //set note text
        holder.mCallNoteTextTextView.setText(note.getText());

        //set note time
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, HH:mm");
        Date date = new Date(Long.parseLong(note.getTimestamp()) * 1000);
        String dateString = formatter.format(date);
        holder.mCallTimeTextView.setText(dateString);

        holder.mNoteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String phone = note.getNumber();

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                        .setMessage(note.getNumber())
                        .setCancelable(true)
                        .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                mContext.startActivity(intent);
                            }
                        })
                        .setNegativeButton("SMS", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phone, null)));
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        if (position == mNotesList.size() - 1)
            ((BaseCallNoteActivity) mContext).dismissProgressDialog();

    }

    @Override
    public int getItemCount() {
        return mNotesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.note_item_card_view)
        CardView mNoteCard;
        @BindView(R.id.note_item_relative_layout)
        RelativeLayout mNoteRelativeLayout;

        @BindView(R.id.caller_name_text_view)
        TextView mCallerNameTextView;
        @BindView(R.id.caller_number_text_view)
        TextView mCallerNumberTextView;
        @BindView(R.id.caller_note_text_text_view)
        TextView mCallNoteTextTextView;
        @BindView(R.id.call_time_text_view)
        TextView mCallTimeTextView;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }


    }
}
