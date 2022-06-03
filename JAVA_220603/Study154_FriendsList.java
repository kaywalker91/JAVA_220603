package study0603;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

public class Study154_FriendsList 
{

	public static void main(String[] args) 
	{
		FriendsList f = new FriendsList();
	}
}

class FriendsList extends Frame implements ActionListener 
{
	
	Connection conn = null;
	String url = "jdbc:mysql://127.0.0.1:3306/study?useUnicode=true&characterEncoding=utf8";
	String id = "root";// ID
	String pass = "qwer";// ��й�ȣ
	String sql = "SELECT name, addr, memo, phone " + "FROM new_friends WHERE memo = ";
	
	//��� ����Ÿ ���Կ�
	PreparedStatement pstmt = null;		
	
	//��� ��ȸ��
	Statement stmt = null;
	ResultSet rs = null;
	
	String result="";

	//������ �߾ӹ�ġ�� ���� ���� 
	private Dimension dimen, dimen1;
	private int xpos, ypos;
	
	Button btnAdd = new Button("���");	
	Button btnSearch = new Button("�˻�");
	Button btnModi = new Button("����");
	Button btnDel = new Button("����");
	
	Label lbTitle = new Label("ȸ������ ���������");	
	Label lbName = new Label("�̸�:");
	Label lbAddr = new Label("�ּ�:");
	Label lbMemo = new Label("�޸�: ");
	Label lbPhone = new Label("����: ");
	Label lbList = new Label("ģ�� ����Ʈ");
	
	TextField tfName  = new TextField(20);	
	TextField tfAddr  = new TextField(20);
	TextField tfMemo  = new TextField(20);
	TextField tfPhone  = new TextField(20);
	
	TextArea taList = new TextArea();
	
	public FriendsList() 
	{
		super("ģ�� ����Ʈ");
		
		this.init();
		
		this.start();
		
		this.dataLoad();
		
		this.setSize(420, 600);	
		
		dimen = Toolkit.getDefaultToolkit().getScreenSize();
		dimen1 = this.getSize();
		xpos = (int) (dimen.getWidth() / 2 - dimen1.getWidth() / 2);
		ypos = (int) (dimen.getHeight() / 2 - dimen1.getHeight() / 2);
		this.setLocation(xpos, ypos);
		this.setVisible(true);
	}

	public void init() 
	{
		// �ۼַ�Ʈ���  : ���� ��ǥ �Է¹��.
		this.setLayout(null);

		Font font20 = new Font("SansSerif", Font.BOLD, 20);
		Font font15 = new Font("SansSerif", Font.BOLD, 15);
		Font font10 = new Font("SansSerif", Font.BOLD, 10);
		//Ÿ��Ʋ
		this.add(lbTitle);
		lbTitle.setBounds(100, 50, 300, 30);		
		lbTitle.setFont(font20);
		
		this.add(lbName);
		lbName.setBounds(50, 100, 50, 30);
		lbName.setFont(font15);
		
		this.add(tfName);
		tfName.setBounds(100, 100, 150, 30);
	
		this.add(lbAddr);
		lbAddr.setBounds(50, 150, 50, 30);
		lbAddr.setFont(font15);
		
		this.add(tfAddr);
		tfAddr.setBounds(100, 150, 150, 30);
		tfAddr.setFont(font15);
		
		this.add(lbMemo);
		lbMemo.setBounds(50, 200, 50, 30);
		lbMemo.setFont(font15);
		
		this.add(tfMemo);
		tfMemo.setBounds(100, 200, 150, 30);
		tfMemo.setFont(font15);
		
		this.add(lbPhone);
		lbPhone.setBounds(50, 250, 50, 30);
		lbPhone.setFont(font15);
		
		this.add(tfPhone);
		tfPhone.setBounds(100, 250, 150, 30);
		tfPhone.setFont(font15);	
		
		this.add(btnAdd);
		btnAdd.setBounds(260, 100, 80, 30);
		btnAdd.setFont(font15);
		
		this.add(btnSearch);
		btnSearch.setBounds(260, 150, 80, 30);
		btnSearch.setFont(font15);
		
		this.add(btnModi);
		btnModi.setBounds(260, 200, 80, 30);
		btnModi.setFont(font15);
		
		this.add(btnDel);
		btnDel.setBounds(260, 250, 80, 30);
		btnDel.setFont(font15);
		
		this.add(lbList);
		lbList.setBounds(50, 300, 170, 30);
		lbList.setFont(font15);
		
		this.add(taList);
		taList.setBounds(50, 350, 300, 200);
		taList.setFont(font15);

	}

