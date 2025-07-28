package dev.mvc.openai;

import java.io.IOException;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import dev.mvc.cate.CateVO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.LLMKey;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/openai")
public class OpenAICont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")  // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  private final RestTemplate restTemplate;

  public OpenAICont(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    System.out.println("-> this.restTemplate hashCode: " + this.restTemplate.hashCode());
    System.out.println("-> OpenAICont created.");    
  }
  
  /**
   * 번역기
   * @return
   */
  @GetMapping(value="/translator")  
  public String translator(HttpSession session) {
    if (this.memberProc.isAdmin(session)) {
      return "openai/translator"; // /templates/openai/translator.html      
    } else {
      return "redirect:/member/login_cookie_need?url=/openai/translator"; // redirect
    }   

  }
  
  /**
   * 번역기
   * @return
   */
  @PostMapping(value="/translator")
  @ResponseBody
  public String translator_proc(HttpSession session, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"current_passwd":"1234"}
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
      body.put("sentence", src.get("sentence"));
      body.put("language", src.get("language"));
      body.put("age", Integer.parseInt(src.get("age").toString())); // Object -> String - > Integer

      // HttpEntity로 헤더 + 바디 묶기
      HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      // POST 요청 보내고, 결과를 String으로 받기
      String response = restTemplate.postForObject(url, requestEntity, String.class);
      System.out.println("-> response: " + response);
      
      return response;    
    } else {
      return "redirect:/member/login_cookie_need?url=/openai/translator"; // redirect
    }   

  }  
  
  /**
   * 영화 추천
   * @return
   */
  @GetMapping(value="/movie")  
  public String movie(HttpSession session, Model model) {
    if (this.memberProc.isAdmin(session)) {
      model.addAttribute("cnt", 25);
      
      return "openai/movie"; // /templates/openai/movie.html  
    } else {
      return "redirect:/member/login_cookie_need?url=/openai/movie"; // redirect
    }   
    
  }

  /**
   * 영화 추천
   * @return
   */
  @PostMapping(value="/movie")
  @ResponseBody
  public String movie_proc(HttpSession session, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"current_passwd":"1234"}
    JSONObject src = new JSONObject(json_src); // String -> JSON
    // String current_passwd = (String)src.get("current_passwd"); // 값 가져오기
    // System.out.println("-> current_passwd: " + current_passwd);
    
    if (this.memberProc.isAdmin(session)) {
      // FastAPI 서버 URL (포트는 환경에 맞게 조정)
      String url = "http://localhost:8000/movie";

      // HTTP 헤더 설정 (JSON)
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      // 요청 바디에 담을 데이터
      Map<String, Object> body = new HashMap<>();
      body.put("SpringBoot_FastAPI_KEY", new LLMKey().getSpringBoot_FastAPI_KEY());
      body.put("movie", src.get("movie"));

      // HttpEntity로 헤더 + 바디 묶기
      HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      // POST 요청 보내고, 결과를 String으로 받기
      String response = restTemplate.postForObject(url, requestEntity, String.class);
      System.out.println("-> response: " + response);
      
      return response;    
    } else {
      return "redirect:/member/login_cookie_need?url=/openai/translator"; // redirect
    }   

  }  
  
  
  /**
   * 나의 관심 분야 알기
   * @return
   */
  @GetMapping(value="/genre")  
  public String genre(HttpSession session, Model model) {
    if (this.memberProc.isAdmin(session)) {
      model.addAttribute("cnt", 25);
      
      return "openai/genre"; // /templates/openai/movie.html  
    } else {
      return "redirect:/member/login_cookie_need?url=/openai/genre"; // redirect
    }   
    
  }
  
  /**
   * 나의 관심 분야 알기
   * @return
   */
  @PostMapping(value="/genre")
  @ResponseBody
  public String genre_proc(HttpSession session, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"current_passwd":"1234"}
    JSONObject src = new JSONObject(json_src); // String -> JSON
    // String current_passwd = (String)src.get("current_passwd"); // 값 가져오기
    // System.out.println("-> current_passwd: " + current_passwd);
    
    if (this.memberProc.isAdmin(session)) {
      // FastAPI 서버 URL (포트는 환경에 맞게 조정)
      String url = "http://localhost:8000/genre";

      // HTTP 헤더 설정 (JSON)
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      // 요청 바디에 담을 데이터
      Map<String, Object> body = new HashMap<>();
      body.put("SpringBoot_FastAPI_KEY", new LLMKey().getSpringBoot_FastAPI_KEY());
      body.put("movie", src.get("movie"));

      // HttpEntity로 헤더 + 바디 묶기
      HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      // POST 요청 보내고, 결과를 String으로 받기
      String response = restTemplate.postForObject(url, requestEntity, String.class);
      System.out.println("-> response: " + response);
      
      return response;    
    } else {
      return "redirect:/member/login_cookie_need?url=/openai/translator"; // redirect
    }   

  }  
    
  /**
   * 커뮤니티 요약
   * @return
   */
  @GetMapping(value="/summary")  
  public String summary(HttpSession session, Model model) {
    if (this.memberProc.isAdmin(session)) {
      // model.addAttribute("cnt", 25);
      
      return "openai/summary"; // /templates/openai/summary.html  
    } else {
      return "redirect:/member/login_cookie_need?url=/openai/summary"; // redirect
    }   
    
  }
  
  /**
   * 커뮤니티 요약 처리
   * @return
   */
  @PostMapping(value="/summary")
  @ResponseBody
  public String summary_proc(HttpSession session, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"article":"요약할 문장..."}
    JSONObject src = new JSONObject(json_src); // String -> JSON
    // String current_passwd = (String)src.get("current_passwd"); // 값 가져오기
    // System.out.println("-> current_passwd: " + current_passwd);
    
    if (this.memberProc.isAdmin(session)) {
      // FastAPI 서버 URL (포트는 환경에 맞게 조정)
      String url = "http://localhost:8000/summary";

      // HTTP 헤더 설정 (JSON)
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      // 요청 바디에 담을 데이터
      Map<String, Object> body = new HashMap<>();
      body.put("SpringBoot_FastAPI_KEY", new LLMKey().getSpringBoot_FastAPI_KEY());
      body.put("article", src.get("article"));

      // HttpEntity로 헤더 + 바디 묶기
      HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      // POST 요청 보내고, 결과를 String으로 받기
      String response = restTemplate.postForObject(url, requestEntity, String.class);
      System.out.println("-> response: " + response);
      
      return response;    
    } else {
      return "redirect:/member/login_cookie_need?url=/openai/summary"; // redirect
    }   

  } 

  /**
   * 감정분석
   * @return
   */
  @GetMapping(value="/emotion")  
  public String emotion(HttpSession session, Model model) {
    if (this.memberProc.isAdmin(session)) {
      // model.addAttribute("cnt", 25);
      
      return "openai/emotion"; // /templates/openai/emotion.html  
    } else {
      return "redirect:/member/login_cookie_need?url=/openai/summary"; // redirect
    }   
    
  }
  
  /**
   * 감정 분석 처리
   * @return
   */
  @PostMapping(value="/emotion")
  @ResponseBody
  public String emotion_proc(HttpSession session, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"article":"요약할 문장..."}
    JSONObject src = new JSONObject(json_src); // String -> JSON
    // String current_passwd = (String)src.get("current_passwd"); // 값 가져오기
    // System.out.println("-> current_passwd: " + current_passwd);
    
    if (this.memberProc.isAdmin(session)) {
      // FastAPI 서버 URL (포트는 환경에 맞게 조정)
      String url = "http://localhost:8000/emotion";

      // HTTP 헤더 설정 (JSON)
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      // 요청 바디에 담을 데이터
      Map<String, Object> body = new HashMap<>();
      body.put("SpringBoot_FastAPI_KEY", new LLMKey().getSpringBoot_FastAPI_KEY());
      body.put("article", src.get("article"));

      // HttpEntity로 헤더 + 바디 묶기
      HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      // POST 요청 보내고, 결과를 String으로 받기
      String response = restTemplate.postForObject(url, requestEntity, String.class);
      System.out.println("-> response: " + response);
      
      return response;    
    } else {
      return "redirect:/member/login_cookie_need?url=/openai/emotion"; // redirect
    }   

  } 
  
  /**
   * 이미지 생성
   * @return
   */
  @GetMapping(value="/member_img")  
  public String member_img(HttpSession session, Model model) {
    if (this.memberProc.isAdmin(session)) {
      // model.addAttribute("cnt", 25);
      
      return "openai/member_img"; // /templates/openai/member_img.html  
    } else {
      return "redirect:/member/login_cookie_need?url=/openai/member_img"; // redirect
    }   
    
  }
  
  /**
   * 이미지 생성 처리
   * @return
   */
  @PostMapping(value="/member_img")
  @ResponseBody
  public String member_img_proc(HttpSession session, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"article":"요약할 문장..."}
    JSONObject src = new JSONObject(json_src); // String -> JSON
    // String current_passwd = (String)src.get("current_passwd"); // 값 가져오기
    // System.out.println("-> current_passwd: " + current_passwd);
    
    if (this.memberProc.isAdmin(session)) {
      // FastAPI 서버 URL (포트는 환경에 맞게 조정)
      String url = "http://localhost:8000/member_img";

      // HTTP 헤더 설정 (JSON)
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      // 요청 바디에 담을 데이터
      Map<String, Object> body = new HashMap<>();
      body.put("SpringBoot_FastAPI_KEY", new LLMKey().getSpringBoot_FastAPI_KEY());
      body.put("prompt", src.get("prompt"));

      // HttpEntity로 헤더 + 바디 묶기
      HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      // POST 요청 보내고, 결과를 String으로 받기
      String response = restTemplate.postForObject(url, requestEntity, String.class);
      System.out.println("-> response: " + response);
      // {"file_name": "C:/kd/deploy/resort/contents/storage/20250516185224_272.jpg"}
      
      return response;    
    } else {
      return "redirect:/member/login_cookie_need?url=/openai/member_img"; // redirect
    }   

  }
  
  /**
   * 꽃병 이미지 업로드
   * @return
   */
  @GetMapping(value="/flower")  
  public String flower(HttpSession session, Model model) {
    if (this.memberProc.isAdmin(session)) {
      // model.addAttribute("cnt", 25);
      
      return "openai/flower"; // /templates/openai/floawer.html  
    } else {
      return "redirect:/member/login_cookie_need?url=/openai/flower"; // redirect
    }   
    
  }
  

  /**
   * 꽃병 이미지 업로드 처리
   * @return
   */
  @PostMapping("/flower")
  @ResponseBody
  public String flower_proc(HttpSession session,
                            @RequestParam("file") MultipartFile file,
                            @RequestParam("prompt") String prompt) throws IOException {

      System.out.println("-> prompt: " + prompt);
      System.out.println("-> file name: " + file.getOriginalFilename());

      if (this.memberProc.isAdmin(session)) {
          String url = "http://localhost:8000/flower";

          // RestTemplate 설정
          HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.MULTIPART_FORM_DATA);

          // Multipart 바디 구성
          MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
          body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
          body.add("prompt", prompt);
          body.add("SpringBoot_FastAPI_KEY", new LLMKey().getSpringBoot_FastAPI_KEY());

          HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
          String response = restTemplate.postForObject(url, requestEntity, String.class);
          System.out.println("-> response: " + response);

          return response;
      } else {
          return "redirect:/member/login_cookie_need?url=/openai/flower";
      }
  }

  /**
   * Pizza 이미지 업로드
   * @return
   */
  @GetMapping(value="/pizza")  
  public String pizza(HttpSession session, Model model) {
    if (this.memberProc.isAdmin(session)) {
      // model.addAttribute("cnt", 25);
      
      return "openai/pizza"; // /templates/openai/pizza.html  
    } else {
      return "redirect:/member/login_cookie_need?url=/openai/pizza"; // redirect
    }   
    
  }
  

  /**
   * 꽃병 이미지 업로드 처리
   * @return
   */
  @PostMapping("/pizza")
  @ResponseBody
  public String pizza_proc(HttpSession session,
                            @RequestParam("file") MultipartFile file,
                            @RequestParam("prompt") String prompt) throws IOException {

      System.out.println("-> prompt: " + prompt);
      System.out.println("-> file name: " + file.getOriginalFilename());

      if (this.memberProc.isAdmin(session)) {
          String url = "http://localhost:8000/pizza";

          // RestTemplate 설정
          HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.MULTIPART_FORM_DATA);

          // Multipart 바디 구성
          MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
          body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
          body.add("prompt", prompt);
          body.add("SpringBoot_FastAPI_KEY", new LLMKey().getSpringBoot_FastAPI_KEY());

          HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
          String response = restTemplate.postForObject(url, requestEntity, String.class);
          System.out.println("-> response: " + response);

          return response;
      } else {
          return "redirect:/member/login_cookie_need?url=/openai/pizza";
      }
  }

}

