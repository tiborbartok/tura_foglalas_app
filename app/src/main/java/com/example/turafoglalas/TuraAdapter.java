package com.example.turafoglalas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TuraAdapter extends RecyclerView.Adapter<TuraAdapter.ViewHolder> implements Filterable {
    private ArrayList<TuraItem> mTuraItemsData;
    private ArrayList<TuraItem> mTuraItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;

    TuraAdapter(Context context, ArrayList<TuraItem> itemsData){
        this.mTuraItemsData = itemsData;
        this.mTuraItemsDataAll = itemsData;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_items, parent, false));
    }

    @Override
    public void onBindViewHolder(TuraAdapter.ViewHolder holder, int position) {
        TuraItem currentItem = mTuraItemsData.get(position);

        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mTuraItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return turaFilter;
    }

    private Filter turaFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<TuraItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0){
                results.count = mTuraItemsDataAll.size();
                results.values = mTuraItemsDataAll;
            } else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(TuraItem item : mTuraItemsDataAll){
                    if (item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mTuraItemsData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mNameText;
        private TextView mLengthText;
        private TextView mDateText;
        private TextView mPriceText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mNameText = itemView.findViewById(R.id.turaName);
            mLengthText = itemView.findViewById(R.id.turaLength);
            mDateText = itemView.findViewById(R.id.turaDate);
            mPriceText = itemView.findViewById(R.id.turaPrice);

            itemView.findViewById(R.id.turaSignup).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                }
            });

        }

        public void bindTo(TuraItem currentItem) {
            mNameText.setText(currentItem.getName());
            mLengthText.setText(currentItem.getLength());
            mDateText.setText(currentItem.getDate());
            mPriceText.setText(currentItem.getPrice());

            itemView.findViewById(R.id.turaSignup).setOnClickListener(view -> ((TuraListActivity)mContext).sendNotification());
            itemView.findViewById(R.id.turaDelete).setOnClickListener(view -> ((TuraListActivity)mContext).deleteItem(currentItem));
        }
    }
}

