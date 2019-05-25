package com.Servlet;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;




import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtil {
	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return map
	 * @throws Exception
	 */
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap();
		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		System.out.println("获取输入流");
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList) {
			System.out.println(e.getName() + "|" + e.getText());
			map.put(e.getName(), e.getText());
		}

		// 释放资源
		inputStream.close();
		inputStream = null;
		return map;
	}

	/**
	 * 根据消息类型 构造返回消息
	 */
	public static String buildXml(Map<String, String> map) {

		String result;
		String msgType = map.get("MsgType").toString();
		System.out.println("MsgType:" + msgType);
		if (msgType.toUpperCase().equals("TEXT")) {
			String Content = map.get("Content").toString();
//			System.out.println("用户消息:"+Content);
			if (Content.equals("音乐")) {
				result = buildMusicMessage(map);
			} else if (Content.equals("图文")) {
				result = buildNewsMessage(map);
			}

			else if (Content.contains("天气") && !"".equals(Content)) {
				if (Content.contains(":")) {
					String cityName = Content.substring(Content.lastIndexOf(":") + 1, Content.length());
					String info = "Sorry，该功能我们的程序员爸爸正在调试，暂不开放哦！";
					result = buildTextMessage(map, info);
//                    
//                    result = buildTextMessage(map,weatherInfo);
				} else {
					String notice = "查询天气的正确姿势: 天气:城市名\n请客官输入正确的格式哟~";
					result = buildTextMessage(map, notice);
				}

			}

			else {
				result = buildTextMessage(map, "Hey,Siri为您服务, 请问客官想要点啥?");
			}

		} else if (msgType.toUpperCase().equals("IMAGE")) {
			result = buildImageMessage(map, "/Users/zxg/Downloads/timg.jpeg");

		}

		else if (msgType.toUpperCase().equals("VIDEO")) {
			result = buildVideoMessage(map);

		}

		else if (msgType.toUpperCase().equals("VOICE")) {
			result = buildVoiceMessage(map);

		}

		else {
			String fromUserName = map.get("FromUserName");
			// 开发者微信号
			String toUserName = map.get("ToUserName");

			result = String.format(
					"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
							+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>"
							+ "<Content><![CDATA[%s]]></Content>" + "</xml>",
					fromUserName, toUserName, getUtcTime(), "请回复如下关键词：\n文本\n图片\n语音\n视频\n音乐\n图文");
		}

		return result;
	}

	/**
	 * 构造文本消息
	 *
	 * @param map
	 * @param content
	 * @return
	 */
	private static String buildTextMessage(Map<String, String> map, String content) {
		// 发送方帐号
		String fromUserName = map.get("FromUserName");
		// 开发者微信号
		String toUserName = map.get("ToUserName");
		/**
		 * 文本消息XML数据格式
		 */
		return String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>"
						+ "<Content><![CDATA[%s]]></Content>" + "</xml>",
				fromUserName, toUserName, getUtcTime(), content);
	}

	private static String getUtcTime() {
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat df = new SimpleDateFormat("yyyyMMddhhmm");// 设置显示格式
		String nowTime = df.format(dt);
		long dd = (long) 0;
		try {
			dd = df.parse(nowTime).getTime();
		} catch (Exception e) {

		}
		return String.valueOf(dd);
	}

	/**
	 * 构建图片消息
	 * 
	 * @param map
	 * @param picUrl
	 * @return
	 */
	private static String buildImageMessage(Map<String, String> map, String picUrl) {
		String fromUserName = map.get("FromUserName");
		String toUserName = map.get("ToUserName");
		/* 返回指定的图片(该图片是上传为素材的,获得其media_id) */
		// String media_id =
		// "UCWXNCogK5ub6YFFQf7QcEpvDIYLf3Zh0L5W9i4aEp2ehfnTrASeV59x3LMD88SS";

		/* 返回用户发过来的图片 */
		String media_id = map.get("MediaId");
		System.out.println("media_id:" + media_id);
		return String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[image]]></MsgType>" + "<Image>"
						+ "   <MediaId><![CDATA[%s]]></MediaId>" + "</Image>" + "</xml>",
				fromUserName, toUserName, getUtcTime(), media_id);
	}

	/**
	 * 构造语音消息
	 * 
	 * @param map
	 * @return
	 */
	private static String buildVoiceMessage(Map<String, String> map) {
		String fromUserName = map.get("FromUserName");
		String toUserName = map.get("ToUserName");
		/* 返回用户发过来的语音 */
		String media_id = map.get("MediaId");
		return String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[voice]]></MsgType>" + "<Voice>"
						+ "   <MediaId><![CDATA[%s]]></MediaId>" + "</Voice>" + "</xml>",
				fromUserName, toUserName, getUtcTime(), media_id);
	}

	/**
	 * 回复视频消息
	 * 
	 * @param map
	 * @return
	 */
	private static String buildVideoMessage(Map<String, String> map) {
		String fromUserName = map.get("FromUserName");
		String toUserName = map.get("ToUserName");
		String title = "客官发过来的视频哟~~";
		String description = "客官您呐,现在肯定很开心,对不啦 嘻嘻?";
		/* 返回用户发过来的视频 */
//        String media_id = map.get("MediaId");
		String media_id = "BoLeFtIaJIz3f-gy3sJxWzZwjdlKfuuCbhAk2G_Zd7xBJOQXvb0xd20zo-Dpt9yf";
		return String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[video]]></MsgType>" + "<Video>"
						+ "   <MediaId><![CDATA[%s]]></MediaId>" + "   <Title><![CDATA[%s]]></Title>"
						+ "   <Description><![CDATA[%s]]></Description>" + "</Video>" + "</xml>",
				fromUserName, toUserName, getUtcTime(), media_id, title, description);
	}

	/**
	 * 回复音乐消息
	 * 
	 * @param map
	 * @return
	 */
	private static String buildMusicMessage(Map<String, String> map) {
		String fromUserName = map.get("FromUserName");
		String toUserName = map.get("ToUserName");
		String title = "多想在平庸的生活拥抱你";
		String description = "多听音乐 心情棒棒 嘻嘻?";

		String hqMusicUrl = " https://music.163.com/#/song?id=1346104327";

		return String.format("<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>"
				+ "<FromUserName><![CDATA[%s]]></FromUserName>" + "<CreateTime>%s</CreateTime>"
				+ "<MsgType><![CDATA[music]]></MsgType>" + "<Music>" + "   <Title><![CDATA[%s]]></Title>"
				+ "   <Description><![CDATA[%s]]></Description>" + "   <MusicUrl>< ![CDATA[%s] ]></MusicUrl>" + // 非必须项
																												// 音乐链接
				"   <HQMusicUrl><![CDATA[%s]]></HQMusicUrl>" + // 非必须项 高质量音乐链接，WIFI环境优先使用该链接播放音乐
				"</Music>" + "</xml>", fromUserName, toUserName, getUtcTime(), title, description, hqMusicUrl,
				hqMusicUrl);
	}

	/**
	 * 返回图文消息
	 * 
	 * @param map
	 * @return
	 */
	private static String buildNewsMessage(Map<String, String> map) {
		String fromUserName = map.get("FromUserName");
		String toUserName = map.get("ToUserName");
		String title1 = "全球首例：四川卧龙国家级自然保护区拍摄到白色大熊猫";
		String description1 = "野外红外触发相机拍摄到的白色大熊猫影像。四川卧龙国家级自然保护区管理局供图";
		String picUrl1 = "http://pics2.baidu.com/feed/adaf2edda3cc7cd90ddc5b16866bc13bba0e91ff.jpeg?token=bfd72e56df71a79603c26f7e6b7ae8c3&s=85032EF3DCF5EF866F9E3F6303003057";
		String textUrl1 = "http://baijiahao.baidu.com/s?id=1634490736723977520";

		String title2 = "阿里技术狂人蔡景现：一个人顶一个团队，一个月搭起淘宝";
		String description2 = "蔡景现，阿里云高级研究员。2000年加入阿里巴巴；2003年成为淘宝网初创团队的三名工程师之一，和另外两位工程师一起从零开始，在一个月内，搭起了这个名叫“淘宝”的网站，并涵盖所有交易系统和论坛系统；曾有一段时间，独自一人维护淘宝搜索引擎，并且这还不是他全部的工作；2014年，蔡景现被邀约成为阿里巴巴合伙人。";
		String picUrl2 = "http://pics3.baidu.com/feed/cefc1e178a82b90109e48188198e76733812efcc.jpeg?token=3e883065e340983d4bef566df4df82a8&s=350349B54C370F821694B9A10300F011";
		String textUrl2 = "http://baijiahao.baidu.com/s?id=1634391965543319638";

		return String.format("<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>"
				+ "<FromUserName><![CDATA[%s]]></FromUserName>" + "<CreateTime>%s</CreateTime>"
				+ "<MsgType><![CDATA[news]]></MsgType>" + "<ArticleCount>2</ArticleCount>" + // 图文消息个数，限制为8条以内
				"<Articles>" + // 多条图文消息信息，默认第一个item为大图,注意，如果图文数超过8，则将会无响应
				"<item>" + "<Title><![CDATA[%s]]></Title> " + "<Description><![CDATA[%s]]></Description>"
				+ "<PicUrl><![CDATA[%s]]></PicUrl>" + // 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
				"<Url><![CDATA[%s]]></Url>" + // 点击图文消息跳转链接
				"</item>" + "<item>" + "<Title><![CDATA[%s]]></Title>" + "<Description><![CDATA[%s]]></Description>"
				+ "<PicUrl><![CDATA[%s]]]></PicUrl>" + "<Url><![CDATA[%s]]]></Url>" + "</item>" + "</Articles>"
				+ "</xml>", fromUserName, toUserName, getUtcTime(), title1, description1, picUrl1, textUrl1, title2,
				description2, picUrl2, textUrl2);
	}

}
