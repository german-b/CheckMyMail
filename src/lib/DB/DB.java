package lib.DB;

import java.sql.*;
import java.util.HashMap;

/**
 * Created by German on 25.11.2015.
 *
 * Primary class that connects to the local SQLite DB and runs the queries.
 * Input check should have already been done on the application side.
 *
 */
public class DB {
    Connection conn = null;
    Statement stat = null;

    public DB() {
        createConn();
        createTables();
    }

    private void createConn() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:cmm.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        System.out.println("DB Connected.");
    }

    public void createTables() {
        String sql1 = "CREATE TABLE IF NOT EXISTS USERS (ID INT AUTO_INCREMENT, USERNAME TEXT, " +
                "PASSWORD TEXT);";
        String sql2= "CREATE TABLE IF NOT EXISTS SETTINGS (DELAY INT DEFAULT 1, SSL BOOLEAN DEFAULT 1, " +
                "COUNT INT DEFAULT 5, PROTOCOL TEXT DEFAULT 'imap', HOST TEXT DEFAULT 'imap.gmail.com', PORT INT DEFAULT 993);";

        String sql3 = "INSERT INTO SETTINGS DEFAULT VALUES;";

        updateTable(sql1);
        updateTable(sql2);
        updateTable(sql3);
    }

    private void updateTable(String sql) {
        try {
            stat = conn.createStatement();
            stat.executeUpdate(sql);
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createUser(String username, String password) {
        String sql = "INSERT INTO USERS (USERNAME, PASSWORD) VALUES ('"+username+"','"+password+"');";
        updateTable(sql);
    }

/*    public boolean login(String username, String password) { //Not needed at this point
        try {
            Statement stat = conn.createStatement();

            // SELECT on nagu excelis hiirega "selekteeriks" mingeid kaste. SQLis tähendab konkreetselt,
            // et milliste tulpade infot soovid kätte saada. WHERE'ga käsed välja võtta ainult tingimustele
            // vastavad väljad.
            String sql = "SELECT * FROM USERS WHERE USERNAME = '" + username + "' LIMIT 1;";

            // Kuna tegu on päringuga siis käsuks on executeQuery ja ta tagastab andme objekti ResultSet.
            ResultSet rs = stat.executeQuery(sql);

            // Kui Query andmeid ei tagastanud (päring ei toonud tulemusi) siis rs-i kasutada ei saa.
            // Seepärast, kui kasutajat ei eksisteeri tuleb lihtsalt error ja "return" käsuni ei jõutagi.
            // Aga jõutakse lõpu "return false" käsuni küll.
            String dbPassword = rs.getString("password");

            rs.close();
            stat.close();
            return password.equals(dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return false;
    }*/

    public void closeConn() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("DB Connection closed.");
    }

    public HashMap getUser() {
        HashMap user = new HashMap();
        try {
            stat = conn.createStatement();
            String sql = "SELECT * FROM USERS LIMIT 1;";

            ResultSet rs = stat.executeQuery(sql);
            if(rs.next()){
                user.put("username", rs.getString("username"));
                user.put("password", rs.getString("password"));
            }
            rs.close();
            stat.close();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return user;
    }

    public int getCount(){
        int count = 5;
        try {
            stat = conn.createStatement();
            String sql = "SELECT * FROM SETTINGS LIMIT 1;";
            ResultSet rs = stat.executeQuery(sql);
            if(rs.next()) {
                count = rs.getInt("count");
            }
            rs.close();
            stat.close();

            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean getSSL(){
        boolean ssl = false;
        try {
            stat = conn.createStatement();
            String sql = "SELECT * FROM SETTINGS LIMIT 1;";
            ResultSet rs = stat.executeQuery(sql);
            if(rs.next()) {
                ssl = rs.getBoolean("ssl");
            }
            rs.close();
            stat.close();

            return ssl;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ssl;
    }

    public int getDelay() {
        int delay = 1;
        try {
            stat = conn.createStatement();
            String sql = "SELECT * FROM SETTINGS LIMIT 1;";
            ResultSet rs = stat.executeQuery(sql);
            if (rs.next()) {
                delay = rs.getInt("delay");
            }
            rs.close();
            stat.close();

            return delay;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return delay;
    }

    public void setCount(int emailCount) {

        try {
            Statement stat = conn.createStatement();
            String sql = "UPDATE SETTINGS SET COUNT = " + emailCount;
            stat.executeUpdate(sql);
            System.out.println("Count set to: " + emailCount);
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void setDelay(int delay) {

        try {
            Statement stat = conn.createStatement();
            String sql = "UPDATE SETTINGS SET DELAY = " + delay;
            stat.executeUpdate(sql);
            System.out.println("Delay set to: " + delay);
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void setSSL(boolean ssl) {
        int s = 0;
        if (ssl){
            s = 1;
        }

        try {
            Statement stat = conn.createStatement();
            String sql = "UPDATE SETTINGS SET SSL = " + s;
            stat.executeUpdate(sql);
            System.out.println("SSL set to: " + ssl);
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void deleteUser() {
        try {
            stat = conn.createStatement();
            String sql = "DELETE FROM USERS;";
            stat.executeUpdate(sql);
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getProtocol() {
        String protocol = null;
        try {
            stat = conn.createStatement();
            String sql = "SELECT * FROM SETTINGS LIMIT 1;";
            ResultSet rs = stat.executeQuery(sql);
            if (rs.next()) {
                protocol = rs.getString("protocol");
            }
            rs.close();
            stat.close();

            return protocol;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return protocol;
    }

    public String getHost() {
        String host = null;
        try {
            stat = conn.createStatement();
            String sql = "SELECT * FROM SETTINGS LIMIT 1;";
            ResultSet rs = stat.executeQuery(sql);
            if (rs.next()) {
                host = rs.getString("host");
            }
            rs.close();
            stat.close();

            return host;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return host;
    }

    public int getPort() {
        int port = 993;
        try {
            stat = conn.createStatement();
            String sql = "SELECT * FROM SETTINGS LIMIT 1;";
            ResultSet rs = stat.executeQuery(sql);
            if (rs.next()) {
                port = rs.getInt("port");
            }
            rs.close();
            stat.close();

            return port;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return port;
    }
}