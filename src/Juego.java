import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Juego extends JPanel {
    Laberinto laberinto = new Laberinto();
    Personaje personaje = new Personaje();
    private String nombreJugador;
    private long tiempoInicio;
    private boolean juegoEnCurso = false;
    private static int nivel=1;
    private static final String RANKING_FILE = "ranking.txt";
    private Ranking ranking = new Ranking();
    private Clip musicClip;

    public Juego() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                personaje.teclaPresionada(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        setFocusable(true);
    }

    public void paint(Graphics grafico) {
        laberinto.paint(grafico);
        personaje.paint(grafico);
        paintTiempoNivel(grafico);
    }

    public void paintTiempoNivel(Graphics grafico){
        long tiempoTranscurrido = (System.currentTimeMillis()-tiempoInicio)/1000;

        Font score=new Font("Arial", Font.BOLD,30);
        grafico.setFont(score);
        grafico.setColor(Color.black);
        grafico.drawString("Tiempo: "+tiempoTranscurrido,100,30);
        grafico.drawString("Nivel: "+nivel, 570,30);
    }
    public void pedirNombreJugador() {
        nombreJugador = JOptionPane.showInputDialog("Ingrese su nombre:");
        if (nombreJugador == null || nombreJugador.isEmpty()) {
            nombreJugador = "Anónimo";
        }
    }

    private void iniciarTemporizador() {
        tiempoInicio = System.currentTimeMillis();
    }
    public static int cambiaNivel(){
        return nivel++;
    }

    public static int obtieneNivel(){
        return nivel;
    }
    private void cargarRanking() {
        List<String> rankingList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RANKING_FILE))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                rankingList.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ranking.actualizarRankingDesdeArchivo(rankingList);
    }
    private void guardarRanking() {
        List<String> rankingList = ranking.obtenerRanking();
        try (FileWriter writer = new FileWriter(RANKING_FILE)) {
            for (String entry : rankingList) {
                writer.write(entry + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarRanking() {
        List<String> rankingList = ranking.obtenerRanking();
        StringBuilder rankingStr = new StringBuilder("Ranking:\n");
        int i = 1;
        for(String entry : rankingList){
            rankingStr.append(i++).append(". ").append(entry).append("\n");
        }
        JOptionPane.showMessageDialog(null,rankingStr.toString());
    }

    private void finalizarJuego() {
        long tiempoTotal = (System.currentTimeMillis() - tiempoInicio) / 1000;
        ranking.actualizarRanking(nombreJugador, tiempoTotal);
        guardarRanking();
        juegoEnCurso = false;
        detenerMusica();
    }
    public void MenuPrincipal() {
        juegoEnCurso= false;
        cargarRanking();
        String[] opcionesMenu = {"Jugar", "Ver Ranking", "Salir"};
        int opcionSeleccionada = JOptionPane.showOptionDialog(
                null,
                "Bienvenido al Laberinto XTREME",
                "Menú Principal",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcionesMenu,
                opcionesMenu[0]);


        switch (opcionSeleccionada) {
                case 0: // Jugar
                    nivel = 1;
                    tiempoInicio=0;
                    iniciarJuego();
                    break;
                case 1: // Ver Ranking
                    mostrarRanking();
                    MenuPrincipal();
                    break;
                case 2: // Salir
                    System.exit(0);
                    break;
        }
    }

    private void iniciarJuego() {
        pedirNombreJugador();
        playMusic("laberinto_music.wav", true);
        JOptionPane.showMessageDialog(null, "¿Listo para jugar?");
        JFrame miVentana = new JFrame("Laberinto XTREME");
        miVentana.add(this);
        iniciarTemporizador();
        miVentana.setSize(940, 560);
        miVentana.setLocation(300, 200);
        miVentana.setVisible(true);
        miVentana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        juegoEnCurso=true;

        while (juegoEnCurso) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
            }
            miVentana.repaint();

            if (obtieneNivel() > 4) {
                JOptionPane.showMessageDialog(null, "¡Felicidades! has completado todos los niveles");
                this.finalizarJuego();
                miVentana.setVisible(false);
                MenuPrincipal();
            }
        }
    }

    private void playMusic(String musicFilePath, boolean loop) {
        try {
            File musicFile = new File(musicFilePath);
            if (musicFile.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicFile);
                musicClip = AudioSystem.getClip();
                musicClip.open(audioInput);
                if(loop){
                    musicClip.loop(Clip.LOOP_CONTINUOUSLY);
                }
                musicClip.start();
            } else {
                System.out.println("Archivo de música no encontrado: " + musicFilePath);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void detenerMusica(){
        if(musicClip != null){
            musicClip.stop();
            musicClip.close();
        }
    }
    public static void main(String[] args) {
        Juego game = new Juego();
        game.MenuPrincipal();
    }
}

