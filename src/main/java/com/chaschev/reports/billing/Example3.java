package com.chaschev.reports.billing;

import com.chaschev.reports.itext.*;
import com.itextpdf.text.PageSize;

import java.io.FileOutputStream;

public class Example3 {

    public static void main(String[] args) throws Exception {
        ReportBuilder b = ReportBuilder.newReportBuilder(
            new FileOutputStream("e:/test.pdf"), PageSize.A4
        );

//        b.newCompositeBuilder(FacilityRow.class)
        //todo add fixed widths (10px, 10%)
        //todo b. can be avoided
        IterableCompositeColumnFall<BSRBillingRow> billingCodeTable = b
            .newTableBuilder(BSRBillingRow.class, "codeTable")
            .rectangle(0, 0, 690, 842)       //todo this should be moved into a root builder
            .rows(
                b.newCompositeBuilder(BSRBillingRow.class, "codeTableRow")
                    .setRelativeWidths(3, 10, 80)
                    .setChildrenProjector(new HCompositeColumnFall.Projector<BSRBillingRow>() {
                        @Override
                        public RowData<BSRBillingRow> apply(final BSRBillingRow bRow) {
                            return new RowData<BSRBillingRow>(new Object[]{}, new Object[]{bRow.code}, bRow.rows);
                        }
                    })
                    .addChildren(
                        b.newSingle("empty"),
                        b.newSingle("code"),
                        b.newTableBuilder(BSRPatientRow.class, "patientTable")
                            .rows(
                                b.newCompositeBuilder(BSRPatientRow.class, "patientTableRow")
                                    .setRelativeWidths(60, 20, 20)
                                    .setChildrenProjector(new FieldProjector<BSRPatientRow>(BSRPatientRow.class, "name", "price1", "price2"))
                                    .addChildren(
                                        b.newSingle("name"),
                                        b.newSingle("price1"),
                                        b.newSingle("price2")
                                    )
                                    .build()
                            ).build()
                    ).build()
            ).build();


        ///////////// DATA INIT ///////////////

        PhysicianRow physicianRow1 = new PhysicianRow("Phys 1");

        facRow1(physicianRow1, 1000, "Facility 1");


//        List<BSRPatientRow> rows = physicianRow1.get("Facility 1").codeToRow.get(10002).rows;
//        patientTableFall.parent = null;
//        patientTableFall.applyAdder(false, rows, true, false);
//        document.close();
//        System.exit(0);


        billingCodeTable.applyAdder(false, physicianRow1.get("Facility 1").codeToRow.values(), true, false);

        b.close();


        //
        // | doctor 1 [(cont.)] |
        // | | facility 1 [(cont.)]|
        // | |  | billing_code 1 | patient 1 | sum1 | sum2
        // | |  |                | patient 3 | sum1 | sum2
        // | |  |                | patient 4 | sum1 | sum2
        // | |  | billing_code 2 | patient 2 | sum3 | sum4
        // | | facility 2 [(cont.)]|
        // | |  | billing_code 1  [(cont.)] | patient 1 | sum1 | sum2
        // | |  |                | patient 6 | sum1 | sum2
        // | |  | billing_code 2 | patient 5 | sum3 | sum4
        // | |  | billing_code 3 | patient 3 | sum3 | sum4
        // | |  |                | patient 6 | sum3 | sum4
        // | |  |                | patient 7 | sum3 | sum4

//        CompositeColumnFall doctorFall = new CompositeColumnFall(/* 2 children: space and facility */);
//
//        ColumnFall facilityFall = new CompositeColumnFall(/* 2 children: space and billing_ccode */);
//        ColumnFall billingCodeFall = new CompositeColumnFall(/* 3 children: space, codeCell and patientsTable */);
//        ColumnFall patientsTableFall = new CompositeColumnFall(/* 3 cells:  patient, sum1, sum2 */);
//
//        doctorFall.applyAdder(doctorAdderIth, allowBreak, allowBreak); //  will be fallen back to print a header
//        facilityFall.applyAdder(facilityAdderIth, allowBreak, allowBreak)  //  will be fallen back to print a header


        /**
         * data: facility   (COMPOSITE)
         * cycles through codes adding (code, table)
         *  - child1.addText code (trivial)
         *  - child2.applyAdder(facility.get(code))
         * when receives handlePageBreak from a child:
         * - calls parent.handlePageBreak()
         * - adds (code + (cont.))
         * if can't add (billing_code 1 is too big :-) ),
         * - calls parent.handlePageBreak()
         * - handles page break
         * - continues adding
         */
//        billingCodeFall.applyAdder(billingCodeAdderIth, allowBreak, allowBreak);

        /**
         * data: patient table (COMPOSITE)
         * cycles through patients adding rows
         * if can't add,
         * - calls parent.handlePageBreak()
         * - handles page break
         * - continues adding
         */
//        patientsTableFall.applyAdder(patientAdderIth, dontAllowBreak, allowBreak);

    }

    private static void facRow1(PhysicianRow row1, int total, String name) {
        FacilityRow facRow1 = row1.get(name);

        facRow1.totalSum = total;

        billingRow1(facRow1, 10002, 23);
        billingRow2(facRow1, 10003, 24);
        billingRow2(facRow1, 10005, 29);
        billingRow1(facRow1, 10006, 23);
        billingRow1(facRow1, 10007, 23);
        billingRow1(facRow1, 10008, 23);
    }

    private static void billingRow1(FacilityRow facRow1, int cptCode, int price) {
        BSRBillingRow billingRow = facRow1.get(cptCode, price);

        addPatient(billingRow, 1, "Patient Pat1");
        addPatient(billingRow, 2, "Patient Pat2");
//        addPatient(billingRow, 2, "Patient Pat2");
//        addPatient(billingRow, 3, "Patient Pat3");
//        addPatient(billingRow, 3, "Patient Pat3");
//
        for(int i = 4;i<80;i++){
            addPatient(billingRow, i, "Patient Pat" + i);
        }
    }

    private static void addPatient(BSRBillingRow billingRow, int id, String patientName1) {
        billingRow.addPatient(id, patientName1, "$" + id, "$" + id * 2);
    }

    private static void billingRow2(FacilityRow facRow1, int cptCode, int price) {
        BSRBillingRow billingRow = facRow1.get(cptCode, price);

        addPatient(billingRow, 1, "Patient 7");
        addPatient(billingRow, 2, "Patient 8");
//
    }
}
