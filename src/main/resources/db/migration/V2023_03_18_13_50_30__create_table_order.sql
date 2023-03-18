CREATE TABLE `orders`
(
    `id`           VARCHAR(255) NOT NULL,
    `address`      varchar(50)  NULL,
    `contact_name`  varchar(50)  NULL,
    `phone_number`  varchar(50)  NULL,
    `remark`       varchar(50)  NULL,
    `total_price`   decimal NULL,
    `created_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);