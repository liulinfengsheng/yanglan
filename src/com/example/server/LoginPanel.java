package com.example.server;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * @author ����
 * LoginPanel��ֻ��������һ�����������ڵ�¼��
 */
public class LoginPanel extends JPanel {
	protected ImageIcon icon;
	public int width,height;
	//���캯��
	public LoginPanel(){
		super();
		icon=new ImageIcon("image/login_small.png");
		width=icon.getIconWidth();
		height=icon.getIconHeight();
		setSize(width,height);
	}
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Image img=icon.getImage();
		g.drawImage(img,0,0,getParent());
	}
}
