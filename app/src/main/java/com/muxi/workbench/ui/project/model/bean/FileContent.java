package com.muxi.workbench.ui.project.model.bean;

public class FileContent {


    /**
     * content : <h3>主要书籍：《Android进阶之光》，《Android开发艺术探索》</h3>
     * create_time : 2019-09-01 23:41:30
     * creator : 王鹏宇-Messi
     * lasteditor : 王鹏宇-Messi
     * name : Android进阶学习
     */

    private String content;
    private String create_time;
    private String creator;
    private String lasteditor;
    private String name;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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

    public String getLasteditor() {
        return lasteditor;
    }

    public void setLasteditor(String lasteditor) {
        this.lasteditor = lasteditor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
