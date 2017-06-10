sudo -H -u jessepeng git pull
sudo -H -u jessepeng mvn assembly:assembly -DdescriptorId=jar-with-dependencies package
mv target/alexa-dosage-calculator.war /var/lib/tomcat7/webapps/alexa-dosage-calculator.war