/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.util.ArrayList;
import util.MyToys;

/**
 *
 * @author Do Ngoc Huu
 */
public class Menu {

    private String title;
    private ArrayList<String> optionsList;

    public Menu(String title) {
        this.title = title;
        this.optionsList = new ArrayList<>();
    }

    public void addNewOption(String option) {
        if (optionsList.contains(option)) {
            return;
        }
        optionsList.add(option);
    }

    public void showMenu() {
        if (optionsList.isEmpty()) {
            System.out.println("There is no option.");
        } else {
            System.out.println("============= " + this.title + " =============");
            for (String string : optionsList) {
                System.out.println(string);
            }
        }
    }

    public int getChoice() {
        if (optionsList.isEmpty()) {
            return -1;
        }

        return MyToys.getAnInteger("Input choice: ", "Choice is from 1 to " + optionsList.size() + ".",
                1, optionsList.size());
    }
}
