public class TaxableItem extends Item {
    private double taxRate = 7;
    
    // constructor

    public TaxableItem(String name, double price, int quantity, DiscountType discountType, double discountAmount){
        super(name, price, quantity, discountType, discountAmount);
    }

    // methods

    @Override
    public double calculatePrice() {
        double final_price = super.calculatePrice();
        double tax = this.taxRate / 100.0 * this.price;
        return final_price + tax;
    }

    // getter and setter

    public void setTaxRate(double rate) {
        if(rate>=0){
            taxRate = rate;
        }
    }
}
