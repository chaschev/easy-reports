package com.chaschev.reports.billing;

import com.chaschev.reports.itext.*;
import com.itextpdf.text.PageSize;

import java.io.FileOutputStream;

//todo add fixed widths (10px, 10%)
//todo b. can be avoided

public class VerticalExample1 {

    public static void main(String[] args) throws Exception {
        ReportBuilder b = ReportBuilder.newReportBuilder(
            new FileOutputStream("e:/test.pdf"), PageSize.A4
        );

        VerticalCompositeColumnFall<String[]> fall = b.newVCompositeBuilder(String[].class, "facilityRow")
            .setChildrenProjector(new Projector<String[]>() {
                @Override
                protected RowData apply(String[] ss) {
                    Object[] o = new Object[ss.length];
                    for (int i = 0; i < ss.length; i++) {
                        o[i] = chunks(ss[i]);

                    }
                    return new RowData(o);
                }
            })
            .rectangle(0, 0, 690, 842)       //todo this should be moved into a root builder
            .setChildren(
                b.newSingle("row1"),
                b.newSingle("row2"))
            .build();

        ///////////// DATA INIT ///////////////


        fall.applyAdder(false, new String[]{"row1", "row2"}, true, false);

        b.close();
    }
}
