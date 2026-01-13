# Text Compression and Decompression Using Huffman Encoding

📌 **Project Overview**

This project is a Java-based desktop application that performs text compression and decompression using the Huffman Encoding algorithm. It is designed to reduce the size of text data without losing any information (lossless compression).

The application demonstrates how data compression works at a low level by encoding characters into binary codes based on their frequency of occurrence.

🎯 **Objective**

The main objectives of this project are:

- To understand and implement Huffman Encoding
- To reduce file size using lossless compression
- To learn how binary data is written and read using bit streams
- To apply data structures (trees, priority queues) in a real-world problem

🧠 **What is Huffman Encoding?**

Huffman Encoding is a greedy compression algorithm that assigns:

- Shorter binary codes to frequently occurring characters
- Longer binary codes to less frequent characters

This results in:

- Reduced file size
- No loss of original data during decompression

🖥️ **Type of Application**

- Desktop Application
- Built using Java
- Uses Java Swing for user interaction (GUI-based)
- Runs locally on the user's system

⚠️ This is NOT a web application.

🗂️ **Project Structure**

```
Text-Compression-Decompression-Using-Huffman-Encoding
│
├── BitInputStream.java
├── BitOutputStream.java
├── HuffmanCompression.java
├── HuffmanNode.java
├── README.md
├── out/ (compiled files)
└── .idea/ (IDE configuration)
```

📄 **File Description**

🔹 **HuffmanNode.java**

Represents a node in the Huffman Tree

Stores:

- Character
- Frequency
- Left and right child nodes

Used to build the binary tree for encoding

🔹 **HuffmanCompression.java**

This is the core file of the project.
It handles:

- Reading input text
- Calculating character frequencies
- Building the Huffman Tree
- Generating Huffman codes
- Compressing the text
- Decompressing the compressed file back to original form

🔹 **BitOutputStream.java**

Writes individual bits to an output file

- Used during compression
- Helps store binary Huffman codes efficiently

🔹 **BitInputStream.java**

Reads individual bits from a compressed file

- Used during decompression
- Converts binary data back to readable text

🔄 **Working of the Project**

🔹 **Compression Process**

1. Read the input text file
2. Count frequency of each character
3. Build Huffman Tree using a priority queue
4. Generate binary codes for characters
5. Replace characters with binary codes
6. Write compressed data using bit streams

🔹 **Decompression Process**

1. Read the compressed binary file
2. Rebuild the Huffman Tree
3. Decode binary data using the tree
4. Recover the original text exactly

✔ No data loss occurs

⚙️ **Technologies Used**

- Java
- Java Swing
- Data Structures
  - Priority Queue
  - Binary Tree
- File Handling
- Bit-level I/O

🚀 **How to Run the Project**

**Prerequisites**

- Java JDK installed (version 8 or above)
- Any Java IDE (IntelliJ IDEA / Eclipse / VS Code)

**Steps**

1. Clone the repository

   ```
   git clone https://github.com/Aditya-2026/Text-Compression-Decompression-Using-Huffman-Encoding.git
   ```

2. Open the project in your IDE

3. Compile and run `HuffmanCompression.java`

4. Provide input text file

5. View compressed and decompressed output

✅ **Features**

- Lossless text compression
- Efficient storage using bit-level operations
- Accurate decompression
- Clean and modular code structure
- Real-world application of greedy algorithms

📈 **Applications**

- File compression utilities
- Data transmission optimization
- Storage optimization
- Learning compression algorithms
- Academic and research purposes

🧪 **Limitations**

- Works only for text files
- No direct browser support (desktop-only)
- Compression efficiency depends on text content

👨‍🎓 **Academic Relevance**

This project is ideal for:

- Data Structures and Algorithms (DSA)
- File Handling concepts
- Java programming practice
- Mini-project / academic submission

📝 **Conclusion**

This project successfully demonstrates how Huffman Encoding can be used to compress and decompress text efficiently using Java. It combines algorithmic concepts with practical file handling and provides a strong foundation for understanding real-world compression techniques.

👤 **Author**

Aditya Dahake
