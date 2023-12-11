package com.dvaren.bill.constants;

public enum BillType {
    EXPEND("income"),
    INCOME("expend");

    private String type;

    BillType(String type) {
        this.type = type;
    }

   public String getType(){
        return type;
   }
}
