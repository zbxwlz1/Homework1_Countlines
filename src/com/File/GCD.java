package com.File;

/*
 * 
 * 
 */
import java.util.Scanner;

public class GCD {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);/* //*/

        // 输入两个整数
        System.out.print("请输入第一个整数: ");  
        int num1 = scanner.nextInt();
        System.out.print("请输入第二个整数: ");   
        int num2 = scanner.nextInt();/* */  //ssss

        // 计算最大公约数
        int gcd = findGCD(num1, num2);
        System.out.println("两个数的最大公约数是: " + gcd); 
        scanner.close();
    }// sadadasdadd

    // 计算最大公约数的递归方法

    public static int findGCD(int a, int b) {
        if (b == 0) {
            return a;
        } // wwwf//
        return findGCD(b, a % b);
    }
}