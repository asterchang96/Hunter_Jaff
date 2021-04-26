import java.awt.Color; 
import java.awt.Container; 
import java.awt.Graphics;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import java.util.Random;
import javax.swing.Timer;
public class finalprog122 extends JComponent implements Runnable,MouseMotionListener,MouseListener,ActionListener {
	Thread t; //執行緒 
	Thread t2; //執行緒
	int speed = 3; //移動間距
	int Size=200;//圖片大小
	int x = -115, y = 0; //主角座標
	int back_x =0,back_x2 = 0; //背景座標
	int win_x =150-500;//贏了的座標
	int[] hunt_x = new int[14];//獵人的座標
	int hunt_count = 0;//獵人
	Image[] image = new Image[21];//圖片
	int jumptwice=-1;//-1:剛開始 0:一段跳躍 1:上升時的第二段跳躍 2:下降時的第二段跳躍
	int jumpoffset=0;//下降時的第二段跳躍要跳多高
	int run=1;//猴子跑步圖16張
	boolean win=false;
	boolean lose=false;
	public static JButton b1 = new JButton("Restart");
	public static JLabel lab_score,lab_timer;
	public static double ms, mt;
	public static javax.swing.Timer time;
	public static JFrame window;
	public static int offset = 100, score;
	public static String text;
	
