package com.dvaren.bill;

import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootTest
class BillApplicationTests {
    public static void main(String[] args) {
        BigDecimal len = new BigDecimal(3);
        BigDecimal splitMoney = new BigDecimal(13.00).divide(len,2, RoundingMode.CEILING);
        System.out.println(splitMoney);
    }
}
