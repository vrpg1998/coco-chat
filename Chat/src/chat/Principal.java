/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import classes.ButtonEditor;
import classes.DataBaseConnection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import classes.ButtonRenderer;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;

/**
 *
 * @author andim
 */
public class Principal extends javax.swing.JFrame {

    private int numAmigos;
    private int userID;
    private int ACTIVE_CONVO;
    private int ACTIVE_CONVO_USER_ID;
    private String ACTIVE_CONVO_TYPE;
    private String ACTIVE_CONVO_USERNAME;
    private ResultSet friendRequests;
    private DataBaseConnection con;
    /**
     * Creates new form Principal
     */
    
    public Principal() {
        initComponents();
        con = new DataBaseConnection();
        friendRequests = null;
        numAmigos = 0;
        ACTIVE_CONVO = 0;
        ACTIVE_CONVO_USER_ID = 0;
        ACTIVE_CONVO_TYPE = "";
        ACTIVE_CONVO_USERNAME = "";
        DefaultListModel m = new DefaultListModel();
        m.setSize(0);
        listConvo.setModel(m);
        listGroups.setModel(m);
        listOffline.setModel(m);
        listOfflineFriends.setModel(m);
        listOnline.setModel(m);
        listOnlineFriends.setModel(m);
        
        listConvo.setFont(listConvo.getFont().deriveFont(Font.PLAIN));
        listGroups.setFont(listConvo.getFont().deriveFont(Font.PLAIN));
        listOfflineFriends.setFont(listConvo.getFont().deriveFont(Font.PLAIN));
        listOnlineFriends.setFont(listConvo.getFont().deriveFont(Font.PLAIN));
        listOffline.setFont(listConvo.getFont().deriveFont(Font.PLAIN));
        listOnline.setFont(listConvo.getFont().deriveFont(Font.PLAIN));
        
        addGroupMember.setVisible(false);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                con.logoutUser(userID);
            }
        });
    }
    
    public void setUserId(int id) {
        this.userID = id;
        loadUser();
    }
    
    public void loadFriendRequestsTable() {
        try {
            int requestCount = con.getRequestCount(userID), count = 0;
            Object[][] users = null;
            DefaultTableModel model = new DefaultTableModel();
            if(!friendRequests.next()) {
                jTabbedPane_usuarios.setEnabledAt(2, false);
                setTabFristIndex();
            } else {
                System.out.println(requestCount + "");
                if(requestCount > 0) {
                    users = new Object[requestCount][3];
                } else {
                    JOptionPane.showMessageDialog(null, "Error");
                    return;
                }
                do {
                    ResultSet user = con.getUser(friendRequests.getInt("FK_sender_id"));
                    user.next();
                    users[count][0] = user.getString("usr_Name");
                    users[count][1] = "<html>Aceptar</html>," + friendRequests.getInt("PK_request_id");
                    users[count++][2] = "<html>Rechazar</html>," + friendRequests.getInt("PK_request_id");
                } while(friendRequests.next());
                
                model.setDataVector(users, new Object[]{"Usuario", "Aceptar", "Rechazar"});
                tableRequests.setModel(model);
                tableRequests.getColumn("Aceptar").setCellRenderer(new ButtonRenderer());
                tableRequests.getColumn("Aceptar").setCellEditor(new ButtonEditor(new JCheckBox(), this));
                tableRequests.getColumn("Rechazar").setCellRenderer(new ButtonRenderer());
                tableRequests.getColumn("Rechazar").setCellEditor(new ButtonEditor(new JCheckBox(), this));
            }
        } catch (SQLException e) {
            System.out.println("/loadFriendRequests: " + e.getMessage());
        }
    }
    
    public void setTabFristIndex() {
        jTabbedPane_usuarios.setSelectedIndex(0);
    }
    
    public void loadUser() {
        con.loginUser(userID);
        numAmigos = con.getFriendsCount(userID);
        loadFriendRequests();
        loadFriendRequestsTable();
        loadOnlineUsers();
        loadOfflineUsers();
        loadOnlineFriends();
        loadOfflineFriends();
        loadGroups();
        
        if(numAmigos == -1)
            JOptionPane.showMessageDialog(null, "Error. Reinicie la aplicación.");
    }
    
    public void loadFriendRequests() {
        friendRequests = con.getPendingRequests(userID);
    }
    
    private void loadOnlineUsers() {
        try {
            ResultSet res = con.getOnlineUsers();
            DefaultListModel model = new DefaultListModel();
            while(res.next()) {
                if(userID != res.getInt("PK_usr_ID")) {
                    if(checkUnreadMessages(res.getInt("PK_usr_ID")))
                        model.addElement("<html><b>" + res.getString("usr_Name") + "</b></html>," + res.getInt(1));
                    else
                        model.addElement("<html>" + res.getString("usr_Name") + "</html>," + res.getInt(1));
                }
            }
            listOnline.setModel(model);
        } catch (Exception e) {
            System.out.println("/loadOnlineUsers::Principal: " + e.getMessage());
        }
    }
    
    private void loadOfflineUsers() {
        try {
            ResultSet res = con.getOfflineUsers();
            DefaultListModel model = new DefaultListModel();
            while(res.next()) {
                if(userID != res.getInt("PK_usr_ID")) {
                    if(checkUnreadMessages(res.getInt("PK_usr_ID")))
                        model.addElement("<html><b>" + res.getString("usr_Name") + "</b></html>," + res.getInt(1));
                    else
                        model.addElement("<html>" + res.getString("usr_Name") + "</html>," + res.getInt(1));
                }
            }
            listOffline.setModel(model);
        } catch (Exception e) {
            System.out.println("/loadOfflineUsers::Principal: " + e.getMessage());
        }
    }
    
    private void loadOnlineFriends() {
        try {
            ResultSet res = con.getOnlineUsers(userID);
            DefaultListModel model = new DefaultListModel();
            while(res.next()) {
                if(userID != res.getInt("PK_usr_ID")) {
                    if(checkUnreadMessages(res.getInt("PK_usr_ID")))
                        model.addElement("<html><b>" + res.getString("usr_Name") + "</b></html>," + res.getInt(1));
                    else
                        model.addElement("<html>" + res.getString("usr_Name") + "</html>," + res.getInt(1));
                }
            }
            listOnlineFriends.setModel(model);
        } catch (Exception e) {
            System.out.println("/loadOnlineFriends: " + e.getMessage());
        }
    }
    
    private void loadOfflineFriends() {
        try {
            ResultSet res = con.getOfflineUsers(userID);
            DefaultListModel model = new DefaultListModel();
            while(res.next()) {
                if(userID != res.getInt("PK_usr_ID")) {
                    if(checkUnreadMessages(res.getInt("PK_usr_ID")))
                        model.addElement("<html><b>" + res.getString("usr_Name") + "</b></html>," + res.getInt(1));
                    else
                        model.addElement("<html>" + res.getString("usr_Name") + "</html>," + res.getInt(1));
                }
            }
            listOfflineFriends.setModel(model);
        } catch (Exception e) {
            System.out.println("/loadOfflineUsers::Principal: " + e.getMessage());
        }
    }
    
    private void loadConversation(int friendID, String friendName) {
        try {
            int convo = con.getUserConversation(friendID, userID);
            ResultSet res = con.getConversation(convo);
            DefaultListModel model = new DefaultListModel();
            
            while(res.next()) {
                String author = "";
                if(res.getInt("FK_usr_ID") == userID)
                    author = "Yo: ";
                else
                    author = ACTIVE_CONVO_USERNAME + ": ";
                model.addElement(author + res.getString("msg_text"));
                    
            }
            listConvo.setModel(model);
            con.readMessage(convo, userID);
        } catch (Exception e) {
            System.out.println("/loadConversation: " + e.getMessage());
        }
        return;
    }
    
    private boolean checkUnreadMessages(int id) {
        try {
            int convo = con.getUserConversation(id, userID);
            ResultSet res = con.getUnreadConvos(convo, userID, "user");
            
            while(res.next()) {
                if(res.getInt(1) == id)
                    return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("/checkUnreadMessages: " + e.getMessage());
        }
        return false;
    }
    
    private void loadUsers() {
        loadOfflineUsers();
        loadOfflineFriends();
        loadOnlineUsers();
        loadOnlineFriends();
        loadGroups();
    }
    
    private void loadGroups() {
        try {
            DefaultListModel model = new DefaultListModel();
            ResultSet res = con.getGroups(userID);
            while(res.next()) {
                System.out.println(res.getInt(1));
                ArrayList<Integer> id = con.getUnreadGroupMessages(userID);
                if(id.size() == 0)
                    model.addElement("<html>" + res.getString(2) + "</html>," + res.getInt(1));
                for(int i = 0; i < id.size(); i++) {
                    if(res.getInt(1) == id.get(i))
                        model.addElement("<html><b>" + res.getString(2) + "</b></html>," + res.getInt(1));
                    else
                        model.addElement("<html>" + res.getString(2) + "</html>," + res.getInt(1));
                }
            }
            listGroups.setModel(model);
        } catch (SQLException e) {
            System.out.println("/loadGroups: " + e.getMessage());
        }
    }
    
    public void reloadMessages(int convoId) {
        if(convoId == ACTIVE_CONVO) {
            loadConversation(userID, ACTIVE_CONVO_USERNAME);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel_gpo = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        btn_agregargpo = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        listGroups = new javax.swing.JList<>();
        jTabbedPane_usuarios = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        listOnline = new javax.swing.JList<>();
        jScrollPane10 = new javax.swing.JScrollPane();
        listOffline = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jLabel4 = new javax.swing.JLabel();
        addFriend = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        listOnlineFriends = new javax.swing.JList<>();
        jScrollPane12 = new javax.swing.JScrollPane();
        listOfflineFriends = new javax.swing.JList<>();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tableRequests = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageArea = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        labelChatName = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listConvo = new javax.swing.JList<>();
        addGroupMember = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("WhastCoco");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel_gpo.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel7.setText("Grupos");

        btn_agregargpo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chat/add-round-button.png"))); // NOI18N
        btn_agregargpo.setText(" ");
        btn_agregargpo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_agregargpoMouseClicked(evt);
            }
        });

        listGroups.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listGroups.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listGroupsMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(listGroups);

        javax.swing.GroupLayout jPanel_gpoLayout = new javax.swing.GroupLayout(jPanel_gpo);
        jPanel_gpo.setLayout(jPanel_gpoLayout);
        jPanel_gpoLayout.setHorizontalGroup(
            jPanel_gpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_gpoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_gpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11)
                    .addGroup(jPanel_gpoLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_agregargpo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel_gpoLayout.setVerticalGroup(
            jPanel_gpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_gpoLayout.createSequentialGroup()
                .addGroup(jPanel_gpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(btn_agregargpo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane_usuarios.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        jLabel1.setText("Conectados:");
        jScrollPane3.setViewportView(jLabel1);

        jScrollPane4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        jLabel2.setText("Desconectados:");
        jScrollPane4.setViewportView(jLabel2);

        listOnline.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listOnline.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listOnlineMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(listOnline);

        listOffline.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listOffline.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listOfflineMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(listOffline);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane_usuarios.addTab("Usuarios", jPanel1);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        jLabel3.setText("Conectados:");
        jScrollPane5.setViewportView(jLabel3);

        jScrollPane6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        jLabel4.setText("Desconectados:");
        jScrollPane6.setViewportView(jLabel4);

        addFriend.setText("Agregar amigo");
        addFriend.setToolTipText("");
        addFriend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFriendActionPerformed(evt);
            }
        });

        listOnlineFriends.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listOnlineFriends.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listOnlineFriendsMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(listOnlineFriends);

        listOfflineFriends.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listOfflineFriends.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listOfflineFriendsMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(listOfflineFriends);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addFriend, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addComponent(jScrollPane5)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane12))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(addFriend)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );

        jTabbedPane_usuarios.addTab("Amigos", jPanel3);

        tableRequests.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nombre de usuario", "Aceptar", "Rechazar"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableRequests.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableRequestsMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tableRequests);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
        );

        jTabbedPane_usuarios.addTab("Solicitudes", jPanel8);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel_gpo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(37, 37, 37))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTabbedPane_usuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(45, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jTabbedPane_usuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(jPanel_gpo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        messageArea.setColumns(20);
        messageArea.setRows(5);
        jScrollPane1.setViewportView(messageArea);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jButton1.setText("Enviar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        labelChatName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelChatName.setText("     Grupo");

        listConvo.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(listConvo);

        addGroupMember.setText("Agregar usuario al grupo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelChatName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addGroupMember)))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelChatName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addGroupMember)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_agregargpoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_agregargpoMouseClicked
        // TODO add your handling code here:
        agregar_gpo view = new agregar_gpo();
        view.setUserID(userID);
        view.setVisible(true);
        loadGroups();
    }//GEN-LAST:event_btn_agregargpoMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here
        if(ACTIVE_CONVO == 0)
            return;
        
        if(messageArea.getText().equals(""))
            return;
        
        con.addMessage(messageArea.getText(), userID, ACTIVE_CONVO, ACTIVE_CONVO_TYPE);
        loadConversation(ACTIVE_CONVO_USER_ID, ACTIVE_CONVO_USERNAME);
        messageArea.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void addFriendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFriendActionPerformed
        // TODO add your handling code here:
        agregar_amigo form = new agregar_amigo();
        form.setUserID(userID);
        form.setVisible(true);
    }//GEN-LAST:event_addFriendActionPerformed

    private void tableRequestsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableRequestsMouseClicked

    }//GEN-LAST:event_tableRequestsMouseClicked

    private void listOnlineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listOnlineMouseClicked
        // TODO add your handling code here:
        String value = (String)listOnline.getModel().getElementAt(listOnline.locationToIndex(evt.getPoint()));
        String[] data = value.split(",");
        String username = data[0].replaceAll("</html>", "").replaceAll("<b>", "").replaceAll("</b>", "");
        int userId = Integer.parseInt(data[1]);
        loadConversation(userId, username);
        ACTIVE_CONVO = con.getUserConversation(userId, userID);
        ACTIVE_CONVO_USERNAME = username;
        ACTIVE_CONVO_USER_ID = userId;
        ACTIVE_CONVO_TYPE = "user";
        labelChatName.setText(username);
        con.readMessage(ACTIVE_CONVO, userID);
        loadUsers();
    }//GEN-LAST:event_listOnlineMouseClicked

    private void listOfflineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listOfflineMouseClicked
        // TODO add your handling code here:
        String value = (String)listOffline.getModel().getElementAt(listOffline.locationToIndex(evt.getPoint()));
        String[] data = value.split(",");
        String username = data[0].replaceAll("</html>", "").replaceAll("<b>", "").replaceAll("</b>", "");
        int userId = Integer.parseInt(data[1]);
        loadConversation(userId, username);
        ACTIVE_CONVO = con.getUserConversation(userId, userID);
        ACTIVE_CONVO_USERNAME = username;
        ACTIVE_CONVO_USER_ID = userId;
        ACTIVE_CONVO_TYPE = "user";
        labelChatName.setText(username);
        con.readMessage(ACTIVE_CONVO, userID);
        loadUsers();
        
    }//GEN-LAST:event_listOfflineMouseClicked

    private void listOnlineFriendsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listOnlineFriendsMouseClicked
        // TODO add your handling code here:
        String value = (String)listOnlineFriends.getModel().getElementAt(listOnlineFriends.locationToIndex(evt.getPoint()));
        String[] data = value.split(",");
        String username = data[0].replaceAll("</html>", "").replaceAll("<b>", "").replaceAll("</b>", "");
        int userId = Integer.parseInt(data[1]);
        loadConversation(userId, username);
        ACTIVE_CONVO = con.getUserConversation(userId, userID);
        ACTIVE_CONVO_USERNAME = username;
        ACTIVE_CONVO_USER_ID = userId;
        ACTIVE_CONVO_TYPE = "user";
        labelChatName.setText(username);
        con.readMessage(ACTIVE_CONVO, userID);
        loadUsers();
    }//GEN-LAST:event_listOnlineFriendsMouseClicked

    private void listOfflineFriendsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listOfflineFriendsMouseClicked
        // TODO add your handling code here:
        String value = (String)listOfflineFriends.getModel().getElementAt(listOfflineFriends.locationToIndex(evt.getPoint()));
        String[] data = value.split(",");
        String username = data[0].replaceAll("</html>", "").replaceAll("<b>", "").replaceAll("</b>", "");
        int userId = Integer.parseInt(data[1]);
        loadConversation(userId, username);
        ACTIVE_CONVO = con.getUserConversation(userId, userID);
        ACTIVE_CONVO_USERNAME = username;
        ACTIVE_CONVO_USER_ID = userId;
        ACTIVE_CONVO_TYPE = "user";
        labelChatName.setText(username);
        con.readMessage(ACTIVE_CONVO, userID);
        loadUsers();
    }//GEN-LAST:event_listOfflineFriendsMouseClicked

    private void listGroupsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listGroupsMouseClicked
        // TODO add your handling code here:
        String value = (String)listOfflineFriends.getModel().getElementAt(listOfflineFriends.locationToIndex(evt.getPoint()));
        String[] data = value.split(",");
        String username = data[0].replaceAll("</html>", "").replaceAll("<b>", "").replaceAll("</b>", "");
        int userId = Integer.parseInt(data[1]);
        loadConversation(userId, username);
        ACTIVE_CONVO = con.getUserConversation(userId, userID);
        ACTIVE_CONVO_USERNAME = username;
        ACTIVE_CONVO_USER_ID = userId;
        ACTIVE_CONVO_TYPE = "group";
        labelChatName.setText(username);
        con.readMessage(ACTIVE_CONVO, userID);
        loadUsers();
    }//GEN-LAST:event_listGroupsMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addFriend;
    private javax.swing.JButton addGroupMember;
    private javax.swing.JLabel btn_agregargpo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel_gpo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane_usuarios;
    private javax.swing.JLabel labelChatName;
    private javax.swing.JList<String> listConvo;
    private javax.swing.JList<String> listGroups;
    private javax.swing.JList<String> listOffline;
    private javax.swing.JList<String> listOfflineFriends;
    private javax.swing.JList<String> listOnline;
    private javax.swing.JList<String> listOnlineFriends;
    private javax.swing.JTextArea messageArea;
    private javax.swing.JTable tableRequests;
    // End of variables declaration//GEN-END:variables
}