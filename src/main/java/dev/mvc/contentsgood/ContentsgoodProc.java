package dev.mvc.contentsgood;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.contentsgood.ContentsgoodProc")
public class ContentsgoodProc implements ContentsgoodProcInter {
  @Autowired
  ContentsgoodDAOInter contentsgoodDAO;
  
  @Override
  public int create(ContentsgoodVO contentsgoodVO) {
    int cnt = this.contentsgoodDAO.create(contentsgoodVO);
    return cnt;
  }

  @Override
  public ArrayList<ContentsgoodVO> list_all() {
    ArrayList<ContentsgoodVO> list = this.contentsgoodDAO.list_all();
    return list;
  }

  @Override
  public ContentsgoodVO read(int contentsgoodno) {
    ContentsgoodVO contentsgoodVO = this.contentsgoodDAO.read(contentsgoodno);
    return contentsgoodVO;
  }
  
  @Override
  public int delete(int contentsgoodno) {
    int cnt = this.contentsgoodDAO.delete(contentsgoodno);
    return cnt;
  }

  @Override
  public int hartCnt(HashMap<String, Object> map) {
    int cnt = this.contentsgoodDAO.hartCnt(map);
    return cnt;
  }

  @Override
  public ContentsgoodVO readByContentsnoMemberno(HashMap<String, Object> map) {
    ContentsgoodVO contentsgoodVO = this.contentsgoodDAO.readByContentsnoMemberno(map);
    return contentsgoodVO;
  }

  @Override
  public ArrayList<ContentsContentsgoodMemberVO> list_all_join() {
    ArrayList<ContentsContentsgoodMemberVO> list = this.contentsgoodDAO.list_all_join();
    return list;
  }

}



