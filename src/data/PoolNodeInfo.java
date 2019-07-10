/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author Do Ngoc Huu
 */
public class PoolNodeInfo implements Comparable<PoolNodeInfo>{
    private int startedIndex, endedIndex;

    public PoolNodeInfo(int startedIndex, int endedIndex) {
        this.startedIndex = startedIndex;
        this.endedIndex = endedIndex;
    }
    
    public int getNumberOfSector() {
        return endedIndex - startedIndex + 1;
    }

    public int getStartedIndex() {
        return startedIndex;
    }
    
    public void setStartedIndex(int startedIndex) {
        this.startedIndex = startedIndex;
    }

    public int getEndedIndex() {
        return endedIndex;
    }

    public void setEndedIndex(int endedIndex) {
        this.endedIndex = endedIndex;
    }

    @Override
    public String toString() {
        return "{StartedIndex=" + startedIndex + ", endedIndex=" + endedIndex + '}';
    }
    
    public void showRangesOfPool() {
        System.out.println("Rangle: " + startedIndex + " - " + endedIndex);
    }

    @Override
    public int compareTo(PoolNodeInfo o) {
        return this.startedIndex - o.startedIndex;
    }
}
