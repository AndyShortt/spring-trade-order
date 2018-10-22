package core;

import org.springframework.stereotype.Service;

@Service
public class TradeService {

    TradeRepository tradeRepository;
    SecurityRepository securityRepository;

    public TradeService() {

        tradeRepository = new TradeRepository();
        securityRepository = new SecurityRepository();
    }

    // First stop when new trade is received
    public Trade process(Trade trade) {

        // Perform validation, kick it out before submission if not valid
        if (!validateTrade(trade)) {
            return trade;
        }

        // Submit new or existing trade
        if (trade.getTransId() == 0) {
            trade.setNewFlag(true);
            trade.setResult(tradeRepository.submitNewTrade(trade));
        }
        else {
            trade.setNewFlag(false);
            trade.setResult(tradeRepository.submitExistingTrade(trade));
        }

        return trade;
    }

    public boolean validateTrade(Trade trade) {

        //1. Is quantity acceptable (greater than or equal to zero, less than 1 million)
        //2. Is Symbol acceptable (listed in tradable list)

        if (trade.getQuantity() > -1000000 && trade.getQuantity() < 1000000 &&
                securityRepository.tradable("SYMBOL",trade.getSymbol())) {

            return true;
        }
        else {
            trade.setResult("Failed Validation On Quantity or Symbol");
            return false;
        }

    }

}