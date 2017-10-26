/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author VRPG1
 */
public class DataBaseConnection {
    
    private Connection con;
    
    public DataBaseConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/cocochat", "root", "");
         } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("/C: " + ex.getMessage());
        }
    }
    
    /**
     * Función para iniciar sesión
     * 
     * @param username Nombre de usuario que desea iniciar sesión
     * @param password Contraseña de usuario que desea iniciar sesión
     * @return ID del usuario ingresado o -1 si no se encuentran registros
     * en la base de datos
     */
    public int getUser(String username, String password) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE usr_Name = '" + username + "' AND usr_password = '" + password +"'");
            ResultSet set = ps.executeQuery();
            int count = 0;
            
            while(set.next()) {
                count++;
            }
            
            if(count != 1)
                return -1;
            set.last();
            return set.getInt("PK_usr_ID");
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    /**
     * Obtiene los datos del usuario
     * 
     * @param userId Número de identificación del usuario
     * @return Resultado de la consulta a la base de datos
     */
    public ResultSet getUser(int userId) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE PK_usr_ID = " + userId);
            ResultSet set = ps.executeQuery();
            return set;
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     *  Función para registrar usuarios en la base de datos
     * 
     * @param username Nombre de usuario a registrar
     * @param password Contraseña de usuario a registrar
     */
    public void addUser(String username, String password) {
        try {
            String query = "INSERT INTO users VALUES(NULL,\"" + username + "\", \"" + password +"\", \"0\")";
            PreparedStatement ps = con.prepareStatement(query);
            if(!ps.execute())
                JOptionPane.showMessageDialog(null, "Usuario registrado con exito");
            else
                JOptionPane.showMessageDialog(null, "Error al registrar usuario");
        } catch(HeadlessException | SQLException e) {
            System.out.println("/addUser: " + e.getMessage());
        }
    }
    
    /**
     *  Función para cambiar el estado del usuario
     * 
     * @param userId Número identificador del usuario
     */
    public void loginUser(int userId) {
        try {
            String query = "UPDATE users SET usr_state = 1 WHERE PK_usr_ID = " + userId;
            PreparedStatement ps = con.prepareStatement(query);
            if(ps.executeUpdate() != 0)
                System.out.println("Conectado");
            else
                System.out.println("Error al conectar");
        } catch(SQLException e) {
            System.out.println("/loginUser: " + e.getMessage());
        }
    }
    
    /**
     *  Función para cambiar el estado del usuario
     * 
     * @param userId Número identificador del usuario
     */
    public void logoutUser(int userId) {
        try {
            String query = "UPDATE users SET usr_state = 0 WHERE PK_usr_ID = " + userId;
            PreparedStatement ps = con.prepareStatement(query);
            if(ps.executeUpdate() != 0)
                System.out.println("Desconectado");
            else
                System.out.println("Error al desconectar");
        } catch(SQLException e) {
            System.out.println("/logoutUser: " + e.getMessage());
        }
    }
    
    /**
     * Función para obtener los amigos del usuario
     * 
     * @param userID Número identificador del usuario
     * @return 
     */
    public ResultSet getFriends(int userID) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user_friends WHERE FK_USR_ID = " + userID);
            ResultSet res = ps.executeQuery();
            
            return res;
        } catch (SQLException ex) {
            System.out.println("/getFriends: " + ex.getMessage());
        }
        return null;
    }
    
    /**
     * Función para obtener la cantidad de amigos de un usuario
     * 
     * @param userID
     * @return 
     */
    public int getFriendsCount(int userID) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user_friends WHERE FK_USR_ID = " + userID);
            ResultSet res = ps.executeQuery();
            int count = 0;
            
            while(res.next()) {
                count++;
            }
            
            return count;
        } catch (SQLException ex) {
            System.out.println("/getFriendsCount: " + ex.getMessage());
        }
        return -1;
    }
    
    public void addFriendRequest(int sender, int target) {
        try {
            String query = "INSERT INTO friend_requests VALUES(NULL," + sender + ", " + target +", 0)";
            PreparedStatement ps = con.prepareStatement(query);
            if(!ps.execute())
                JOptionPane.showMessageDialog(null, "Solicitud enviada");
            else
                JOptionPane.showMessageDialog(null, "Error al enviar la solicitud");
        } catch(SQLException e) {
            System.out.println("/addFriendRequest" + e.getMessage());
        }
    }
    
    public ResultSet getRequest(int requestID) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM friend_requests WHERE FK_target_id = " + requestID);
            ResultSet res = ps.executeQuery();
            
            return res;
        } catch (SQLException ex) {
            System.out.println("/getRequest: " + ex.getMessage());
        }
        return null;
    }
    
    public ResultSet getRequestById(int requestID) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM friend_requests WHERE PK_request_id = " + requestID);
            ResultSet res = ps.executeQuery();
            
            return res;
        } catch (SQLException ex) {
            System.out.println("/getRequest: " + ex.getMessage());
        }
        return null;
    }
    
    public int getRequestCount(int requestID) {
        try {
            String query = "SELECT * FROM friend_requests WHERE req_state = 0 AND FK_target_id = " + requestID;
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet res = ps.executeQuery();
            int count = 0;
            
            while(res.next()) {
                count++;
            }
            
            return count;
        } catch (SQLException e) {
            System.out.println("/getPendingRequests: " + e.getMessage());
        }
        return -1;
    }
    
    public void addFriend(int requestID) {
        try {
            ResultSet request = getRequestById(requestID);
            request.next();
            String query = "INSERT INTO user_friends VALUES (NULL, " + request.getInt(2) + ", " + request.getInt(3) + ")";
            String query2 = "INSERT INTO user_friends VALUES (NULL, " + request.getInt(3) + ", " + request.getInt(2) + ")";
            PreparedStatement ps = con.prepareStatement(query);
            PreparedStatement ps2 = con.prepareStatement(query2);
            ps2.execute();
            if(!ps.execute())
                System.out.println("Amigo agregado");
            else
                System.out.println("Error al agregar amigo");
        } catch(SQLException e) {
            System.out.println("/addFriend: " + e.getMessage());
        }
    }
    
    public void acceptFriend(int request, int response) {
        try {
            String query = "UPDATE friend_requests SET req_state = " + response + "  WHERE PK_request_id = " + request;
            PreparedStatement ps = con.prepareStatement(query);
            ps.execute();
            
            if(response == 1) {
                addFriend(request);
            }
        } catch(Exception e) {
            System.out.println("/acceptFriend: " + e.getMessage());
        }
    }
    
    public int getUserId(String username) {
        try {
            String query = "SELECT PK_usr_ID FROM users WHERE usr_Name = \"" + username + "\"";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet res = ps.executeQuery();
            
            if(res.next())
                return res.getInt(1);
            return -1;
        } catch(SQLException e) {
            System.out.println("/getUserId: " + e.getMessage());
        }
        return -1;
    }
    
    public ResultSet getPendingRequests(int userId) {
        try {
            String query = "SELECT * FROM friend_requests WHERE req_state = 0 AND FK_target_id = " + userId;
            PreparedStatement ps = con.prepareStatement(query);
            
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("/getPendingRequests: " + e.getMessage());
        }
        return null;
    }
    
    public ResultSet getOnlineUsers() {
        try {
            String query = "SELECT * FROM users WHERE usr_state = 1";
            PreparedStatement ps = con.prepareStatement(query);
            
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("/getPendingRequests: " + e.getMessage());
        }
        return null;
    }
    
    public ResultSet getOfflineUsers() {
        try {
            String query = "SELECT * FROM users WHERE usr_state = 0";
            PreparedStatement ps = con.prepareStatement(query);
            
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("/getPendingRequests: " + e.getMessage());
        }
        return null;
    }
    
    public ResultSet getOnlineUsers(int friendID) {
        try {
            String query = "SELECT users.* FROM user_friends JOIN users ON user_friends.FK_Friend_id = users.PK_usr_ID WHERE users.usr_state = 1 AND FK_USR_ID = " + friendID;
            PreparedStatement ps = con.prepareStatement(query);
            
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("/getPendingRequests: " + e.getMessage());
        }
        return null;
    }
    
    public ResultSet getOfflineUsers(int friendID) {
        try {
            String query = "SELECT users.* FROM user_friends JOIN users ON user_friends.FK_Friend_id = users.PK_usr_ID WHERE users.usr_state = 0 AND FK_USR_ID = " + friendID;
            PreparedStatement ps = con.prepareStatement(query);
            
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("/getPendingRequests: " + e.getMessage());
        }
        return null;
    }
    
    public int addConversation(int userA, int userB) {
        try {
            String query = "INSERT INTO conversations VALUES(NULL, " + userA + ", " + userB + ")";
            PreparedStatement ps = con.prepareStatement(query);
            ps.execute();
            
            return getUserConversation(userA, userB);
        } catch (SQLException e) {
            System.out.println("/addConversation: " + e.getMessage());
        }
        return -1;
    }
    
    public int getUserConversation(int userA, int userB) {
        try {
            String query = "SELECT PK_convo_ID FROM conversations WHERE (FK_usr_A = " + userA + " AND FK_usr_B = " + userB + ") OR (FK_usr_A = " + userB + " AND FK_usr_B = " + userA + ")";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet res = ps.executeQuery();
            
            if(!res.next())
                return addConversation(userA, userB);
            return res.getInt(1);
        } catch (SQLException e) {
            System.out.println("/getUserConversation: " + e.getMessage());
        }
        return -1;
    }
    
    public ResultSet getConversation(int convo) {
        try {
            String query = "SELECT * FROM messages WHERE FK_convo_ID = " + convo;
            PreparedStatement ps = con.prepareStatement(query);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("/getConversation: " + e.getMessage());
        }
        return null;
    }
    
    public void addMessage(String message, int userId, int convo, String type) {
        try {
            String query = "INSERT INTO messages VALUES(NULL, " + convo + ", " + userId + ", \"" + message + "\", \"" + type + "\", 0)";
            PreparedStatement ps = con.prepareStatement(query);
            if(!ps.execute())
                System.out.println("Mensaje enviado");
            else
                System.out.println("Mensaje enviado");
        } catch(SQLException e) {
            System.out.println("/addMessage: " + e.getMessage());
        }
    }
    
    public void readMessage(int convo, int userSent) {
        try {
            String query = "UPDATE messages SET msg_state = 1 WHERE FK_convo_ID = " + convo + " AND FK_usr_ID <> " + userSent;
            PreparedStatement ps = con.prepareStatement(query);
            if(ps.execute())
                System.out.println("Mensaje leído");
        } catch (SQLException e) {
            System.out.println("/readMessage: " + e.getMessage());
        }
    }
    
    public ResultSet getUnreadConvos(int convo, int userId, String type) {
        try {
            String query = "SELECT FK_usr_ID FROM messages WHERE msg_state = 0 AND msg_type = \"" + type + "\" AND FK_convo_ID = " + convo + " AND FK_usr_ID <> " + userId;
            PreparedStatement ps = con.prepareStatement(query);
            return ps.executeQuery();
        } catch(Exception e) {
            System.out.println("/getUnreadConvos: " + e.getMessage());
        }
        return null;
    }
    
    public void createGroup(String groupName, int creator) {
        try {
            int validate = getGroupId(groupName);
            if (validate != -1){
                JOptionPane.showMessageDialog(null, "Nombre de grupo no válido");
                return;
            }
            String query  = "INSERT INTO groups VALUES(NULL, \"" + groupName + "\")";
            PreparedStatement ps = con.prepareStatement(query);
            if(!ps.execute()) {
                System.out.println("Grupo creado");
                validate = getGroupId(groupName);
                addGroupMember(validate, creator);
            } else
                System.out.println("Error al crear el grupo");
        } catch (SQLException e) {
            System.out.println("/createGroup: " + e.getMessage());
        }
    }
    
    public int getGroupId(String groupName) {
        try {
            String query  = "SELECT PK_Group_ID FROM groups WHERE group_name = \"" + groupName + "\"";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet res = ps.executeQuery();            
            
            if(res.next())
                return res.getInt(1);
            return -1;
        } catch (SQLException e) {
            System.out.println("/getGroupId: " + e.getMessage());
        }
        return -1;
    }
    
    public void addGroupMember(int groupID, int userID) {
        try {
            String query = "INSERT INTO group_members VALUES(NULL, " + groupID + ", " + userID + ")";
            PreparedStatement ps = con.prepareStatement(query);
            
            if(ps.execute())
                System.out.println("Miembro agregado al group");
            else
                System.out.println("Error al agregar miembro al grupo");
        } catch (SQLException e) {
            System.out.println("/addGroupMember: " + e.getMessage());
        }
    }
    
    public void addGroupMessage(int groupID, int userID, String message){
        try {
            String query = "INSERT INTO group_messages VALUES (NULL, " + groupID + ", " + userID + ", \"" + message + "\")";
            PreparedStatement ps = con.prepareStatement(query);
            if(!ps.execute())
                System.out.println("Mensaje enviado");
            else
                System.out.println("Error al enviar mesaje");
        } catch (SQLException e) {
            System.out.println("/addGroupMessage: " + e.getMessage());
        }
    }
    
    public ResultSet getGroups(int userID) {
        try {
            String query = "SELECT groups.PK_Group_ID, groups.group_name FROM group_members JOIN groups WHERE group_members.FK_usr_ID = " + userID;
            PreparedStatement ps = con.prepareStatement(query);
            System.out.println(userID);
            return ps.executeQuery();
        } catch(SQLException e) {
          System.out.println("/getGroupsId: " + e.getMessage());
        }
        return null;
    }
    
    public ResultSet getGroupsId(int userID) {
        try {
            String query = "SELECT FK_grp_ID FROM group_members WHERE FK_usr_ID = " + userID;
            PreparedStatement ps = con.prepareStatement(query);
            return ps.executeQuery();
        } catch(SQLException e) {
          System.out.println("/getGroupsId: " + e.getMessage());
        }
        return null;
    }
    
    private ResultSet getUserReadGrupoMessages(int userID) {
        try {
            String query = "SELECT FK_grp_ID, COUNT(FK_grp_ID) FROM group_message_seen WHERE FK_usr_ID = " + userID + " GROUP BY FK_grp_ID";
            PreparedStatement ps = con.prepareStatement(query);
            
            return ps.executeQuery();
        } catch(SQLException e) {
          System.out.println("/getGroupsId: " + e.getMessage());
        }
        return null;
    }
    
    private ResultSet getGroupsMessagesCount(int userID) {
        try {
            String query = "SELECT FK_grp_ID, COUNT(FK_grp_ID) FROM group_messages WHERE FK_usr_ID = " + userID;
            PreparedStatement ps = con.prepareStatement(query);
            return ps.executeQuery();
        } catch(SQLException e) {
          System.out.println("/getGroupsId: " + e.getMessage());
        }
        return null;
    }
    
    public ArrayList<Integer> getUnreadGroupMessages(int userID) {
        try {
            ResultSet groups = getGroupsId(userID);
            ResultSet messages = getUserReadGrupoMessages(userID);
            ArrayList<Integer> id = new ArrayList<>();
            
            while(messages.next()) {
                while(groups.next()) {
                    if(messages.getInt(1) == groups.getInt(1) && messages.getInt(2) != messages.getInt(2)) {
                        id.add(groups.getInt(2));
                    }
                }
            }
            
            return id;
        } catch(Exception e) {
            System.out.println("/getUnreadMessages: " + e.getMessage());
        }
        return null;
    }
}
