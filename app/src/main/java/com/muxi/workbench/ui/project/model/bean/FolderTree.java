package com.muxi.workbench.ui.project.model.bean;

import java.util.List;

public class FolderTree {


    /**
     * folder : true
     * id : 0
     * name : 全部文件
     * router : [0]
     * selected : true
     * finalSelected : true
     * child : [{"folder":true,"id":"19","name":"u4efbu52a1u4e00","child":[{"folder":false,"id":"37","name":"gojek_driver_app_android_assignment (1).pdf","router":[0,"19","37"]}],"router":[0,"19"]}]
     */

    private boolean folder;
    private int id;
    private String name;
    private boolean selected;
    private boolean finalSelected;
    private List<Integer> router;
    private List<ChildBeanX> child;

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isFinalSelected() {
        return finalSelected;
    }

    public void setFinalSelected(boolean finalSelected) {
        this.finalSelected = finalSelected;
    }

    public List<Integer> getRouter() {
        return router;
    }

    public void setRouter(List<Integer> router) {
        this.router = router;
    }

    public List<ChildBeanX> getChild() {
        return child;
    }

    public void setChild(List<ChildBeanX> child) {
        this.child = child;
    }

    public static class ChildBeanX {
        /**
         * folder : true
         * id : 19
         * name : u4efbu52a1u4e00
         * child : [{"folder":false,"id":"37","name":"gojek_driver_app_android_assignment (1).pdf","router":[0,"19","37"]}]
         * router : [0,"19"]
         */

        private boolean folder;
        private String id;
        private String name;
        private List<ChildBean> child;
        private List<Integer> router;

        public boolean isFolder() {
            return folder;
        }

        public void setFolder(boolean folder) {
            this.folder = folder;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ChildBean> getChild() {
            return child;
        }

        public void setChild(List<ChildBean> child) {
            this.child = child;
        }

        public List<Integer> getRouter() {
            return router;
        }

        public void setRouter(List<Integer> router) {
            this.router = router;
        }

        public static class ChildBean {
            /**
             * folder : false
             * id : 37
             * name : gojek_driver_app_android_assignment (1).pdf
             * router : [0,"19","37"]
             */

            private boolean folder;
            private String id;
            private String name;
            private List<Integer> router;

            public boolean isFolder() {
                return folder;
            }

            public void setFolder(boolean folder) {
                this.folder = folder;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<Integer> getRouter() {
                return router;
            }

            public void setRouter(List<Integer> router) {
                this.router = router;
            }
        }
    }
}
