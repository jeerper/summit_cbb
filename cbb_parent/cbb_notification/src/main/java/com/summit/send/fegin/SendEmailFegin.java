package com.summit.send.fegin;

import com.summit.send.pojo.SendEmail;
import com.summit.send.service.SendEmailService;
/*import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;*/

import javax.mail.MessagingException;

//@FeignClient(value = "cbb-notification")
public interface SendEmailFegin extends SendEmailService {

    /*@Override
    @RequestMapping(value = "/sendMail",method = RequestMethod.POST)
    void sendMail(@RequestBody SendEmail sendEmail);*/

}
