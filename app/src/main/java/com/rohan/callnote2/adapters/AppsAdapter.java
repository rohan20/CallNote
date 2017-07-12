package com.rohan.callnote2.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohan.callnote2.R;
import com.rohan.callnote2.models.App;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rohan on 12-Jul-17.
 */

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    List<App> mAppsList;
    Context mContext;

    public AppsAdapter(Context context, List<App> appsList) {
        mContext = context;
        mAppsList = appsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_app_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Picasso.with(mContext).load(mAppsList.get(position).getAppIconUrl()).placeholder(R
                .drawable.placeholder_no_image_available).into(holder.appIconImageView);
        holder.appTitleTextView.setText(mAppsList.get(position).getAppName());
        holder.appDescTextView.setText(mAppsList.get(position).getAppDesc());

        holder.appItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(mAppsList.get(position).getAppPlayStoreUrl()));
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAppsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.app_icon_image_view)
        ImageView appIconImageView;
        @BindView(R.id.app_title_text_view)
        TextView appTitleTextView;
        @BindView(R.id.app_desc_text_view)
        TextView appDescTextView;
        @BindView(R.id.item_app_layout)
        CardView appItemLayout;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

    }
}
