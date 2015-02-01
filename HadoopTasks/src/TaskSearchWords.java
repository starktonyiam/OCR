/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TaskSearchWords {

    String rawOCRClob = "";

    public String getRawOCRClob() {
        return rawOCRClob;
    }

    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {
            Configuration configuration = context.getConfiguration();
            String rawOCRClob = configuration.get("RAWOCRCLOB").toLowerCase();

            String[] tokens = value.toString().split(",");
            for (String token : tokens) {
                if (rawOCRClob.contains(token.toLowerCase())) {
                    word.set(token);
                    context.write(word, one);
                }
            }
        }
    }

    public static class IntSumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {

        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {

        String hadoopServer = "ip-172-31-13-245.ap-southeast-1.compute.internal";

        Configuration conf = new Configuration();

        // this should be like defined in your mapred-site.xml
        conf.set("mapred.job.tracker", hadoopServer + ":54311");

        // like defined in hdfs-site.xml
        conf.set("fs.default.name", "hdfs://" + hadoopServer + ":9000");

        //setting mapred classes for HDFS to know which classes to process
        conf.set("mapreduce.map.class", "TokenizerMapper");
        conf.set("mapreduce.reduce.class", "IntSumReducer");

        //to prevent classdefnotfound exception
        conf.set("mapred.jar", "C:\\GitRepos\\OCR\\HadoopTasks\\dist\\HadoopTasks.jar");

        //to pass parameters to mapred classes
        conf.set("RAWOCRCLOB", "Omeprazole_Cap E/C 10mg\n" +
"Dressit Ster esDress\n" +
"Flaminal Forte 15g\n" +
"Co-Magaldrox_Susp 195mg/220mg/5ml S/F\n" +
"Antacid/Oxetacaine_Oral Susp S/F\n" +
"Simeticone_Susp 40mg/ml S/F\n" +
"Infacol_Susp 40mg/ml S/F");

        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(TaskSearchWords.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path("/user/ubuntu/MedicinesProcessed.csv"));
        FileSystem fs = FileSystem.get(conf);
        Path out = new Path("/user/ubuntu/processed/");
        fs.delete(out, true);

        //finally set the empty out path
        FileOutputFormat.setOutputPath(job, out);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
