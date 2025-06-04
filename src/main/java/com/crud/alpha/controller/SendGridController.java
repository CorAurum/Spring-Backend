package com.crud.alpha.controller;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/public/SG")
public class SendGridController {

    @PostMapping
    public String sendEmail() {
        Email from = new Email("noreply@enbondi.xyz"); // your verified sender
        String subject = "El jijeador";
        Email to = new Email("falifali.g@hotmail.com");
        Content content = new Content("text/plain", "Me jijeo");
        Mail mail = new Mail(from, subject, to, content);
        mail.setReplyTo(new Email("fabriciogarcia20@gmail.com"));


        //codigo de prod
        //String api = System.getenv("SENDGRID_API");
        //if (api == null) {
         //   throw new IllegalStateException("SENDGRID_API environment variable not set.");
        //}
        //SendGrid sg = new SendGrid(api); // use the actual key from env

        SendGrid sg = new SendGrid("Poner Api key aca"); // better use an env var
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            return "Email sent! Status: " + response.getStatusCode();
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Failed to send email: " + ex.getMessage();
        }
    }
}