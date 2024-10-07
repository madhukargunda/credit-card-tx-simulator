# Create the topic
#! /bin/bash
kafka-topics.sh --create --topic credit-card-transactions --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092


