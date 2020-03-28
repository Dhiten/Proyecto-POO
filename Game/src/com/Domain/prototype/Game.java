/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Domain.prototype;

import com.Domain.prototype.entity.movil.Player;
import com.Domain.prototype.graphics.Screen;
import com.Domain.prototype.input.KeyBoard;
import com.Domain.prototype.input.Mouse;
import com.Domain.prototype.level.Level;
import com.Domain.prototype.level.Level01;

import com.Domain.prototype.level.TileCoordenada;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;
    public static int width = 300;
    public static int height = width / 16 * 9;
    public static int scale = 3;
    public static boolean activarMecanica=false;
    private Level level;
    private Thread thread;
    private boolean running = true;
    private Player player;
    private KeyBoard key;
    private Mouse mouse;
    public JFrame frame;
    private String windowTittle = "Hera's Legacy";
    private TileCoordenada spawnplayer = new TileCoordenada(width/2, height/2);
    private int[] spawnpj = spawnplayer.getXY();
    
    //Se crea un obejto BufferedImage que en sí mismo es una imagen (grupo de pixeles) que posee un Buffer. No puede ser manipulada para usarse por si sola.
    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();//Profundizar
    public static Screen screen;
    

    public Game() {
        Dimension size = new Dimension(width * scale, height * scale);
        setPreferredSize(size);
        frame = new JFrame();
        screen = new Screen(width, height);
        key = new KeyBoard();
        level = Level.level01;
        player = new Player(spawnpj[0],spawnpj[1],key);
        player.init(level);
        addKeyListener(key);
        mouse = new Mouse();
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
    }

    public synchronized void start() {
        thread = new Thread(this, "Display");
        thread.start();
    }
    public synchronized void stop() {
        try {
            thread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
//Esto entra al metodo run una sola vez
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();//1000 ms = 1sg
        final double convert = 1000000000.0 / 120;
        double deltaTime = 0;
        int frames = 0; //Imagenes por segundo
        int uptades = 0; //veces que es llamada la funcion dentro del ciclo
        requestFocus();
        //Y esto se hará tantas veces como la condición se cumpla;
        while (running) {
            long now = System.nanoTime();
            deltaTime += (now - lastTime) / convert;//tiempo real
            lastTime = now;

            //regula las veces que se llama al método uptade
            while (deltaTime >= 1) {
                uptade();
                uptades++;
                deltaTime--;
            }
            render();
            frames++;
            //Para contar refrescar una vez por segundo
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frame.setTitle(windowTittle + " " + uptades + " ups |" + frames + " fps");
                uptades = 0;
                frames = 0;
            }
        }

        stop();
    }
   

    public void uptade() {
        key.uptade();
        player.uptade();
    }

    public void render() {

        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        screen.clear();
        int xScroll = player.x - screen.width/2;
        int yScroll = player.y - screen.height/2;
        
        //Las dos variables de aquí van abajo.
        //System.out.println(xScroll+" || "+yScroll);
        
        level.render(xScroll, yScroll, screen);
        player.render(screen);
        
        //if bool colision = true then renderizar datos en Level01 y pasarlos a screen
        if(activarMecanica){
            //System.out.println(mouse.getMouseX()+"  || "+mouse.getMouseY());
            level.mecanica();
        }
        if(key.restart){
            
            player.x = spawnpj[0];
            player.y = spawnpj[1];
        }
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = screen.pixels[i];
        }
        
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        //clear the frame
        g.dispose();
        //display the buffer
        bs.show();

    }
}
