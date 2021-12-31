package com.crux.qxm.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.utils.Model;

import java.util.ArrayList;

public class MultipleSelectAdapter extends RecyclerView.Adapter<MultipleSelectAdapter.ViewHolder> {

    public interface ChnageStatusListener{
        void onItemChangeListener(int position, Model model);
    }

    ArrayList<Model> interestList;
    Context mContext;
    ChnageStatusListener chnageStatusListener;

    public void setModel(ArrayList<Model> interestList){
        this.interestList=interestList;
    }

    public MultipleSelectAdapter(ArrayList<Model> interestList, Context mContext, ChnageStatusListener chnageStatusListener) {
        this.interestList = interestList;
        this.mContext = mContext;
        this.chnageStatusListener = chnageStatusListener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rec_layout_qxm_category,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {



        Model model=interestList.get(i);
        if(model!=null){
            viewHolder.text.setText(model.getText());
            viewHolder.position=i;
//            viewHolder.image.setImageResource(model.getImage());
            if(model.isSelect()){
                viewHolder.view.setBackground(mContext.getResources().getDrawable(
                        R.drawable.background_qxm_category_grid_item)
                );
            }
            else{
                viewHolder.view.setBackground(null);
            }
        }
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model model1=interestList.get(i);
                if(model1.isSelect()){
                    model1.setSelect(false);
                }else{
                    model1.setSelect(true);
                }
                interestList.set(viewHolder.position,model1);
                if(chnageStatusListener!=null){
                    chnageStatusListener.onItemChangeListener(viewHolder.position,model1);
                }
                notifyItemChanged(viewHolder.position);

            }
        });



    }

    @Override
    public int getItemCount() {
        if(interestList!=null){
            return interestList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView image;
        public TextView text;
        public View view;
        public int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            image=itemView.findViewById(R.id.selQInterestIV);
            text=itemView.findViewById(R.id.qSetInterestTV);
        }
    }
}
