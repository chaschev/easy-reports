package com.chaschev.reports.billing;

/**
 * User: chaschev
 * Date: 6/11/13
 */

/**
 * This should be a private static class in a service, but Grails is crappy with static inner classes.
 */
public class BSRPatientRow implements Comparable<BSRPatientRow>{
    public long id;
    public String name;
    public String price1;
    public String price2;
    public int count = 1;

    public BSRPatientRow(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int compareTo(BSRPatientRow o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BSRPatientRow{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", count=").append(count);
        sb.append('}');
        return sb.toString();
    }
}
