package com.example.server;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;



/**
 * @author 杨兰
 * 程序的入口①
 */
public class Login extends JFrame implements ActionListener{
	private JLabel userLabel;//JLabel标签对象：显示文本、图像或者同时显示二者
	private JLabel passLabel;
	
	private JButton exit;//按钮
	private JButton login;
	
	final JTextField userName;//单行文本输入框	
	final JPasswordField userPassword;//密码输入框
	
	public Login(){
		setTitle("欢迎登录");
		
		final JPanel panel=new LoginPanel();//画板
		panel.setLayout(null);
		getContentPane().add(panel);
		setBounds(300, 200, panel.getWidth(), panel.getHeight());
		
		
		userLabel=new JLabel();//用户名标签
		userLabel.setText("用户名：");
		userLabel.setBounds(130,160,200,20);
		panel.add(userLabel);
		
		userName=new JTextField();//单行文本输入框
		userName.setBounds(180,160,200,20);
		panel.add(userName);
		
		passLabel=new JLabel();//密码标签
		passLabel.setText("密  码：");
		passLabel.setBounds(130,190,200,20);
		panel.add(passLabel);
		
		userPassword=new JPasswordField();//密码输入框
		userPassword.setBounds(180,190,200,20);
		userPassword.setEchoChar('●');//•
		userPassword.addKeyListener(new KeyPressListener());
		panel.add(userPassword);
//		
		login=new JButton();//登录按钮
		login.setBounds(180,230,61,26);
		login.setBorder(null);
		login.setIcon(new ImageIcon("image/login_dl1.png"));
		login.setRolloverIcon(new ImageIcon("image/login_dl2.png"));
		login.setPressedIcon(new ImageIcon("image/login_dl3.png"));
		login.addActionListener(this);
		panel.add(login);
		
		exit=new JButton();//退出按钮
		exit.setBounds(290, 230, 61,26);
		exit.addActionListener(this);
		exit.setIcon(new ImageIcon("image/login_tc1.png"));
		exit.setRolloverIcon(new ImageIcon("image/login_tc2.png"));
		exit.setPressedIcon(new ImageIcon("image/login_tc3.png"));
		panel.add(exit);
	
		setVisible(true);
		setResizable(false);
		/*将服务器登陆窗口置于电脑屏幕的中央处*/
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		Dimension dimension=toolkit.getScreenSize();
		int screenheight=(int) dimension.getHeight();
		int screenwidth=(int) dimension.getWidth();
		setLocation((screenwidth-this.getWidth())/2,(screenheight-this.getHeight())/2-50);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(final ActionEvent e){
		if (e.getSource() == login) {
			if ((userName.getText().trim().equals("yanglan")) && (userPassword.getText().trim().equals("920627"))) {
				setVisible(false);
				new MainFrame();//进入界面，程序的第二步②
			} else {
				JOptionPane.showMessageDialog(this, "用户名或密码有误,请重新输入！");
				userName.setText(null);
				userPassword.setText(null);
				return;
			}
		} else if (e.getSource() == exit) {
			this.dispose();
			System.exit(0);
		}
	}

	class KeyPressListener implements KeyListener{
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode()==KeyEvent.VK_ENTER){
				login.doClick();
			}
		}
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}
	}
	static{
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//入口函数
	public static void main(String []argv){
		//new Login();
		new MainFrame();
	}
}
