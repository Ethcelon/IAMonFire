#!/usr/bin/env bash

mkdir -p forgerock-openbanking-excercise-tpp/src/main/resources/keystore
openssl pkcs12 -export -in *.pem -inkey *.key -name "transport" \
 -out ./forgerock-openbanking-excercise-tpp/src/main/resources/keystore/keystore.p12 -password pass:changeit
