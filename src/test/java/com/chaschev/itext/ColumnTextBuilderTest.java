package com.chaschev.itext;

import com.chaschev.itext.columns.BalancedColumnsBuilderTest;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;
import java.util.Random;

/**
 * User: chaschev
 * Date: 9/10/13
 */
public class ColumnTextBuilderTest {

    public static final String loremIpsumSmall = "Lorem ipsum dolor sit amet,";
    public static final String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque et iaculis justo, vitae porta massa. Duis eget mauris sed nibh blandit cursus et in justo. Integer luctus orci eget nibh tincidunt, ut gravida est convallis. Etiam faucibus, ante quis interdum viverra, justo turpis varius neque, nec consequat lacus mauris ut ante. Nulla lobortis aliquam augue, at pretium est blandit adipiscing. Nam tincidunt pretium est, vitae cursus ante dignissim quis. Vestibulum pharetra nibh a nunc fringilla lacinia.";

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testAtomicIterator() throws Exception {
        int i;
//        for(i = 1; i<=1; i++){
//            testIterator(i, loremIpsumSmall, 150, String.format("ipsum_small_%02d", i));
//        }

        for(i=1; i<=16; i++){
            testIterator(i, loremIpsum, 150, String.format("ipsum_150px_%02d", i));
        }

        for(i=1; i<=20; i++){
            testIterator(i, loremIpsum, 100, String.format("ipsum_100px_%02d", i));
        }

        for(i=1; i<=60; i++){
            testIterator(i, loremIpsum, 40, String.format("ipsum_40px_%02d", i));
        }
    }

    public void testIterator(int maxStepCount, String text, float width, String title) throws Exception {
        ITextBuilder b = BalancedColumnsBuilderTest
            .createTestBuilder()
            .drawBorders();

        final Rectangle rect = new Rectangle(250, 500, 250 + width, 520);

        b.getCanvasBuilder().drawGrayRectangle(rect, BaseColor.BLUE);

        final ColumnTextBuilder ctb = b.newColumnTextBuilder()
            .setSimpleColumn(rect)
            .addElement(b.phrase("Text will go below", "header").build());

        ctb.go();

        final Phrase phrase = b.phrase(text).build();

        final RectangleBuilder currentStepRectangle = b.newCopyRectangleBuilder(ctb.getCurrentRectangle().get());

        final Iterator<AtomicIncreaseResult> iterator = ctb
            .saveState()
            .newAtomicIteratorFor(phrase);

        float totalHeight = 0;

        final Random r = new Random(1);

        for (int step = 0; step < maxStepCount; step++) {
            if(!iterator.hasNext()){
                break;
            }

            final AtomicIncreaseResult next = iterator.next();

            System.out.printf("step: %d, result: %s, height: %.2f, yLine: %.2f%n", step + 1, next.type, next.height, ctb.getYLine());

            totalHeight += next.height;

            currentStepRectangle.growBottom(next.height);

            b.getCanvasBuilder().drawGrayRectangle(currentStepRectangle.get(), new BaseColor(r.nextInt(180), r.nextInt(180), r.nextInt(180)));
        }

        System.out.printf("ctb vs totalHeight: %.2f - %.2f%n", ctb.getYLine(), currentStepRectangle.getTop() - totalHeight);

        ctb.restoreState();
//        ctb.reset();

        ctb
            .growBottom(totalHeight + 1f)
            .addElement(phrase)
            .go();

        System.out.printf("done.%n");

        b.saveToFile(new File("output/atomicIterator/test_" + title + ".pdf"));
    }
}
