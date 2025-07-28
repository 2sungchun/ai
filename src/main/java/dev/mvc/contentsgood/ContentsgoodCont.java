package dev.mvc.contentsgood;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.calendar.CalendarVO;
import dev.mvc.cate.CateProcInter;
import dev.mvc.cate.CateVOMenu;
import dev.mvc.member.MemberProcInter;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/contentsgood")
public class ContentsgoodCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.cate.CateProc") // @Component("dev.mvc.cate.CateProc")
  private CateProcInter cateProc;
  
  @Autowired
  @Qualifier("dev.mvc.contentsgood.ContentsgoodProc") 
  ContentsgoodProcInter contentsgoodProc;
  
  public ContentsgoodCont() {
    System.out.println("-> ContentsgoodCont created.");
  }
  
  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue = "") String url) {
    ArrayList<CateVOMenu> menu = this.cateProc.menu();
    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }
  
  @PostMapping(value="/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody ContentsgoodVO contentsgoodVO) {
    System.out.println("-> 수신 데이터:" + contentsgoodVO.toString());
    
    // int memberno = 1; // test 
    int memberno = (int)session.getAttribute("memberno"); // 보안성 향상
    contentsgoodVO.setMemberno(memberno);
    
    int cnt = this.contentsgoodProc.create(contentsgoodVO);
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);
    
    return json.toString();
  }
  
//  /**
//   * 목록
//   * 
//   * @param model
//   * @return
//   */
//  // http://localhost:9091/contentsgood/list_all
//  @GetMapping(value = "/list_all")
//  public String list_all(Model model) {
//    ArrayList<ContentsgoodVO> list = this.contentsgoodProc.list_all();
//    model.addAttribute("list", list);
//
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);
//
//    return "contentsgood/list_all"; // /templates/contentsgood/list_all.html
//  }
  
  /**
   * 목록(contents, contentsgood, member 테이블 join)
   * 
   * @param model
   * @return
   */
  // http://localhost:9091/contentsgood/list_all
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    ArrayList<ContentsContentsgoodMemberVO> list = this.contentsgoodProc.list_all_join();
    model.addAttribute("list", list);

    ArrayList<CateVOMenu> menu = this.cateProc.menu();
    model.addAttribute("menu", menu);

    return "contentsgood/list_all_join"; // /templates/contentsgood/list_all_join.html
  }
  
  /**
   * 삭제 처리 http://localhost:9091/contentsgood/delete?calendarno=1
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, 
      Model model, 
      @RequestParam(name="contentsgoodno", defaultValue = "0") int contentsgoodno, 
      RedirectAttributes ra) {    
    
    if (this.memberProc.isAdmin(session)) { // 관리자 로그인 확인
      this.contentsgoodProc.delete(contentsgoodno);       // 삭제

      return "redirect:/contentsgood/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/contentsgood/post2get"; // @GetMapping(value = "/msg")
    }

  }
  
}







