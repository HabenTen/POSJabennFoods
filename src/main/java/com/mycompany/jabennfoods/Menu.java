package com.mycompany.jabennfoods;

import com.mycompany.jabennfoods.AdminLogin;
import com.mycompany.jabennfoods.Inventory;
import java.awt.Component;
import java.awt.Image;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;


 
public final class Menu extends javax.swing.JFrame {
    public Menu() {
        initComponents();
        Connect();
        LoadData();
        centerMenuData();
        Menu.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());

        // Add a ListSelectionListener to the Menu table
        Menu.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                // Get the selected row in the Menu table
                int selectedRow = Menu.getSelectedRow();
                
                if (selectedRow != -1) {
                    // Get data from the selected row
                    String mealName = (String) Menu.getValueAt(selectedRow, 0); // Assuming MealName is in the first column
                    String price = (String) Menu.getValueAt(selectedRow, 1);    // Assuming Price is in the second column
                    
                    // Default value for Qyt
                    String defaultQyt = "1";
                    
                    // Check if the item already exists in the CartTable
                    boolean itemExists = false;
                    DefaultTableModel cartTableModel = (DefaultTableModel) CartTable.getModel();
                    for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                        String cartMealName = (String) CartTable.getValueAt(i, 0);
                        if (mealName.equals(cartMealName)) {
                            // Item already exists, increment the Qyt value
                            int currentQyt = Integer.parseInt((String) CartTable.getValueAt(i, 2));
                            CartTable.setValueAt(String.valueOf(currentQyt + 1), i, 2);
                            itemExists = true;
                            break;
                        }
                    }

