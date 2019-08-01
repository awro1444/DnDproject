import java.util.ArrayList;

public class User {
    private String haslo; //20 znakow
    private String nazwa; //20 znakow
    private ArrayList<Character> character_list;
    private int id;

    SqlConnectionPHP sql = new SqlConnectionPHP();

    public boolean register( String username, String password){

        if( sql.checkUsername(username).equals("000") ){
            sql.addUser( username, password );
            return true;
        }

        return false;
    }

    public boolean logIn(String username, String password) {
        String passw = sql.checkUsername(username);
        if(passw.equals("000")){

            return false;
        } else if (password.equals(passw)) {
                System.out.println("ok");
                return true;


        }
    return false;
    }
}

