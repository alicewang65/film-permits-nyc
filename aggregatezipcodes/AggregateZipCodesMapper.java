import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class AggregateZipCodesMapper extends Mapper<Object, Text, Text, IntWritable> {
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // need to get the zipcodes

        // convert value to array
        // regex from AIT class (taught by Professor Versosa), which was sourced from this SO post:
        // https://stackoverflow.com/a/49670696
        String[] fields = (value.toString()).split(",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))");

        // zipcodes are the 4th index, type of filming is the 3rd index

        // get the zipcodes
        String[] zipcodes = (fields[4]).split(",");

        for (String zip : zipcodes) {
            // some zipcodes might have a leading or trailing " or space that 
            // needs to be removed
            context.write(new Text((zip.replace("\"", "")).trim() + " " + fields[3]), new IntWritable(1));
        }
    }
}
