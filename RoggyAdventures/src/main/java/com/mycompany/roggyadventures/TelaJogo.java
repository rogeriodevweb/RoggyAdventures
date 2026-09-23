package com.mycompany.roggyadventures;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Rectangle;
import java.util.ArrayList;

public class TelaJogo extends JPanel implements KeyListener {

    // =========================================================
    // TAMANHO DA TELA
    // =========================================================

    private final int LARGURA = 1000;
    private final int ALTURA = 600;

    // =========================================================
    // MUNDO
    // =========================================================

    private final int LARGURA_MUNDO = 5000;

    private int cameraX = 0;

    // =========================================================
    // ROGGY
    // =========================================================

    private int jogadorX = 100;
    private int jogadorY = 400;

    private final int jogadorLargura = 40;
    private final int jogadorAltura = 60;

    private double velocidadeY = 0;

    private final double gravidade = 0.7;
    private final double forcaPulo = -14;

    private final int velocidade = 5;

    private boolean esquerda = false;
    private boolean direita = false;

    private boolean noChao = false;

    // =========================================================
    // JOGO
    // =========================================================

    private int vidas = 3;
    private int pontos = 0;
    private int moedas = 0;

    private boolean iniciou = false;
    private boolean gameOver = false;
    private boolean venceu = false;

    // =========================================================
    // OBJETOS
    // =========================================================

    private ArrayList<Rectangle> plataformas;
    private ArrayList<Rectangle> moedasLista;
    private ArrayList<Inimigo> inimigos;

    // =========================================================
    // TECLADO
    // =========================================================

    private boolean pressionouPulo = false;

    // =========================================================
    // TIMER
    // =========================================================

    private Timer timer;

    // =========================================================
    // CONSTRUTOR
    // =========================================================

    public TelaJogo() {

    setFocusable(true);

    setBackground(new Color(120, 200, 255));

    criarFase();

    configurarTeclas();
}
    // =========================================================
    // INICIAR JOGO
    // =========================================================

    public void iniciarJogo() {

        requestFocusInWindow();

        timer = new Timer(16, e -> atualizar());

        timer.start();
    }

    // =========================================================
    // CRIAR FASE
    // =========================================================

    private void criarFase() {

        plataformas = new ArrayList<>();
        moedasLista = new ArrayList<>();
        inimigos = new ArrayList<>();

        // =====================================================
        // CHÃO PRINCIPAL
        // =====================================================

        plataformas.add(new Rectangle(0, 500, 900, 100));
        plataformas.add(new Rectangle(1000, 500, 700, 100));
        plataformas.add(new Rectangle(1800, 500, 800, 100));
        plataformas.add(new Rectangle(2700, 500, 900, 100));
        plataformas.add(new Rectangle(3700, 500, 1300, 100));

        // =====================================================
        // PLATAFORMAS
        // =====================================================

        plataformas.add(new Rectangle(300, 400, 180, 30));
        plataformas.add(new Rectangle(600, 330, 180, 30));

        plataformas.add(new Rectangle(1100, 390, 200, 30));
        plataformas.add(new Rectangle(1400, 320, 180, 30));

        plataformas.add(new Rectangle(1900, 390, 180, 30));
        plataformas.add(new Rectangle(2200, 300, 180, 30));

        plataformas.add(new Rectangle(2850, 400, 180, 30));
        plataformas.add(new Rectangle(3200, 330, 180, 30));

        plataformas.add(new Rectangle(3900, 390, 200, 30));
        plataformas.add(new Rectangle(4250, 300, 200, 30));

        // =====================================================
        // MOEDAS
        // =====================================================

        adicionarMoeda(350, 350);
        adicionarMoeda(420, 350);
        adicionarMoeda(650, 280);
        adicionarMoeda(720, 280);

        adicionarMoeda(1150, 340);
        adicionarMoeda(1220, 340);
        adicionarMoeda(1450, 270);

        adicionarMoeda(1950, 340);
        adicionarMoeda(2020, 340);
        adicionarMoeda(2250, 250);

        adicionarMoeda(2900, 350);
        adicionarMoeda(2970, 350);
        adicionarMoeda(3250, 280);

        adicionarMoeda(3950, 340);
        adicionarMoeda(4020, 340);
        adicionarMoeda(4300, 250);
        adicionarMoeda(4370, 250);

        // =====================================================
        // INIMIGOS
        // =====================================================

        inimigos.add(new Inimigo(700, 440, 650, 850));
        inimigos.add(new Inimigo(1200, 440, 1050, 1600));
        inimigos.add(new Inimigo(1950, 440, 1850, 2500));
        inimigos.add(new Inimigo(2900, 440, 2750, 3500));
        inimigos.add(new Inimigo(4000, 440, 3750, 4600));
    }

