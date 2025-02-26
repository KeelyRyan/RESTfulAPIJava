CREATE TABLE IF NOT EXISTS `customer` (
    `customer_id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `mobile_number` VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS customer_order (
    `order_id` INT AUTO_INCREMENT PRIMARY KEY,
    `order_date` DATE NOT NULL,
    `amount` INT,
    `customer_id` INT NOT NULL,
    `product` VARCHAR(64),
    `price` DECIMAL(10,2),
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE
);