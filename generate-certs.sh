#!/bin/bash
set -e

# Set passwords
PASSWORD=changeit

# Output dir
mkdir -p secrets
cd secrets

### Step 1: Create a CA (self-signed)
openssl req -new -x509 -keyout ca-key.pem -out ca-cert.pem -days 365 \
  -subj "/CN=MyKafkaCA" -nodes

### Step 2: Generate Keystore and CSR for Kafka
keytool -genkeypair -alias kafka \
  -keyalg RSA -keysize 2048 -validity 365 \
  -dname "CN=kafka" \
  -keystore kafka.keystore.jks \
  -storepass $PASSWORD -keypass $PASSWORD

keytool -certreq -alias kafka \
  -file kafka.csr \
  -keystore kafka.keystore.jks \
  -storepass $PASSWORD

### Step 3: Sign Kafka cert with CA
openssl x509 -req -CA ca-cert.pem -CAkey ca-key.pem -in kafka.csr \
  -out kafka.crt -days 365 -CAcreateserial -sha256

### Step 4: Import CA and signed cert into Kafka keystore
keytool -import -alias CARoot -file ca-cert.pem \
  -keystore kafka.keystore.jks \
  -storepass $PASSWORD -noprompt

keytool -import -alias kafka -file kafka.crt \
  -keystore kafka.keystore.jks \
  -storepass $PASSWORD -noprompt

### Step 5: Create truststore for Kafka (contains only CA cert)
keytool -import -alias CARoot -file ca-cert.pem \
  -keystore kafka.truststore.jks \
  -storepass $PASSWORD -noprompt


### Step 5: Create truststore for Kafka UI (contains only CA cert)
keytool -import -alias CARoot -file ca-cert.pem \
  -keystore kafka-ui.truststore.jks \
  -storepass $PASSWORD -noprompt

### Step 6: Create truststore for Kafka Producer and Consumer (contains only CA cert)
cd ..
cd src/main/resources
keytool -import -alias CARoot -file ../../../secrets/ca-cert.pem \
  -keystore kafka.client.truststore.jks \
  -storepass $PASSWORD -noprompt

echo "✅ kafka-ui.truststore.jks created for Kafka UI access."


echo "✅ All certificates, keystores, and truststores generated in 'secrets/'"
