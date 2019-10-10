import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppTest {

    private ArrayList<String> firms;
    private ArrayList<String> syms;
    private ArrayList<String> ids;

    @BeforeAll
    void initialize() {
        firms = new ArrayList<>();
        firms.add("Amazon");
        firms.add("Microsoft");
        firms.add("Google");
        firms.add("TOBB ETU");

        syms = new ArrayList<>();
        syms.add("I");
        syms.add("Am");
        syms.add("cro");
        syms.add("Na");
        syms.add("le");
        syms.add("abc");
        syms.add("tobb");
        syms.add("to");

        ids = new ArrayList<>();
        ids.add("101");
        ids.add("117");
        ids.add("26");
        ids.add("68");
        ids.add("193");
        ids.add("33");
        ids.add("6");
        ids.add("90");
    }

    @Test
    void matchCaseTest()
    {
        List<String> shouldBe = new ArrayList<>();
        shouldBe.add("117[Am]azon");
        shouldBe.add("26Mi[cro]soft");
        shouldBe.add("193Goog[le]");

        List<String> tmp = new ArrayList<>();

        for (Row m : App.matcher(firms, syms.toArray(new String[0]), ids, true)) {
            tmp.add(m.getId() + m.getName());
        }

        Assertions.assertArrayEquals(tmp.toArray(), shouldBe.toArray());
    }

    @Test
    void matchCaseInsensitiveTest()
    {
        List<String> shouldBe = new ArrayList<>();
        shouldBe.add("117[Am]azon");
        shouldBe.add("26Mi[cro]soft");
        shouldBe.add("193Goog[le]");
        shouldBe.add("6[TOBB] ETU");

        List<String> tmp = new ArrayList<>();

        for (Row m : App.matcher(firms, syms.toArray(new String[0]), ids, false)) {
            tmp.add(m.getId() + m.getName());
        }

        Assertions.assertArrayEquals(tmp.toArray(), shouldBe.toArray());
    }

    @Test
    void inequalSymbolandIdCountTest()
    {
        ArrayList<String> temp = new ArrayList<>(syms);
        temp.remove(0);
        temp.remove(0);

        List<String> shouldBe = new ArrayList<>();
        shouldBe.add("117[Am]azon");
        shouldBe.add("26Mi[cro]soft");
        shouldBe.add("193Goog[le]");
        shouldBe.add("6[TOBB] ETU");

        List<String> tmp = new ArrayList<>();

        for (Row m : App.matcher(firms, syms.toArray(new String[0]), ids, false)) {
            tmp.add(m.getId() + m.getName());
        }

        Assertions.assertArrayEquals(tmp.toArray(), shouldBe.toArray());
    }

    @Test
    void symsNullTest()
    {
        Assertions.assertThrows(NullPointerException.class, () -> {
            App.matcher(firms, null, ids, false);
        });
    }

    @Test
    void symsEmptyListTest()
    {
        Assertions.assertArrayEquals(App.matcher(firms, new String[0], ids, false).toArray(), new String[0]);
    }

}