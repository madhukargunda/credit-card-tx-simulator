
# Credit Card Transaction Consumer Application

![Kafka Streams Consumer Architecture](file-9kC1dCxCCvKrh662qLZw6YFT)

## Overview of the Application

The consumer application subscribes to a Kafka topic and uses a polling mechanism to retrieve records. It processes credit card transactions using Kafka Streams, a powerful streaming engine that reads and processes data in real-time. The processed data is then stored in RocksDB, which Kafka Streams uses by default to manage the application's state.

- **Kafka Topic**: Source of credit card transaction data
- **Spring Boot Consumer**: Handles subscriptions and record polling
- **Kafka Streams Processing**: Real-time data processing engine
- **RocksDB**: State store for Kafka Streams
