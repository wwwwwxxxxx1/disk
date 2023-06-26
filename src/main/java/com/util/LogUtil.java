package com.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author wuxin
 * @Date 2023/6/26 09:18
 * @Description
 * @Version
 */
public class LogUtil {
    public static void log(String s)  {
        //后面的 true 表示以追加的方式写入
        BufferedWriter bw=null;
        try {
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            String fileName=df.format(new Date())+".log";  //2023-6-25
            FileWriter fw=new FileWriter("/Users/wuxin/Desktop/Linda_disk/logs/"+fileName,true);

            //包装成缓冲流
            bw=new BufferedWriter(fw);

            //写出日志到文件中
            bw.write(s);
            //写一个换行符
            bw.newLine();
            bw.flush();

        }catch(IOException ex) {
            ex.printStackTrace();
        }finally {
            if(bw!=null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
