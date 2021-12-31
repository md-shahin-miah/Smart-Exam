package com.crux.qxm.adapter.questions;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crux.qxm.R;

import java.util.ArrayList;


public class QxmCategoryAdapter extends RecyclerView.Adapter<QxmCategoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> qSetInterestArrayList;
    private ArrayList<String> selectedInterestArrayList;
    private CategorySelectListener categorySelectListener;


    public QxmCategoryAdapter(Context context, ArrayList<String> qSetInterestArrayList, ArrayList<String> selectedInterestArrayList, CategorySelectListener categorySelectListener) {
        this.context = context;
        this.qSetInterestArrayList = qSetInterestArrayList;
        this.selectedInterestArrayList = selectedInterestArrayList;
        this.categorySelectListener = categorySelectListener;

    }

    @NonNull
    @Override
    public QxmCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View qSetInterestView = inflater.inflate(R.layout.rec_layout_qxm_category, parent, false);
        return new QxmCategoryAdapter.ViewHolder(qSetInterestView);

    }

    @Override
    public void onBindViewHolder(@NonNull QxmCategoryAdapter.ViewHolder holder, int position) {

        if (selectedInterestArrayList != null) {
            String category = qSetInterestArrayList.get(position);
            if (selectedInterestArrayList.contains(category)) {
                holder.interestMainContent.setBackground(context.getResources().getDrawable(
                        R.drawable.background_qxm_category_grid_item)
                );
            }
        }

        holder.qSetInterestTV.setText(qSetInterestArrayList.get(position));

        holder.interestMainContent.setOnClickListener(v -> categorySelectListener.onCategorySelected(
                holder.interestMainContent, holder.qSetInterestTV, position
        ));

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return qSetInterestArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView selQInterestIV;
        TextView qSetInterestTV;
        RelativeLayout interestMainContent;
        RelativeLayout qSetInterestCV;

        public ViewHolder(View itemView) {
            super(itemView);
            selQInterestIV = itemView.findViewById(R.id.selQInterestIV);
            qSetInterestTV = itemView.findViewById(R.id.qSetInterestTV);
            interestMainContent = itemView.findViewById(R.id.interestMainContent);
            qSetInterestCV = itemView.findViewById(R.id.qSetInterestCV);

        }
    }

    public interface CategorySelectListener {

        void onCategorySelected(RelativeLayout interestMainContent, TextView interestTV, int position);
    }
}
