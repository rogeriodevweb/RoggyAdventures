package com.mycompany.roggyadventures;

import javax.swing.JFrame;

public class Jogo {

    public void iniciar() {

        JFrame janela = new JFrame("Roggy Adventures");

        TelaJogo tela = new TelaJogo();

        janela.add(tela);

        janela.setSize(1000, 600);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLocationRelativeTo(null);
        janela.setResizable(false);

        janela.setVisible(true);
    }
}