    // =========================================================
    // MOEDA
    // =========================================================

    private void adicionarMoeda(int x, int y) {

        moedasLista.add(new Rectangle(x, y, 25, 25));
    }

    // =========================================================
    // ATUALIZAR JOGO
    // =========================================================

    private void atualizar() {

        if (!iniciou) {

            repaint();

            return;
        }

        if (gameOver || venceu) {

            repaint();

            return;
        }

        moverJogador();

        aplicarGravidade();

        verificarPlataformas();

        atualizarInimigos();

        verificarMoedas();

        verificarInimigos();

        verificarQueda();

        verificarFinal();

        atualizarCamera();

        repaint();
    }

    // =========================================================
    // MOVIMENTO DO JOGADOR
    // =========================================================

    private void moverJogador() {

        if (esquerda) {

            jogadorX -= velocidade;
        }

        if (direita) {

            jogadorX += velocidade;
        }

        if (jogadorX < 0) {

            jogadorX = 0;
        }

        if (jogadorX > LARGURA_MUNDO - jogadorLargura) {

            jogadorX = LARGURA_MUNDO - jogadorLargura;
        }

        if (pressionouPulo && noChao) {

            velocidadeY = forcaPulo;

            noChao = false;
        }

        pressionouPulo = false;
    }

    // =========================================================
    // GRAVIDADE
    // =========================================================

    private void aplicarGravidade() {

        velocidadeY += gravidade;

        jogadorY += velocidadeY;
    }

    // =========================================================
    // COLISÃO COM PLATAFORMAS
    // =========================================================

    private void verificarPlataformas() {

        noChao = false;

        Rectangle jogador = new Rectangle(
                jogadorX,
                jogadorY,
                jogadorLargura,
                jogadorAltura
        );

        for (Rectangle plataforma : plataformas) {

            if (jogador.intersects(plataforma)) {

                if (velocidadeY >= 0
                        && jogadorY + jogadorAltura - velocidadeY <= plataforma.y) {

                    jogadorY = plataforma.y - jogadorAltura;

                    velocidadeY = 0;

                    noChao = true;
                }
            }
        }
    }

    // =========================================================
    // ATUALIZAR INIMIGOS
    // =========================================================

    private void atualizarInimigos() {

        for (Inimigo inimigo : inimigos) {

            inimigo.x += inimigo.velocidade;

            if (inimigo.x <= inimigo.limiteEsquerdo
                    || inimigo.x >= inimigo.limiteDireito) {

                inimigo.velocidade *= -1;
            }
        }
    }

    // =========================================================
    // PEGAR MOEDAS
    // =========================================================

    private void verificarMoedas() {

        Rectangle jogador = new Rectangle(
                jogadorX,
                jogadorY,
                jogadorLargura,
                jogadorAltura
        );

        for (int i = moedasLista.size() - 1; i >= 0; i--) {

            Rectangle moeda = moedasLista.get(i);

            if (jogador.intersects(moeda)) {

                moedasLista.remove(i);

                moedas++;

                pontos += 100;
            }
        }
    }

    // =========================================================
    // COLISÃO COM INIMIGOS
    // =========================================================

    private void verificarInimigos() {

        Rectangle jogador = new Rectangle(
                jogadorX,
                jogadorY,
                jogadorLargura,
                jogadorAltura
        );

        for (Inimigo inimigo : inimigos) {

            Rectangle areaInimigo = new Rectangle(
                    inimigo.x,
                    inimigo.y,
                    inimigo.largura,
                    inimigo.altura
            );

            if (jogador.intersects(areaInimigo)) {

                // Roggy está caindo sobre o inimigo
                if (velocidadeY > 0
                        && jogadorY + jogadorAltura - velocidadeY <= inimigo.y) {

                    inimigo.derrotado = true;

                    velocidadeY = -10;

                    pontos += 200;

                } else {

                    perderVida();

                    return;
                }
            }
        }

        inimigos.removeIf(inimigo -> inimigo.derrotado);
    }

