package com.immoc.service;

import com.immoc.constant.CommonConstant;

public class GetInput {

    public static int getInputClassNo() {
        int classNo = 0;
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        for (;;) {
            System.out.println("请输入需要下载的课程编号（如：" + CommonConstant.IMOOC_VIDEO_URL + "learn/601，则输入601）：");
            try {
                classNo = scanner.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("课程编号填写错误，只能输入数字!");
                scanner.nextLine();
            }
        }
        return classNo;
    }

}
