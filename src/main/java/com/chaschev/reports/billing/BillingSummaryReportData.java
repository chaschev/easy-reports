package com.chaschev.reports.billing;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: chaschev
 * Date: 6/11/13
 */
public class BillingSummaryReportData {
    public String header;
    public Map<Long, PhysicianRow> physicianRowMap = new LinkedHashMap<Long, PhysicianRow>();

    public BillingSummaryReportData() {
    }

    public PhysicianRow get(long id, String name){
        PhysicianRow row = physicianRowMap.get(id);

        if(row == null){
            physicianRowMap.put(id, row = new PhysicianRow(name));
        }

        return row;
    }
}
