package com.servlet; /**
 * @Author wuxin
 * @Date 2023/6/29 13:51
 * @Description ${description}
 * @Version ${version}
 */

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.beans.ScoreInfo;
import com.mapreduce.SortPartitionTest;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

@WebServlet(name = "MR_PartSortServlet", value = "/MR_PartSortServlet")
public class MR_PartSortServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 创建一个作业
            Job job = Job.getInstance();

            // 设定map 相关的配置
            job.setMapperClass(TotalMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(ScoreInfo.class);

            //指定要操作哪个文件
            String filePath = request.getParameter("filePath");
            FileInputFormat.setInputPaths(job, new Path("hdfs://10.90.6.168:8020/" + filePath));

            // 设定 reduce 相关的配置
            job.setReducerClass(TotalReduce.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(ScoreInfo.class);

            // 因为如果目标目录存在,将出错,所以可以先将目标删除
            URI uri = new URI("hdfs://10.90.6.168:8020");
            Configuration conf = new Configuration();

            FileSystem fs = FileSystem.get(uri, conf);
            fs.delete(new Path("hdfs://10.90.6.168:8020/testing/partsortgroupresult"), true);

            // 指明计算完成以后,输出结果放在哪里
            FileOutputFormat.setOutputPath(job, new Path("hdfs://10.90.6.168:8020/testing/partsortgroupresult"));

            // 提交作业
            job.waitForCompletion(true);

            //上面是完成了reduce,下面是再进行一次map目的是排序
            Job job2 = Job.getInstance();

            // 设定map 相关的配置
            job2.setMapperClass(SortMapper.class);
            job2.setMapOutputKeyClass(ScoreInfo.class);
            job2.setMapOutputValueClass(Text.class);

            //读入上一步生成的文件
            FileInputFormat.setInputPaths(job2, new Path("hdfs://10.90.6.168:8020/testing/partsortgroupresult/part-r-00000"));

            job2.setOutputKeyClass(ScoreInfo.class);
            job2.setOutputValueClass(Text.class);
            fs.delete(new Path("hdfs://10.90.6.168:8020/testing/partsortresult"), true);
            FileOutputFormat.setOutputPath(job2, new Path("hdfs://10.90.6.168:8020/testing/partsortresult"));

            //设置开启分区
            job2.setNumReduceTasks(5);
            job2.setPartitionerClass(MyPartition.class);

            boolean result=job2.waitForCompletion(true);

            System.out.println(result?"处理成功":"处理失败");

            //把分区过的数据,读出来,传到页面
            FSDataInputStream fsInput=null;

            List<List<ScoreInfo>> dataList=new ArrayList<>();

            for(int i=0;i<5;i++) {
                Path path=new Path("hdfs://10.90.6.168:8020/testing/partsortresult/part-r-0000"+i);
                fsInput=fs.open(path);

                BufferedReader br =new BufferedReader(new InputStreamReader(fsInput,"utf-8"));
                List<ScoreInfo> scoreInfoList=new ArrayList<>();

                String str=null;
                while((str=br.readLine())!=null) {
                    System.out.println(str);  //广州	风云	28	女	fengyun
                    String [] data =str.split("\t");
                    String address=data[0];
                    String name=data[1];
                    Integer score=Integer.parseInt(data[2]);
                    String gender=data[3];
                    String idCard=data[4];

                    ScoreInfo info =new ScoreInfo();
                    info.setAddress(address);
                    info.setName(name);
                    info.setScore(score);
                    info.setGender(gender);
                    info.setIdCard(idCard);

                    scoreInfoList.add(info);
                }

                br.close();
                fsInput.close();

                dataList.add(scoreInfoList);
            }

            fs.close();
            request.setAttribute("dataList", dataList);
            request.getRequestDispatcher("/mapreduce/partsort-result.jsp").forward(request, response);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static class TotalMapper extends Mapper<LongWritable,Text,Text,ScoreInfo>{
        Text k2=new Text();

        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            //cuijing	上海	崔精	 64	女
            String line=value.toString();
            String [] data =line.split("\t");

            ScoreInfo info=new ScoreInfo();
            info.setIdCard(data[0]);
            info.setAddress(data[1]);
            info.setName(data[2]);
            info.setScore(Integer.parseInt(data[3]));
            info.setGender(data[4]);


            k2.set(info.getIdCard());
            context.write(k2, info);
        }
    }

    public static class TotalReduce extends Reducer<Text,ScoreInfo,Text,ScoreInfo >{
        int totalScore=0;
        int i=0;
        ScoreInfo info;
        protected void reduce(Text key, Iterable<ScoreInfo> values,
                              Context context) throws IOException, InterruptedException {
            for(ScoreInfo o:values) {
                //其为同一组的中的数据,除了分数外,其他的基本信息都是相同的,所以取第一个数据的信息就可以
                if(i==0) {
                    info=o;
                }
                totalScore+=o.getScore();
                i++;
            }

            info.setScore(totalScore);

            context.write(key, info);

            totalScore=0;
            i=0;
        }
    }

    public static class SortMapper extends Mapper<LongWritable,Text,ScoreInfo,Text>{

        ScoreInfo info=new ScoreInfo();
        Text k2=new Text();

        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            //zhongjinhui	韩国	仲瑾惠	77	女
            String line=value.toString();
            String [] data=line.split("\t");

            info.setIdCard(data[0]);
            info.setAddress(data[1]);
            info.setName(data[2]);
            info.setScore(Integer.parseInt(data[3]));
            info.setGender(data[4]);
            k2.set(info.getIdCard());

            context.write(info, k2);    //zhongjinhui	韩国	仲瑾惠	77	女   zhongjinhui
        }

    }

    public static class MyPartition extends Partitioner<ScoreInfo,Text>{
        public int getPartition(ScoreInfo key , Text value , int numPartitions) {
            String addr=key.getAddress();

            if(addr.equals("北京")) {
                return 0;
            }
            if(addr.equals("上海")) {
                return 1;
            }
            if(addr.equals("广州")){
                return 2;
            }
            if(addr.equals("深圳")) {
                return 3;
            }
            else {
                return 4;
            }
        }
    }
}
