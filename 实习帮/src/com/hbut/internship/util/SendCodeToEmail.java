package com.hbut.internship.util;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.content.Intent;

/*
 * 向邮箱发送验证码。获取验证码。
 */
public class SendCodeToEmail {

	public static void sendEmail(Intent intent, String Code) throws Exception {
		String email = intent.getStringExtra("email");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.126.com");
		props.put("mail.smtp.auth", "true");
		PopupAuthenticator auth = new PopupAuthenticator();
		Session session = Session.getInstance(props, auth);
		MimeMessage message = new MimeMessage(session);

		Address addressFrom = new InternetAddress("nie_gz@126.com");
		Address addressTo = new InternetAddress(email);
		Address addressCopy = new InternetAddress("nie_gz@126.com");

		message.setContent("验证码如下：" + "\n" + Code + "\n"
				+ "请尽快将验证码输入客户端进行修改密码。", "text/plain");// 或者使用message.setText("Hello");
		message.setSubject("“实习帮”找回密码");
		message.setFrom(addressFrom);
		message.addRecipient(Message.RecipientType.TO, addressTo);
		message.addRecipient(Message.RecipientType.CC, addressCopy);
		message.saveChanges();

		Transport transport = session.getTransport("smtp");
		transport.connect("smtp.126.com", "nie_gz@126.com", "ngz19950324");
		transport.send(message);
		transport.close();
	}
}

// 邮箱验证
class PopupAuthenticator extends Authenticator {

	public PasswordAuthentication getPasswordAuthentication() {
		String username = "nie_gz@126.com"; // 126邮箱登录帐号
		String pwd = "ngz19950324"; // 登录密码
		return new PasswordAuthentication(username, pwd);
	}
}
