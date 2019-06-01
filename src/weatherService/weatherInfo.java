package weatherService;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class weatherInfo {

	public static String conditionIdDay(int id) {
		switch (id) {
		case 0:
			return "晴";
		case 1:
			return "多云";
		case 2:
			return "阴";
		case 3:
			return "阵雨";
		case 13:
			return "阵雪";
		case 18:
			return "雾";
		case 20:
			return "沙尘暴";
		case 29:
			return "扬沙";
		case 45:
			return "霾";
		case 4:
			return "雷阵雨";
		case 5:
			return "雷阵雨伴有冰雹";
		case 6:
			return "雨夹雪";
		case 7:
			return "小雨";
		case 8:
			return "中雨";
		case 9:
			return "大雨";
		case 10:
			return "暴雨";
		case 14:
			return "小雪";
		case 15:
			return "中雪";
		case 16:
			return "大雪";
		case 17:
			return "暴雪";
		case 19:
			return "冻雨";

		default:
			return null;
		}

	}

	public static String conditionIdNight(int id) {
		switch (id) {
		case 30:
			return "晴";
		case 31:
			return "多云";
		case 2:
			return "阴";
		case 33:
			return "阵雨";
		case 34:
			return "阵雪";
		case 32:
			return "雾";
		case 36:
			return "沙尘暴";
		case 35:
			return "扬沙";
		case 46:
			return "霾";
		case 4:
			return "雷阵雨";
		case 5:
			return "雷阵雨伴有冰雹";
		case 6:
			return "雨夹雪";
		case 7:
			return "小雨";
		case 8:
			return "中雨";
		case 9:
			return "大雨";
		case 10:
			return "暴雨";
		case 14:
			return "小雪";
		case 15:
			return "中雪";
		case 16:
			return "大雪";
		case 17:
			return "暴雪";
		case 19:
			return "冻雨";

		default:
			return null;
		}

	}

	public static String cityId(String proviceName,String cityName,String pacleName) {
		// 1、创建一个DocumentBuilderFactory的对象
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// 2、创建一个DocumentBuilder的对象
		try {
			// 创建DocumentBuilder对象
			DocumentBuilder db = dbf.newDocumentBuilder();
			// 3、通过DocumentBuilder对象的parser方法加载books.xml文件到当前项目下
			/* 注意导入Document对象时，要导入org.w3c.dom.Document包下的 */
			Document document = db.parse("src/weatherService/5937城市信息.xml");// 传入文件名可以是相对路径也可以是绝对路径
			// 获取所有book节点的集合
			NodeList bookList = document.getElementsByTagName("Row");
			// 通过nodelist的getLength()方法可以获取bookList的长度
//			System.out.println("一共有" + bookList.getLength() + "个城市信息");
			// 遍历每一个book节点
			for (int i = 0; i < bookList.getLength(); i++) {
//                System.out.println("=================下面开始遍历第" + (i + 1) + "个城市的内容=================");
				// ❤未知节点属性的个数和属性名时:
				// 通过 item(i)方法 获取一个book节点，nodelist的索引值从0开始
				Node book = bookList.item(i);
				// 获取book节点的所有属性集合
				NamedNodeMap attrs = book.getAttributes();
//                System.out.println("第 " + (i + 1) + "个城市共有" + attrs.getLength() + "个属性");
				// 遍历book的属性
				for (int j = 0; j < attrs.getLength(); j++) {
					// 通过item(index)方法获取book节点的某一个属性
					Node attr = attrs.item(j);
					// 获取属性名
					System.out.print("属性名：" + attr.getNodeName());
					// 获取属性值
					System.out.println("--属性值" + attr.getNodeValue());
				}
				// ❤已知book节点有且只有1个id属性:
				/*
				 * //前提：已经知道book节点有且只能有1个id属性 //将book节点进行强制类型转换，转换成Element类型 Element book1 =
				 * (Element) bookList.item(i); //通过getAttribute("id")方法获取属性值 String attrValue =
				 * book1.getAttribute("id"); System.out.println("id属性的属性值为" + attrValue);
				 */

				// 解析book节点的子节点
				NodeList childNodes = book.getChildNodes();
				// 遍历childNodes获取每个节点的节点名和节点值
//                System.out.println("第" + (i+1) + "个城市共有" + childNodes.getLength() + "个子节点");
				for (int k = 0; k < childNodes.getLength(); k++) {
					// 区分出text类型的node以及element类型的node
					if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
						// 获取了element类型节点的节点名
//                        System.out.print("第" + (k) + "个节点的节点名：" + childNodes.item(k).getNodeName());
						// 获取了element类型节点的节点
						Node childNodes2 = childNodes.item(k).getFirstChild();
//                        System.out.println("--节点值是：" + childNodes2.getTextContent());
						if (proviceName.equals(childNodes2.getTextContent())) {
//							System.out.println("找到:"+proviceName);
							if(cityName.equals(childNodes.item(k+2).getFirstChild().getTextContent())) {
//								System.out.println("找到:"+cityName);
								if(pacleName.equals(childNodes.item(k+4).getFirstChild().getTextContent())) {
//									System.out.println("找到:"+pacleName+"k为"+k);
									String chooseCity = childNodes.item(k-2).getFirstChild().getTextContent();
//									System.out.println(chooseCity);
//									System.out.println("城市ID为:"+chooseCity);
									
									return chooseCity;
								}
							}
							
						}

					}
				}
//                System.out.println("======================结束遍历第" + (i + 1) + "个城市的内容=================");
			}
			return null;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	

}
