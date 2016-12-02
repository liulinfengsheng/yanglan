package com.example.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class HelpDialog extends JDialog {
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JLabel bglabel,label1,label2;
	private Container con;
	private JSplitPane splitPane;
	public static String str="室外定位系统服务器是基于Java Swing开发的GUI应用程序，该服务器与Android定位客户端和Android指纹采集客户端构成了一个完整的基于Android的室外定位系统。\n" +
			"服务器系统主要实现以下功能：\n" +
			"1、Socket连接监听定位请求/修改数据库请求；\n" +
			"2、室外基站相关参数信息的接收和处理；\n" +
			"3、访问数据库/修改数据库；\n" +
			"4、执行定位算法；\n" +
			"5、返回执行定位算法后的定位结果到客户端。\n" +
			"\n意见反馈：\n" ;
	//构造函数
	public HelpDialog(JFrame owner){
		super(owner);
		setTitle("About...");
		setVisible(true);
		setResizable(false);
		setSize(380,owner.getHeight());
		setLocation(owner.getX()+owner.getWidth(),owner.getY());
		initHelpDialog();
	}
	public void initHelpDialog(){
		bglabel=new JLabel();
        bglabel.setIcon(new ImageIcon("image/background.png"));
		label1=new JLabel();
		label1.setText(" 室外定位(V3.0)            ");
		label2=new JLabel();
		label2.setText("(C)2016 ll Inc.All rights reserved.");
		JPanel panel=new JPanel();
		panel.add(label1);
		panel.add(label2);
		
		textArea=new JTextArea();
		textArea.setEditable(true);
		textArea.setBackground(new Color(200,230,250));
//		textArea.setEnabled(false);
		textArea.setLineWrap(true);
		textArea.append(str);
		scrollPane=new JScrollPane(textArea);
		scrollPane.setBorder(BorderFactory.createTitledBorder("���..."));
		
		splitPane=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(50);
		splitPane.setEnabled(false);
		splitPane.setTopComponent(panel);
		splitPane.setBottomComponent(scrollPane);
		
		con=getContentPane();
		con.setLayout(new BorderLayout());
		con.add(bglabel,BorderLayout.NORTH);
		con.add(splitPane,BorderLayout.CENTER);		
	}
}