    // =========================================================
    // PERDER VIDA
    // =========================================================

    private void perderVida() {

        vidas--;

        if (vidas <= 0) {

            gameOver = true;

            return;
        }

        jogadorX = 100;
        jogadorY = 400;

        velocidadeY = 0;

        cameraX = 0;
    }

    // =========================================================
    // QUEDA
    // =========================================================

    private void verificarQueda() {

        if (jogadorY > ALTURA + 100) {

            perderVida();
        }
    }

    // =========================================================
    // FINAL DA FASE
    // =========================================================

    private void verificarFinal() {

        if (jogadorX >= 4700) {

            venceu = true;
        }
    }

    // =========================================================
    // CÂMERA
    // =========================================================

    private void atualizarCamera() {

        cameraX = jogadorX - 350;

        if (cameraX < 0) {

            cameraX = 0;
        }

        if (cameraX > LARGURA_MUNDO - LARGURA) {

            cameraX = LARGURA_MUNDO - LARGURA;
        }
    }

    // =========================================================
    // DESENHAR
    // =========================================================

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        if (!iniciou) {

            desenharMenu(g2);

            return;
        }

        desenharCenario(g2);

        desenharPlataformas(g2);

        desenharMoedas(g2);

        desenharInimigos(g2);

        desenharJogador(g2);

        desenharInterface(g2);

        if (gameOver) {

            desenharGameOver(g2);
        }

