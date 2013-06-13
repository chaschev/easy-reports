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
public class PhysicianRow {
    public PhysicianRow(String name) {
        this.name = name;
    }

    public String name;
    public Map<String, FacilityRow> facToRow = new TreeMap<String, FacilityRow>();

    public FacilityRow get(String name) {
        FacilityRow r = facToRow.get(name);

        if(r == null){
            facToRow.put(name, r = new FacilityRow(name));
        }

        return r;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PhysicianRow{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

