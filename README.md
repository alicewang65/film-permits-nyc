# Film Permits in NYC

## Introduction

This project/code leverages Hadoop MapReduce to analyze the data provided by NYC OpenData in their [Film Permit](https://data.cityofnewyork.us/City-Government/Film-Permits/tg4x-b46p) data set, which provides information about film permit requests received. In particular, the code here is used in conjunction with Megan Chen's analysis on NYC OpenData's [Property Valuation and Assessment Data](https://data.cityofnewyork.us/City-Government/Property-Valuation-and-Assessment-Data/yjxr-fw8i). Check our her work [here](https://github.com/megalo999/property-valuations-nyc). The goal of combining information from these two data sets is to see if there is a correlation between number of film permits in a given zip code and the average property valuation. This project looks specifically at Manhattan in 2018.

## Importance

Settings and environment are significant parts of movies, TV shows and commercials. So, by examining the locations, specifically the neighborhoods, that these films are shot in can give insight to the genres of the film. In particular, if we find that there is more film shooting in wealthier neighborhoods (i.e. neighborhoods with a higher average property valuation), then that suggests a lack of diversity in the content of these films. This information can encourage viewers and others in the film industry to consider diversity from a different, analytical perspective. Furthermore, oftentimes, shoot locations become tourist attractions after the work has been released, impacting the neighborhoods in unforeseen ways, including congestion, income, etc. So, this analytic can help content creators to be more intentional when selecting locations to place their work, particularly when considering the ripple effects of their work.


## Code Structure

### Data Source
* [filmpermits.csv](./filmpermits.csv) - original data source with film permit information
* [filmpermits_clean.csv](./filmpermits_clean.csv) - cleaned data source

### Cleaning the Data
* [cleanlines](./cleanlines) - the files (java, class and jar) in this folder are specifically for cleaning the original data set
    * note: there's no reducer class since it wasn't necessary for cleaning the data
    * input: original data set => [filmpermits.csv](./filmpermits.csv)
    * outputs: a cleaned data set that includes only film permits located in Manhattan during 2018; each line includes the event type (load in/out, shooting, DCAS prep/shoot/wrap, rigging), year (2018), borough (Manhattan), category (both main and subcategory describing type of production) and zip code
    * result: [filmpermits_clean.csv](./filmpermits_clean.csv)

### Analyzing the Data
* [countlines](./countlines) - the files (java, class and jar) in this folder are for counting the number of lines in the data set
    * inputs: original data set => [filmpermits.csv](./filmpermits.csv), cleaned data set => [filmpermits_clean.csv](./filmpermits_clean.csv)
    * outputs: number of lines in a given input file
    * result (original data set): [originalcount](./output/originalcount) => 74453
    * result (cleaned data set): [cleancount](./output/cleancount) => 4119
* [countzipcodes](./countzipcodes) - the files (java, class and jar) in this folder count the number of film permits per zip code of the cleaned data set
    * input: cleaned data set => [filmpermits_clean.csv](./filmpermits_clean.csv)
    * outputs: number of film permits per zip code of the cleaned data set
    * result: [zipoutput](./output/zipoutput)
* [countfilmtypes](./countfilmtypes) - the files (java, class and jar) in this folder provide a count of the number of different types of film permits requested in Manhattan in 2018
    * note: this count breaks the types of categories into their provided subcategories
    * input: cleaned data set => [filmpermits_clean.csv](./filmpermits_clean.csv)
    * outputs: number of film permits per subcategory
    * result: [filmoutput](./output/filmoutput)
* [aggregatezipcodes](./aggregatezipcodes) - the files (java, class and jar) in this folder count the number of film permits based on both zipcode and subcategory
    * input: cleaned data set => [filmpermits_clean.csv](./filmpermits_clean.csv)
    * outputs: number of film permits per subcategory and zip code combination
    * result: [aggregateoutput](./output/aggregateoutput)

## Replicating Results

Note: This process assumes knowledge of compiling and running Hadoop MapReduce jobs. All necessary java, class, jar and input files are provided in this repo.

1. Starting with the original data set ([filmpermits.csv](./filmpermits.csv)), run [clean.jar](./cleanlines/clean.jar) in order to output the cleaned data set.
    1. It should match the results in [filmpermits_clean.csv](./filmpermits_clean.csv)
2. Using the cleaned data set ([filmpermits_clean.csv](./filmpermits_clean.csv)) you can run [countZipCodes.jar](./countzipcodes/countZipCodes.jar) to generate the count of film permits per zip code.
    1. It should match the results in [zipoutput](./output/zipoutput)
3. Using the cleaned data set ([filmpermits_clean.csv](./filmpermits_clean.csv)) you can run [countFilmTypes.jar](./countfilmtypes/countFilmTypes.jar) to generate the number of film permits for each subcategory of production type.
    1. It should match the results in [filmoutput](./output/filmoutput)
4. Using the cleaned data set ([filmpermits_clean.csv](./filmpermits_clean.csv)) you can run [aggregateZipCodes.jar](./aggregatezipcodes/aggregateZipCodes.jar) to generate the count of film permits for each combination of zip code and subcategory of production type. 
    1. It should match the results in [aggregateoutput](./output/aggregateoutput)
5. If you would like to reproduce the results of counting the number of lines in the original and cleaned data sets, follow the steps below:
    1. Starting with the original data set([filmpermits.csv](./filmpermits.csv)), run [countRecs.jar](./countlines/countRecs.jar) to generate the count of lines in the data set.
        1. It should match the results/count in [originalcount](./output/originalcount)
    2. Next, clean the original data set ([filmpermits.csv](./filmpermits.csv)) by running [clean.jar](./cleanlines/clean.jar).
        1. Your output should match the results in [filmpermits_clean.csv](./filmpermits_clean.csv)
    3. Finally, count the number of records in the cleaned data set by again running [countRecs.jar](./countlines/countRecs.jar).
        1. Your output should match the results in [cleancount](./output/cleancount)
6. To combine both the film permit and average building valuation results, follow the steps below:
    1. Make sure the zip code count of film permits is loaded in HDFS. In other words, make sure [zipoutput](./output/zipoutput) exists in HDFS.
    2. Make sure the average valuation of buildings by zip codes is loaded in HDFS. The file should look something like [avgvaluations](./avgvaluations). Note: this file was generated by Megan, refer to her [repo](https://github.com/megalo999/property-valuations-nyc) for more details about how she generated this particular analysis. 
    3. Follow the commands in [hivecommands.txt](./hive/hivecommands.txt) to load the data into Hive and execute the appropriate join queries.
    4. After executing step 6 of the Hive commands, your output should look like [combined_hdfs.csv](./hive/combined_hdfs.csv)
    5. After executing step 7 of the Hive commands, your output should look like [combined_hlog.csv](./hive/combined_hlog.csv)
