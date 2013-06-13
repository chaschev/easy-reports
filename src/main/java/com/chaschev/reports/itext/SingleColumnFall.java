package com.chaschev.reports.itext;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;

import static com.chaschev.reports.itext.AdditionResult.OK;
import static com.chaschev.reports.itext.AdditionResult.OVERFLOW;

/**
 * User: chaschev
 * Date: 6/12/13
 */
public class SingleColumnFall<DATA> extends ColumnFall<DATA, SingleColumnFall>{
    ColumnText singleColumn;
    ColumnText backup;

    public SingleColumnFall(PdfContentByte canvas){
        singleColumn = new ColumnText(canvas);
        backup = new ColumnText(canvas);
    }

    @Override
    public SingleColumnFall setRectangle(float llx, float lly, float urx, float ury) {
        super.setRectangle(llx, lly, urx, ury);

        singleColumn.setSimpleColumn(llx, lly, urx, ury);

        return this;
    }

    public AdditionResult addChunk(Chunk chunk) {
        return tryAddTextObjects(chunk);
    }

    public AdditionResult addPhrase(Phrase phrase) {
        return tryAddTextObjects(phrase);
    }

    public float getYLine() {
        return singleColumn.getYLine();
    }

    @Override
    public AdditionResult growBy(float height) {
        float newY = singleColumn.getYLine() + height;

        if(newY < lly){
            return OVERFLOW;
        }
        return OK;
    }

    public AdditionResult setYLine(float yLine) {
        if(yLine < lly){
            return OVERFLOW;
        }
        singleColumn.setYLine(yLine);

        return OK;
    }

    @Override
    public AdditionResult applyAdder(boolean simulate, DATA data, boolean allowBreak) {
        return adder.add(simulate, this, data);
    }

    // todo Remove or refactor
    protected AdditionResult tryAddTextObjects(Object... texts) {
        backup();

        AdditionResult result = addTextObjects(true, texts);

        rollback();

        if (result != OK) {
            return result;
        } else {
            addTextObjects(false, texts);

            return OK;
        }
    }

    protected void backupMe() {
        backup.setACopy(singleColumn);
    }

    public AdditionResult addTextObjects(boolean simulate, Object... texts) {
        try {
            for (Object text : texts) {
                if (text instanceof Phrase) {
                    singleColumn.addText((Phrase) text);
                }else{
                    singleColumn.addText((Chunk) text);
                }
            }

            int status = singleColumn.go(simulate);

            return ColumnText.hasMoreText(status) ? OVERFLOW : OK;
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    protected void rollbackMe() {
        singleColumn.setACopy(backup);
    }

    @Override
    public void handlePageBreak() {
        throw new UnsupportedOperationException("todo SingleColumnFall.handlePageBreak");
    }
}
