package core;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;


public class TradeRepository {

    AmazonDynamoDB client;
    DynamoDB dynamoDB;
    Table table;

    public TradeRepository() {
        client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new ProfileCredentialsProvider("spring_trade_service"))
                .build();
        dynamoDB = new DynamoDB(client);
        table = dynamoDB.getTable("TradeRepo");
    }

    public int getCurrentTransID() {

        // Note: Not even sure I need this function anymore... but will keep it for a bit
        String TransID = "Max_Trans";
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("TransID", TransID);

        try {
            System.out.println("Attempting to read Max TransID counter...");
            Item outcome = table.getItem(spec);
            System.out.println("GetItem succeeded: " + outcome.getNumber("Latest"));
            return outcome.getNumber("Latest").intValueExact();
        }
        catch (Exception e) {
            System.err.println("Unable to read item: " + TransID);
            System.err.println(e.getMessage());
            return -1;
        }

    }

    public int incrementTransID() {

        String TransID = "Max_Trans";
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("TransID", TransID)
                .withUpdateExpression("set Latest = Latest + :val")
                .withValueMap(new ValueMap().withNumber(":val",1))
                .withReturnValues(ReturnValue.UPDATED_NEW)
                ;

        int newTrans = -1;
        int retryCount = 0;
        while (newTrans == -1 && retryCount < 3) {

            try {
                Thread.sleep(1000 * retryCount);
                System.out.println("Incrementing the max trans_ID counter, attempt " + retryCount);
                UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
                newTrans = outcome.getItem().getNumber("Latest").intValueExact();
                System.out.println("UpdateItem succeeded, New Trans ID: " + newTrans);
            }
            catch (Exception e) {
                System.err.println("Unable to update max trans_id");
                System.err.println(e.getMessage());
            }

            retryCount++;
        }

        return newTrans;

    }

    public String submitNewTrade(Trade trade) {

        String transID = String.valueOf(incrementTransID());
        String symbol = String.valueOf(trade.getSymbol());
        String quantity = String.valueOf(trade.getQuantity());
        String price = String.valueOf(trade.getPrice());

        Item item = new Item()
                .withPrimaryKey("TransID", transID)
                .withString("Symbol", symbol)
                .withString("Quantity", quantity)
                .withString("Price", price);

        int retryCount = 0;
        while (retryCount < 3) {

            try {
                Thread.sleep(1000 * retryCount);
                System.out.println("Trying to add a new trade, trans ID " + transID);
                PutItemOutcome outcome = table.putItem(item, "attribute_not_exists(TransID)", null, null);
                System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
                return "New Trade with TransId " + transID + " Succeeded";
            } catch (ConditionalCheckFailedException e) {

                System.err.println("Unable to add item: " + transID + ". May retry with new ID");
                System.err.println(e.getMessage());
                item.withPrimaryKey("TransID", String.valueOf(incrementTransID()));

            } catch (Exception e) {

                System.err.println("Unable to add item: " + transID + ". May retry with same ID");
                System.err.println(e.getMessage());

            }

            retryCount++;
        }

        return "New Trade Rejected";
    }

    public String submitExistingTrade(Trade trade) {

        String transID = String.valueOf(trade.getTransId());
        String symbol = String.valueOf(trade.getSymbol());
        String quantity = String.valueOf(trade.getQuantity());
        String price = String.valueOf(trade.getPrice());

        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("TransID", transID)
                .withUpdateExpression("attribute_exists(TransID)")
                .withValueMap(new ValueMap().withString("Symbol", symbol).withString("Quantity", quantity).withString("Price", price))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        int retryCount = 0;
        while (retryCount < 3) {

            try {
                Thread.sleep(1000 * retryCount);
                System.out.println("Trying to update trade, trans ID " + transID);
                UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
                System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());
                return "Update Trade with TransId " + transID + " Succeeded";
            } catch (ConditionalCheckFailedException e) {

                System.err.println("Item: " + transID + " does not exist.");
                System.err.println(e.getMessage());
                return "Trade Update Rejected. TransID Does Not Exist";

            } catch (Exception e) {
                System.err.println("Unable to update item: " + transID + ". May retry.");
                System.err.println(e.getMessage());

            }
            retryCount++;
        }
        return "Trade Update Rejected";
    }
}
