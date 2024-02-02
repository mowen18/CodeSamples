/* Delete the tables if they already exist */
drop table  Restaurant;
drop table  Reviewer;
drop table  Rating;

/* Create the schema for our tables */
create table Restaurant(rID int, name varchar2(100), address varchar2(100), cuisine varchar2(100));
create table Reviewer(vID int, name varchar2(100));
create table Rating(rID int, vID int, stars int, ratingDate date);

/* Populate the tables with our data */
insert into Restaurant values(101, 'India House Restaurant', '59 W Grand Ave Chicago, IL 60654', 'Indian');
insert into Restaurant values(102, 'Bombay Wraps', '122 N Wells St Chicago, IL 60606', 'Indian');
insert into Restaurant values(103, 'Rangoli', '2421 W North Ave Chicago, IL 60647', 'Indian');
insert into Restaurant values(104, 'Cumin', '1414 N Milwaukee Ave Chicago, IL 60622', 'Indian');
insert into Restaurant values(105, 'Shanghai Inn', '4723 N Damen Ave Chicago, IL 60625', 'Chinese');
insert into Restaurant values(106, 'MingHin Cuisine', '333 E Benton Pl Chicago, IL 60601', 'Chinese');
insert into Restaurant values(107, 'Shanghai Terrace', '108 E Superior St Chicago, IL 60611', 'Chinese');
insert into Restaurant values(108, 'Jade Court', '626 S Racine Ave Chicago, IL 60607', 'Chinese');

insert into Reviewer values(2001, 'Sarah M.');
insert into Reviewer values(2002, 'Daniel L.');
insert into Reviewer values(2003, 'B. Harris');
insert into Reviewer values(2004, 'P. Suman');
insert into Reviewer values(2005, 'Suikey S.');
insert into Reviewer values(2006, 'Elizabeth T.');
insert into Reviewer values(2007, 'Cameron J.');
insert into Reviewer values(2008, 'Vivek T.');

insert into Rating values( 101, 2001,2, DATE '2011-01-22');
insert into Rating values( 101, 2001,4, DATE '2011-01-27');
insert into Rating values( 106, 2002,4, null);
insert into Rating values( 103, 2003,2, DATE '2011-01-20');
insert into Rating values( 108, 2003,4, DATE '2011-01-12');
insert into Rating values( 108, 2003,2, DATE '2011-01-30');
insert into Rating values( 101, 2004,3, DATE '2011-01-09');
insert into Rating values( 103, 2005,3, DATE '2011-01-27');
insert into Rating values( 104, 2005,2, DATE '2011-01-22');
insert into Rating values( 108, 2005,4, null);
insert into Rating values( 107, 2006,3, DATE '2011-01-15');
insert into Rating values( 106, 2006,5, DATE '2011-01-19');
insert into Rating values( 107, 2007,5, DATE '2011-01-20');

--Q1
--Use the Restaurants.sql file, which creates three tables Restaurant, Reviewer, and Rating. 
--In this problem, we are concerned with the Restaurant table, which has a single attribute 'Address' of type 'varchar2(100)'.
--We would like address to be searchable.  So we would like to create another table Restaurant_Locations with the following attributes:
--rID, name, street_address,  city, state, zipcode, cuisine

--A: Create Restaurant_Locations table. Use the source dataset to determine the data types (and sizes) to use for each of the attributes.
drop table Restaurant_Locations;
create table Restaurant_Locations(rID int, street_address varchar2(100),city varchar2(100), state varchar(3), zipcode varchar2(5), cuisine varchar2(100));


--B:Cursor
declare
    street_address restaurant.address%TYPE;
    city restaurant.address%TYPE;
    state restaurant.address%TYPE;
    zipcode restaurant.address%TYPE;
    rid restaurant.rid%type;
    cuisine restaurant.cuisine%TYPE;
 
    cursor L1Cursor is select rid, REGEXP_SUBSTR(address, '([[:digit:]])*([^[:space:]])([^[:space:]]*)(\s)([^[:space:]]*)([^[:space:]])([^[:space:]]*)'), rtrim((REGEXP_SUBSTR (address, '[^ ]*,',1, 1)), ','), ltrim((REGEXP_SUBSTR (address, ', [^ ]*',1, 1)), ','), substr(address, length(address)-4, 5), cuisine from Restaurant;
    begin
    open L1Cursor;
    loop
        fetch L1Cursor into rid, street_address, city, state, zipcode, cuisine;
        exit when L1Cursor%NotFound;

    insert into Restaurant_Locations values(rid, street_address, city, state, zipcode, cuisine);
    end loop; 
    close L1Cursor;
    end;
 


--Run query after PL/SQL procedure is completed
 SELECT * FROM Restaurant_Locations;