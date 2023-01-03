package com.pzhu.acp.email;

import com.pzhu.acp.constant.RedisConstant;
import com.pzhu.acp.constant.UserConstant;
import com.pzhu.acp.utils.GsonUtil;
import com.pzhu.acp.utils.VerCodeGenerateUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: gali
 * @Date: 2022-12-22 22:21
 * @Description:
 */
@Component
public class MailProducer {
    @Resource
    private JavaMailSender mailSender;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void sendEmail(String email) {
        //创建邮件
        SimpleMailMessage message = new SimpleMailMessage();
        //设置发件人信息
        message.setFrom(UserConstant.EMAIL_DEFAULT_ADDRESS);
        message.setTo(email);
        message.setSubject("咖喱欢迎您来到该平台");
        //生成六位随机验证码
        String verCode = VerCodeGenerateUtil.generateVerCode();
        //加入缓存中
        String key = String.format(RedisConstant.EMAIL_CODE, email);
        redisTemplate.opsForValue().set(key, GsonUtil.toJson(verCode), 60 * 5, TimeUnit.SECONDS);
        message.setText("亲爱的同学,您好:\n"
                + "\n本次的邮件验证码为:" + verCode + ",本验证码 5 分钟内效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封通过自动发送的邮件，请不要直接回复）\n"
                + "\n 本网站由咖喱独自完成，欢迎各位大佬点个star♥！！");
        mailSender.send(message);
    }
}
