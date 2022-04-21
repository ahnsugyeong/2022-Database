import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class JC20011844M extends JFrame {
	Connection con;
	JLabel ResultLabel = new JLabel("");
	JTextField textfield = new JTextField("");
	int flag = 0;
	String tablename = "";
	String insertInfo = "";
	JLabel state = new JLabel("state: ");

	JC20011844M() {
		String Driver = "";
		String url = "jdbc:mysql://localhost:3306/hospital?&serverTimezone=Asia/Seoul";
		String userid = "hospital";
		String pwd = "hospital";

		try { /* 드라이버를 찾는 과정 */
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("드라이버 로드 성공");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("데이터베이스 연결 준비...");
			con = DriverManager.getConnection(url, userid, pwd);
			System.out.println("데이터베이스 연결 성공");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		setTitle("20011844 안수경");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container c = getContentPane();
		c.setLayout(null);
		JButton reset = new JButton("reset");
		JButton insert = new JButton("insert");
		JButton search = new JButton("search");
		JButton search1 = new JButton("search1");
		JButton search2 = new JButton("search2");
		JButton search3 = new JButton("search3");
		c.add(reset);
		reset.setSize(100, 40);
		reset.setLocation(50, 10);
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetAll();
				selectAll();
				
			}
		});

		c.add(search);
		search.setSize(100, 40);
		search.setLocation(170, 10);
		search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectAll();
			}
		});

		c.add(search1);
		search1.setSize(100, 40);
		search1.setLocation(290, 10);
		search1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				search1();
			}
		});

		c.add(search2);
		search2.setSize(100, 40);
		search2.setLocation(410, 10);
		search2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				search2();
			}
		});

		c.add(search3);
		search3.setSize(100, 40);
		search3.setLocation(530, 10);

		JButton button = new JButton("go");
		c.add(button);
		button.setSize(50, 30);
		button.setLocation(620, 70);

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(textfield.getText().equals("")) {
					state.setText("please input!!");
					return;
				}
				

				if (flag == 0) {
					String text =textfield.getText().toLowerCase();
					if(text.equals("doctors")||text.equals("nurses")||text.equals("patients")||text.equals("treatments")||text.equals("charts")) {
					tablename = textfield.getText();
					flag = 1;
					textfield.setText("");
					state.setText("table input success. please enter information");
					}
					else {
						state.setText("input error");
						textfield.setText("");
						return;
					}
				} else if (flag == 1) {
					insertInfo = textfield.getText();
					flag = 0;
					insert(tablename, insertInfo);
					textfield.setText("");
				}
				
			}
		});

		c.add(textfield);
		textfield.setSize(550, 30);
		textfield.setLocation(50, 70);

		JScrollPane jscp = new JScrollPane(ResultLabel);
		c.add(jscp);

		jscp.setSize(650, 485);
		jscp.setLocation(20, 130);
		
		c.add(state);
		state.setSize(100, 30);
		state.setLocation(300, 100);
		
		setSize(700, 2000);
		setVisible(true);

		c.setFocusable(true);
		c.requestFocus();
	}

	public static void main(String[] args) {
		new JC20011844M();

		// so.selectAll();
//		so.sqlModify();
//		so.sqlRun();

	}

	void resetAll() {
		String reset1 = "DROP TABLE Charts";
		String reset2 = "DROP TABLE Treatments";
		String reset3 = "DROP TABLE Patients";
		String reset4 = "DROP TABLE Nurses";
		String reset5 = "DROP TABLE Doctors";

		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(reset1);
			stmt.executeUpdate(reset2);
			stmt.executeUpdate(reset3);
			stmt.executeUpdate(reset4);
			stmt.executeUpdate(reset5);
			stmt.executeUpdate("CREATE TABLE Doctors (\r\n" + "  doc_id INTEGER NOT NULL,\r\n"
					+ "  major_treat VARCHAR(25) NOT NULL,\r\n" + "  doc_name VARCHAR(20) NOT NULL,\r\n"
					+ "  doc_gen VARCHAR(1) NOT NULL,\r\n" + "  doc_phone VARCHAR(15) NULL,\r\n"
					+ "  doc_email VARCHAR(50) UNIQUE,\r\n" + "  doc_position VARCHAR(20) NOT NULL\r\n" + ");");

			stmt.executeUpdate("ALTER TABLE Doctors\r\n" + "  ADD CONSTRAINT doc_id_pk PRIMARY KEY (doc_id);");
			stmt.executeUpdate("CREATE TABLE Nurses (\r\n" + "  nur_id INTEGER NOT NULL,\r\n"
					+ "  major_job VARCHAR(25) NOT NULL,\r\n" + "  nur_name VARCHAR(20) NOT NULL,\r\n"
					+ "  nur_gen char(1) NOT NULL,\r\n" + "  nur_phone VARCHAR(15) NULL,\r\n"
					+ "  nur_email VARCHAR(50) UNIQUE,\r\n" + "  nur_position VARCHAR(20) NOT NULL\r\n" + ");");
			stmt.executeUpdate("ALTER TABLE Nurses\r\n" + "  ADD CONSTRAINT nur_id_pk PRIMARY KEY (nur_id);");
			stmt.executeUpdate(
					"CREATE TABLE Patients (\r\n" + "  pat_id INTEGER NOT NULL,\r\n" + "  nur_id INTEGER NOT NULL,\r\n"
							+ "  doc_id INTEGER NOT NULL,\r\n" + "  pat_name VARCHAR(20) NOT NULL,\r\n"
							+ "  pat_gen VARCHAR(1) NOT NULL,\r\n" + "  pat_jumin VARCHAR(14) NOT NULL,\r\n"
							+ "  pat_addr VARCHAR(100) NOT NULL,\r\n" + "  pat_phone VARCHAR(15) NULL,\r\n"
							+ "  pat_email VARCHAR(50) UNIQUE,\r\n" + "  pat_job VARCHAR(20) NOT NULL\r\n" + ");");
			stmt.executeUpdate("ALTER TABLE Patients\r\n" + "  ADD CONSTRAINT pat_id_pk PRIMARY KEY (pat_id);");
			stmt.executeUpdate("ALTER TABLE Patients\r\n"
					+ "  ADD (CONSTRAINT R_2 FOREIGN KEY (doc_id) REFERENCES Doctors (doc_id));");
			stmt.executeUpdate("ALTER TABLE Patients\r\n"
					+ "  ADD (CONSTRAINT R_3 FOREIGN KEY (nur_id) REFERENCES Nurses (nur_id));");
			stmt.executeUpdate("CREATE TABLE Treatments (\r\n" + "  treat_id INTEGER NOT NULL,\r\n"
					+ "  pat_id INTEGER NOT NULL,\r\n" + "  doc_id INTEGER NOT NULL,\r\n"
					+ "  treat_contents VARCHAR(1000) NOT NULL,\r\n" + "  treat_date DATE NOT NULL\r\n" + ");");
			stmt.executeUpdate("ALTER TABLE Treatments\r\n"
					+ "  ADD CONSTRAINT treat_pat_doc_id_pk PRIMARY KEY (treat_id, pat_id, doc_id);");
			stmt.executeUpdate("ALTER TABLE Treatments\r\n"
					+ "  ADD (CONSTRAINT R_5 FOREIGN KEY (pat_id) REFERENCES Patients (pat_id));");
			stmt.executeUpdate("ALTER TABLE Treatments\r\n"
					+ "  ADD (CONSTRAINT R_6 FOREIGN KEY (doc_id) REFERENCES Doctors (doc_id));");
			stmt.executeUpdate("CREATE TABLE Charts (\r\n" + "  chart_id VARCHAR(20) NOT NULL,\r\n"
					+ "  treat_id INTEGER NOT NULL,\r\n" + "  doc_id INTEGER NOT NULL,\r\n"
					+ "  pat_id INTEGER NOT NULL,\r\n" + "  nur_id INTEGER NOT NULL,\r\n"
					+ "  chart_contents VARCHAR(1000) NOT NULL\r\n" + ");");
			stmt.executeUpdate("ALTER TABLE Charts\r\n"
					+ "  ADD CONSTRAINT chart_treat_doc_pat_id_pk PRIMARY KEY (chart_id, treat_id, doc_id, pat_id);");
			stmt.executeUpdate("ALTER TABLE Charts\r\n"
					+ "  ADD (CONSTRAINT R_4 FOREIGN KEY (nur_id) REFERENCES Nurses (nur_id));");
			stmt.executeUpdate("ALTER TABLE Charts\r\n"
					+ "  ADD (CONSTRAINT R_7 FOREIGN KEY (treat_id, pat_id, doc_id) REFERENCES Treatments (treat_id, pat_id, doc_id));");
			stmt.executeUpdate(
					"INSERT INTO Doctors VALUES(980312, '소아과', '이태정', 'M', '010-333-1340', 'ltj@hanbit.com', '과장');");
			stmt.executeUpdate(
					"INSERT INTO Doctors VALUES(000601, '내과', '안성기', 'M', '011-222-0987', 'ask@hanbit.com', '과장');");
			stmt.executeUpdate(
					"INSERT INTO Doctors VALUES(001208, '외과', '김민종', 'M', '010-333-8743', 'kmj@hanbit.com', '과장');");
			stmt.executeUpdate(
					"INSERT INTO Doctors VALUES(020403, '피부과', '이태서', 'M', '019-777-3764', 'lts@hanbit.com', '과장');");
			stmt.executeUpdate(
					"INSERT INTO Doctors VALUES(050900, '소아과', '김연아', 'F', '010-555-3746', 'kya@hanbit.com', '전문의');");
			stmt.executeUpdate(
					"INSERT INTO Doctors VALUES(050101, '내과', '차태현', 'M', '011-222-7643', 'cth@hanbit.com', '전문의');");
			stmt.executeUpdate(
					"INSERT INTO Doctors VALUES(062019, '소아과', '전지현', 'F', '010-999-1265', 'jjh@hanbit.com', '전문의');");
			stmt.executeUpdate(
					"INSERT INTO Doctors VALUES(070576, '피부과', '홍길동', 'M', '016-333-7263', 'hgd@hanbit.com', '전문의');");
			stmt.executeUpdate(
					"INSERT INTO Doctors VALUES(080543, '방사선과', '유재석', 'M', '010-222-1263', 'yjs@hanbit.com', '과장');");
			stmt.executeUpdate(
					"INSERT INTO Doctors VALUES(091001, '외과', '김병만', 'M', '010-555-3542', 'kbm@hanbit.com', '전문의');");
			
			stmt.executeUpdate("INSERT INTO Nurses VALUES(050302, '소아과', '김은영', 'F', '010-555-8751', 'key@hanbit.com', '수간호사');");
			stmt.executeUpdate("INSERT INTO Nurses VALUES(050021, '내과', '윤성애', 'F', '016-333-8745', 'ysa@hanbit.com', '수간호사');");
			stmt.executeUpdate("INSERT INTO Nurses VALUES(040089, '피부과', '신지원', 'M', '010-666-7646', 'sjw@hanbit.com', '주임');");
			stmt.executeUpdate("INSERT INTO Nurses VALUES(070605, '방사선과', '유정화', 'F', '010-333-4588', 'yjh@hanbit.com', '주임');");
			stmt.executeUpdate("INSERT INTO Nurses VALUES(070804, '내과', '라하나', 'F', '010-222-1340', 'nhn@hanbit.com', '주임');");
			stmt.executeUpdate("INSERT INTO Nurses VALUES(071018, '소아과', '김화경', 'F', '019-888-4116', 'khk@hanbit.com', '주임');");
			stmt.executeUpdate("INSERT INTO Nurses VALUES(100356, '소아과', '이선용', 'M', '010-777-1234', 'lsy@hanbit.com', '간호사');");
			stmt.executeUpdate("INSERT INTO Nurses VALUES(104145, '외과', '김현', 'M', '010-999-8520', 'kh@hanbit.com', '간호사');");
			stmt.executeUpdate("INSERT INTO Nurses VALUES(120309, '피부과', '박성완', 'M', '010-777-4996', 'psw@hanbit.com', '간호사');");
			stmt.executeUpdate("INSERT INTO Nurses VALUES(130211, '외과', '이서연', 'F', '010-222-3214', 'lsy2@hanbit.com', '간호사');");

			
			stmt.executeUpdate("INSERT INTO Patients VALUES(2345, 050302, 980312, '안상건', 'M', 232345, '서울', '010-555-7845', 'ask@ab.com', '회사원');");
			stmt.executeUpdate("INSERT INTO Patients VALUES(3545, 040089, 020403, '김성룡', 'M', 543545, '서울', '010-333-7812', 'ksn@bb.com', '자영업');");
			stmt.executeUpdate("INSERT INTO Patients VALUES(3424, 070605, 080543, '이종진', 'M', 433424, '부산', '010-888-4859', 'ljj@ab.com', '회사원');");
			stmt.executeUpdate("INSERT INTO Patients VALUES(7675, 100356, 050900, '최광석', 'M', 677675, '당진', '010-222-4847', 'cks@cc.com', '회사원');");
			stmt.executeUpdate("INSERT INTO Patients VALUES(4533, 070804, 000601, '정한경', 'M', 744533, '강릉', '010-777-9630', 'jhk@ab.com', '교수');");
			stmt.executeUpdate("INSERT INTO Patients VALUES(5546, 120309, 070576, '유원현', 'M', 765546, '대구', '016-777-0214', 'ywh@cc.com', '자영업');");
			stmt.executeUpdate("INSERT INTO Patients VALUES(4543, 070804, 050101, '최재정', 'M', 454543, '부산', '010-555-4187', 'cjj@bb.com', '회사원');");
			stmt.executeUpdate("INSERT INTO Patients VALUES(9768, 130211, 091001, '이진희', 'F', 119768, '서울', '010-888-3675', 'ljh@ab.com', '교수');");
			stmt.executeUpdate("INSERT INTO Patients VALUES(4234, 130211, 091001, '오나미', 'F', 234234, '속초', '010-999-6541', 'onm@cc.com', '학생');");
			stmt.executeUpdate("INSERT INTO Patients VALUES(7643, 071018, 062019, '송석묵', 'M', 987643, '서울', '010-222-5874', 'ssm@bb.com', '학생');");
			
			stmt.executeUpdate("INSERT INTO Treatments VALUES(130516023, 2345, 980312, '감기, 몸살', STR_TO_DATE('2013-05-16','%Y-%m-%d'));");
			stmt.executeUpdate("INSERT INTO Treatments VALUES(130628100, 3545, 020403, '피부 트러블 치료', STR_TO_DATE('2013-06-28','%Y-%m-%d'));");
			stmt.executeUpdate("INSERT INTO Treatments VALUES(131205056, 3424, 080543, '목 디스크로 MRI 촬영', STR_TO_DATE('2013-12-05','%Y-%m-%d'));");
			stmt.executeUpdate("INSERT INTO Treatments VALUES(131218024, 7675, 050900, '중이염', STR_TO_DATE('2013-12-18','%Y-%m-%d'));");
			stmt.executeUpdate("INSERT INTO Treatments VALUES(131224012, 4533, 000601, '장염', STR_TO_DATE('2013-12-24','%Y-%m-%d'));");
			stmt.executeUpdate("INSERT INTO Treatments VALUES(140103001, 5546, 070576, '여드름 치료', STR_TO_DATE('2014-01-03','%Y-%m-%d'));");
			stmt.executeUpdate("INSERT INTO Treatments VALUES(140109026, 4543, 050101, '위염', STR_TO_DATE('2014-01-09','%Y-%m-%d'));");
			stmt.executeUpdate("INSERT INTO Treatments VALUES(140226102, 9768, 091001, '화상치료', STR_TO_DATE('2014-02-26','%Y-%m-%d'));");
			stmt.executeUpdate("INSERT INTO Treatments VALUES(140303003, 4234, 091001, '교통사고 외상치료', STR_TO_DATE('2014-03-03','%Y-%m-%d'));");
			stmt.executeUpdate("INSERT INTO Treatments VALUES(140308087, 7643, 062019, '장염', STR_TO_DATE('2014-03-08','%Y-%m-%d'));");
			
			stmt.executeUpdate("INSERT INTO Charts VALUES('PD13572410', 130516023, 980312, 2345, 050302, '편도선, 감기약 처방');");
			stmt.executeUpdate("INSERT INTO Charts VALUES('DM11389132', 130628100, 020403, 3545, 040089, '피부약 처방');");
			stmt.executeUpdate("INSERT INTO Charts VALUES('RD10023842', 131205056, 080543, 3424, 070605, '목 디스크 의심, 추가 검사 필요');");
			stmt.executeUpdate("INSERT INTO Charts VALUES('PD13581241', 131218024, 050900, 7675, 100356, '세반고리관 추가 검사 필요');");
			stmt.executeUpdate("INSERT INTO Charts VALUES('IM12557901', 131224012, 000601, 4533, 070804, '위장약 처방');");
			stmt.executeUpdate("INSERT INTO Charts VALUES('DM11400021', 140103001, 070576, 5546, 120309, '여드름 치료제 처방');");
			stmt.executeUpdate("INSERT INTO Charts VALUES('IM12708224', 140109026, 050101, 4543, 070804, '위염 심각, 추가 검사 후 수술 권함');");
			stmt.executeUpdate("INSERT INTO Charts VALUES('GS17223681', 140226102, 091001, 9768, 130211, '화상약 처방 및 물리치료');");
			stmt.executeUpdate("INSERT INTO Charts VALUES('GS17264430', 140303003, 091001, 4234, 130211, '추가 성형수술 필요함');");
			stmt.executeUpdate("INSERT INTO Charts VALUES('PD13664611', 140308087, 062019, 7643, 071018, '장염약 처방');");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	void selectAll() {
		String select_doctors = "SELECT * FROM Doctors";
		String select_nurses = "SELECT * FROM Nurses";
		String select_patients = "SELECT * FROM Patients";
		String select_treatments = "SELECT * FROM Treatments";
		String select_charts = "SELECT * FROM Charts";
		String str = "<html>";
		try { /* 데이터베이스에 질의 결과를 가져오는 과정 */
			String DoctorStr = "";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(select_doctors);

			System.out.println("\t doc_id \tmajor_treat \tdoc_name \tdoc_gen \tdoc_phone \tdoc_email \tdoc_position");
			DoctorStr += "\t doc_id \tmajor_treat \tdoc_name \tdoc_gen \tdoc_phone \tdoc_email \tdoc_position" + "<br>";

			while (rs.next()) {
				System.out.print(" \t" + rs.getInt(1));

				System.out.print(" \t\t" + rs.getString(2));
				System.out.print(" \t\t" + rs.getString(3));
				System.out.print(" \t\t" + rs.getString(4));
				System.out.print(" \t\t" + rs.getString(5));
				System.out.print(" \t" + rs.getString(6));
				System.out.println(" \t\t" + rs.getString(7));
				DoctorStr += " \t" + rs.getInt(1) + " \t\t" + rs.getString(2) + " \t\t" + rs.getString(3) + " \t\t"
						+ rs.getString(4) + " \t\t" + rs.getString(5) + " \t" + rs.getString(6) + " \t\t"
						+ rs.getString(7) + "<br>";
			}
			str += DoctorStr + "<br>";

			String NurseStr = "";
			rs = stmt.executeQuery(select_nurses);

			System.out.println("\t nur_id \tmajor_job \tnur_name \tnur_gen \tnur_phone \tnur_email \tnur_position");
			NurseStr += "\t nur_id \tmajor_job \tnur_name \tnur_gen \tnur_phone \tnur_email \tnur_position" + "<br>";

			while (rs.next()) {
				System.out.print(" \t" + rs.getInt(1));

				System.out.print(" \t\t" + rs.getString(2));
				System.out.print(" \t\t" + rs.getString(3));
				System.out.print(" \t\t" + rs.getString(4));
				System.out.print(" \t\t" + rs.getString(5));
				System.out.print(" \t" + rs.getString(6));
				System.out.println(" \t\t" + rs.getString(7));
				NurseStr += " \t" + rs.getInt(1) + " \t\t" + rs.getString(2) + " \t\t" + rs.getString(3) + " \t\t"
						+ rs.getString(4) + " \t\t" + rs.getString(5) + " \t" + rs.getString(6) + " \t\t"
						+ rs.getString(7) + "<br>";
			}
			str += NurseStr + "<br>";

			String PatientStr = "";
			rs = stmt.executeQuery(select_patients);

			System.out.println(
					"\t pat_id \tnur_id \tdoc_id \tpat_name \tpat_gen \tpat_jumin \tpat_addr \tpat_phone \tpat_email \tpat_job");
			PatientStr += "\t pat_id \tnur_id \tdoc_id \tpat_name \tpat_gen \tpat_jumin \tpat_addr \tpat_phone \tpat_email \tpat_job"
					+ "<br>";

			while (rs.next()) {
				System.out.print(" \t" + rs.getInt(1));
				System.out.print(" \t\t" + rs.getInt(2));
				System.out.print(" \t\t" + rs.getInt(3));
				System.out.print(" \t\t" + rs.getString(4));
				System.out.print(" \t\t" + rs.getString(5));
				System.out.print(" \t" + rs.getString(6));
				System.out.print(" \t" + rs.getString(7));
				System.out.print(" \t" + rs.getString(8));
				System.out.print(" \t" + rs.getString(9));
				System.out.println(" \t\t" + rs.getString(10));
				PatientStr += " \t" + rs.getInt(1) + " \t\t" + rs.getInt(2) + " \t\t" + rs.getInt(3) + " \t\t"
						+ rs.getString(4) + " \t\t" + rs.getString(5) + " \t" + rs.getString(6) + " \t\t"
						+ rs.getString(7) + " \t\t" + rs.getString(8) + " \t\t" + rs.getString(9) + " \t\t"
						+ rs.getString(10) + "<br>";
			}
			str += PatientStr + "<br>";

			String TreatStr = "";
			rs = stmt.executeQuery(select_treatments);

			System.out.println("\t treat_id \tpat_id \tdoc_id \ttreat_contents \tpat_gen \ttreat_date");
			TreatStr += "\t treat_id \tpat_id \tdoc_id \ttreat_contents \tpat_gen \ttreat_date" + "<br>";

			while (rs.next()) {
				System.out.print(" \t" + rs.getInt(1));
				System.out.print(" \t\t" + rs.getInt(2));
				System.out.print(" \t\t" + rs.getInt(3));
				System.out.print(" \t\t" + rs.getString(4));
				System.out.println(" \t\t" + rs.getString(5));

				TreatStr += " \t" + rs.getInt(1) + " \t\t" + rs.getInt(2) + " \t\t" + rs.getInt(3) + " \t\t"
						+ rs.getString(4) + " \t\t" + rs.getString(5) + "<br>";
			}
			str += TreatStr + "<br>";

			String ChartStr = "";
			rs = stmt.executeQuery(select_charts);

			System.out.println("\t chart_id \ttreat_id \tdoc_id \tpat_id \tnur_id \tchart_contents");
			ChartStr += "\t chart_id \ttreat_id \tdoc_id \tpat_id \tnur_id \tchart_contents" + "<br>";

			while (rs.next()) {
				System.out.print(" \t" + rs.getString(1));
				System.out.print(" \t\t" + rs.getInt(2));
				System.out.print(" \t\t" + rs.getInt(3));
				System.out.print(" \t\t" + rs.getInt(4));
				System.out.println(" \t\t" + rs.getString(5));

				ChartStr += " \t" + rs.getString(1) + " \t\t" + rs.getInt(2) + " \t\t" + rs.getInt(3) + " \t\t"
						+ rs.getInt(4) + " \t\t" + rs.getString(5) + "<br>";
			}
			str += ChartStr + "<br>";

			ResultLabel.setText(str);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	void insert(String tablename, String tableInfo) {
		System.out.println("tablename = " + tablename + ", tableinfo = " + tableInfo);
		String query = "insert into " + tablename + " values (" + tableInfo + ");"; /* SQL 문 */
		System.out.println(query);
		try { /* 데이터베이스에 질의 결과를 가져오는 과정 */
			Statement stmt = con.createStatement();
			stmt.execute(query);
			System.out.println("insert success");
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	void search1() {

		try { /* 데이터베이스에 질의 결과를 가져오는 과정 */
			String searchStr ="<html> ";
			String query = "SELECT P.pat_name,  P.pat_addr, P.pat_phone, \r\n"
					+ "(select T.treat_contents\r\n"
					+ "from treatments T\r\n"
					+ "where P.pat_id = T.pat_id\r\n"
					+ ") , (select T.treat_date\r\n"
					+ "from treatments T\r\n"
					+ "where P.pat_id = T.pat_id\r\n"
					+ ") from patients P where P.doc_id = 91001;"; /* SQL 문 */
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("\t pat_name \tpat_addr \tpat_phone \ttreat_contents \ttreat_data");
			
			searchStr += "\t pat_name \tpat_addr \tpat_phone \ttreat_contents \ttreat_data" + "<br>";

			while (rs.next()) {
				System.out.print(" \t" + rs.getString(1));
				System.out.print(" \t\t" + rs.getString(2));
				System.out.print(" \t\t" + rs.getString(3));
				System.out.print(" \t" + rs.getString(4));
				System.out.println(" \t\t" + rs.getString(5));

				searchStr += " \t" + rs.getString(1) + " \t\t" + rs.getString(2) + " \t\t" + rs.getString(3) + " \t"
						+ rs.getString(4) + " \t\t" + rs.getString(5) + "<br>";
			}
			searchStr += "<br> </html>";
			ResultLabel.setText(searchStr);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void search2() {

		try { /* 데이터베이스에 질의 결과를 가져오는 과정 */
			String searchStr ="<html> ";
			String query = "select *\r\n"
					+ "from Doctors D\r\n"
					+ "where not exists (\r\n"
					+ "	SELECT doc_id from charts C where D.doc_id = C.doc_id\r\n"
					+ ") ;;"; /* SQL 문 */
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("\t doc_id \tmajor_treat \tdoc_name \tdoc_gen \tdoc_phone \tdoc_email \tdoc_position");
			searchStr += "\t doc_id \tmajor_treat \tdoc_name \tdoc_gen \tdoc_phone \tdoc_email \tdoc_position" + "<br>";

			while (rs.next()) {
				System.out.print(" \t" + rs.getInt(1));

				System.out.print(" \t\t" + rs.getString(2));
				System.out.print(" \t\t" + rs.getString(3));
				System.out.print(" \t\t" + rs.getString(4));
				System.out.print(" \t\t" + rs.getString(5));
				System.out.print(" \t" + rs.getString(6));
				System.out.println(" \t\t" + rs.getString(7));
				searchStr += " \t" + rs.getInt(1) + " \t\t" + rs.getString(2) + " \t\t" + rs.getString(3) + " \t\t"
						+ rs.getString(4) + " \t\t" + rs.getString(5) + " \t" + rs.getString(6) + " \t\t"
						+ rs.getString(7) + "<br>";
			}
			
			searchStr += "<br> </html>";
			ResultLabel.setText(searchStr);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void search3() {

		try { /* 데이터베이스에 질의 결과를 가져오는 과정 */
			String searchStr ="<html> ";
			String query = "select * \r\n"
					+ "from Doctors D\r\n"
					+ "group by D.doc_id \r\n"
					+ "having (count(D.doc_id) = ( select max(count(C.doc_id)) from Charts C group by C.doc_id));"; /* SQL 문 */
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("\t doc_id \tmajor_treat \tdoc_name \tdoc_gen \tdoc_phone \tdoc_email \tdoc_position");
			searchStr += "\t doc_id \tmajor_treat \tdoc_name \tdoc_gen \tdoc_phone \tdoc_email \tdoc_position" + "<br>";

			while (rs.next()) {
				System.out.print(" \t" + rs.getInt(1));

				System.out.print(" \t\t" + rs.getString(2));
				System.out.print(" \t\t" + rs.getString(3));
				System.out.print(" \t\t" + rs.getString(4));
				System.out.print(" \t\t" + rs.getString(5));
				System.out.print(" \t" + rs.getString(6));
				System.out.println(" \t\t" + rs.getString(7));
				searchStr += " \t" + rs.getInt(1) + " \t\t" + rs.getString(2) + " \t\t" + rs.getString(3) + " \t\t"
						+ rs.getString(4) + " \t\t" + rs.getString(5) + " \t" + rs.getString(6) + " \t\t"
						+ rs.getString(7) + "<br>";
			}
			
			searchStr += "<br> </html>";
			ResultLabel.setText(searchStr);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void sqlRun() {
		String query = "SELECT * FROM patients"; /* SQL 문 */
		try { /* 데이터베이스에 질의 결과를 가져오는 과정 */
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
//			System.out.println(" BOOK NO \tBOOK NAME \t\tPUBLISHER \tPRICE ");
//			while (rs.next()) {
//				System.out.print("\t" + rs.getInt(1));
//				System.out.print("\t" + rs.getString(2));
//				System.out.print("\t\t" + rs.getString(3));
//				System.out.println("\t" + rs.getInt(4));
//			}

			// con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void sqlModify() {
		Statement stmt = null;

		try {
			System.out.println("Insert");
			stmt = con.createStatement(); // SQL문 처리용 Statement 객체 생성
			stmt.executeUpdate("insert into book values(11,'Korea History','Sejong Pub',20000);"); // 레코드 추가

			stmt.executeUpdate("update book set price=20000 where publisher='대한미디어'"); // 데이터 수정

			stmt.executeUpdate("delete from book where bookid=4"); // 레코드 삭제 // 외래키 조건을 제거해야 실행 가능 (현재는 실행 오류)

		} catch (SQLException e) {
			System.out.println("SQL 실행 오류");
		}
	}

}