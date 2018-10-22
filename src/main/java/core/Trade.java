package core;

public class Trade {

  private int transID;
  private String symbol;
  private double quantity;
  private double price;
  private boolean newFlag;
  private String result = "Not Submitted";

  public Trade(int transID, String symbol, double quantity, double price) {
      this.transID = transID;
      this.symbol = symbol;
      this.quantity = quantity;
      this.price = price;
  }

  public int getTransId() {
    return transID;
  }

  public void setTransId(int transID) {
    this.transID = transID;
  }

  public String getSymbol() {
      return symbol;
  }

  public void setSymbol(String symbol) {
      this.symbol = symbol;
  }

  public double getQuantity() {
    return quantity;
  }

  public void setQuantity(double quantity) {
    this.quantity = quantity;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public boolean getNewFlag() {
    return newFlag;
  }

  public void setNewFlag(boolean newflag) {this.newFlag = newflag;}

  public String getResult() {
        return result;
    }

  public void setResult(String result) {this.result = result;}

}
