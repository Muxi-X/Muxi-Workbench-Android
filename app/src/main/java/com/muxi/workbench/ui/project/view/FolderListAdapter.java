package com.muxi.workbench.ui.project.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muxi.workbench.R;
import com.muxi.workbench.ui.project.model.bean.FolderTree.ChildBean;

import java.util.ArrayList;
import java.util.List;

public class FolderListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChildBean> mList;
    private OnItemClickListener listener;
    public FolderListAdapter(){
        mList =new ArrayList<>();
    }


    public void setEmpty(){
        mList.clear();
        notifyDataSetChanged();

    }
    public void setmList(List<ChildBean> list){

        mList.clear();
        notifyDataSetChanged();
        List<ChildBean>temp=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ChildBean childBean= list.get(i);
            childBean.androidRoute=i;
            if (childBean.isFolder()){
                mList.add(childBean);
            }else {
                temp.add(childBean);
            }
        }
        mList.addAll(temp);

        notifyDataSetChanged();
        Log.i("ttt", "setList: "+Thread.currentThread().getName());
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file_folder,parent,false);

        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FolderViewHolder viewHolder=(FolderViewHolder) holder;
        ChildBean child= mList.get(position);
        if (child.isFolder()){
            viewHolder.imageView.setImageResource(R.drawable.doc_folder_icon);
            viewHolder.nameTextView.setTextColor(Color.rgb(12,128,218));
            viewHolder.nameTextView.setTypeface(Typeface.DEFAULT_BOLD);
            viewHolder.nameTextView.setTextSize(18);

        }else {
            viewHolder.imageView.setImageResource(R.drawable.doc_icon);
            viewHolder.nameTextView.setTextColor(Color.GRAY);
            viewHolder.nameTextView.setTextSize(15);
            viewHolder.nameTextView.setTypeface(Typeface.DEFAULT);
        }
        viewHolder.nameTextView.setText(child.getName());
        viewHolder.item.setOnClickListener(v -> {
            if (listener!=null){
                listener.onClick(mList.get(position),position);
            }
        });
    }


    @Override
    public int getItemCount() {
        Log.i("ttt", "getItemCount: "+ mList.size());

        return mList.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    interface OnItemClickListener{
        void onClick(ChildBean childBean,int position);
    }
    class FolderViewHolder extends RecyclerView.ViewHolder{

        View item;
        ImageView imageView;
        TextView nameTextView;
        TextView timeTextView;
        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            item=itemView;
            imageView=itemView.findViewById(R.id.folder_image);
            nameTextView=itemView.findViewById(R.id.folder_name);
            timeTextView=itemView.findViewById(R.id.folder_time);
        }
    }
}
