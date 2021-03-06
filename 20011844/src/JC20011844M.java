import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class JC20011844M extends JFrame {
	LinkedList<String> tableNameList = new LinkedList<String>();
	

	JC20011844M() {
		tableNameList.add("Movie");
		tableNameList.add("Theater");
		tableNameList.add("Movietime");
		tableNameList.add("Seat");
		tableNameList.add("Customer");
		tableNameList.add("Reservation");
		tableNameList.add("Ticket");
		
		
		setTitle("20011844 안수경");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 2000);
		setVisible(true);
		
		Container c = getContentPane();
		c.setLayout(null);
		
		JButton manager = new JButton("관리자");
		
		manager.setSize(200, 100);
		
		manager.setLocation(100, 100);
		manager.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Manager();
			}
		});
		c.add(manager);
		
		JButton member = new JButton("회원");
		member.setSize(200, 100);
		member.setLocation(350, 100);
		member.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Member();
			}
		});
		c.add(member);
	}
	

	public static void main(String[] args) {
		new JC20011844M();


	}
	class Manager extends JFrame {
		JScrollPane jscp;
		Connection con;
		
		JTextArea ResultLabel = new JTextArea("");
		JTextField textfield = new JTextField("");
		int flag = 0;
		String tablename = "";
		String insertInfo = "";
		JLabel state = new JLabel("state: ");
		JLabel label = new JLabel("exeption: ");
		Manager(){
			
			setTitle("20011844 안수경 - 관리자");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(700, 2000);
			setVisible(true);
			Container c = getContentPane();
			String Driver = "";
			String url = "jdbc:mysql://localhost:3306/madang?&serverTimezone=Asia/Seoul";
			String userid = "madang";
			String pwd = "madang";
			c.add(label);
			label.setSize(600, 30);
			label.setLocation(10,610);
			try { /* 드라이버를 찾는 과정 */
				Class.forName("com.mysql.cj.jdbc.Driver");
				System.out.println("드라이버 로드 성공");
				label.setText("No exception!");
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
				label.setText(e.getMessage());
			}

			try {
				System.out.println("데이터베이스 연결 준비...");
				con = DriverManager.getConnection(url, userid, pwd);
				System.out.println("데이터베이스 연결 성공");
				label.setText("No exception!");
			} catch (SQLException e) {
				//e.printStackTrace();
				label.setText(e.getMessage());
			}
			// 초기 테이블 설정
			resetAll();
			
			c.setLayout(null);
			JButton reset = new JButton("reset");
			JButton search = new JButton("search");
			

			c.add(reset);
			reset.setSize(100, 40);
			reset.setLocation(50, 10);
			reset.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					resetAll();
					
				}
			});
		
			c.add(search);
			search.setSize(100, 40);
			search.setLocation(170, 10);
			search.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					searchAll();
				}
			});
		
			
		
			JButton button = new JButton("go");
			c.add(button);
			button.setSize(50, 30);
			button.setLocation(620, 70);
			// 입력, 삭제, 변경
			button.addActionListener(new ActionListener() {
		
				@Override
				public void actionPerformed(ActionEvent e) {
					if(textfield.getText().equals("")) {
						state.setText("please input!!");
						return;
					}
					String text = textfield.getText();
					String[] tmpText = text.split(" ");
					String tableText = tmpText[2];
					String tableText2 = tmpText[0];
					System.out.println(tableText);
					if (tableText2.equals("DELETE")) {
						tablename = textfield.getText();
						flag = 1;
						textfield.setText("");
						delete(text);
						state.setText("success");
					}
					else if (tableText2.equals("UPDATE")) {
						tablename = textfield.getText();
						flag = 1;
						textfield.setText("");
						update(text);
						state.setText("success");
					}
					else if (tableText2.equals("INSERT")) {
						tablename = textfield.getText();
						flag = 1;
						textfield.setText("");
						insert(text);
						state.setText("success");
					}
					/*else if(tableText.equals("Movie")||tableText.equals("Theater")||tableText.equals("Movietime")||tableText.equals("Seat")
							||tableText.equals("Customer")||tableText.equals("Reservation")||tableText.equals("Ticket")) {
						tablename = textfield.getText();
						flag = 1;
						textfield.setText("");
						insert(text);
						state.setText("success");
						//selectAll();
					}*/
					else {
						state.setText("input error");
						textfield.setText("");
						return;
					}
				}
			});
			
			jscp = new JScrollPane(ResultLabel);
			c.add(jscp);
			jscp.setSize(650, 485);
			jscp.setLocation(20, 130);
			
			c.add(state);
			state.setSize(100, 30);
			state.setLocation(300, 100);
			
			
			c.add(textfield);
			textfield.setSize(550, 30);
			textfield.setLocation(50, 70);
			
			c.add(state);
			state.setSize(100, 30);
			state.setLocation(300, 100);
			
			c.setFocusable(true);
			c.requestFocus();
		}
		void delete(String query) {
			System.out.println(query);
			try { /* 데이터베이스에 질의 결과를 가져오는 과정 */
				Statement stmt = con.createStatement();
				resetAll();
				stmt.execute("ALTER TABLE Movietime DROP FOREIGN KEY Movietime_ibfk_1;");
				stmt.execute("ALTER TABLE Movietime DROP INDEX movie_number;");
				stmt.execute("ALTER TABLE Movietime DROP FOREIGN KEY Movietime_ibfk_2;");
				stmt.execute("ALTER TABLE Movietime DROP INDEX theater_number;");
				
				stmt.execute("ALTER TABLE Seat DROP FOREIGN KEY Seat_ibfk_1;");
				stmt.execute("ALTER TABLE Seat DROP INDEX theater_number;");
				stmt.execute("ALTER TABLE Reservation DROP FOREIGN KEY Reservation_ibfk_1;");
				stmt.execute("ALTER TABLE Reservation DROP INDEX customer_id;");
				
				stmt.execute("ALTER TABLE Ticket DROP FOREIGN KEY Ticket_ibfk_1;");
				stmt.execute("ALTER TABLE Ticket DROP INDEX movietime_number;");
				stmt.execute("ALTER TABLE Ticket DROP FOREIGN KEY Ticket_ibfk_2;");
				stmt.execute("ALTER TABLE Ticket DROP INDEX theater_number;");
				stmt.execute("ALTER TABLE Ticket DROP FOREIGN KEY Ticket_ibfk_3;");
				stmt.execute("ALTER TABLE Ticket DROP INDEX seat_number;");
				stmt.execute("ALTER TABLE Ticket DROP FOREIGN KEY Ticket_ibfk_4;");
				
				stmt.execute("ALTER TABLE Ticket DROP INDEX reservation_number;");
				stmt.execute("ALTER TABLE Seat ADD FOREIGN KEY (theater_number) REFERENCES Theater(theater_number) ON DELETE SET NULL;");
				stmt.execute("ALTER TABLE Reservation ADD FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE SET NULL; ");
				stmt.execute("ALTER TABLE Movietime ADD FOREIGN KEY (movie_number) REFERENCES Movie(movie_number) ON DELETE SET NULL;");
				stmt.execute("ALTER TABLE Movietime ADD FOREIGN KEY (theater_number) REFERENCES Theater(theater_number) ON DELETE SET NULL;");
				stmt.execute("ALTER TABLE Ticket ADD FOREIGN KEY (movietime_number) REFERENCES Movietime(movietime_number) ON DELETE SET NULL; ");
				stmt.execute("ALTER TABLE Ticket ADD FOREIGN KEY (seat_number) REFERENCES Seat(seat_number) ON DELETE SET NULL; ");
				stmt.execute("ALTER TABLE Ticket ADD FOREIGN KEY (theater_number) REFERENCES Theater(theater_number) ON DELETE SET NULL; ");
				stmt.execute("ALTER TABLE Ticket ADD FOREIGN KEY (reservation_number) REFERENCES Reservation(reservation_number) ON DELETE SET NULL;");
				
				stmt.execute(query);
				System.out.println("insert success");
				label.setText("No exception!");
				
			} catch (SQLException e) {
				//e.printStackTrace();
				label.setText(e.getMessage());
			}
		}
		void update(String query) {
			System.out.println(query);
			try { /* 데이터베이스에 질의 결과를 가져오는 과정 */
				Statement stmt = con.createStatement();
				resetAll();
				stmt.execute("ALTER TABLE Movietime DROP FOREIGN KEY Movietime_ibfk_1;");
				stmt.execute("ALTER TABLE Movietime DROP INDEX movie_number;");
				stmt.execute("ALTER TABLE Movietime DROP FOREIGN KEY Movietime_ibfk_2;");
				stmt.execute("ALTER TABLE Movietime DROP INDEX theater_number;");
				
				stmt.execute("ALTER TABLE Seat DROP FOREIGN KEY Seat_ibfk_1;");
				stmt.execute("ALTER TABLE Seat DROP INDEX theater_number;");
				stmt.execute("ALTER TABLE Reservation DROP FOREIGN KEY Reservation_ibfk_1;");
				stmt.execute("ALTER TABLE Reservation DROP INDEX customer_id;");
				
				stmt.execute("ALTER TABLE Ticket DROP FOREIGN KEY Ticket_ibfk_1;");
				stmt.execute("ALTER TABLE Ticket DROP INDEX movietime_number;");
				stmt.execute("ALTER TABLE Ticket DROP FOREIGN KEY Ticket_ibfk_2;");
				stmt.execute("ALTER TABLE Ticket DROP INDEX theater_number;");
				stmt.execute("ALTER TABLE Ticket DROP FOREIGN KEY Ticket_ibfk_3;");
				stmt.execute("ALTER TABLE Ticket DROP INDEX seat_number;");
				stmt.execute("ALTER TABLE Ticket DROP FOREIGN KEY Ticket_ibfk_4;");
				stmt.execute("ALTER TABLE Ticket DROP INDEX reservation_number;");
				
				stmt.execute("ALTER TABLE Movietime ADD FOREIGN KEY (movie_number) REFERENCES Movie(movie_number) ON UPDATE SET NULL;");
				stmt.execute("ALTER TABLE Movietime ADD FOREIGN KEY (theater_number) REFERENCES Theater(theater_number) ON UPDATE SET NULL;");
				stmt.execute("ALTER TABLE Seat ADD FOREIGN KEY (theater_number) REFERENCES Theater(theater_number) ON UPDATE SET NULL;");
				stmt.execute("ALTER TABLE Reservation ADD FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON UPDATE SET NULL; ");
				stmt.execute("ALTER TABLE Ticket ADD FOREIGN KEY (movietime_number) REFERENCES Movietime(movietime_number) ON UPDATE SET NULL; ");
				stmt.execute("ALTER TABLE Ticket ADD FOREIGN KEY (seat_number) REFERENCES Seat(seat_number) ON UPDATE SET NULL; ");
				stmt.execute("ALTER TABLE Ticket ADD FOREIGN KEY (theater_number) REFERENCES Theater(theater_number) ON UPDATE SET NULL; ");
				stmt.execute("ALTER TABLE Ticket ADD FOREIGN KEY (reservation_number) REFERENCES Reservation(reservation_number) ON UPDATE SET NULL;");
				stmt.execute(query);
				System.out.println("insert success");
				label.setText("No exception!");
				
			} catch (SQLException e) {
				
				//e.printStackTrace();
				label.setText(e.getMessage());
			}
		}
		void insert(String query) {
			
			System.out.println(query);
			try { /* 데이터베이스에 질의 결과를 가져오는 과정 */
				Statement stmt = con.createStatement();
				stmt.execute(query);
				System.out.println("insert success");
				label.setText("No exception!");
				
			} catch (SQLException e) {
				//e.printStackTrace();
				label.setText(e.getMessage());
			}
		}
		void resetAll() {
			String reset_query = "DROP TABLE IF EXISTS ";
			
		
			try {
				Statement stmt = con.createStatement();
				for(int i=tableNameList.size()-1;i>=0;i--) {
					stmt.executeUpdate(reset_query+tableNameList.get(i));
				}
				
				tableNameList.add("Movie");
				tableNameList.add("Theater");
				tableNameList.add("Movietime");
				tableNameList.add("Seat");
				tableNameList.add("Customer");
				tableNameList.add("Reservation");
				tableNameList.add("Ticket");
				
				// create table
				stmt.executeUpdate("CREATE TABLE Movie (\r\n"
						+ "	movie_number INTEGER PRIMARY KEY,\r\n"
						+ "    movie_name VARCHAR(50) ,\r\n"
						+ "    movie_time TIME ,\r\n"
						+ "    movie_grade INTEGER,\r\n"
						+ "    movie_director VARCHAR(50),\r\n"
						+ "    movie_actor VARCHAR(50),\r\n"
						+ "    movie_genre VARCHAR(50),\r\n"
						+ "    movie_introduce VARCHAR(100),\r\n"
						+ "    movie_date DATE\r\n"
						+ ");");
				stmt.executeUpdate("CREATE TABLE Theater(\r\n"
						+ "	theater_number INTEGER PRIMARY KEY,\r\n"
						+ "    theater_seatN INTEGER,\r\n"
						+ "    theater_use BOOLEAN\r\n"
						+ ");");
				stmt.executeUpdate("CREATE TABLE Movietime(\r\n"
						+ "	movietime_number INTEGER PRIMARY KEY,\r\n"
						+ "    movie_number INTEGER,\r\n"
						+ "    theater_number INTEGER,\r\n"
						+ "    movie_firstday DATE,\r\n"
						+ "    movie_day VARCHAR(20),\r\n"
						+ "    movie_n INTEGER,\r\n"
						+ "    movie_startTime TIME,\r\n"
						+ "    FOREIGN KEY (movie_number) REFERENCES Movie(movie_number),\r\n"
						+ "    FOREIGN KEy (theater_number) REFERENCES Theater(theater_number)\r\n"
						+ ");");
				stmt.executeUpdate("CREATE TABLE Seat(\r\n"
						+ "	seat_number INTEGER PRIMARY KEY,\r\n"
						+ "    theater_number INTEGER,\r\n"
						+ "    seat_use BOOLEAN,\r\n"
						+ "    FOREIGN KEy (theater_number) REFERENCES Theater(theater_number)\r\n"
						+ ");");
				stmt.executeUpdate("CREATE TABLE Customer(\r\n"
						+ "	customer_id VARCHAR(100) PRIMARY KEY,\r\n"
						+ "    customer_name VARCHAR(50),\r\n"
						+ "    customer_phone VARCHAR(15),\r\n"
						+ "    customer_email VARCHAR(50)\r\n"
						+ ");");
				stmt.executeUpdate("CREATE TABLE Reservation(\r\n"
						+ "	reservation_number INTEGER PRIMARY KEY,\r\n"
						+ "    payment_option VARCHAR(20),\r\n"
						+ "    payment_status BOOLEAN,\r\n"
						+ "    payment_price INTEGER,\r\n"
						+ "    customer_id VARCHAR(100),\r\n"
						+ "    payment_date DATE,\r\n"
						+ "    FOREIGN KEy (customer_id) REFERENCES Customer(customer_id)\r\n"
						+ ");");
				stmt.executeUpdate("CREATE TABLE Ticket(\r\n"
						+ "	ticket_number INTEGER PRIMARY KEY,\r\n"
						+ "    movietime_number INTEGER,\r\n"
						+ "    theater_number INTEGER,\r\n"
						+ "    seat_number INTEGER,\r\n"
						+ "    reservation_number INTEGER,\r\n"
						+ "    ticket_out BOOLEAN,\r\n"
						+ "    ticket_avgPrice INTEGER,\r\n"
						+ "    ticket_price INTEGER,\r\n"
						+ "    \r\n"
						+ "    FOREIGN KEY (movietime_number) REFERENCES Movietime(movietime_number),\r\n"
						+ "    FOREIGN KEy (theater_number) REFERENCES Theater(theater_number),\r\n"
						+ "    FOREIGN KEy (seat_number) REFERENCES Seat(seat_number),\r\n"
						+ "    FOREIGN KEy (reservation_number) REFERENCES Reservation(reservation_number)\r\n"
						+ ");");
				
			
						stmt.executeUpdate("INSERT INTO Movie VALUES(1, 'movie_1', '02:01:00' , 15, 'director_1', 'actor_1', 'romance', '소개소개', STR_TO_DATE('2021-01-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Movie VALUES(2, 'movie_2', '02:02:00' , 15, 'director_2', 'actor_2', 'thriller', '소개소개', STR_TO_DATE('2021-02-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Movie VALUES(3, 'movie_3', '02:03:00' , 15, 'director_3', 'actor_3', 'comedy', '소개소개', STR_TO_DATE('2021-03-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Movie VALUES(4, 'movie_4', '02:04:00' , 15, 'director_4', 'actor_4', 'action', '소개소개', STR_TO_DATE('2021-04-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Movie VALUES(5, 'movie_5', '02:05:00' , 15, 'director_5', 'actor_5', 'animation', '소개소개', STR_TO_DATE('2021-05-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Movie VALUES(6, 'movie_6', '02:06:00' , 15, 'director_6', 'actor_6', 'horror', '소개소개', STR_TO_DATE('2021-06-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Movie VALUES(7, 'movie_7', '02:07:00' , 19, 'director_7', 'actor_7', 'SF', '소개소개', STR_TO_DATE('2021-07-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Movie VALUES(8, 'movie_8', '02:08:00' , 19, 'director_8', 'actor_8', 'fantasy', '소개소개', STR_TO_DATE('2021-08-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Movie VALUES(9, 'movie_9', '02:09:00' , 19, 'director_9', 'actor_9', 'adventure', '소개소개', STR_TO_DATE('2021-09-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Movie VALUES(10, 'movie_10', '02:10:00' , 19, 'director_10', 'actor_10', 'documentary', '소개소개', STR_TO_DATE('2021-10-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Movie VALUES(11, 'movie_11', '02:11:00' , 19, 'director_11', 'actor_11', 'historical', '소개소개', STR_TO_DATE('2021-11-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Movie VALUES(12, 'movie_12', '02:12:00' , 19, 'director_12', 'actor_12', 'mystery', '소개소개', STR_TO_DATE('2021-12-01','%Y-%m-%d'));");
						
						stmt.executeUpdate("INSERT INTO Theater VALUES(1, 10, TRUE);");
						stmt.executeUpdate("INSERT INTO Theater VALUES(2, 10, TRUE);");
						stmt.executeUpdate("INSERT INTO Theater VALUES(3, 10, TRUE);");
						stmt.executeUpdate("INSERT INTO Theater VALUES(4, 10, TRUE);");
						stmt.executeUpdate("INSERT INTO Theater VALUES(5, 10, TRUE);");
						stmt.executeUpdate("INSERT INTO Theater VALUES(6, 10, TRUE);");
						stmt.executeUpdate("INSERT INTO Theater VALUES(7, 10, TRUE);");
						stmt.executeUpdate("INSERT INTO Theater VALUES(8, 10, TRUE);");
						stmt.executeUpdate("INSERT INTO Theater VALUES(9, 10, TRUE);");
						stmt.executeUpdate("INSERT INTO Theater VALUES(10, 10, TRUE);");
						stmt.executeUpdate("INSERT INTO Theater VALUES(11, 10, TRUE);");
						stmt.executeUpdate("INSERT INTO Theater VALUES(12, 10, TRUE);");
						
						stmt.executeUpdate("INSERT INTO Movietime VALUES(1, 1, 1, STR_TO_DATE('2021-01-01','%Y-%m-%d'), 'Friday', '1', '09:00:00');");
						stmt.executeUpdate("INSERT INTO Movietime VALUES(2, 2, 1, STR_TO_DATE('2021-02-01','%Y-%m-%d'), 'Friday', '1', '09:00:00');");
						stmt.executeUpdate("INSERT INTO Movietime VALUES(3, 3, 1, STR_TO_DATE('2021-03-01','%Y-%m-%d'), 'Friday', '1', '09:00:00');");
						stmt.executeUpdate("INSERT INTO Movietime VALUES(4, 4, 1, STR_TO_DATE('2021-04-01','%Y-%m-%d'), 'Friday', '1', '09:00:00');");
						stmt.executeUpdate("INSERT INTO Movietime VALUES(5, 5, 1, STR_TO_DATE('2021-05-01','%Y-%m-%d'), 'Friday', '1', '09:00:00');");
						stmt.executeUpdate("INSERT INTO Movietime VALUES(6, 6, 1, STR_TO_DATE('2021-06-01','%Y-%m-%d'), 'Friday', '1', '09:00:00');");
						stmt.executeUpdate("INSERT INTO Movietime VALUES(7, 7, 1, STR_TO_DATE('2021-07-01','%Y-%m-%d'), 'Friday', '1', '09:00:00');");
						stmt.executeUpdate("INSERT INTO Movietime VALUES(8, 8, 1, STR_TO_DATE('2021-08-01','%Y-%m-%d'), 'Friday', '1', '09:00:00');");
						stmt.executeUpdate("INSERT INTO Movietime VALUES(9, 9, 1, STR_TO_DATE('2021-09-01','%Y-%m-%d'), 'Friday', '1', '09:00:00');");
						stmt.executeUpdate("INSERT INTO Movietime VALUES(10, 10, 1, STR_TO_DATE('2021-10-01','%Y-%m-%d'), 'Friday', '1', '09:00:00');");
						stmt.executeUpdate("INSERT INTO Movietime VALUES(11, 11, 1, STR_TO_DATE('2021-11-01','%Y-%m-%d'), 'Friday', '1', '09:00:00');");
						stmt.executeUpdate("INSERT INTO Movietime VALUES(12, 12, 1, STR_TO_DATE('2021-12-01','%Y-%m-%d'), 'Friday', '1', '09:00:00');");
						
						stmt.executeUpdate("INSERT INTO Seat VALUES(11, 1,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(12, 1,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(13, 1,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(14, 1,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(15, 1,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(16, 1,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(17, 1,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(18, 1,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(19, 1,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(110, 1,FALSE);");
						
						stmt.executeUpdate("INSERT INTO Seat VALUES(21, 2,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(22, 2,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(23, 2,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(24, 2,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(25, 2,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(26, 2,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(27, 2,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(28, 2,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(29, 2,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(210, 2,FALSE);");
						
						stmt.executeUpdate("INSERT INTO Seat VALUES(31, 3,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(32, 3,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(33, 3,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(34, 3,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(35, 3,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(36, 3,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(37, 3,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(38, 3,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(39, 3,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(310, 3,FALSE);");
						
						stmt.executeUpdate("INSERT INTO Seat VALUES(41, 4,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(42, 4,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(43, 4,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(44, 4,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(45, 4,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(46, 4,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(47, 4,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(48, 4,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(49, 4,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(410, 4,FALSE);");
						
						stmt.executeUpdate("INSERT INTO Seat VALUES(51, 5,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(52, 5,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(53, 5,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(54, 5,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(55, 5,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(56, 5,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(57, 5,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(58, 5,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(59, 5,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(510, 5,FALSE);");
						
						stmt.executeUpdate("INSERT INTO Seat VALUES(61, 6,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(62, 6,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(63, 6,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(64, 6,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(65, 6,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(66, 6,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(67, 6,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(68, 6,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(69, 6,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(610, 6,FALSE);");
						
						stmt.executeUpdate("INSERT INTO Seat VALUES(71, 7,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(72, 7,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(73, 7,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(74, 7,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(75, 7,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(76, 7,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(77, 7,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(78, 7,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(79, 7,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(710, 7,FALSE);");
						
						stmt.executeUpdate("INSERT INTO Seat VALUES(81, 8,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(82, 8,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(83, 8,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(84, 8,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(85, 8,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(86, 8,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(87, 8,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(88, 8,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(89, 8,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(810, 8,FALSE);");
						
						stmt.executeUpdate("INSERT INTO Seat VALUES(91, 9,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(92, 9,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(93, 9,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(94, 9,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(95, 9,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(96, 9,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(97, 9,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(98, 9,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(99, 9,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(910, 9,FALSE);");
						
						stmt.executeUpdate("INSERT INTO Seat VALUES(101, 10,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(102, 10,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(103, 10,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(104, 10,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(105, 10,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(106, 10,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(107, 10,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(108, 10,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(109, 10,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(1010, 10,FALSE);");
						
						stmt.executeUpdate("INSERT INTO Seat VALUES(111, 11,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(112, 11,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(113, 11,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(114, 11,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(115, 11,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(116, 11,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(117, 11,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(118, 11,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(119, 11,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(1110, 11,FALSE);");
						
						stmt.executeUpdate("INSERT INTO Seat VALUES(121, 12,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(122, 12,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(123, 12,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(124, 12,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(125, 12,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(126, 12,TRUE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(127, 12,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(128, 12,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(129, 12,FALSE);");
						stmt.executeUpdate("INSERT INTO Seat VALUES(1210, 12,FALSE);");

						/* Customer */
						
						stmt.executeUpdate("INSERT INTO Customer VALUES('customer_1', '이름_1', '010-0000-0001', 'customer_1@gmail.com');");
						stmt.executeUpdate("INSERT INTO Customer VALUES('customer_2', '이름_2', '010-0000-0002', 'customer_2@gmail.com');");
						stmt.executeUpdate("INSERT INTO Customer VALUES('customer_3', '이름_3', '010-0000-0003', 'customer_3@gmail.com');");
						stmt.executeUpdate("INSERT INTO Customer VALUES('customer_4', '이름_4', '010-0000-0004', 'customer_4@gmail.com');");
						
						
						stmt.executeUpdate("INSERT INTO Reservation VALUES(11, 'credit', TRUE, 12000, 'customer_1', STR_TO_DATE('2021-01-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(16, 'cash', TRUE, 9000, 'customer_2', STR_TO_DATE('2021-01-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(21, 'credit', TRUE, 12000, 'customer_1', STR_TO_DATE('2021-02-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(26, 'cash', TRUE, 9000, 'customer_2', STR_TO_DATE('2021-02-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(31, 'credit', TRUE, 12000, 'customer_1', STR_TO_DATE('2021-03-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(36, 'cash', TRUE, 9000, 'customer_2', STR_TO_DATE('2021-03-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(41, 'credit', TRUE, 12000, 'customer_1', STR_TO_DATE('2021-04-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(46, 'cash', TRUE, 9000, 'customer_2', STR_TO_DATE('2021-04-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(51, 'credit', TRUE, 12000, 'customer_1', STR_TO_DATE('2021-05-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(56, 'cash', TRUE, 9000, 'customer_2', STR_TO_DATE('2021-05-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(61, 'credit', TRUE, 12000, 'customer_1', STR_TO_DATE('2021-06-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(66, 'cash', TRUE, 9000, 'customer_2', STR_TO_DATE('2021-06-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(71, 'credit', TRUE, 12000, 'customer_3', STR_TO_DATE('2021-07-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(76, 'cash', TRUE, 9000, 'customer_4', STR_TO_DATE('2021-07-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(81, 'credit', TRUE, 12000, 'customer_3', STR_TO_DATE('2021-08-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(86, 'cash', TRUE, 9000, 'customer_4', STR_TO_DATE('2021-08-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(91, 'credit', TRUE, 12000, 'customer_3', STR_TO_DATE('2021-09-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(96, 'cash', TRUE, 9000, 'customer_4', STR_TO_DATE('2021-09-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(101, 'credit', TRUE, 12000, 'customer_3', STR_TO_DATE('2021-10-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(106, 'cash', TRUE, 9000, 'customer_4', STR_TO_DATE('2021-10-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(111, 'credit', TRUE, 12000, 'customer_3', STR_TO_DATE('2021-11-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(116, 'cash', TRUE, 9000, 'customer_4', STR_TO_DATE('2021-11-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(121, 'credit', TRUE, 12000, 'customer_3', STR_TO_DATE('2021-12-01','%Y-%m-%d'));");
						stmt.executeUpdate("INSERT INTO Reservation VALUES(126, 'cash', TRUE, 9000, 'customer_4', STR_TO_DATE('2021-12-01','%Y-%m-%d'));");
						
						stmt.executeUpdate("INSERT INTO Ticket VALUES(1, 1, 1, 11, 11, TRUE, 12000, 12000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(2, 1, 1, 12, NULL, FALSE, 12000, 11000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(3, 1, 1, 13, NULL, FALSE, 12000, 10000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(4, 1, 1, 14, NULL, FALSE, 12000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(5, 1, 1, 15, NULL, FALSE, 12000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(6, 1, 1, 16, 16, TRUE, 9000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(7, 1, 1, 17, NULL, FALSE, 9000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(8, 1, 1, 18, NULL, FALSE, 9000, 7000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(9, 1, 1, 19, NULL, FALSE, 9000, 6000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(10, 1, 1, 110, NULL, FALSE, 9000, 5000);");
						
						stmt.executeUpdate("INSERT INTO Ticket VALUES(11, 2, 2, 21, 21, TRUE, 12000, 12000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(12, 2, 2, 22, NULL, FALSE, 12000, 11000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(13, 2, 2, 23, NULL, FALSE, 12000, 10000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(14, 2, 2, 24, NULL, FALSE, 12000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(15, 2, 2, 25, NULL, FALSE, 12000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(16, 2, 2, 26, 26, TRUE, 9000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(17, 2, 2, 27, NULL, FALSE, 9000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(18, 2, 2, 28, NULL, FALSE, 9000, 7000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(19, 2, 2, 29, NULL, FALSE, 9000, 6000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(20, 2, 2, 210, NULL, FALSE, 9000, 5000);");
						
						stmt.executeUpdate("INSERT INTO Ticket VALUES(21, 3, 3, 31, 31, TRUE, 12000, 12000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(22, 3, 3, 32, NULL, FALSE, 12000, 11000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(23, 3, 3, 33, NULL, FALSE, 12000, 10000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(24, 3, 3, 34, NULL, FALSE, 12000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(25, 3, 3, 35, NULL, FALSE, 12000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(26, 3, 3, 36, 36, TRUE, 9000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(27, 3, 3, 37, NULL, FALSE, 9000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(28, 3, 3, 38, NULL, FALSE, 9000, 7000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(29, 3, 3, 39, NULL, FALSE, 9000, 6000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(30, 3, 3, 310, NULL, FALSE, 9000, 5000);");
						
						stmt.executeUpdate("INSERT INTO Ticket VALUES(31, 4, 4, 41, 41, TRUE, 12000, 12000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(32, 4, 4, 42, NULL, FALSE, 12000, 11000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(33, 4, 4, 43, NULL, FALSE, 12000, 10000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(34, 4, 4, 44, NULL, FALSE, 12000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(35, 4, 4, 45, NULL, FALSE, 12000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(36, 4, 4, 46, 46, TRUE, 9000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(37, 4, 4, 47, NULL, FALSE, 9000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(38, 4, 4, 48, NULL, FALSE, 9000, 7000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(39, 4, 4, 49, NULL, FALSE, 9000, 6000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(40, 4, 4, 410, NULL, FALSE, 9000, 5000);");
						
						stmt.executeUpdate("INSERT INTO Ticket VALUES(41, 5, 5, 51, 51, TRUE, 12000, 12000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(42, 5, 5, 52, NULL, FALSE, 12000, 11000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(43, 5, 5, 53, NULL, FALSE, 12000, 10000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(44, 5, 5, 54, NULL, FALSE, 12000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(45, 5, 5, 55, NULL, FALSE, 12000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(46, 5, 5, 56, 56, TRUE, 9000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(47, 5, 5, 57, NULL, FALSE, 9000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(48, 5, 5, 58, NULL, FALSE, 9000, 7000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(49, 5, 5, 59, NULL, FALSE, 9000, 6000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(50, 5, 5, 510, NULL, FALSE, 9000, 5000);");
						
						stmt.executeUpdate("INSERT INTO Ticket VALUES(51, 6, 6, 61, 61, TRUE, 12000, 12000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(52, 6, 6, 62, NULL, FALSE, 12000, 11000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(53, 6, 6, 63, NULL, FALSE, 12000, 10000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(54, 6, 6, 64, NULL, FALSE, 12000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(55, 6, 6, 65, NULL, FALSE, 12000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(56, 6, 6, 66, 66, TRUE, 9000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(57, 6, 6, 67, NULL, FALSE, 9000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(58, 6, 6, 68, NULL, FALSE, 9000, 7000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(59, 6, 6, 69, NULL, FALSE, 9000, 6000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(60, 6, 6, 610, NULL, FALSE, 9000, 5000);");
						
						stmt.executeUpdate("INSERT INTO Ticket VALUES(61, 7, 7, 71, 71, TRUE, 12000, 12000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(62, 7, 7, 72, NULL, FALSE, 12000, 11000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(63, 7, 7, 73, NULL, FALSE, 12000, 10000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(64, 7, 7, 74, NULL, FALSE, 12000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(65, 7, 7, 75, NULL, FALSE, 12000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(66, 7, 7, 76, 76, TRUE, 9000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(67, 7, 7, 77, NULL, FALSE, 9000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(68, 7, 7, 78, NULL, FALSE, 9000, 7000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(69, 7, 7, 79, NULL, FALSE, 9000, 6000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(70, 7, 7, 710, NULL, FALSE, 9000, 5000);");
						
						stmt.executeUpdate("INSERT INTO Ticket VALUES(71, 8, 8, 81, 81, TRUE, 12000, 12000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(72, 8, 8, 82, NULL, FALSE, 12000, 11000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(73, 8, 8, 83, NULL, FALSE, 12000, 10000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(74, 8, 8, 84, NULL, FALSE, 12000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(75, 8, 8, 85, NULL, FALSE, 12000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(76, 8, 8, 86, 86, TRUE, 9000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(77, 8, 8, 87, NULL, FALSE, 9000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(78, 8, 8, 88, NULL, FALSE, 9000, 7000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(79, 8, 8, 89, NULL, FALSE, 9000, 6000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(80, 8, 8, 810, NULL, FALSE, 9000, 5000);");
						
						stmt.executeUpdate("INSERT INTO Ticket VALUES(81, 9, 9, 91, 91, TRUE, 12000, 12000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(82, 9, 9, 92, NULL, FALSE, 12000, 11000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(83, 9, 9, 93, NULL, FALSE, 12000, 10000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(84, 9, 9, 94, NULL, FALSE, 12000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(85, 9, 9, 95, NULL, FALSE, 12000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(86, 9, 9, 96, 96, TRUE, 9000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(87, 9, 9, 97, NULL, FALSE, 9000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(88, 9, 9, 98, NULL, FALSE, 9000, 7000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(89, 9, 9, 99, NULL, FALSE, 9000, 6000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(90, 9, 9, 910, NULL, FALSE, 9000, 5000);");
						
						stmt.executeUpdate("INSERT INTO Ticket VALUES(91, 10, 10, 101, 101, TRUE, 12000, 12000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(92, 10, 10, 102, NULL, FALSE, 12000, 11000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(93, 10, 10, 103, NULL, FALSE, 12000, 10000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(94, 10, 10, 104, NULL, FALSE, 12000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(95, 10, 10, 105, NULL, FALSE, 12000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(96, 10, 10, 106, 106, TRUE, 9000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(97, 10, 10, 107, NULL, FALSE, 9000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(98, 10, 10, 108, NULL, FALSE, 9000, 7000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(99, 10, 10, 109, NULL, FALSE, 9000, 6000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(100, 10, 10, 1010, NULL, FALSE, 9000, 5000);");
						
						stmt.executeUpdate("INSERT INTO Ticket VALUES(101, 11, 11, 111, 111, TRUE, 12000, 12000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(102, 11, 11, 112, NULL, FALSE, 12000, 11000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(103, 11, 11, 113, NULL, FALSE, 12000, 10000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(104, 11, 11, 114, NULL, FALSE, 12000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(105, 11, 11, 115, NULL, FALSE, 12000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(106, 11, 11, 116, 116, TRUE, 9000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(107, 11, 11, 117, NULL, FALSE, 9000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(108, 11, 11, 118, NULL, FALSE, 9000, 7000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(109, 11, 11, 119, NULL, FALSE, 9000, 6000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(110, 11, 11, 1110, NULL, FALSE, 9000, 5000);");
						
						stmt.executeUpdate("INSERT INTO Ticket VALUES(111, 12, 12, 121, 121, TRUE, 12000, 12000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(112, 12, 12, 122, NULL, FALSE, 12000, 11000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(113, 12, 12, 123, NULL, FALSE, 12000, 10000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(114, 12, 12, 124, NULL, FALSE, 12000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(115, 12, 12, 125, NULL, FALSE, 12000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(116, 12, 12, 126, 126, TRUE, 9000, 9000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(117, 12, 12, 127, NULL, FALSE, 9000, 8000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(118, 12, 12, 128, NULL, FALSE, 9000, 7000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(119, 12, 12, 129, NULL, FALSE, 9000, 6000);");
						stmt.executeUpdate("INSERT INTO Ticket VALUES(120, 12, 12, 1210, NULL, FALSE, 9000, 5000);");	
						
						label.setText("No exception!");
		
			} catch (SQLException e) {
				//e.printStackTrace();
				label.setText(e.getMessage());
			}
		}
		void searchAll() {
			
			try { /* 데이터베이스에 질의 결과를 가져오는 과정 */
				Statement stmt = con.createStatement();	
				String resultStr = "";
				
				for(int i=0;i<tableNameList.size();i++) {
					ResultSet rs = stmt.executeQuery("SELECT * FROM "+tableNameList.get(i));
					ResultSetMetaData rsmd = rs.getMetaData();
					String columnStr = tableNameList.get(i)+"\n";
					for(int j=1;j<=rsmd.getColumnCount();j++) {
						columnStr += rsmd.getColumnName(j)+"\t";
					}
					columnStr += "\n";
					while(rs.next()) {
						
						for(int k=1;k<=rsmd.getColumnCount();k++) {
							System.out.println(rsmd.getColumnTypeName(k));
							if(rsmd.getColumnTypeName(k).equals("VARCHAR") || rsmd.getColumnTypeName(k).equals("DATE")||rsmd.getColumnTypeName(k).equals("TIME")) {
								columnStr += rs.getString(k) + "\t";
							}
							else if(rsmd.getColumnTypeName(k).equals("INT")) {
								if (Integer.toString(rs.getInt(k)).equals("0"))  columnStr +="null\t";
								else columnStr += Integer.toString(rs.getInt(k)) + "\t";
							}
							else if(rsmd.getColumnTypeName(k).equals("BIT")) {
								columnStr += Boolean.toString(rs.getBoolean(k)) + "\t";
							}
						}
						columnStr += "\n";
					}

					columnStr += "\n\n\n";

					resultStr += columnStr + "\n\n\n";
				}
				ResultLabel.setText(resultStr);
				label.setText("No exception!");
				
			} catch (SQLException e) {
				//e.printStackTrace();
				label.setText(e.getMessage());
			}
		}
		
	}


	class Member extends JFrame {
		Container c;
		String customer_id = "customer_1";
		Connection con;
		JLabel label_1 = new JLabel("영화명");
		JLabel label_2 = new JLabel("감독명");
		JLabel label_3 = new JLabel("배우명");
		JLabel label_4 = new JLabel("장르명");
		JTextField textfield_1 = new JTextField("");
		JTextField textfield_2 = new JTextField("");
		JTextField textfield_3 = new JTextField("");
		JTextField textfield_4 = new JTextField("");
		
		JTextArea ResultLabel = new JTextArea("");
		JTextField textfield = new JTextField("");

		Statement stmt;
		ResultSet rs;
		
		JTable table;
		JTable reservation_table;
		JScrollPane jscp;
		JScrollPane reservation_jscp;
		JTable update_movie_reservation_table;
		JScrollPane update_movie_reservation_jscp;
		JTable update_movietime_reservation_table;
		JScrollPane update_movietime_reservation_jscp;
		
		
		JLabel exep = new JLabel("exeption: ");
		
		LinkedList<Integer> reservation_numbers;
		
		
		
		
		Member(){
			setTitle("20011844 안수경 - 회원");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(700, 2000);
			setVisible(true);
			c = getContentPane();
			String Driver = "";
			String url = "jdbc:mysql://localhost:3306/madang?&serverTimezone=Asia/Seoul";
			String userid = "madang";
			String pwd = "madang";
			c.add(exep);
			exep.setSize(600, 30);
			exep.setLocation(10,610);
			try { /* 드라이버를 찾는 과정 */
				Class.forName("com.mysql.cj.jdbc.Driver");
				System.out.println("드라이버 로드 성공");
				exep.setText("No exception!");
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
				exep.setText(e.getMessage());
			}

			try {
				System.out.println("데이터베이스 연결 준비...");
				con = DriverManager.getConnection(url, userid, pwd);
				System.out.println("데이터베이스 연결 성공");
				exep.setText("No exception!");
			} catch (SQLException e) {
				//e.printStackTrace();
				exep.setText(e.getMessage());
			}
			c.setLayout(null);
			
			c.add(textfield_1);
			textfield_1.setSize(100, 30);
			textfield_1.setLocation(50, 10);
			c.add(label_1);
			label_1.setSize(100, 40);
			label_1.setLocation(80, 30);
		
			c.add(textfield_2);
			textfield_2.setSize(100, 30);
			textfield_2.setLocation(160, 10);
			c.add(label_2);
			label_2.setSize(100, 40);
			label_2.setLocation(190, 30);
			
			c.add(textfield_3);
			textfield_3.setSize(100, 30);
			textfield_3.setLocation(270, 10);
			c.add(label_3);
			label_3.setSize(100, 40);
			label_3.setLocation(300, 30);
			
			c.add(textfield_4);
			textfield_4.setSize(100, 30);
			textfield_4.setLocation(380, 10);
			c.add(label_4);
			label_4.setSize(100, 40);
			label_4.setLocation(410, 30);
			
			
			JButton search = new JButton("search");
			c.add(search);
			search.setSize(100, 40);
			search.setLocation(500, 10);
			search.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					searchAllMovie();
				}
			});
			reservation_jscp = new JScrollPane();
			c.add(reservation_jscp);
			reservation_jscp.setSize(650, 150);
			reservation_jscp.setLocation(20, 280);
			jscp = new JScrollPane();
			c.add(jscp);
			jscp.setSize(650, 150);
			jscp.setLocation(20, 70);
			
			
			JLabel reservation_label = new JLabel(customer_id+"님의 예매 내역");
			c.add(reservation_label);
			reservation_label.setSize(150, 30);
			reservation_label.setLocation(50, 240);
			
			JButton r_delete = new JButton("예매 정보 삭제");
			r_delete.setSize(130, 30);
			r_delete.setLocation(210, 240);
			c.add(r_delete);
			r_delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						stmt = con.createStatement();
						String movie_name = (String) reservation_table.getValueAt(reservation_table.getSelectedRow(), 0);
						String movie_date = (String) reservation_table.getValueAt(reservation_table.getSelectedRow(), 1);
						int theater_number = Integer.parseInt((String) reservation_table.getValueAt(reservation_table.getSelectedRow(), 2));
						int seat_number = Integer.parseInt((String) reservation_table.getValueAt(reservation_table.getSelectedRow(), 3));
						int ticket_price = Integer.parseInt((String) reservation_table.getValueAt(reservation_table.getSelectedRow(), 4));
						
						rs = stmt.executeQuery("select ticket.ticket_number, reservation.reservation_number from  movie, ticket, reservation\r\n"
								+ "where movie.movie_name = '"+movie_name+"' and movie.movie_date = '"+movie_date+"' and ticket.theater_number = "+theater_number+" and ticket.seat_number = "+seat_number+" and ticket.ticket_price = "+ticket_price+" and reservation.reservation_number = ticket.reservation_number;");
						rs.next();
						int ticket_number = rs.getInt(1);
						int reservation_number = rs.getInt(2);
						stmt.executeUpdate("update ticket set reservation_number = NULL, ticket_out = FALSE where ticket_number = "+ticket_number+";");
						stmt.executeUpdate("delete from reservation where customer_id = '"+customer_id+"' and reservation_number = "+reservation_number+";");
						stmt.executeUpdate("update seat set seat_use = FALSE where seat_number = "+seat_number+";");
						searchAllReservation();
						exep.setText("No exception!");
						
					} catch (SQLException e1) {
						//e1.printStackTrace();
						exep.setText(e1.getMessage());
					}
				}
				
			});
			JButton r_movie_update = new JButton("예매 영화 변경");
			r_movie_update.setSize(130, 30);
			r_movie_update.setLocation(350, 240);
			c.add(r_movie_update);
			r_movie_update.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						stmt = con.createStatement();
						String movie_name = (String) reservation_table.getValueAt(reservation_table.getSelectedRow(), 0);
						String movie_date = (String) reservation_table.getValueAt(reservation_table.getSelectedRow(), 1);
						int theater_number = Integer.parseInt((String) reservation_table.getValueAt(reservation_table.getSelectedRow(), 2));
						int seat_number = Integer.parseInt((String) reservation_table.getValueAt(reservation_table.getSelectedRow(), 3));
						int ticket_price = Integer.parseInt((String) reservation_table.getValueAt(reservation_table.getSelectedRow(), 4));
						
						rs = stmt.executeQuery("select * from ticket where ticket_number in (select reservation_number from reservation where customer_id = '"+customer_id+"')\r\n"
								+ "and\r\n"
								+ "ticket_price = "+ticket_price+" and seat_number = "+seat_number+" and theater_number = "+theater_number+";");
						rs.next();
						
						int ticket_number = rs.getInt(1);
						
						
						new MovieUpdateReservation(ticket_number);
						
						searchAllReservation();
						exep.setText("No exception!");
						
					} catch (SQLException e1) {
						//e1.printStackTrace();
						exep.setText(e1.getMessage());
					}
				}
				
			});
			JButton r_movietime_update = new JButton("예매 상영 일정 변경");
			r_movietime_update.setSize(150, 30);
			r_movietime_update.setLocation(490, 240);
			c.add(r_movietime_update);
			r_movietime_update.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
						try {
							stmt = con.createStatement();
							String movie_name = (String) reservation_table.getValueAt(reservation_table.getSelectedRow(), 0);
							String movie_date = (String) reservation_table.getValueAt(reservation_table.getSelectedRow(), 1);
							int theater_number = Integer.parseInt((String) reservation_table.getValueAt(reservation_table.getSelectedRow(), 2));
							int seat_number = Integer.parseInt((String) reservation_table.getValueAt(reservation_table.getSelectedRow(), 3));
							int ticket_price = Integer.parseInt((String) reservation_table.getValueAt(reservation_table.getSelectedRow(), 4));
							
							
							rs = stmt.executeQuery("select * from ticket where ticket_number in (select reservation_number from reservation where customer_id = '"+customer_id+"')\r\n"
								+ "and\r\n"
								+ "ticket_price = "+ticket_price+" and seat_number = "+seat_number+" and theater_number = "+theater_number+";");
							rs.next();
							
							int ticket_number = rs.getInt(1);
							int movietime_number = rs.getInt(2);
							
							rs = stmt.executeQuery("select movie_number from movietime where movietime_number = "+movietime_number+";");
							rs.next();
							int movie_number = rs.getInt(1);
							
							new MovieTimeUpdateReservation(ticket_number, movietime_number, movie_number);
							
							searchAllReservation();
							exep.setText("No exception!");
							
						} catch (SQLException e1) {
							//e1.printStackTrace();
							exep.setText(e1.getMessage());
						}	
					} 
			});			
			
			searchAllReservation();
		}
		
		void searchAllReservation() {
			try {
				
				stmt = con.createStatement();
				rs = stmt.executeQuery("select m.movie_number, m.movie_name, m.movie_date, t.theater_number, t.seat_number, t.ticket_price, t.reservation_number, m.movie_number    \r\n"
						+ "from reservation r\r\n"
						+ "left join ticket t on r.reservation_number = t.reservation_number\r\n"
						+ "left join movietime mt on mt.movietime_number = t.movietime_number\r\n"
						+ "left join movie m on m.movie_number = mt.movie_number\r\n"
						+ "where customer_id = '"+customer_id+"';");
				
				String reservation_header[] = {"movie_name", "movie_date", "theater_number", "seat_number", "ticket_price", "select"};
				LinkedList<LinkedList<String>> reservation_contents = new LinkedList<LinkedList<String>>();
				reservation_numbers = new LinkedList<>();
				while (rs.next()) {
					LinkedList<String> tmp_string = new LinkedList<String>();
					if (rs.getString(1) == null) continue;
					
					tmp_string.add(rs.getString(2));
					tmp_string.add(rs.getString(3));
					tmp_string.add(Integer.toString(rs.getInt(4)));
					tmp_string.add(Integer.toString(rs.getInt(5)));
					tmp_string.add(Integer.toString(rs.getInt(6)));
					
					reservation_numbers.add(rs.getInt(7));
					
					
					reservation_contents.add(tmp_string);
					
				}
				
				String[][] reservation_table_contents = new String[reservation_contents.size()][6];
				for(int i=0;i<reservation_contents.size();i++) {
					for(int j=0;j<5;j++) {
						reservation_table_contents[i][j] = reservation_contents.get(i).get(j);
					}
				}
				System.out.println(reservation_numbers);
				for(int i=0;i<reservation_contents.size();i++) {
					System.out.println(reservation_table_contents[i][1]);
					rs = stmt.executeQuery("select movietime.movie_firstday from movietime, ticket where movietime.movietime_number = ticket.movietime_number and ticket.reservation_number ="+Integer.toString(reservation_numbers.get(i))+";");
					rs.next();
					reservation_table_contents[i][1] = rs.getString(1);
				}
				
				if (reservation_jscp != null) c.remove(reservation_jscp);
				reservation_table = new JTable(reservation_table_contents, reservation_header);
				reservation_table.getColumnModel().getColumn(reservation_table.getColumnCount()-1).setCellRenderer(new ReservationTableCell());
				reservation_table.getColumnModel().getColumn(reservation_table.getColumnCount()-1).setCellEditor(new ReservationTableCell());
				
				reservation_jscp = new JScrollPane(reservation_table);
				c.add(reservation_jscp);
				reservation_jscp.setSize(650, 150);
				reservation_jscp.setLocation(20, 280);
				exep.setText("No exception!");
			} catch (SQLException e1) {
				//e1.printStackTrace();
				exep.setText(e1.getMessage());
			}
			
		}
		
		void searchAllMovie() {
			String select_movies = "SELECT * FROM Movie ";
			try { /* 데이터베이스에 질의 결과를 가져오는 과정 */
				
				String text1 = textfield_1.getText();
				String text2 = textfield_2.getText();
				String text3 = textfield_3.getText();
				String text4 = textfield_4.getText();
				textfield_1.setText("");
				textfield_2.setText("");
				textfield_3.setText("");
				textfield_4.setText("");
				
				int start_flag = 0;
				if(text1.equals("") == false) {
					if(start_flag == 0) {
						select_movies += "WHERE ";
						start_flag = 1;
					}
					select_movies += "movie_name = '" + text1 + "'";
				}
				if(text2.equals("") == false) {
					if(start_flag == 0) {
						select_movies += "WHERE ";
						start_flag = 1;
					}
					else select_movies += " and ";
					select_movies += "movie_director = '" + text2 + "'";
				}
				if(text3.equals("") == false) {
					if(start_flag == 0) {
						select_movies += "WHERE ";
						start_flag = 1;
					}
					else select_movies += " and ";
					select_movies += "movie_actor = '" + text3 + "'";
				}
				if(text4.equals("") == false) {
					if(start_flag == 0) {
						select_movies += "WHERE ";
						start_flag = 1;
					}
					else select_movies += " and ";
					select_movies += "movie_genre = '" + text4 + "'";
				}
				select_movies += ';';
				
				stmt = con.createStatement();
				rs = stmt.executeQuery(select_movies);
				
				// searchAllMovies
				String header[] = {"movie_name", "movie_director", "movie_actor", "movie_genre", "reservation"};
				LinkedList<LinkedList<String>> contents = new LinkedList<LinkedList<String>>();
				while (rs.next()) {
					LinkedList<String> tmp_string = new LinkedList<String>();
					tmp_string.add(rs.getString(2));
					tmp_string.add(rs.getString(5));
					tmp_string.add(rs.getString(6));
					tmp_string.add(rs.getString(7));
					contents.add(tmp_string);
				}
				String[][] table_contents = new String[contents.size()][5];
				for(int i=0;i<contents.size();i++) {
					for(int j=0;j<4;j++) {
						table_contents[i][j] = contents.get(i).get(j);
					}
				}
				
				table = new JTable(table_contents, header);
				table.getColumnModel().getColumn(table.getColumnCount()-1).setCellRenderer(new TableCell());
				table.getColumnModel().getColumn(table.getColumnCount()-1).setCellEditor(new TableCell());
				if(jscp != null) c.remove(jscp);

				jscp = new JScrollPane(table);
				c.add(jscp);
				jscp.setSize(650, 150);
				jscp.setLocation(20, 70);
				exep.setText("No exception!");
			} catch (SQLException e) {
				//e.printStackTrace();
				exep.setText(e.getMessage());
			}
		}
		
		class TableCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer{
			JButton jb;
			int cnt = 0;
			String movie_name;
			int movietime_number = 0;
			int theater_number = 0;
			int reservation_number = 0;
			int seat_number = 0;
			public TableCell() {
				jb = new JButton("예매");
				
				jb.addActionListener(e -> {
					movie_name = (String) table.getValueAt(table.getSelectedRow(), 0);
					
					try {						
						stmt = con.createStatement();
						rs = stmt.executeQuery("select mt.movie_number, mt.movietime_number, mt.theater_number\r\n"
								+ "from movietime mt\r\n"
								+ "where mt.movie_number = (select m.movie_number from movie m where m.movie_name = '"+movie_name+"') ;");
						rs.next();
						int movie_number = rs.getInt(1);
						movietime_number = rs.getInt(2);
						theater_number = rs.getInt(3);
						
						rs = stmt.executeQuery("select ticket_number, seat_number from ticket where ticket_out = FALSE and movietime_number in (select movietime_number from movietime where movie_number = "+movie_number+");");
						rs.next();
						
						int ticket_number = rs.getInt(1);
						seat_number = rs.getInt(2);
						reservation_number = ticket_number;
			
						
						// seat_use TRUE로 바꿔줌
						stmt.executeUpdate("UPDATE Seat SET seat_use = TRUE WHERE theater_number = "+theater_number+" and seat_number = "+seat_number+";");
						// 선택된 좌석번호 = 예약번호
						stmt.executeUpdate("INSERT INTO Reservation VALUES("+reservation_number+", 'cash', TRUE, 9000, '"+customer_id+"', STR_TO_DATE('2021-12-31','%Y-%m-%d'));");
						// 선택된 좌석번호의 티켓의 예약번호에 좌석번호 대입 & 티켓 발권 TRUE로
						stmt.executeUpdate("UPDATE Ticket SET reservation_number = "+reservation_number+", ticket_out = TRUE WHERE ticket_number = "+ticket_number+";");
						
						searchAllReservation();
						
						exep.setText("No exception!");
							
						} catch (SQLException e1) {
							exep.setText(e1.getMessage());
						}
				});
			}
			
			@Override
			public Object getCellEditorValue() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				// TODO Auto-generated method stub
				return jb;
			}
			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
					int column) {
				// TODO Auto-generated method stub
				return jb;
			}		
		}
		
		class ReservationTableCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer{
			JButton jb;
			int cnt = 0;
			String movie_name;
			int movietime_number = 0;
			int theater_number = 0;
			int reservation_number = 0;
			
			int seat_number = 0;
			public ReservationTableCell() {
				jb = new JButton("info");
				
				jb.addActionListener(e -> {
					//reservation_number = (int) reservation_table.getValueAt(reservation_table.getSelectedRow(), 4);
					reservation_number = reservation_numbers.get(reservation_table.getSelectedRow());

					new ReservationInfo(reservation_number);
				});
				
			}
			
			
			@Override
			public Object getCellEditorValue() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				// TODO Auto-generated method stub
				return jb;
			}
			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
					int column) {
				// TODO Auto-generated method stub
				return jb;
			}		
		}
		
	
	
		class ReservationInfo extends JFrame{
			String customer_id = "customer_1";
	
			Container c;
			Connection con;
		
			JTextArea ResultLabel = new JTextArea("");
			JTextField textfield = new JTextField("");
	
			Statement stmt;
			ResultSet rs;
			
			JTable reservation_info_table_1;
			JTable reservation_info_table_2;
			JTable reservation_info_table_3;
			JScrollPane reservation_info_jscp_1;
			JScrollPane reservation_info_jscp_2;
			JScrollPane reservation_info_jscp_3;
			
			int flag = 0;
			String tablename = "";
			String insertInfo = "";
			JLabel state = new JLabel("state: ");
			
			int reservation_number = 0;
			
			JLabel exep = new JLabel("exeption: ");
			ReservationInfo(int reservation_number_in){
				reservation_number = reservation_number_in;
				
				setTitle("20011844 안수경 - reservation info");
				setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				setSize(700, 2000);
				setVisible(true);
				c = getContentPane();
				String Driver = "";
				String url = "jdbc:mysql://localhost:3306/madang?&serverTimezone=Asia/Seoul";
				String userid = "madang";
				String pwd = "madang";
				c.add(exep);
				exep.setSize(600, 30);
				exep.setLocation(10,610);
				try { /* 드라이버를 찾는 과정 */
					Class.forName("com.mysql.cj.jdbc.Driver");
					System.out.println("드라이버 로드 성공");
					exep.setText("No exeption!");
				} catch (ClassNotFoundException e) {
					//e.printStackTrace();
					exep.setText(e.getMessage());
				}
	
				try {
					System.out.println("데이터베이스 연결 준비...");
					con = DriverManager.getConnection(url, userid, pwd);
					System.out.println("데이터베이스 연결 성공");
					exep.setText("No exeption!");
				} catch (SQLException e) {
					//e.printStackTrace();
					exep.setText(e.getMessage());
				}
				c.setLayout(null);
				
				try {
					stmt = con.createStatement();
					rs = stmt.executeQuery("select mt.*\r\n"
							+ "from reservation r\r\n"
							+ "left join ticket t on r.reservation_number = t.reservation_number\r\n"
							+ "left join theater th on th.theater_number = t.theater_number\r\n"
							+ "left join movietime mt on mt.movietime_number = t.movietime_number\r\n"
							+ "where customer_id = '"+customer_id+"'  and r.reservation_number = "+reservation_number+";");
					
					String header_1[] = {"movietime_number", "movie_number", "theater_number", "movie_firstday", "movie_day", "movie_n", "movie_startTime"};
					LinkedList<LinkedList<String>> contents_1 = new LinkedList<LinkedList<String>>();
					while (rs.next()) {
						LinkedList<String> tmp_string = new LinkedList<String>();
						tmp_string.add(Integer.toString(rs.getInt(1)));
						tmp_string.add(Integer.toString(rs.getInt(2)));
						tmp_string.add(Integer.toString(rs.getInt(3)));
						tmp_string.add(rs.getString(4));
						tmp_string.add(rs.getString(5));
						tmp_string.add(Integer.toString(rs.getInt(6)));
						tmp_string.add(rs.getString(7));
						contents_1.add(tmp_string);
					}
					String[][] table_contents_1 = new String[contents_1.size()][7];
					for(int i=0;i<contents_1.size();i++) {
						for(int j=0;j<7;j++) {
							table_contents_1[i][j] = contents_1.get(i).get(j);
						}
					}
					
					reservation_info_table_1 = new JTable(table_contents_1, header_1);
					reservation_info_jscp_1 = new JScrollPane(reservation_info_table_1);
					c.add(reservation_info_jscp_1);
					reservation_info_jscp_1.setSize(650, 150);
					reservation_info_jscp_1.setLocation(20, 30);
					
					rs = stmt.executeQuery("select th.*\r\n"
							+ "from reservation r\r\n"
							+ "left join ticket t on r.reservation_number = t.reservation_number\r\n"
							+ "left join theater th on th.theater_number = t.theater_number\r\n"
							+ "left join movietime mt on mt.movietime_number = t.movietime_number\r\n"
							+ "where customer_id = '"+customer_id+"'  and r.reservation_number = "+reservation_number+";");
					
					String header_2[] = {"theater_number", "theater_seatN", "theater_use"};
					LinkedList<LinkedList<String>> contents_2 = new LinkedList<LinkedList<String>>();
					while (rs.next()) {
						LinkedList<String> tmp_string = new LinkedList<String>();
						tmp_string.add(Integer.toString(rs.getInt(1)));
						tmp_string.add(Integer.toString(rs.getInt(2)));
						tmp_string.add(Boolean.toString(rs.getBoolean(3)));
						contents_2.add(tmp_string);
					}
					String[][] table_contents_2 = new String[contents_2.size()][3];
					for(int i=0;i<contents_2.size();i++) {
						for(int j=0;j<3;j++) {
							table_contents_2[i][j] = contents_2.get(i).get(j);
						}
					}
					
					reservation_info_table_2 = new JTable(table_contents_2, header_2);
					reservation_info_jscp_2 = new JScrollPane(reservation_info_table_2);
					c.add(reservation_info_jscp_2);
					reservation_info_jscp_2.setSize(650, 150);
					reservation_info_jscp_2.setLocation(20, 200);
					
					
					
					rs = stmt.executeQuery("select t.*\r\n"
							+ "from reservation r\r\n"
							+ "left join ticket t on r.reservation_number = t.reservation_number\r\n"
							+ "left join theater th on th.theater_number = t.theater_number\r\n"
							+ "left join movietime mt on mt.movietime_number = t.movietime_number\r\n"
							+ "where customer_id = '"+customer_id+"'  and r.reservation_number = "+reservation_number+";");
					
					String header_3[] = {"ticket_number", "movietime_number", "theater_number", "seat_number", "reservation_number", "ticket_out", "ticket_avgPrice", "ticket_price"};
					LinkedList<LinkedList<String>> contents_3 = new LinkedList<LinkedList<String>>();
					while (rs.next()) {
						LinkedList<String> tmp_string = new LinkedList<String>();
						tmp_string.add(Integer.toString(rs.getInt(1)));
						tmp_string.add(Integer.toString(rs.getInt(2)));
						tmp_string.add(Integer.toString(rs.getInt(3)));
						tmp_string.add(Integer.toString(rs.getInt(4)));
						tmp_string.add(Integer.toString(rs.getInt(5)));
						tmp_string.add(Boolean.toString(rs.getBoolean(6)));
						tmp_string.add(Integer.toString(rs.getInt(7)));
						tmp_string.add(Integer.toString(rs.getInt(8)));
						contents_3.add(tmp_string);
					}
					String[][] table_contents_3 = new String[contents_3.size()][8];
					for(int i=0;i<contents_3.size();i++) {
						for(int j=0;j<8;j++) {
							table_contents_3[i][j] = contents_3.get(i).get(j);
						}
					}
					
					reservation_info_table_3 = new JTable(table_contents_3, header_3);
					reservation_info_jscp_3 = new JScrollPane(reservation_info_table_3);
					c.add(reservation_info_jscp_3);
					reservation_info_jscp_3.setSize(650, 150);
					reservation_info_jscp_3.setLocation(20, 370);
					exep.setText("No exeption!");
					
				} catch (SQLException e) {
					//e.printStackTrace();
					exep.setText(e.getMessage());
				}
			}
		}
		
		class MovieUpdateReservation extends JFrame{
			
			Container c;
			Connection con;
			
			int ticket_number = 0;
			
			int reservation_number = 0;
			JLabel exep = new JLabel("exeption: ");
			MovieUpdateReservation(int ticket_number_in){
				ticket_number = ticket_number_in;
				System.out.println("ticket_number = "+ticket_number);
				setTitle("20011844 안수경 - update movie reservation");
				setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				setSize(700, 2000);
				setVisible(true);
				c = getContentPane();
				String Driver = "";
				String url = "jdbc:mysql://localhost:3306/madang?&serverTimezone=Asia/Seoul";
				String userid = "madang";
				String pwd = "madang";
				c.add(exep);
				exep.setSize(600, 30);
				exep.setLocation(10,610);
				try { /* 드라이버를 찾는 과정 */
					Class.forName("com.mysql.cj.jdbc.Driver");
					System.out.println("드라이버 로드 성공");
					exep.setText("No exeption!");
				} catch (ClassNotFoundException e) {
					//e.printStackTrace();
					exep.setText(e.getMessage());
				}
	
				try {
					System.out.println("데이터베이스 연결 준비...");
					con = DriverManager.getConnection(url, userid, pwd);
					System.out.println("데이터베이스 연결 성공");
					exep.setText("No exeption!");
				} catch (SQLException e) {
					//e.printStackTrace();
					exep.setText(e.getMessage());
				}
				c.setLayout(null);
				
				try {
					stmt = con.createStatement();
					rs = stmt.executeQuery("select * from movie;");
					
					String update_movie_reservation_header[] = {"movie_number", "movie_name", "movie_time", "movie_grade", "movie_director", "movie_actor", "movie_genre", "movie_introduce", "movie_date", "select"};
					LinkedList<LinkedList<String>> update_movie_reservation_contents = new LinkedList<LinkedList<String>>();
					while (rs.next()) {
						LinkedList<String> tmp_string = new LinkedList<String>();
						if(rs.getString(2).equals(reservation_table.getValueAt(reservation_table.getSelectedRow(), 0))) continue;
						tmp_string.add(Integer.toString(rs.getInt(1)));
						tmp_string.add(rs.getString(2));
						tmp_string.add(rs.getString(3));
						tmp_string.add(Integer.toString(rs.getInt(4)));
						tmp_string.add(rs.getString(5));
						tmp_string.add(rs.getString(6));
						tmp_string.add(rs.getString(7));
						tmp_string.add(rs.getString(8));
						tmp_string.add(rs.getString(9));
						update_movie_reservation_contents.add(tmp_string);
					}
					
					String[][] update_movie_reservation_table_contents = new String[update_movie_reservation_contents.size()][10];
					for(int i=0;i<update_movie_reservation_contents.size();i++) {
						for(int j=0;j<9;j++) {
							update_movie_reservation_table_contents[i][j] = update_movie_reservation_contents.get(i).get(j);
						}
					}
					
					update_movie_reservation_table = new JTable(update_movie_reservation_table_contents, update_movie_reservation_header);
					update_movie_reservation_table.getColumnModel().getColumn(update_movie_reservation_table.getColumnCount()-1).setCellRenderer(new MovieUpdateTableCell(ticket_number));
					update_movie_reservation_table.getColumnModel().getColumn(update_movie_reservation_table.getColumnCount()-1).setCellEditor(new MovieUpdateTableCell(ticket_number));
					update_movie_reservation_jscp = new JScrollPane(update_movie_reservation_table);
					c.add(update_movie_reservation_jscp);
					update_movie_reservation_jscp.setSize(650, 300);
					update_movie_reservation_jscp.setLocation(20, 30);
					exep.setText("No exeption!");
				
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					exep.setText(e.getMessage());
				}
			}
			
		}
		
		
		class MovieUpdateTableCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer{
			JButton jb;
			int cnt = 0;
			String movie_name;
			int movietime_number = 0;
			int theater_number = 0;
			int reservation_number = 0;
			int seat_number = 0;
			int ticket_number = 0;
			int movie_number = 0;
			
			public MovieUpdateTableCell(int ticket_number_in) {
				ticket_number = ticket_number_in;
				jb = new JButton("update");
				
				jb.addActionListener(e -> {
					System.out.println(update_movie_reservation_table.getSelectedColumn());
					System.out.println(update_movie_reservation_table.getValueAt(update_movie_reservation_table.getSelectedRow(), 0));
					movie_number = Integer.parseInt( (String) update_movie_reservation_table.getValueAt(update_movie_reservation_table.getSelectedRow(), 0));
					
					try {
						stmt = con.createStatement();
						rs = stmt.executeQuery("select reservation_number from ticket where ticket_number = "+ticket_number+";");
						rs.next();
						int original_reservation = rs.getInt(1);
												
						int original_ticket_number = ticket_number;
				
						
						// 새로 선택한 예매 내역 등록
						rs = stmt.executeQuery("select mt.movietime_number, mt.theater_number\r\n"
								+ "from movietime mt\r\n"
								+ "where mt.movie_number = "+movie_number+" ;");
						rs.next();
						movietime_number = rs.getInt(1);
						theater_number = rs.getInt(2);
						
						rs = stmt.executeQuery("select ticket_number, seat_number from ticket where ticket_out = FALSE and movietime_number = "+movietime_number+";");
						rs.next();
						
						ticket_number = rs.getInt(1);
						seat_number = rs.getInt(2);
						
						int reservation_number = ticket_number;	// 임의로 이걸로 설정. 이미 있어서 중복되면 어떡하지?
						// seat_use TRUE로 바꿔줌
						stmt.executeUpdate("UPDATE Seat SET seat_use = TRUE WHERE theater_number = "+theater_number+" and seat_number = "+seat_number+";");
						// 선택된 좌석번호 = 예약번호
						stmt.executeUpdate("INSERT INTO Reservation VALUES("+reservation_number+", 'cash', TRUE, 9000, '"+customer_id+"', STR_TO_DATE('2021-01-01','%Y-%m-%d'));");
						// 선택된 좌석번호의 티켓의 예약번호에 좌석번호 대입 & 티켓 발권 TRUE로
						stmt.executeUpdate("UPDATE Ticket SET reservation_number = "+reservation_number+", ticket_out = TRUE WHERE seat_number = "+seat_number+" and ticket_number = "+ticket_number+";");
						
						
						
						// 선택한 기존 예매 내역 삭제
						stmt.executeUpdate("update ticket set ticket_out = FALSE, reservation_number = NULL where ticket_number = "+original_ticket_number+";\r\n"
								+ "");
						stmt.executeUpdate("update seat, ticket set seat_use = FALSE  where seat.seat_number = ticket.seat_number and ticket.ticket_number = "+original_ticket_number+";");
						stmt.executeUpdate("delete reservation from reservation where reservation_number ="+original_reservation+";");
						
						searchAllReservation();
						exep.setText("No exeption!");
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
						exep.setText(e1.getMessage());
					}					
				});
			}
			
			@Override
			public Object getCellEditorValue() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				// TODO Auto-generated method stub
				return jb;
			}
			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
					int column) {
				// TODO Auto-generated method stub
				return jb;
			}		
		}
		
		class MovieTimeUpdateReservation extends JFrame{
			
			Container c;
			Connection con;
			
			int flag = 0;
			String tablename = "";
			String insertInfo = "";
			JLabel state = new JLabel("state: ");
			int ticket_number = 0;
			int movietime_number = 0;
			int movie_number = 0;
			
			int reservation_number = 0;		
			JLabel exep = new JLabel("exeption: ");
			MovieTimeUpdateReservation(int ticket_number_in, int movietime_number_in, int movie_number_in){
				ticket_number = ticket_number_in;
				movietime_number = movietime_number_in;
				movie_number = movie_number_in;
				
				System.out.println("ticket_number = "+ticket_number);
				setTitle("20011844 안수경 - update movietime reservation");
				setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				setSize(700, 2000);
				setVisible(true);
				c = getContentPane();
				String Driver = "";
				String url = "jdbc:mysql://localhost:3306/madang?&serverTimezone=Asia/Seoul";
				String userid = "madang";
				String pwd = "madang";
				c.add(exep);
				exep.setSize(600, 30);
				exep.setLocation(10,610);
				try { /* 드라이버를 찾는 과정 */
					Class.forName("com.mysql.cj.jdbc.Driver");
					System.out.println("드라이버 로드 성공");
					exep.setText("No exception!");
				} catch (ClassNotFoundException e) {
					//e.printStackTrace();
					exep.setText(e.getMessage());
				}
	
				try {
					System.out.println("데이터베이스 연결 준비...");
					con = DriverManager.getConnection(url, userid, pwd);
					System.out.println("데이터베이스 연결 성공");
					exep.setText("No exception!");
				} catch (SQLException e) {
					//e.printStackTrace();
					exep.setText(e.getMessage());
				}
				c.setLayout(null);
				
				try {
					stmt = con.createStatement();
					System.out.println(movie_number);
					rs = stmt.executeQuery("select * from movietime where movie_number = "+movie_number+";");
					
					String movietime_header[] = {"movietime_number", "movie_number", "theater_number", "movie_firstday", "movie_day", "movie_n", "movie_startTime", "selected"};
					LinkedList<LinkedList<String>> movietime_contents = new LinkedList<LinkedList<String>>();
					while (rs.next()) {

						LinkedList<String> tmp_string = new LinkedList<String>();
						if(movietime_number == rs.getInt(1)) continue;
						tmp_string.add(Integer.toString(rs.getInt(1)));
						tmp_string.add(Integer.toString(rs.getInt(2)));
						tmp_string.add(Integer.toString(rs.getInt(3)));
						tmp_string.add(rs.getString(4));
						tmp_string.add(rs.getString(5));
						tmp_string.add(Integer.toString(rs.getInt(6)));
						tmp_string.add(rs.getString(7));
						movietime_contents.add(tmp_string);
					}
					String[][] movietime_table_contents = new String[movietime_contents.size()][8];
					for(int i=0;i<movietime_contents.size();i++) {
						for(int j=0;j<7;j++) {
							movietime_table_contents[i][j] = movietime_contents.get(i).get(j);
						}
					}
				
					
					update_movietime_reservation_table = new JTable(movietime_table_contents, movietime_header);
					update_movietime_reservation_table.getColumnModel().getColumn(update_movietime_reservation_table.getColumnCount()-1).setCellRenderer(new MovieTimeUpdateTableCell(ticket_number));
					update_movietime_reservation_table.getColumnModel().getColumn(update_movietime_reservation_table.getColumnCount()-1).setCellEditor(new MovieTimeUpdateTableCell(ticket_number));
					update_movietime_reservation_jscp = new JScrollPane(update_movietime_reservation_table);
					c.add(update_movietime_reservation_jscp);
					update_movietime_reservation_jscp.setSize(650, 300);
					update_movietime_reservation_jscp.setLocation(20, 30);
				
					searchAllReservation();
					exep.setText("No exception!");
					
				} catch (SQLException e) {
					//e.printStackTrace();
					exep.setText(e.getMessage());
				}
			}
			
		}
		
		
		class MovieTimeUpdateTableCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer{
			JButton jb;
			int cnt = 0;
			String movie_name;
			int movietime_number = 0;
			int theater_number = 0;
			int reservation_number = 0;
			int seat_number = 0;
			int original_ticket_number = 0;
			public MovieTimeUpdateTableCell(int ticket_number_in) {
				original_ticket_number = ticket_number_in;
				jb = new JButton("update");
				
				jb.addActionListener(e -> {
					movietime_number = Integer.parseInt( (String) update_movietime_reservation_table.getValueAt(update_movietime_reservation_table.getSelectedRow(), 0));
					
					try {
						stmt = con.createStatement();
						rs = stmt.executeQuery("select reservation_number from ticket where ticket_number = "+original_ticket_number+";");
						rs.next();
						int original_reservation = rs.getInt(1);
						
						// 빈 좌석 찾기
						
						rs = stmt.executeQuery("select mt.theater_number\r\n"
								+ "from movietime mt\r\n"
								+ "where mt.movietime_number = "+movietime_number+" ;");
						rs.next();
						theater_number = rs.getInt(1);
						rs = stmt.executeQuery("select seat_number, ticket_number from ticket where ticket_out = FALSE and movietime_number = "+movietime_number+";");
						rs.next();
						seat_number = rs.getInt(1);
						int ticket_number = rs.getInt(2);
						int reservation_number = ticket_number;	
						
						System.out.println(ticket_number+" "+reservation_number+" "+seat_number+" "+theater_number);
						
						// 새로 선택한 예매 내역 등록						
						// seat_use TRUE로 바꿔줌
						stmt.executeUpdate("UPDATE Seat SET seat_use = TRUE WHERE theater_number = "+theater_number+" and seat_number = "+seat_number+";");
						// 선택된 좌석번호 = 예약번호
						stmt.executeUpdate("INSERT INTO Reservation VALUES("+reservation_number+", 'cash', TRUE, 9000, '"+customer_id+"', STR_TO_DATE('2021-01-01','%Y-%m-%d'));");
						// 선택된 좌석번호의 티켓의 예약번호에 좌석번호 대입 & 티켓 발권 TRUE로
						stmt.executeUpdate("UPDATE Ticket SET reservation_number = "+reservation_number+", ticket_out = TRUE WHERE ticket_number = "+ticket_number+";");
						
						
						// 선택한 기존 예매 내역 삭제
						stmt.executeUpdate("update ticket set ticket_out = FALSE, reservation_number = NULL where ticket_number = "+original_ticket_number+";\r\n"
								+ "");
						stmt.executeUpdate("update seat, ticket set seat_use = FALSE  where seat.seat_number = ticket.seat_number and ticket.ticket_number = "+original_ticket_number+";");
						stmt.executeUpdate("delete reservation from reservation where reservation_number = "+original_reservation+";");
						
						searchAllReservation();
						exep.setText("No exeption!");
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
						exep.setText(e1.getMessage());
					}					
				});
			}
			
			@Override
			public Object getCellEditorValue() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				// TODO Auto-generated method stub
				return jb;
			}
			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
					int column) {
				// TODO Auto-generated method stub
				return jb;
			}		
		}
	}
}
	



