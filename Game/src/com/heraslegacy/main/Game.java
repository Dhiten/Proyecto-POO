
package com.heraslegacy.main;

import com.heraslegacy.graphics.Screen;
import com.heraslegacy.graphics.Sound;
import com.heraslegacy.graphics.Text;
import com.heraslegacy.manager.KeyBoard;
import com.heraslegacy.manager.Mouse;
import com.heraslegacy.level.Level;
import com.heraslegacy.level.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
    
    public Level level;
    private Thread thread;
    private KeyBoard key;
    private Mouse mouse;
    public JFrame frame; 
    private static final long serialVersionUID = 1L;
    public static int width = 300;
    public static int height = width / 16 * 9;
    public static int scale = 3;
    private boolean running = true;
    private final double TIME_BEFORE_UPDATE = 1000000000.0 / 120.0;
    public static boolean activarMecanica = false; 
    //Para dibujar texto
    private String text="Puta";
    private int x=0, y=0;
    private Color c=Color.white;
    private Font f;
    //para dibujar texto
    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    public static Screen screen;
    Sound theme;

    public Game() {
        Dimension size = new Dimension(width * scale, height * scale);
        setPreferredSize(size);
        frame = new JFrame();
        screen = new Screen(width, height);
        Sound.init();
        Text.init();
        f=Text.spaceFont;
        theme=new Sound(Sound.de);
        //theme.loop(); //MUSICA PARA EL JUEGO
        key = new KeyBoard();
        //level = new Level("/levels/lobby/lobby.png","/levels/lobby/collisionlobby.png",new Lobby());
        //level = new Level("/levels/level01/level1.png","/levels/level01/collisionlevel1.png",new MathLevel());
        level = new Level("/levels/level02/level2.png","/levels/level02/collisionlevel2.png",new SpaceLevel());
        //level = new Level("/levels/level03/nivel3.png","/levels/level03/nivel3COLLITION.png",new LibraryLevel());
        level.configPlayer();
        
        addKeyListener(key);
        mouse = new Mouse();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    public synchronized void start() {
        thread = new Thread(this, "GameHerasLegacy");
        thread.start();
    }
    
    public synchronized void stop() {
        try {
            thread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        
        long timer = System.currentTimeMillis();        
        long startTime = System.nanoTime();
        double elapsedTime = 0;
        int frames = 0;
        int updates = 0;
        
        while(running){
            
            elapsedTime += (System.nanoTime() - startTime) / TIME_BEFORE_UPDATE;            
            startTime = System.nanoTime();
            while(elapsedTime >= 1){
                update();
                updates++;
                elapsedTime--;
            }
            
            render();
            frames++;
            
            //Refresh image once per second
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frame.setTitle("Hera's Legacy | " + updates + " ups | " + frames + " fps");
                updates = 0;
                frames = 0;
            }		
        }
        stop();
    }
   

    public void update() {
        key.uptade();
        level.getPlayer().update();
            
        
    }

    public void render() {

        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        
        screen.clear();
        int xScroll = level.getPlayer().getX() - screen.width/2;
        int yScroll = level.getPlayer().getY() - screen.height/2;

        
        level.render(xScroll, yScroll, screen);    
        level.getPlayer().render(screen);
        
        if(activarMecanica){
            level.mecanica();
            
        }        
       
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = screen.pixels[i];
        }
        
        Graphics g = bs.getDrawGraphics();
        if( g != null){
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            g.setFont(f);
            g.setColor(c);
            g.drawString(level.getText(), (width/2)*scale-50, (height/2)*scale-50);
            g.dispose();
            bs.show();
        }
    }

    public void setTexX(int x) {
        this.x = x;
    }

    public void setTextY(int y) {
        this.y = y;
    }

    public void setTextC(Color c) {
        this.c = c;
    }

    public void setF(Font f) {
        this.f = f;
    }
    public String setText(){
        text=level.getText();
        return text;
    }
        
        
}
