/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import data.Cabinet;
import ui.Menu;

/**
 *
 * @author Do Ngoc Huu
 */
public class Main {

    public static void main(String[] args) {
        Menu menu = new Menu("File Management");
        menu.addNewOption("1. Save file");
        menu.addNewOption("2. Delete file");
        menu.addNewOption("3. Deflagmentation");
        menu.addNewOption("4. Show all files");
        menu.addNewOption("5. Show pool");
        menu.addNewOption("6. Show disk");
        menu.addNewOption("7. Exit");

        Cabinet cage = new Cabinet();
        int choice;
        do {
            menu.showMenu();
            choice = menu.getChoice();
            switch (choice) {
                case 1:
                    cage.saveFile();
                    break;
                case 2:
                    cage.deleteFile();
                    break;
                case 3:
                    cage.deflagmentation();
                    break;
                case 4:
                    System.out.println("========== FILE MANAGER ==========");
                    cage.showFiles();
                    break;
                case 5:
                    System.out.println("========== POOL MANAGER ==========");
                    cage.showPool();
                    break;
                case 6:
                    System.out.println("========== DISK MANAGER ==========");
                    cage.showDisk();
                    break;
                case 7:
                    System.out.println("Bye bye, see you later.");
                    break;
            }
        } while (choice != 7);
    }
}
