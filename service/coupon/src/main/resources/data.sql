-- 회원가입 쿠폰 (SIGNUP)
INSERT INTO coupon (
    type, code, discount_amount, min_order_amount,
    start_date, end_date, total_quantity, remaining_quantity,
    created_at, updated_at
) VALUES (
             'SIGNUP',
             'WELCOME10',
             10000,                   -- 1만원 할인
             30000,                   -- 최소 주문 3만원 이상
             NOW(), DATE_ADD(NOW(), INTERVAL 6 MONTH), -- 오늘 ~ 6개월 뒤
             1000,                    -- 총 1000장
             1000,                    -- 남은 1000장
             NOW(), NOW()
         );

-- 이벤트 쿠폰 (EVENT)
INSERT INTO coupon (
    type, code, discount_amount, min_order_amount,
    start_date, end_date, total_quantity, remaining_quantity,
    created_at, updated_at
) VALUES (
             'EVENT',
             'SUMMER20',
             5000,                   --  할인
             50000,                   -- 최소 주문 5만원 이상
             '2025-06-01 00:00:00', '2025-08-31 23:59:59', -- 여름 이벤트 기간
             500,                     -- 총 500장
             500,                     -- 남은 500장
             NOW(), NOW()
         );

-- 또 다른 이벤트 쿠폰
INSERT INTO coupon (
    type, code, discount_amount, min_order_amount,
    start_date, end_date, total_quantity, remaining_quantity,
    created_at, updated_at
) VALUES (
             'EVENT',
             'BLACKFRIDAY30',
             30000,                   -- 3만원 할인
             100000,                  -- 최소 주문 10만원 이상
             '2025-11-01 00:00:00', '2025-11-30 23:59:59',
             2000,
             2000,
             NOW(), NOW()
         );
