package com.mavharsha.ecom;

import com.mavharsha.ecom.model.ZipCode;
import com.mavharsha.ecom.model.ZipCodeRange;
import com.mavharsha.ecom.service.IShippingService;
import com.mavharsha.ecom.service.ShippingService;

import java.util.List;
import java.util.Scanner;

public class MainApplication {

    public static void main(String[] args) {
        int input;
        IShippingService shippingService = new ShippingService();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Based on the inputs in resources/ListOfShippingZipCodeRanges.json, the following ranges are consolidate shippable ranges:");
        try {
            List<ZipCodeRange> listOfShippableRanges = shippingService.getConsolidatedShippableZipCodeRanges();
            for (int index = 0; index < listOfShippableRanges.size(); index++) {
                System.out.println("[ "+ listOfShippableRanges.get(index).getLowerRange().getCode() + ", " + listOfShippableRanges.get(index).getUpperRange().getCode() + " ]");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Please enter the ZipCode of an attempted delivery.");

        while((input = scanner.nextInt()) > 0) {
            try {
                if(shippingService.canBeShippedToZipCode(new ZipCode(input))) {
                    System.out.println("Yes! Can be shipped to " + input);
                } else {
                    System.out.println("Nope! Cannot be shipped to " + input);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
