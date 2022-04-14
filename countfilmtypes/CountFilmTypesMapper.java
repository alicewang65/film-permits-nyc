import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class CountFilmTypesMapper extends Mapper<Object, Text, Text, IntWritable> {
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // need to get the zipcodes

        // convert value to array
        // regex from AIT class (taught by Professor Versosa), which was sourced from this SO post:
        // https://stackoverflow.com/a/49670696
        String[] fields = (value.toString()).split(",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))");

        // type of filming is the 3rd index

        context.write(new Text(fields[3].trim()), new IntWritable(1));
    }
}
