import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.zip.*;

public class FileCompressorDecompressor extends JFrame implements ActionListener {

    private final JButton compressBtn;
    private final JButton decompressBtn;
    private final JLabel statusLbl;
    private final JFileChooser fileChooser;

    public FileCompressorDecompressor() {
        setTitle("File Compressor/Decompressor");
        setSize(400, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        compressBtn = new JButton("Compress File");
        compressBtn.addActionListener(this);

        decompressBtn = new JButton("Decompress File");
        decompressBtn.addActionListener(this);

        statusLbl = new JLabel("Select a file to compress/decompress.");

        panel.add(compressBtn);
        panel.add(decompressBtn);
        panel.add(statusLbl);

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));

        add(panel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == compressBtn) {
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File inputFile = fileChooser.getSelectedFile();
                File compressedFile = new File(inputFile.getAbsolutePath() + ".compressed");
                compress(inputFile, compressedFile);
                statusLbl.setText("File compressed successfully.");
            }
        } else if (e.getSource() == decompressBtn) {
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File compressedFile = fileChooser.getSelectedFile();
                String filename = compressedFile.getAbsolutePath();
                if (filename.endsWith(".compressed")) {
                    filename = filename.substring(0, filename.lastIndexOf('.'));
                }
                File decompressedFile = new File(filename);
                decompress(compressedFile, decompressedFile);
                statusLbl.setText("File decompressed successfully.");
            }
        }
    }

    private void compress(File inputFile, File compressedFile) {
        try {
            FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream outputStream = new FileOutputStream(compressedFile);
            DeflaterOutputStream compressor = new DeflaterOutputStream(outputStream);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                compressor.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            compressor.finish();
            compressor.close();
            outputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void decompress(File compressedFile, File decompressedFile) {
        try {
            FileInputStream inputStream = new FileInputStream(compressedFile);
            InflaterInputStream decompressor = new InflaterInputStream(inputStream);
            FileOutputStream outputStream = new FileOutputStream(decompressedFile);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = decompressor.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            decompressor.close();
            outputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new FileCompressorDecompressor();
    }
}