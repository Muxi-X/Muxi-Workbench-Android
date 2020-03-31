package com.muxi.workbench.ui.notifications.model;

import java.util.List;

public class NotificationsResponse {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * action : 编辑
         * fromAvatar : http://ossworkbench.muxixyz.com/1553690265.9402404.TIM20190327203628.jpg
         * fromName : 刘竞林
         * projectID : 4
         * readed : true
         * sourceID : 258
         * sourceKind : 0
         * time : 2019-10-19 16:31:26
         */

        private String action;
        private String fromAvatar;
        private String fromName;
        private int projectID;
        private boolean readed;
        private int sourceID;
        private int sourceKind;
        private String time;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getFromAvatar() {
            return fromAvatar;
        }

        public void setFromAvatar(String fromAvatar) {
            this.fromAvatar = fromAvatar;
        }

        public String getFromName() {
            return fromName;
        }

        public void setFromName(String fromName) {
            this.fromName = fromName;
        }

        public int getProjectID() {
            return projectID;
        }

        public void setProjectID(int projectID) {
            this.projectID = projectID;
        }

        public boolean isReaded() {
            return readed;
        }

        public void setReaded(boolean readed) {
            this.readed = readed;
        }

        public int getSourceID() {
            return sourceID;
        }

        public void setSourceID(int sourceID) {
            this.sourceID = sourceID;
        }

        public int getSourceKind() {
            return sourceKind;
        }

        public void setSourceKind(int sourceKind) {
            this.sourceKind = sourceKind;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
