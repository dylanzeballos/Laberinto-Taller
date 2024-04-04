import java.awt.*;
import java.awt.event.KeyEvent;

public class Personaje {
    Laberinto lab=new Laberinto();
    int x=40;
    int y =40;
    int ancho =40;
    int alto=40;
    int movimiento=40;

    public void paint(Graphics grafico){
        grafico.setColor(Color.blue);
        grafico.fillOval(x, y, ancho, alto);
        grafico.setColor(Color.black);
        grafico.drawOval(x, y, ancho, alto);

        //ojos
        grafico.setColor(Color.white);
        grafico.fillOval(x+7,y+8,13,13);
        grafico.fillOval(x+21,y+8,13,13);
        grafico.setColor(Color.black);
        grafico.fillOval(x+11,y+12,6,6);
        grafico.fillOval(x+25,y+12,6,6);

        //boca
        grafico.setColor(Color.black);
        grafico.fillOval(x+7,y+24,27,7);
    }

    public void teclaPresionada(KeyEvent evento){
        int [][]laberinto=lab.obtieneLaberinto();

        if(evento.getKeyCode()==37){//izquierda
            if(laberinto[y/40][(x/40)-1]!=1){
                x=x-movimiento;
            }
        }
        if(evento.getKeyCode()==39){//derecha
            if(laberinto[y/40][(x/40)+1]!=1){
                x=x+movimiento;
            }
        }
        if(evento.getKeyCode()==40){//abajo
            if(laberinto[(y/40)+1][x/40]!=1){
                y=y+movimiento;
            }
        }
        if(evento.getKeyCode()==38){//arriba
            if(laberinto[(y/40)-1][x/40]!=1){
                y=y-movimiento;
            }
        }
        //si llega final
        if(x==840 && y==440){
            Juego.cambiaNivel();
            x=40;
            y=40;
        }
    }

}


