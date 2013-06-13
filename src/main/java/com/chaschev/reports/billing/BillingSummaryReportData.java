package com.chaschev.reports.billing;

import java.util.Map;

/**
 * User: chaschev
 * Date: 6/11/13
 */
public class BillingSummaryReportData {
    public String header;
    public Map<Long, PhysicianRow> physicianRowMap;

    public BillingSummaryReportData(Map<Long, PhysicianRow> physicianRowMap) {
        this.physicianRowMap = physicianRowMap;
    }
}
