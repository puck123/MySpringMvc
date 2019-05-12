package com.ruanmou.vip.myspringmvc.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    今日内容:IOC和DI
 * </pre>
 * XMl文件的解析类型
 * @author gerry
 * @date 2018-07-03
 */
public class XMLConfigurationParser {
    public static String readXMLBasePackage(String configPath) throws Exception {
        SAXReader reader = new SAXReader();
        InputStream inputStream = XMLConfigurationParser.class.getClassLoader().getResourceAsStream(configPath);
        // 获取XML文档对象
        Document document = reader.read(inputStream);
        inputStream.close();
        // 获取配置文件的根元素
        Element rootElement = document.getRootElement();
        // 获取到component-scan元素
        Element scanElement = rootElement.element("component-scan");
        // 获取base-package配置的属性值
        String bsePackageStirng = scanElement.attributeValue("base-package");

        return bsePackageStirng;
    }

    public static void main(String[] args) throws Exception {
        String s = readXMLBasePackage("mvc/applicationContext.xml");
        System.out.println(s);
    }
}
