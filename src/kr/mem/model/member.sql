select * from EMPLOYEES;
-- 회원가입
create table tblMem(
	num number primary key,
	name varchar2(20) not null,
	phone varchar2(20) not null,
	addr varchar2(50),
	lat number(16,12),
	lng number(16,12)
)
-- drop table tblMem;

create sequence seq_num;
insert into tblMem values(seq_num.nextval, '나길동', '010-1111-1111', '광주광역시 서구 풍암동', 35.1257699845615, 126.852047602507);

select * from tblMem;

--alter table tblmem modify name varchar2(50);
--alter table tblmem modify addr varchar2(150);