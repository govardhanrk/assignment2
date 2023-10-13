package assignment.vehicles;

/**
 * This class represents a vehicle record in the database. Each record consists of two
 * parts: a DataKey and the data associated with the DataKey.
 */
public class VehicleRecord {

    private DataKey key;
    private String about;
    private String sound;
    private String image;

    // default constructor
    public VehicleRecord() {
        this(null, null, null, null);
    }

    public VehicleRecord(DataKey k, String a, String s, String i) {
        key = k;
        about = a;
        sound = s;
        image = i;
    }

    public DataKey getDataKey() {
        return key;
    }

    public String getAbout() {
        return about;
    }
    
 
    public String getSound() {
        return sound;
    }
    
    public String getImage() {
        return image;
    }
    
 


}
