import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SqlPhp {

    SqlCredentials credentials = new SqlCredentials();

    public String insertData( String sqlQuery ){

        try {
            // open a connection to the site
            URL url = new URL(credentials.getUrl());
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site
            ps.print("sqlQuery=" + sqlQuery );

            //get response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String data = "";
            String line = "";
            while ((line = in.readLine()) != null) {
                data += line;
            }

            // close the print stream
            ps.close();
            System.out.println(data);
            return data;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "I_AM_ERROR";
    }

}
