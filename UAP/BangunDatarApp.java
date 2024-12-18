package UAP;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

// Model untuk menyimpan data bangun datar
class BangunDatar {
    String jenis;
    double nilai;
    double luas;
    double keliling;

    public BangunDatar(String jenis, double nilai, double luas, double keliling) {
        this.jenis = jenis;
        this.nilai = nilai;
        this.luas = luas;
        this.keliling = keliling;
    }

    // Method untuk mengembalikan data sebagai array untuk tabel
    public Object[] toTableRow() {
        return new Object[]{jenis, nilai, luas, keliling};
    }
}

// Kelas utama
public class BangunDatarApp extends JFrame {

    // Komponen GUI
    private JTextField inputField;
    private JLabel resultLabel, imageLabel;
    private JComboBox<String> bangunDatarComboBox;
    private JButton hitungButton, simpanButton, lihatButton, hapusButton, loadImageButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private List<BangunDatar> bangunDatarList = new ArrayList<>();
    private BufferedImage image;  // Gambar bangun datar yang dipilih

    public BangunDatarApp() {
        // Pengaturan Frame
        setTitle("Menghitung Bangun Datar");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        // Label untuk memilih bangun datar
        JLabel selectLabel = new JLabel("Pilih Bangun Datar:");
        selectLabel.setBounds(10, 20, 150, 25);
        panel.add(selectLabel);

        String[] bangunDatar = {"Persegi", "Lingkaran", "Segitiga"};
        bangunDatarComboBox = new JComboBox<>(bangunDatar);
        bangunDatarComboBox.setBounds(180, 20, 150, 25);
        panel.add(bangunDatarComboBox);

        // Label untuk input nilai
        JLabel inputLabel = new JLabel("Masukkan Nilai:");
        inputLabel.setBounds(10, 60, 150, 25);
        panel.add(inputLabel);

        inputField = new JTextField(20);
        inputField.setBounds(180, 60, 150, 25);
        panel.add(inputField);

        // Tombol Hitung
        hitungButton = new JButton("Hitung");
        hitungButton.setBounds(10, 100, 150, 25);
        panel.add(hitungButton);

        // Tombol Simpan
        simpanButton = new JButton("Simpan Hasil");
        simpanButton.setBounds(180, 100, 150, 25);
        panel.add(simpanButton);

        // Tombol Lihat Hasil
        lihatButton = new JButton("Lihat Hasil");
        lihatButton.setBounds(180, 140, 150, 25);
        panel.add(lihatButton);

        // Tombol Hapus
        hapusButton = new JButton("Hapus Hasil");
        hapusButton.setBounds(180, 180, 150, 25);
        panel.add(hapusButton);

        // Tombol Load Image
        loadImageButton = new JButton("Load Gambar");
        loadImageButton.setBounds(10, 140, 150, 25);
        panel.add(loadImageButton);

        // Label untuk hasil
        resultLabel = new JLabel("Hasil:");
        resultLabel.setBounds(10, 220, 300, 25);
        panel.add(resultLabel);

        // Setup Tabel untuk hasil
        String[] columnNames = {"Jenis", "Nilai", "Luas", "Keliling"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        tableScrollPane.setBounds(10, 220, 550, 200);
        panel.add(tableScrollPane);

        // Label untuk menampilkan gambar
        imageLabel = new JLabel();
        imageLabel.setBounds(10, 440, 550, 100);  // Sesuaikan ukuran label untuk gambar
        panel.add(imageLabel);

        // Event untuk tombol Hitung
        hitungButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Mendapatkan bangun datar yang dipilih
                    String pilihan = (String) bangunDatarComboBox.getSelectedItem();

                    // Membaca nilai input dari pengguna
                    double input = Double.parseDouble(inputField.getText());
                    if (input <= 0) {
                        throw new NumberFormatException();
                    }

                    // Variabel untuk menyimpan hasil
                    double luas = 0;
                    double keliling = 0;

                    // Perhitungan berdasarkan pilihan bangun datar
                    switch (pilihan) {
                        case "Persegi":
                            luas = input * input;
                            keliling = 4 * input;
                            break;
                        case "Lingkaran":
                            luas = Math.PI * input * input;
                            keliling = 2 * Math.PI * input;
                            break;
                        case "Segitiga":
                            double tinggi = inputField.getText().isEmpty() ? 0 : Double.parseDouble(JOptionPane.showInputDialog("Masukkan Tinggi Segitiga:"));
                            if (tinggi <= 0) {
                                throw new NumberFormatException();
                            }
                            luas = 0.5 * input * tinggi;
                            keliling = 3 * input;  // Asumsi segitiga sama sisi
                            break;
                    }

                    // Menampilkan hasil
                    resultLabel.setText(String.format("Luas: %.2f, Keliling: %.2f", luas, keliling));
                    // Menyimpan hasil perhitungan ke dalam list
                    BangunDatar bangun = new BangunDatar(pilihan, input, luas, keliling);
                    bangunDatarList.add(bangun);
                } catch (NumberFormatException ex) {
                    // Menangani input yang tidak valid
                    JOptionPane.showMessageDialog(null, "Input harus angka positif dan valid!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Event untuk tombol Simpan
        simpanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Menyimpan hasil perhitungan ke tabel
                BangunDatar bangun = new BangunDatar(
                        (String) bangunDatarComboBox.getSelectedItem(),
                        Double.parseDouble(inputField.getText()),
                        Double.parseDouble(resultLabel.getText().split(" ")[1].split(":")[1]),
                        Double.parseDouble(resultLabel.getText().split(" ")[2].split(":")[1])
                );
                bangunDatarList.add(bangun);
                tableModel.addRow(bangun.toTableRow()); // Menambahkan baris ke tabel
                JOptionPane.showMessageDialog(null, "Hasil berhasil disimpan!");
            }
        });

        // Event untuk tombol Lihat Hasil
        lihatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Menampilkan semua hasil yang disimpan dalam tabel
                tableModel.setRowCount(0);  // Clear tabel terlebih dahulu
                for (BangunDatar bd : bangunDatarList) {
                    tableModel.addRow(bd.toTableRow()); // Menambah data ke tabel
                }
            }
        });

        // Event untuk tombol Hapus
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = resultTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Menghapus baris yang dipilih
                    tableModel.removeRow(selectedRow);
                    bangunDatarList.remove(selectedRow); // Menghapus dari list juga
                    JOptionPane.showMessageDialog(null, "Hasil berhasil dihapus!");
                } else {
                    JOptionPane.showMessageDialog(null, "Pilih hasil yang ingin dihapus!", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Event untuk tombol Load Image
        loadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBangun = (String) bangunDatarComboBox.getSelectedItem();
                String imagePath = "";

                // Menentukan gambar berdasarkan bangun datar yang dipilih
                switch (selectedBangun) {
                    case "Persegi":
                        imagePath = "path/to/persegi_image.png";
                        break;
                    case "Lingkaran":
                        imagePath = "path/to/lingkaran_image.png";
                        break;
                    case "Segitiga":
                        imagePath = "path/to/segitiga_image.png";
                        break;
                }

                // Memuat gambar jika ada
                try {
                    image = ImageIO.read(new File(imagePath));
                    imageLabel.setIcon(new ImageIcon(image));  // Menampilkan gambar
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Gambar tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Main method untuk menjalankan aplikasi
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BangunDatarApp().setVisible(true);
            }
        });
    }
}
