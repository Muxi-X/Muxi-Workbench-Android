package com.muxi.workbench.ui.project.model.bean;

public class Folder {


    /**
     * filetree : {"folder":true,"id":0,"name":"全部文件","router":[0],"selected":true,"finalSelected":true,"child":[{"folder":true,"id":"19","name":"任务一","child":[{"folder":false,"id":"37","name":"gojek_driver_app_android_assignment (1).pdf","router":[0,"19","37"]}],"router":[0,"19"]}]}
     */

    private String doctree;
    private String filetree;
    public String getDoctree() {
        return doctree;
    }

    public void setDoctree(String doctree) {
        this.doctree = doctree;
    }

    public String getFiletree() {
        return filetree;
    }

    public void setFiletree(String filetree) {
        this.filetree = filetree;
    }
}
