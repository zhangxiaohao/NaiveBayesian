package com.zhangxiaohao.nlp.service;

import edu.fudan.example.nlp.ChineseWordSegmentation;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.util.exception.LoadModelException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Administrator on 2016/6/11 0011.
 * 用于将垃圾邮件处理成标准格式
 */
@Service("mailProcessingService")
public class mailProcessingService implements ImailProcessingService {

    public HashMap<String, Integer> hashMap = new HashMap<String, Integer>();

    public HashMap<String, Integer> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, Integer> hashMap) {
        this.hashMap = hashMap;
    }

    @Override
    public String getMail() {
        String str = "";
        try {
            String path = "E:/安装包/trec06c(1)/trec06c/";
            File file = new File("E:/安装包/trec06c(1)/trec06c/full/index");
//            File file = new File("C:\\Users\\Administrator\\Desktop\\in.txt");
            Scanner in = new Scanner(file);
            while(in.hasNext()) {
                str = in.nextLine();
                System.out.println(str);
                String ss[] = str.split(" ");
                ss[1] = ss[1].substring(3);
                System.out.println(path + ss[1]);
                Scanner in2 = new Scanner(new File(path + ss[1]));
                String content = "";
                while(in2.hasNext())
                    content += in2.nextLine();
                CWSTagger var1 = new CWSTagger("D:\\baiduyun\\Workspaces\\MyEclipse 10\\workspace\\nlpHomework\\web\\WEB-INF\\models\\seg.m");
                ArrayList<String> var = var1.tag2List(content);
                if(var == null) continue;
                for(String s : var) {
                    if (hashMap.containsKey(s)) hashMap.put(s, hashMap.get(s) + 1);
                    else hashMap.put(s, 1);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LoadModelException e) {
            e.printStackTrace();
        }
        return "" + hashMap.size();
    }
}
