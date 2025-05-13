docker exec -it kafka bash


# Kafka broker address (SASL_SSL listener)
BOOTSTRAP_SERVER="localhost:9093"

# Define principals
PRODUCER_USER="User:producer"
CONSUMER_USER="User:consumer"

echo "Adding ACLs for PRODUCER ($PRODUCER_USER)..."
sh kafka-acls.sh --bootstrap-server $BOOTSTRAP_SERVER \
  --command-config  /opt/bitnami/kafka/config/client.properties \
  --add \
  --allow-principal "$PRODUCER_USER" \
  --operation Write \
  --operation Create \
  --topic '*' \


echo "Adding ACLs for CONSUMER ($CONSUMER_USER)..."
sh kafka-acls.sh --bootstrap-server $BOOTSTRAP_SERVER \
  --command-config  /opt/bitnami/kafka/config/client.properties \
  --add \
  --allow-principal "$CONSUMER_USER" \
  --operation Read \
  --operation Describe \
  --topic '*' \
  --group '*'

# Allow all operations on all topics
sh kafka-acls.sh --bootstrap-server $BOOTSTRAP_SERVER \
  --command-config  /opt/bitnami/kafka/config/client.properties \
  --add \
  --allow-principal User:admin \
  --operation All \
  --topic '*'

# Allow all operations on all consumer groups
sh kafka-acls.sh --bootstrap-server $BOOTSTRAP_SERVER \
  --command-config  /opt/bitnami/kafka/config/client.properties \
  --add \
  --allow-principal User:admin \
  --operation All \
  --group '*'

# Allow all operations on the Kafka cluster
sh kafka-acls.sh --bootstrap-server $BOOTSTRAP_SERVER \
  --command-config  /opt/bitnami/kafka/config/client.properties \
  --add \
  --allow-principal User:admin \
  --operation All \
  --cluster

# Allow all operations on all transactional IDs
sh kafka-acls.sh --bootstrap-server $BOOTSTRAP_SERVER \
  --command-config  /opt/bitnami/kafka/config/client.properties \
  --add \
  --allow-principal User:admin \
  --operation All \
  --transactional-id '*'

# Allow all operations on all topics for idempotent writes (optional but useful)
sh kafka-acls.sh --bootstrap-server $BOOTSTRAP_SERVER \
  --command-config  /opt/bitnami/kafka/config/client.properties \
  --add \
  --allow-principal User:admin \
  --operation IdempotentWrite \
  --topic '*'

sh kafka-acls.sh --bootstrap-server $BOOTSTRAP_SERVER \
  --command-config  /opt/bitnami/kafka/config/client.properties \
  --add \
  --allow-principal User:producer \
  --operation Create \
  --operation Write \
  --topic '*'

sh kafka-acls.sh --bootstrap-server $BOOTSTRAP_SERVER \
  --command-config  /opt/bitnami/kafka/config/client.properties \
  --add \
  --allow-principal User:consumer \
  --operation Read \
  --topic test-kafka-topic

sh kafka-acls.sh --bootstrap-server $BOOTSTRAP_SERVER \
  --command-config  /opt/bitnami/kafka/config/client.properties \
  --add \
  --allow-principal User:consumer \
  --operation Read \
  --group test-kafka-topic

sh kafka-acls.sh --bootstrap-server $BOOTSTRAP_SERVER \
  --command-config  /opt/bitnami/kafka/config/client.properties \
  --add \
  --allow-principal User:producer \
  --operation IdempotentWrite \
  --cluster

sh kafka-acls.sh --bootstrap-server $BOOTSTRAP_SERVER \
  --command-config /opt/bitnami/kafka/config/client.properties \
  --add \
  --allow-principal User:producer \
  --operation Write \
  --operation Create \
  --topic '*'

sh kafka-acls.sh --bootstrap-server $BOOTSTRAP_SERVER  \
  --command-config /opt/bitnami/kafka/config/client.properties \
  --add \
  --allow-principal User:admin \
  --operation IdempotentWrite \
  --cluster

sh  kafka-acls.sh \
     --bootstrap-server $BOOTSTRAP_SERVER \
     --command-config /opt/bitnami/kafka/config/client.properties \
     --add \
     --allow-principal User:admin \
     --operation All \
     --cluster



