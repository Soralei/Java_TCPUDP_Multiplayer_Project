import org.junit.jupiter.api.Test;

public class ByteTest {
    @Test
    void name() {
        byte[] bytes = new byte[32];
        bytes[0] = (byte)1231214151;

        for (byte aByte : bytes) {
            System.out.println(aByte);
        }
    }
}
