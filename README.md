# vttp5_paf_2024_assessment

SHOW VARIABLES LIKE 'secure_file_priv';
If the result is NULL, LOAD DATA INFILE is completely disabled.
If the result shows a directory path (e.g., /var/lib/mysql-files/), you must place the CSV file in this directory.


-- load data infile '/Users/shamus/Documents/VTTP/ssjh96/PAF Assessment 2024/paf_assessment_template/data/users.csv' into table users
-- fields terminated by ','
-- optionally enclosed by '"'
-- lines terminated by '\n'
-- ignore 1 rows;
