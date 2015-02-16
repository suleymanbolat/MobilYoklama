package com.suleymanaybuke.mobilyoklama.object;

import java.io.Serializable;
import java.util.List;

public class SerializableListClass implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 4532366131480062438L;
private List<Student> studentList;
	
	public List<Student> getStudentList() {
		return studentList;
	}
	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

}
