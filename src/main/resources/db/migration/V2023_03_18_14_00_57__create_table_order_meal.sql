CREATE TABLE `order_meal`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT,
    `order_id`     varchar(255) NOT NULL,
    `meal_id`       varchar(50) NOT NULL,
    `quantity`     int,
    `created_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);