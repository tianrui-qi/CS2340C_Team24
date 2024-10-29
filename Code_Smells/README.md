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

### Long method in `Order/sendConfirmationEmail()`

We can extract the logic to print message for each item into `Item` and create
a method `toString()` that returns info of the item. 

The original code in `Order/sendConfirmationEmail()` is

```java
public void sendConfirmationEmail() {
    String message = "Thank you for your order, " + customerName + "!\n\n" +
            "Your order details:\n";
    for (Item item : items) {
        message += item.getName() + " - " + item.getPrice() + "\n";
    }
    message += "Total: " + calculateTotalPrice();
    EmailSender.sendEmail(customerEmail, "Order Confirmation", message);
}
```

We extract the logic to print message for each item into `Item/toString()`

```java
@Override
public String toString() {
    return this.getName() + " - " + this.getPrice();
}
```

Then, in `Order/sendConfirmationEmail()`, we can simply call `toString()` method of each item

```java
public void sendConfirmationEmail() {
    String message = "Thank you for your order, " + customerName + "!\n\n" +
            "Your order details:\n";
    for (Item item : items) {
        message += item.toString() + "\n";
    }
    message += "Total: " + calculateTotalPrice();
    EmailSender.sendEmail(customerEmail, "Order Confirmation", message);
}
```

### Duplicate code in printing order

Since all information has printed in `Order/sendConfirmationEmail()`, we can 
remove the duplicate method `Order/printOrder()` and following duplicate code 
in main:

```java
System.out.println("Total Price: " + order.calculateTotalPrice());
order.printOrder();
```

### Unnecessary getter in `Item` and subclasses

Since we move logic to calculate price and print info of item into the item
class itself, we no longer need getter for attributes in `Item` and its subclasses.
We can remove these unnecessary getters and directly access these attributes insde the method of the class. 

### Unncessary `EmailSender` class

Since `EmailSender` only has a static method `sendEmail` and no other 
attributes or methods, it is better to this method `EmailSender/sendEmail()` to 
`Order` and remove this lazy class.

We move method `EmailSender/sendEmail()`

```java
public static void sendEmail(String customerEmail, String subject, String message){
    System.out.println("Email to: " + customerEmail);
    System.out.println("Subject: " + subject);
    System.out.println("Body: " + message);
}
```

to `Order/sendEmail()`

```java
private void sendEmail(String subject, String message){
    System.out.println("Email to: " + this.customerEmail);
    System.out.println("Subject: " + subject);
    System.out.println("Body: " + message);
}
```

and use this method when we want to send any email. For example, for user case send confirmation email,

```java
public void sendConfirmationEmail() {
    // logic to create message
    this.sendEmail("Order Confirmation", message);
}
```

## Contributors

All members of the group contributed to this code smell detection and reafactoring equally. 
