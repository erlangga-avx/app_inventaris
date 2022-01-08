/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tampilan;

import java.awt.Dialog;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import koneksi.koneksi;

/**
 *
 * @author USER
 */
public class transaksi_bm extends javax.swing.JInternalFrame {
    
    public final Connection conn = new koneksi().connect();
    private DefaultTableModel tabmode;
    private DefaultTableModel tabmode2;
    
    private void aktif(){
        TxtIDBm.setEnabled(true);
        TxtNamaSupplierBm.setEnabled(true);
        TxtKodeSupplierBm.setEnabled(true);
    }
    
    protected void kosong(){
        tanggal();
        TxtIDBm.setText(null);
        TxtNamaSupplierBm.setText(null);
        TxtKodeSupplierBm.setText(null);
    }
    
    public void noTable(){
        int Baris = tabmode.getRowCount();
        for (int a = 0; a < Baris; a++) {
            String nomor = String.valueOf(a + 1);
            tabmode.setValueAt(nomor + ".", a, 0);
        }
    }
    
    public void noTable2() {
    int Baris = tabmode2.getRowCount();
    for (int a = 0; a < Baris; a++) {
        String nomor = String.valueOf(a + 1);
        tabmode2.setValueAt(nomor + ".", a, 0);
        }
    }
    
    public void tanggal(){
        Date tgl = new Date();
        btnTanggal.setDate(tgl);
    }
    
