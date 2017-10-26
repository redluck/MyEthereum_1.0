package it.redluck.materialdesign.model;

public enum Investment {

    INVESTMENT_01("08/08/2017 15:40", 242.18, 0.92904223, 225.00),
    INVESTMENT_02("09/08/2017 12:25", 245.93, 1.08000000, 265.60),
    INVESTMENT_03("29/08/2017 15:10", 303.59, 0.99095777, 300.84),
    INVESTMENT_04("27/09/2017 10:49", 248.16, 1.00000000, 248.16),
    INVESTMENT_05("17/10/2017 10:49", 284.42, 0.50000000, 142.21);

    private String date;
    private double ethValueAtDate;
    private double boughtEth;
    private double costInEu;
    private double currentInvestmentValue;
    private double profit;

    Investment(String date, double ethValueAtDate, double boughtEth, double costInEu){
        this.date = date;
        this.ethValueAtDate = ethValueAtDate;
        this.boughtEth = boughtEth;
        this.costInEu = costInEu;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public double getEthValueAtDate() {
        return ethValueAtDate;
    }
    public void setEthValueAtDate(double ethValueAtDate) {
        this.ethValueAtDate = ethValueAtDate;
    }

    public double getBoughtEth() {
        return boughtEth;
    }
    public void setBoughtEth(double boughtEth) {
        this.boughtEth = boughtEth;
    }

    public double getCostInEu() {
        return costInEu;
    }
    public void setCostInEu(double costInEu) {
        this.costInEu = costInEu;
    }

    public double getCurrentInvestmentValue() {
        return currentInvestmentValue;
    }
    public void setCurrentInvestmentValue(double currentInvestmentValue) {
        this.currentInvestmentValue = currentInvestmentValue;
    }

    public double getProfit() {
        return profit;
    }
    public void setProfit(double profit) {
        this.profit = profit;
    }
}
