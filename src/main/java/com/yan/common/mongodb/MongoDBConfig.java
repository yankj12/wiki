package com.yan.common.mongodb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

//@Configuration注解的类必需使用<context:component-scanbase-package="XXX"/>扫描
@Configuration
public class MongoDBConfig {

	@Value("${mongodb.ip}")
	private String ip;
	
	@Value("${mongodb.port}")
	private Integer port;
	
	@Value("${mongodb.database}")
	private String database;
	
	@Value("${mongodb.dbUserDefined}")
	private String dbUserDefined;
	
	@Value("${mongodb.user}")
	private String user;
	
	@Value("${mongodb.password}")
	private String password;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDbUserDefined() {
		return dbUserDefined;
	}
	public void setDbUserDefined(String dbUserDefined) {
		this.dbUserDefined = dbUserDefined;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
}
