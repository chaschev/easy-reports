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
        return new SingleColumnFall(name, document, canvas);
    }

    public <DATA> VerticalBuilder<DATA> newVCompositeBuilder(Class<DATA> aClass, String name){
        return new VerticalBuilder<DATA>(aClass, name, this);
    }

    public static class CompositeBuilder<DATA>{
        HCompositeColumnFall<DATA, HCompositeColumnFall> hCompositeColumnFall;

        public CompositeBuilder(Class<DATA> aClass, String name, ReportBuilder reportBuilder) {
            hCompositeColumnFall = new HCompositeColumnFall<DATA, HCompositeColumnFall>(name, reportBuilder.document);
            hCompositeColumnFall.document = reportBuilder.document;
        }

        public CompositeBuilder<DATA> setRelativeWidths(float... values) {
            hCompositeColumnFall.setRelativeWidths(values);
            return this;
        }

        public CompositeBuilder<DATA> setChildrenProjector(Projector<DATA> childrenProjector) {
            hCompositeColumnFall.setChildrenProjector(childrenProjector);
            return this;
        }

        public CompositeBuilder<DATA> addChildren(ColumnFall... children) {
            for (ColumnFall child : children) {
                hCompositeColumnFall.addChild(child);
            }
            return this;
        }

        public ColumnFall<DATA, ? extends ColumnFall> build() {
            return hCompositeColumnFall;
        }
    }

    public static class TableBuilder<DATA>{
        TableCompositeColumnFall<DATA> tableColumnFall;

        public TableBuilder(Class<DATA> aClass, String name, ReportBuilder reportBuilder) {
            tableColumnFall = new TableCompositeColumnFall<DATA>(name, reportBuilder.document);
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

        public TableCompositeColumnFall<DATA> build() {
            tableColumnFall.initPositions();
            return tableColumnFall;
        }
    }

    public static class VerticalBuilder<DATA>{
        VerticalCompositeColumnFall<DATA> verticalFall;

        public VerticalBuilder(Class<DATA> aClass, String name, ReportBuilder reportBuilder) {
            verticalFall = new VerticalCompositeColumnFall<DATA>(name, reportBuilder.document);
        }

        public VerticalBuilder<DATA> setChildren(ColumnFall... columnFall) {
            verticalFall.setChildren(columnFall);
            return this;
        }

        public VerticalBuilder<DATA> setChildrenProjector(Projector<DATA> childrenProjector) {
            verticalFall.setChildrenProjector(childrenProjector);
            return this;
        }

        public VerticalBuilder<DATA> rectangle(float llx, float lly, float urx, float ury) {
            verticalFall.setRectangle(llx, lly, urx, ury);
            return this;
        }

        public VerticalCompositeColumnFall<DATA> build() {
            if(verticalFall.childrenProjector == null){
                throw new IllegalStateException("projector must be set for " + verticalFall.name);
            }

            if(verticalFall.children == null){
                throw new IllegalStateException("children must be set for " + verticalFall.name);
            }

            verticalFall.initPositions();

            return verticalFall;
        }
    }

    public void close() {
        document.close();
    }
}
