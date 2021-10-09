package com.todo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.*;
import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;
public class TodoUtil {

	public static void createItem(TodoList list) {

		String title, desc, category, due_date;
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in)); //선언

		System.out.println("------일정 추가------");
		
		try {
			System.out.print("[제목] ");
			title = bf.readLine();
			if (list.isDuplicate(title)) {
				System.out.printf("제목은 중복될 수 없습니다 ㅠ\n");
				return;
			}

			System.out.print("[설명] ");
			desc = bf.readLine();
			
			System.out.print("[카테고리] ");
			category = bf.readLine();
			
			System.out.print("[마감 일자] ");
			due_date = bf.readLine();

			TodoItem t = new TodoItem(title, desc, category, due_date);
			list.addItem(t);
			
			System.out.println("\" <"+title+"> "+desc+" \"이(가) 저장되었습니다 :)");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void deleteItem(TodoList l) {

		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in)); //선언
		Scanner sc = new Scanner(System.in); // Scanner 객체 생성
		
		System.out.println("------일정 삭제------");

		int del_num;
		try {
			List<TodoItem> list = l.getList();
			System.out.println("삭제할 일정의 번호를 입력하세요");
			System.out.print("[번호] ");
			del_num = Integer.parseInt(bf.readLine());
			if(del_num < 1 || del_num > list.size()) {
				System.out.println("존재하지 않는 일정입니다.");
			}
			else {
				TodoItem item = list.get(del_num-1);
				System.out.println("[" + item.getCategory() + "] " + del_num + ". <"+ item.getTitle() + "> " + item.getDesc() + " (마감 : " + item.getDue_date() + ") - " + item.getCurrent_date());
				System.out.print("위 항목을 삭제하시겠습니까? (y/n) > ");
				String yes_or_no = bf.readLine();
				if (yes_or_no.equals("y") || yes_or_no.equals("Y")) {
					l.deleteItem(item);
					System.out.println("삭제되었습니다.");
				}
				if (yes_or_no.equals("n") || yes_or_no.equals("N")) {
					System.out.println("삭제가 취소되었습니다.");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void updateItem(TodoList l) {

		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in)); //선언

		System.out.println("------일정 수정------");
		String title;
		try {
			List<TodoItem> list = l.getList();
			System.out.println("수정할 일정의 번호를 입력하세요");
			System.out.print("[번호] ");
			int edit_num = Integer.parseInt(bf.readLine());
			if(edit_num < 1 || edit_num > list.size()) { 
				System.out.println("존재하지 않는 일정입니다.");
			}
			else {
				TodoItem item = list.get(edit_num-1);
				System.out.println("[" + item.getCategory() + "] " + edit_num + ". <"+ item.getTitle() + "> " + item.getDesc() + " (마감 : " + item.getDue_date() + ") - " + item.getCurrent_date());
				
				System.out.print("[변경할 제목] ");
				String new_title = bf.readLine().trim();
				if (l.isDuplicate(new_title)) {
					System.out.printf("제목은 중복될 수 없습니다 ㅠ\n");
					return;
				}
				System.out.print("[변경할 설명] ");
				String new_description = bf.readLine().trim();
				
				System.out.print("[변경할 카테고리] ");
				String new_category = bf.readLine().trim();
				
				System.out.print("[변경할 마감 일자] ");
				String new_due_date = bf.readLine().trim();
				
				l.deleteItem(item);
				TodoItem t = new TodoItem(new_title, new_description, new_category, new_due_date);
				l.addItem(t);
				System.out.println("해당 일정이 \" <"+new_title+"> "+new_description+" \"로 변경되었습니다 :)");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void listAll(TodoList l) {
		int count = 0;
		System.out.println("------전체 목록------");
		System.out.println("[총 "+l.getList().size()+"개의 일정]");
		for (TodoItem item : l.getList()) {
			count ++;
			System.out.println("[" + item.getCategory() + "] " + count + ". <"+ item.getTitle() + "> " + item.getDesc() + " (마감 : " + item.getDue_date() + ") - " + item.getCurrent_date());
		}
	}
	
	public static void saveList(TodoList l, String filename) {
		try {
			String filePath = Paths.get(".").toAbsolutePath().toString() +"/"+ filename;
			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			for(TodoItem item : l.getList()) {
				bw.write(item.toSaveString());
                bw.flush();
			}
            bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadList(TodoList l, String filename) throws FileNotFoundException {
		
		try {
			String filePath = Paths.get(".").toAbsolutePath().toString() +"/"+ filename;
			File file = new File(filePath);
			
			if(file.exists()) {
	            BufferedReader br = new BufferedReader(new FileReader(file));

	            String line = "";
	            while ((line = br.readLine()) != null) {
	                StringTokenizer st = new StringTokenizer(line, "##");
	                String category = st.nextToken().trim();
	                String title = st.nextToken().trim();
	                String desc = st.nextToken().trim();
	                String due_date = st.nextToken().trim();
	                String current_date = st.nextToken().trim();

	                l.addItem(new TodoItem(title,desc,current_date,category,due_date));
	            }
	            System.out.println("파일 로딩 완료!");
	            br.close();
	        }
	        else {
	            System.out.println("파일 없음");
	        }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e){
			e.printStackTrace();
        } 
	}
	
	public static void findKeyword (TodoList l, String keyword) {
		int count = 0;
		int find_count = 0;
		System.out.println("------검색 결과------");
		for(TodoItem item : l.getList()) {
			count ++;
			if(item.getTitle().contains(keyword) || item.getDesc().contains(keyword)) {
				System.out.println("[" + item.getCategory() + "] " + count + ". <"+ item.getTitle() + "> " + item.getDesc() + " (마감 : " + item.getDue_date() + ") - " + item.getCurrent_date());
				find_count++;
			}
		}
		System.out.println("총 "+find_count+"개의 일정을 찾았습니다.");
	}
	
	public static void findCateKeyword (TodoList l, String keyword) {
		int count = 0;
		int find_count = 0;
		System.out.println("------검색 결과------");
		for(TodoItem item : l.getList()) {
			count ++;
			if(item.getCategory().contains(keyword)) {
				System.out.println("[" + item.getCategory() + "] " + count + ". <"+ item.getTitle() + "> " + item.getDesc() + " (마감 : " + item.getDue_date() + ") - " + item.getCurrent_date());
				find_count++;
			}
		}
		System.out.println("총 "+find_count+"개의 일정을 찾았습니다.");
	}
	
	public static void listCate (TodoList l) {
		int count = 0;
		int find_count = 0;
		HashSet<String> set = new HashSet<String>(); //HashSet생성
		for(TodoItem item : l.getList()) {
			set.add(item.getCategory());
		}
		
		Iterator iter = set.iterator();	// Iterator 사용
		while(iter.hasNext()) {//값이 있으면 true 없으면 false
		    System.out.println("["+iter.next()+"]");
		}
		System.out.println("총 "+set.size()+"개의 카테고리가 등록되어 있습니다.");
	}
}
