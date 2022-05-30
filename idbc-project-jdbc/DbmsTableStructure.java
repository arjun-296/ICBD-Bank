use mydb
create table customertable(customerid int primary key auto_increment, 
firstname varchar(25)not null, 
lastname varchar(20),
dob date not null) auto_increment=10;



create table account
(accountno bigint primary key auto_increment,
acctype enum('save','pay'),
customerid int not null,
balance double default 0,
Interest double not null default 0) auto_increment= 1320047374760

alter table account add constraint key_ca foreign key(customerid) references customertable(customerid)

create table logbook
(logid int primary key auto_increment,
accountno bigint not null,
TransacationType varchar(10),
amount int not null,
logdate datetime default now())


use mydb

desc customertable;
desc account;
desc logbook

select * from account;
select * from logbook;
select *from customertable

delete from account where customerid=1 

drop table logbook

alter table account change Interest  InterestRate double

 
select customertable.customerid, firstname,lastname,accountno,acctype,InterestRate,balance
from customertable
inner join account
on customertable.customerid=account.customerid


	
select firstname as salespersonName, salesid, count(fname) as numberofCustomersdealt 
from salesperson
inner join customer
on salesid=sale_id
group by sale_id


