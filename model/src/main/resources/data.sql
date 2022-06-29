INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('ATV riding', 'Description ATV riding', 100, 10, '2022-04-01T10:12:45.123', '2022-04-07T14:15:13.257') ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('Horse riding', 'Horse riding description', 80, 8, '2022-04-02T10:12:45.123', '2022-04-05T14:15:13.257') ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('Visiting a restaurant', 'Visiting the Plaza restaurant', 50, 7, '2022-04-02T10:12:45.123', '2022-04-02T14:15:13.257') ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('Visit to the drama theater', 'Description visit to the drama theater', 45, 2, '2022-03-30T10:12:45.123', '2022-04-08T14:15:13.257') ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('Shopping at the tool store', 'Description shopping at the tool store', 30, 10, '2022-03-25T10:12:45.123', '2022-04-01T14:15:13.257') ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('Shopping at the supermarket', 'Shopping at Lidl supermarket chain', 80, 12, '2022-04-01T10:12:45.123', '2022-04-014T14:15:13.257') ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ( 'Hot air balloon flight', 'An unforgettable hot air balloon flight', 150, 12, '2022-03-01T10:12:45.123', '2022-03-014T14:15:13.257') ON CONFLICT DO NOTHING;

INSERT INTO tag (name) VALUES ('rest') ON CONFLICT DO NOTHING;
INSERT INTO tag (name) VALUES ('nature') ON CONFLICT DO NOTHING;
INSERT INTO tag (name) VALUES ('shopping') ON CONFLICT DO NOTHING;
INSERT INTO tag (name) VALUES ('atv') ON CONFLICT DO NOTHING;
INSERT INTO tag (name) VALUES ('horse') ON CONFLICT DO NOTHING;
INSERT INTO tag (name) VALUES ('theater') ON CONFLICT DO NOTHING;
INSERT INTO tag (name) VALUES ('tool') ON CONFLICT DO NOTHING;
INSERT INTO tag (name) VALUES ('food') ON CONFLICT DO NOTHING;
INSERT INTO tag (name) VALUES ('supermarket') ON CONFLICT DO NOTHING;
INSERT INTO tag (name) VALUES ('restaurant') ON CONFLICT DO NOTHING;
INSERT INTO tag (name) VALUES ('flight') ON CONFLICT DO NOTHING;
INSERT INTO tag (name) VALUES ('visit') ON CONFLICT DO NOTHING;

INSERT INTO gift_certificate_tag VALUES (1, 1) ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate_tag VALUES (1, 2) ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate_tag VALUES (1, 4) ON CONFLICT DO NOTHING;

INSERT INTO gift_certificate_tag VALUES (2, 1) ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate_tag VALUES (2, 2) ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate_tag VALUES (2, 5) ON CONFLICT DO NOTHING;

INSERT INTO gift_certificate_tag VALUES (3, 8) ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate_tag VALUES (3, 10) ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate_tag VALUES (3, 12) ON CONFLICT DO NOTHING;

INSERT INTO gift_certificate_tag VALUES (4, 6) ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate_tag VALUES (4, 12) ON CONFLICT DO NOTHING;

INSERT INTO gift_certificate_tag VALUES (5, 3) ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate_tag VALUES (5, 7) ON CONFLICT DO NOTHING;

INSERT INTO gift_certificate_tag VALUES (6, 3) ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate_tag VALUES (6, 8) ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate_tag VALUES (6, 9) ON CONFLICT DO NOTHING;

INSERT INTO gift_certificate_tag VALUES (7, 1) ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate_tag VALUES (7, 2) ON CONFLICT DO NOTHING;
INSERT INTO gift_certificate_tag VALUES (7, 11) ON CONFLICT DO NOTHING;

INSERT INTO customer (name) VALUES ('user1') ON CONFLICT DO NOTHING;
INSERT INTO customer (name) VALUES ('user2') ON CONFLICT DO NOTHING;
INSERT INTO customer (name) VALUES ('user3') ON CONFLICT DO NOTHING;

INSERT INTO customer_order (customer_id, purchase_time, amount) VALUES (1, '2022-04-02T10:12', 150) ON CONFLICT DO NOTHING;
INSERT INTO customer_order (customer_id, purchase_time, amount) VALUES (2, '2022-04-03T10:12', 180) ON CONFLICT DO NOTHING;
INSERT INTO customer_order (customer_id, purchase_time, amount) VALUES (3, '2022-04-05T10:12', 125) ON CONFLICT DO NOTHING;
INSERT INTO customer_order (customer_id, purchase_time, amount) VALUES (1, '2022-04-04T10:12', 110) ON CONFLICT DO NOTHING;
INSERT INTO customer_order (customer_id, purchase_time, amount) VALUES (2, '2022-04-07T10:12', 50) ON CONFLICT DO NOTHING;

INSERT INTO customer_order_gift_certificate VALUES (1, 7) ON CONFLICT DO NOTHING;

INSERT INTO customer_order_gift_certificate VALUES (2, 1) ON CONFLICT DO NOTHING;
INSERT INTO customer_order_gift_certificate VALUES (2, 2) ON CONFLICT DO NOTHING;

INSERT INTO customer_order_gift_certificate VALUES (3, 2) ON CONFLICT DO NOTHING;
INSERT INTO customer_order_gift_certificate VALUES (3, 4) ON CONFLICT DO NOTHING;

INSERT INTO customer_order_gift_certificate VALUES (4, 5) ON CONFLICT DO NOTHING;
INSERT INTO customer_order_gift_certificate VALUES (4, 6) ON CONFLICT DO NOTHING;

INSERT INTO customer_order_gift_certificate VALUES (5, 3) ON CONFLICT DO NOTHING;

COMMIT;