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
public class transaksi_bk extends javax.swing.JInternalFrame {
    
    public final Connection conn = new koneksi().connect();
    private DefaultTableModel tabmode;
    private DefaultTableModel tabmode2;
    
    private void aktif(){
        TxtIDBk.setEnabled(true);
        TxtNamaGudangBk.setEnabled(true);
        TxtKodeGudangBk.setEnabled(true);
    }
    
    protected void kosong(){
        tanggal();
        TxtIDBk.setText(null);
        TxtNamaGudangBk.setText(null);
        TxtKodeGudangBk.setText(null);
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
            String sql = "select max(right(id_bk, 3)) as no from tb_brg_keluar where id_bk like'%" + "BK" + tanggal.toString() + "%'";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                if (rs.first() == false) {
                    TxtIDBk.setText("BK" + tanggal.toString() + "0001");
                } else {
                    rs.last();
                    int auto_id = rs.getInt(1) + 1;
                    String no = String.valueOf(auto_id);
                    int no_id = no.length();
                    for (int j = 0; j < 3 - no_id; j++){
                        no = "0" + no;
                    }
                     TxtIDBk.setText("BK" + tanggal.toString() + no);
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
        TabelBk.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        kolom = TabelBk.getColumnModel().getColumn(0);
        kolom.setPreferredWidth(40);
        kolom = TabelBk.getColumnModel().getColumn(1);
        kolom.setPreferredWidth(75);
        kolom = TabelBk.getColumnModel().getColumn(2);
        kolom.setPreferredWidth(100);
        kolom = TabelBk.getColumnModel().getColumn(3);
        kolom.setPreferredWidth(150);
        kolom = TabelBk.getColumnModel().getColumn(4);
        kolom.setPreferredWidth(75);
    }
    
    public void lebarKolom02() {
    TableColumn column2;
    TabeGudangList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    column2 = TabeGudangList.getColumnModel().getColumn(0);
    column2.setPreferredWidth(40);
    column2 = TabeGudangList.getColumnModel().getColumn(1);
    column2.setPreferredWidth(150);
    column2 = TabeGudangList.getColumnModel().getColumn(2);
    column2.setPreferredWidth(200);
} 
    
    public void dataTable(){
        Object[] Baris = {"No", "Tanggal", "ID Bk", "Nama Gudang", "Kode Gudang"};
        tabmode = new DefaultTableModel(null, Baris);
        TabelBk.setModel(tabmode);
        String sql = "select * from tb_brg_keluar JOIN tb_gudang on tb_gudang.kode_gudang = tb_brg_keluar.gudang order by tb_brg_keluar.id_bk asc";
        try{
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()){
                String tanggal = hasil.getString("tanggal");
                String id_bk = hasil.getString("id_bk");
                String nama_gudang = hasil.getString("nama_gudang");
                String gudang = hasil.getString("gudang");
                String[] data = {"", tanggal, id_bk, nama_gudang, gudang};
                tabmode.addRow(data);
                noTable();
                lebarKolom();
            }
        } catch (Exception e) {
        }
    }
    
    public void dataTabel2() {
    Object[] Baris = {"No", "Kode Gudang", "Gudang"};
    tabmode2 = new DefaultTableModel(null, Baris);
    TabeGudangList.setModel(tabmode2);
    String sql = "select * from tb_gudang order by kode_gudang asc";
    try {
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while (hasil.next()) {
            String kode_gudang = hasil.getString("kode_gudang");
            String nama_gudang = hasil.getString("nama_gudang");
            String[] data = {"", kode_gudang, nama_gudang};
            tabmode2.addRow(data);
            noTable2();
            lebarKolom02();
        }
    } catch (Exception e) {
    }
} 
     
