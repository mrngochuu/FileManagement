/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.LinkedList;
import util.MyToys;

/**
 *
 * @author Do Ngoc Huu
 */
public class Cabinet {

    private Files files = new Files();
    private Pool pool = new Pool();
    private Disk disk = new Disk();

    public Cabinet() {
    }

    public void saveFile() {
        String name, characters;
        int numOfCharacter, indexOfFile;
        FilesNodeInfo currFile;
        
        // check if the pool has no sector
        if (pool.getTotalEmptySector() == 0) {
            System.out.println("There is no more sector to store.");
            return;
        }
        
        //check if the file exists
        do {
            name = MyToys.getString("Input file name to save: ", "File name is required.");
            indexOfFile = getIndexOfFile(name);
            if (indexOfFile != -1) {
                System.out.println("File already exists.");
            }
        } while (indexOfFile != -1);

        characters = MyToys.getString("Input characters: ", "Characters of file is required.");

        currFile = new FilesNodeInfo(name, characters);
        
        //Check if the pool has enough sectors to save the file
        if(currFile.getNumberOfSector() > pool.getTotalEmptySector()) {
            System.out.println("The pool has not enough sectors to save file");
            return;
        }
        files.getFiles().add(currFile);

        addNewPoolIntoFile(currFile, currFile.getNumberOfSector(), 0);

        files.refreshFiles();
        pool.refreshPool();
        System.out.println("Added files is succesful.");
        currFile.showFileInfo();
    }

    public void deleteFile() {
        if (files.getFiles().isEmpty()) {
            System.out.println("There is no file to delete");
            return;
        }
        
        showFiles();
        String name;
        name = MyToys.getString("Input file name to delete: ", "File name is required.");
        int pos = getIndexOfFile(name);
        if (pos == -1) {
            System.out.println("File does not exist.");
            return;
        }

        LinkedList<PoolNodeInfo> listOfSector = files.getFiles().get(pos).getListOfSector();
        for (PoolNodeInfo poolNodeInfo : listOfSector) {
            pool.getPool().add(poolNodeInfo);
            pool.refreshPool();
        }
        files.getFiles().remove(pos);
        files.refreshFiles();
        System.out.println("Deleted files is successful.");
    }

    public void showDisk() {
        disk.showDisk();
    }

    public void showPool() {
        pool.showRanglePool();
    }

    public void showFiles() {
        if (files.getFiles().isEmpty()) {
            System.out.println("There is no saved file.");
            return;
        }

        files.showAllFiles();
    }

    private void addNewPoolIntoFile(FilesNodeInfo fileInfo, int numOfSector, int indexChar) {
        files.refreshFiles();
        pool.refreshPool();
        int sectorOfFirstPool = pool.getNumberOfSectorOfFirstPoolNode();

        if (numOfSector < sectorOfFirstPool) {
            //add sectors that save file into listOfFile
            fileInfo.addNewPool(new PoolNodeInfo(pool.getPool().get(0).getStartedIndex(), pool.getPool().get(0).getStartedIndex() + numOfSector - 1));
            //add characters into disk
            disk.addIntoDisk(fileInfo.getCharacters(), pool.getPool().get(0).getStartedIndex(), pool.getPool().get(0).getStartedIndex() + numOfSector, indexChar);
            //delete sectors that save file from pool
            pool.getPool().getFirst().setStartedIndex(pool.getPool().get(0).getStartedIndex() + numOfSector);
        } else if (numOfSector == sectorOfFirstPool) {
            fileInfo.addNewPool(pool.getPool().get(0));
            disk.addIntoDisk(fileInfo.getCharacters(), pool.getPool().get(0).getStartedIndex(), pool.getPool().get(0).getStartedIndex() + numOfSector, indexChar);
            pool.getPool().remove(0);
        } else if (numOfSector > sectorOfFirstPool) {
            fileInfo.addNewPool(pool.getPool().get(0));
            disk.addIntoDisk(fileInfo.getCharacters(), pool.getPool().get(0).getStartedIndex(), pool.getPool().get(0).getEndedIndex() + 1, indexChar);
            pool.getPool().remove(0);
            //Use recursion to continue saving files
            addNewPoolIntoFile(fileInfo, numOfSector - sectorOfFirstPool, sectorOfFirstPool * 4);
        }
    }

    private int getIndexOfFile(String name) {
        for (int i = 0; i < files.getFiles().size(); i++) {
            if (name.equals(files.getFiles().get(i).getFileName())) {
                return i;
            }
        }
        return -1;
    }

    public void defragmentation() {
        //refresh
        files.refreshFiles();
        pool.refreshPool();
        
        int currSector = 0;
        for (int i = 0; i < files.getFiles().size(); i++) {
            // compute the total sector of file to use
            int totalSector = files.getFiles().get(i).getNumberOfSector();
            // use currSector to change the sector of files
            for (int j = 0; j < totalSector; j++) {
                //check currSector of which file
                int indexFileInSector = getIndexFileInSector(currSector);
                if (indexFileInSector == -1) { // currSector is empty
                    swapSectorOfFileToEmptySector(files.getFiles().get(i), currSector);
                } else { // currSector is not empty
                    if (indexFileInSector != i) { //if there are 2 different files, change the sector
                        swapSectorToSector(files.getFiles().get(i), currSector, files.getFiles().get(indexFileInSector));
                    }
                    //if they are the same file, do not thing
                }
                currSector++;
            }
        }
        System.out.println("Deragmented is successfully.");
        showDisk();
    }

