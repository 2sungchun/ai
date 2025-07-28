package dev.mvc.resort_v1sbm3c;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import dev.mvc.cate.CateDAOInter;
import dev.mvc.cate.CateProcInter;
import dev.mvc.cate.CateVO;

@SpringBootTest
class ResortV1sbm3cApplicationTests {
  // CateDAOInter interface 객체를 만들수 없으나,
  // Spring이 CateDAOInter interface를 자동으로 구현하여
  // 객체를 생성하여 cateDAO 변수에 할당함.
  @Autowired
  private CateDAOInter cateDAO;

  @Autowired // Spring이 CateProcInter를 구현한 CateProc 클래스의 객체를 생성하여 할당
  @Qualifier("dev.mvc.cate.CateProc")
  private CateProcInter cateProc;

  @Test
	void contextLoads() {
	}

//  @Test // 자동 실행
//  public void testCreate() {
//    CateVO cateVO = new CateVO();
//    cateVO.setCateno(1);
//    cateVO.setGrp("여행");
//    cateVO.setName("강원도");
//    cateVO.setCnt(0);
//    cateVO.setSeqno(1);
//    cateVO.setVisible("Y");
//    cateVO.setRdate("2025-03-18");
//    
//    int cnt = this.cateDAO.create(cateVO);
//    System.out.println("-> cnt: " + cnt);
//    
//  }

  @Test // 자동 실행
  public void testCreate() {
    CateVO cateVO = new CateVO();
    cateVO.setCateno(1);
    cateVO.setGrp("여행");
    cateVO.setName("바다");
    cateVO.setCnt(0);
    cateVO.setSeqno(1);
    cateVO.setVisible("Y");
    cateVO.setRdate("2025-03-19");
    
    int cnt = this.cateProc.create(cateVO);
    System.out.println("-> cnt: " + cnt);
    
  }
  
}







