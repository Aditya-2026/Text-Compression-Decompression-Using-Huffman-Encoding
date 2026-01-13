public class HuffmanNode implements Comparable<HuffmanNode> {
    public byte data;
    public int frequency;
    public HuffmanNode left, right;

    public HuffmanNode(byte data, int frequency) {
        this.data = data;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    @Override
    public int compareTo(HuffmanNode other) {
        return this.frequency - other.frequency;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}
