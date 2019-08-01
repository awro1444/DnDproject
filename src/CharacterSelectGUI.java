import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CharacterSelectGUI {
    private JPanel panel1;
    private JButton createCharacterButton;
    private JPanel characterPanel;
    private JTextField nameField;
    private JComboBox raceBox;
    private JComboBox classBox;
    private JSpinner lvlSpinner;
    private JButton logoutButton;
    private JTextField dmCodeText;
    private JButton confirmButton;

    public JPanel getPanel1() {
        return panel1;
    }

    JFrame frameX;

    public CharacterSelectGUI(JFrame f) {

        String userID = DataStorage.getInstance().getUserId() + "";
        frameX = f;
        refresh();
        refreshComboboxes();
        lvlSpinner.setModel(new SpinnerNumberModel(1,1,20,1));
        createCharacterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int race = raceList.get(raceBox.getSelectedIndex()).id;
                int klasa = classList.get(classBox.getSelectedIndex()).id;
                if( conn.addData(nameField.getText(),race + "",klasa + "",lvlSpinner.getValue().toString(), userID) ){
                    System.out.println("Działa");
                    refresh();
                }
                else{
                    System.out.println("Error");
                }
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DataStorage.getInstance().setUserId(0);
                LogSreenGUI logScreen = new LogSreenGUI(frameX);
                frameX.setContentPane(logScreen.getPanel1());
                frameX.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameX.pack();
                frameX.setVisible(true);
            }
        });
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ourCode = dmCodeText.getText();
                if ( ourCode.length() == 8) {
                    int newCharId = Integer.parseInt(ourCode.substring(3, 3 + Integer.parseInt(ourCode.charAt(1)+"")));
                    if(conn.checkDm( Integer.parseInt(userID), newCharId )){
                        conn.addDm( Integer.parseInt(userID), newCharId );
                        System.out.println("New character connected.");
                        refresh();
                    }
                    else{
                        System.out.println("Character already connected.");
                    }
                }else{
                    System.out.println("Invalid Code");
                }
            }
        });
    }

    DefaultListModel characterList = new DefaultListModel();
    User user = new User();
    SqlConnectionPHP conn = new SqlConnectionPHP();

    ArrayList<ComboBoxHelper> raceList = new ArrayList<>();
    ArrayList<ComboBoxHelper> classList = new ArrayList<>();

    /*public void refresh() {
        characterList.addElement(conn.getCharacters());
    }*/

    public void refreshComboboxes(){
        raceList = conn.getRaceList();
        classList = conn.getClassList();

        String[] raceArray = new String[raceList.size()];
        for (int i= 0; i < raceList.size(); i++ ) {
            raceArray[i] = raceList.get(i).text_value;

        }

        raceBox.setModel(new DefaultComboBoxModel(raceArray));

        String[] classArray = new String[classList.size()];
        for (int i= 0; i < classList.size(); i++ ) {
            classArray[i] = classList.get(i).text_value;

        }

        classBox.setModel(new DefaultComboBoxModel(classArray));

    }

    public void refresh() {

        characterPanel.removeAll();
        characterPanel.revalidate();
        characterPanel.repaint();
        characterPanel.setLayout(new BoxLayout(characterPanel, BoxLayout.Y_AXIS));

        try {
            String data = conn.getCharacters( DataStorage.getInstance().getUserId() );
            String[] items = data.split(";");

        for (int i = 0; i<items.length; i++) {
            JPanel panel = new JPanel();
            //panel.setMaximumSize(new Dimension(500, 40));
            //panel.setPreferredSize(new Dimension(500, 40));
            panel.setSize(200, 40);
            panel.setBounds(0, 0, 200, 40);
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel label = new JLabel();
            label.setText(items[i].split("_")[1]);
            JButton deleteButton = new JButton();
            JButton openButton = new JButton();
            deleteButton.setText("Delete");
            openButton.setText("Open");
            openButton.setName("openBtn_" + items[i].split("_")[0]);
            deleteButton.setName("delBtn_" + items[i].split("_")[0]);
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (conn.deleteData(deleteButton.getName().split("_")[1])) {
                        refresh();
                    } else {
                        System.out.println("ERROR");
                    }
                }
            });
            openButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DataStorage.getInstance().setCurrentChar(Integer.parseInt(openButton.getName().split("_")[1]));
                    CharSheetGUIv2 sheet = new CharSheetGUIv2(frameX);
                    frameX.setContentPane(sheet.getPanel1());
                    frameX.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameX.pack();
                    frameX.setVisible(true);
                }
            });
            panel.add(label);
            panel.add(openButton);

            panel.add(deleteButton);
            panel.revalidate();
            characterPanel.add(panel);
            characterPanel.revalidate();
        }

        } catch (Exception e) {
            System.out.println("nie ma postaci");
        }

        refreshConnectedChars();
    }

    public void refreshConnectedChars() {

        try {
            String data = conn.getConnectedCharacters( DataStorage.getInstance().getUserId() );
            System.out.println(data);
            String[] items = data.split(";");

            for (int i = 0; i<items.length; i++) {
                JPanel panel = new JPanel();
                //panel.setMaximumSize(new Dimension(500, 40));
                //panel.setPreferredSize(new Dimension(500, 40));
                panel.setSize(200, 40);
                panel.setBounds(0, 0, 200, 40);
                panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                JLabel label = new JLabel();
                label.setText(items[i].split("_")[1] + " (" + items[i].split("_")[5] + ")");
                JButton deleteButton = new JButton();
                JButton openButton = new JButton();
                deleteButton.setText("Disconnect");
                openButton.setText("Open");
                openButton.setName("openBtn_" + items[i].split("_")[0]);
                deleteButton.setName("delBtn_" + items[i].split("_")[0]);
                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (conn.deleteDM( DataStorage.getInstance().getUserId(), Integer.parseInt(deleteButton.getName().split("_")[1])) ) {
                            refresh();
                        } else {
                            System.out.println("ERROR");
                        }
                    }
                });
                openButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        DataStorage.getInstance().setCurrentChar(Integer.parseInt(openButton.getName().split("_")[1]));
                        CharSheetGUIv2 sheet = new CharSheetGUIv2(frameX);
                        frameX.setContentPane(sheet.getPanel1());
                        frameX.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frameX.pack();
                        frameX.setVisible(true);
                    }
                });
                panel.add(label);
                panel.add(openButton);

                panel.add(deleteButton);
                panel.revalidate();
                characterPanel.add(panel);
                characterPanel.revalidate();
            }

        } catch (Exception e) {
            System.out.println("nie ma dodatkowych postaci");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ProjectDnD");
        frame.setContentPane(new CharacterSelectGUI(frame).panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}
