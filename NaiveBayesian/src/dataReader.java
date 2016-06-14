import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Administrator on 2016/6/14 0014.
 */
public class dataReader {
    public ArrayList<String> RubbishMails;
    public ArrayList<String> NormalMails;
    public int rubbishCount;
    public int normalCount;
    public static int COUNT = 20;

    dataReader(int _COUNT) {
        rubbishCount = normalCount = 0;
        RubbishMails = new ArrayList<>();
        NormalMails = new ArrayList<>();
        COUNT = _COUNT;
    }

    public String getAMail(String path) {
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

    public void getMails() {
        try {
            String path = "NaiveBayesian\\trec06c\\", pcontent = "", ncontent = "";
            Scanner in = new Scanner(new File(path + "full\\index"));
            int cnt = 0;
            while(in.hasNext()) {
                String[] sample = in.nextLine().split(" ");
                sample[1] = sample[1].substring(3);
                System.out.println(path + sample[1]);
                String content = getAMail(path + sample[1]);
                if(sample[0].equals("spam")) {
                    RubbishMails.add(content);
                    rubbishCount ++;
                } else {
                    NormalMails.add(content);
                    normalCount ++;
                }
                if(cnt ++ == COUNT) break;
            }
            System.out.println("-------------- read data over -------------");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
