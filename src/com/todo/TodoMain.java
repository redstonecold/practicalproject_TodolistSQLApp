package com.todo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.w3c.dom.ls.LSOutput;

import com.todo.dao.TodoList;
import com.todo.menu.Menu;
import com.todo.service.TodoUtil;

public class TodoMain {
	
	public static void start() {
		
		try {

			Scanner sc = new Scanner(System.in);
			TodoList l = new TodoList();
			boolean isList = false;
			boolean quit = false;
			String keyword;
			String filename = "todolist.txt";
			
			System.out.println(filename + "을(를) 로드합니다.");
			TodoUtil.loadList(l, filename);
			do {
				Menu.prompt();
				isList = false;
				String choice = sc.next();
//				System.out.println(choice);
				switch (choice) {

				case "add":
					TodoUtil.createItem(l);
					break;
				
				case "del":
					TodoUtil.deleteItem(l);
					break;
					
				case "edit":
					TodoUtil.updateItem(l);
					break;
				
				case "help" :
					Menu.displaymenu();
					break;
					
				case "ls":
					TodoUtil.listAll(l);
					break;

				case "ls_name_asc":
					l.sortByName();
					System.out.println("제목순으로 정렬하였습니다.");
					isList = true;
					break;

				case "ls_name_desc":
					l.sortByName();
					l.reverseList();
					System.out.println("제목역순으로 정렬하였습니다.");
					isList = true;
					break;
					
				case "ls_date":
					l.sortByDate();
					System.out.println("날짜순으로 정렬하였습니다.");
					isList = true;
					break;
				
				case "ls_date_desc":
					l.sortByDate();
					l.reverseList();
					System.out.println("날짜역순으로 정렬하였습니다.");
					isList = true;
					break;
				
				case "ls_cate" :
					TodoUtil.listCate(l);
					break;
				
				case "find" :
					keyword = sc.next().trim();
//					keyword = sc.nextLine().trim();
					TodoUtil.findKeyword(l,keyword);
					break;
				
				case "find_cate" :
					keyword = sc.next().trim();
//					keyword = sc.nextLine().trim();
					TodoUtil.findCateKeyword(l, keyword);
					break;

				case "exit":
					quit = true;
					break;

				default:
					System.out.println("정확한 명령어를 입력하세요. (명령어 보기 - help)");
					break;
				}
				
				if(isList) l.listAll();
			} while (!quit);
			
			TodoUtil.saveList(l, filename);
			System.out.println("목록이 " + filename + "에 저장되었습니다.");
			System.out.println("TodoList가 종료되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
