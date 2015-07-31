package jp.gr.java_conf.boj.app.regex;

import java.io.Serializable;
import java.util.List;

class MethodInfo implements Serializable {
	private String id;
	private String name;
	private String declaration;
	private List<String> paramList;
	private String returnType;
	private List<String> exceptionList;
	private String explain;
	
	public String getDeclaration() {
		return declaration;
	}
	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}
	public List<String> getExceptionList() {
		return exceptionList;
	}
	public void setExceptionList(List<String> exceptionList) {
		this.exceptionList = exceptionList;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getParamList() {
		return paramList;
	}
	public void setParamList(List<String> paramList) {
		this.paramList = paramList;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
}
