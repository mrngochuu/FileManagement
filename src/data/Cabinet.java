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

        if (pool.getNumberOfSectorOfFirstPoolNode() == 0) {
            System.out.println("There is no more sector to store.");
            return;
        }

        do {
            name = MyToys.getString("Input file name: ", "File name is required.");
            indexOfFile = getIndexOfFile(name);
            if (indexOfFile != -1) {
                System.out.println("File exist.");
            }
        } while (indexOfFile != -1);

        characters = MyToys.getString("Input characters: ", "Characters of file is required.");

        currFile = new FilesNodeInfo(name, characters);
        files.getFiles().add(currFile);

        addNewPoolIntoFile(currFile, currFile.getNumberOfSector(), 0);

        files.refreshFiles();
        pool.refreshPool();
        System.out.println("Added files is succesful.");
    }

    public void deleteFile() {
        if (files.getFiles().isEmpty()) {
            System.out.println("There is no file to delete");
            return;
        }

        String name;
        name = MyToys.getString("Input file name: ", "File name is required.");
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
            fileInfo.addNewPool(new PoolNodeInfo(pool.getPool().get(0).getStartedIndex(), pool.getPool().get(0).getStartedIndex() + numOfSector - 1));
            //disk
            disk.addIntoDisk(fileInfo.getCharacters(), pool.getPool().get(0).getStartedIndex(), pool.getPool().get(0).getStartedIndex() + numOfSector, indexChar);
            pool.getPool().getFirst().setStartedIndex(pool.getPool().get(0).getStartedIndex() + numOfSector);
        } else if (numOfSector == sectorOfFirstPool) {
            fileInfo.addNewPool(pool.getPool().get(0));
            disk.addIntoDisk(fileInfo.getCharacters(), pool.getPool().get(0).getStartedIndex(), pool.getPool().get(0).getStartedIndex() + numOfSector, indexChar);
            pool.getPool().remove(0);
        } else if (numOfSector > sectorOfFirstPool) {
            fileInfo.addNewPool(pool.getPool().get(0));
            disk.addIntoDisk(fileInfo.getCharacters(), pool.getPool().get(0).getStartedIndex(), pool.getPool().get(0).getEndedIndex() + 1, indexChar);
            pool.getPool().remove(0);
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

    public void deflagmentation() {
        files.refreshFiles();
        pool.refreshPool();
        int currSector = 0;
        for (FilesNodeInfo file : files.getFiles()) {
            int totalSector = file.getNumberOfSector();
            for (int i = 0; i < totalSector; i++) {
                int indexFileInSector = getIndexFileInSector(currSector);
                if (indexFileInSector == -1) {
                    swapSectorOfFileToEmptySector(file, currSector);
                    currSector++;
                }
            }
        }
    }

    private void swapSectorOfFileToEmptySector(FilesNodeInfo currFile, int sector) {
        currFile.getListOfSector().add(sector, new PoolNodeInfo(sector, sector));
        // set pool
        if (pool.getPool().get(0).getStartedIndex() != pool.getPool().get(0).getEndedIndex()) {
            pool.getPool().get(0).setStartedIndex(sector + 1);
        } else {
            pool.getPool().remove(0);
        }
        for (PoolNodeInfo poolNodeInfo : currFile.getListOfSector()) {
            if (poolNodeInfo.getStartedIndex() > sector) {
                if (poolNodeInfo.getStartedIndex() == poolNodeInfo.getEndedIndex()) {
                    // swap on disk
                    disk.changeDisk(poolNodeInfo.getStartedIndex(), sector);
                    // set pool
                    pool.getPool().add(new PoolNodeInfo(poolNodeInfo.getStartedIndex(), poolNodeInfo.getStartedIndex()));
                    // swap on sector of file
                    currFile.getListOfSector().remove(poolNodeInfo);
                } else {
                    // swap on disk
                    disk.changeDisk(poolNodeInfo.getStartedIndex(), sector);
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