    public void pencarian (String sql){
        Object[] Baris = {"No", "Tanggal", "ID Bk", "Nama Gudang", "Kode Gudang"};
        tabmode = new DefaultTableModel(null, Baris);
        TabelBk.setModel(tabmode);
        int brs = TabelBk.getRowCount();
        for (int i = 0;1 < brs; i++) {
            tabmode.removeRow(1);
        }
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()){
                String tanggal = hasil.getString("tanggal");
                String id_bk = hasil.getString("id_bk");
                String nama_gudang = hasil.getString("nama_gudang");
                String gudang = hasil.getString("gudang");
                String[] data = {"", tanggal, id_bk, nama_gudang, gudang};
                tabmode.addRow(data);
                noTable();
            }
        } catch (Exception e){
        }
    }
    
   public void pencarian02(String sql) {
    Object[] Baris = {"No", "Kode Gudang", "Gudang"};
    tabmode2 = new DefaultTableModel(null, Baris);
    TabeGudangList.setModel(tabmode2);
    int brs = TabeGudangList.getRowCount();
    for (int i = 0; 1 < brs; i++) {
        tabmode2.removeRow(1);
    }
    try {
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while (hasil.next()) {
            String kode_gudang = hasil.getString("kode_gudang");
            String nama_gudang = hasil.getString("nama_gudang");
            String[] data = {"", kode_gudang, nama_gudang};
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
    public transaksi_bk() {
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

        ubahGudang = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnTanggalUbah = new com.toedter.calendar.JDateChooser();
        TxtKodeGudangUbah = new javax.swing.JTextField();
        TxtNamaGudangUbah = new javax.swing.JTextField();
        btnSimpanUbah = new javax.swing.JButton();
        btnCancelUbah = new javax.swing.JButton();
        listGudang = new javax.swing.JDialog();
        jLabel11 = new javax.swing.JLabel();
        TxtCariGudang = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        TabeGudangList = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnTanggal = new com.toedter.calendar.JDateChooser();
        TxtIDBk = new javax.swing.JTextField();
        TxtNamaGudangBk = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBersih = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        TxtKodeGudangBk = new javax.swing.JTextField();
        btnKodeGudang = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        TxtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelBk = new javax.swing.JTable();

        ubahGudang.setSize(new java.awt.Dimension(500, 444));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Gudang"));

        jLabel6.setText("Tanggal");

        jLabel7.setText("Kode Gudang");

        jLabel8.setText("Nama Gudang");

        btnTanggalUbah.setDateFormatString("dd-MM-yyyy");

        TxtKodeGudangUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtKodeGudangUbahActionPerformed(evt);
            }
        });
        TxtKodeGudangUbah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TxtKodeGudangUbahKeyPressed(evt);
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
                    .addComponent(TxtKodeGudangUbah)
                    .addComponent(btnTanggalUbah, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .addComponent(TxtNamaGudangUbah))
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
                    .addComponent(TxtKodeGudangUbah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(TxtNamaGudangUbah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(88, 88, 88)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSimpanUbah, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(btnCancelUbah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(111, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ubahGudangLayout = new javax.swing.GroupLayout(ubahGudang.getContentPane());
        ubahGudang.getContentPane().setLayout(ubahGudangLayout);
        ubahGudangLayout.setHorizontalGroup(
            ubahGudangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 481, Short.MAX_VALUE)
            .addGroup(ubahGudangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ubahGudangLayout.createSequentialGroup()
                    .addGap(28, 28, 28)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(113, Short.MAX_VALUE)))
        );
        ubahGudangLayout.setVerticalGroup(
            ubahGudangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 395, Short.MAX_VALUE)
            .addGroup(ubahGudangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ubahGudangLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        listGudang.setTitle("Daftar data Kategori");
        listGudang.setSize(new java.awt.Dimension(450, 460));

        jLabel11.setText("Cari Gudang");

        TxtCariGudang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TxtCariGudangKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TxtCariGudangKeyTyped(evt);
            }
        });

        TabeGudangList.setModel(new javax.swing.table.DefaultTableModel(
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
        TabeGudangList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabeGudangListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TabeGudangList);

        javax.swing.GroupLayout listGudangLayout = new javax.swing.GroupLayout(listGudang.getContentPane());
        listGudang.getContentPane().setLayout(listGudangLayout);
        listGudangLayout.setHorizontalGroup(
            listGudangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listGudangLayout.createSequentialGroup()
                .addGroup(listGudangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(listGudangLayout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(jLabel11)
                        .addGap(44, 44, 44)
                        .addComponent(TxtCariGudang))
                    .addGroup(listGudangLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        listGudangLayout.setVerticalGroup(
            listGudangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listGudangLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(listGudangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(TxtCariGudang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(102, 0, 102));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Data Barang");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Transaksi Barang Keluar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(298, 298, 298)
                .addComponent(jLabel1)
                .addContainerGap(491, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Barang Keluar"));

        jLabel2.setText("Tanggal");

        jLabel3.setText("ID Bar. Keluar");

        jLabel4.setText("Nama Gudang");

        btnTanggal.setDateFormatString("dd-MM-yyyy");

        TxtIDBk.setEditable(false);
        TxtIDBk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TxtIDBkKeyPressed(evt);
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

        jLabel9.setText("Kode Gudang");

        btnKodeGudang.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnKodeGudang.setText("Kode");
        btnKodeGudang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKodeGudangActionPerformed(evt);
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
                            .addComponent(TxtIDBk)
                            .addComponent(btnTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                            .addComponent(TxtNamaGudangBk)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(TxtKodeGudangBk, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnKodeGudang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                    .addComponent(TxtIDBk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(TxtNamaGudangBk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnKodeGudang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(TxtKodeGudangBk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabel Data Barang Keluar"));

        jLabel5.setText("Pencarian");

        TxtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtCariActionPerformed(evt);
            }
        });
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

        TabelBk.setModel(new javax.swing.table.DefaultTableModel(
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
        TabelBk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelBkMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TabelBk);

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

    private void TxtIDBkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtIDBkKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            TxtNamaGudangBk.requestFocus();
        }
    }//GEN-LAST:event_TxtIDBkKeyPressed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
       if (TxtNamaGudangBk.getText().equals("")) {
           JOptionPane.showMessageDialog(null, "Nama Gudang tidak boleh kosong");
       } else if (TxtKodeGudangBk.getText().equals("")) {
           JOptionPane.showMessageDialog(null, "kode Gudang tidak boleh kosong");
       }  else {
           String sql = "insert into tb_brg_keluar values (?,?,?)";
           String tampilan = "dd-MM-yyyy";
           SimpleDateFormat fm = new SimpleDateFormat(tampilan);
           String tanggal = String.valueOf(fm.format(btnTanggal.getDate()));
           try {
               PreparedStatement stat = conn.prepareStatement(sql);
               stat.setString(1, tanggal.toString());
               stat.setString(2, TxtIDBk.getText());
               stat.setString(3, TxtKodeGudangBk.getText());
               stat.executeUpdate();
               JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
               kosong();
               dataTable();
               lebarKolom();
               TxtKodeGudangBk.requestFocus();
           } catch (SQLException e) {
               JOptionPane.showMessageDialog(null, "Data gagal disimpan" + e);
           }
       }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void TabelBkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelBkMouseClicked
        int bar = TabelBk.getSelectedRow();
        String a = tabmode.getValueAt(bar, 0).toString();
        String b = tabmode.getValueAt(bar, 1).toString();
        String c = tabmode.getValueAt(bar, 2).toString();
        String d = tabmode.getValueAt(bar, 3).toString();
        String e = tabmode.getValueAt(bar, 4).toString();
        
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        Date dateValue = null;
        try {
            dateValue = date.parse((String) TabelBk.getValueAt(bar, 1));
        } catch (ParseException ex) {
            Logger.getLogger(transaksi_bk.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        btnTanggal.setDate(dateValue);
        btnTanggalUbah.setDate(dateValue);
        TxtIDBk.setText(c);
        TxtKodeGudangBk.setText(d);
        TxtNamaGudangBk.setText(e);
        TxtNamaGudangUbah.setText(e);
    }//GEN-LAST:event_TabelBkMouseClicked

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        ubahGudang.setLocationRelativeTo(this);
        TxtKodeGudangUbah.setEditable(false);
        ubahGudang.setVisible(true);
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnBersihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBersihActionPerformed
        tanggal();
        kosong();
    }//GEN-LAST:event_btnBersihActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "Apakah anda yakin ingin "+ "Menghapus Data", "Konfirmasi Dialog", JOptionPane.YES_NO_OPTION);
        if (ok == 0) {
            String sql = "delete from tb_brg_keluar where id_bk='" + TxtIDBk.getText() + "'";
            try{
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
                kosong();
                dataTable();
                lebarKolom();
                TxtKodeGudangBk.requestFocus();
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Data gagal dihapus" + e);
            }
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void TxtCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtCariKeyTyped
        String sqlPencarian = "select * from tb_brg_keluar join tb_gudang on tb_gudang.kode_gudang = tb_brg_keluar.kode_gudang"
                + "where tb_brg_keluar.id_gudang like '&"+ TxtCari.getText() +"&' or"
                + "tb_brg_keluar.kode_gudang like '&"+ TxtCari.getText() +"&' or"
                + "tb_gudang.nama_gudang like '&"+ TxtCari.getText() +"&'";
        pencarian (sqlPencarian);
        lebarKolom();
    }//GEN-LAST:event_TxtCariKeyTyped

    private void btnKodeGudangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKodeGudangActionPerformed
        dataTabel2();
        lebarKolom02();
        listGudang.setLocationRelativeTo(this);
        listGudang.setVisible(true);
    }//GEN-LAST:event_btnKodeGudangActionPerformed

    private void TxtCariGudangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtCariGudangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtCariGudangKeyPressed

    private void TxtCariGudangKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtCariGudangKeyTyped
        String sqlPencarian2 = "select * from tb_gudang where kode_gudang like '%" + TxtCariGudang.getText() + "%' or "
                + "nama_gudang like '%" + TxtCariGudang.getText() + "%'";
        pencarian02(sqlPencarian2);
        lebarKolom02();
    }//GEN-LAST:event_TxtCariGudangKeyTyped

    private void TabeGudangListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabeGudangListMouseClicked
       int bar = TabeGudangList.getSelectedRow();
       String a = tabmode2.getValueAt(bar, 0).toString();
       String b = tabmode2.getValueAt(bar, 1).toString();
       String c = tabmode2.getValueAt(bar, 2).toString();
       
       TxtCariGudang.setText(c);
       TxtKodeGudangBk.setText(b);
        TxtNamaGudangBk.setText(c);
        TxtNamaGudangUbah.setText(c);
       listGudang.dispose();
    }//GEN-LAST:event_TabeGudangListMouseClicked

    private void btnCancelUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelUbahActionPerformed
        ubahGudang.setVisible(false);
    }//GEN-LAST:event_btnCancelUbahActionPerformed

    private void btnSimpanUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanUbahActionPerformed
        String sql = "update tb_gudang set tanggal=?, kode_gudang=?,nama_gudang=? where kode_gudang='" + TxtKodeGudangUbah.getText() + "'";
        String tampilan = "dd-MM-yyyy";
        SimpleDateFormat fm = new SimpleDateFormat(tampilan);
        String tanggal = String.valueOf(fm.format(btnTanggalUbah.getDate()));
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, tanggal.toString());
            stat.setString(2, TxtIDBk.getText());
            stat.setString(3, TxtNamaGudangBk.getText());
            stat.setString(4, TxtKodeGudangBk.getText());
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
            kosong();
            dataTable();
            lebarKolom();
            TxtIDBk.requestFocus();
            ubahGudang.dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah" + e);
        }
    }//GEN-LAST:event_btnSimpanUbahActionPerformed

    private void TxtKodeGudangUbahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtKodeGudangUbahKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtKodeGudangUbahKeyPressed

    private void TxtKodeGudangUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtKodeGudangUbahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtKodeGudangUbahActionPerformed

    private void TxtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtCariActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TabeGudangList;
    private javax.swing.JTable TabelBk;
    private javax.swing.JTextField TxtCari;
    private javax.swing.JTextField TxtCariGudang;
    private javax.swing.JTextField TxtIDBk;
    private javax.swing.JTextField TxtKodeGudangBk;
    private javax.swing.JTextField TxtKodeGudangUbah;
    private javax.swing.JTextField TxtNamaGudangBk;
    private javax.swing.JTextField TxtNamaGudangUbah;
    private javax.swing.JButton btnBersih;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCancelUbah;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKodeGudang;
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
    private javax.swing.JDialog listGudang;
    private javax.swing.JDialog ubahGudang;
    // End of variables declaration//GEN-END:variables
}
