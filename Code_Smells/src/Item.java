class Item {
    private String name;
    private double price;
    private int quantity;
    private DiscountType discountType;
    private double discountAmount;

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
        double final_price = this.getPrice();
        switch (this.getDiscountType()) {
            case PERCENTAGE:
                final_price -= this.getDiscountAmount() * this.getPrice();
                break;
            case AMOUNT:
                final_price -= this.getDiscountAmount();
                break;
            default:
                break;
        }
        return final_price * this.getQuantity();
    }

    // getter and setter

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }
}
