package com.muxi.workbench.ui.project.model;

import java.util.List;

public class Project {


    /**
     * count : 15
     * hasNext : true
     * list : [{"intro":"","projectID":2,"projectName":"这世界就是你的","userCount":15},{"intro":"Born to Hack.\nHack 4 Muxi.\n","projectID":3,"projectName":"木犀Web组","userCount":15},{"intro":"","projectID":4,"projectName":"木犀Android组","userCount":15},{"intro":"","projectID":5,"projectName":"菁程","userCount":15},{"intro":"【Mini-ProjectListAdapter】\n新人培养终极任务\u2014项目驱动","projectID":6,"projectName":"木犀101","userCount":15},{"intro":"工作台，世界上最厉害的团队协作工具","projectID":7,"projectName":"木犀工作台","userCount":15},{"intro":"华师姿势，尽在匣子","projectID":9,"projectName":"华师匣子","userCount":15},{"intro":"","projectID":12,"projectName":"木犀lab","userCount":15},{"intro":"这是一个设计组","projectID":13,"projectName":"木犀设计组","userCount":15},{"intro":"木犀木犀！\n千秋万代！","projectID":14,"projectName":"永远的招新","userCount":15}]
     * pageMax : 2
     * pageNow : 1
     */

    private int count;
    private boolean hasNext;
    private int pageMax;
    private int pageNow;
    private List<ListBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

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

    public int getPageNow() {
        return pageNow;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * intro :
         * projectID : 2
         * projectName : 这世界就是你的
         * userCount : 15
         */

        private String intro;
        private int projectID;
        private String projectName;
        private int userCount;

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getProjectID() {
            return projectID;
        }

        public void setProjectID(int projectID) {
            this.projectID = projectID;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public int getUserCount() {
            return userCount;
        }

        public void setUserCount(int userCount) {
            this.userCount = userCount;
        }
    }
}
