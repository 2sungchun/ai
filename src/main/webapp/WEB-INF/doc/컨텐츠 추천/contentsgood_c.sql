DROP TABLE contentsgood;

CREATE TABLE contentsgood (
    contentsgoodno    NUMBER(10)        NOT NULL,
    rdate            DATE        NOT NULL,
    contentsno        NUMBER(10)        NOT NULL,
    memberno        NUMBER(10)        NOT NULL,
    PRIMARY KEY (contentsgoodno),
    FOREIGN KEY (contentsno) REFERENCES contents(contentsno),
    FOREIGN KEY (memberno) REFERENCES member(memberno)
);


DROP SEQUENCE contentsgood_seq;

CREATE SEQUENCE contentsgood_seq
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지


-- 데이터 삽입
INSERT INTO contentsgood(contentsgoodno, rdate, contentsno, memberno)
VALUES (contentsgood_seq.nextval, sysdate, 43, 1);

INSERT INTO contentsgood(contentsgoodno, rdate, contentsno, memberno)
VALUES (contentsgood_seq.nextval, sysdate, 43., 1);

INSERT INTO contentsgood(contentsgoodno, rdate, contentsno, memberno)
VALUES (contentsgood_seq.nextval, sysdate, 43, 1);

INSERT INTO contentsgood(contentsgoodno, rdate, contentsno, memberno)
VALUES (contentsgood_seq.nextval, sysdate, 43, 1);

COMMIT;

-- 전체 목록
SELECT contentsgoodno, rdate, contentsno, memberno
FROM contentsgood
ORDER BY contentsgoodno DESC;

CONTENTSGOODNO RDATE               CONTENTSNO   MEMBERNO
-------------- ------------------- ---------- ----------
             5 2025-01-07 10:51:32          3          5
             3 2025-01-07 10:50:51          1          4
             2 2025-01-07 10:50:34          1          3
             1 2025-01-07 10:50:17          1          1

-- PK 조회
SELECT contentsgoodno, rdate, contentsno, memberno
FROM contentsgood
WHERE contentsgoodno = 5;

-- contentsno, memberno로 조회
SELECT contentsgoodno, rdate, contentsno, memberno
FROM contentsgood
WHERE contentsno=43 AND memberno=1;

-- 삭제
DELETE FROM contentsgood
WHERE contentsgoodno = 5;

commit;

-- 특정 컨텐츠의 특정 회원 추천 갯수 산출
SELECT COUNT(*) as cnt
FROM contentsgood
WHERE contentsno=1 AND memberno=1;

       CNT
----------
         1 <-- 이미 추천을 함
         
SELECT COUNT(*) as cnt
FROM contentsgood
WHERE contentsno=5 AND memberno=1;

       CNT
----------
         0 <-- 추천 안됨
         
-- JOIN, 어느 배우를 누가 추천 했는가?
SELECT contentsgoodno, rdate, contentsno, memberno
FROM contentsgood
ORDER BY contentsgoodno DESC;

-- 테이블 2개 join
SELECT r.contentsgoodno, r.rdate, r.contentsno, c.title, r.memberno
FROM contents c, contentsgood r
WHERE c.contentsno = r.contentsno
ORDER BY contentsgoodno DESC;

-- 테이블 3개 join, as 사용시 컴럼명 변경 가능: c.title as c_title
SELECT r.contentsgoodno, r.rdate, r.contentsno, c.title as c_title, r.memberno, m.id, m.mname
FROM contents c, contentsgood r, member m
WHERE c.contentsno = r.contentsno AND r.memberno = m.memberno
ORDER BY contentsgoodno DESC;

   
 
 