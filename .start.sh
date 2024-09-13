aws dynamodb create-table \
    --table-name account \
    --attribute-definitions \
        AttributeName=user_name,AttributeType=S \
        AttributeName=password,AttributeType=S \
        AttributeName=email,AttributeType=S \
        AttributeName=roles,AttributeType=L \
        AttributeName=account_statuses,AttributeType=L \
    --key-schema AttributeName=user_name,KeyType=HASH AttributeName=email,KeyType=RANGE \
    --global-secondary-indexes \
        "[
          {
            \"IndexName\": \"EmailIndex\",
            \"keySchema\": {\"AttributeName\":\"email\",\"KeyType\"=\"HASH\"}
          }
        ]" \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
    --table-class STANDARD

#java -D"java.library.path"=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb