package com.chaschev.reports.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.OutputStream;

/**
 * User: chaschev
 * Date: 6/15/13
 */
public class ReportBuilder {
    protected Document document;
    protected PdfWriter writer;
    protected PdfContentByte canvas;

    private ReportBuilder(OutputStream os) {
        this(os, PageSize.A4);
    }

    private ReportBuilder(OutputStream os, Rectangle pageSize){
        try {
            document = new Document(pageSize);
            writer = PdfWriter.getInstance(document, os);
            document.open();
            canvas = writer.getDirectContent();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public static ReportBuilder newReportBuilder(OutputStream os, Rectangle pageSize){
        return new ReportBuilder(os, pageSize);
    }

    public <DATA> CompositeBuilder<DATA> newCompositeBuilder(Class<DATA> aClass, String name){
        return new CompositeBuilder<DATA>(aClass, name, this);
    }

    public <DATA> TableBuilder<DATA> newTableBuilder(Class<DATA> aClass, String name){
        return new TableBuilder<DATA>(aClass, name, this);
    }

    public SingleColumnFall newSingle(String name) {
        return new SingleColumnFall(name, canvas);
    }

    public static class CompositeBuilder<DATA>{
        HCompositeColumnFall<DATA, HCompositeColumnFall> HCompositeColumnFall;

        public CompositeBuilder(Class<DATA> aClass, String name, ReportBuilder reportBuilder) {
            HCompositeColumnFall = new HCompositeColumnFall<DATA, HCompositeColumnFall>(name);
            HCompositeColumnFall.document = reportBuilder.document;
        }

        public CompositeBuilder<DATA> setRelativeWidths(float... values) {
            HCompositeColumnFall.setRelativeWidths(values);
            return this;
        }

        public CompositeBuilder<DATA> setChildrenProjector(HCompositeColumnFall.Projector<DATA> childrenProjector) {
            HCompositeColumnFall.setChildrenProjector(childrenProjector);
            return this;
        }

        public CompositeBuilder<DATA> addChildren(ColumnFall... children) {
            for (ColumnFall child : children) {
                HCompositeColumnFall.addChild(child);
            }
            return this;
        }

        public ColumnFall<DATA, ? extends ColumnFall> build() {
            return HCompositeColumnFall;
        }
    }

    public static class TableBuilder<DATA>{
        IterableCompositeColumnFall<DATA> tableColumnFall;

        public TableBuilder(Class<DATA> aClass, String name, ReportBuilder reportBuilder) {
            tableColumnFall = new IterableCompositeColumnFall<DATA>(name);
            tableColumnFall.document = reportBuilder.document;
        }

        public TableBuilder<DATA> rows(ColumnFall<DATA, ? extends ColumnFall> rowColumnFall) {
            tableColumnFall.setRowFall(rowColumnFall);
            return this;
        }

        public TableBuilder<DATA> rectangle(float llx, float lly, float urx, float ury) {
            tableColumnFall.setRectangle(llx, lly, urx, ury);
            return this;
        }

        public IterableCompositeColumnFall<DATA> build() {
            if(tableColumnFall.isRectangleSet()){
                tableColumnFall.initPositions();
            }
            return tableColumnFall;
        }
    }

    public void close() {
        document.close();
    }
}
