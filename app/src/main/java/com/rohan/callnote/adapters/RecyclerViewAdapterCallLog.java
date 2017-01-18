package com.rohan.callnote.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohan.callnote.AddNoteFragment;
import com.rohan.callnote.BaseCallNoteActivity;
import com.rohan.callnote.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.everything.providers.android.calllog.Call;

/**
 * Created by Rohan on 17-Jan-17.
 */

public class RecyclerViewAdapterCallLog extends RecyclerView.Adapter<RecyclerViewAdapterCallLog.ViewHolder> {

    Context mContext;
    List<Call> mCallsList;

    public RecyclerViewAdapterCallLog(Context context) {
        mContext = context;
    }

    public void setRecyclerViewCallLogList(List<Call> callsList) {
        mCallsList = callsList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewAdapterCallLog.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_call_log, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterCallLog.ViewHolder holder, int position) {

        final Call call = mCallsList.get(position);

        //set caller name
        if (call.name == null) {
            holder.mCallNameTextView.setText(call.number);
        } else {
            holder.mCallNameTextView.setText(call.name);
        }

        final int callType;

        //set call type
        if (call.type.equals(Call.CallType.MISSED)) {
            callType = 0;
            holder.mCallTypeImageView.setImageResource(R.drawable.ic_call_missed_red_300_18dp);
        } else if (call.type.equals(Call.CallType.INCOMING)) {
            callType = 1;
            holder.mCallTypeImageView.setImageResource(R.drawable.ic_call_received_blue_300_18dp);
        } else {
            callType = 2;
            holder.mCallTypeImageView.setImageResource(R.drawable.ic_call_made_green_300_18dp);
        }

        //set call number
        holder.mCallNumberTextView.setText(call.number);

        //set call date
        Date date = new Date(call.callDate);
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, HH:mm");
        String dateString = formatter.format(date);
        holder.mCallDateTextView.setText(dateString);

        holder.mCallCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("name", call.name);
                bundle.putString("number", call.number);
                bundle.putInt("callType", callType);
                bundle.putLong("date", call.callDate);
                ((BaseCallNoteActivity) mContext).switchFragment(new AddNoteFragment(), true, bundle, AddNoteFragment.class.getSimpleName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCallsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.call_log_item_card)
        CardView mCallCard;

        @BindView(R.id.call_name_text_view)
        TextView mCallNameTextView;
        @BindView(R.id.call_number_text_view)
        TextView mCallNumberTextView;
        @BindView(R.id.call_type_image_view)
        ImageView mCallTypeImageView;
        @BindView(R.id.call_date_text_view)
        TextView mCallDateTextView;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }


    }
}
