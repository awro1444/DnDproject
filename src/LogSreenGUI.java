import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogSreenGUI {
    private JPanel panel1;
    private JButton login_button;
    private JPasswordField password_field;
    private JTextField username_field;
    private JLabel Login;
    private JLabel Password;
    private JButton register_button;

    User user = new User();
    JFrame frameX;
    SqlConnectionPHP conn = new SqlConnectionPHP();

    public JPanel getPanel1() {
        return panel1;
    }

    public LogSreenGUI(JFrame f) {
        frameX = f;
        login_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(user.logIn(username_field.getText(), String.valueOf(password_field.getPassword()))) {

                    DataStorage.getInstance().setUserId(Integer.parseInt(conn.getUserID(username_field.getText())));
                    CharacterSelectGUI sheet = new CharacterSelectGUI(frameX);
                    frameX.setContentPane(sheet.getPanel1());
                    frameX.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameX.pack();
                    frameX.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog( null, "Invalid login or password" );
                }
            }
        });
        register_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean success = false;
                if( !username_field.getText().equals("") && !String.valueOf(password_field.getPassword()).equals("") )
                    success = user.register( username_field.getText(), String.valueOf(password_field.getPassword()) );

                if(success == false){
                    JOptionPane.showMessageDialog(null, "istnieje juz taki username");
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ProjectDnD");
        frame.setContentPane(new LogSreenGUI(frame).panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
