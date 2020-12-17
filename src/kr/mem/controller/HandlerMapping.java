package kr.mem.controller;

import java.util.HashMap;

import kr.mem.pojo.Controller;
import kr.mem.pojo.*;

public class HandlerMapping {
	//		Key(/list.do), Value(MemberListController)
	private HashMap<String, Controller> mappings;
	public HandlerMapping() {
		mappings = new HashMap<String, Controller>();
		initMap();
	}

	// ★★★★★★★★  MVC STEP2★★★★★★★★ 클라인트의 요청받아서 어디로 연결해줄지 ★★★★★★★★
	private void initMap() {
		try {
			mappings.put("/list.do", new MemberListController()); //객체기 때문에 new로 객체생성 후 넣어야 함
			mappings.put("/insert.do", new MemberInsertController()); 
			mappings.put("/insertForm.do", new MemberInsertFormController()); 
			mappings.put("/delete.do", new MemberDeleteController()); 
			mappings.put("/content.do", new MemberContentController()); 
			mappings.put("/update.do", new MemberUpdateController());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Controller getController(String key) {
		return mappings.get(key);
	}


}
