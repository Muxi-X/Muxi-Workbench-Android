package com.muxi.workbench.ui.progress.model.net;

import java.util.List;

public class GetGroupUserListResponse {

    /**
     * 0 全体
     * 1 产品组
     * 2 前端组
     * 3 后端组
     * 4 安卓组
     * 5 设计组
     */

    /**
     * count : 0
     * list : [{"teamID":0,"username":"string","userID":0,"groupName":"string","groupID":0,"role":0,"email":"string","avatar":"string"}]
     */

    private int count;
    private List<ListBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * teamID : 0
         * username : string
         * userID : 0
         * groupName : string
         * groupID : 0
         * role : 0
         * email : string
         * avatar : string
         */

        private int teamID;
        private String username;
        private int userID;
        private String groupName;
        private int groupID;
        private int role;
        private String email;
        private String avatar;

        public int getTeamID() {
            return teamID;
        }

        public void setTeamID(int teamID) {
            this.teamID = teamID;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public int getGroupID() {
            return groupID;
        }

        public void setGroupID(int groupID) {
            this.groupID = groupID;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
