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
public class FilesNodeInfo {

    private String fileName;
    private String characters;
    private LinkedList<PoolNodeInfo> listOfSector;
    private int sizeOfSector = 4;

    public FilesNodeInfo(String fileName, String characters) {
        this.fileName = fileName;
        this.characters = characters;
        this.listOfSector = new LinkedList<>();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCharacters() {
        return characters;
    }

    public void setCharacters(String characters) {
        this.characters = characters;
    }
    
    public int getNumberOfSector() {
        int numOfSector = characters.length() / sizeOfSector;
        if(characters.length() % sizeOfSector != 0) {
            numOfSector++;
        }
        return numOfSector;
    }

    public LinkedList<PoolNodeInfo> getListOfSector() {
        return listOfSector;
    }

    public void setListOfSector(LinkedList<PoolNodeInfo> listOfSector) {
        this.listOfSector = listOfSector;
    }

    public void addNewPool(PoolNodeInfo poolNode) {
        this.listOfSector.add(poolNode);
    }

    public void refreshPoolOfFile() {
        int pos;
        while ((pos = getTwoPoolNearlyIndex()) != -1) {
            listOfSector.get(pos).setEndedIndex(listOfSector.get(pos + 1).getEndedIndex());
            listOfSector.remove(pos + 1);
        }
        Collections.sort(listOfSector);
    }

    private int getTwoPoolNearlyIndex() {
        Collections.sort(listOfSector);
        for (int i = 0; i < listOfSector.size() - 1; i++) {
            if ((listOfSector.get(i).getEndedIndex() + 1) == listOfSector.get(i + 1).getStartedIndex()) {
                return i;
            }
        }
        return -1;
    }

    public void showFileInfo() {
        System.out.println("------------- File " + fileName + " -------------");
        System.out.println("Characters: " + characters);
        System.out.println("List of sector: " + listOfSector.toString());
    }
}
