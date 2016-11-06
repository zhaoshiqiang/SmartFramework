package org.smart4j.framework;

/**
 * 维护配置文件中相关的配置项名称
 * Created by zhaoshiqiang on 2016/11/5.
 */
public interface ConfigConstant {
    String CONFIG_FILE = "smart.properties";

    String JDBC_DRIVER = "smart.framework.jdbc.driver";
    String JDBC_URL = "smart.framework.jdbc.url";
    String JDBC_USERNAME = "smart.framework.jdbc.username";
    String JDBC_PASSWORD = "smart.framework.jdbc.password";
    //项目的基础包名
    String APP_BASE_PACKAGE = "smart.framework.app.base_package";
    //jsp的基础路径
    String APP_JSP_PATH = "smart.framework.app.jsp_path";
    //静态资源文件的基础路径
    String APP_ASSET_PATH = "smart.framework.app.asset_path";
}
