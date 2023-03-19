Rock Paper Scissors Telegram Bot
==================================

## What is it for?
This bot allows the user to play rock paper scissors with the computer. Each user keeps statistics for his games played, as well as wins and losses.

## Usage
1. Build the jar file with gradle.
2. Create a new folder, where you place the created jar file and [application.properties](#applicationproperties-file-configuration).
3. Run the jar file from the console. The generated .csv file will be located in this directory.

## application.properties file configuration

rpsbot.name=  
rpsbot.token=  
spring.datasource.url=  
spring.datasource.driverClassName=  
spring.datasource.username=  
spring.datasource.password=  
spring.jpa.database-platform=  
spring.jpa.hibernate.ddl-auto=update  
spring.h2.console.enabled=true  
logging.file.path=  