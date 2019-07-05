/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author Do Ngoc Huu
 */
public class Files {
    private LinkedList<FilesNodeInfo> files = new LinkedList<>();

    public Files() {
    }

    public LinkedList<FilesNodeInfo> getFiles() {
        return files;
    }

    public void setFiles(LinkedList<FilesNodeInfo> files) {
        this.files = files;
    }
    
    public void showAllFiles() {
        for (FilesNodeInfo file : files) {
            file.showFileInfo();
        }
    }
    
    public void refreshFiles() {
        for (FilesNodeInfo file : files) {
            file.refreshPoolOfFile();
        }
    }
}
