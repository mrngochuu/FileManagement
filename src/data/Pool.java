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
public class Pool {

    private LinkedList<PoolNodeInfo> pool = new LinkedList<>();

    public Pool() {
        pool.add(new PoolNodeInfo(0, 250));
    }

    public LinkedList<PoolNodeInfo> getPool() {
        return pool;
    }

    public void setPool(LinkedList<PoolNodeInfo> pool) {
        this.pool = pool;
    }
    
    public int getTotalEmptySector() {
        int total = 0;
        for (PoolNodeInfo poolNodeInfo : pool) {
            total += poolNodeInfo.getNumberOfSector();
        }
        return total;
    }
    public int getNumberOfSectorOfFirstPoolNode() {
        return pool.get(0).getNumberOfSector();
    }

    public void showRanglePool() {
        for (PoolNodeInfo poolNodeInfo : pool) {
            poolNodeInfo.showRangesOfPool();
        }
    }

    public void refreshPool() {
        int pos;
        while ((pos = getTwoPoolNealyIndex()) != -1) {
            pos = getTwoPoolNealyIndex();
            pool.get(pos).setEndedIndex(pool.get(pos + 1).getEndedIndex());
            pool.remove(pos + 1);
        }
        Collections.sort(pool);
    }

    private int getTwoPoolNealyIndex() {
        Collections.sort(pool);
        for (int i = 0; i < pool.size() - 1; i++) {
            if ((pool.get(i).getEndedIndex() + 1) == pool.get(i + 1).getStartedIndex()) {
                return i;
            }
        }
        return -1;
    }
}
