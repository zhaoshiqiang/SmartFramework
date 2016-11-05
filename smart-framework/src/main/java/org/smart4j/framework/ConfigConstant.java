package org.smart4j.framework;

/**
 * �ṩ�������
 * Created by zhaoshiqiang on 2016/11/5.
 */
public interface ConfigConstant {
    String CONFIG_FILE = "smart.properties";

    String JDBC_DRIVER = "smart.framework.jdbc.driver";
    String JDBC_URL = "smart.framework.jdbc.url";
    String JDBC_USERNAME = "smart.framework.jdbc.username";
    String JDBC_PASSWORD = "smart.framework.jdbc.password";
    //��ʾ��Ŀ�Ļ�������
    String APP_BASE_PACKAGE = "smart.framework.app.base_package";
    //��ʾjsp�Ļ���·��
    String APP_JSP_PATH = "smart.framework.app.jsp_path";
    //��ʾ��̬��Դ�ļ��Ļ���·��������JS��CSS��ͼƬ��
    String APP_ASSET_PATH = "smart.framework.app.asset_path";
}
