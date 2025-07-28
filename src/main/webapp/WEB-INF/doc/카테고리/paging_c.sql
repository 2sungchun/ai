-- 1) 테이블 생성 및 데이터 준비
DROP TABLE PG;
 
CREATE TABLE PG(
  no    NUMBER(5) NOT NULL,
  title  VARCHAR(20) NOT NULL,
  PRIMARY KEY(no )
);

-- 일련번호를 sequence를 사용하지 않고 subquery를 이용하여 생성하는 경우
-- 서브 쿼리: SQL 내부에 SQL 사용, ()가 존재해야함, 독립적으로 실행 가능.
-- (SELECT NVL(MAX(no ), 0) +1 as no  FROM pg)
-- MAX(): 최대값을 가져오는 함수, 레코드가 없으면 null을 리턴
-- NVL(MAX(no ), 0): MAX 함수가 null이면 특정값으로 대체
-- 레코드가 없는 경우 null을 리턴
SELECT MAX(no) as no  FROM pg;

-- 레코드가 없는 경우 null에 대한 사칙연산은 null을 리턴
SELECT MAX(no) + 1 as no  FROM pg;

-- NVL(null, 0): null이 0으로 변경됨으로 +1 정상적으로 진행됨.
SELECT NVL(MAX(no), 0)+1 as no  FROM pg;
        NO
----------
         1

-- PK용 일련번호는 Subquery만 가능한 것이 아님.
-- 쿼리안에 쿼리가 있는 서브 쿼리의 사용, 서브 쿼리가 먼저 실행됨.
INSERT INTO pg(no , title) VALUES((SELECT NVL(MAX(no ), 0) +1 as no  FROM pg), '01월');
INSERT INTO pg(no , title) VALUES((SELECT NVL(MAX(no ), 0) +1 as no  FROM pg), '02월');
INSERT INTO pg(no , title) VALUES((SELECT NVL(MAX(no ), 0) +1 as no  FROM pg), '03월');
INSERT INTO pg(no , title) VALUES((SELECT NVL(MAX(no ), 0) +1 as no  FROM pg), '04월');
INSERT INTO pg(no , title) VALUES((SELECT NVL(MAX(no ), 0) +1 as no  FROM pg), '05월');
INSERT INTO pg(no , title) VALUES((SELECT NVL(MAX(no ), 0) +1 as no  FROM pg), '06월');
INSERT INTO pg(no , title) VALUES((SELECT NVL(MAX(no ), 0) +1 as no  FROM pg), '07월');
INSERT INTO pg(no , title) VALUES((SELECT NVL(MAX(no ), 0) +1 as no  FROM pg), '08월');
INSERT INTO pg(no , title) VALUES((SELECT NVL(MAX(no ), 0) +1 as no  FROM pg), '09월');
INSERT INTO pg(no , title) VALUES((SELECT NVL(MAX(no ), 0) +1 as no  FROM pg), '10월');
INSERT INTO pg(no , title) VALUES((SELECT NVL(MAX(no ), 0) +1 as no  FROM pg), '11월');
INSERT INTO pg(no , title) VALUES((SELECT NVL(MAX(no ), 0) +1 as no  FROM pg), '12월');

COMMIT;

SELECT no, title FROM pg;

  NO TITLE
 --- -----
   1 01월
   2 02월
   3 03월
   4 04월
   5 05월
   6 06월
   7 07월
   8 08월
   9 09월
  10 10월
  11 11월
  12 12월

-- 분기별 페이징(1분기:1~3월, 2분기:4~6월, 3분기:7~9월, 4분기:10~12월), FWGHSRO
-- 1분기
SELECT no, title, r
FROM(
  SELECT no, title, rownum as r
  FROM(
    SELECT no, title
    FROM pg
    ORDER BY no ASC
  )
)
WHERE r>=1 AND r<=3;

        NO TITLE                         R
---------- -------------------- ----------
         1 01월                          1
         2 02월                          2
         3 03월                          3

-- 2분기
SELECT no, title, r
FROM(
  SELECT no, title, rownum as r
  FROM(
    SELECT no, title
    FROM pg
    ORDER BY no ASC
    )
)
WHERE r>=4 AND r<=6;

        NO TITLE                         R
---------- -------------------- ----------
         4 04월                          4
         5 05월                          5
         6 06월                          6
         
-- 3분기
SELECT no, title, r
FROM(
  SELECT no, title, rownum as r
  FROM(
    SELECT no, title
    FROM pg
    ORDER BY no ASC
  )
)
WHERE r>=7 AND r<=9;

        NO TITLE                         R
---------- -------------------- ----------
         7 07월                          7
         8 08월                          8
         9 09월                          9
         
-- 4분기
SELECT no, title, r
FROM(
  SELECT no, title, rownum as r
  FROM(
    SELECT no, title
    FROM pg
    ORDER BY no ASC
  )
)
WHERE r>=10 AND r<=12;

        NO TITLE                         R
---------- -------------------- ----------
        10 10월                         10
        11 11월                         11
        12 12월                         12
        
        
        