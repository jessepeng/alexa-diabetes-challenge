git pull
mvn assembly:assembly -DdescriptorId=jar-with-dependencies package
mv target/alexa-dosage-calculator.war /var/lib/tomcat7/webapps/alexa-dosage-calculator.war