/*  GROUP 16 SCHEMA.SQL */

--Make sure the table names are not in use
drop table ourSysDATE cascade constraints;
drop table Customer cascade constraints;
drop table Administrator cascade constraints;
drop table Product cascade constraints;
drop table Bidlog cascade constraints;
drop table Category cascade constraints;
drop table BelongsTo cascade constraints;


--CREATE TABLES WITH THEIR CORRECT SCHEMAS
create table ourSysDATE(
    c_date date,
    
    constraint pk_ourSysDATE primary key(c_date)
    );
    
create table Customer(
    login varchar2(10),
    password varchar2(10),
    name varchar2(20),
    address varchar2(30),
    email varchar2(20),
    
    constraint pk_Customer primary key(login)
);

create table Administrator(
    login varchar2(10),
    password varchar2(10),
    name varchar2(20),
    address varchar2(30),
    email varchar2(20),
    
    constraint pk_Administrator primary key(login)
);

create table Product(
    auction_id int,
    name varchar2(20),
    description varchar2(30),
    seller varchar2(10),
    start_date date,
    min_price int,
    number_of_days int,
    status varchar2(15) not null,
    buyer varchar2(10),
    sell_date date,
    amount int,
    
    constraint pk_Product primary key(auction_id),
    constraint fk_Product_Seller foreign key(seller) references Customer(login)
        on delete cascade,
    constraint fk_Product_Buyer foreign key(buyer) references Customer(login)
        on delete cascade,
    constraint Product_Status check  ( status in('under auction','sold','withdrawn','closed'))
);


create table Bidlog(
    bidsn int,
    auction_id int,
    bidder varchar2(10),
    bid_time date,
    amount int,
    
    constraint pk_Bidlog primary key(bidsn),
    constraint fk_Bidlog_Auction_ID foreign key(auction_id) references Product(auction_id)
        on delete cascade,
    constraint fk_Bidlog_Buyer foreign key(bidder) references Customer(login)
        on delete cascade
);


create table Category(
    name varchar2(20),
    parent_category varchar2(20),
    
    constraint pk_Category primary key(name),
    constraint fk_Category_Parent foreign key(parent_category) references Category(name)
        on delete cascade
);


create table BelongsTo(
    auction_id int,
    category varchar2(20),
    
    constraint pk_BelongsTo primary key(auction_id, category),
    constraint fk_BelongsTo_Auction_ID foreign key(auction_id) references Product(auction_id)
        on delete cascade,
    constraint fk_BelongsTo_Category foreign key(category) references Category(name)
        on delete cascade
);