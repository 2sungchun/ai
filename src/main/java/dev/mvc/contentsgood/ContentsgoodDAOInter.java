package dev.mvc.contentsgood;

import java.util.ArrayList;
import java.util.HashMap;

public interface ContentsgoodDAOInter {
  /**
   * 등록, 추상 메소드
   * @param contentsgoodVO
   * @return
   */
  public int create(ContentsgoodVO contentsgoodVO);
  
  /**
   * 모든 목록
   * @return
   */
  public ArrayList<ContentsgoodVO> list_all();
  
  /**
   * 삭제
   * @param contentsgoodno
   * @return
   */
  public int delete(int contentsgoodno);
  
  /**
   * 특정 컨텐츠의 특정 회원 추천 갯수 산출
   * @param map
   * @return
   */
  public int hartCnt(HashMap<String, Object> map);  

  /**
   * 조회
   * @param contentsgoodno
   * @return
   */
  public ContentsgoodVO read(int contentsgoodno);

  /**
   * contentsno, memberno로 조회
   * @param map
   * @return
   */
  public ContentsgoodVO readByContentsnoMemberno(HashMap<String, Object> map);
  
  /**
   * 모든 목록, 테이블 3개 join
   * @return
   */
  public ArrayList<ContentsContentsgoodMemberVO> list_all_join();
  
}




