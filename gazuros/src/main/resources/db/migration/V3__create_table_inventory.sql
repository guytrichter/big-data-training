CREATE TABLE inventory (
  inventory_id bigint(20) NOT NULL AUTO_INCREMENT,
  product_id bigint(20) DEFAULT NULL,
  count int(11) DEFAULT NULL,
  last_update bigint(20) DEFAULT NULL,
  packager varchar(128) DEFAULT NULL,
  PRIMARY KEY (inventory_id)
);