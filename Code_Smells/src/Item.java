class Item {
    protected String name;
    protected double price;
    protected int quantity;
    protected DiscountType discountType;
    protected double discountAmount;

    // constructor

    public Item(String name, double price, int quantity, DiscountType discountType, double discountAmount) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
    }

    // methods

    public double calculatePrice() {
        double final_price = this.price;
        switch (this.discountType) {
            case PERCENTAGE:
                final_price -= this.discountAmount * this.price;
                break;
            case AMOUNT:
                final_price -= this.discountAmount;
                break;
            default:
                break;
        }
        return final_price * this.quantity;
    }

    @Override
    public String toString() {
        return this.name + " - " + this.price;
    }
}
