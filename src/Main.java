import entities.Customer;
import entities.Order;
import entities.Product;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        System.out.println("-------- CREAZIONE DATI DI ESEMPIO --------");

        List<Product> products = Arrays.asList(
                new Product(1L, "Ichiraku Ramen Recipe Book", "Books", 120.0),
                new Product(2L, "Pirate King Adventure Guide", "Books", 85.0),
                new Product(3L, "Three Sword Style Manual", "Books", 150.0),
                new Product(4L, "Baby Stroller", "Baby", 250.0),
                new Product(5L, "Sake Brewing Kit", "Baby", 180.0),
                new Product(6L, "Rubber Toy Pistol", "Boys", 45.0),
                new Product(7L, "Ninja Action Figure", "Boys", 35.0),
                new Product(8L, "Pirate Ship LEGO Set", "Boys", 80.0),
                new Product(9L, "Premium Meat Smoker", "Electronics", 500.0),
                new Product(10L, "Cotton Candy Machine", "Baby", 25.0)
        );

        List<Customer> customers = Arrays.asList(
                new Customer(1L, "Naruto Uzumaki", 1),
                new Customer(2L, "Monkey D. Luffy", 2),
                new Customer(3L, "Roronoa Zoro", 2),
                new Customer(4L, "Sasuke Uchiha", 1),
                new Customer(5L, "Portgas D. Ace", 3)
        );

        List<Order> orders = Arrays.asList(
                new Order(1L, "DELIVERED", LocalDate.of(2021, 2, 15), LocalDate.of(2021, 2, 20),
                        Arrays.asList(products.get(0), products.get(1)), customers.get(1)), // Anna (tier 2)

                new Order(2L, "DELIVERED", LocalDate.of(2021, 3, 10), LocalDate.of(2021, 3, 15),
                        Arrays.asList(products.get(3), products.get(4)), customers.get(2)), // Luigi (tier 2)

                new Order(3L, "SHIPPED", LocalDate.of(2021, 1, 20), LocalDate.of(2021, 1, 25),
                        Arrays.asList(products.get(5), products.get(6)), customers.get(1)), // Anna (tier 2)

                new Order(4L, "DELIVERED", LocalDate.of(2021, 3, 25), LocalDate.of(2021, 3, 30),
                        Arrays.asList(products.get(9)), customers.get(0)), // Mario (tier 1)

                new Order(5L, "PROCESSING", LocalDate.of(2021, 4, 5), null,
                        Arrays.asList(products.get(2)), customers.get(4)) // Francesco (tier 3)
        );

        System.out.println("Prodotti creati: " + products.size());
        System.out.println("Clienti creati: " + customers.size());
        System.out.println("Ordini creati: " + orders.size());
        System.out.println();


        System.out.println("-------- ESERCIZIO 1 --------");
        System.out.println("Prodotti categoria 'Books' con prezzo > 100:");

        List<Product> expensiveBooks = products.stream()
                .filter(product -> "Books".equals(product.getCategory()))
                .filter(product -> product.getPrice() > 100)
                .collect(Collectors.toList());

        expensiveBooks.forEach(System.out::println);
        System.out.println("Totale prodotti trovati: " + expensiveBooks.size());
        System.out.println();


        System.out.println("-------- ESERCIZIO 2 --------");
        System.out.println("Ordini con prodotti categoria 'Baby':");

        List<Order> ordersWithBabyProducts = orders.stream()
                .filter(order -> order.getProducts().stream()
                        .anyMatch(product -> "Baby".equals(product.getCategory())))
                .collect(Collectors.toList());

        ordersWithBabyProducts.forEach(order -> {
            System.out.println("Order ID: " + order.getId() +
                    ", Customer: " + order.getCustomer().getName() +
                    ", Status: " + order.getStatus());
            System.out.println("  Prodotti Baby nell'ordine:");
            order.getProducts().stream()
                    .filter(product -> "Baby".equals(product.getCategory()))
                    .forEach(product -> System.out.println("    - " + product.getName() + " (€" + product.getPrice() + ")"));
            System.out.println();
        });
        System.out.println("Totale ordini con prodotti Baby: " + ordersWithBabyProducts.size());
        System.out.println();

        System.out.println("-------- ESERCIZIO 3 --------");
        System.out.println("Prodotti categoria 'Boys' con sconto 10% applicato:");

        List<Product> discountedBoysProducts = products.stream()
                .filter(product -> "Boys".equals(product.getCategory()))
                .map(product -> {
                    Product discountedProduct = new Product();
                    discountedProduct.setId(product.getId());
                    discountedProduct.setName(product.getName());
                    discountedProduct.setCategory(product.getCategory());
                    discountedProduct.setPrice(product.getPrice() * 0.9); // Applica 10% di sconto
                    return discountedProduct;
                })
                .collect(Collectors.toList());

        discountedBoysProducts.forEach(product ->
                System.out.println(product.getName() +
                        " - Prezzo originale: €" + products.stream()
                        .filter(p -> p.getId().equals(product.getId()))
                        .findFirst().get().getPrice() +
                        " - Prezzo scontato: €" + String.format("%.2f", product.getPrice())));
        System.out.println("Totale prodotti Boys scontati: " + discountedBoysProducts.size());
        System.out.println();

        System.out.println("-------- ESERCIZIO 4 --------");
        System.out.println("Prodotti ordinati da clienti tier 2 tra 01-Feb-2021 e 01-Apr-2021:");

        LocalDate startDate = LocalDate.of(2021, 2, 1);
        LocalDate endDate = LocalDate.of(2021, 4, 1);

        List<Product> tier2Products = orders.stream()
                .filter(order -> order.getCustomer().getTier() == 2) // Clienti tier 2
                .filter(order -> order.getOrderDate().isAfter(startDate.minusDays(1)) &&
                        order.getOrderDate().isBefore(endDate.plusDays(1))) // Tra le date specificate
                .flatMap(order -> order.getProducts().stream()) // Ottieni tutti i prodotti dagli ordini
                .distinct() // Rimuovi duplicati
                .collect(Collectors.toList());

        tier2Products.forEach(product -> System.out.println(product));

        System.out.println("Dettaglio ordini che soddisfano i criteri:");
        orders.stream()
                .filter(order -> order.getCustomer().getTier() == 2)
                .filter(order -> order.getOrderDate().isAfter(startDate.minusDays(1)) &&
                        order.getOrderDate().isBefore(endDate.plusDays(1)))
                .forEach(order -> {
                    System.out.println("Order ID: " + order.getId() +
                            ", Customer: " + order.getCustomer().getName() +
                            " (Tier " + order.getCustomer().getTier() + ")" +
                            ", Data: " + order.getOrderDate());
                    order.getProducts().forEach(product ->
                            System.out.println("  - " + product.getName() + " (€" + product.getPrice() + ")"));
                });

        System.out.println("Totale prodotti unici trovati: " + tier2Products.size());

    }
}