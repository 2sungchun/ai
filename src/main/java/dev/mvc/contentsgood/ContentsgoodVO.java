package dev.mvc.contentsgood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE contentsgood (
//    contentsgoodno    NUMBER(10)        NOT NULL,
//    rdate            DATE        NOT NULL,
//    contentsno        NUMBER(10)        NOT NULL,
//    memberno        NUMBER(10)        NOT NULL,
//    PRIMARY KEY (contentsgoodno),
//    FOREIGN KEY (contentsno) REFERENCES contents(contentsno),
//    FOREIGN KEY (memberno) REFERENCES member(memberno)
//);

@Getter @Setter @ToString
public class ContentsgoodVO {
  /** 컨텐츠 추천 번호 */
  private int contentsgoodno;
  
  /** 등록일 */
  private String rdate;
  
  /** 컨텐츠 번호 */
  private int contentsno;
  
  /** 회원 번호 */
  private int memberno;
  
}



