import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class HuffmanCompression extends JFrame {
    private JTextArea textArea, codesArea;
    private JPanel treePanel;
    private JButton selectFilesButton, compressButton, decompressButton, zoomInButton, zoomOutButton;
    private JButton scrollUpButton, scrollDownButton, scrollLeftButton, scrollRightButton;
    private List<File> selectedFiles;
    private File compressedFile, decompressedFile;
    private HuffmanNode huffmanTreeRoot;
    private Map<Byte, String> huffmanCodes;
    private JTabbedPane tabbedPane;

    private double scale = 1.0; // Zoom scale factor
    private int offsetX = 0;    // Horizontal offset for scrolling
    private int offsetY = 0;    // Vertical offset for scrolling

    public HuffmanCompression() {
        setTitle("Huffman Compression");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tabs: Log | Huffman Tree | Huffman Codes
        tabbedPane = new JTabbedPane();

        // Tab 1: Text log
        textArea = new JTextArea();
        tabbedPane.add("Log", new JScrollPane(textArea));

        // Tab 2: Huffman tree visual panel with scrolling
        treePanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (huffmanTreeRoot != null) {
                    drawTree(g, huffmanTreeRoot, getWidth() / 2 + offsetX, 50 + offsetY, (int) (getWidth() / 4 * scale));
                }
            }
        };
        JScrollPane treeScrollPane = new JScrollPane(treePanel);
        tabbedPane.add("Huffman Tree", treeScrollPane);
        
        // Tab 3: Huffman codes as text with scrolling
        codesArea = new JTextArea();
        codesArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane codesScrollPane = new JScrollPane(codesArea);
        tabbedPane.add("Huffman Codes", codesScrollPane);

        add(tabbedPane, BorderLayout.CENTER);

        // Bottom buttons
        JPanel buttonPanel = new JPanel();
        selectFilesButton = new JButton("Select Files");
        compressButton = new JButton("Compress");
        decompressButton = new JButton("Decompress");
        zoomInButton = new JButton("Zoom In");
        zoomOutButton = new JButton("Zoom Out");

        scrollUpButton = new JButton("Up");
        scrollDownButton = new JButton("Down");
        scrollLeftButton = new JButton("Left");
        scrollRightButton = new JButton("Right");

        buttonPanel.add(selectFilesButton);
        buttonPanel.add(compressButton);
        buttonPanel.add(decompressButton);
        buttonPanel.add(zoomInButton);
        buttonPanel.add(zoomOutButton);
        buttonPanel.add(scrollUpButton);
        buttonPanel.add(scrollDownButton);
        buttonPanel.add(scrollLeftButton);
        buttonPanel.add(scrollRightButton);
        add(buttonPanel, BorderLayout.SOUTH);

        selectFilesButton.addActionListener(e -> selectFiles());
        compressButton.addActionListener(e -> compressFiles());
        decompressButton.addActionListener(e -> decompressFile());
        zoomInButton.addActionListener(e -> zoomIn());
        zoomOutButton.addActionListener(e -> zoomOut());
        scrollUpButton.addActionListener(e -> scrollUp());
        scrollDownButton.addActionListener(e -> scrollDown());
        scrollLeftButton.addActionListener(e -> scrollLeft());
        scrollRightButton.addActionListener(e -> scrollRight());
    }

    private void drawTree(Graphics g, HuffmanNode node, int x, int y, int xOffset) {
        if (node == null) return;
        String label = node.isLeaf() ? (char) node.data + " (" + node.frequency + ")" : String.valueOf(node.frequency);
        g.drawOval(x - 20, y - 20, 40, 40);
        g.drawString(label, x - 15, y + 5);

        if (node.left != null) {
            g.drawLine(x, y, x - xOffset, y + 80);
            drawTree(g, node.left, x - xOffset, y + 80, xOffset / 2);
        }

        if (node.right != null) {
            g.drawLine(x, y, x + xOffset, y + 80);
            drawTree(g, node.right, x + xOffset, y + 80, xOffset / 2);
        }
    }

    private void selectFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFiles = Arrays.asList(fileChooser.getSelectedFiles());
            textArea.setText("Selected Files:\n");
            for (File file : selectedFiles) {
                textArea.append(file.getName() + "\n");
            }
        }
    }

    private void compressFiles() {
        if (selectedFiles == null || selectedFiles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No files selected!");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Compressed File");
        fileChooser.setSelectedFile(new File("compressed_video.bin"));
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        compressedFile = fileChooser.getSelectedFile();

        try {
            ByteArrayOutputStream allBytes = new ByteArrayOutputStream();
            for (File file : selectedFiles) {
                allBytes.write(Files.readAllBytes(file.toPath()));
            }
            byte[] data = allBytes.toByteArray();

            Map<Byte, Integer> freqMap = new HashMap<>();
            for (byte b : data) {
                freqMap.put(b, freqMap.getOrDefault(b, 0) + 1);
            }

            PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
            for (Map.Entry<Byte, Integer> entry : freqMap.entrySet()) {
                pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
            }

            while (pq.size() > 1) {
                HuffmanNode left = pq.poll();
                HuffmanNode right = pq.poll();
                HuffmanNode parent = new HuffmanNode((byte) 0, left.frequency + right.frequency);
                parent.left = left;
                parent.right = right;
                pq.add(parent);
            }

            huffmanTreeRoot = pq.poll();
            huffmanCodes = new HashMap<>();
            generateHuffmanCodes(huffmanTreeRoot, "", huffmanCodes);

            // Display Huffman codes in text area
            codesArea.setText("Huffman Codes:\n");
            for (Map.Entry<Byte, String> entry : huffmanCodes.entrySet()) {
                char ch = (char) entry.getKey().byteValue();
                String displayChar = Character.isISOControl(ch) ? "[" + (entry.getKey() & 0xFF) + "]" : "'" + ch + "'";
                codesArea.append(displayChar + ": " + entry.getValue() + "\n");
            }

            StringBuilder encoded = new StringBuilder();
            for (byte b : data) {
                encoded.append(huffmanCodes.get(b));
            }

            byte[] packed = packBits(encoded.toString());

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(compressedFile))) {
                out.writeObject(freqMap);
                out.writeObject(packed);
            }

            treePanel.repaint();
            JOptionPane.showMessageDialog(this, "Compression successful!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void decompressFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Compressed File");
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        compressedFile = fileChooser.getSelectedFile();

        fileChooser.setDialogTitle("Save Decompressed File");
        fileChooser.setSelectedFile(new File("decompressed_output.mp4"));
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        decompressedFile = fileChooser.getSelectedFile();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(compressedFile))) {
            Map<Byte, Integer> freqMap = (Map<Byte, Integer>) in.readObject();
            byte[] compressedData = (byte[]) in.readObject();

            PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
            for (Map.Entry<Byte, Integer> entry : freqMap.entrySet()) {
                pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
            }

            while (pq.size() > 1) {
                HuffmanNode left = pq.poll();
                HuffmanNode right = pq.poll();
                HuffmanNode parent = new HuffmanNode((byte) 0, left.frequency + right.frequency);
                parent.left = left;
                parent.right = right;
                pq.add(parent);
            }

            HuffmanNode root = pq.poll();
            String decodedBits = unpackBits(compressedData);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            HuffmanNode current = root;
            for (char bit : decodedBits.toCharArray()) {
                current = (bit == '0') ? current.left : current.right;
                if (current.isLeaf()) {
                    output.write(current.data);
                    current = root;
                }
            }

            Files.write(decompressedFile.toPath(), output.toByteArray());
            JOptionPane.showMessageDialog(this, "Decompression successful!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void generateHuffmanCodes(HuffmanNode node, String code, Map<Byte, String> codes) {
        if (node.isLeaf()) {
            codes.put(node.data, code);
            return;
        }
        generateHuffmanCodes(node.left, code + "0", codes);
        generateHuffmanCodes(node.right, code + "1", codes);
    }

    private byte[] packBits(String bits) {
        int length = bits.length();
        int byteCount = (length + 7) / 8;
        byte[] packed = new byte[byteCount];

        for (int i = 0; i < length; i++) {
            if (bits.charAt(i) == '1') {
                packed[i / 8] |= (1 << (7 - i % 8));
            }
        }

        return packed;
    }

    private String unpackBits(byte[] data) {
        StringBuilder bits = new StringBuilder();
        for (byte b : data) {
            for (int i = 7; i >= 0; i--) {
                bits.append((b & (1 << i)) != 0 ? '1' : '0');
            }
        }
        return bits.toString();
    }

    private void zoomIn() {
        scale *= 1.2;
        treePanel.repaint();
    }

    private void zoomOut() {
        scale /= 1.2;
        treePanel.repaint();
    }

    private void scrollUp() {
        offsetY -= 20; // Move up by 20 pixels
        treePanel.repaint();
    }

    private void scrollDown() {
        offsetY += 20; // Move down by 20 pixels
        treePanel.repaint();
    }

    private void scrollLeft() {
        offsetX -= 20; // Move left by 20 pixels
        treePanel.repaint();
    }

    private void scrollRight() {
        offsetX += 20; // Move right by 20 pixels
        treePanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HuffmanCompression().setVisible(true));
    }
}