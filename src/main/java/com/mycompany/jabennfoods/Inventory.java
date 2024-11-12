package com.mycompany.jabennfoods;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public final class Inventory extends javax.swing.JFrame {
    File file = null;
    byte[] image = null;
    private String imagePath;
   
    public Inventory() {
        initComponents();
        Connect();
        LoadMeal();
        LoadData();
    }

    Connection connection; 
    PreparedStatement pst;
    ResultSet rs;

    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Inventory", "root", "");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void LoadMeal(){
        try {
            pst = connection.prepareStatement("SELECT ID FROM Inventory");
            rs = pst.executeQuery();
            MealId.removeAllItems();
            
            while(rs.next()) {
                MealId.addItem(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void LoadData() {
        try {
            pst = connection.prepareStatement("SELECT * FROM Inventory");
            rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            int q = rss.getColumnCount();

            DefaultTableModel table = (DefaultTableModel) InventoryTable.getModel();
            table.setRowCount(0);

            while (rs.next()) {
                Vector vector = new Vector();
                for (int tableValues = 1; tableValues <= q; tableValues++) {
                    // Retrieve data using the column index (tableValues)
                    vector.add(rs.getString(tableValues));
                }
                table.addRow(vector);
            }
            centerData();

        } catch (SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void updateMealIds() {
        try {
            // Select all IDs from the database
            pst = connection.prepareStatement("SELECT ID FROM Inventory");
            rs = pst.executeQuery();

            // Clear and update the combo box with the new IDs
            MealId.removeAllItems();
            while (rs.next()) {
                MealId.addItem(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void updateRemainingIds(int deletedId) throws SQLException {
        // Update the IDs of remaining records
        pst = connection.prepareStatement("UPDATE Inventory SET ID = ID - 1 WHERE ID > ?");
        pst.setInt(1, deletedId);
        pst.executeUpdate();
    }
    public void centerData() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int columnIndex = 0; columnIndex < InventoryTable.getColumnCount(); columnIndex++) {
            InventoryTable.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        Top = new javax.swing.JPanel();
        LogOutButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        MealName = new javax.swing.JTextField();
        Qyt = new javax.swing.JTextField();
        Price = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        MealId = new javax.swing.JComboBox<>();
        SearchButton = new javax.swing.JButton();
        AddButton = new javax.swing.JButton();
        UpdateButton = new javax.swing.JButton();
        DeleteButton = new javax.swing.JButton();
        Photo = new javax.swing.JLabel();
        BrowseButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        InventoryTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inventory");

        jPanel2.setBackground(new java.awt.Color(45, 47, 60));

        Top.setBackground(new java.awt.Color(255, 255, 255));

        LogOutButton.setBackground(new java.awt.Color(45, 47, 60));
        LogOutButton.setFont(new java.awt.Font("Stencil", 0, 18)); // NOI18N
        LogOutButton.setForeground(new java.awt.Color(255, 255, 255));
        LogOutButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LogOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogOutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout TopLayout = new javax.swing.GroupLayout(Top);
        Top.setLayout(TopLayout);
        TopLayout.setHorizontalGroup(
            TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LogOutButton)
                .addContainerGap(1072, Short.MAX_VALUE))
        );
        TopLayout.setVerticalGroup(
            TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LogOutButton)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(45, 47, 60));

        jLabel4.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Meal Name: ");

        jLabel3.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Price: ");

        jLabel5.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Qyt: ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MealName, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Qyt, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Price, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MealName, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Price, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Qyt, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel2.setText("Meal Id: ");

        MealId.setFont(new java.awt.Font("Sylfaen", 0, 14)); // NOI18N
        MealId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        MealId.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        SearchButton.setBackground(new java.awt.Color(45, 47, 60));
        SearchButton.setFont(new java.awt.Font("Stencil", 0, 18)); // NOI18N
        SearchButton.setForeground(new java.awt.Color(255, 255, 255));
        SearchButton.setText("Search");
        SearchButton.setBorder(null);
        SearchButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchButtonActionPerformed(evt);
            }
        });

        AddButton.setBackground(new java.awt.Color(45, 47, 60));
        AddButton.setFont(new java.awt.Font("Stencil", 0, 18)); // NOI18N
        AddButton.setForeground(new java.awt.Color(255, 255, 255));
        AddButton.setText("ADD");
        AddButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });

        UpdateButton.setBackground(new java.awt.Color(45, 47, 60));
        UpdateButton.setFont(new java.awt.Font("Stencil", 0, 18)); // NOI18N
        UpdateButton.setForeground(new java.awt.Color(255, 255, 255));
        UpdateButton.setText("UPDATE");
        UpdateButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        UpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateButtonActionPerformed(evt);
            }
        });

        DeleteButton.setBackground(new java.awt.Color(45, 47, 60));
        DeleteButton.setFont(new java.awt.Font("Stencil", 0, 18)); // NOI18N
        DeleteButton.setForeground(new java.awt.Color(255, 255, 255));
        DeleteButton.setText("DELETE");
        DeleteButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        DeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteButtonActionPerformed(evt);
            }
        });

        Photo.setBackground(new java.awt.Color(204, 204, 204));

        BrowseButton.setBackground(new java.awt.Color(45, 47, 60));
        BrowseButton.setFont(new java.awt.Font("Stencil", 0, 14)); // NOI18N
        BrowseButton.setForeground(new java.awt.Color(255, 255, 255));
        BrowseButton.setText("Browse Image");
        BrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BrowseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(MealId, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(SearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(AddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(UpdateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(DeleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(Photo, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BrowseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(SearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(MealId, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UpdateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DeleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BrowseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(95, 95, 95))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Photo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        InventoryTable.setFont(new java.awt.Font("Sitka Text", 0, 14)); // NOI18N
        InventoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Meal Id", "Meal name", "Prce", "Qyt"
            }
        ));
        InventoryTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        InventoryTable.setRowHeight(30);
        InventoryTable.setSelectionBackground(new java.awt.Color(45, 47, 60));
        jScrollPane1.setViewportView(InventoryTable);
        if (InventoryTable.getColumnModel().getColumnCount() > 0) {
            InventoryTable.getColumnModel().getColumn(0).setResizable(false);
            InventoryTable.getColumnModel().getColumn(1).setResizable(false);
            InventoryTable.getColumnModel().getColumn(2).setResizable(false);
            InventoryTable.getColumnModel().getColumn(3).setResizable(false);
            InventoryTable.getColumnModel().getColumn(3).setPreferredWidth(20);
        }

        jLabel1.setBackground(new java.awt.Color(45, 47, 60));
        jLabel1.setFont(new java.awt.Font("Stencil", 1, 36)); // NOI18N
        jLabel1.setText("Jabenn Food Inventory");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(jLabel1))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 642, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Top, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Top, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchButtonActionPerformed
        try {
            String mealId = MealId.getSelectedItem().toString();

            pst = connection.prepareStatement("SELECT * FROM Inventory WHERE ID=?");
            pst.setString(1, mealId);
            rs = pst.executeQuery();

            if (rs.next()) {
                MealName.setText(rs.getString(2));
                Price.setText(rs.getString(3));
                Qyt.setText(rs.getString(4));

                // Assuming 'imageColumnIndex' is the index of the Image column
                int imageColumnIndex = 5;

                // Retrieve the image data as a byte array
                byte[] imageData = rs.getBytes(imageColumnIndex);

                // Check if image data is not empty
                if (imageData != null && imageData.length > 0) {
                    // Create an ImageIcon from the byte array
                    ImageIcon imageIcon = new ImageIcon(imageData);

                    if (imageIcon != null) {
                        // Resize the image to fit into the JLabel
                        Image scaledImage = imageIcon.getImage().getScaledInstance(188, 165, Image.SCALE_SMOOTH);

                        // Update the ImageIcon with the resized image
                        imageIcon = new ImageIcon(scaledImage);

                        // Set the ImageIcon to the JLabel
                        Photo.setIcon(imageIcon);

                        // Refresh the Photo label
                        Photo.revalidate();
                        Photo.repaint();
                    }
                } else {
                    // Handle empty image data (e.g., set a default image or clear the label)
                    Photo.setIcon(null);  // Set to null or another default ImageIcon
                }
            } else {
                JOptionPane.showMessageDialog(this, "No record found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_SearchButtonActionPerformed

    private void LogOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogOutButtonActionPerformed
        Front FrontPage = new Front();
        FrontPage.setVisible(true);
        FrontPage.pack();
        FrontPage.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_LogOutButtonActionPerformed

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed
        String mealName = MealName.getText();
        String price = Price.getText();
        String qyt = Qyt.getText();

        try {
            // Attempt to parse the price and quantity
            BigDecimal parsedPrice = new BigDecimal(price);

            // Get the maximum ID from the database
            pst = connection.prepareStatement("SELECT MAX(ID) FROM Inventory");
            rs = pst.executeQuery();
            int maxId = 0;
            if (rs.next()) {
                maxId = rs.getInt(1);
            }

            // Increment the maximum ID to generate a new ID
            int newId = maxId + 1;

            // Insert the new record with the generated ID and image data
            pst = connection.prepareStatement("INSERT INTO Inventory(ID, MealName, Price, Qyt, Image) VALUES(?, ?, ?, ?, ?)");
            pst.setInt(1, newId);
            pst.setString(2, mealName);
            pst.setBigDecimal(3, parsedPrice);
            pst.setString(4, qyt);
            pst.setBytes(5, image);

            int i = pst.executeUpdate();

            if (i == 1) {
                JOptionPane.showMessageDialog(this, "Meal Added", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Update the meal IDs in the database
                updateMealIds();

                MealName.setText("");
                Price.setText("");
                Qyt.setText("");
                imagePath = null; // Reset imagePath after use
                LoadData();
                LoadMeal();
            } else {
                JOptionPane.showMessageDialog(this, "No meal added", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid values for Price and Quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_AddButtonActionPerformed

    private void UpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateButtonActionPerformed
        try {
            String mealId = MealId.getSelectedItem().toString();
            String mealName = MealName.getText();
            String price = Price.getText();
            String qyt = Qyt.getText();

            // Attempt to parse the price and quantity
            BigDecimal parsedPrice = new BigDecimal(price);

            // Check if the image array is null or empty
            boolean updateImage = (image != null && image.length > 0);

            // Build the SQL query based on whether the image needs to be updated
            if (updateImage) {
                pst = connection.prepareStatement("UPDATE Inventory SET MealName=?, Price=?, Qyt=?, Image=? WHERE ID=?");
                pst.setBytes(4, image); // Use the image byte array
            } else {
                pst = connection.prepareStatement("UPDATE Inventory SET MealName=?, Price=?, Qyt=? WHERE ID=?");
            }

            pst.setString(1, mealName);
            pst.setBigDecimal(2, parsedPrice);
            pst.setString(3, qyt);
            pst.setString(5, mealId);

            int updatedInventory = pst.executeUpdate();

            if (updatedInventory == 1) {
                // Show a success message
                JOptionPane.showMessageDialog(this, "Meal Added", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Set the updated image to the Photo label if the image is updated
                if (updateImage) {
                    ImageIcon updatedImageIcon = new ImageIcon(image);
                    Image updatedImage = updatedImageIcon.getImage().getScaledInstance(188, 165, Image.SCALE_SMOOTH);
                    updatedImageIcon = new ImageIcon(updatedImage);
                    Photo.setIcon(updatedImageIcon);
                }

                // Clear other fields and refresh data
                MealName.setText("");
                Price.setText("");
                Qyt.setText("");
                MealName.requestFocus();

                // Assuming LoadMeal is a method to refresh the displayed data
                LoadMeal();
                LoadData();
            } else {
                JOptionPane.showMessageDialog(this, "No meal added", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating meal. Please check input values and updated image file.","Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_UpdateButtonActionPerformed

    private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteButtonActionPerformed
         try {
            String mealIdToDelete = MealId.getSelectedItem().toString();

            pst = connection.prepareStatement("DELETE FROM Inventory WHERE ID=?");
            pst.setString(1, mealIdToDelete);

            int deletedRows = pst.executeUpdate();

            if (deletedRows == 1) {
                JOptionPane.showMessageDialog(this, "Meal deleted", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Delete the selected item from the combo box
                MealId.removeItem(mealIdToDelete);

                // Update the meal IDs in the database
                updateMealIds();

                // Update the IDs of remaining records
                updateRemainingIds(Integer.parseInt(mealIdToDelete));

                // Clear text fields
                MealName.setText("");
                Price.setText("");
                Qyt.setText("");
                MealName.requestFocus();

                // Refresh the displayed data
                LoadMeal();
                LoadData();
                updateMealIds();
            } else {
                JOptionPane.showMessageDialog(this, "No meal deleted", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_DeleteButtonActionPerformed

    private void BrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BrowseButtonActionPerformed
         JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter extension = new FileNameExtensionFilter("PNG JPG AND JPEG", "png", "jpg", "jpeg");
    fileChooser.addChoosableFileFilter(extension);
    int load = fileChooser.showOpenDialog(null);

    if (load == JFileChooser.APPROVE_OPTION) {
        try {
            file = fileChooser.getSelectedFile();
            imagePath = file.getAbsolutePath();

            // Read the image into a byte array
            Path path = Paths.get(imagePath);
            image = Files.readAllBytes(path);

            // Create an ImageIcon from the byte array
            ImageIcon imageIcon = new ImageIcon(image);

            // Resize the image to fit into the JLabel
            Image scaledImage = imageIcon.getImage().getScaledInstance(188, 165, Image.SCALE_SMOOTH);

            // Update the ImageIcon with the resized image
            imageIcon = new ImageIcon(scaledImage);

            // Set the ImageIcon to the JLabel
            Photo.setIcon(imageIcon);
        } catch (IOException ex) {
            ex.printStackTrace(); // Handle the exception appropriately
        }
    }
    }//GEN-LAST:event_BrowseButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddButton;
    private javax.swing.JButton BrowseButton;
    private javax.swing.JButton DeleteButton;
    private javax.swing.JTable InventoryTable;
    private javax.swing.JButton LogOutButton;
    private javax.swing.JComboBox<String> MealId;
    private javax.swing.JTextField MealName;
    private javax.swing.JLabel Photo;
    private javax.swing.JTextField Price;
    private javax.swing.JTextField Qyt;
    private javax.swing.JButton SearchButton;
    private javax.swing.JPanel Top;
    private javax.swing.JButton UpdateButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
