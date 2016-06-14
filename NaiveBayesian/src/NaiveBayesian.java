import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.util.exception.LoadModelException;

import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class NaiveBayesian {

    public static HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
    public static Set<String> dic = new HashSet<String>();
    public static HashMap<String, Integer> wordCountPositive = new HashMap<String, Integer>();
    public static HashMap<String, Integer> wordCountNegative = new HashMap<String, Integer>();
    public static int RubbishMail = 0, NormalMail = 0;
    public static final int COUNT = 20;

    public static String getAMail(String path) {
        String content = "";
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(path),"GBK"));
            String line = null;
            while((line = br.readLine()) != null) {
                content += line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void getWords(String fileContent) throws LoadModelException {
        CWSTagger var1 = new CWSTagger("D:\\baiduyun\\Workspaces\\MyEclipse 10\\workspace\\nlpHomework\\web\\WEB-INF\\models\\seg.m");
        ArrayList<String> words = var1.tag2List(fileContent);
        if(words == null) return;
        for(String word : words) {
            if(hashMap.containsKey(word))hashMap.put(word, hashMap.get(word) + 1);
            else hashMap.put(word, 1);
        }
    }

    public static void run() {
        try {
            FileWriter fileWriter = new FileWriter(new File("dic.txt"));
            String path = "NaiveBayesian\\trec06c\\";
            Scanner in = new Scanner(new File(path + "full\\index"));
            String fileContent = "";
            int i = 0;
            while(in.hasNext()) {
                String[] sample = in.nextLine().split(" ");
                sample[1] = sample[1].substring(3);
                System.out.println(path + sample[1]);
                fileContent += getAMail(path + sample[1]);
                if(fileContent.length() > 10000) {
                    getWords(fileContent);
                    fileContent = "";
                }
                if(i ++ == COUNT) break;
            }
            getWords(fileContent);
            Iterator it = hashMap.keySet().iterator();
            while(it.hasNext()) {
                String sample = (String)it.next();
                if(hashMap.get(sample) > 0 && sample.length() > 1) {
                    dic.add(sample);
                    System.out.println(sample);
                    fileWriter.write(sample + " ");
                    fileWriter.flush();
                }
            }
            fileWriter.close();
            System.out.println(dic.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (LoadModelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getDic() {
        try {
            Scanner in = new Scanner(new File("dic.txt"));
            int  num = 0;
            while(in.hasNext()) {
                String str = in.next();
                dic.add(str);
                wordCountPositive.put(str, 0);
                wordCountNegative.put(str, 0);
                num ++;
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void processContent(String content, int isRubbish) {
        try {
            CWSTagger var1 = new CWSTagger("D:\\baiduyun\\Workspaces\\MyEclipse 10\\workspace\\nlpHomework\\web\\WEB-INF\\models\\seg.m");
            ArrayList<String> words = var1.tag2List(content);
            if(words == null) return;
            for(String word : words) {
                if(dic.contains(word) == false) continue;
                if(isRubbish == 1) wordCountPositive.put(word, wordCountPositive.get(word) + 1);
                else wordCountNegative.put(word, wordCountNegative.get(word) + 1 );
            }
        } catch (LoadModelException e) {
            e.printStackTrace();
        }
    }

    public static void getModel() {
        try {
            FileWriter fileWriter = new FileWriter(new File("model.txt"));
            getDic();
            String path = "NaiveBayesian\\trec06c\\", pcontent = "", ncontent = "";
            Scanner in = new Scanner(new File(path + "full\\index"));
            int cnt = 0;
            while(in.hasNext()) {
                String[] sample = in.nextLine().split(" ");
                sample[1] = sample[1].substring(3);
                System.out.println(path + sample[1]);
                String content = getAMail(path + sample[1]);
                if(sample[0].equals("spam")) {
                    RubbishMail ++;
                    pcontent += content;
                } else {
                    NormalMail ++;
                    ncontent += content;
                }
                if(cnt == COUNT) break;
                cnt ++;
                if(pcontent.length() > 100000) {
                    processContent(pcontent, 1);
                    pcontent = "";
                }
                if(ncontent.length() > 100000) {
                    processContent(ncontent, 0);
                    ncontent = "";
                }
            }
            processContent(pcontent, 1);
            processContent(ncontent, 0);
            fileWriter.write("" + RubbishMail + " " + NormalMail);
            fileWriter.flush();
            Iterator<String> it = dic.iterator();
            System.out.println(dic.size());
            while(it.hasNext()) {
                String word = it.next();
                fileWriter.write(" " + word + " " + wordCountPositive.get(word) + " " + wordCountNegative.get(word));
                fileWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {
//        run();
//        getModel();
//        prediction();
    }

    public static double getAnswer(String content) {
        double zi = 1, mu1 = 1, mu2 = 1, ret = 0;
        try {
            CWSTagger var1 = new CWSTagger("D:\\baiduyun\\Workspaces\\MyEclipse 10\\workspace\\nlpHomework\\web\\WEB-INF\\models\\seg.m");
            ArrayList<String> words = var1.tag2List(content);
            if(words == null) return -1;
            int cnt = 0;
            double py = RubbishMail / (RubbishMail + NormalMail);
            for(String word : words) {
                if(dic.contains(word) == false) continue;
                cnt ++;
                zi *= (double)wordCountPositive.get(word) / RubbishMail;
                mu1 *= (double)wordCountPositive.get(word) / RubbishMail;
                mu2 *= (double)wordCountNegative.get(word) / NormalMail;
            }
            if(cnt == 0) return -1;
            ret = (zi*py + 1) / (mu1*py + mu2*(1-py) + cnt);
        } catch (LoadModelException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void readModel() {
        try {
            Scanner in = new Scanner(new File("model.txt"));
            RubbishMail = in.nextInt(); NormalMail = in.nextInt();
            while(in.hasNext()) {
                String key = in.next();
                dic.add(key);
                wordCountPositive.put(key, in.nextInt());
                wordCountNegative.put(key, in.nextInt());
            }
            in.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void prediction() {
        readModel();
        try {
            String path = "NaiveBayesian\\trec06c\\", pcontent = "", ncontent = "";
            Scanner in = new Scanner(new File(path + "full\\index"));
            int cnt = 0;
            while(in.hasNext()) {
                String[] sample = in.nextLine().split(" ");
                sample[1] = sample[1].substring(3);
                System.out.println(path + sample[1]);
                String content = getAMail(path + sample[1]);
                double ans = getAnswer(content);
                System.out.println(sample[0] + " " + ans);
                if(cnt ++ == COUNT) break;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
