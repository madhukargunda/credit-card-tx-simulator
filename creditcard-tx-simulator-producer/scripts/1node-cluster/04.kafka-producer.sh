
#! /bin/bash
# Producer producing the csv file to kafka cluster
# kafka-console-producer.sh --topic test --broker-list localhost:9092 < ../csv/sample.csv

#kafka-console-producer.sh --topic words-stream --broker-list localhost:9092 < ../../data/sentences.dat

kafka-console-producer.sh --topic greetings --broker-list localhost:9092 < ../../data/sentences.dat
