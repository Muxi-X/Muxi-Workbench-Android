package com.muxi.workbench.ui.project.model.bean;

import java.util.List;

public class test {

    private List<DocListBean> DocList;
    private List<?> FolderList;

    public List<DocListBean> getDocList() {
        return DocList;
    }

    public void setDocList(List<DocListBean> DocList) {
        this.DocList = DocList;
    }

    public List<?> getFolderList() {
        return FolderList;
    }

    public void setFolderList(List<?> FolderList) {
        this.FolderList = FolderList;
    }

    public static class DocListBean {
        /**
         * create_time : 2019-08-30 18:01:28
         * creator : 王鹏宇-Messi
         * id : 180
         * lastcontent : <h2><br />日常规范：</h2>
         <ol>
         <li><strong>木犀工作台的进度</strong>：至少两天一更，可以写自己这两天学了什么，看了哪些文章，哪些值得分享的内容</li>
         <l
         * name : Android组日常规范
         */

        private String create_time;
        private String creator;
        private int id;
        private String lastcontent;
        private String name;

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

        public String getLastcontent() {
            return lastcontent;
        }

        public void setLastcontent(String lastcontent) {
            this.lastcontent = lastcontent;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