	public static void main(String[] args){
		mt = 60;//倒數秒數
		ms = 0;
		score = 0;
		window = new JFrame("finalprog122");
		Container cp = window.getContentPane(); 
		finalprog122 frm = new finalprog122(); 
		time.start();
		b1.setBounds(20,20,80,40);
		lab_score=new JLabel("Score: 0");
		lab_timer=new JLabel("剩下: 60秒");
		lab_score.setFont(new java.awt.Font("Dialog", 1, 25));
		lab_timer.setFont(new java.awt.Font("Dialog", 1, 25));
		lab_score.setForeground(Color.blue);
		lab_timer.setForeground(Color.blue);
		lab_score.setBounds(140,20,160,40);
		lab_timer.setBounds(300,20,160,40);
		cp.add(b1);
		cp.add(lab_score);
		cp.add(lab_timer);
		cp.add(frm);
		frm.addMouseListener(frm);
		frm.addMouseMotionListener(frm);
		Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();		// 設定視窗範圍
		window.setSize(screenSize.width-offset*2,screenSize.height-offset*2); 
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	public finalprog122(){
		b1.addActionListener(this);
		try {
			time = new javax.swing.Timer(500, this);
            image[0] = ImageIO.read(new File("city1-01.png"));//背景圖片讀取
			image[17] = ImageIO.read(new File("city-01.png"));//背景圖片讀取
			image[18] = ImageIO.read(new File("win-01.png"));//贏了圖片讀取
			image[19] = ImageIO.read(new File("hunt-01.png"));//獵人圖片讀取
			image[20] = ImageIO.read(new File("lose-01.png"));//輸圖片讀取
			
			for(int i=1;i<=16;i++)
				image[i] = ImageIO.read(new File("RUN/run"+ Integer.toString(i)+"-01.png"));//主角圖片讀取
        }
        catch (Exception ex) {
            System.out.println("No example!!");
        }
		//15個獵人間隔
		Random r_hunt=new Random();
		for(int i=0;i<14;i++){
			r_hunt=new Random();
			if(i==0)
				hunt_x[i]=r_hunt.nextInt(550)+450+this.getSize().height;		
			else if(i==7)
				hunt_x[i]=r_hunt.nextInt(550)+1200+hunt_x[i-1];
			else if(i<7)
				hunt_x[i]=r_hunt.nextInt(550)+550+hunt_x[i-1];
			else
				hunt_x[i]=r_hunt.nextInt(550)+825+hunt_x[i-1];
		}

		t = new Thread(this); 
		t.start();
		t2 = new Thread(this); 
		t2.start();	
	}
	@Override 
	public void run() { 
		while(!lose){
			//判斷有沒有碰到
			for(int i=0;i<14;i++){
				if(hunt_x[i]>-150){
					if(((hunt_x[i]-150>0&&hunt_x[i]-150<50)//獵人在前後
						||(hunt_x[i]-150<0&&Math.abs(hunt_x[i]-150)<100))
						&&(Math.abs(y-(this.getSize().height-Size-130))<50||y==this.getSize().height-Size-50))
						lose=true;
				}
				
				if(i<7)hunt_x[i]-=10;
				else if(i>=7)hunt_x[i]-=13;
				
			}
			if(!lose){
				x += speed;
				if(jumptwice==-1)//一開始不要跳
					y=this.getSize().height-Size-50;
				if(jumptwice==0)//一段跳躍
					y=x*x/(40)+80;
				else if(jumptwice==1)//二段跳躍
					y=x*x/(40)+20;
				else if(jumptwice==2)//二段跳躍			
					y=x*x/(40)+jumpoffset;


				if(back_x2<-1*this.getSize().height*2.8&&win==false){//第二張完全播完，結束
					x=150;
					win=true;
				}
				
			}
			repaint();
				
			try { 
				Thread.sleep(70); 
			}
			catch (InterruptedException e) { 
				e.printStackTrace(); 
			} 
		} 
	}

	public void paint(Graphics g){
		int _h=this.getSize().height;
		
		if(lose){
			g.drawImage(image[17], back_x2, 0,_h*5,_h, null);
			g.drawImage(image[17], back_x, 0,_h*5,_h, null);
			for(int i=0;i<14;i++){
				g.drawImage(image[19],hunt_x[i],_h-Size-130,300,300, null);//hunter
			}
			
			g.drawImage(image[run],150, y,Size,Size, null);//平地上走路
			g.drawImage(image[20],325, this.getSize().height-500,500,500, null);//LOSE標示
		}
		else if(win&& (int)Math.round(mt)>0){//第二張完全播完，結束
			g.drawImage(image[17], back_x2, 0,_h*5,_h, null);
			g.drawImage(image[run++],x++, this.getSize().height-Size-50,Size,Size, null);//平地上走路
			if(run>16)
				run=1;
			g.drawImage(image[18],win_x, 150,500,500, null);//WIN標示
			if(win_x<90)
				win_x+=3;	
		}
		else{
			//背景移動
			if(back_x<-1*_h*5){//第一張背景完全播完，第二張背景開始播
				//back_x=_h/2;
				g.drawImage(image[17], back_x2, 0,_h*5,_h, null);
				back_x2-=5;
			}
			else if(back_x<=-1*_h*2.8&&back_x>=-1*_h*5){//第一張背景剩一個螢幕大小，第二章背景接續
				g.drawImage(image[0], back_x, 0,_h*5,_h, null);
				back_x2=back_x+_h*5;
				back_x-=5;
				g.drawImage(image[17], back_x2, 0,_h*5,_h, null);
			}
			else{
				g.drawImage(image[0], back_x, 0,_h*5,_h, null);
				back_x-=5;	
			}
			
			//障礙物移動
			for(int i=0;i<14;i++){
				g.drawImage(image[19],hunt_x[i],_h-Size-130,300,300, null);//hunter
			}
			
			//猴子移動
			if(y<_h-Size-50)
				g.drawImage(image[run], 150, y,Size,Size, null);//跳躍中
			else{
				y=_h-Size-50;
				g.drawImage(image[run++], 150, y,Size,Size, null);//平地上走路
				if(run>16)
					run=1;
			}		
		}
	}
	@Override
	public void mousePressed(MouseEvent e)
	{
		if(!win){
			if(y>=this.getSize().height-Size-50){//回到最初高度時才能再跳躍
				x=-115;//最初高度時的x座標
				jumptwice=0;
			}
			else if(jumptwice==0){//第二次點擊滑鼠的第二段跳躍
				if(x<=0){//正在上升時二段跳躍
					x-=30;
					jumptwice=1;
				}
				else{//正在下降時二段跳躍
					jumpoffset=y-100;
					x*=-1;
					jumptwice=2;
				}
			}
		}	
	}
	public void mouseDragged(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseMoved(MouseEvent e){} 
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void actionPerformed(ActionEvent e){

		if (e.getSource()== b1){
			window.dispose();
			mt = 60;//倒數秒數
			ms = 0;
			score = 0;
			window = new JFrame("finalprog122");
			Container cp = window.getContentPane(); 
			finalprog122 frm = new finalprog122(); 
			time.start();
			b1.setBounds(20,20,80,40);
			lab_score=new JLabel("Score: 0");
			lab_timer=new JLabel("剩下: 60秒");
			lab_score.setFont(new java.awt.Font("Dialog", 1, 25));
			lab_timer.setFont(new java.awt.Font("Dialog", 1, 25));
			lab_score.setForeground(Color.blue);
			lab_timer.setForeground(Color.blue);
			lab_score.setBounds(140,20,160,40);
			lab_timer.setBounds(300,20,160,40);
			cp.add(b1);
			cp.add(lab_score);
			cp.add(lab_timer);
			cp.add(frm);
			frm.addMouseListener(frm);
			frm.addMouseMotionListener(frm);
			Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();		// 設定視窗範圍
			window.setSize(screenSize.width-offset*2,screenSize.height-offset*2); 
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setVisible(true);
			}
		if (e.getSource()== time){
			if(lose == true || win == true) time.stop();
			else{
				ms=ms+0.5;
				mt=mt-0.5;
				score = (int)Math.round(ms)*10;
				text = new String("Score: "+ score);
				repaint();
			}
			lab_score.setText(text);
			if(Math.round(mt) == mt)
				lab_timer.setText("剩下:"+(int)Math.round(mt)+"秒");
			
		}	
	}
	
}