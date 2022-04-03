package model;
/**
 * Models an in-house Part.
 * @author Jeramiah Coffey
 */
public class InHouse extends Part {
    /**
     * The machine ID for an in-house Part.
     */
    private int machineId;

    /**
     * Constructor for an instance of an in-house Part object.
     * @param id the ID for the Part
     * @param name the name of the Part
     * @param price the price of the Part
     * @param stock the stock level of the Part
     * @param min the minimum level of stock for the Part
     * @param max the maximum level of stock for the Part
     * @param machineId the in-house ID for the machine which manufactures the Part
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @param machineId the ID to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * @return the machine ID
     */
    public int getMachineId() {
        return machineId;
    }
}
