package com.chaschev.reports.itext;

import com.chaschev.reports.billing.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

/**
 * User: chaschev
 * Date: 6/12/13
 */

abstract class AbstractList extends java.util.AbstractList{
    int size;

    protected AbstractList(int size) {
        this.size = size;
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("AbstractList.size");
    }
}

public class Example1 {

    public static void main(String[] args) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("e:/test.pdf"));
        document.open();

        PdfContentByte canvas = writer.getDirectContent();

        CompositeColumnFall<BSRPatientRow, CompositeColumnFall> patientTableRowFall = new CompositeColumnFall<BSRPatientRow, CompositeColumnFall>("patientTable");

        patientTableRowFall
//            .setRectangle(0, 0, 700, 842)
            .setRelativeWidths(60, 20, 20);

        patientTableRowFall.document = document;

        patientTableRowFall.setChildrenProjector(new FieldProjector<BSRPatientRow>(BSRPatientRow.class, "name", "price1", "price2"));

        patientTableRowFall
            .addChild(new SingleColumnFall<String>("name", canvas))
            .addChild(new SingleColumnFall<String>("price1", canvas))
            .addChild(new SingleColumnFall<String>("price2", canvas))
            .calcChildrenPositions();

        IterableCompositeColumnFall<BSRPatientRow> patientTableFall = new IterableCompositeColumnFall<BSRPatientRow>("BSRPatientRow")
            .setRectangle(0, 0, 700, 842);

        patientTableFall
            .setRelativeWidths(1)
            .setRowFall(patientTableRowFall);
//            .calcChildrenPositions();

        CompositeColumnFall<BSRBillingRow, CompositeColumnFall> billingCodeRowFall = new CompositeColumnFall<BSRBillingRow, CompositeColumnFall>("codeRow");

        billingCodeRowFall
            .setRelativeWidths(10, 90);

        billingCodeRowFall.document = document;

        billingCodeRowFall.setChildrenProjector(new CompositeColumnFall.Projector<BSRBillingRow>() {
            @Override
            public RowData<BSRBillingRow> apply(final BSRBillingRow bRow) {
                return new RowData<BSRBillingRow>(new Object[]{bRow.code}, bRow.rows);
            }
        }); //new FieldProjector<BSRBillingRow>(BSRBillingRow.class, "code", "rows")

        billingCodeRowFall
            .addChild(new SingleColumnFall<StringBuffer>("code", canvas))
            .addChild(patientTableFall);

        billingCodeRowFall.document = document;

        IterableCompositeColumnFall<BSRBillingRow> billingCodeTable = new IterableCompositeColumnFall<BSRBillingRow>("codeTable")
            .setRectangle(0, 0, 690, 842);

        billingCodeTable.document = document;

        billingCodeTable

            .setRowFall(billingCodeRowFall)
            .setChildrenProjector(CompositeColumnFall.IDENTITY_PROJECTOR)
//            .addChild(billingCodeRowFall)
            .calcChildrenPositions();

        ///////////// DATA INIT ///////////////

        PhysicianRow physicianRow1 = new PhysicianRow("Phys 1");

        facRow1(physicianRow1, 1000, "Facility 1");


//        List<BSRPatientRow> rows = physicianRow1.get("Facility 1").codeToRow.get(10002).rows;
//        patientTableFall.parent = null;
//        patientTableFall.applyAdder(false, rows, true, false);
//        document.close();
//        System.exit(0);


        billingCodeTable.applyAdder(false, physicianRow1.get("Facility 1").codeToRow.values(), true, false);

        document.close();


        //
        // | doctor 1 [(cont.)] |
        // | | facility 1 [(cont.)]|
        // | |  | billing_code 1 | patient 1 | sum1 | sum2
        // | |  |                | patient 3 | sum1 | sum2
        // | |  |                | patient 4 | sum1 | sum2
        // | |  | billing_code 2 | patient 2 | sum3 | sum4
        // | | facility 2 [(cont.)]|
        // | |  | billing_code 1 | patient 1 | sum1 | sum2
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
