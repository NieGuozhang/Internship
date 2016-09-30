package com.hbut.internship.util;

import java.util.Random;

/*
 * 验证码生成器
 */
public class IdentifyingCode {

	private static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k',
			'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	private static IdentifyingCode bmpCode;

	public static IdentifyingCode getInstance() {
		if (bmpCode == null)
			bmpCode = new IdentifyingCode();
		return bmpCode;
	}

	// default settings
	private static final int DEFAULT_CODE_LENGTH = 5;

	// number of chars, lines; font size
	private int codeLength = DEFAULT_CODE_LENGTH;

	private Random random = new Random();

	// 验证码生成
	public String createCode() {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < codeLength; i++) {
			buffer.append(CHARS[random.nextInt(CHARS.length)]);
		}
		return buffer.toString();
	}
}
