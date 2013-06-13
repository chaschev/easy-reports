package com.chaschev.reports.billing;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * User: chaschev
 * Date: 6/11/13
 */
public class BillingSummaryReporter {
    BillingSummaryReportData reportData;

    public BillingSummaryReporter(BillingSummaryReportData reportData) {
        this.reportData = reportData;
    }

    class PageHeader extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {

        }
    }

    public static PdfStructureElement newCptElement(PdfStructureElement parent, String name, Map<String, Object> props){
        PdfStructureElement r = new PdfStructureElement(parent, new PdfName(name));

        for (Map.Entry<String, Object> e : props.entrySet()) {
            r.setAttribute(PdfName.N, new PdfString(e.getKey()));
            r.setAttribute(PdfName.V, new PdfString(e.getValue().toString()));
        }

        return r;
    }

    //
    public byte[] create() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLDITALIC);
            Font textFont = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC);

            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.setPageEvent(new PageHeader());
            writer.setTagged();

            document.open();

            PdfStructureTreeRoot root = writer.getStructureTreeRoot();
            PdfContentByte dc = writer.getDirectContent();

            ColumnText ct = new ColumnText(dc);

            ct.setSimpleColumn(20, 1, 600, 800);
            ct.setExtraParagraphSpace(6);
//            ct.setFollowingIndent(27);

            int status = ColumnText.START_COLUMN;
//            ColumnText.hasMoreText(ColumnText.START_COLUMN);

            for (PhysicianRow physicianRow : reportData.physicianRowMap.values()) {
                dc.beginMarkedContentSequence(PdfName.H1);

                ct.setIndent(0);

                //damn the creator of that library!
                ct.setText(new Phrase(physicianRow.name, headerFont));

                status = ct.go();

                dc.endMarkedContentSequence();

                for (FacilityRow facilityRow : physicianRow.facToRow.values()) {
                    dc.beginMarkedContentSequence(PdfName.H2);

//                    ct.setYLine(ct.getYLine() - 40);
                    ct.setIndent(20);
                    ct.setText(new Phrase(facilityRow.facilityName, textFont));
                    ct.addText(Chunk.NEWLINE);

                    status = ct.go();

                    if(ColumnText.hasMoreText(status)){
                        System.out.println("SHIIIIT");
                    }

                    dc.endMarkedContentSequence();

                    PdfPTable table = new PdfPTable(4);

                    for (BSRBillingRow billingRow : facilityRow.codeToRow.values()) {
                        PdfPCell cell = new PdfPCell(new Phrase(Integer.toString(billingRow.code), textFont));

                        cell.setRowspan(billingRow.rows.size());
//                        cell.setBorder(Rectangle.NO_BORDER);

                        table.addCell(cell);

                        table.setWidths(new int[]{60, 300, 100, 100});
                        table.setTotalWidth(400);
                        table.setLockedWidth(true);

                        PdfPCell defaultCell = table.getDefaultCell();

                        defaultCell.setFixedHeight(20);
//                        defaultCell.setBorder(Rectangle.NO_BORDER);

                        for (BSRPatientRow patientRow : billingRow.rows) {
//                            dc.beginMarkedContentSequence(PdfName.DIV);

                            table.addCell(patientRow.name);

                            table.addCell("$ " + billingRow.price + " x " + patientRow.count);
                            table.addCell("$ " + billingRow.price * patientRow.count);

//                            dc.endMarkedContentSequence();
                        }

                        table.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table.setKeepTogether(false);

//                        ct.addElement();
                    }

                    dc.beginMarkedContentSequence(PdfName.DIV);
                    ct.setIndent(120);
                    ct.addElement(table);
                    status = ct.go();
                    ct.addText(Chunk.NEWLINE);
                    status = ct.go();


//                        ct.addText(new Chunk("test", textFont));
//                        ct.addText(Chunk.NEWLINE);
//                        document.add(new Phrase("test2"));

                    dc.endMarkedContentSequence();
                }
            }

            document.close();

            return baos.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
