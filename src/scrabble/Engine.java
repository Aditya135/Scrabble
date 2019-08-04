/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrabble;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author raiad
 */

class Node{
    char c;
    HashMap<Character,Node> subnodes;
    boolean lastchar;
    Node(char c){
        this.c = c;
        lastchar = false;
        subnodes = new HashMap<>();
    }
}

class Trie{
    public HashMap<Character, Node> roots;
    public Trie(String filename)throws IOException{
       roots = new HashMap<>();
       for(char c = 'a';c<='z';c++){
           roots.put(c,new Node(c));
       }
       
       File f = new File(filename);
       BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(filename)));
       String line;
       while((line = br.readLine())!=null){
           line = line.toLowerCase();
           char c = line.charAt(0);
           Node current = roots.get(c);
           
           for(int i=1;i<line.length();i++){
               char ch = line.charAt(i);
               if(!current.subnodes.containsKey(ch)){
                   current.subnodes.put(ch,new Node(ch));
               }
               current = current.subnodes.get(ch);
               if(i==(line.length()-1)){
                   current.lastchar = true;
               }
           }
       }
       br.close();
    }
    public boolean exist(String word){
          if(!roots.containsKey(word.charAt(0))) return false;
          if(word.length()==1) return false;
          Node current = roots.get(word.charAt(0));
          int ind = 1;
          while(ind<=word.length()){
              if(ind==word.length() && current.lastchar) return true;
              if(ind<word.length() && current.subnodes.containsKey(word.charAt(ind))){
                 current = current.subnodes.get(word.charAt(ind));
                 ind++;
                 
              }
              else return false;
          }
          return false;
    }
}

public class Engine {
    public HashMap<String,Boolean> visited;
    public char[][] board;
    public Trie tr;
    public int num_cells;
    private ArrayList<String> discovered_words;
    public Engine(int num_cells,String filename)throws IOException{
        
        visited = new HashMap<>();
        
        this.num_cells = num_cells;
        board =  new char[num_cells][num_cells];
        for(int i=0;i<num_cells;i++){
            for(int j=0;j<num_cells;j++){
                board[i][j] = '\0';
            }
        }
        tr = new Trie(filename);
    }
    public char[] getRandomLetters(int size){
        char[] tmp = new char[size];
        Random rnd = new Random();
        for(int i=0;i<size;i++){
            char c;
            do{
                c = (char) ('a'+rnd.nextInt(26));
            }while(checkLetter(c,tmp));
            tmp[i] = c;
        }
        return tmp;
    }
    
    private boolean checkLetter(char c, char[] ca){
        for(char k: ca){
            if(c==k) return true;
        }
        return false;
    }
    
    public int boardUpdate(int i, int j, char key){
        if(board[i][j]!='\0') return -1;
        else{
            discovered_words = new ArrayList<>();
            board[i][j] = key;
            int origin_i = i;
            int origin_j = j;
            int result = 0;
            String s = "";
            
            int left = i-1;
            int right = i;
            while((left>=0 || right<num_cells) && ((right<num_cells && board[right][j]!='\0') || (left>=0 && board[left][j]!='\0'))){
                if(right<num_cells && board[right][j]!='\0'){
                    s = s+board[right++][j];
                }
                if(left>=0 && board[left][j]!='\0'){
                    s = board[left--][j]+s;
                }
            }
            
            result+=getScore(s)+getScore(new StringBuilder(s).reverse().toString());
            s = "";
            int up = j-1;
            int down = j;
            while((up>=0 && board[origin_i][up]!='\0') || (down<num_cells && board[origin_i][down]!='\0')){
                if(up>=0 && board[origin_i][up]!='\0'){
                    s = s+board[origin_i][up--];
                }
                if(down<num_cells && board[origin_i][down]!='\0'){
                    s = board[origin_i][down++]+s;
                }
            }
            
            result+=getScore(s)+getScore(new StringBuilder(s).reverse().toString());
            
            return result;
        }
    }
    
    public int getScore(String s){
        int result = 0;
        
        for(int i=0;i<s.length();i++){
            for(int j=i+1;j<=s.length();j++){
                String tmp = s.substring(i,j);
                if(!visited.containsKey(tmp) && tr.exist(tmp)){
                    discovered_words.add(tmp);
                    result+=tmp.length();
                    visited.put(tmp, Boolean.TRUE);
                }
            }
        }
        return result;
    }
    
    public String getRecently(){
        StringBuilder sb = new StringBuilder("<html>");
        discovered_words.stream().map((s) -> {
            sb.append(s);
            return s;
        }).forEach((_item) -> {
            sb.append("<br>");
        });
        sb.append("</html>");
        return sb.toString();
    }
}
