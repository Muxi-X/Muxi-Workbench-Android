package com.muxi.workbench.ui.project.model.bean;

import java.util.List;

public class FilesResponse {


    private List<FileListBean> FileList;
    private List<FileListBean> DocList;

    public List<FileListBean> getFileList() {
        return FileList;
    }

    public void setFileList(List<FileListBean> FileList) {
        this.FileList = FileList;
    }

    public List<FileListBean> getDocList() {
        return DocList;
    }

    public void setDocList(List<FileListBean> docList) {
        DocList = docList;
    }

    public static class FileListBean {
        /**
         * create_time : 2019-09-02 21:28:33
         * creator : 王鹏宇-Messi
         * id : 37
         * name : gojek_driver_app_android_assignment (1).pdf
         * url : http://ossworkbench.muxixyz.com/1567430906.8288484.gojek_driver_app_android_assignment_1.pdf
         */

        private String create_time;
        private String creator;
        private int id;
        private String name=" ";
        private String url;

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }


}
