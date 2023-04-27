package view;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Models.Customer;
import controller.FormMainListener;

import javax.swing.JTable;
import javax.swing.JScrollBar;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Font;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.Normalizer.Form;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import java.awt.Window.Type;

public class FormMainPage extends JFrame {

	public JPanel contentPane;
	public JButton btnAcc,btnBookTicket,btnBill,btnLogOut;
	public JPanel body;
	public Customer cus;
	
	public static final JPanel userPanel = new PanelUser();
	public static final JPanel bookingTicket1Panel = new BookingTicket1();
	public static final JPanel bookingTicket2Panel = new BookingTicket2();

	public static final FormMainPage FORM_MAIN_PAGE = new FormMainPage();
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					FormMainPage frame = new FormMainPage();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public FormMainPage() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 710, 535);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelMenu = new JPanel();
		panelMenu.setBackground(new Color(0, 255, 255));
		panelMenu.setBounds(0, 0, 167, 498);
		contentPane.add(panelMenu);
		panelMenu.setLayout(null);
		
		btnAcc = new JButton("Tài khoản");
		btnAcc.setIcon(new ImageIcon(FormMainPage.class.getResource("/photo/Custom-Icon-Design-Silky-Line-User-User.32.png")));
		btnAcc.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		btnAcc.setHorizontalAlignment(SwingConstants.LEFT);
		btnAcc.setBackground(new Color(0, 128, 192));
		btnAcc.setBounds(10, 84, 147, 53);
		panelMenu.add(btnAcc);
		
		btnBookTicket = new JButton("Đặt vé ");
		btnBookTicket.setIcon(new ImageIcon(FormMainPage.class.getResource("/photo/Icons8-Ios7-Cinema-Ticket.32.png")));
		btnBookTicket.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		btnBookTicket.setHorizontalAlignment(SwingConstants.LEFT);
		btnBookTicket.setBackground(new Color(0, 128, 192));
		btnBookTicket.setBounds(10, 172, 147, 53);
		panelMenu.add(btnBookTicket);
		
		btnBill = new JButton("Hóa đơn");
		btnBill.setIcon(new ImageIcon(FormMainPage.class.getResource("/photo/Icons8-Ios7-Finance-Bill.32.png")));
		
		btnBill.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		btnBill.setHorizontalAlignment(SwingConstants.LEFT);
		btnBill.setBackground(new Color(0, 128, 192));
		btnBill.setBounds(10, 260, 147, 53);
		panelMenu.add(btnBill);
		
		btnLogOut = new JButton("Đăng xuất");
		btnLogOut.setIcon(new ImageIcon(FormMainPage.class.getResource("/photo/Github-Octicons-Sign-out-24.32.png")));
		btnLogOut.setHorizontalAlignment(SwingConstants.LEFT);
		btnLogOut.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		btnLogOut.setBackground(new Color(0, 128, 192));
		btnLogOut.setBounds(10, 348, 147, 53);
		panelMenu.add(btnLogOut);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(167, 142, 546, 356);
		panelMenu.add(panel_2);
		
//		JPanel pn3 = new PanelUser();
//		JPanel pn4 = new PanelUserTop();
//		
//		
		body = new JPanel();
		body.setBounds(167, 0, 529,500);
		body.setBackground(Color.BLACK);
		body.setLayout(null);
//		body.add(pn4);
//		body.add(pn3);
		contentPane.add(body);
		
		ActionListener m = new FormMainListener(this);
		btnAcc.addActionListener(m);
		btnBookTicket.addActionListener(m);
		btnBill.addActionListener(m);
		btnLogOut.addActionListener(m);
		((BookingTicket1)bookingTicket1Panel).addAc(m);
		
		this.body.add(userPanel);
		this.body.add( new PanelUserTop());
		setVisible(true);
	}
	public void changeToAcc() {
		body.removeAll();
//		this.contentPane.remove(body);
		JPanel p1 = new PanelUserTop();
//		JPanel p2 = new PanelUser();
		body.add(p1);
		
		body.add(userPanel);
//		body.add(p2);
		body.revalidate();
		body.repaint();
	}
	public void changeToBooking() {
		body.removeAll();
//		JPanel p = new BookingTicket1(this);
		
		
		body.add(bookingTicket1Panel);
		body.revalidate();
		body.repaint();
	}
	public void changeToBill() {
		body.removeAll();
		JPanel p1 = new PanelMainTop();
		JPanel p2 = new PanelListBill(12);
		body.add(p1);
		body.add(p2);
		body.revalidate();
		body.repaint();
	}
	public void changeToSitting() {
		body.removeAll();
	
	}
	public void changeToListTicket() throws SQLException {
		body.removeAll();
		
		// SetData
		((BookingTicket2)this.bookingTicket2Panel).KindOfBook = ((BookingTicket1)this.bookingTicket1Panel).KindOfBook;
		((BookingTicket2)this.bookingTicket2Panel).listDate = ((BookingTicket1)this.bookingTicket1Panel).listDate;
		((BookingTicket2)this.bookingTicket2Panel).CusID = ((BookingTicket1)this.bookingTicket1Panel).CusID;
		((BookingTicket2)this.bookingTicket2Panel).listRoute = ((BookingTicket1)this.bookingTicket1Panel).listRoute;
		
		((BookingTicket2)this.bookingTicket2Panel).Init();
		
		
		// SetEvent
		body.add(bookingTicket2Panel);
		body.revalidate();
		body.repaint();
	}
}