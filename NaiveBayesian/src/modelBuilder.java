import com.sun.applet2.AppletParameters;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.util.exception.LoadModelException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2016/6/14 0014.
 */
public class modelBuilder {
    public HashSet<String> dic;
    public ArrayList<String> rubbishWords;
    public ArrayList<String> normalWords;
    public HashMap<String, Integer> rubbishWordCount;
    public HashMap<String, Integer> normalWordCount;
    dataReader dataReader;

    modelBuilder () {
        dic = new HashSet<>();
        rubbishWordCount = new HashMap<>();
        normalWordCount = new HashMap<>();
        rubbishWords = new ArrayList<>();
        normalWords = new ArrayList<>();
        dataReader = new dataReader(20);
        dataReader.getMails();
    }

    private void getDic(ArrayList<String> mails, boolean isRubbish) {
        try {
            String content = "";
            for(String mail : mails) {
                content += mail;
            }
            CWSTagger var1 = new CWSTagger("D:\\baiduyun\\Workspaces\\MyEclipse 10\\workspace\\nlpHomework\\web\\WEB-INF\\models\\seg.m");
            if(isRubbish) rubbishWords = var1.tag2List(content);
            else normalWords = var1.tag2List(content);
        } catch (LoadModelException e) {
            e.printStackTrace();
        }
    }

    private void getWordCount() {
        for(String word : rubbishWords) {
            if(dic.contains(word) == false) {
                dic.add(word);
                rubbishWordCount.put(word, 0);
                normalWordCount.put(word, 0);
            }
            rubbishWordCount.put(word, rubbishWordCount.get(word) + 1);
        }
        for(String word : normalWords) {
            if(dic.contains(word) == false) {
                dic.add(word);
                rubbishWordCount.put(word, 0);
                normalWordCount.put(word, 0);
            }
            normalWordCount.put(word, normalWordCount.get(word) + 1);
        }
    }

    public void printModelinFile() {
        try {
            FileWriter fileWriter = new FileWriter(new File("model.txt"));
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(dataReader.rubbishCount + " " + dataReader.normalCount);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Iterator<String> iterator = dic.iterator();
            while(iterator.hasNext()) {
                String word = iterator.next();
                bufferedWriter.write(word + " " + rubbishWordCount.get(word) + " " + normalWordCount.get(word));
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getModel() {
        System.out.println("Get Rubbish Mails: " + dataReader.RubbishMails.size());
        System.out.println("Get Normal Mails: " + dataReader.NormalMails.size());
        getDic(dataReader.RubbishMails, true);
        getDic(dataReader.NormalMails, false);
        getWordCount();
        System.out.println("-------------------------------------------");
        System.out.println("The size of the dic is : " + dic.size());
        System.out.println("The size of the RubbishWord is : " + rubbishWordCount.size());
        System.out.println("The size of the NormalWord is : " + normalWordCount.size());
        System.out.println("RubbishCount: " + dataReader.rubbishCount);
        System.out.println("NormalCount: " + dataReader.normalCount);
        printModelinFile();
    }
}