    public void auto_id_masuk(){
        try {
            Connection conn = new koneksi().connect();
            java.sql.Statement stat = conn.createStatement();
            Date tanggal_update = new Date();
            String tampilan = "yyMM";
            SimpleDateFormat fm = new SimpleDateFormat(tampilan);
            String tanggal = String.valueOf(fm.format(btnTanggal.getDate()));
            String sql = "select max(right(id_bm, 3)) as no from tb_brg_masuk where id_bm like'%" + "BM" + tanggal.toString() + "%'";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                if (rs.first() == false) {
                    TxtIDBm.setText("BM" + tanggal.toString() + "0001");
                } else {
                    rs.last();
                    int auto_id = rs.getInt(1) + 1;
                    String no = String.valueOf(auto_id);
                    int no_id = no.length();
                    for (int j = 0; j < 3 - no_id; j++){
                        no = "0" + no;
                    }
                     TxtIDBm.setText("BM" + tanggal.toString() + no);
                }
            }
            rs.close();
            stat.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan " + e);
        }
    }
    
    public void lebarKolom(){
        TableColumn kolom;
        TabelBm.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        kolom = TabelBm.getColumnModel().getColumn(0);
        kolom.setPreferredWidth(40);
        kolom = TabelBm.getColumnModel().getColumn(1);
        kolom.setPreferredWidth(75);
        kolom = TabelBm.getColumnModel().getColumn(2);
        kolom.setPreferredWidth(100);
        kolom = TabelBm.getColumnModel().getColumn(3);
        kolom.setPreferredWidth(150);
        kolom = TabelBm.getColumnModel().getColumn(4);
        kolom.setPreferredWidth(75);
    }
    
    public void lebarKolom02() {
    TableColumn column2;
    TabeSupplierList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    column2 = TabeSupplierList.getColumnModel().getColumn(0);
    column2.setPreferredWidth(40);
    column2 = TabeSupplierList.getColumnModel().getColumn(1);
    column2.setPreferredWidth(150);
    column2 = TabeSupplierList.getColumnModel().getColumn(2);
    column2.setPreferredWidth(200);
} 
    
    public void dataTable(){
        Object[] Baris = {"No", "Tanggal", "ID Bm", "Nama Supplier", "Kode Supplier"};
        tabmode = new DefaultTableModel(null, Baris);
        TabelBm.setModel(tabmode);
        String sql = "select * from tb_brg_masuk JOIN tb_supplier on tb_supplier.kode_supplier = tb_brg_masuk.supplier order by tb_brg_masuk.id_bm asc";
        try{
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()){
                String tanggal = hasil.getString("tanggal");
                String id_bm = hasil.getString("id_bm");
                String nama_supplier = hasil.getString("nama_supplier");
                String supplier = hasil.getString("supplier");
                String[] data = {"", tanggal, id_bm, nama_supplier, supplier};
                tabmode.addRow(data);
                noTable();
                lebarKolom();
            }
        } catch (Exception e) {
        }
    }
    
    public void dataTabel2() {
    Object[] Baris = {"No", "Kode Supplier", "Supplier"};
    tabmode2 = new DefaultTableModel(null, Baris);
    TabeSupplierList.setModel(tabmode2);
    String sql = "select * from tb_supplier order by kode_supplier asc";
    try {
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while (hasil.next()) {
            String kode_supplier = hasil.getString("kode_supplier");
            String nama_supplier = hasil.getString("nama_supplier");
            String[] data = {"", kode_supplier, nama_supplier};
            tabmode2.addRow(data);
            noTable2();
            lebarKolom02();
        }
    } catch (Exception e) {
    }
} 
     
    public void pencarian (String sql){
        Object[] Baris = {"No", "Tanggal", "ID Bm", "Nama Supplier", "Kode Supplier"};
        tabmode = new DefaultTableModel(null, Baris);
        TabelBm.setModel(tabmode);
        int brs = TabelBm.getRowCount();
        for (int i = 0;1 < brs; i++) {
            tabmode.removeRow(1);
        }
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()){
                String tanggal = hasil.getString("tanggal");
                String id_bm = hasil.getString("id_bm");
                String nama_supplier = hasil.getString("nama_supplier");
                String supplier = hasil.getString("supplier");
                String[] data = {"", tanggal, id_bm, nama_supplier, supplier};
                tabmode.addRow(data);
                noTable();
            }
        } catch (Exception e){
        }
    }
    
   public void pencarian02(String sql) {
    Object[] Baris = {"No", "Kode Supplier", "Supplier"};
    tabmode2 = new DefaultTableModel(null, Baris);
    TabeSupplierList.setModel(tabmode2);
    int brs = TabeSupplierList.getRowCount();
    for (int i = 0; 1 < brs; i++) {
        tabmode2.removeRow(1);
    }
    try {
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while (hasil.next()) {
            String kode_supplier = hasil.getString("kode_supplier");
            String nama_supplier = hasil.getString("nama_supplier");
            String[] data = {"", kode_supplier, nama_supplier};
            tabmode2.addRow(data);
            noTable2();
            lebarKolom02();
        }
    } catch (Exception e) {

    }
} 

    /**
     * Creates new form data_kategori
     */
    public transaksi_bm() {
        initComponents();
        aktif();
        tanggal();
        auto_id_masuk();
        dataTable();
        lebarKolom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ubahSupplier = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnTanggalUbah = new com.toedter.calendar.JDateChooser();
        TxtKodeSupplierUbah = new javax.swing.JTextField();
        TxtNamaSupplier = new javax.swing.JTextField();
        btnSimpanUbah = new javax.swing.JButton();
        btnCancelUbah = new javax.swing.JButton();
        listSupplier = new javax.swing.JDialog();
        jLabel11 = new javax.swing.JLabel();
        TxtCariSupplier = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        TabeSupplierList = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnTanggal = new com.toedter.calendar.JDateChooser();
        TxtIDBm = new javax.swing.JTextField();
        TxtNamaSupplierBm = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBersih = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        TxtKodeSupplierBm = new javax.swing.JTextField();
        btnKodeSupplier = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        TxtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelBm = new javax.swing.JTable();

        ubahSupplier.setSize(new java.awt.Dimension(500, 444));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Supplier"));

        jLabel6.setText("Tanggal");

        jLabel7.setText("Nama Supplier");

        jLabel8.setText("Kode Supplier");

        btnTanggalUbah.setDateFormatString("dd-MM-yyyy");

        TxtKodeSupplierUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtKodeSupplierUbahActionPerformed(evt);
            }
        });
        TxtKodeSupplierUbah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TxtKodeSupplierUbahKeyPressed(evt);
            }
        });

        btnSimpanUbah.setText("Simpan");
        btnSimpanUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanUbahActionPerformed(evt);
            }
        });

        btnCancelUbah.setText("Cancel");
        btnCancelUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelUbahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TxtKodeSupplierUbah)
                    .addComponent(btnTanggalUbah, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .addComponent(TxtNamaSupplier))
                .addGap(0, 28, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(btnSimpanUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCancelUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(btnTanggalUbah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(TxtKodeSupplierUbah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(TxtNamaSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(88, 88, 88)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSimpanUbah, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(btnCancelUbah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(111, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ubahSupplierLayout = new javax.swing.GroupLayout(ubahSupplier.getContentPane());
        ubahSupplier.getContentPane().setLayout(ubahSupplierLayout);
        ubahSupplierLayout.setHorizontalGroup(
            ubahSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 481, Short.MAX_VALUE)
            .addGroup(ubahSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ubahSupplierLayout.createSequentialGroup()
                    .addGap(28, 28, 28)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(112, Short.MAX_VALUE)))
        );
        ubahSupplierLayout.setVerticalGroup(
            ubahSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 395, Short.MAX_VALUE)
            .addGroup(ubahSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ubahSupplierLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        listSupplier.setTitle("Daftar data Kategori");
        listSupplier.setSize(new java.awt.Dimension(450, 460));

        jLabel11.setText("Cari Supplier");

        TxtCariSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TxtCariSupplierKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TxtCariSupplierKeyTyped(evt);
            }
        });

        TabeSupplierList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        TabeSupplierList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabeSupplierListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TabeSupplierList);

        javax.swing.GroupLayout listSupplierLayout = new javax.swing.GroupLayout(listSupplier.getContentPane());
        listSupplier.getContentPane().setLayout(listSupplierLayout);
        listSupplierLayout.setHorizontalGroup(
            listSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listSupplierLayout.createSequentialGroup()
                .addGroup(listSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(listSupplierLayout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(jLabel11)
                        .addGap(44, 44, 44)
                        .addComponent(TxtCariSupplier))
                    .addGroup(listSupplierLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        listSupplierLayout.setVerticalGroup(
            listSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listSupplierLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(listSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(TxtCariSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(102, 0, 102));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Data Barang");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Transaksi Barang Masuk");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(298, 298, 298)
                .addComponent(jLabel1)
                .addContainerGap(490, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Barang Masuk"));

        jLabel2.setText("Tanggal");

        jLabel3.setText("ID Bar. Masuk");

        jLabel4.setText("Nama Supplier");

        btnTanggal.setDateFormatString("dd-MM-yyyy");

        TxtIDBm.setEditable(false);
        TxtIDBm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TxtIDBmKeyPressed(evt);
            }
        });

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnUbah.setText("Ubah");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnBersih.setText("Bersih");
        btnBersih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBersihActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel9.setText("Kode Supplier");

        btnKodeSupplier.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnKodeSupplier.setText("Kode");
        btnKodeSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKodeSupplierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TxtIDBm)
                            .addComponent(btnTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                            .addComponent(TxtNamaSupplierBm)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(TxtKodeSupplierBm, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnKodeSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(3, 3, 3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapus, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)))
                .addGap(0, 28, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(btnBersih, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(btnTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(TxtIDBm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(TxtNamaSupplierBm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnKodeSupplier, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(TxtKodeSupplierBm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(71, 71, 71)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBersih, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(155, 155, 155))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabel Data Barang Masuk"));

        jLabel5.setText("Pencarian");

        TxtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TxtCariKeyTyped(evt);
            }
        });

        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        TabelBm.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        TabelBm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelBmMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TabelBm);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(184, 184, 184)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TxtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(TxtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        String sqlPencarian = "select * from tb_barang where kode_part like '%" + TxtCari.getText() + "%' or "
                + "nama_part like '%" + TxtCari.getText() + "%'";
        pencarian(sqlPencarian);
        lebarKolom();
    }//GEN-LAST:event_btnCariActionPerformed

    private void TxtIDBmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtIDBmKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            TxtNamaSupplierBm.requestFocus();
        }
    }//GEN-LAST:event_TxtIDBmKeyPressed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
       if (TxtNamaSupplierBm.getText().equals("")) {
           JOptionPane.showMessageDialog(null, "Nama Supplier tidak boleh kosong");
       } else if (TxtKodeSupplierBm.getText().equals("")) {
           JOptionPane.showMessageDialog(null, "kode Supplier tidak boleh kosong");
       }  else {
           String sql = "insert into tb_brg_masuk values (?,?,?)";
           String tampilan = "dd-MM-yyyy";
           SimpleDateFormat fm = new SimpleDateFormat(tampilan);
           String tanggal = String.valueOf(fm.format(btnTanggal.getDate()));
           try {
               PreparedStatement stat = conn.prepareStatement(sql);
               stat.setString(1, tanggal.toString());
               stat.setString(2, TxtIDBm.getText());
               stat.setString(3, TxtKodeSupplierBm.getText());
               stat.executeUpdate();
               JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
               kosong();
               dataTable();
               lebarKolom();
               TxtKodeSupplierBm.requestFocus();
           } catch (SQLException e) {
               JOptionPane.showMessageDialog(null, "Data gagal disimpan" + e);
           }
       }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void TabelBmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelBmMouseClicked
        int bar = TabelBm.getSelectedRow();
        String a = tabmode.getValueAt(bar, 0).toString();
        String b = tabmode.getValueAt(bar, 1).toString();
        String c = tabmode.getValueAt(bar, 2).toString();
        String d = tabmode.getValueAt(bar, 3).toString();
        String e = tabmode.getValueAt(bar, 4).toString();
        
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        Date dateValue = null;
        try {
            dateValue = date.parse((String) TabelBm.getValueAt(bar, 1));
        } catch (ParseException ex) {
            Logger.getLogger(transaksi_bm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        btnTanggal.setDate(dateValue);
        btnTanggalUbah.setDate(dateValue);
        TxtIDBm.setText(c);
        TxtKodeSupplierBm.setText(d);
        TxtNamaSupplierBm.setText(e);
        TxtNamaSupplier.setText(e);
    }//GEN-LAST:event_TabelBmMouseClicked

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        ubahSupplier.setLocationRelativeTo(this);
        TxtKodeSupplierUbah.setEditable(false);
        ubahSupplier.setVisible(true);
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnBersihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBersihActionPerformed
        tanggal();
        kosong();
    }//GEN-LAST:event_btnBersihActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "Apakah anda yakin ingin "+ "Menghapus Data", "Konfirmasi Dialog", JOptionPane.YES_NO_OPTION);
        if (ok == 0) {
            String sql = "delete from tb_brg_masuk where id_bm='" + TxtIDBm.getText() + "'";
            try{
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
                kosong();
                dataTable();
                lebarKolom();
                TxtKodeSupplierBm.requestFocus();
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Data gagal dihapus" + e);
            }
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void TxtCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtCariKeyTyped
        String sqlPencarian = "select * from tb_brg_masuk join tb_supplier on tb_supplier.kode_supplier = tb_brg_masuk.kode_supplier"
                + "where tb_brg_masuk.id_supplier like '&"+ TxtCari.getText() +"&' or"
                + "tb_brg_masuk.kode_supplier like '&"+ TxtCari.getText() +"&' or"
                + "tb_supplier.nama_supplier like '&"+ TxtCari.getText() +"&'";
        pencarian (sqlPencarian);
        lebarKolom();
    }//GEN-LAST:event_TxtCariKeyTyped

    private void btnKodeSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKodeSupplierActionPerformed
        dataTabel2();
        lebarKolom02();
        listSupplier.setLocationRelativeTo(this);
        listSupplier.setVisible(true);
    }//GEN-LAST:event_btnKodeSupplierActionPerformed

    private void TxtCariSupplierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtCariSupplierKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtCariSupplierKeyPressed

    private void TxtCariSupplierKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtCariSupplierKeyTyped
        String sqlPencarian2 = "select * from tb_supplier where kode_supplier like '%" + TxtCariSupplier.getText() + "%' or "
                + "nama_supplier like '%" + TxtCariSupplier.getText() + "%'";
        pencarian02(sqlPencarian2);
        lebarKolom02();
    }//GEN-LAST:event_TxtCariSupplierKeyTyped

    private void TabeSupplierListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabeSupplierListMouseClicked
       int bar = TabeSupplierList.getSelectedRow();
       String a = tabmode2.getValueAt(bar, 0).toString();
       String b = tabmode2.getValueAt(bar, 1).toString();
       String c = tabmode2.getValueAt(bar, 2).toString();
       
       TxtCariSupplier.setText(c);
       TxtKodeSupplierBm.setText(b);
        TxtNamaSupplierBm.setText(c);
        TxtNamaSupplier.setText(c);
       listSupplier.dispose();
    }//GEN-LAST:event_TabeSupplierListMouseClicked

    private void btnCancelUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelUbahActionPerformed
        ubahSupplier.setVisible(false);
    }//GEN-LAST:event_btnCancelUbahActionPerformed

    private void btnSimpanUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanUbahActionPerformed
        String sql = "update tb_supplier set tanggal=?, kode_supplier=?,nama_supplier=? where kode_supplier='" + TxtKodeSupplierUbah.getText() + "'";
        String tampilan = "dd-MM-yyyy";
        SimpleDateFormat fm = new SimpleDateFormat(tampilan);
        String tanggal = String.valueOf(fm.format(btnTanggalUbah.getDate()));
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, tanggal.toString());
            stat.setString(2, TxtIDBm.getText());
            stat.setString(3, TxtNamaSupplierBm.getText());
            stat.setString(4, TxtKodeSupplierBm.getText());
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
            kosong();
            dataTable();
            lebarKolom();
            TxtIDBm.requestFocus();
            ubahSupplier.dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah" + e);
        }
    }//GEN-LAST:event_btnSimpanUbahActionPerformed

    private void TxtKodeSupplierUbahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtKodeSupplierUbahKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtKodeSupplierUbahKeyPressed

    private void TxtKodeSupplierUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtKodeSupplierUbahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtKodeSupplierUbahActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TabeSupplierList;
    private javax.swing.JTable TabelBm;
    private javax.swing.JTextField TxtCari;
    private javax.swing.JTextField TxtCariSupplier;
    private javax.swing.JTextField TxtIDBm;
    private javax.swing.JTextField TxtKodeSupplierBm;
    private javax.swing.JTextField TxtKodeSupplierUbah;
    private javax.swing.JTextField TxtNamaSupplier;
    private javax.swing.JTextField TxtNamaSupplierBm;
    private javax.swing.JButton btnBersih;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCancelUbah;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKodeSupplier;
    private javax.swing.JButton btnSimpanUbah;
    private javax.swing.JButton btnTambah;
    private com.toedter.calendar.JDateChooser btnTanggal;
    private com.toedter.calendar.JDateChooser btnTanggalUbah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JDialog listSupplier;
    private javax.swing.JDialog ubahSupplier;
    // End of variables declaration//GEN-END:variables
}
