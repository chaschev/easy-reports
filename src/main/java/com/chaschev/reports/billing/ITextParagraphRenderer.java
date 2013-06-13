package com.chaschev.reports.billing;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * User: chaschev
 * Date: 6/11/13
 */
public class ITextParagraphRenderer {
    private static final float pageHeight = 900;
    PdfContentByte pdfContentByte;

    public void render(ITextText text){
        ColumnText colText = new ColumnText(pdfContentByte);
        colText.setSimpleColumn(text.getPhrase(),
            text.x + text.paddingLeft,
            pageHeight
                - text.y
                - text.paddingTop
//                - verticalAlignOffset
                - text.leadingOffset,
            text.x + text.width - text.paddingRight,
            pageHeight
                - text.y
                - text.height
                + text.paddingBottom,
            0,
            Element.ALIGN_LEFT
            );

        try {
            colText.go();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
