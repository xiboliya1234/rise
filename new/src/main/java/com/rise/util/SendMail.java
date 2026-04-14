package com.rise.util;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {

    // 发邮件账号
    public static String sendEmailAccount = "137987491@qq.com";
    // 发邮箱账号授权码
    public static String sendEmailPassword = "nuhvxwvqquqscaig";
    // 发邮箱账号主机
    public static String sendEmailSMTPHost = "smtp.qq.com";
    // 发邮箱协议
    public static String sendEmailProtocol = "smtp";
    // 邮件的标题
    public static String sendEmailSubject = "测试";
    // 邮件的内容
    public static String sendEmailContext = "测试";
    // 收邮箱账号
    public static String receiveMailAccount = "137987491@qq.com";

    public static void main(String[] args) {
        // 配置参数
        Properties prop = new Properties();
        // 发件人的邮箱的SMTP 服务器地址（不同的邮箱，服务器地址不同，如139和qq的邮箱服务器地址不同）
        prop.setProperty("mail.host", sendEmailSMTPHost);
        // 使用的协议（JavaMail规范要求）
        prop.setProperty("mail.transport.protocol", sendEmailProtocol);
        // 需要请求认证
        prop.setProperty("mail.smtp.auth", "true");

        // 使用JavaMail发送邮件的5个步骤
        // 1 创建session
        Session session = Session.getInstance(prop);
        // 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);
        Transport ts = null;
        try {
            // 2 通过session得到transport对象
            ts = session.getTransport();
            // 3 使用账户和授权码
            ts.connect(sendEmailSMTPHost, sendEmailAccount, sendEmailPassword);
            // 4 创建邮件
            Message message = createSimpleMail(session);
            // 5 发送邮件
            ts.sendMessage(message, message.getAllRecipients());
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭transport对象
                ts.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建只包含文本的邮件
     *
     * @param session
     * @return
     * @throws MessagingException
     */
    public static MimeMessage createSimpleMail(Session session)
            throws MessagingException {
        // 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        // 指明发件人
        message.setFrom(new InternetAddress(sendEmailAccount));
        // 指明收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveMailAccount));
        // 可选 添加收件人
//        message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiveMailAccount));
        //    Cc: 抄送（可选）
//        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(receiveMailAccount));
        //    Bcc: 密送（可选）
//        message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(receiveMailAccount));

        // 邮件的标题
        message.setSubject(sendEmailSubject,"UTF-8");
        // 邮件的文本内容
        message.setContent(sendEmailContext, "text/html;charset=UTF-8");

        // 设置显示发件时间
        message.setSentDate(new Date());

        return message;
    }

    /**
     * 创建包含文本和附件的邮件
     *
     * @param session
     * @return
     * @throws MessagingException
     */
    public static MimeMessage createFileMail(Session session)
            throws MessagingException {
        // 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        // 指明发件人
        message.setFrom(new InternetAddress(sendEmailAccount));
        // 指明收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveMailAccount));
        // 可选 添加收件人
//        message.addRecipient(Message.RecipientType.TO, new InternetAddress("1499767535@qq.com"));
        //    Cc: 抄送（可选）
//        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(receiveMailAccount));
        //    Bcc: 密送（可选）
//        message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(receiveMailAccount));


        // 邮件的标题
        message.setSubject(sendEmailSubject,"UTF-8");
        // 邮件的文本内容
//        message.setContent(sendEmailContext, "text/html;charset=UTF-8");

        // 设置显示发件时间
        message.setSentDate(new Date());


        // 创建消息部分
        BodyPart messageBodyPart = new MimeBodyPart();
        // 消息
        messageBodyPart.setText("This is message body");

        // 创建多重消息
        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // 附件部分
        messageBodyPart = new MimeBodyPart();
        String filePath = "C:\\Users\\13798\\Desktop\\邮件附件\\测试.txt";
        String fileName = "测试.txt";
        DataSource source = new FileDataSource(filePath);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);
        multipart.addBodyPart(messageBodyPart);

        // 发送完整消息
        message.setContent(multipart);

        return message;
    }}
