package com.Project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Scanner;

public class CodeCounter {

    public static void main(String[] args) {

        // ����Ŀ��Ŀ¼
        System.out.println("��������Ŀ�ļ�Ŀ¼��");
        Scanner sc = new Scanner(System.in);
        String directoryPath = sc.nextLine();
        sc.close();
        // String directoryPath = "D:\\Coding\\Java practise\\File\\src\\com";

        // ����Ŀ¼������Java�ļ������������հ�������ע������
        LineCountResult result = countLinesInDirectory(new File(directoryPath));

        System.out.println("����Ŀ����" + result.totalLines + "�д��롣");
        System.out.println("����Ŀ����" + result.blankLines + "�пհ��С�");
        System.out.println("����Ŀ����" + result.commentLines + "��ע�͡�");

    }

    // �ݹ����Ŀ¼������Java�ļ������������հ�������ע������
    public static LineCountResult countLinesInDirectory(File directory) {
        LineCountResult result = new LineCountResult();

        // ����Ŀ¼�е������ļ�����Ŀ¼
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // �����Ŀ¼�ļ��У��ݹ鴦��
                    LineCountResult subResult = countLinesInDirectory(file);
                    result.totalLines += subResult.totalLines;
                    result.blankLines += subResult.blankLines;
                    result.commentLines += subResult.commentLines;
                } else if (file.isFile() && file.getName().endsWith(".java")) {
                    // �����Java�ļ���ͳ���������հ�������ע������
                    LineCountResult fileResult = countLinesInFile(file);
                    result.totalLines += fileResult.totalLines;
                    result.blankLines += fileResult.blankLines;
                    result.commentLines += fileResult.commentLines;
                }
            }
        }

        return result;
    }

    // ͳ�Ƶ����ļ����������հ�������ע������
    public static LineCountResult countLinesInFile(File file) {
        LineCountResult result = new LineCountResult();
        boolean inMultiLineComment = false; // ����Ƿ��ڶ���ע����
        boolean inString = false; // ����Ƿ����ַ���������

        try (Reader fr = new FileReader(file);
                BufferedReader bf = new BufferedReader(fr)) {

            String line;
            while ((line = bf.readLine()) != null) {
                result.totalLines++;

                // ȥ������β�Ŀհ��ַ�
                String trimmedLine = line.trim();

                // �ж��Ƿ�Ϊ�հ���
                if (trimmedLine.isEmpty()) {
                    result.blankLines++;
                    continue;
                }

                // ���ַ����������ݣ�����ע�ͺ��ַ���
                int i = 0;
                while (i < trimmedLine.length()) {
                    if (!inString && !inMultiLineComment && trimmedLine.startsWith("//", i)) {
                        // �����ǰ�����ַ���������������ע��
                        result.commentLines++;

                        break; // ��������ʣ�ಿ��
                    } else if (!inString && trimmedLine.startsWith("/*", i)) {
                        // �������ע��
                        inMultiLineComment = true;
                        i += 2; // ���� "/*"

                        // ����Ƿ���ͬһ���ڽ����˶���ע��
                        int endCommentIndex = trimmedLine.indexOf("*/", i);
                        if (endCommentIndex != -1) {
                            // ����ҵ��� "*/"����ʾ����ע����ͬһ�н���
                            inMultiLineComment = false;
                            i = endCommentIndex + 2; // ���� "*/"
                            result.commentLines++; // ��Ϊһ��ע��
                            break; // �����������ʣ��Ĳ���
                        } else {
                            // ���û���ҵ� "*/"�������ж���ע��
                            result.commentLines++; // ��Ϊһ��ע��
                            break; // ��������ʣ�ಿ��
                        }
                    } else if (inMultiLineComment) {
                        // ������ڶ���ע���У���������ע����

                        int endCommentIndex = trimmedLine.indexOf("*/", i);
                        if (endCommentIndex != -1) {
                            // ����ҵ��� "*/"����ʾ����ע����ͬһ�н���
                            inMultiLineComment = false;
                        }
                        result.commentLines++;
                        break; // ��������ʣ�ಿ��
                    } else if (trimmedLine.charAt(i) == '"' || trimmedLine.charAt(i) == '\'') {
                        // �����ַ������ַ��������л�״̬
                        char stringChar = trimmedLine.charAt(i);
                        i++;
                        while (i < trimmedLine.length() && trimmedLine.charAt(i) != stringChar) {
                            if (trimmedLine.charAt(i) == '\\') {
                                // ����ת���ַ������һ���ַ�
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
        System.out.println(" [ " + file.getName() + " ] ��" + result.totalLines + "�д���, " + result.blankLines
                + "�пհ���, " + result.commentLines + "��ע�͡�");
        return result;
    }
}