    private void swapSectorToSector(FilesNodeInfo currFile, int sector, FilesNodeInfo diffFile) {
        //add new sector into currFile
        currFile.getListOfSector().add(new PoolNodeInfo(sector, sector));

        //remove this sector from diffFile
        if (diffFile.getListOfSector().get(0).getStartedIndex() == diffFile.getListOfSector().get(0).getEndedIndex()) {
            diffFile.getListOfSector().remove(0);
        } else {
            diffFile.getListOfSector().get(0).setStartedIndex(sector + 1);
        }
        int sectorToChange = -1;
        for (PoolNodeInfo poolNodeInfo : currFile.getListOfSector()) {
            if (poolNodeInfo.getStartedIndex() > sector) {
                if (poolNodeInfo.getStartedIndex() == poolNodeInfo.getEndedIndex()) {
                    // swap on disk
                    disk.changeDiskWithOtherSector(poolNodeInfo.getStartedIndex(), sector);
                    // swap on sector from currFile to diffFile
                    diffFile.getListOfSector().add(new PoolNodeInfo(poolNodeInfo.getStartedIndex(), poolNodeInfo.getStartedIndex()));
                    changeToFirstDisk(poolNodeInfo.getStartedIndex(), diffFile);
                    currFile.getListOfSector().remove(poolNodeInfo);
                } else {
                    // swap on disk
                    disk.changeDiskWithOtherSector(poolNodeInfo.getStartedIndex(), sector);
                    diffFile.getListOfSector().add(new PoolNodeInfo(poolNodeInfo.getStartedIndex(), poolNodeInfo.getStartedIndex()));
                    // swap on sector from currFile to diffFile
                    changeToFirstDisk(poolNodeInfo.getStartedIndex(), diffFile);
                    poolNodeInfo.setStartedIndex(poolNodeInfo.getStartedIndex() + 1);
                }
                break;
            }
        }
        files.refreshFiles();
        pool.refreshPool();
    }

    private boolean isFirstSector(int sector, FilesNodeInfo file) {
        if(sector == file.getListOfSector().get(0).getStartedIndex()) {
            return true;
        }
        return false;
    }
    
    private int getNearSector(int sector, FilesNodeInfo file) {
        for (int i = sector - 1; i >= 0; i--) {
            for (PoolNodeInfo poolNodeInfo : file.getListOfSector()) {
                if(i >= poolNodeInfo.getStartedIndex() && i <= poolNodeInfo.getEndedIndex()) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void changeToFirstDisk(int sector, FilesNodeInfo file) {
        boolean flag = isFirstSector(sector, file);
        while(!flag) {
            int x = getNearSector(sector, file);
            disk.changeDiskWithOtherSector(sector, x);
            sector = x;
            flag = isFirstSector(sector, file);
        }
    }

    private void swapSectorOfFileToEmptySector(FilesNodeInfo currFile, int sector) {
        // add new sector into File
        currFile.getListOfSector().add(new PoolNodeInfo(sector, sector));
        // remove a pool empty
        if (pool.getPool().get(0).getStartedIndex() != pool.getPool().get(0).getEndedIndex()) {
            pool.getPool().get(0).setStartedIndex(sector + 1);
        } else {
            pool.getPool().remove(0);
        }
        for (PoolNodeInfo poolNodeInfo : currFile.getListOfSector()) {
            if (poolNodeInfo.getStartedIndex() > sector) {
                if (poolNodeInfo.getStartedIndex() == poolNodeInfo.getEndedIndex()) {
                    // swap on disk
                    disk.changeDiskWithEmptySector(poolNodeInfo.getStartedIndex(), sector);
                    // set pool
                    pool.getPool().add(new PoolNodeInfo(poolNodeInfo.getStartedIndex(), poolNodeInfo.getStartedIndex()));
                    // swap on sector of file
                    currFile.getListOfSector().remove(poolNodeInfo);
                } else {
                    // swap on disk
                    disk.changeDiskWithEmptySector(poolNodeInfo.getStartedIndex(), sector);
                    // set pool
                    pool.getPool().add(new PoolNodeInfo(poolNodeInfo.getStartedIndex(), poolNodeInfo.getStartedIndex()));
                    // swap on sector of file
                    poolNodeInfo.setStartedIndex(poolNodeInfo.getStartedIndex() + 1);
                }
                break;
            }
        }
        files.refreshFiles();
        pool.refreshPool();
    }

    private int getIndexPoolContantSector(int sector) {
        for (int i = 0; i < pool.getPool().size(); i++) {
            if (sector >= pool.getPool().get(i).getStartedIndex() && sector <= pool.getPool().get(i).getEndedIndex()) {
                return i;
            }
        }
        return -1;
    }

    private int getIndexFileInSector(int sector) {
        for (int i = 0; i < files.getFiles().size(); i++) {
            for (int j = 0; j < files.getFiles().get(i).getListOfSector().size(); j++) {
                if (sector >= files.getFiles().get(i).getListOfSector().get(j).getStartedIndex()
                        && sector <= files.getFiles().get(i).getListOfSector().get(j).getEndedIndex()) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int getTotalUsedSector() {
        int total = 0;
        for (FilesNodeInfo file : files.getFiles()) {
            for (PoolNodeInfo poolNodeInfo : file.getListOfSector()) {
                total += (poolNodeInfo.getEndedIndex() - poolNodeInfo.getStartedIndex() + 1);
            }
        }
        return total;
    }
}
