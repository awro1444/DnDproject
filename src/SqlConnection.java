import java.sql.*;
import java.util.ArrayList;

public class SqlConnection{

    SqlCredentials credentials = new SqlCredentials();
    //ComboBoxHelper combo = new ComboBoxHelper();
    Connection conn;
    SqlPhp php = new SqlPhp();

    String getData(){

        String data = "";
        try{
            Statement myStm = conn.createStatement();
            String sql = "Select * from testtable";
            ResultSet rs = myStm.executeQuery(sql);

            while( rs.next() ){
                data += rs.getString( "id" ) + "_" + rs.getString( "name" ) + "_"
                        + rs.getString("class") + "_" + rs.getString("race")
                        + "_" + rs.getString("level") + ";";
                System.out.println( data );
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }

        return data;
    }

    String getCharacters(int userId){

        String data = "";
        try{
            Statement myStm = conn.createStatement();
            String sql = "Select characterID, name, race, class, lvl from characters WHERE userID=" + userId;
            //php.insertData(sql);
            ResultSet rs = myStm.executeQuery(sql);

            while( rs.next() ){
                data += rs.getString("characterID") + "_" + rs.getString( "name" ) + "_" + rs.getString( "race" ) + "_"
                        + rs.getString("class") + "_" + rs.getString("lvl")
                        + ";";
                System.out.println( data );
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }

        return data;
    }

    String getCurrentChar(int charId){

        String data = "";
        try{
            Statement myStm = conn.createStatement();
            String sql = "Select stats, name, race, class, lvl from characters WHERE characterID=" + charId;
            ResultSet rs = myStm.executeQuery(sql);

            if( rs.next() != false ){
                data += rs.getString("stats") + "_" + rs.getString( "name" ) + "_" + rs.getString( "race" ) + "_"
                        + rs.getString("class") + "_" + rs.getString("lvl");
                System.out.println( data );
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }

        return data;
    }

    boolean updateCharacterData(String characterId, String name, String race, String klasa, String lvl, String json){
        try{
            Statement myStm = conn.createStatement();
            //String sql = "UPDATE characters SET stats='" + json + "' WHERE characterID=" + characterId;
            String sql = "UPDATE characters SET name='" + name + "', race="
                    + race + ", class=" + klasa + ", lvl=" + lvl + ", stats='" + json +"' WHERE characterID="+ characterId;
            System.out.println(sql);
            Integer rs = myStm.executeUpdate(sql);
            System.out.println(rs.toString());
        }
        catch (SQLException e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    String checkUsername( String username ){

        String data = "";
        try{
            Statement myStm = conn.createStatement();
            String sql = "Select * from users WHERE username='" + username + "'";
            ResultSet rs = myStm.executeQuery(sql);

            if( rs.next() != false ){
                data += rs.getString( "password" );
                System.out.println( data );
            }
            else{
                return data = "000";
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }

        return data;
    }

    boolean addUser(String username, String password){
        try{
            Statement myStm = conn.createStatement();
            String sql = "INSERT INTO users(username,password) VALUES('" + username + "','" + password + "')";
            System.out.println(sql);
            Integer rs = myStm.executeUpdate(sql);
            System.out.println(rs.toString());
        }
        catch (SQLException e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    boolean addData(String name, String race, String klasa, String lvl, String userID){
        try{
            Statement myStm = conn.createStatement();
            String sql = "INSERT INTO characters(name,race,class, lvl, stats, userID) VALUES('" + name + "',"
                    + race + "," + klasa + "," + lvl + ",'[]',"+ userID +" )";
            System.out.println(sql);
            Integer rs = myStm.executeUpdate(sql);
            System.out.println(rs.toString());
        }
        catch (SQLException e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    boolean deleteData(String id){
        try{
            Statement myStm = conn.createStatement();
            String sql = "DELETE FROM characters WHERE characterID=" +id;
            System.out.println(sql);
            Integer rs = myStm.executeUpdate(sql);
            System.out.println(rs.toString());
        }
        catch (SQLException e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    public ArrayList<ComboBoxHelper> getRaceList(){

        ArrayList<ComboBoxHelper> list = new ArrayList<>();


        try{
            Statement myStm = conn.createStatement();
            String sql = "Select raceID, race from races;";
            ResultSet rs = myStm.executeQuery(sql);

            while( rs.next() ){
                list.add( new ComboBoxHelper( Integer.parseInt(rs.getString("raceID")), rs.getString("race")));
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }

        return list;
    }

    public ArrayList<ComboBoxHelper> getClassList(){

        ArrayList<ComboBoxHelper> list = new ArrayList<>();


        try{
            Statement myStm = conn.createStatement();
            String sql = "Select classID, class from classes;";
            ResultSet rs = myStm.executeQuery(sql);

            while( rs.next() ){
                list.add( new ComboBoxHelper( Integer.parseInt(rs.getString("classID")), rs.getString("class")));
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }

        return list;
    }


    SqlConnection(){

        try {
            conn = DriverManager.getConnection( credentials.getUrl(), credentials.getUser(), credentials.getPassword());
        }
        catch (SQLException e){
            System.out.println(e);
        }

    }

}

