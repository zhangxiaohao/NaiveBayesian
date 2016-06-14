import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.util.exception.LoadModelException;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/14 0014.
 */
public class predictor {
    modelBuilder modelBuilder;

    predictor() {
        modelBuilder = new modelBuilder();
    }

    public ArrayList<String> apartToWords(String content) {
        try {
            CWSTagger var1 = new CWSTagger("D:\\baiduyun\\Workspaces\\MyEclipse 10\\workspace\\nlpHomework\\web\\WEB-INF\\models\\seg.m");
            return var1.tag2List(content);
        } catch (LoadModelException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    public void predict(ArrayList<String> mails) {
        for(String mail : mails) {
            double zi = 0, mu1 = 0, mu2 = 0, ret = 0;
            ArrayList<String> words = apartToWords(mail);
            int cnt = 0;
            double py = (double)modelBuilder.rubbishWords.size() / (modelBuilder.rubbishWords.size() + modelBuilder.normalWords.size());
            py = Math.log(py);
            for(String word : words) {
                if(modelBuilder.dic.contains(word) == false) continue;
                cnt ++;
                zi += Math.log(((double)modelBuilder.rubbishWordCount.get(word) + 1) / (modelBuilder.rubbishWords.size() + modelBuilder.dic.size()));
                mu1 += Math.log(((double)modelBuilder.rubbishWordCount.get(word) + 1) / (modelBuilder.rubbishWords.size() + modelBuilder.dic.size()));
                mu2 += Math.log(((double)modelBuilder.normalWordCount.get(word) + 1)/ (modelBuilder.normalWords.size() + modelBuilder.dic.size()));
//                System.out.println(cnt + " " + mu1 + " " + mu2 + " " + py);
            }
            if(cnt == 0) ret = -1;
            else ret = 1/(1 + Math.exp(mu2 - mu1));
            System.out.println(zi + " " + mu1 + " " + mu2);
            System.out.println(ret);
        }
    }

    public void run() {
        modelBuilder.getModel();
        System.out.println("----------- Start prediction! -----------");
        dataReader dataReader = new dataReader(50);
        dataReader.getMails();
        System.out.println("Rubbish:");
        predict(dataReader.RubbishMails);
        System.out.println("Normal:");
        predict(dataReader.NormalMails);
    }

    public static void main(String [] args) {
        new predictor().run();
    }
}
