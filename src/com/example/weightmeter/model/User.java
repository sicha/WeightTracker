package com.example.weightmeter.model;

import java.util.ArrayList;

import android.app.Application;

public class User extends Application {
	
	private String name;
	private String sex;
	private Integer age;
	private ArrayList<MeasuredWeight> measures;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public ArrayList<MeasuredWeight> getMeasures() {
		return measures;
	}
	public void setMeasures(ArrayList<MeasuredWeight> measures) {
		this.measures = measures;
	}

}
