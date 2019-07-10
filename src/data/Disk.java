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
public class Disk {

    private char[] disk;
    private int numberOfCharacters = 1000;

    public Disk() {
        this.disk = new char[numberOfCharacters];
    }

    public int getNumberOfCharacters() {
        return numberOfCharacters;
    }

    public void setNumberOfCharacters(int numberOfCharacters) {
        this.numberOfCharacters = numberOfCharacters;
    }

    public char[] getDisk() {
        return disk;
    }

    public void setDisk(char[] disk) {
        this.disk = disk;
    }

    public void addIntoDisk(String characters, int posStarted, int posEnded, int indexCharacter) {
        for (int i = posStarted * 4; i < posEnded * 4; i++) {
            if (indexCharacter < characters.length()) {
                disk[i] = characters.charAt(indexCharacter);
            }
            indexCharacter++;
        }
    }
    
    public void changeDiskWithEmptySector (int fromSector, int toSector) {
        int characterInSector = toSector * 4;
        for (int i = fromSector * 4; i < (fromSector + 1) * 4; i++) {
            disk[characterInSector] = disk[i];
            characterInSector++;
        }
    }
    
    public void changeDiskWithOtherSector(int fromSector, int toSector) {
        int characterInSector = toSector * 4;
        for (int i = fromSector * 4; i < (fromSector + 1) * 4; i++) {
            char temp = disk[characterInSector];
            disk[characterInSector] = disk[i];
            disk[i] = temp;
            characterInSector++;
        }
    }

    public void showDisk() {
        for (int i = 0; i < disk.length; i++) {
            //separator
            if (i % 100 == 0 && i != 0) {
                System.out.println("");
            }
            if (i % 4 == 0) {
                System.out.print("|");
            }
            // print character, if it is \u0000 'default of character\, print a space
            if (disk[i] == '\u0000') {
                System.out.print(" ");
            } else {
                System.out.print(disk[i]);
            }

        }
        System.out.println("");
    }
}
