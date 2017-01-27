CREATE TABLE `order` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL,
  `num_items_per_box` int(11) DEFAULT NULL,
  `num_boxes` int(11) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `status` varchar(128) DEFAULT NULL,
  `date_timestamp` bigint(20) DEFAULT NULL,
  `date_str` varchar(128) DEFAULT NULL,
  `packager` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
);

