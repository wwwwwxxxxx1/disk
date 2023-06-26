package com.mapreduce;
import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
/**
 * @Author wuxin
 * @Date 2023/6/26 11:12
 * @Description
 * @Version
 */
public class WordCountTest {
    public static void main(String[] args) throws Exception {
        //创建一个作业
        Job  job=Job.getInstance();

        //指定main函数所在的类
        job.setJarByClass(WordCountTest.class);

        //设定map 相关的配置
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //设定输入文件的路径
        String path="hdfs://10.90.6.168:8020/testing/book.txt";
        FileInputFormat.setInputPaths(job,new Path(path));

        //设定reduce 相关的配置
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //指明计算完成以后,输出的结果放在哪里
        String resultPath="hdfs://10.90.6.168:8020/testing/book-result";
        FileOutputFormat.setOutputPath(job, new Path(resultPath));

        job.waitForCompletion(true); //true表示在作业执行的时候输出提示信息

        System.out.println("程序运行结束");
    }

    static class MyMapper extends Mapper<LongWritable,Text,Text,LongWritable>{
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            //读入字符串
            String line=value.toString();
            String [] wordList=line.split(" ");

            for(String word:wordList) {
                context.write(new Text(word), new LongWritable(1));
            }
        }
    }

    static class MyReducer extends Reducer<Text,LongWritable,Text,LongWritable>{
        LongWritable n=new LongWritable();

        protected void reduce(Text key, Iterable<LongWritable> values,
                              Context context) throws IOException, InterruptedException {
            int count=0;
            for(LongWritable i:values) {
                count+=i.get();
            }

            n.set(count);
            context.write(key,n );
        }
    }

}
