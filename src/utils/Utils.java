package utils;

import java.util.Scanner;

public class Utils {

//----------------------------------------------------    
    public static String getValue(String input) {
        Scanner sc = new Scanner(System.in);
        System.out.print(input);
        return sc.nextLine();
    }

//----------------------------------------------------    
    public static int checkInt(String s, String errmsg) {
        int num = 0;
        while (true) {
            try {

                num = Integer.parseInt(getValue(s));
                if (num <= 0) {
                    System.out.println("Enter a number > 0");
                } else {
                    return num;
                }
            } catch (Exception e) {
                System.err.println(errmsg);
            }
        }
    }
//----------------------------------------------------    

    public static double checkDouble(String s, String errmsg) {
        double num = 0;
        while (true) {
            try {

                num = Double.parseDouble(getValue(s));
                if (num <= 0) {
                    System.out.println("Enter a number > 0");
                } else {
                    return num;
                }
            } catch (Exception e) {
                System.err.println(errmsg);
            }
        }
    }
//----------------------------------------------------    

    public static String checkString(String inputmsg, String errmsg, String regex) {

        while (true) {
            try {
                String s = getValue(inputmsg);
                if (s.matches(regex)) {
                    return s;
                } else {
                    System.out.println(errmsg);
                }
            } catch (Exception e) {
                System.err.println(errmsg);
            }
        }
    }
//----------------------------------------------------    

    public static char checkChar(String inputMsg, String errMsg, String regex) {

        while (true) {

            try {
                String input = getValue(inputMsg);
                if (input.isEmpty()) {
                    System.err.println(" Please enter a single character.");
                    continue;
                }

                // Check if input length is greater than 1
                if (input.length() > 1) {
                    System.err.println(errMsg + " Please enter only one character.");
                    continue;
                }

                char ch = input.charAt(0);

                // Check if character matches the regex (if provided)
                if (regex != null && !String.valueOf(ch).matches(regex)) {
                    System.err.println(errMsg);
                    continue;
                }

                return ch;
            } catch (Exception e) {
                System.err.println(errMsg);
            }
        }
    }

//----------------------------------------------------   
    public static void main(String[] args) {
    }
}
