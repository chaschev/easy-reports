package com.chaschev.reports.billing;
/**
 * User: chaschev
 * Date: 6/11/13
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This should be a private static class in a service, but Grails is crappy with static inner classes.
 */
public class BSRBillingRow {
    public int code;
    public double price;

    public List<BSRPatientRow> rows = new ArrayList<BSRPatientRow>(2);

    public BSRBillingRow(int code, double price) {
        this.code = code;
        this.price = price;
    }

    public void addPatient(long id, String fullname, String price1, String price2){
        for (BSRPatientRow row : rows) {
            if(row.id == id){
                row.count++;
                return;
            }
        }

        rows.add(new BSRPatientRow(id, fullname, price1, price2));

        Collections.sort(rows);
    }


    @Override
    public java.lang.String toString()
    {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder("BSRBillingRow{");
        sb.append("code=").append(code);
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }
}
