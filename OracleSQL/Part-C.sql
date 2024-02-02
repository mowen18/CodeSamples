DROP TABLE SocialNetwork;

CREATE TABLE SocialNetwork
(
  Person       VARCHAR2(25) NOT NULL,
  Friend       VARCHAR2(25) NOT NULL
);

insert into SocialNetwork values ('Amy', 'Brad');
insert into SocialNetwork values ('Amy', 'Christine');
insert into SocialNetwork values ('Amy', 'Edgar');
insert into SocialNetwork values ('Brad', 'Fiona');
insert into SocialNetwork values ('Brad', 'Gary');
insert into SocialNetwork values ('Brad', 'Hannah');
insert into SocialNetwork values ('Christine', 'Hannah');
insert into SocialNetwork values ('Christine', 'Ingrid');
insert into SocialNetwork values ('Christine', 'Dave');
insert into SocialNetwork values ('Dave', 'Ingrid');
insert into SocialNetwork values ('Dave', 'Kate');
insert into SocialNetwork values ('Dave', 'James');
insert into SocialNetwork values ('Dave', 'Leo');
insert into SocialNetwork values ('Edgar', 'Kate');
insert into SocialNetwork values ('Edgar', 'Melissa');
insert into SocialNetwork values ('Edgar', 'Nicole');
insert into SocialNetwork values ('Fiona', 'Amy');    
insert into SocialNetwork values ('Gary', 'Oliver');
insert into SocialNetwork values ('Hannah', 'Quincey');
insert into SocialNetwork values ('James', 'Quincey');
insert into SocialNetwork values ('Melissa', 'Leo');
insert into SocialNetwork values ('Oliver', 'Fiona');
insert into SocialNetwork values ('Oliver', 'Penny');
insert into SocialNetwork values ('Quincey', 'James');

With friendchain(p, f, path) as
(
select person, friend, person||'.'||friend from socialnetwork where person = 'Brad'
union all
select p, friend, path || '.' || friend
from friendchain, SocialNetwork
where f = person
and path not like '%' || friend || '%'
)
select person from socialnetwork
where person != 'Brad' and
person not in(select distinct(f) from friendchain)
union select friend from socialnetwork
where friend != 'Brad' and
friend not in(select distinct(f) from friendchain);

with bradsfriends(person,friend, path, cnt) AS
(
SELECT person,friend, person || '.' || friend, 1 from SocialNetwork
UNION ALL
SELECT S.person, bf.friend, s.person || '.' || path, cnt + 1 FROM SocialNetwork S, bradsfriends bf WHERE s.friend = bf.person AND path NOT LIKE '%' || s.person || '%'
)
--Select * from bradsfriends WHERE person = 'Brad' and Path NOT LIKE '%' || bradsfriends.friend || '%';
--Select distinct friend FROM bradsfriends WHERE path LIKE '%' || 'Brad' || '%';
--Select distinct friend from bradsfriends WHERE person ='Brad';--path LIKE '%' || 'Brad' || '%';
--3Select * from bradsfriends WHERE friend = 'Christine' AND cnt > 1;
--4
--SELECT * FROM bradsfriends WHEREperson = 'Amy' AND friend = 'James' AND cnt in (SELECT min(cnt) FROM bradsfriends WHERE person = 'Amy' AND friend = 'James')


with friendchain(p, f, path, count) as
(
select person, friend, person||'.'||friend, 1 from socialnetwork where friend='James'
union all
select person, f, person||'.'||path, count + 1
from friendchain, socialnetwork
where friend = p and path not like '%' || person || '%'
)
select * from friendchain where path like 'Amy%' and
count in (select min(count) from friendchain
where path like 'Amy%');
--select * from friendchain where count > 1;

--(1)People to whom Brad can reach to either directly or transitively.
With NETWORK(PERSON,FRIEND, path) AS(Select PERSON, FRIEND, PERSON || '.' || FRIEND FROM SOCIALNETWORK
UNION ALL
Select F.PERSON, R.FRIEND,F.PERSON|| '.' || path FROM SOCIALNETWORK F, NETWORK R WHERE R.PERSON = F.FRIEND AND path NOT LIKE '%' || F.PERSON || '%')
SELECT DISTINCT FRIEND from NETWORK WHERE PERSON='Brad';

--(2)People in DePauledIN network to whom Brad cannot reach to
with cte(PERSON,FRIEND, path)  as
(
select Person, Friend, Person || '.' || Friend from SocialNetwork
union all
select a.Person, b.Friend, a.Person ||'.'|| PATH from SocialNetwork a, cte b WHERE b.Person !=a.Friend AND PATH NOT LIKE '%' || b.Person || '%'
)
SELECT FRIEND FROM SOCIALNETWORK
UNION
SELECT Person FROM SOCIALNETWORK 
MINUS 
select distinct Friend from cte where Path like '%Brad%';

--(3)Only those people who are connected to Christine via transitive relationship i.e not an immediate follower.
with cte(PERSON,FRIEND, path)  as
(
select Person, Friend, Person || '.' || Friend from SocialNetwork 
union all
select a.Person, b.Friend, a.Person ||'.'|| PATH from SocialNetwork a, cte b WHERE a.Person = b.Friend AND PATH NOT LIKE '%' || b.Person || '%'
)
select distinct Friend from cte where Friend not in (select Friend from SocialNetwork where Person = 'Christine') AND path not like '%Christine%';


--4Find the shortest path to reach from Amy to James.
With Reaches(person, friend, path) AS
(
Select person, friend, person || '.' || friend FROM socialnetwork
UNION all
Select F.person, R.friend,F.person || '.' || path FROM socialnetwork F, Reaches R WHERE F.PERSON = 'Amy' AND R.friend = 'James'
) 
cycle person, friend set cycle to 1 default 0
SELECT person, friend, path from Reaches where path like 'Amy%James%';