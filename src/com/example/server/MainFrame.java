package com.example.server;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;




/**
 * @author 杨兰
 * ②
 */

public class MainFrame implements ActionListener {
	private JFrame f;//窗口
	
	private JButton btnData;//按钮--创建指纹数据库
	private JButton btnLocation;
	
	private JTextArea area;//多行文本输入框
	
	private JScrollPane js;//带水平及垂直滚动条的容器组件
	
	private JMenuBar mb;//菜单栏
	private JMenu m1;//菜单
	private JMenuItem mil;//菜单项
	private JMenuItem mi2;
	
	private AbstractButton m2;

	private LocaMain loca;
	
	
	public MainFrame(){
		
		f=new JFrame("基于Android的室外定位系统服务器");//窗口
		f.setSize(800, 600);
		f.setLocation(100, 100);
		f.getContentPane().setBackground(Color.pink);
		f.setVisible(true);	
		f.setLayout(null);
		
	    btnData=new JButton("创建指纹数据库");//创建指纹数据库按钮
	    btnData.setBounds(180,40,170,50);
	    btnData.setForeground(Color.red);
	    f.add(btnData,"North");      
	    btnData.addActionListener(this);//添加按钮1事件监听
	    
	    btnLocation=new JButton("开启定位服务器");//开启定位服务器按钮
        btnLocation.setBounds(450,40,170,50);//设置左边距，上边距，宽度，高度
        btnLocation.setForeground(Color.red); //字体颜色    
	    f.add(btnLocation,"North");	   
	    btnLocation.addActionListener(this); //添加按钮2事件监听
	    
	    area=new JTextArea();//多行文本输入框
	    area.setForeground(Color.blue);
	  
	    js=new JScrollPane(area);//带水平及垂直滚动条的容器组件
	    js.setBounds(20, 120, 740, 400);
	    f.add(js,"center");	    
	    f.addWindowListener(new WindowAdapter(){
	    	public void windowClosing(WindowEvent e){
	    	         System.exit(0);
				}
	    });
	    
	  //添加菜单栏
	     mb=new JMenuBar();//菜单栏
	     m1= new JMenu("系统");//菜单栏中的菜单
	     m1.setIcon(new ImageIcon("image/system.png"));
	     m2=new JMenu("帮助");//菜单栏中的菜单
	     m2.setIcon(new ImageIcon("image/help.png"));
	     mil=new JMenuItem("退出");//菜单项
	     mi2=new JMenuItem("关于");	
	   //事件监听器
	     mil.addActionListener(this);
	     mi2.addActionListener(this);
	   
	    m1.add(mil);
	    m2.add(mi2);
	    mb.add(m1);
	    mb.add(m2);
	    f.setJMenuBar(mb);
	
	     f.setVisible(true);
	}
/**高级事件：ActionEvent事件：比如在TextField中按Enter键会触发ActionEvent事件
 * 事件：ActionEvent----监听器接口：ActionListener-----处理器及触发时机：actionPerformed按钮、文本框、菜单项被单击时触发
 * **/

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnData){
			btnData.setLabel("正在接收指纹信息");
	    	DBMain dbCreat=new DBMain(area);//DBMain是一个线程类，③
	    	dbCreat.start();//开启一个线程
	    	}
		if(e.getSource()== btnLocation){
			 btnLocation.setLabel("正在定位");
	     	 
			loca = new LocaMain(area);
	     	 loca.start();//开启一个线程
	    	}
			if(e.getSource()==mil){
				 System.exit(0);
	    	}
			if(e.getSource()==mi2){
				new HelpDialog(f);
	   	    }
		}
		
	}