                    // If the item doesn't exist, add a new row to the CartTable
                    if (!itemExists) {
                        cartTableModel.addRow(new Object[]{mealName, price, defaultQyt});
                    }
                    centerMCartData();
                }
                updateTotal();
            }
        });

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
    public void centerMenuData() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int columnIndex = 0; columnIndex < Menu.getColumnCount(); columnIndex++) {
            Menu.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }
    }
    public void centerMCartData() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int columnIndex = 0; columnIndex < CartTable.getColumnCount(); columnIndex++) {
            CartTable.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }
    }
    class ImageRenderer extends DefaultTableCellRenderer {
        private static final int ICON_WIDTH = 150;  // Adjust the width as needed
        private static final int ICON_HEIGHT = 150;  // Adjust the height as needed

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ImageIcon imageIcon) {
                // Resize the ImageIcon
                ImageIcon resizedIcon = resizeImageIcon(imageIcon, ICON_WIDTH, ICON_HEIGHT);

                // Render the resized ImageIcon
                setIcon(resizedIcon);
                setText(null);
            } else {
                // Render text or other data
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
            return this;
        }

    private ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        // Get the Image from the ImageIcon
        Image image = icon.getImage();

        // Resize the Image
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // Create a new ImageIcon with the resized Image
        return new ImageIcon(resizedImage);
        }
    }


    public void LoadData() {
        try {
            pst = connection.prepareStatement("SELECT MealName, Price, Image FROM Inventory");
            rs = pst.executeQuery();

            DefaultTableModel table = (DefaultTableModel) Menu.getModel();
            table.setRowCount(0);

            while (rs.next()) {
                String mealName = rs.getString("MealName");
                String price = rs.getString("Price");

                // Assuming the Image column is in the 5th position (index 4)
                Blob imageBlob = rs.getBlob("Image");
                byte[] imageData = null;

                if (imageBlob != null) {
                    // Convert Blob data to byte array
                    int blobLength = (int) imageBlob.length();
                    imageData = imageBlob.getBytes(1, blobLength);
                }

                // Create an ImageIcon from the byte array and resize it
                ImageIcon imageIcon = (imageData != null) ? resizeImage(new ImageIcon(imageData), 160, 150) : null;

                // Add data to the table model
                table.addRow(new Object[]{mealName, price, imageIcon});
            }

            setRowHeightBasedOnImageSize(Menu);
        } catch (SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setRowHeightBasedOnImageSize(JTable table) {
        int preferredRowHeight = 150 + table.getRowMargin();
        table.setRowHeight(preferredRowHeight);
    }

    private ImageIcon resizeImage(ImageIcon originalIcon, int width, int height) {
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
    private void updateTotal() {
        DefaultTableModel cartTableModel = (DefaultTableModel) CartTable.getModel();
        double total = 0;

        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            String price = (String) CartTable.getValueAt(i, 1);
            String qyt = (String) CartTable.getValueAt(i, 2);

            double rowTotal = Double.parseDouble(price) * Integer.parseInt(qyt);
            total += rowTotal;
        }

        // Format the total to display with two decimal places
        String formattedTotal = String.format("%.2f", total);

        Total.setText(formattedTotal);
    }


    private String generateReceipt() {
        StringBuilder receiptBuilder = new StringBuilder();
        // Add items and quantities to the receipt
        DefaultTableModel cartTableModel = (DefaultTableModel) CartTable.getModel();

        // Check if CartTable is empty
        if (cartTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No items in the cart", "Empty Cart", JOptionPane.INFORMATION_MESSAGE);
        } else {
            receiptBuilder.append("\n");
            receiptBuilder.append("                             Jabenn Foods\n");
            receiptBuilder.append("                  FEU Institute of Technology\n");
            receiptBuilder.append("                             Final Project\n");
            receiptBuilder.append(" ===================================== \n");

            // Create separate formats for date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

            // Get current date and time
            Date currentDate = new Date();
            String orderDate = dateFormat.format(currentDate);
            String orderTime = timeFormat.format(currentDate);

            receiptBuilder.append("                     ").append(orderDate).append(" ");
            receiptBuilder.append(orderTime).append("\n");
            receiptBuilder.append(" ===================================== \n");

            receiptBuilder.append(" -------------------JABENNFOODS---------------------\n");
            for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                String mealName = (String) CartTable.getValueAt(i, 0);
                String price = (String) CartTable.getValueAt(i, 1);
                String qty = (String) CartTable.getValueAt(i, 2);

                receiptBuilder.append("              ").append(qty).append("  ").append(mealName).append("                   $").append(price).append("\n");
            }
            receiptBuilder.append(" ------------------------------------------------------------\n");

            // Add total amount to the receipt
            String totalAmount = Total.getText();
            String payment = Amount.getText();

            try {
                double totalAmount2 = Double.parseDouble(totalAmount);
                double payment2 = Double.parseDouble(payment);
                double change2 =  payment2- totalAmount2;

                // Format totalAmount2 and change2 with 2 decimal places
                String formattedTotalAmount = String.format("%.2f", totalAmount2);
                String formattedChange = String.format("%.2f", change2);

                // Check if totalAmount2 is greater than payment2
                if (totalAmount2 > payment2) {
                    JOptionPane.showMessageDialog(null, "Invalid amount.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                    return null;  // Return null to indicate an error condition
                }

                receiptBuilder.append("                                     Total: $").append(formattedTotalAmount).append("\n");
                receiptBuilder.append("                                  Change: $").append(formattedChange).append("\n");

                // Display success message
                JOptionPane.showMessageDialog(null, "Payment successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Update the database after payment
        updateDatabaseAfterPayment();
            } catch (NumberFormatException e) {
                // Handle the exception, e.g., display an error message
                e.printStackTrace();
            }
            receiptBuilder.append(" ===================================== \n");
            receiptBuilder.append("                            Thank you!! \n");

        }

        // Display the receipt in the JTextArea
        Receipt.setText(receiptBuilder.toString());

        return receiptBuilder.toString();
    }
    private String generateTxtReceipt() {
        StringBuilder receiptBuilder = new StringBuilder();
        // Add items and quantities to the receipt
        DefaultTableModel cartTableModel = (DefaultTableModel) CartTable.getModel();

        receiptBuilder.append("\n");
        receiptBuilder.append("                           Jabenn Foods\n");
        receiptBuilder.append("                  FEU Institute of Technology\n");
        receiptBuilder.append("                           Final Project\n");
        receiptBuilder.append(" ================================================================= \n");

        // Create separate formats for date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

        // Get current date and time
        Date currentDate = new Date();
        String orderDate = dateFormat.format(currentDate);
        String orderTime = timeFormat.format(currentDate);

        // Append date and time separately to the receipt
        receiptBuilder.append("                      ").append(orderDate).append(" ");
        receiptBuilder.append(orderTime).append("\n");
        receiptBuilder.append(" ================================================================= \n");

        // Add header
        receiptBuilder.append("        -------------------JABENNFOODS---------------------\n");
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            String mealName = (String) CartTable.getValueAt(i, 0);
            String price = (String) CartTable.getValueAt(i, 1);
            String qty = (String) CartTable.getValueAt(i, 2);
            // Format each line with a specific number of spaces
            receiptBuilder.append("              ").append(qty).append("  ").append(mealName).append("                   $").append(price).append("\n");
        }
        receiptBuilder.append("   -------------------------------------------------------------\n");

        // Add total amount to the receipt
        String totalAmount = Total.getText();
        String payment = Amount.getText();

        // Parse payment and total amounts to double
        double totalAmount2 = Double.parseDouble(totalAmount);
        double payment2 = Double.parseDouble(payment);
        double change = payment2 - totalAmount2; 

        // Format totalAmount2 and payment2 with 2 decimal places
        String formattedTotalAmount = String.format("%.2f", totalAmount2);
        String formattedPayment = String.format("%.2f", payment2);
        String formattedChange = String.format("%.2f", change);

        // Check if totalAmount2 is greater than payment2
        if (totalAmount2 > payment2) {
            Amount.setText("");
            return null; // Return null to indicate an error condition
        }

        receiptBuilder.append("                                   Total: $").append(formattedTotalAmount).append("\n");
        receiptBuilder.append("                                 Payment: $").append(formattedPayment).append("\n");
        receiptBuilder.append("                                  Change: $").append(formattedChange).append("\n");

        receiptBuilder.append(" ================================================================= \n");
        receiptBuilder.append("                           Thank you!! \n");
        return receiptBuilder.toString();
    }



    private void saveReceiptToText(String receipt, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Calculate the maximum line length
            int maxLineLength = getMaxLineLength(receipt);

            // Calculate the number of spaces needed for centering
            int leadingSpaces = (maxLineLength - receipt.length()) / 2;

            // Print leading spaces before the receipt content
            for (int i = 0; i < leadingSpaces; i++) {
                writer.print(" ");
            }
            // Print the receipt content
            writer.println(receipt);

            JOptionPane.showMessageDialog(null, "Receipt saved to file: " + filename, "Save Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving receipt to file", "File Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private int getMaxLineLength(String receipt) {
        int maxLineLength = 0;
        String[] lines = receipt.split("<br>");

        for (String line : lines) {
            // Remove HTML tags to get the actual line content
            String plainLine = line.replaceAll("\\<.*?\\>", "");

            if (plainLine.length() > maxLineLength) {
                maxLineLength = plainLine.length();
            }
        }

        return maxLineLength;
    }


    private void clearPaymentDetails() {
        // Clear CartTable
        DefaultTableModel cartTableModel = (DefaultTableModel) CartTable.getModel();
        cartTableModel.setRowCount(0);

        // Clear Amount and Total
        Amount.setText("");
        Total.setText("");

        // Clear Receipt
        Receipt.setText("");
    }
    private void updateDatabaseAfterPayment() {
        DefaultTableModel cartTableModel = (DefaultTableModel) CartTable.getModel();

        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            String mealName = (String) cartTableModel.getValueAt(i, 0);
            String qty = (String) cartTableModel.getValueAt(i, 2);

            // Deduct the quantity from the database
            deductQuantityFromDatabase(mealName, Integer.parseInt(qty));
        }
    }

    private void deductQuantityFromDatabase(String mealName, int deductedQty) {
        try {
            // Prepare a SQL update statement to deduct quantity from the database
            String updateQuery = "UPDATE Inventory SET Qyt = Qyt - ? WHERE MealName = ?";
            pst = connection.prepareStatement(updateQuery);
            pst.setInt(1, deductedQty);
            pst.setString(2, mealName);

            // Execute the update statement
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle any SQL exception, e.g., log the error or show an error message
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Left = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Menu = new javax.swing.JTable();
        Top = new javax.swing.JPanel();
        SettingsButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        Center = new javax.swing.JPanel();
        ClearButton = new javax.swing.JButton();
        ProceedButton = new javax.swing.JButton();
        TotalText = new javax.swing.JLabel();
        Total = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        CartTable = new javax.swing.JTable();
        TotalText1 = new javax.swing.JLabel();
        Amount = new javax.swing.JTextField();
        Right = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Receipt = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu");

        jPanel1.setBackground(new java.awt.Color(45, 47, 60));
        jPanel1.setPreferredSize(new java.awt.Dimension(1220, 661));
        jPanel1.setLayout(null);

        Left.setBackground(new java.awt.Color(255, 255, 255));

        Menu.setBackground(new java.awt.Color(255, 255, 255));
        Menu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Meal Name", "Price", "Image"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Menu.setRowHeight(50);
        jScrollPane2.setViewportView(Menu);
        if (Menu.getColumnModel().getColumnCount() > 0) {
            Menu.getColumnModel().getColumn(0).setPreferredWidth(0);
            Menu.getColumnModel().getColumn(1).setPreferredWidth(0);
            Menu.getColumnModel().getColumn(2).setPreferredWidth(38);
        }

        javax.swing.GroupLayout LeftLayout = new javax.swing.GroupLayout(Left);
        Left.setLayout(LeftLayout);
        LeftLayout.setHorizontalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                .addContainerGap())
        );
        LeftLayout.setVerticalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(Left);
        Left.setBounds(30, 80, 440, 550);

        Top.setBackground(new java.awt.Color(255, 255, 255));
        Top.setLayout(null);

        SettingsButton.setBackground(new java.awt.Color(45, 47, 60));
        SettingsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SettingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SettingsButtonActionPerformed(evt);
            }
        });
        Top.add(SettingsButton);
        SettingsButton.setBounds(6, 6, 30, 30);

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Stencil", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(45, 47, 60));
        jLabel1.setText("Jabenn foods menu");
        Top.add(jLabel1);
        jLabel1.setBounds(460, 10, 260, 25);

        jPanel1.add(Top);
        Top.setBounds(30, 20, 1160, 40);

        Center.setBackground(new java.awt.Color(255, 255, 255));

        ClearButton.setBackground(new java.awt.Color(45, 47, 60));
        ClearButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ClearButton.setForeground(new java.awt.Color(255, 255, 255));
        ClearButton.setText("Clear all");
        ClearButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearButtonActionPerformed(evt);
            }
        });

        ProceedButton.setBackground(new java.awt.Color(45, 47, 60));
        ProceedButton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ProceedButton.setForeground(new java.awt.Color(255, 255, 255));
        ProceedButton.setText("Proceed");
        ProceedButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ProceedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProceedButtonActionPerformed(evt);
            }
        });

        TotalText.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TotalText.setText("Total :");

        Total.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N

        CartTable.setBackground(new java.awt.Color(255, 255, 255));
        CartTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Meal Name", "Price", "Qyt"
            }
        ));
        CartTable.setRowHeight(50);
        CartTable.setSelectionBackground(new java.awt.Color(45, 47, 60));
        jScrollPane1.setViewportView(CartTable);

        TotalText1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TotalText1.setText("Amount :");

        Amount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N

        javax.swing.GroupLayout CenterLayout = new javax.swing.GroupLayout(Center);
        Center.setLayout(CenterLayout);
        CenterLayout.setHorizontalGroup(
            CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CenterLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
            .addGroup(CenterLayout.createSequentialGroup()
                .addGroup(CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CenterLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CenterLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ProceedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(CenterLayout.createSequentialGroup()
                                .addGroup(CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(TotalText1)
                                    .addComponent(TotalText, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Total, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Amount, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(0, 24, Short.MAX_VALUE))
        );
        CenterLayout.setVerticalGroup(
            CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CenterLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TotalText, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Total, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
                .addGap(21, 21, 21)
                .addGroup(CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Amount)
                    .addComponent(TotalText1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(ProceedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(105, 105, 105))
        );

        jPanel1.add(Center);
        Center.setBounds(480, 80, 400, 550);

        Right.setBackground(new java.awt.Color(255, 255, 255));

        Receipt.setBackground(new java.awt.Color(255, 255, 255));
        Receipt.setColumns(20);
        Receipt.setRows(5);
        jScrollPane3.setViewportView(Receipt);

        javax.swing.GroupLayout RightLayout = new javax.swing.GroupLayout(Right);
        Right.setLayout(RightLayout);
        RightLayout.setHorizontalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        RightLayout.setVerticalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel1.add(Right);
        Right.setBounds(890, 80, 300, 550);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SettingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SettingsButtonActionPerformed
        AdminLogin adminLogin = new AdminLogin();
        adminLogin.setVisible(true);
        adminLogin.pack();
        adminLogin.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_SettingsButtonActionPerformed

    private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearButtonActionPerformed
        DefaultTableModel cartTableModel = (DefaultTableModel) CartTable.getModel();
        cartTableModel.setRowCount(0);
        Total.setText(String.format("%.2f", 0.0));
    }//GEN-LAST:event_ClearButtonActionPerformed

    private void ProceedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProceedButtonActionPerformed
        String receipt = generateReceipt();
        String receiptForText = generateTxtReceipt();

        // Assuming you have a JTextArea named ReceiptTextArea to display the receipt
        Receipt.setText(receipt);

        // Save the receipt to a text file
        saveReceiptToText(receiptForText, "JabennFoodsReceipt.txt");

        // Clear CartTable, Amount, Total, and Receipt after successful payment
        clearPaymentDetails();
    }//GEN-LAST:event_ProceedButtonActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Amount;
    private javax.swing.JTable CartTable;
    private javax.swing.JPanel Center;
    private javax.swing.JButton ClearButton;
    private javax.swing.JPanel Left;
    private javax.swing.JTable Menu;
    private javax.swing.JButton ProceedButton;
    private javax.swing.JTextArea Receipt;
    private javax.swing.JPanel Right;
    private javax.swing.JButton SettingsButton;
    private javax.swing.JPanel Top;
    private javax.swing.JTextField Total;
    private javax.swing.JLabel TotalText;
    private javax.swing.JLabel TotalText1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
