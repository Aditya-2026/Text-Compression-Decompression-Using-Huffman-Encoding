import java.io.FileOutputStream;
import java.io.IOException;

public class BitOutputStream implements AutoCloseable {
    private FileOutputStream out;
    private int currentByte;
    private int bitCount;

    public BitOutputStream(FileOutputStream out) {
        this.out = out;
        this.currentByte = 0;
        this.bitCount = 0;
    }

    public void write(String bits) throws IOException {
        for (char bit : bits.toCharArray()) {
            writeBit(bit == '1' ? 1 : 0);
        }
        flush();
    }

    private void writeBit(int bit) throws IOException {
        currentByte = (currentByte << 1) | bit;
        bitCount++;
        if (bitCount == 8) {
            out.write(currentByte);
            bitCount = 0;
            currentByte = 0;
        }
    }

    private void flush() throws IOException {
        if (bitCount > 0) {
            currentByte <<= (8 - bitCount);
            out.write(currentByte);
        }
    }

    @Override
    public void close() throws IOException {
        flush();
        out.close();
    }
}
