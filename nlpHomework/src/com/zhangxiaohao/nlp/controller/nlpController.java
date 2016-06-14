package com.zhangxiaohao.nlp.controller;

import com.zhangxiaohao.nlp.service.ImailProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2016/6/11 0011.
 */
@Controller
public class nlpController {

    @Autowired
    ImailProcessingService mailProcessingService;

    public ImailProcessingService getMailProcessingService() {
        return mailProcessingService;
    }

    public void setMailProcessingService(ImailProcessingService mailProcessingService) {
        this.mailProcessingService = mailProcessingService;
    }

    @RequestMapping("/training")
    @ResponseBody
    public String training() {
        return mailProcessingService.getMail();
    }
}
