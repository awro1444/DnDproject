import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CharSheetGUI {
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField1;
    private JTextField att_0;
    private JTextField att_5;
    private JTextField att_1;
    private JTextField att_2;
    private JTextField att_3;
    private JTextField att_4;
    private JTextField attm_0;
    private JTextField attm_1;
    private JTextField attm_2;
    private JTextField attm_3;
    private JTextField attm_4;
    private JTextField attm_5;
    private JCheckBox st_0;
    private JCheckBox st_1;
    private JCheckBox st_2;
    private JCheckBox st_3;
    private JCheckBox st_4;
    private JCheckBox st_5;
    private JCheckBox s_0;
    private JCheckBox checkBox8;
    private JCheckBox checkBox9;
    private JCheckBox checkBox10;
    private JCheckBox checkBox11;
    private JCheckBox checkBox12;
    private JCheckBox checkBox13;
    private JCheckBox checkBox14;
    private JCheckBox checkBox15;
    private JCheckBox checkBox16;
    private JCheckBox checkBox17;
    private JCheckBox checkBox18;
    private JCheckBox checkBox19;
    private JCheckBox checkBox20;
    private JCheckBox checkBox21;
    private JCheckBox checkBox22;
    private JCheckBox checkBox23;
    private JCheckBox checkBox24;
    private JPanel panel1;
    private JButton backButton;

    JFrame frameX;

    public JPanel getPanel1() {
        return panel1;
    }

    public CharSheetGUI(JFrame f) {

        frameX = f;
        SqlConnection conn = new SqlConnection();
        int currentChar = DataStorage.getInstance().getCurrentChar();
        String characterData = conn.getCurrentChar( currentChar );
        //System.out.println(characterData);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                CharacterSelectGUI select = new CharacterSelectGUI(frameX);
                frameX.setContentPane(select.getPanel1());
                frameX.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameX.pack();
                frameX.setVisible(true);

            }
        });
    }
}
