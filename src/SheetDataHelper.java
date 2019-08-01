import java.util.ArrayList;

public class SheetDataHelper {

    //private String name;
    //private String race;
    //private String profession;
    //private int level;

    //private int armor;
    //private int speed;
    //private int health;
    //private int maxHealth;
    //private int initiative;

    private int[] attributes;
    private boolean[] savingThrowsProf;
    private boolean[] skillsProf;
    private String weapons;
    private String equipment;
    private String feats;

    public String getWeapons() {
        return weapons;
    }

    public void setWeapons(String weapons) {
        this.weapons = weapons;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getFeats() {
        return feats;
    }

    public void setFeats(String feats) {
        this.feats = feats;
    }

    public int[] getAttributes() {
        return attributes;
    }

    public void setAttributes(int[] attributes) {
        this.attributes = attributes;
    }

    public boolean[] getSavingThrowsProf() {
        return savingThrowsProf;
    }

    public void setSavingThrowsProf(boolean[] savingThrowsProf) {
        this.savingThrowsProf = savingThrowsProf;
    }

    public boolean[] getSkillsProf() {
        return skillsProf;
    }

    public void setSkillsProf(boolean[] skillsProf) {
        this.skillsProf = skillsProf;
    }
}
