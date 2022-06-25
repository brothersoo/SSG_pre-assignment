insert into ecommerce_product_group(name) values("특별배송"), ("새벽배송"), ("택배");

insert into ecommerce_product(name, price, stock, created_at, updated_at, ecommerce_product_group_id) values
("달콤 초콜릿", 600, 10, NOW(), NOW(), 1),
("바삭 곡물 간식", 2800, 5, NOW(), NOW(), 1),
("유정란 15입", 4350, 0, NOW(), NOW(), 1),
("깔끔 손 세정제", 2600, 6, NOW(), NOW(), 1),
("달달 꿀호떡", 1200, 4, NOW(), NOW(), 1),
("탱탱 손만두", 6400, 2, NOW(), NOW(), 2),
("맛난 아이스크림 4입", 2800, 1, NOW(), NOW(), 2),
("신선 우유 900ml", 2500, 3, NOW(), NOW(), 2),
("유기농 방울토마토 500g", 4500, 0, NOW(), NOW(), 2),
("꿀꿀바 (70ml*6 입)", 3000, 1, NOW(), NOW(), 3),
("아이와 피크닉 툴", 65000, 4, NOW(), NOW(), 3),
("에티오피아 원두 500g", 18500, 4, NOW(), NOW(), 3),
("세련된 머그잔", 23000, 7, NOW(), NOW(), 3),
("고급 UHD 스마트모니터 47", 480000, 2, NOW(), NOW(), 3),
("햇빛 반사 썬캡", 25000, 5, NOW(), NOW(), 3);
