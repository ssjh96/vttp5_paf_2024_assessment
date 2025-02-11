# vttp5_paf_2024_assessment

SHOW VARIABLES LIKE 'secure_file_priv';
If the result is NULL, LOAD DATA INFILE is completely disabled.
If the result shows a directory path (e.g., /var/lib/mysql-files/), you must place the CSV file in this directory.


-- load data infile '/Users/shamus/Documents/VTTP/ssjh96/PAF Assessment 2024/paf_assessment_template/data/users.csv' into table users
-- fields terminated by ','
-- optionally enclosed by '"'
-- lines terminated by '\n'
-- ignore 1 rows;

MYSQL 
ROOT
mysql -u root -p | source task1.sql;

Mongodb
mongoimport -d bedandbreakfast -c listings_and_reviews --type=json --file=listings.json

using mongosh
terminal
mongosh
show dbs;
use bedandbreakfast;
show collections;
db.<colletionName>.findOne();

To do
1. create railway mysql > create fred > source task1.sql to create db w 3 tables
2. create railway mongo > run task2.1 - 2.5 cmds > run java -jar csv2sql.jar reviews.csv reviews.sql
3. source reviews.sql to insert data into mysql's 'reviews' table
