package com.File;

import java.util.Random;
import java.util.Scanner;

public class GuessNumberGame {
    public static void main(String[] args) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        // 生成一个1到100之间的随机数
        int targetNumber = random.nextInt(100) + 1;
        int guess = 0;

        System.out.println("欢迎来到猜数字游戏! 我已经生成了一个 1 到 100 之间的数字。");

        // 不断提示用户输入，直到猜中为止
        while (guess != targetNumber) {
            System.out.print("请输入你猜的数字: ");
            guess = scanner.nextInt();

            if (guess > targetNumber) {
                System.out.println("你猜的数字偏大了！");
            } else if (guess < targetNumber) {
                System.out.println("你猜的数字偏小了！");
            } else {
                System.out.println("恭喜你猜对了！这个数字是: " + targetNumber);
            }
        }
        scanner.close();
    }
}
