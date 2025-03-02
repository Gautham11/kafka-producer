#!/bin/bash


# Exit immediately if a command exits with a non-zero status
set -e

# Start Docker Compose
echo "Starting Docker Compose..."
docker-compose up -d

# Wait for Kafka to be ready
echo "Waiting for Kafka to be ready..."
sleep 10  # Adjust based on startup time

# Create Kafka topic
TOPIC_NAME="test-kafka-topic"
KAFKA_BROKER="localhost:9092"
echo "Creating Kafka topic: $TOPIC_NAME..."
sh ~/Downloads/kafka_2.12-3.9.0/bin/kafka-topics.sh --bootstrap-server "localhost:9092" \
         --create \
         --topic "$TOPIC_NAME" \
         --partitions "1" \
         --replication-factor "1" \


# Wait for Vault to be ready
echo "Waiting for Vault to be ready..."
sleep 5  # Adjust based on startup time

# Export Vault address
export VAULT_ADDR='http://127.0.0.1:8200'
VAULT_TOKEN="myroot"  # Change this to match your Vault token setup

# Enable Transit Secrets Engine
echo "Enabling Vault Transit secrets engine..."
vault login $VAULT_TOKEN
vault secrets enable transit

# Create a transit encryption key
TRANSIT_KEY_NAME="encrypt-key"
echo "Creating Vault transit key: $TRANSIT_KEY_NAME..."
vault write -f transit/keys/$TRANSIT_KEY_NAME

echo "Setup complete!"
