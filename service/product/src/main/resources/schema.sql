create table product (
                         id bigint not null auto_increment primary key,
                         name varchar(100) not null,
                         price decimal(19,2) not null,
                         description varchar(1000),
                         category varchar(255) not null,
                         created_at datetime not null,
                         updated_at datetime not null,

                         key idx_name (name),
                         key idx_category_price (category, price),
                         key idx_category_name (category, name)
);

create table product_option (
                                id bigint not null auto_increment primary key,
                                product_id bigint not null,
                                color varchar(255) not null,
                                size int not null,
                                stock int not null,
                                status varchar(255),

                                key idx_product_option_product (product_id),
                                constraint uk_product_color_size unique (product_id, color, size),
                                constraint fk_product_option_product foreign key (product_id) references product (id)
);