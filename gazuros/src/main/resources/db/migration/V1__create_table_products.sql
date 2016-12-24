CREATE TABLE product (
  product_id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(128) DEFAULT NULL,
  price decimal(10,2) DEFAULT NULL,
  provider varchar(128) DEFAULT NULL,
  description varchar(128) DEFAULT NULL,
  PRIMARY KEY (product_id)
);


