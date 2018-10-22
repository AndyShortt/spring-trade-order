package core;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import java.util.Iterator;
import java.util.Set;

public class SecurityRepository {

    AmazonDynamoDB client;
    DynamoDB dynamoDB;
    Table table;

    public SecurityRepository() {
        client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new ProfileCredentialsProvider("spring_trade_service"))
                .build();
        dynamoDB = new DynamoDB(client);
        table = dynamoDB.getTable("TradeRepo");
    }

    public boolean tradable(String identifierType, String identifier) {

        // While this can handle multiple types of identifiers in the future (symbol, cusip, symbol, etc)
        // We will start with assuming everything is a basic symbol from primary exchange
        String TransID = "Active_Securities";
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("TransID", TransID);
        Boolean symbolFound = false;

        try {
            System.out.println("Attempting to read Max TransID counter...");
            Item outcome = table.getItem(spec);
            System.out.println("GetItem succeeded: " + outcome.getJSONPretty("Latest"));

            Set<String> set = outcome.getStringSet("Latest");
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext() && !symbolFound) {
                if (iterator.next().equals(identifier)) {
                    symbolFound = true;
                }
            }
        }
        catch (Exception e) {
            System.err.println("Unable to read item: " + TransID);
            System.err.println(e.getMessage());

        }

        return symbolFound;
    }

}
