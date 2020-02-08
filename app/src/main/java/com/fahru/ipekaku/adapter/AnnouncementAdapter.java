package com.fahru.ipekaku.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fahru.ipekaku.R;
import com.fahru.ipekaku.object.AnnouncementObject;

import java.util.ArrayList;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.viewHolder> {

    private ArrayList<AnnouncementObject> list;
    private int[] imgDrw =
            {R.drawable.ann_1, R.drawable.ann_2, R.drawable.ann_3, R.drawable.ann_4, R.drawable.ann_5, R.drawable.ann_6,
                    R.drawable.ann_7, R.drawable.ann_8, R.drawable.ann_9, R.drawable.ann_10, R.drawable.ann_11, R.drawable.ann_12,
                    R.drawable.ann_13, R.drawable.ann_14, R.drawable.ann_15, R.drawable.ann_16, R.drawable.ann_17, R.drawable.ann_18,
                    R.drawable.ann_19, R.drawable.ann_20, R.drawable.ann_21, R.drawable.ann_22,R.drawable.ann_23,R.drawable.ann_24,R.drawable.ann_25,
                    R.drawable.ann_26,R.drawable.ann_27,R.drawable.ann_28,R.drawable.ann_29,R.drawable.ann_30};

    public AnnouncementAdapter(ArrayList<AnnouncementObject> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcement, parent, false);
        viewHolder vh = new viewHolder(view);
        return vh;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, msg;
        ImageView img;
        Context context;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.announcementItemTitle);
            msg = itemView.findViewById(R.id.announcementItemMsg);
            img = itemView.findViewById(R.id.announcementItemImg);
            context = itemView.getContext();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        AnnouncementObject ao = list.get(position);
        // get our html content
        holder.title.setText(ao.getTitle());
        holder.title.setTypeface(setTypefaceAgenB(holder.context));
        Spanned htmlAsSpanned = Html.fromHtml(ao.getMsg()); // used by TextView
        holder.msg.setText(htmlAsSpanned);
        holder.msg.setTypeface(setTypefaceAgenR(holder.context));
        holder.img.setImageResource(imgDrw[ao.getImg()]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected Typeface setTypefaceAgenB(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "comicbd.ttf");
    }
    protected Typeface setTypefaceAgenR(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "comic.ttf");
    }

}
