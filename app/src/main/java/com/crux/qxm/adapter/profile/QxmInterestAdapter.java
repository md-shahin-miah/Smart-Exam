package com.crux.qxm.adapter.profile;


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


public class QxmInterestAdapter extends RecyclerView.Adapter<QxmInterestAdapter.ViewHolder> {

    private Context context;
    private String currentSelectedInterest;
    private ArrayList<String> interestArrayList;
    private InterestSelectedListener interestSelectedListener;


    public QxmInterestAdapter(Context context, ArrayList<String> interestArrayList,String currentSelectedInterest, InterestSelectedListener interestSelectedListener) {
        this.context = context;
        this.currentSelectedInterest = currentSelectedInterest;
        this.interestArrayList =  interestArrayList;
        this.interestSelectedListener = interestSelectedListener;

    }

    @NonNull
    @Override
    public QxmInterestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View qSetInterestView = inflater.inflate(R.layout.rec_layout_qxm_category, parent, false);
        return new QxmInterestAdapter.ViewHolder(qSetInterestView);

    }

    @Override
    public void onBindViewHolder(@NonNull QxmInterestAdapter.ViewHolder holder, int position) {

        if (currentSelectedInterest != null) {

            if(interestArrayList.get(position).equals(currentSelectedInterest)){

                holder.qSetInterestCV.setBackground(context.getResources().getDrawable(
                        R.drawable.background_qxm_category_grid_item)
                );

            }else {

                holder.qSetInterestCV.setBackground(null);

            }

        }

        holder.qSetInterestTV.setText(interestArrayList.get(position));

        holder.interestMainContent.setOnClickListener(v -> {

            currentSelectedInterest = holder.qSetInterestTV.getText().toString().trim();
            interestSelectedListener.onInterestSelected(holder.interestMainContent,currentSelectedInterest,holder.getAdapterPosition());
            notifyDataSetChanged();


        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return interestArrayList.size();
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

    public interface InterestSelectedListener {

        void onInterestSelected(RelativeLayout interestMainContent,String selectedInterest, int position);
    }
}
