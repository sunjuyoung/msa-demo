CREATE TABLE coupon (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,

                        type VARCHAR(20) NOT NULL,                -- ENUM 대신 문자열 저장 (CouponType)
                        code VARCHAR(50) NOT NULL UNIQUE,         -- 쿠폰 코드 (이벤트, 가입 등)

                        discount_amount INT NOT NULL,             -- 할인 금액
                        min_order_amount INT NOT NULL,            -- 최소 주문 금액

                        start_date DATETIME NOT NULL,             -- 시작일
                        end_date DATETIME NOT NULL,               -- 종료일

                        total_quantity INT NOT NULL,              -- 총 발급 수량
                        remaining_quantity INT NOT NULL,          -- 남은 수량

                        created_at DATETIME NOT NULL,             -- 생성일
                        updated_at DATETIME NOT NULL              -- 수정일
) ;
