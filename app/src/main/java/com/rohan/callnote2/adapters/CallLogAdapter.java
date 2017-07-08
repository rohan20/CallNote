package com.rohan.callnote2.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohan.callnote2.BaseCallNoteActivity;
import com.rohan.callnote2.R;
import com.rohan.callnote2.fragments.AddNoteFragment;
import com.rohan.callnote2.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.everything.providers.android.calllog.Call;

/**
 * Created by Rohan on 17-Jan-17.
 */

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder> {

    Context mContext;
    List<Call> mCallsList;

    public CallLogAdapter(Context context) {
        mContext = context;
        mCallsList = new ArrayList<>();
    }

    public void setRecyclerViewCallLogList(List<Call> callsList) {
        mCallsList = callsList;
        notifyDataSetChanged();
    }

    @Override
    public CallLogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_call_log, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CallLogAdapter.ViewHolder holder, int position) {

        final Call call = mCallsList.get(position);

        //set caller name
        if (call.name == null) {
            holder.mCallNameTextView.setText(call.number);
        } else {
            holder.mCallNameTextView.setText(call.name);
        }

        //set call type
        final int callType = ((BaseCallNoteActivity) mContext).getCallType(call.type);
        if (callType == Constants.CALL_MISSED) {
            holder.mCallTypeImageView.setImageResource(R.drawable.ic_call_missed_red_300_18dp);
        } else if (callType == Constants.CALL_RECEIVED) {
            holder.mCallTypeImageView.setImageResource(R.drawable.ic_call_received_blue_300_18dp);
        } else {
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
                bundle.putString(mContext.getString(R.string.name_key), call.name);
                bundle.putString(mContext.getString(R.string.number_key), call.number);
                bundle.putInt(mContext.getString(R.string.callType_key), callType);
                bundle.putLong(mContext.getString(R.string.date_key), call.callDate);
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