        if (venceu) {

            desenharVitoria(g2);
        }
    }

    // =========================================================
    // MENU
    // =========================================================

    private void desenharMenu(Graphics2D g) {

        g.setColor(new Color(35, 45, 80));

        g.fillRect(0, 0, LARGURA, ALTURA);

        g.setColor(Color.WHITE);

        g.setFont(new Font("Arial", Font.BOLD, 60));

        String titulo = "ROGGY ADVENTURES";

        int larguraTitulo = g.getFontMetrics().stringWidth(titulo);

        g.drawString(
                titulo,
                (LARGURA - larguraTitulo) / 2,
                200
        );

        g.setFont(new Font("Arial", Font.BOLD, 28));

        String texto = "PRESSIONE ENTER PARA JOGAR";

        int larguraTexto = g.getFontMetrics().stringWidth(texto);

        g.drawString(
                texto,
                (LARGURA - larguraTexto) / 2,
                330
        );

        g.setFont(new Font("Arial", Font.PLAIN, 20));

        g.drawString(
                "← →  Mover",
                390,
                400
        );

        g.drawString(
                "ESPAÇO  Pular",
                390,
                435
        );
    }

    // =========================================================
    // CENÁRIO
    // =========================================================

    private void desenharCenario(Graphics2D g) {

        // Céu
        g.setColor(new Color(120, 200, 255));

        g.fillRect(
                0,
                0,
                LARGURA,
                ALTURA
        );

        // Nuvens
        g.setColor(Color.WHITE);

        for (int i = 0; i < 10; i++) {

            int x = i * 600 - cameraX / 3;
            int y = 80 + (i % 3) * 40;

            g.fillOval(x, y, 80, 40);
            g.fillOval(x + 40, y - 20, 80, 60);
            g.fillOval(x + 90, y, 80, 40);
        }

        // Montanhas
        g.setColor(new Color(90, 170, 100));

        for (int i = 0; i < 8; i++) {

            int x = i * 700 - cameraX / 2;

            int[] pontosX = {
                    x,
                    x + 250,
                    x + 500
            };

            int[] pontosY = {
                    500,
                    230,
                    500
            };

            g.fillPolygon(
                    pontosX,
                    pontosY,
                    3
            );
        }
    }

    // =========================================================
    // PLATAFORMAS
    // =========================================================

    private void desenharPlataformas(Graphics2D g) {

        for (Rectangle plataforma : plataformas) {

            int x = plataforma.x - cameraX;

            // Terra
            g.setColor(new Color(130, 80, 45));

            g.fillRect(
                    x,
                    plataforma.y,
                    plataforma.width,
                    plataforma.height
            );

            // Grama
            g.setColor(new Color(50, 180, 70));

            g.fillRect(
                    x,
                    plataforma.y,
                    plataforma.width,
                    12
            );
        }
    }

    // =========================================================
    // MOEDAS
    // =========================================================

    private void desenharMoedas(Graphics2D g) {

        for (Rectangle moeda : moedasLista) {

            int x = moeda.x - cameraX;

            g.setColor(new Color(255, 210, 0));

            g.fillOval(
                    x,
                    moeda.y,
                    moeda.width,
                    moeda.height
            );

            g.setColor(new Color(255, 240, 100));

            g.drawOval(
                    x,
                    moeda.y,
                    moeda.width,
                    moeda.height
            );
        }
    }

    // =========================================================
    // INIMIGOS
    // =========================================================

    private void desenharInimigos(Graphics2D g) {

        for (Inimigo inimigo : inimigos) {

            int x = inimigo.x - cameraX;

            // Corpo
            g.setColor(new Color(130, 60, 35));

            g.fillOval(
                    x,
                    inimigo.y,
                    inimigo.largura,
                    inimigo.altura
            );

            // Olhos
            g.setColor(Color.WHITE);

            g.fillOval(
                    x + 7,
                    inimigo.y + 8,
                    10,
                    10
            );

            g.fillOval(
                    x + 23,
                    inimigo.y + 8,
                    10,
                    10
            );

            // Pupilas
            g.setColor(Color.BLACK);

            g.fillOval(
                    x + 10,
                    inimigo.y + 11,
                    5,
                    5
            );

            g.fillOval(
                    x + 26,
                    inimigo.y + 11,
                    5,
                    5
            );
        }
    }

    // =========================================================
    // ROGGY
    // =========================================================

    private void desenharJogador(Graphics2D g) {

        int x = jogadorX - cameraX;

        // Corpo
        g.setColor(new Color(30, 100, 220));

        g.fillRoundRect(
                x,
                jogadorY + 20,
                jogadorLargura,
                40,
                10,
                10
        );

        // Cabeça
        g.setColor(new Color(255, 190, 140));

        g.fillOval(
                x + 5,
                jogadorY,
                30,
                32
        );

        // Cabelo
        g.setColor(new Color(80, 45, 25));

        g.fillArc(
                x + 5,
                jogadorY - 3,
                30,
                20,
                0,
                180
        );

        // Olhos
        g.setColor(Color.BLACK);

        g.fillOval(
                x + 12,
                jogadorY + 12,
                4,
                5
        );

        g.fillOval(
                x + 24,
                jogadorY + 12,
                4,
                5
        );

        // Braços
        g.setColor(new Color(255, 190, 140));

        g.fillRect(
                x - 5,
                jogadorY + 25,
                8,
                25
        );

        g.fillRect(
                x + jogadorLargura - 3,
                jogadorY + 25,
                8,
                25
        );

        // Pernas
        g.setColor(Color.DARK_GRAY);

        g.fillRect(
                x + 5,
                jogadorY + 55,
                12,
                10
        );

        g.fillRect(
                x + 24,
                jogadorY + 55,
                12,
                10
        );
    }

    // =========================================================
    // INTERFACE
    // =========================================================

    private void desenharInterface(Graphics2D g) {

        g.setColor(new Color(0, 0, 0, 130));

        g.fillRoundRect(
                15,
                15,
                400,
                60,
                15,
                15
        );

        g.setColor(Color.WHITE);

        g.setFont(new Font("Arial", Font.BOLD, 20));

        g.drawString(
                "❤️ Vidas: " + vidas,
                30,
                42
        );

        g.drawString(
                "🪙 Moedas: " + moedas,
                150,
                42
        );

        g.drawString(
                "⭐ Pontos: " + pontos,
                290,
                42
        );
    }

    // =========================================================
    // GAME OVER
    // =========================================================

    private void desenharGameOver(Graphics2D g) {

        g.setColor(new Color(0, 0, 0, 180));

        g.fillRect(
                0,
                0,
                LARGURA,
                ALTURA
        );

        g.setColor(Color.RED);

        g.setFont(new Font("Arial", Font.BOLD, 70));

        String texto = "GAME OVER";

        int largura = g.getFontMetrics().stringWidth(texto);

        g.drawString(
                texto,
                (LARGURA - largura) / 2,
                250
        );

        g.setColor(Color.WHITE);

        g.setFont(new Font("Arial", Font.BOLD, 25));

        String reiniciar = "PRESSIONE R PARA REINICIAR";

        largura = g.getFontMetrics().stringWidth(reiniciar);

        g.drawString(
                reiniciar,
                (LARGURA - largura) / 2,
                330
        );
    }

    // =========================================================
    // VITÓRIA
    // =========================================================

    private void desenharVitoria(Graphics2D g) {

        g.setColor(new Color(0, 0, 0, 180));

        g.fillRect(
                0,
                0,
                LARGURA,
                ALTURA
        );

        g.setColor(new Color(255, 215, 0));

        g.setFont(new Font("Arial", Font.BOLD, 65));

        String texto = "VOCÊ VENCEU!";

        int largura = g.getFontMetrics().stringWidth(texto);

        g.drawString(
                texto,
                (LARGURA - largura) / 2,
                240
        );

        g.setColor(Color.WHITE);

        g.setFont(new Font("Arial", Font.BOLD, 25));

        String pontosTexto = "Pontuação: " + pontos;

        largura = g.getFontMetrics().stringWidth(pontosTexto);

        g.drawString(
                pontosTexto,
                (LARGURA - largura) / 2,
                300
        );

        String reiniciar = "PRESSIONE R PARA JOGAR NOVAMENTE";

        largura = g.getFontMetrics().stringWidth(reiniciar);

        g.drawString(
                reiniciar,
                (LARGURA - largura) / 2,
                350
        );
    }

    // =========================================================
    // REINICIAR
    // =========================================================

    private void reiniciar() {

        jogadorX = 100;
        jogadorY = 400;

        velocidadeY = 0;

        cameraX = 0;

        vidas = 3;

        pontos = 0;

        moedas = 0;

        gameOver = false;

        venceu = false;

        criarFase();
    }

    // =========================================================
    // TECLADO
    // =========================================================

    @Override
    public void keyPressed(KeyEvent e) {

        int tecla = e.getKeyCode();

        if (tecla == KeyEvent.VK_ENTER && !iniciou) {

            iniciou = true;

            requestFocusInWindow();

            return;
        }

        if (tecla == KeyEvent.VK_R && (gameOver || venceu)) {

            reiniciar();

            return;
        }

        if (!iniciou) {

            return;
        }

        if (tecla == KeyEvent.VK_LEFT
                || tecla == KeyEvent.VK_A) {

            esquerda = true;
        }

        if (tecla == KeyEvent.VK_RIGHT
                || tecla == KeyEvent.VK_D) {

            direita = true;
        }

        if (tecla == KeyEvent.VK_SPACE
                || tecla == KeyEvent.VK_UP
                || tecla == KeyEvent.VK_W) {

            pressionouPulo = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int tecla = e.getKeyCode();

        if (tecla == KeyEvent.VK_LEFT
                || tecla == KeyEvent.VK_A) {

            esquerda = false;
        }

        if (tecla == KeyEvent.VK_RIGHT
                || tecla == KeyEvent.VK_D) {

            direita = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private void configurarTeclas() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // =========================================================
    // CLASSE DOS INIMIGOS
    // =========================================================

    private class Inimigo {

        int x;
        int y;

        int largura = 40;
        int altura = 40;

        int limiteEsquerdo;
        int limiteDireito;

        int velocidade = 2;

        boolean derrotado = false;

        public Inimigo(
                int x,
                int y,
                int limiteEsquerdo,
                int limiteDireito) {

            this.x = x;
            this.y = y;

            this.limiteEsquerdo = limiteEsquerdo;
            this.limiteDireito = limiteDireito;
        }
    }
}