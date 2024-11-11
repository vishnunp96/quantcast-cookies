Application to count most active cookies for a specific day from given logfile

Args:
- "-f" - log file in csv, with headers cookie and timestamp
- "-d" - date for which active cookies have to be listed, in yyyy-mm-dd format


> mvn clean package
> 
> java -jar target/cookies-1.0.0-SNAPSHOT.jar -f src/test/resources/cookie_log_test.csv -d 2018-12-09
