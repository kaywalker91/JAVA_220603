package study0603_2;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Study3_FriendsList {

	public static void main(String[] args) {
		Friends3 f = new Friends3();
	}
}

class Friends3 extends Frame implements ActionListener {
	//디비 접속 정보 
	Connection conn = null;
	String url = "jdbc:mysql://127.0.0.1:3306/study?useUnicode=true&characterEncoding=utf8";
	String id = "root";// ID
	String pass = "qwer";// 비밀번호	
	
	//디비 데이타 삽입용
	PreparedStatement pstmt = null;	
		
	//디비 조회용
	Statement stmt = null;
	ResultSet rs = null;
	
	
	String result="";//텍스트에이리어에 누적해서 한번에 출력하기위해서 
			
	
	
	
	//윈도우 중앙배치를 위한 변수 
	private Dimension dimen, dimen1;
	private int xpos, ypos;
	
	Button btnAdd = new Button("등록");	
	Button btnSearch = new Button("검색");
	Button btnEdit = new Button("수정");
	Button btnDel = new Button("삭제");
	Label lbTitle = new Label("회원관리 윈도우버전3");	
	Label lbName = new Label("이 름:");
	Label lbHp = new Label("전 번  :");
	Label lbAddr = new Label("주 소 : ");
	Label lbMemo = new Label("메 모");
	Label lbList = new Label("친구 리스트");
	TextField tfName  = new TextField(20);	
	TextField tfHp  = new TextField(20);
	TextField tfAddr  = new TextField(20);
	TextField tfMemo  = new TextField(20);
	
	TextArea taList = new TextArea();
	
	
	public Friends3() {
		super("친구관리하기3");
		this.init();
		this.start();		
		this.setSize(420, 600);	
		dimen = Toolkit.getDefaultToolkit().getScreenSize();
		dimen1 = this.getSize();
		xpos = (int) (dimen.getWidth() / 2 - dimen1.getWidth() / 2);
		ypos = (int) (dimen.getHeight() / 2 - dimen1.getHeight() / 2);
		this.setLocation(xpos, ypos);
		this.setVisible(true);
		
		
		
		
		
		
	}

	public void init() {
		// 앱솔루트방식  : 직접 좌표 입력방식.
		this.setLayout(null);

		
		Font font20 = new Font("SansSerif", Font.BOLD, 20);
		Font font15 = new Font("SansSerif", Font.BOLD, 15);
		Font font10 = new Font("SansSerif", Font.BOLD, 10);
		
		//타이틀
		this.add(lbTitle);
		lbTitle.setBounds(100, 50, 300, 30);		
		lbTitle.setFont(font20);
		
		this.add(lbName);
		lbName.setBounds(50, 100, 50, 30);
		lbName.setFont(font15);
		
		this.add(tfName);
		tfName.setBounds(120, 100, 100, 30);
		tfName.setFont(font15);		
		
		this.add(btnAdd);
		btnAdd.setBounds(250, 100, 100, 30);
		btnAdd.setFont(font15);
	
		this.add(lbHp);
		lbHp.setBounds(50, 150, 50, 30);
		lbHp.setFont(font15);
		
		this.add(tfHp);
		tfHp.setBounds(120, 150, 100, 30);
		tfHp.setFont(font15);		
		
		this.add(btnSearch);
		btnSearch.setBounds(250, 150, 100, 30);
		btnSearch.setFont(font15);
		
		this.add(lbAddr);
		lbAddr.setBounds(50, 200, 50, 30);
		lbAddr.setFont(font15);
		
		this.add(tfAddr);
		tfAddr.setBounds(120, 200, 100, 30);
		tfAddr.setFont(font15);		
		
		this.add(btnEdit);
		btnEdit.setBounds(250, 200, 100, 30);
		btnEdit.setFont(font15);	
	
		this.add(lbMemo);
		lbMemo.setBounds(50, 250, 50, 30);
		lbMemo.setFont(font15);
		
		this.add(tfMemo);
		tfMemo.setBounds(120, 250, 100, 30);
		tfMemo.setFont(font15);		
		
		this.add(btnDel);
		btnDel.setBounds(250, 250, 100, 30);
		btnDel.setFont(font15);
		
		
		this.add(lbList);
		lbList.setBounds(50, 300, 170, 30);
		lbList.setFont(font15);
		
		
		
		this.add(taList);
		taList.setBounds(50, 350, 300, 200);
		taList.setFont(font15);

		//화면 다 그리고 하단에 디비접속해서 리스트 뿌리기.
		loadData();
		
	}

