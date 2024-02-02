set serveroutput on;
DROP TABLE CONTRACT CASCADE CONSTRAINTS;
DROP TABLE TASK CASCADE CONSTRAINTS;

 
CREATE TABLE TASK (
    TaskID CHAR(3),
    TasKName VARCHAR(20),
    ContractCount NUMERIC(1,0) DEFAULT 0,
    CONSTRAINT PK_TASK PRIMARY KEY (TaskID)
);

CREATE TABLE CONTRACT(
    TaskID CHAR(3),
    WorkerID CHAR(7),
    Payment NUMERIC(6,2),
    CONSTRAINT PK_CONTRACT PRIMARY KEY (TaskID, WorkerID),
    CONSTRAINT FK_CONTRACTTASK FOREIGN KEY (TaskID) REFERENCES TASK (TaskID)
);


INSERT INTO TASK (TaskID, TaskName) VALUES ('333', 'Security' );
INSERT INTO TASK (TaskID, TaskName) VALUES ('322', 'Infrastructure');
INSERT INTO TASK (TaskID, TaskName) VALUES ('896', 'Compliance' );

--SELECT * FROM TASK;
--COMMIT;
--Q1 FIRST TRIGGER
create or replace trigger NewContract 
before insert on contract
for each row
declare
cCount task.contractcount%TYPE;
begin
    select contractcount into cCount from task
        where taskid = :new.taskid;
        
        if cCount = 3 then
            raise_application_error (-20888, 'The task is full');         
        else
            update task
            set contractcount = contractcount +1
            where taskid = :new.taskid;
        end if;
end;
/


--Q2
--SECOND TRIGGER
create or replace trigger EndContract
after delete on contract
for each row
declare
cCount task.contractcount%TYPE;
taskD  task.taskid%TYPE;
begin
    update task
    set contractcount = contractcount -1
    where taskid = :old.taskid;
    select contractcount into cCount from task
        where taskid = :old.taskid;
    select taskid into taskD from task
        where taskid = :old.taskid;
    dbms_output.put_line('ContractCount reduced to ' || cCount || ' for task ' || taskD);
end;
/


--Q3 THIRD TRIGGER
create or replace trigger NoChanges
before update on contract
begin
    raise_application_error (-20889, 'No updates permitted to existing rows of contract');         
end;
/

--select * from contract;
--select * from Task;
--Querys for first trigger - Q1
insert into contract values ('322', '1123568', 150);
insert into contract values ('333', '1123567', 150);
insert into contract values ('333', '1123565', 150);
insert into contract values ('322', '1123569', 150);
insert into contract values ('322','1123562',200);
insert into contract values ('333', '1123561', 150);
insert into contract values ('896', '1123560', 150);
insert into contract values ('896', '0123560', 150);
insert into contract values ('333', '1123564', 150);--4th insert for task 333 will trigger task full error

--select * from contract;
--select * from Task;
--Querys for second trigger - Q2
delete from contract where taskid = 322;
delete from contract where taskid = 333;

--Querys for third trigger - Q3
update contract set payment = 300 where taskid = 896;--this will trigger the no update error
