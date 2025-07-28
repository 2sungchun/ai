package dev.mvc.mail;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.LLMKey;
import dev.mvc.tool.MailTool;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;

// 메일 전송 테스트용
@Controller
@RequestMapping("/mail")
public class MailCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  private final RestTemplate restTemplate;

  public MailCont(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    System.out.println("-> this.restTemplate hashCode: " + this.restTemplate.hashCode());
    System.out.println("-> MailCont created.");
  }

  // http://localhost:9091/mail/form
  /**
   * 메일 입력 화면
   * 
   * @return
   */
  @GetMapping(value = "/form")
  public String form(Model model) {
    return "/mail/form"; // /templates/mail/form.html
  }

  // http://localhost:9091/mail/send
  /**
   * 메일 전송
   * 
   * @return
   */
  @PostMapping(value = "/send")
  public String send(@RequestParam(name = "receiver", defaultValue = "") String receiver,
      @RequestParam(name = "from", defaultValue = "") String from,
      @RequestParam(name = "title", defaultValue = "") String title,
      @RequestParam(name = "content", defaultValue = "") String content) {
    MailTool mailTool = new MailTool();
    
    content = Tool.convertChar(content);
    System.out.println("-> content: " + content);
    
    mailTool.send(receiver, from, title, content); // 메일 전송

    return "/mail/sended"; // /templates/mail/sended.html
  }

  // http://localhost:9091/mail/form_file
  /**
   * 파일 첨부 메일 입력폼
   * 
   * @return
   */
  @GetMapping(value = "/form_file")
  public String form_file() {
    return "/mail/form_file";
  }

//    // http://localhost:9091/mail/send_file.do
//    /**
//     * 메일 전송
//     * @return
//     */
//    @RequestMapping(value = {"/mail/send_file.do"}, method=RequestMethod.POST)
//    public ModelAndView send_file(String receiver, String from, String title, String content,
//                                             MultipartFile file1MF) {
//      ModelAndView mav = new ModelAndView();
//      mav.setViewName("/mail/sended");  // /WEB-INF/views/mail/sended.jsp
//
//      MailTool mailTool = new MailTool();
//      mailTool.send_file(receiver, from, title, content, file1MF, "C:/kd/deploy/mail/images/"); // 메일 전송
//      
//      return mav;
//    }

  // http://localhost:9091/mail/send_file
  /**
   * 메일 전송
   * 
   * @return
   */
  @PostMapping(value = "/send_file")
  public String send_file(@RequestParam(name = "receiver", defaultValue = "") String receiver,
      @RequestParam(name = "from", defaultValue = "") String from,
      @RequestParam(name = "title", defaultValue = "") String title,
      @RequestParam(name = "content", defaultValue = "") String content,
      @RequestParam(name = "file1MF", defaultValue = "null") MultipartFile[] file1MF) {
    // java.io.FileNotFoundException:
    // C:\kd\deploy\mvc_sms_mail\mail\storage\cup01.jpg (지정된 파일을 찾을 수 없습니다)
    // 전송 하려는 파일을 C:/kd/deploy/mvc_sms_mail/mail/storage/ 폴더에 사전에 복사한후 업로드, 다른 폴더
    // 인식안됨. ★★★★★
    MailTool mailTool = new MailTool();
    mailTool.send_file(receiver, from, title, content, file1MF, "C:/kd/deploy/resort/mail/storage/"); // 메일 전송

    return "/mail/sended";
  }

  // http://localhost:9091/mail/form_oprnai
  /**
   * 번역 기능 지원 메일 입력폼
   * 
   * @return
   */
  @GetMapping(value = "/form_openai")
  public String form_oprnai() {
    return "/mail/form_openai";
  }

  /**
   * 커뮤니티 요약 처리
   * 
   * @return
   */
  @PostMapping(value = "/translator")
  @ResponseBody
  public String mail_translator_proc(HttpSession session, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"article":"요약할 문장..."}
    JSONObject src = new JSONObject(json_src); // String -> JSON
    // String current_passwd = (String)src.get("current_passwd"); // 값 가져오기
    // System.out.println("-> current_passwd: " + current_passwd);

    if (this.memberProc.isAdmin(session)) {
      // FastAPI 서버 URL (포트는 환경에 맞게 조정)
      String url = "http://localhost:8000/translator";

      // HTTP 헤더 설정 (JSON)
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      // 요청 바디에 담을 데이터
      Map<String, Object> body = new HashMap<>();
      body.put("SpringBoot_FastAPI_KEY", new LLMKey().getSpringBoot_FastAPI_KEY());
      body.put("title", src.get("title"));
      body.put("content", src.get("content"));
      body.put("language", src.get("language"));

      // HttpEntity로 헤더 + 바디 묶기
      HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      // POST 요청 보내고, 결과를 String으로 받기
      String response = restTemplate.postForObject(url, requestEntity, String.class);
      System.out.println("-> response: " + response);

      return response;
    } else {
      return "redirect:/member/login_cookie_need?url=/openai/mail_translator"; // redirect
    }

  }

}