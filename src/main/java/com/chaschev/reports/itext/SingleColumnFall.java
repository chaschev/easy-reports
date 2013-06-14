package com.chaschev.reports.itext;

import com.chaschev.reports.billing.RowData;
import com.chaschev.reports.billing.Utils;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;

import java.util.Arrays;

import static com.chaschev.reports.itext.AdditionResultType.OK;
import static com.chaschev.reports.itext.AdditionResultType.OVERFLOW;

/**
 * User: chaschev
 * Date: 6/12/13
 */
public class SingleColumnFall<DATA> extends ColumnFall<DATA, SingleColumnFall>{
    ColumnText singleColumn;
    ColumnText backup;

    public SingleColumnFall(String name, PdfContentByte canvas){
        super(name);
        singleColumn = new ColumnText(canvas);
        backup = new ColumnText(canvas);
    }

    @Override
    public SingleColumnFall setRectangle(float llx, float lly, float urx, float ury) {
        super.setRectangle(llx, lly, urx, ury);

        singleColumn.setSimpleColumn(llx, lly, urx, ury);

        return this;
    }

//    public AdditionResultType addChunk(Chunk chunk) {
//        return tryAddTextObjects(chunk);
//    }
//
//    public AdditionResultType addPhrase(Phrase phrase) {
//        return tryAddTextObjects(phrase);
//    }

    public float getYLine() {
        return singleColumn.getYLine();
    }

    @Override
    public AdditionResultType growBy(float height) {
        float newY = singleColumn.getYLine() - height;

        if(newY < lly){
            return OVERFLOW;
        }

        singleColumn.setYLine(newY);

        return OK;
    }

    public void setYLine(float yLine) {
        singleColumn.setYLine(yLine);
    }

    @Override
    public AdditionResult applyAdder(boolean simulate, DATA data, boolean allowBreak, boolean afterPageBreak) {

        return adder.add(simulate, this, new RowData<DATA>(data));
    }

    // todo Remove or refactor
    protected AdditionResult tryAddTextObjects(Object... texts) {
        backup();

        AdditionResult result = addTextObjects(true, texts);

        rollback();

        if (result != AdditionResult.OK) {
            return result;
        } else {
            addTextObjects(false, texts);

            return AdditionResult.OK;
        }
    }

    protected void backupMe() {
        backup.setACopy(singleColumn);
    }

    public AdditionResult addTextObjects(boolean simulate, Object[] texts) {
        Utils.print("[%s]: simple, sim: %s, objs: %s%n", name, simulate, Arrays.toString(texts));
        try {
            //todo move this into functions compositions
            for (int i = 0; i < texts.length; i++) {
                Object text = texts[i];
                if (!(text instanceof Chunk || text instanceof Phrase)) {
                    texts[i] = new Chunk(String.valueOf(text));
                }
            }

            for (Object text : texts) {
                if (text instanceof Phrase) {
                    singleColumn.addText((Phrase) text);
                }else{
                    singleColumn.addText((Chunk) text);
                }
            }

            int status = singleColumn.go(simulate);

            return ColumnText.hasMoreText(status) ? AdditionResult.OVERFLOW : AdditionResult.OK;
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
