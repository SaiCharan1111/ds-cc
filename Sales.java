package org.myorg;

import java.io.IOException;
import java.util.regex.Pattern;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;



public class Sales extends Configured implements Tool {

 public static void main(String[] args) throws Exception {
 int res = ToolRunner.run(new Sales(), args);
 System.exit(res);
 }
 public int run(String[] args) throws Exception {
 Job job = Job.getInstance(getConf(), "sales");
 job.setJarByClass(this.getClass());
 // Use TextInputFormat, the default unless job.setInputFormatClass is used
 FileInputFormat.addInputPath(job, new Path(args[0]));
 FileOutputFormat.setOutputPath(job, new Path(args[1]));
 job.setMapperClass(Map.class);
 job.setReducerClass(Reduce.class);
 job.setOutputKeyClass(Text.class);
 job.setOutputValueClass(IntWritable.class);
 return job.waitForCompletion(true) ? 0 : 1;
 }

 

public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
	private final static IntWritable one = new IntWritable(1);

	public void map(LongWritable key, Text value, Context context) throws IOException,InterruptedException {

		String valueString = value.toString();
		String[] SingleCountryData = valueString.split(",");
		
		context.write(new Text(SingleCountryData[3]),one);
	
		
	}
}


public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

	public void reduce(Text t_key, Iterable<IntWritable> values,Context context) throws IOException,InterruptedException {
		Text key = t_key;
		
		int frequencyForCountry = 0;
		 for (IntWritable val : values) 
{
               frequencyForCountry += val.get();
           }
		
		context.write(key, new IntWritable(frequencyForCountry));
	}
}
}
