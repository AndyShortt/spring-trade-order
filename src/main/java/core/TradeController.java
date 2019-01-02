package core;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;

@RestController
public class TradeController {

// Primary Order instruction capture handler
// http://localhost:8080/trade?transid=100&symbol=AMZN&quantity=500&price=1600
    @RequestMapping("/trade")
    public String trade(
            @RequestParam(value="transid", required = false, defaultValue = "0") int transid,
            @RequestParam(value="symbol") String symbol,
            @RequestParam(value="quantity") double quantity,
            @RequestParam(value="price") double price) {

        Trade newTrade = new Trade(transid,symbol,quantity,price);
        TradeService tradeService = new TradeService();

        return tradeService.process(newTrade).getResult();
    }

// Alternative Order instruction handler using preferred param handler style & POST mapping
// http://localhost:8080/trade2/transid/100/symbol/AMZN/quantity/500/price/1600
    @RequestMapping(value = "/trade2/transid/{transid}/symbol/{symbol}/quantity/{quantity}/price/{price}", method = POST)
    @ResponseBody
    public String trade2(
      @RequestParam(value="transid", defaultValue = "0") int transid,
      @RequestParam(value="symbol") String symbol,
      @RequestParam(value="quantity") double quantity,
      @RequestParam(value="price") double price) {

        Trade newTrade = new Trade(transid,symbol,quantity,price);
        TradeService tradeService = new TradeService();

        return tradeService.process(newTrade).getResult();
}

// Used for testing simple HTTP Request Acceptance
// http://localhost:8080/testspring?transid=100
    @RequestMapping("/testspring")
    public String testSpring(@RequestParam(value="transid") int transid) {

        return "this works";
    }

    // Used for testing DynamoDB Connection
    // http://localhost:8080/testrepo?transid=100
        @RequestMapping("/testrepo")
        public String testRepo(@RequestParam(value="transid") int transid) {

            TradeRepository tradeRepository = new TradeRepository();
            System.out.println ("current trans is " + tradeRepository.getCurrentTransID());
            return "Trans incremented to " + tradeRepository.incrementTransID();
        }

    // Use for testing Service classes
    // http://localhost:8080/testservice?transid=100
    @RequestMapping("/testservice")
    public String testService(@RequestParam(value="transid") int transid) {

        Trade newTrade = new Trade(transid,"AMZN",100,1500);
        TradeService tradeService = new TradeService();
        if (tradeService.validateTrade(newTrade)){
            return "Trans valid";
        }
        else {
            return "Trans invalid";
        }

    }
}
