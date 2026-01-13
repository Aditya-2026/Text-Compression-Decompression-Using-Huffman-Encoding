import java.io.FileInputStream;
import java.io.IOException;

public class BitInputStream implements AutoCloseable {
    private FileInputStream in;
    private int currentByte;
    private int bitCount;

    public BitInputStream(FileInputStream in) {
        this.in = in;
        this.bitCount = 0;
    }

    public boolean hasNext() throws IOException {
        if (bitCount == 0) {
            currentByte = in.read();
            bitCount = 8;
        }
        return currentByte != -1;
    }

    public int readBit() throws IOException {
        if (bitCount == 0) {
            currentByte = in.read();
            bitCount = 8;
            if (currentByte == -1) return -1;
        }
        int bit = (currentByte >> (bitCount - 1)) & 1;
        bitCount--;
        return bit;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
