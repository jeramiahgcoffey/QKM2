package model;

/**
 * Models an outsourced Part.
 * @author Jeramiah Coffey
 */
public class Outsourced extends Part {
    /**
     * The company name for an outsourced Part.
     */
    private String companyName;

    /**
     * Constructor for an instance of an outsourced Part object.
     * @param id the ID for the Part
     * @param name the name of the Part
     * @param price the price of the Part
     * @param stock the stock level of the Part
     * @param min the minimum level of stock for the Part
     * @param max the maximum level of stock for the Part
     * @param companyName the company name for the outsourced Part
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @param companyName the company name to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the company name
     */
    public String getCompanyName() {
        return companyName;
    }
}
