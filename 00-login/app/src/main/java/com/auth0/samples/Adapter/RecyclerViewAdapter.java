package com.auth0.samples.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.auth0.samples.R;
import com.auth0.samples.Model.UserModel;

import java.util.List;

/**
 * Created by ayush on 20/3/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>  {

    private List<UserModel> userModelList;
    private List<UserModel> items;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userId, userName;

        public MyViewHolder(View view) {
            super(view);
            userId = (TextView) view.findViewById(R.id.userId);
            userName = (TextView) view.findViewById(R.id.userName);
        }
    }


    public RecyclerViewAdapter(List<UserModel> userModelList) {
        this.userModelList = userModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserModel userModel = userModelList.get(position);
        holder.userId.setText(String.valueOf(userModel.getUserCode()));
        holder.userName.setText(userModel.getUserName());
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }


    public void updateList(List<UserModel> list){
        userModelList = list;
        notifyDataSetChanged();
    }
}
