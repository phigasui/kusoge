import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

class Game{
    public static void main(String args[])throws Exception{
        JFrame frame=new JFrame("kusoge");
        GaPanel gp=new GaPanel();
        frame.getContentPane().add(gp);
        frame.setSize(815,630);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
class GaPanel extends JPanel implements Runnable {
    JLabel label=new JLabel("",SwingConstants.CENTER);
    JLabel label2=new JLabel("",SwingConstants.CENTER);
    Image hitoshi,loc,death,nan,start,haikei,setsumei,gorl,clear;
    int ts=50,h=50,w=50,vx=0,vy=0,s=10,jump=20,gravity=1,dwl=350,dwr=450,dw=800;
    int pnx1,pnx2,pny1,pny2,px1,px2,py1,py2,n,p=0,game=0,c,x,y,cmax=30,time;
    boolean left,right,onground=true;
    Thread gameLoop;
    AlphaComposite composite1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
    AlphaComposite composite2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
    int[][] map;
    Timer t=new Timer(1000,new MyListener());

    GaPanel()throws Exception{
        setBackground(Color.white);
        setFocusable(true);
        setLayout(new BorderLayout());
        addKeyListener(new KListener());
        t=new Timer(1000,new MyListener());
        gameLoop=new Thread(this);
        hitoshi=ImageIO.read(new File("hitoshi.jpg"));
        loc=ImageIO.read(new File("loc.jpg"));
        death=ImageIO.read(new File("death.jpg"));
        nan=ImageIO.read(new File("nan.jpg"));
        start=ImageIO.read(new File("start.jpg"));
        haikei=ImageIO.read(new File("haikei.jpg"));
        setsumei=ImageIO.read(new File("setsumei.jpg"));
        gorl=ImageIO.read(new File("gorl.jpg"));
        clear=ImageIO.read(new File("clear.jpg"));
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if(game==0){
            int [][] mapsub={
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
                {1,2,2,2,2,0,0,0,0,0,0,0,3,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,1},
                {1,1,1,1,1,0,0,0,0,0,0,1,1,1,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,1,1,1,1,1,1,1,1,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,1,3,0,1,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,0,0,0,1,1,1,0,0,0,1,1,1,0,0,0,1,1,1,0,0,0,1,1,1,0,0,0,1,1,1,0,0,0,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            };
            c=0;x=80;y=500;time=0;map=mapsub;
            g.drawImage(start,0,0,this);
        }else if(game==2){
            g.drawImage(setsumei,0,0,this);
        }else if(game==1){
            g.drawImage(haikei,0,0,this);
            t.start();
            add(label,BorderLayout.NORTH);
            label.setText("coins:"+c+" time:"+time);
            label.setFont(new Font("MS UI Gothic",Font.BOLD,25));
            if(c==cmax)map[9][1]=4;
            if(x-dwl<=0){
                g.setColor(Color.pink);
                g.drawImage(hitoshi,x,y,this);
                for (int i=0;i<map.length;i++){
                    for (int j=0;j<dw/ts;j++){
                        if(map[i][j]==1){
                            g.drawImage(loc,ts*j,ts*i,this);
                        }else if(map[i][j]==2){
                            g.setColor(Color.yellow);
                            g.fillOval(ts*j,ts*i,ts,ts);
                        }else if(map[i][j]==3){
                            g2.setComposite(composite1);
                            g2.drawImage(nan,ts*j,ts*i,this);
                            g2.setComposite(composite2);
                        }else if(map[i][j]==4){
                            g2.setComposite(composite1);
                            g2.drawImage(gorl,ts*j,ts*i,this);
                            g2.setComposite(composite2);
                        }
                    }
                }
            }else if(x+dwr>=map[0].length*ts){
                g.setColor(Color.pink);
                g.drawImage(hitoshi,(x-(map[0].length*ts-dw)),y,this);
                for (int i=0;i<map.length;i++){
                    n=0;
                    for (int j=(int)((map[0].length*ts-dw)/(double)ts);j<map[0].length;j++){
                        if(map[i][j]==1){
                            g.drawImage(loc,ts*n,ts*i,this);
                        }else if(map[i][j]==2){
                            g.setColor(Color.yellow);
                            g.fillOval(ts*n,ts*i,ts,ts);
                        }else if(map[i][j]==3){
                            g2.setComposite(composite1);
                            g.drawImage(nan,ts*n,ts*i,this);
                            g2.setComposite(composite2);
                        }
                    n++;
                    }
                }
            }else{
                g.setColor(Color.pink);
                g.drawImage(hitoshi,dwl,y,this);
                for(int i=0;i<map.length;i++){
                    n=0;
                    for (int j=(x-dwl)/ts;j<(x+dwr)/ts+1;j++){
                        if(map[i][j]==1){
                            g.drawImage(loc,n*ts-(x-dwl)%ts,ts*i,this);
                        }else if(map[i][j]==2){
                            g.setColor(Color.yellow);
                            g.fillOval(n*ts-(x-dwl)%ts,ts*i,ts,ts);
                        }else if(map[i][j]==3){
                            g2.setComposite(composite1);
                            g.drawImage(nan,n*ts-(x-dwl)%ts,ts*i,this);
                            g2.setComposite(composite2);
                        }else if(map[i][j]==4){
                            g2.setComposite(composite1);
                            g.drawImage(gorl,n*ts-(x-dwl)%ts,ts*i,this);
                            g2.setComposite(composite2);
                        }
                    n++;
                    }
                }
            }
        }else if(game==3){
            t.stop();
            remove(label);
            g.drawImage(death,0,0,this);
        }else if(game==4){
            t.stop();
            g.drawImage(clear,0,0,this);
            add(label2,BorderLayout.CENTER);
            label2.setText(time+"s");
            label2.setFont(new Font("MS UI Gothic",Font.BOLD,70));
        }
    }
    public void run(){
        while(true){
            if(!onground)vy+=gravity;
            pny1=(int)Math.ceil((y+h)/(double)ts)-1;
            pny2=y/ts;
            if(right){
                vx=s;
                pnx1=(int)Math.ceil((x+vx+w)/(double)ts)-1;
                if(map[pny1][pnx1]==1 || map[pny2][pnx1]==1){
                    x=pnx1*ts-w;
                    vx=0;
                }
            }else if(left){
                vx=-s;
                pnx2=(x+vx)/ts;
                if(map[pny1][pnx2]==1 || map[pny2][pnx2]==1){
                    x=(pnx2+1)*ts;
                    vx=0;
                }
            }else vx=0;
            pnx1=(int)Math.ceil((x+w)/(double)ts)-1;
            pnx2=x/ts;
            pny1=(int)Math.ceil((y+vy+h)/(double)ts)-1;
            pny2=(y+vy)/ts;
            if(vy>0){
                if(map[pny1][pnx1]==1 || map[pny1][pnx2]==1){
                    y=pny1*ts-h;
                    vy=0;
                    onground=true;
                }
            }else if(vy<0){
                if(map[pny2][pnx1]==1 || map[pny2][pnx2]==1){
                    y=(pny2+1)*ts;
                    vy=0;
                }
            }else if(vy==0){
                if(map[pny1+1][pnx1]!=1 && map[pny1+1][pnx2]!=1)onground=false;
            }
            x+=vx;
            y+=vy;
            px1=(int)Math.ceil((x+w)/(double)ts)-1;
            px2=x/ts;
            py1=(int)Math.ceil((y+h)/(double)ts)-1;
            py2=y/ts;
            if(map[py1][px1]==2){
                map[py1][px1]=0;
                c++;
            }else if(map[py1][px2]==2){
                map[py1][px2]=0;
                c++;
            }
            if(map[py2][px1]==3){
                map[py2][px1]=0;
                time+=10;
            }else if(map[py2][px2]==3){
                map[py2][px2]=0;
                time+=10;
            }else if(map[py1][px1]==3){
                map[py1][px1]=0;
                time+=10;
            }else if(map[py1][px2]==3){
                map[py1][px2]=0;
                time+=10;
            }
            if(y>12*ts)game=3;
            if(c==cmax)
                if(x>=50 && x<=100 && y==500)game=4;
            repaint();
            try {
                Thread.sleep(30);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    class KListener implements KeyListener{
        public void keyPressed(KeyEvent e){
                int key = e.getKeyCode();
            if(key==KeyEvent.VK_LEFT)
                left=true;
            else if(key==KeyEvent.VK_RIGHT)
                right=true;
            else if(key==KeyEvent.VK_UP){
                if(onground){
                    vy=-jump;
                    onground=false;
                }
            }else if(key==KeyEvent.VK_S){
                if(game==0){
                    game=1;
                    gameLoop.start();
                }
            }else if(key==KeyEvent.VK_R){
                if(game==3 || game==4){
                    remove(label);
                    remove(label2);
                    game=0;
                    repaint();
                }
            }else if(key==KeyEvent.VK_A){
                if(game==0){
                    game=2;
                    repaint();
                }
            }else if(key==KeyEvent.VK_B){
                if(game==2){
                    game=0;
                    repaint();
                }
            }
        }
        public void keyReleased(KeyEvent e){
            int key = e.getKeyCode();
            if(key==KeyEvent.VK_LEFT)left=false;
            else if(key==KeyEvent.VK_RIGHT)right=false;
        }
        public void keyTyped(KeyEvent e){}

    }
    class MyListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            time+=1;
            repaint();
        }
    }
}
