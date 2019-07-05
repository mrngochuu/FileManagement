/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Scanner;

/**
 *
 * @author Do Ngoc Huu
 */
public class MyToys {

    private static Scanner sc = new Scanner(System.in);

    public static String getString(String inputMsg, String erroMsg) {
        String str = "";
        while (true) {
            try {
                System.out.print(inputMsg);
                str = sc.nextLine().trim();
                if (str.isEmpty()) {
                    throw new Exception();
                }
                return str;

            } catch (Exception e) {
                System.out.println(erroMsg);
            }
        }
    }

    public static int getAnInteger(String inputMsg, String errorMsg, int lowestBound, String errorBound) {
        int num;
        while (true) {
            try {
                System.out.print(inputMsg);
                num = Integer.parseInt(sc.nextLine());
                if (num <= lowestBound) {
                    System.out.println(errorBound);
                } else {
                    return num;
                }
            } catch (Exception e) {
                System.out.println(errorMsg);
            }
        }
    }

    public static int getAnInteger(String inputMsg, String errorMsg, int lowerBound, int upperBound) {
        int num, temp;

        if (lowerBound > upperBound) {
            temp = lowerBound;
            lowerBound = upperBound;
            upperBound = temp;
        }

        while (true) {
            try {
                System.out.print(inputMsg);
                num = Integer.parseInt(sc.nextLine());
                if (num < lowerBound || num > upperBound) {
                    throw new Exception();
                }
                return num;
            } catch (Exception e) {
                System.out.println(errorMsg);
            }
        }
    }
}
