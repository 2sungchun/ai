package dev.mvc.resort_v1sbm3c;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/tag")
@Controller
public class HTMLCont {
  public HTMLCont() {
    System.out.println("-> HTMLCont created.");
  }
  
  /**
   * radio 태그 초기값 설정, http://localhost:9091/tag/init
   * @param model
   * @return
   */
  @GetMapping("/init")
  public String showForm(Model model) {
    // radio
    String radio_value = "Y";
    model.addAttribute("radio_value", radio_value);
    
    // checkbox
    boolean isCode1 = true;
    boolean isCode2 = false;
    boolean isCode3 = true;
    
    model.addAttribute("isCode1", isCode1);
    model.addAttribute("isCode2", isCode2);
    model.addAttribute("isCode3", isCode3);
    
    String isCode4 = "서울";
    String isCode5 = "인천";
    String isCode6 = "";
    
    model.addAttribute("isCode4", isCode4);
    model.addAttribute("isCode5", isCode5);
    model.addAttribute("isCode6", isCode6);
    
    // selectbox
    String select_value = "A02"; // 인천
    model.addAttribute("select_value", select_value);
    
    // date
    String rdate = "2025-03-26 12:25:30".substring(0, 10);
    model.addAttribute("rdate", rdate);
        
    return "/tag/init"; // /templates/tag/init.html
  }
  
  // @RequestParam(name = "cateno", defaultValue = "0")
  @PostMapping("/init")
  @ResponseBody
  public String processForm(Model model, 
                                       @RequestParam(name = "radio_value", defaultValue = "N") String radio_value,
                                       @RequestParam(name = "code1", defaultValue = "") String code1,
                                       @RequestParam(name = "code2", defaultValue = "") String code2,
                                       @RequestParam(name = "code3", defaultValue = "") String code3,
                                       @RequestParam(name = "code4", defaultValue = "") String code4,
                                       @RequestParam(name = "code5", defaultValue = "") String code5,
                                       @RequestParam(name = "code6", defaultValue = "") String code6,
                                       @RequestParam(name = "area", defaultValue = "") String area,
                                       @RequestParam(name = "rdate", defaultValue = "") String rdate
                                       ) {
    StringBuffer sb = new StringBuffer();
    sb.append("<h2>radio_value: "+radio_value+"</h2>");
    sb.append("<h2>code1: "+code1+"</h2>");
    sb.append("<h2>code2: "+code2+"</h2>");
    sb.append("<h2>code3: "+code3+"</h2>");
    sb.append("<h2>code4: "+code4+"</h2>");
    sb.append("<h2>code5: "+code5+"</h2>");
    sb.append("<h2>code6: "+code6+"</h2>");
    sb.append("<h2>area: "+area+"</h2>");
    sb.append("<h2>rdate: "+rdate+"</h2>");
    
    return sb.toString();
  }
  
}
