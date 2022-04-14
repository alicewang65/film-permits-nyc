import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class AggregateZipCodes {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: CountNeighborhoods <input path> <output path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
	    Job job = Job.getInstance(conf, "Count Lines");
        job.setJarByClass(AggregateZipCodes.class);
        job.setNumReduceTasks(1); // 1 Reduce task

        FileInputFormat.addInputPath(job, new Path(args[0])); 
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        job.setMapperClass(AggregateZipCodesMapper.class);
        job.setReducerClass(AggregateZipCodesReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        System.exit(job.waitForCompletion(true) ? 0 : 1); 
    }
}