	public void start() 
	{
		
		btnAdd.addActionListener(this); 
		btnSearch.addActionListener(this);
		btnModi.addActionListener(this); 
		btnDel.addActionListener(this); 
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
		if(e.getSource()==btnAdd)
		{
			result="";
			
			String name = tfName.getText();
			String addr = tfAddr.getText();
			String memo = tfMemo.getText();
			String phone = tfPhone.getText();
			
			try 
			{
				conn = DriverManager.getConnection(url, id, pass);
				
				String query = "insert into new_friends values (null, ?, ?, ?, ?)";
				
				pstmt = conn.prepareStatement(query);	
				
				pstmt.setString(1, name);
				pstmt.setString(2, addr);
				pstmt.setString(3, memo);
				pstmt.setString(4, phone);
				pstmt.executeUpdate();
				
			} 
			catch (SQLException ee) 
			{
				System.err.println("error = " + ee.toString());
				System.exit(0);
			}		
			
			Statement stmt = null;
			ResultSet rs = null;
			
			try 
			{
				stmt = conn.createStatement();
				rs = stmt.executeQuery("select * from new_friends");
				
				while (rs.next()) 
				{
					result += rs.getString("name") +"/"+ rs.getString("addr") + "/"+rs.getString("memo")+ "/"+rs.getString("phone")+"\n";	
				}
				taList.setText(result);
				rs.close();
			} 
			catch (SQLException error) 
			{
				System.err.println("error = " + error.toString());			
			}	
			
			tfName.setText("");
			tfAddr.setText("");
			tfMemo.setText("");
			tfPhone.setText("");
		}
		else if(e.getSource()==btnSearch)
		{
			result="";
			
			dataLoadSearch();
		}
		else if(e.getSource()==btnModi)
		{
			
		}
		else if(e.getSource()==btnDel)
		{
			try {
				dataLoadDel();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	void dataLoad()
	{
		//��� �����ؼ� �ʱ⿡ ����Ÿ �ε��ϱ�.
		
		try 
		{
			conn = DriverManager.getConnection(url, id, pass);			
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from new_friends");
			
			while (rs.next()) 
			{
				result += rs.getString("name") +"/"+ rs.getString("addr") + "/"+rs.getString("memo")+ "/"+rs.getString("phone")+ "\n";	
			}
			
			taList.setText(result);
			
			rs.close();
		} 
		catch (SQLException error) 
		{
			System.err.println("error = " + error.toString());			
		}	
	}
	
	void dataLoadSearch()
	{
		
		
		
	}
	
	void dataLoadModi()
	{
		result="";
		
		String name = tfName.getText();
		String addr = tfAddr.getText();
		String memo = tfMemo.getText();
		String phone = tfPhone.getText();
		
		try 
		{
			conn = DriverManager.getConnection(url, id, pass);
			
			String query = "update new_friends set name='����' where name='�Ϸиӽ�ũ' ";
			
			pstmt = conn.prepareStatement(query);	
			
			pstmt.setString(1, name);
			pstmt.setString(2, addr);
			pstmt.setString(3, memo);
			pstmt.setString(4, phone);
			pstmt.executeUpdate();
			
		} 
		catch (SQLException ee) 
		{
			System.err.println("error = " + ee.toString());
			System.exit(0);
		}		
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try 
		{
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from new_friends");
			
			while (rs.next()) 
			{
				result += rs.getString("name") +"/"+ rs.getString("addr") + "/"+rs.getString("memo")+ "/"+rs.getString("phone")+"\n";	
			}
			taList.setText(result);
			rs.close();
		} 
		catch (SQLException error) 
		{
			System.err.println("error = " + error.toString());			
		}	
		
		tfName.setText("");
		tfAddr.setText("");
		tfMemo.setText("");
		tfPhone.setText("");
	}
	
	void dataLoadDel() throws SQLException
	{
		
		conn = DriverManager.getConnection(url, id, pass);
		stmt = conn.createStatement();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("DELETE FROM new_friends WHERE name='�Ϸиӽ�ũ'");
		
		int deleteCount = stmt.executeUpdate(sb.toString());
		
		if(deleteCount==0)
		System.out.println("������ ���ڵ尡 �����ϴ�.");
		else
		System.out.println(deleteCount +"���� ���ڵ尡 ���� �Ǿ����ϴ�.");
		
	}

}

