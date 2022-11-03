INSERT INTO T_BUSINESS(id, account_id, status, suspended, balance, currency, country)
    VALUES
        (1, 1, 2, null, 120000, 'XAF', 'CM'),
        (2, 1, 2, null, 120000, 'EUR', 'FR'),
        (3, 1, 2, null, 120000, 'EUR', 'FR')
    ;

INSERT INTO T_PAYMENT_METHOD(id, token, account_id, number, country, owner_name, type, status, deactivated)
    VALUES
        (1001, 'token-100', 100, '+237690000100', 'CM', 'Roger Milla', 1, 1, null),
        (2001, 'token-200', 200, '+237690000200', 'CM', 'Omam Biyick', 1, 1, null)
    ;

INSERT INTO T_ORDER(id, business_fk, customer_id, customer_name, status, currency, total_price)
    VALUES
        ('order-100', 1, 100, 'Roger Milla', 1, 'XAF', 50000),
        ('order-200', 1, 100, 'Roger Milla', 1, 'XAF', 500)
    ;

INSERT INTO T_TRANSACTION(id, idempotency_key, type, status, gateway_type, payment_method_fk, business_fk, order_fk, customer_id, amount, fees, gateway_fees, net, currency, gateway_transaction_id)
    VALUES
        ('tx-101', 'idempotent-101', 1, 1, 3, 2001, 1, 'order-100', 100, 1500, 0, 0, 1500, 'XAF', 'TX-00000-000-101'),
        ('tx-102', 'idempotent-102', 1, 2, 3, 2001, 2, 'order-100', 100, 1500, 0, 0, 1500, 'XAF', 'TX-00000-000-102'),
        ('tx-202', 'idempotent-201', 2, 2, 3, 2001, 1, 'order-200', 200, 1500, 5, 0, 1495, 'XAF', 'TX-00000-000-202')
    ;