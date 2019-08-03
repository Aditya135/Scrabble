/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrabble;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class App extends JFrame{
    
    int score1_val=0,score2_val=0;
    char[] letters;
    Engine eng;
    public int player_turn=0;
    int num_cells;
    
    private JPanel[][] btn;
    private JLabel[] letters_gui;
    private JLabel player1_score,player2_score;
    private char keypressed;
    private JPanel selected;
    private JLabel player_indicator;
    private JLabel discovered;
    public App(){
        num_cells = 10;
        try{
            eng = new Engine(num_cells);
        }
        catch(Exception e){
            eng = null;
            System.out.println("failed to initialize Engine!");
            e.printStackTrace();
        }
        
        setLayout(null);
        setSize(700,530);
        
        JPanel board = new JPanel();
        board.setBorder(new LineBorder(Color.black));
        board.setBounds(0,0,500,500);
        board.setBackground(Color.gray);
        
        
        
        board.setLayout(new GridLayout(num_cells,num_cells));
        
        btn = new JPanel[num_cells][num_cells];
        for(int i=0;i<num_cells;i++){
            for(int j=0;j<num_cells;j++){
                btn[i][j] = new JPanel();
                btn[i][j].setLayout(new GridLayout());
                btn[i][j].setBorder(new LineBorder(Color.black));
                btn[i][j].setBackground(Color.GRAY);
                JLabel lbl = new JLabel("*",SwingConstants.CENTER);
                btn[i][j].add(lbl);
                
                btn[i][j].addMouseListener(new MouseAdapter(){
                    @Override
                    public void mousePressed(MouseEvent e){
                        Component Source = e.getComponent();
                        if(Source instanceof JPanel){
                           JPanel src = (JPanel) Source;
                           if(selected==null){
                                selected = src;
                                selected.setBackground(Color.DARK_GRAY);
                           }
                           else{
                               selected.setBackground(Color.GRAY);
                               selected = src;
                               selected.setBackground(Color.DARK_GRAY);
                           }
                        }
                    }
                    
                    @Override
                    public void mouseReleased(MouseEvent e){
                        
                    }
                });
                
                board.add(btn[i][j]);
            }
        }
        
        add(board);
        
        
        
        
        JPanel infopanel = new JPanel();
        
        //TODO: implement game engine here
        infopanel.setLayout(null);
        
        
        JPanel wordDisplay = new JPanel();
        wordDisplay.setBounds(0,0,200,150);
        wordDisplay.setBackground(Color.blue);
        wordDisplay.setLayout(new GridLayout(1,5));
        wordDisplay.setBorder(new EmptyBorder(30,30,30,30));
        letters_gui = new JLabel[5];
        for(int i=0;i<5;i++){
            letters_gui[i] = new JLabel("_");
            letters_gui[i].setFont(new Font("Serif", Font.PLAIN, 20));
            letters_gui[i].setForeground(Color.yellow);
            wordDisplay.add(letters_gui[i]);
        }
        
        letters = eng.getRandomLetters(5);
        for(int i=0;i<5;i++){
            letters_gui[i].setText(Character.toString(letters[i]));
        }
        
        infopanel.add(wordDisplay);
        
        
        JPanel playerPanel = new JPanel();
        playerPanel.setBounds(0,150,200,200);
        playerPanel.setBackground(Color.red);
        playerPanel.setLayout(null);
        JPanel indicator_panel = new JPanel();
        indicator_panel.setBounds(0,0,200,50);
        indicator_panel.setBackground(Color.GRAY);
        player_indicator = new JLabel("Player 0");
        player_indicator.setFont(new Font("Serif", Font.PLAIN, 20));
        player_indicator.setForeground(Color.blue);
        indicator_panel.add(player_indicator);
        playerPanel.add(indicator_panel);
        
        JPanel discovered_panel = new JPanel();
        discovered_panel.setBounds(0,50,200,150);
        discovered = new JLabel();
        discovered_panel.add(discovered);
        playerPanel.add(discovered_panel);
        
        playerPanel.setBorder(new EmptyBorder(30,30,30,30));
        infopanel.add(playerPanel);
        
        infopanel.setBounds(500,0,200,350);
        add(infopanel);
        
        
        
        
        JPanel scorepanel = new JPanel();
        scorepanel.setBounds(500,350,200,150);
        scorepanel.setLayout(new GridLayout(2,1));
        scorepanel.setBackground(Color.green);
        
        player1_score = new JLabel("Player 1: 0");
        player1_score.setFont(new Font("Serif", Font.PLAIN, 20));
        player1_score.setForeground(Color.BLUE);
        player2_score = new JLabel("Player 2: 0");
        player2_score.setFont(new Font("Serif", Font.PLAIN, 20));
        player2_score.setForeground(Color.BLUE);
        
        scorepanel.add(player1_score);
        scorepanel.add(player2_score);
        
        scorepanel.setBorder(new EmptyBorder(50,50,50,50));
        
        
        add(scorepanel);
        
        
        
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                keypressed = Character.toLowerCase(e.getKeyChar());
                boolean found = false;
                for(char c: letters){
                    if(c==keypressed)found = true;
                }
                if(found && selected!=null){
                    discovered.setText("");
                    for(Component jc : selected.getComponents()){
                        if(jc instanceof JLabel){
                            if(updateEngineBoard(selected, keypressed)){
                                ((JLabel) jc).setText(Character.toString(keypressed));
                                if(player_turn==1){
                                    player_turn = 0;
                                    player_indicator.setText("Player 0");
                                    letters = eng.getRandomLetters(5);
                                    for(int i=0;i<5;i++){
                                        letters_gui[i].setText(Character.toString(letters[i]));
                                    }
                                }
                                else{
                                    player_turn = 1;
                                    player_indicator.setText("Player 1");
                                    letters = eng.getRandomLetters(5);
                                    for(int i=0;i<5;i++){
                                        letters_gui[i].setText(Character.toString(letters[i]));
                                    }
                                }
                                
                            }
                        }
                    }
                    selected.setBackground(Color.gray);
                    selected = null;
                }
                else{
                    discovered.setText("Invalid!");
                }
                keypressed = '\0';
            }

            @Override
            public void keyPressed(KeyEvent e) {
                keypressed = e.getKeyChar();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keypressed = '\0';
            }
            
        });
    }
    
    public boolean updateEngineBoard(JPanel source,char keypressed){
        for(int i=0;i<num_cells;i++){
            for(int j=0;j<num_cells;j++){
                if(btn[i][j].equals(source)){
                    
                    int res = eng.boardUpdate(i,j,keypressed);
                    if(res==-1) return false;
                    
                    if(res>0){
                        discovered.setText(eng.getRecently());
                        if(player_turn==0){
                            score1_val+=res;
                            player1_score.setText("Player 1: "+score1_val);
                            System.out.println("scored 1");
                            System.out.println(res);
                            
                        }
                        else{
                            score2_val+=res;
                            player2_score.setText("Player 2: "+score2_val);
                            System.out.println("scored 2");
                            System.out.println(res);
                            
                        }
                        return true;
                    }
                    
                    else return true;
                }
            }
        }
        return false;
    }
}