	public void start() {
		
		
		 btnAdd.addActionListener(this); 
		 btnSearch.addActionListener(this);
		 btnEdit.addActionListener(this);
		 btnDel.addActionListener(this);
		  
		
		
		
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnAdd)
		{		
			insert();
		}
		else if(e.getSource() == btnSearch)
		{		
			search();
		}
		else if(e.getSource() == btnEdit)
		{		
			edit();
		}
		else if(e.getSource() == btnDel)
		{		
			del();
		}
		
		
	}
	void insert() {
		
		result="";//삽입시 기존 결과가 유지되고있어서 초기화
		
		//입력한 텍스트필드값 변수에저장
		String name = tfName.getText();
		String hp = tfHp.getText();
		String addr = tfAddr.getText();
		String memo = tfMemo.getText();
		

		try {
			//디비 연결
			conn = DriverManager.getConnection(url, id, pass);
			//삽입될때 쿼리문
			String query = "insert into memo values (null, ?, ?, ?, ?)";

			//쿼리문 수행
			pstmt = conn.prepareStatement(query);	
			
			//pstmt에 삽입될 변수값을 탑재
			pstmt.setString(1, name);
			pstmt.setString(2, hp);
			pstmt.setString(3, addr);
			pstmt.setString(4, memo);			
			pstmt.executeUpdate();
		} catch (SQLException e1) {			
		}	
		
		//입력한 값이디비에 들어가고 나머지 보기좋게 입력창 초기화
		tfName.setText("");
		tfHp.setText("");
		tfAddr.setText("");
		tfMemo.setText("");
		
		//추가된 친구정보가 디비에서 읽어서 최신저장된걸 보여주기
		loadData();
	}
	void search() {
		
		//검색대상이되는 이름 가져오기
		String name = tfName.getText();
		
		//디비에서 정보를 가져와서 텍스트에이리어에 뿌려주기				
				try {
					//디비 접속을 위한 conn변수 생성
					conn = DriverManager.getConnection(url, id, pass);
					//디비에연결
					stmt = conn.createStatement();
					//디비에서 조회한 모든 데이타를 가져와서 rs에 저장
					rs = stmt.executeQuery("select * from memo");
					//디비에서 조회한 모든데이타가 저장된 r의 값을 한줄씩 체크해서 없을때까지 반복
					while (rs.next()) {
						//한줄씩 반복하면서 result.에 누적해서 저장
						//getString은 테이블의 컬럼명 기준으로 데이타를 가져올수 있음.
						if(name.equals(rs.getString("name")))
						{
							tfName.setText(rs.getString("name"));
							tfHp.setText(rs.getString("hp"));
							tfAddr.setText(rs.getString("addr"));
							tfMemo.setText(rs.getString("memo"));
							
						}
						
							
					}				
					
					rs.close();
				} catch (SQLException error) {
					System.err.println("error = " + error.toString());			
				}	
		
		
	}
	void edit() {
		result="";//수정시 새로고침 효과
		
		String name = tfName.getText();
		String hp = tfHp.getText();
		String addr = tfAddr.getText();
		String memo = tfMemo.getText();
		
		String query = "update memo set hp = ?, addr = ?, memo = ? where name = ?";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);		
			pstmt.setString(1, hp);
			pstmt.setString(2, addr);
			pstmt.setString(3, memo);
			pstmt.setString(4, name);
			pstmt.executeUpdate();
			pstmt.close();
			System.err.println("회원 정보수정 완료!!");
		} catch (SQLException ee) {
			System.err.println("회원 정보수정 실패!!");
			
		}
		
		loadData();
		
	}
	void del() {
		String name = tfName.getText();
		
		String query = "delete from memo where name = ?";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.executeUpdate();
			pstmt.close();
			System.err.println("회원 삭제 완료!!");
		} catch (SQLException ee) {
			System.err.println("회원 삭제 실패!!");
			
		}
		
		tfName.setText("");
		tfHp.setText("");
		tfAddr.setText("");
		tfMemo.setText("");
		
		loadData();
		
	}
	
	void loadData()
	{
		result="";//수정시 새로고침 효과
		//디비에서 정보를 가져와서 텍스트에이리어에 뿌려주기				
		try {
			//디비 접속을 위한 conn변수 생성
			conn = DriverManager.getConnection(url, id, pass);
			//디비에연결
			stmt = conn.createStatement();
			//디비에서 조회한 모든 데이타를 가져와서 rs에 저장
			rs = stmt.executeQuery("select * from memo");
			//디비에서 조회한 모든데이타가 저장된 r의 값을 한줄씩 체크해서 없을때까지 반복
			while (rs.next()) {
				//한줄씩 반복하면서 result.에 누적해서 저장
				//getString은 테이블의 컬럼명 기준으로 데이타를 가져올수 있음.
				result += rs.getString("name") +"/"+ rs.getString("hp") + "/"+rs.getString("addr")+"/"+ rs.getString("memo") +"\n";	
			}
			//한번에 화면에 뿌리기
			taList.setText(result);
			rs.close();
		} catch (SQLException error) {
			System.err.println("error = " + error.toString());			
		}	
	}
	
	
		
}
