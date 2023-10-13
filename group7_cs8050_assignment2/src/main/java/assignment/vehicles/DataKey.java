package assignment.vehicles;

public class DataKey {
	private String vehicleName;
	private int vehicleSize;

	// default constructor
	public DataKey() {
		this(null, 0);
	}
        
	public DataKey(String name, int size) {
		vehicleName = name;
		vehicleSize = size;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public int getVehicleSize() {
		return vehicleSize;
	}

	/**
	 * Returns 0 if this DataKey is equal to k, returns -1 if this DataKey is smaller
	 * than k, and it returns 1 otherwise. 
	 */
	public int compareTo(DataKey k) {
            if (this.getVehicleSize() == k.getVehicleSize()) {
                int compare = this.vehicleName.compareTo(k.getVehicleName());
                if (compare == 0){
                     return 0;
                } 
                else if (compare < 0) {
                    return -1;
                }
            }
            else if(this.getVehicleSize() < k.getVehicleSize()){
                    return -1;
            }
            return 1;
            
	}
}
