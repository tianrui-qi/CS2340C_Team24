## To use the code

```bash
javac -d bin/ src/*.java
java -cp bin main
```

## Code Smells

### Long method in `Order/calculateTotalPrice()`

The method `calculateTotalPrice` in `Order.java` has a complex logic to determine the price of each kind of item, i.e., price after percentage discount, amount discount, and tax. 
We can move price calculation logic into a seperate method called `calculatePrice` and call it repeatedly for each item in `calculateTotalPrice` method.

To do so, for original code

```java
public double calculateTotalPrice() {
    double total = 0.0;
    for (Item item : items) {
        // logic to calculate price of a single item
    }
    // logic applying on total price
    return total;
}
```

we extract all logic inside the loop into a new method `calculatePrice`

```java
private double calculatePrice(Item item) {
    double price = item.getPrice();
    // logic to calculate price of a single item
    return price;
}

public double calculateTotalPrice() {
    double total = 0.0;
    for (Item item : items) {
        total += calculatePrice(item);
    }
    // logic applying on total price
    return total;
}
```

where `calculatePrice` is a private method with a single function that calculates the price of an item.


### Object oriented violation in `Order/calculatePrice()`

Currently, in `Order/calculatePrice()`, we are using complex logic to determine the price of each item. This is a violation of object-oriented principles as we are not using polymorphism to determine the price of each item.
We can move this calculate price logic into the item class itself and its subclasses.

For original code in `Order.java`

```java
private double calculatePrice(Item item) {
    double price = item.getPrice();
    // logic to calculate price after discount
    // logic to calculate price after quantity
    // logic to calculate price after tax
    return price;
}
```

we move logic to `Item.java`

```java
public double calculatePrice() {
    double final_price = this.getPrice();
    // logic to calculate price after discount
    // logic to calculate price after quantity
    return final_price;
}
```

For special items that need tax, we override this method and include the logic to calculate tax, i.e., in `TaxableItem/calculatePrice()`

```java
@Override
public double calculatePrice() {
    double final_price = super.calculatePrice();
    // logic to calculate price after tax
    return final_price;
}
```

Then, in `Order/calculateTotalPrice()`, we can simply call `calculatePrice()` method of each item.

```java
public double calculateTotalPrice() {
    double total = 0.0;
    for (Item item : items) {
        total += item.calculatePrice();
    }
    // logic applying on total price
    return total;
}
```

### Unnecessary getter in `Item` and subclasses

Since we move logic to calculate price into the item class itself, we no longer
need getter for quantity, discount type, and discount amount in `Item` and tax rate in `TaxableItem`.
We can remove these unnecessary getters and directly access these attributes insde the method of the class. 

## Contributors

All members of the group contributed to this code smell detection and reafactoring equally. 
