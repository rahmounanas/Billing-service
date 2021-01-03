package org.sid.billingservice;

import org.sid.billingservice.dao.BillRepository;
import org.sid.billingservice.dao.CustomerService;
import org.sid.billingservice.dao.InventoryService;
import org.sid.billingservice.dao.ProductItemRepository;
import org.sid.billingservice.entities.Bill;
import org.sid.billingservice.entities.Customer;
import org.sid.billingservice.entities.Product;
import org.sid.billingservice.entities.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BillRepository billRepository, ProductItemRepository productItemRepository, CustomerService customerService, InventoryService inventoryService){
        return args -> {
            Customer c1 =customerService.findCustomerById(1L);
            System.out.println("----------------------------");
            System.out.println("ID= "+c1.getId());
            System.out.println("Name= "+c1.getName());
            System.out.println("Email= "+c1.getEmail());
            System.out.println("----------------------------");
            Bill b1 = billRepository.save(new Bill(null,new Date(),c1.getId(),null,null));
            PagedModel<Product> products = inventoryService.findAllProducts();
            products.getContent().forEach(product -> {
                productItemRepository.save(new ProductItem(null, product.getId(),null, product.getPrice(), 30,b1));
            });
        };
    }
}
