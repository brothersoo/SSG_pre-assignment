insert into ecommerce_role(name, created_at, updated_at) VALUES
("ROLE_TEMPORARY_USER", NOW(), NOW()),
("ROLE_MEMBER", NOW(), NOW()),
("ROLE_ADMIN", NOW(), NOW());

insert into ecommerce_privilege(name, created_at, updated_at) VALUES
("USE_CART", NOW(), NOW()),
("ORDER", NOW(), NOW()),
("CHECK_ORDERS", NOW(), NOW()),
("ADD_PRODUCT_GROUP", NOW(), NOW()),
("ADD_PRODUCT", NOW(), NOW()),
("MANAGE_USER", NOW(), NOW());

insert into ecommerce_role_privilege(created_at, updated_at, ecommerce_role_id, ecommerce_privilege_id) values
(now(), now(), 2, 1),
(now(), now(), 2, 2),
(now(), now(), 2, 3);
