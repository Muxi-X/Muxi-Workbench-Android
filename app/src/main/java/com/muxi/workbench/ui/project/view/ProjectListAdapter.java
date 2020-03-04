package com.muxi.workbench.ui.project.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.muxi.workbench.R;
import com.muxi.workbench.ui.project.model.bean.Project;

import java.util.ArrayList;

public class ProjectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public Project mProject;
    public ProjectListAdapter(){
        mProject=new Project();
        mProject.setList(new ArrayList<>());
    }

    public OnItemClickListener<Project.ListBean>listener;

    public void setProject(Project project){
        mProject=project;
    }

    public void addProject(Project project){
        mProject.getList().addAll(project.getList());
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListener<Project.ListBean> listener){
        this.listener=listener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project_list,parent,false);



        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder=(MyViewHolder) holder;
        myViewHolder.proName.setText(mProject.getList().get(position).getProjectName());

        myViewHolder.itemView.setOnClickListener(v -> {
            if (listener!=null)
                listener.onclick(mProject.getList().get(position),position);
        });

    }

    @Override
    public int getItemCount() {
        Log.i("project", "getItemCount: "+(mProject.getList()==null?0:mProject.getList().size()));
        return mProject.getList()==null?0:mProject.getList().size();
    }



    interface OnItemClickListener<T>{
        void onclick(T t,int position);

    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView image;
        TextView proName;
        TextView proDes;
        View itemView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
            proName=itemView.findViewById(R.id.project_name);
        }
    }
}
