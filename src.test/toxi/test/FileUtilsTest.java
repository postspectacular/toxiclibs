package toxi.test;

import java.util.Iterator;

import junit.framework.TestCase;
import toxi.util.FileSequenceDescriptor;
import toxi.util.FileUtils;

public class FileUtilsTest extends TestCase {

    public void testSequence() {
        FileSequenceDescriptor d =
                FileUtils.getFileSequenceDescriptorFor("test/img-010.tga");
        for (Iterator<String> i = d.iterator(); i.hasNext();) {
            System.out.println(i.next());
        }
        assertEquals(3, d.getDuration());
    }
}
