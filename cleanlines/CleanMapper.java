import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class CleanMapper extends Mapper<Object, Text, Text, Text> {
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // convert value to array
        // regex from AIT class (taught by Professor Versosa), which was sourced from this SO post:
        // https://stackoverflow.com/a/49670696
        String[] fields = (value.toString()).split(",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))");

        // want to keep EventType (1), StartDateTime (2), EndDateTime (3), 
        // Borough (7), Category (10), SubCategoryName (11), ZipCode (13)
        String eventType = "";
        String year = "";
        String bor = "";
        String category = "";
        String zip = "";

        // check for eventType
        if (!(fields[1].isEmpty())) {
            eventType = fields[1];
        }
        // check for year
        if (!(fields[2].isEmpty())) {
            if ((fields[2].substring(8, 10)).equals("18")) {
                year = "2018";
            }
        }
        if (!(fields[3].isEmpty())) {
            if ((fields[3].substring(8, 10)).equals("18")) {
                year = "2018";
            }
        }
        // check for borough
        if (fields[7].equals("Manhattan")) {
            bor = "Manhattan";
        }
        // check for category
        if (!(fields[10].isEmpty())) {
            category = fields[10];

            if (!(fields[11].isEmpty())) {
                if (!(fields[11].equals("Not Applicable"))) {
                    category += " " + fields[11];
                }
            }
        }
        // check for zip
        if (!(fields[13].isEmpty()) && !fields[13].equals("N/A")) {
            zip = fields[13];
        }

        if (eventType.isEmpty() || year.isEmpty() || 
                bor.isEmpty() || category.isEmpty() || zip.isEmpty()) {
            return;
        } else {
            String val = eventType + "," + year + "," + bor + "," + category + "," + zip;

            context.write(new Text(), new Text(val));
        }
    }
}