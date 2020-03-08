package com.muxi.workbench.ui.project.model.bean;

import java.util.ArrayList;
import java.util.List;

public class FilesId {


    private List<Integer> doc;
    private List<Integer> file;
    private List<Integer>folder=new ArrayList<>();
    public List<Integer> getDoc() {
        return doc;
    }

    public void setDoc(List<Integer> doc) {
        this.doc = doc;
    }

    public List<Integer> getFile() {
        return file;
    }

    public void setFile(List<Integer> file) {
        this.file = file;
    }
}
