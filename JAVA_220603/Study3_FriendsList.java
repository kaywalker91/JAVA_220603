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
	//��� ���� ���� 
	Connection conn = null;
	String url = "jdbc:mysql://127.0.0.1:3306/study?useUnicode=true&characterEncoding=utf8";
	String id = "root";// ID
	String pass = "qwer";// ��й�ȣ	
	
	//��� ����Ÿ ���Կ�
	PreparedStatement pstmt = null;	
		
	//��� ��ȸ��
	Statement stmt = null;
	ResultSet rs = null;
	
	
	String result="";//�ؽ�Ʈ���̸�� �����ؼ� �ѹ��� ����ϱ����ؼ� 
			
	
	
	
	//������ �߾ӹ�ġ�� ���� ���� 
	private Dimension dimen, dimen1;
	private int xpos, ypos;
	
	Button btnAdd = new Button("���");	
	Button btnSearch = new Button("�˻�");
	Button btnEdit = new Button("����");
	Button btnDel = new Button("����");
	Label lbTitle = new Label("ȸ������ ���������3");	
	Label lbName = new Label("�� ��:");
	Label lbHp = new Label("�� ��  :");
	Label lbAddr = new Label("�� �� : ");
	Label lbMemo = new Label("�� ��");
	Label lbList = new Label("ģ�� ����Ʈ");
	TextField tfName  = new TextField(20);	
	TextField tfHp  = new TextField(20);
	TextField tfAddr  = new TextField(20);
	TextField tfMemo  = new TextField(20);
	
	TextArea taList = new TextArea();
	
	
	public Friends3() {
		super("ģ�������ϱ�3");
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

		//ȭ�� �� �׸��� �ϴܿ� ��������ؼ� ����Ʈ �Ѹ���.
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
		
		result="";//���Խ� ���� ����� �����ǰ��־ �ʱ�ȭ
		
		//�Է��� �ؽ�Ʈ�ʵ尪 ����������
		String name = tfName.getText();
		String hp = tfHp.getText();
		String addr = tfAddr.getText();
		String memo = tfMemo.getText();
		

		try {
			//��� ����
			conn = DriverManager.getConnection(url, id, pass);
			//���Եɶ� ������
			String query = "insert into memo values (null, ?, ?, ?, ?)";

			//������ ����
			pstmt = conn.prepareStatement(query);	
			
			//pstmt�� ���Ե� �������� ž��
			pstmt.setString(1, name);
			pstmt.setString(2, hp);
			pstmt.setString(3, addr);
			pstmt.setString(4, memo);			
			pstmt.executeUpdate();
		} catch (SQLException e1) {			
		}	
		
		//�Է��� ���̵�� ���� ������ �������� �Է�â �ʱ�ȭ
		tfName.setText("");
		tfHp.setText("");
		tfAddr.setText("");
		tfMemo.setText("");
		
		//�߰��� ģ�������� ��񿡼� �о �ֽ�����Ȱ� �����ֱ�
		loadData();
	}
	void search() {
		
		//�˻�����̵Ǵ� �̸� ��������
		String name = tfName.getText();
		
		//��񿡼� ������ �����ͼ� �ؽ�Ʈ���̸�� �ѷ��ֱ�				
				try {
					//��� ������ ���� conn���� ����
					conn = DriverManager.getConnection(url, id, pass);
					//��񿡿���
					stmt = conn.createStatement();
					//��񿡼� ��ȸ�� ��� ����Ÿ�� �����ͼ� rs�� ����
					rs = stmt.executeQuery("select * from memo");
					//��񿡼� ��ȸ�� ��絥��Ÿ�� ����� r�� ���� ���پ� üũ�ؼ� ���������� �ݺ�
					while (rs.next()) {
						//���پ� �ݺ��ϸ鼭 result.�� �����ؼ� ����
						//getString�� ���̺��� �÷��� �������� ����Ÿ�� �����ü� ����.
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
		result="";//������ ���ΰ�ħ ȿ��
		
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
			System.err.println("ȸ�� �������� �Ϸ�!!");
		} catch (SQLException ee) {
			System.err.println("ȸ�� �������� ����!!");
			
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
			System.err.println("ȸ�� ���� �Ϸ�!!");
		} catch (SQLException ee) {
			System.err.println("ȸ�� ���� ����!!");
			
		}
		
		tfName.setText("");
		tfHp.setText("");
		tfAddr.setText("");
		tfMemo.setText("");
		
		loadData();
		
	}
	
	void loadData()
	{
		result="";//������ ���ΰ�ħ ȿ��
		//��񿡼� ������ �����ͼ� �ؽ�Ʈ���̸�� �ѷ��ֱ�				
		try {
			//��� ������ ���� conn���� ����
			conn = DriverManager.getConnection(url, id, pass);
			//��񿡿���
			stmt = conn.createStatement();
			//��񿡼� ��ȸ�� ��� ����Ÿ�� �����ͼ� rs�� ����
			rs = stmt.executeQuery("select * from memo");
			//��񿡼� ��ȸ�� ��絥��Ÿ�� ����� r�� ���� ���پ� üũ�ؼ� ���������� �ݺ�
			while (rs.next()) {
				//���پ� �ݺ��ϸ鼭 result.�� �����ؼ� ����
				//getString�� ���̺��� �÷��� �������� ����Ÿ�� �����ü� ����.
				result += rs.getString("name") +"/"+ rs.getString("hp") + "/"+rs.getString("addr")+"/"+ rs.getString("memo") +"\n";	
			}
			//�ѹ��� ȭ�鿡 �Ѹ���
			taList.setText(result);
			rs.close();
		} catch (SQLException error) {
			System.err.println("error = " + error.toString());			
		}	
	}
	
	
		
}
