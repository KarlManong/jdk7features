import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class NewFeaturesTest {
    private boolean closed = false;
    @Test
    public void switchTest() {
        String a = switchCase("a");
        assertEquals(a, "a");
        assertEquals(1, switchCase(1));
    }

    @Test
    public void numberTest(){
        int a = 0b100;
        assertEquals(a, 4);
        int b = 100_000;
        assertEquals(b, 100000);
    }

    @Test
    public void exceptionTest1() {
        Exception exception = null;
        try {
            throw new IOException("1");
        } catch (IOException ex) {
            exception = ex;
        } finally {
            try {
                throw new IOException("2");
            } catch (IOException ex) {
                if (exception != null) {
                    exception.addSuppressed(ex);
                }
            }
        }
        assertThat(exception.getSuppressed(), hasItemInArray(Matchers.<Throwable>hasProperty("message", equalTo("2"))));
        exception.printStackTrace();
    }

    @Test(expected = Exception.class)
    /**
     * 7之前是throws Exception
     */
    public void exceptionTest2() throws FileNotFoundException, SocketException{
        boolean b = new Random().nextBoolean();
        try {
            if (b) {
                throw new FileNotFoundException();
            } else {
                throw new SocketException();
            }
        } catch (Exception e) {
            throw e;
        }

    }

    @Test
    public void exceptionTest3(){
        boolean b = new Random().nextBoolean();
        try {
            if (b) {
                throw new FileNotFoundException();
            } else {
                throw new SocketException();
            }
        } catch (FileNotFoundException|SocketException e) {
           e.printStackTrace();
            assertNotNull(e);
        }

    }

    private class AutoClose implements Closeable{

        @Override
        public void close() throws IOException {
            System.out.println("closed");
            closed = true;
        }
    }

    @Test
    public void diamondTypeTest() {
        List<String> list = new ArrayList<>();
        list.add("a");
        ArrayList<String> list2 = Lists.newArrayList();
        list2.add("a");
        assertEquals(list, list2);
    }

    @Test
    public void tryResourceTest() throws IOException {
        assertFalse(closed);
        try (AutoClose c = new AutoClose()) {
            System.out.println("test");
        }
        assertTrue(closed);
    }

    @Test
    public void varArgsTest(){
        method("a", "b");
    }

    private <T> void method(T... tests) {

    }

    private int switchCase(int i) {
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return -1;
        }
    }

    private String switchCase(String string) {
        switch (string) {
            case "a":
                return "a";
            case "b":
                return "b";
            default:
                return "";
        }
    }
}
