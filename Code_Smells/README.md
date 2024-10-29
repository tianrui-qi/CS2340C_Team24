## To use the code

```bash
javac -d bin/ src/*.java
java -cp bin main
```

## Code Smells

### Long method in `Order/calculateTotalPrice()` - 

The method `calculateTotalPrice` in `Order.java` has a complex logic to determine the price of each kind of item, i.e., price after percentage discount, amount discount, and tax. 
We can move price calculation logic into a seperate method called `calculatePrice` and call it repeatedly for each item in `calculateTotalPrice` method.

To do so, for original code

```java
public double calculateTotalPrice() {
    double total = 0.0;
    for (Item item : items) {
        // logic to calculate price of a single item
    }
    // ... logic applying on total price
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
    // ... logic applying on total price
    return total;
}
```

where `calculatePrice` is a private method with a single function that calculates the price of an item.
