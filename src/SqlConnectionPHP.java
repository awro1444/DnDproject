import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;
import java.util.concurrent.ThreadLocalRandom;


import java.sql.*;
import java.util.ArrayList;

public class SqlConnectionPHP {

    SqlPhp php = new SqlPhp();

    String getCharacters(int userId){

        String data = "";
        try{
            String sql = "Select characterID, name, race, class, lvl from characters WHERE userID=" + userId;
            String result = php.insertData(sql);
            //System.out.println(result);

            JsonArray jarray = new JsonParser().parse(result).getAsJsonArray();

            for( int i = 0; i < jarray.size(); i++ ) {
                String characterID = jarray.get(i).getAsJsonObject().get("characterID").getAsString();
                String name = jarray.get(i).getAsJsonObject().get("name").getAsString();
                String race = jarray.get(i).getAsJsonObject().get("race").getAsString();
                String klasa = jarray.get(i).getAsJsonObject().get("class").getAsString();
                String lvl = jarray.get(i).getAsJsonObject().get("lvl").getAsString();
                data += characterID + "_" + name + "_" + race + "_" + klasa + "_" + lvl + ";";
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

        return data;
    }

    String getConnectedCharacters(int userId){

        String data = "";
        try{
            String sql = "SELECT `characterID`, `name`, `race`, `class`, `lvl`, users.username FROM `characters` JOIN dungeonMasters " +
                    "ON characters.characterID=dungeonMasters.charID JOIN users ON characters.userID=users.userID WHERE dmID=" + userId;
            String result = php.insertData(sql);
            //System.out.println(result);

            JsonArray jarray = new JsonParser().parse(result).getAsJsonArray();

            for( int i = 0; i < jarray.size(); i++ ) {
                String characterID = jarray.get(i).getAsJsonObject().get("characterID").getAsString();
                String name = jarray.get(i).getAsJsonObject().get("name").getAsString();
                String race = jarray.get(i).getAsJsonObject().get("race").getAsString();
                String klasa = jarray.get(i).getAsJsonObject().get("class").getAsString();
                String lvl = jarray.get(i).getAsJsonObject().get("lvl").getAsString();
                String username = jarray.get(i).getAsJsonObject().get("username").getAsString();
                data += characterID + "_" + name + "_" + race + "_" + klasa + "_" + lvl + "_" + username + ";";
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

        return data;
    }

    String getCurrentChar(int charId){

        String data = "";
        try{
            String sql = "Select stats, name, race, class, lvl, users.username from characters JOIN users ON characters.userID=users.userID WHERE characterID=" + charId;
            String result = php.insertData(sql);
            //System.out.println(result);

            JsonArray jarray = new JsonParser().parse(result).getAsJsonArray();

            for( int i = 0; i < jarray.size(); i++ ) {
                String stats = jarray.get(i).getAsJsonObject().get("stats").getAsString();
                String name = jarray.get(i).getAsJsonObject().get("name").getAsString();
                String race = jarray.get(i).getAsJsonObject().get("race").getAsString();
                String klasa = jarray.get(i).getAsJsonObject().get("class").getAsString();
                String lvl = jarray.get(i).getAsJsonObject().get("lvl").getAsString();
                String username = jarray.get(i).getAsJsonObject().get("username").getAsString();
                data += stats + "_" + name + "_" + race + "_" + klasa + "_" + lvl + "_" + username;
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

        return data;
    }

    boolean updateCharacterData(String characterId, String name, String race, String klasa, String lvl, String json){
        try{
            String sql = "UPDATE characters SET name='" + name + "', race="
                    + race + ", class=" + klasa + ", lvl=" + lvl + ", stats='" + json +"' WHERE characterID="+ characterId;
            String result = php.insertData(sql);
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    String checkUsername( String username ){

        String data = "";
        try{
            String sql = "Select password from users WHERE username='" + username + "'";
            String result = php.insertData(sql);
            //System.out.println("jaaaa");

            //JsonObject jobject = new JsonParser().parse(result).getAsJsonObject();
            JsonArray jarray = new JsonParser().parse(result).getAsJsonArray();

            for( int i = 0; i < jarray.size(); i++ ) {
                String password = jarray.get(i).getAsJsonObject().get("password").getAsString();
                data += password;
            }

            //System.out.println("jaaaa2");


            /*if( jobject != null ){
                data += jobject.get("password").getAsString();
                System.out.println( data );
            }
            else{
                return data = "000";
            }*/
            if (data.equals("")) {
                return data = "000";
            }else {
                return data;
            }
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println("Blaaaaaad");
            data = "000";
        }

        return data;
    }

    String getUserID(String username) {


        String id ="";
        try {
            String sql = "Select userID from users WHERE username='" + username + "'";
            String result = php.insertData(sql);
            JsonArray jarray = new JsonParser().parse(result).getAsJsonArray();

            if (jarray != null) {
                id += jarray.get(0).getAsJsonObject().get("userID").getAsString();
                System.out.println(id);
            } else {
                return id = "100000";

            }
        }
        catch (Exception e) {
            System.out.println(e);
            System.out.println("blad getUserID");
        }

        return id;
    }

    boolean addUser(String username, String password){
        try{
            String sql = "INSERT INTO users(username,password) VALUES('" + username + "','" + password + "')";
            String result = php.insertData(sql);
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    boolean addData(String name, String race, String klasa, String lvl, String userID){
        try{
            String sql = "INSERT INTO characters(name,race,class, lvl, stats, userID) VALUES('" + name + "',"
                    + race + "," + klasa + "," + lvl + ",'[]',"+ userID +" )";
            String result = php.insertData(sql);
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    boolean deleteData(String id){
        try{
            String sql = "DELETE FROM characters WHERE characterID=" +id;
            String result = php.insertData(sql);
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    public ArrayList<ComboBoxHelper> getRaceList(){

        ArrayList<ComboBoxHelper> list = new ArrayList<>();


        try{
            String sql = "Select raceID, race from races;";
            String result = php.insertData(sql);

            JsonArray jarray = new JsonParser().parse(result).getAsJsonArray();

            for( int i = 0; i < jarray.size(); i++ ) {
                list.add(new ComboBoxHelper( Integer.parseInt(jarray.get(i).getAsJsonObject().get("raceID").getAsString()),
                        jarray.get(i).getAsJsonObject().get("race").getAsString()));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

        return list;
    }

    public ArrayList<ComboBoxHelper> getClassList(){

        ArrayList<ComboBoxHelper> list = new ArrayList<>();


        try{
            String sql = "Select classID, class from classes;";
            String result = php.insertData(sql);

            JsonArray jarray = new JsonParser().parse(result).getAsJsonArray();

            for( int i = 0; i < jarray.size(); i++ ) {
                list.add(new ComboBoxHelper( Integer.parseInt(jarray.get(i).getAsJsonObject().get("classID").getAsString()),
                        jarray.get(i).getAsJsonObject().get("class").getAsString()));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

        return list;
    }

    private int roll(int min, int max) {
        int randomNum = ThreadLocalRandom.current().nextInt(min, max +1);
        return randomNum;
    }

    String generateCode(int charID) {

        String codeDM = "";
        String sql = "Select dmCode from characters WHERE characterID=" + charID;
        String result = php.insertData(sql);
        JsonArray jarray = new JsonParser().parse(result).getAsJsonArray();

        if (jarray.get(0).getAsJsonObject().get("dmCode").isJsonNull()) {

            String id = charID + "";
            int length1 = id.length();
            codeDM += "" + roll(0, 9) + length1 + roll(0, 9) + id ;
            for (int i=0; i<5 - length1 ; i++ ) {
                codeDM += roll(0,9);
            }

            sql = "UPDATE characters SET dmCode=" + codeDM + " WHERE characterID=" + charID;
            result = php.insertData(sql);

        } else {
             codeDM += jarray.get(0).getAsJsonObject().get("dmCode").getAsString();
        }

        return codeDM;
    }

    Boolean checkDm( int dmId, int charId ){

        Boolean data = false;
        try{
            String sql = "SELECT COUNT(dmID) from dungeonMasters WHERE dmID=" + dmId + " AND charID=" + charId;
            String result = php.insertData(sql);

            JsonArray jarray = new JsonParser().parse(result).getAsJsonArray();
            int count = Integer.parseInt(jarray.get(0).getAsJsonObject().get("COUNT(dmID)").getAsString());

            if( count == 1 )
                data = false;
            else if( count == 0 )
                data = true;
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println("Blaaaaaad");
            data = false;
        }

        return data;
    }

    Boolean addDm( int dmId, int charId ){

        try{
            String sql = "INSERT INTO dungeonMasters(dmID, charID) VALUES(" + dmId + "," + charId + ")";
            String result = php.insertData(sql);
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    Boolean deleteDM( int dmId, int charId ){

        try{
            String sql = "DELETE from dungeonMasters WHERE dmID=" + dmId + " AND charID=" + charId;
            String result = php.insertData(sql);

        }
        catch (Exception e){
            System.out.println(e);
            System.out.println("Blaaaaaad");
            return false;
        }

        return true;
    }
}
