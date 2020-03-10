package com.muxi.workbench.ui.project.view.projectFolder;

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
import com.muxi.workbench.ui.project.FolderContract;
import com.muxi.workbench.ui.project.model.bean.FolderTree.ChildBean;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FolderListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOLDER= 0;//编辑框
    private static final int TYPE_FILE = 1;//按钮
    private List<ChildBean> mList;
    private OnItemClickListener listener;
    public FolderListAdapter(){
        mList =new ArrayList<>();
    }


    public void setEmpty(){
        if (mList.isEmpty())
            return;
        mList.clear();
        notifyDataSetChanged();

    }
    public void setmList(List<ChildBean> list){

        mList.clear();
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
    }


    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).isFolder())
            return TYPE_FOLDER;
        else
            return TYPE_FILE;
    }

        @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==TYPE_FOLDER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_folder_list, parent, false);

            return new FolderViewHolder(view);
        }

        else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_file_list, parent, false);

            return new FileViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ChildBean child= mList.get(position);
        Log.i("ttt", "onBindViewHolder: "+position);
        if (holder instanceof FileViewHolder){
            FileViewHolder viewHolder=(FileViewHolder)holder;
            viewHolder.fileName.setText(child.getName());
            viewHolder.creator.setText(child.getCreator());
            viewHolder.timeTextView.setText(child.getTime());
            viewHolder.view.setOnClickListener(v -> {
                if (listener!=null){
                    listener.onClick(mList.get(position),position);

                }
            });
            return;
        }

        if (holder instanceof FolderViewHolder){
            FolderViewHolder viewHolder=(FolderViewHolder) holder;
            viewHolder.nameTextView.setText(child.getName());
            viewHolder.item.setOnClickListener(v -> {
                if (listener!=null){
                    listener.onClick(mList.get(position),position);

                }
            });

        }


    }


    @Override
    public int getItemCount() {
        Log.i("ttt", "getItemCount: "+mList.size());
        return mList.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    interface OnItemClickListener{
        void onClick(ChildBean childBean,int position);
    }

    static class FolderViewHolder extends RecyclerView.ViewHolder{

        View item;
        ImageView imageView;
        TextView nameTextView;
        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            item=itemView;
            imageView=itemView.findViewById(R.id.folder_image);
            nameTextView=itemView.findViewById(R.id.folder_name);
        }
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        TextView creator;
        TextView timeTextView;
        View view;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            fileName=view.findViewById(R.id.file_name);
            creator=view.findViewById(R.id.file_creator);
            timeTextView=view.findViewById(R.id.file_time);
        }
    }
}
