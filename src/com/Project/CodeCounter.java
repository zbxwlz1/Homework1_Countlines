package com.Project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Scanner;

public class CodeCounter {

    public static void main(String[] args) {

        // 输入目标目录
        System.out.println("请输入项目文件目录：");
        Scanner sc = new Scanner(System.in);
        String directoryPath = sc.nextLine();
        sc.close();
        // String directoryPath = "D:\\Coding\\Java practise\\File\\src\\com";

        // 计算目录下所有Java文件的总行数、空白行数和注释行数
        LineCountResult result = countLinesInDirectory(new File(directoryPath));

        System.out.println("该项目共有" + result.totalLines + "行代码。");
        System.out.println("该项目共有" + result.blankLines + "行空白行。");
        System.out.println("该项目共有" + result.commentLines + "行注释。");

    }

    // 递归计算目录中所有Java文件的总行数、空白行数和注释行数
    public static LineCountResult countLinesInDirectory(File directory) {
        LineCountResult result = new LineCountResult();

        // 遍历目录中的所有文件和子目录
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是目录文件夹，递归处理
                    LineCountResult subResult = countLinesInDirectory(file);
                    result.totalLines += subResult.totalLines;
                    result.blankLines += subResult.blankLines;
                    result.commentLines += subResult.commentLines;
                } else if (file.isFile() && file.getName().endsWith(".java")) {
                    // 如果是Java文件，统计行数、空白行数和注释行数
                    LineCountResult fileResult = countLinesInFile(file);
                    result.totalLines += fileResult.totalLines;
                    result.blankLines += fileResult.blankLines;
                    result.commentLines += fileResult.commentLines;
                }
            }
        }

        return result;
    }

    // 统计单个文件的行数、空白行数和注释行数
    public static LineCountResult countLinesInFile(File file) {
        LineCountResult result = new LineCountResult();
        boolean inMultiLineComment = false; // 标记是否处于多行注释中
        boolean inString = false; // 标记是否在字符串常量中

        try (Reader fr = new FileReader(file);
                BufferedReader bf = new BufferedReader(fr)) {

            String line;
            while ((line = bf.readLine()) != null) {
                result.totalLines++;

                // 去除行首尾的空白字符
                String trimmedLine = line.trim();

                // 判断是否为空白行
                if (trimmedLine.isEmpty()) {
                    result.blankLines++;
                    continue;
                }

                // 逐字符解析行内容，处理注释和字符串
                int i = 0;
                while (i < trimmedLine.length()) {
                    if (!inString && !inMultiLineComment && trimmedLine.startsWith("//", i)) {
                        // 如果当前不在字符串中且遇到单行注释
                        result.commentLines++;

                        break; // 跳过该行剩余部分
                    } else if (!inString && trimmedLine.startsWith("/*", i)) {
                        // 进入多行注释
                        inMultiLineComment = true;
                        i += 2; // 跳过 "/*"

                        // 检查是否在同一行内结束了多行注释
                        int endCommentIndex = trimmedLine.indexOf("*/", i);
                        if (endCommentIndex != -1) {
                            // 如果找到了 "*/"，表示多行注释在同一行结束
                            inMultiLineComment = false;
                            i = endCommentIndex + 2; // 跳过 "*/"
                            result.commentLines++; // 计为一行注释
                            break; // 继续处理该行剩余的部分
                        } else {
                            // 如果没有找到 "*/"，则整行都是注释
                            result.commentLines++; // 计为一行注释
                            break; // 跳过该行剩余部分
                        }
                    } else if (inMultiLineComment) {
                        // 如果处于多行注释中，整行算作注释行

                        int endCommentIndex = trimmedLine.indexOf("*/", i);
                        if (endCommentIndex != -1) {
                            // 如果找到了 "*/"，表示多行注释在同一行结束
                            inMultiLineComment = false;
                        }
                        result.commentLines++;
                        break; // 跳过该行剩余部分
                    } else if (trimmedLine.charAt(i) == '"' || trimmedLine.charAt(i) == '\'') {
                        // 遇到字符串或字符常量，切换状态
                        char stringChar = trimmedLine.charAt(i);
                        i++;
                        while (i < trimmedLine.length() && trimmedLine.charAt(i) != stringChar) {
                            if (trimmedLine.charAt(i) == '\\') {
                                // 跳过转义字符后的下一个字符
                                i++;
                            }
                            i++;
                        }
                    }
                    i++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(" [ " + file.getName() + " ] 有" + result.totalLines + "行代码, " + result.blankLines
                + "行空白行, " + result.commentLines + "行注释。");
        return result;
    }
}
