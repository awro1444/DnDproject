import com.google.gson.reflect.TypeToken;
import javafx.geometry.HorizontalDirection;
import com.google.gson.Gson;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class CharSheetGUIv2 {
    private JButton backButton;
    private JPanel panel1;
    private JPanel savingThrowsPanel;
    private JPanel skillsPanel;
    private JPanel attributesPanel;
    private JButton syncButton;
    private JPanel generalDataPanel;
    private JPanel eqAndFeatPanel;
    private JTextField nameField;
    private JTextField playerName;
    private JTextArea weaponsArea;
    private JTextArea equipmentArea;
    private JTextArea featsArea;
    private JComboBox classBox;
    private JComboBox raceBox;
    private JSpinner lvlSpinner;
    private JTextField profField;
    private JButton generateCodeButton;

    ArrayList<ComboBoxHelper> raceList = new ArrayList<>();
    ArrayList<ComboBoxHelper> classList = new ArrayList<>();

    SqlConnectionPHP conn = new SqlConnectionPHP();

    private String attributes[] = {"Strength","Dexterity","Constitution","Intelligence","Wisdom","Charisma"};
    private String skills[] = {"Acrobatics","Animal Handling","Arcana","Athletics","Deception","History","Insight","Intimidation","Investigation","Medicine","Nature","Perception","Performance","Persuasion","Religion","Sleight of Hand","Stealth","Survival"};
    private int skillsAttributes[] = {1,4,3,0,5,3,4,5,3,4,3,4,5,5,3,1,1,4};
    private ArrayList<JTextField> attributesValues = new ArrayList<>();
    private ArrayList<JTextField> attributesMods = new ArrayList<>();
    private ArrayList<JCheckBox> savingThrowsProfs = new ArrayList<>();
    private ArrayList<JTextField> savingThrowsMods = new ArrayList<>();
    private ArrayList<JCheckBox> skillsProfs = new ArrayList<>();
    private ArrayList<JTextField> skillsMods = new ArrayList<>();

    Gson gson = new Gson();

    JFrame frameX;

    public JPanel getPanel1() {
        return panel1;
    }

    public CharSheetGUIv2( JFrame f ) {

        frameX = f;
        lvlSpinner.setModel(new SpinnerNumberModel(1,1,20,1));
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

        refreshComboboxes();
        createAttributes();
        createSavingThrows();
        createSkills();

        playerName.setText( characterData.split("_")[5] );
        nameField.setText(characterData.split("_")[1]);
        for( int i = 0; i < raceList.size(); i++ ){
            int tmp = Integer.parseInt(characterData.split("_")[2]);
            if( raceList.get(i).id == tmp )
                raceBox.setSelectedIndex(i);
        }
        for( int i = 0; i < classList.size(); i++ ){
            int tmp = Integer.parseInt(characterData.split("_")[3]);
            if( classList.get(i).id == tmp )
                classBox.setSelectedIndex(i);
        }
        lvlSpinner.setValue(Integer.parseInt(characterData.split("_")[4]));

        if( characterData.split("_")[0].equals("[]") ){
            for( int i = 0; i < attributes.length; i++ ){
                attributesValues.get(i).setText("10");
            }
        }
        else{
            fillDownloadedData( characterData );
        }

        calculateProf();
        calculateModifiers();
        calculateSkills();

        lvlSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                calculateProf();
                calculateModifiers();
                calculateSkills();
            }
        });

        syncButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int[] att = {0,0,0,0,0,0};
                boolean[] stProf = {false,false,false,false,false,false};
                boolean[] skProf = {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,};

                for( int i = 0; i < attributes.length; i++ ){
                    att[i] = Integer.parseInt(attributesValues.get(i).getText());
                    if( savingThrowsProfs.get(i).isSelected() ){
                        stProf[i] = true;
                    }
                }

                for( int i = 0; i < skills.length; i++ ){
                    if( skillsProfs.get(i).isSelected() ){
                        skProf[i] = true;
                    }
                }

                SheetDataHelper dataHelper = new SheetDataHelper();
                dataHelper.setAttributes(att);
                dataHelper.setSavingThrowsProf(stProf);
                dataHelper.setSkillsProf(skProf);
                dataHelper.setWeapons(weaponsArea.getText().replaceAll("\n", "//n"));
                dataHelper.setEquipment(equipmentArea.getText().replaceAll("\n", "//n"));
                dataHelper.setFeats(featsArea.getText().replaceAll("\n", "//n"));

                String json = gson.toJson(dataHelper);
                System.out.println(json);

                conn.updateCharacterData( currentChar + "", nameField.getText(),
                        raceList.get(raceBox.getSelectedIndex()).id + "", classList.get(classBox.getSelectedIndex()).id + "",
                        lvlSpinner.getValue().toString(),  json );
            }
        });
        lvlSpinner.addComponentListener(new ComponentAdapter() {
        });
        generateCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = conn.generateCode(DataStorage.getInstance().getCurrentChar());
                JOptionPane.showMessageDialog(null, code);
            }
        });
    }

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

    private void createAttributes(){
        attributesPanel.setLayout(new BoxLayout(attributesPanel, BoxLayout.Y_AXIS));
        for( int i = 0; i < attributes.length; i++ ){
            JPanel panel = new JPanel();
            panel.setMaximumSize(new Dimension(75, 110));
            panel.setPreferredSize(new Dimension(75, 110));
            JTextField attTField = new JTextField();
            attTField.setName("att_" + i);
            attTField.setMaximumSize(new Dimension(50, 50));
            attTField.setPreferredSize(new Dimension(50, 50));
            JTextField attMTField = new JTextField();
            attMTField.setName("attm_" + i);
            attMTField.setEnabled(false);
            attMTField.setMaximumSize(new Dimension(25, 25));
            attMTField.setPreferredSize(new Dimension(25, 25));
            JLabel label = new JLabel();
            label.setText( attributes[i] );
            attributesValues.add(attTField);
            attTField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    calculateModifiers();
                    calculateSkills();
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    calculateModifiers();
                    calculateSkills();
                }
            });
            attributesMods.add(attMTField);
            panel.add(label);
            panel.add(attTField);
            panel.add(attMTField);
            panel.revalidate();
            attributesPanel.add(panel);
        }
        attributesPanel.revalidate();
    }

    private void calculateModifiers(){
        for( int i = 0; i < attributes.length; i++ ){
            if( !attributesValues.get(i).getText().equals("") && Integer.parseInt(attributesValues.get(i).getText()) >= 0 ){
                int mod = (Integer.parseInt(attributesValues.get(i).getText()) - 10)/2;
                attributesMods.get(i).setText(mod+"");
                int bonus = mod;
                if( !profField.getText().equals("") && savingThrowsProfs.get(i).isSelected() ){
                    bonus += Integer.parseInt(profField.getText());
                }
                savingThrowsMods.get(i).setText(bonus+"");
            }
        }
    }

    private void calculateProf(){
        int profBonus = (7+Integer.parseInt(lvlSpinner.getValue().toString()))/4;
        profField.setText("+"+profBonus);
    }

    private void calculateSkills(){
        for( int i = 0; i < skills.length; i++ ){
            if( !attributesMods.get(skillsAttributes[i]).getText().equals("") ) {
                int bonus = Integer.parseInt(attributesMods.get(skillsAttributes[i]).getText());
                if( !profField.getText().equals("") && skillsProfs.get(i).isSelected() ){
                    bonus += Integer.parseInt(profField.getText());
                }
                skillsMods.get(i).setText(bonus+"");
            }
        }
    }

    private void createSavingThrows(){
        //System.out.println("");
        savingThrowsPanel.setLayout(new BoxLayout(savingThrowsPanel, BoxLayout.Y_AXIS));
        for( int i = 0; i < attributes.length; i++ ){
            JPanel panel = new JPanel();
            //panel.setMaximumSize(new Dimension(savingThrowsPanel.getWidth(), savingThrowsPanel.getHeight()/6));
            //panel.setPreferredSize(new Dimension(savingThrowsPanel.getWidth(), savingThrowsPanel.getHeight()/6));
            JCheckBox checkbox = new JCheckBox();
            checkbox.setName("st_" + i);
            checkbox.setText( attributes[i] );
            checkbox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    calculateModifiers();
                }
            });
            JTextField bonus = new JTextField();
            bonus.setName("stbonus_" + i);
            bonus.setMaximumSize(new Dimension(25, 25));
            bonus.setPreferredSize(new Dimension(25, 25));
            savingThrowsProfs.add(checkbox);
            savingThrowsMods.add(bonus);
            panel.add(bonus);
            panel.add(checkbox);
            panel.revalidate();
            //bonus.setAlignmentX(Component.LEFT_ALIGNMENT);
            bonus.setHorizontalAlignment(JTextField.LEFT);
            checkbox.setHorizontalAlignment(JCheckBox.LEFT);
            //checkbox.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
            savingThrowsPanel.add(panel);
        }
        savingThrowsPanel.revalidate();
    }

    private void createSkills(){
        skillsPanel.setLayout(new BoxLayout(skillsPanel, BoxLayout.Y_AXIS));
        for( int i = 0; i < skills.length; i++ ){
            JPanel panel = new JPanel();
            JCheckBox checkbox = new JCheckBox();
            checkbox.setName("sk_" + i);
            checkbox.setText( skills[i] );
            checkbox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    calculateSkills();
                }
            });
            skillsProfs.add(checkbox);
            JTextField bonus = new JTextField();
            bonus.setName("stbonus_" + i);
            bonus.setMaximumSize(new Dimension(25, 25));
            bonus.setPreferredSize(new Dimension(25, 25));
            skillsMods.add(bonus);
            panel.add(bonus);
            panel.add(checkbox);
            panel.revalidate();
            skillsPanel.add(panel);
        }
        skillsPanel.revalidate();
    }

    private void fillDownloadedData( String characterData ){
        String json = characterData.split("_")[0];
        SheetDataHelper dataHelper = new SheetDataHelper();
        Type type = new TypeToken<SheetDataHelper>(){}.getType();
        dataHelper = gson.fromJson(json.toString(), type);

        int[] att = dataHelper.getAttributes();
        boolean[] stProf = dataHelper.getSavingThrowsProf();
        boolean[] skProf = dataHelper.getSkillsProf();

        for( int i = 0; i < attributes.length; i++ ){
            attributesValues.get(i).setText( att[i] + "" );
            if( stProf[i] ){
                savingThrowsProfs.get(i).setSelected(true);
            }
        }

        for( int i = 0; i < skills.length; i++ ){
            if( skProf[i] ){
                skillsProfs.get(i).setSelected(true);
            }
        }

        weaponsArea.setText(dataHelper.getWeapons().replaceAll("//n", "\n"));
        equipmentArea.setText(dataHelper.getEquipment().replaceAll("//n", "\n"));
        featsArea.setText(dataHelper.getFeats().replaceAll("//n", "\n"));
    }


}
