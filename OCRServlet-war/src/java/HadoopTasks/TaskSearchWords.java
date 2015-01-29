/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HadoopTasks;

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

    public static class TokenizerMapper
	    extends Mapper<Object, Text, Text, IntWritable> {

	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();

	public void map(Object key, Text value, Context context
	) throws IOException, InterruptedException {
	    StringTokenizer itr = new StringTokenizer(value.toString());
	    while (itr.hasMoreTokens()) {
		word.set(itr.nextToken());
		context.write(word, one);
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
	//fs.delete(out, true);
	// finally set the empty out path
	FileOutputFormat.setOutputPath(job, out);
	System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
