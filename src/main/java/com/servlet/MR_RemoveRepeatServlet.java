package com.servlet; /**
 * @Author wuxin
 * @Date 2023/6/29 09:09
 * @Description ${description}
 * @Version ${version}
 */

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.beans.WordCountInfo;
import net.sf.json.JSONArray;
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


@WebServlet(name = "MR_RemoveRepeatServlet", value = "/MR_RemoveRepeatServlet")
public class MR_RemoveRepeatServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response)  {
        try {
            Configuration conf = new Configuration();
            conf.set("fs:defaultFS", "hdfs://10.90.6.168:8020");

            Job job = Job.getInstance(conf);

            // 设定map 相关的配置
            job.setMapperClass(RepeatMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);

            //指明要处理的文件
            String filePath = request.getParameter("filePath");
            Path path=new Path("hdfs://10.90.6.168:8020/" + filePath);
            FileInputFormat.setInputPaths(job,path);

            // 设定 reduce 相关的配置
            job.setReducerClass(RepeatReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            // 指明计算完成以后,输出结果放在哪里
            Path path2 =new Path("hdfs://10.90.6.168:8020/testing/removerepeatresult");
            FileOutputFormat.setOutputPath(job, path2);

            // 因为如果目标目录存在,将出错,所以可以先将目标删除
            URI uri = new URI("hdfs://10.90.6.168:8020");
            FileSystem fs = FileSystem.get(uri, conf);
            fs.delete( new Path("hdfs://10.90.6.168:8020/testing/removerepeatresult") ,true);

            // 提交作业
            job.waitForCompletion(true);

            //提取出分析完毕之后结果,传给前台
            Path path3 = new Path("hdfs://10.90.6.168:8020/testing/removerepeatresult/part-r-00000");
            FSDataInputStream fsInput = fs.open(path3);

            BufferedReader br = new BufferedReader(new InputStreamReader(fsInput, "utf-8"));

            List<String> wordList = new ArrayList<>();
            String str = null;

            while ((str = br.readLine()) != null) {
                System.out.println(str);
                wordList.add(str);
            }

            //把wordList转成json格式
            JSONArray jsonobj=JSONArray.fromObject(wordList);

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().print(jsonobj);

            br.close();
            fsInput.close();
            fs.close();

        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    static class RepeatMapper extends Mapper<Object, Text, Text, Text> {
        Text txt = new Text();

        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] wordList = line.split(" ");

            for (String word : wordList) {
                context.write(new Text(word), txt); // 因为我们并不心这个txt,所以它是空的也可以
            }
        }
    }

    static class RepeatReducer extends Reducer<Text, Text, Text, Text> {
        Text txt = new Text();

        protected void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            context.write(key, txt);
        }
    }
}
