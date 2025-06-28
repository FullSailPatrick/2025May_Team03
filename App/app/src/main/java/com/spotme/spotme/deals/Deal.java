package com.spotme.spotme.deals;

public class Deal {
    private String name;
    private String reason;
    private String amount;
    private String time;

    public Deal(String name, String reason, String amount, String time) {
        this.name = name;
        this.reason = reason;
        this.amount = amount;
        this.time = time;
    }


    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public String getAmount() {
        return amount;
    }

    public String getTime() {
        return time;
    }
}
