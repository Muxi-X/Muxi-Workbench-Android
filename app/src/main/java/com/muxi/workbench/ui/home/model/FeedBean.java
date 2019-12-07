package com.muxi.workbench.ui.home.model;

import java.util.List;

public class FeedBean {

    /**
     * dataList : [{"user":{"name":"string","id":0,"avatar_url":"string"},"acton":"string","source":{"kind_id":0,"object_id":0,"project_id":0,"name":"string"},"time":"string","split":true}]
     * hasNext : true
     * pageMax : 0
     * pageNum : 0
     * rowsNum : 0
     */

    private boolean hasNext;
    private int pageMax;
    private int pageNum;
    private int rowsNum;
    private List<DataListBean> dataList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getPageMax() {
        return pageMax;
    }

    public void setPageMax(int pageMax) {
        this.pageMax = pageMax;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getRowsNum() {
        return rowsNum;
    }

    public void setRowsNum(int rowsNum) {
        this.rowsNum = rowsNum;
    }

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public static class DataListBean {
        /**
         * user : {"name":"string","id":0,"avatar_url":"string"}
         * acton : string
         * source : {"kind_id":0,"object_id":0,"project_id":0,"name":"string"}
         * time : string
         * split : true
         */

        private UserBean user;
        private String acton;
        private SourceBean source;
        private String time;
        private boolean split;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getActon() {
            return acton;
        }

        public void setActon(String acton) {
            this.acton = acton;
        }

        public SourceBean getSource() {
            return source;
        }

        public void setSource(SourceBean source) {
            this.source = source;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public boolean isSplit() {
            return split;
        }

        public void setSplit(boolean split) {
            this.split = split;
        }

        public static class UserBean {
            /**
             * name : string
             * id : 0
             * avatar_url : string
             */

            private String name;
            private int id;
            private String avatar_url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }
        }

        public static class SourceBean {
            /**
             * kind_id : 0
             * object_id : 0
             * project_id : 0
             * name : string
             */

            private int kind_id;
            private int object_id;
            private int project_id;
            private String name;

            public int getKind_id() {
                return kind_id;
            }

            public void setKind_id(int kind_id) {
                this.kind_id = kind_id;
            }

            public int getObject_id() {
                return object_id;
            }

            public void setObject_id(int object_id) {
                this.object_id = object_id;
            }

            public int getProject_id() {
                return project_id;
            }

            public void setProject_id(int project_id) {
                this.project_id = project_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}