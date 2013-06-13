package com.chaschev.reports.billing;

import java.util.Map;
import java.util.TreeMap;

/**
 * User: chaschev
 * Date: 6/11/13
 */

/**
 * This should be a private static class in a service, but Grails is crappy with static classes.
 */
public class FacilityRow{
    public String facilityName;
    public Map<Integer, BSRBillingRow> codeToRow = new TreeMap<Integer, BSRBillingRow>();
    public double totalSum;

    public FacilityRow(String facilityName) {
        this.facilityName = facilityName;
    }

    public BSRBillingRow get(int cptCode, double price) {
        totalSum += price;

        BSRBillingRow r = codeToRow.get(cptCode);

        if(r == null){
            codeToRow.put(cptCode, r = new BSRBillingRow(cptCode, price));
        }

        return r;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FacilityRow{");
        sb.append("facilityName='").append(facilityName).append('\'');
        sb.append(", totalSum=").append(totalSum);
        sb.append('}');
        return sb.toString();
    }
}

