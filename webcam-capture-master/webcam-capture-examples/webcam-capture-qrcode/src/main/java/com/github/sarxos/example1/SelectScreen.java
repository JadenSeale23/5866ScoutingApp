package com.github.sarxos.example1;

//imports
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.*;

//this class handles the GUI of the intial screen or select screen
public class SelectScreen {

    //private instance variables
    JFrame main;
    JPanel panel;
    JButton match, pit;
    String myName;

    //constructor
    public SelectScreen(String name) {
        myName = name;
        main = new JFrame("Selection Screen");
        panel = new JPanel();
        match = new JButton("To Match Scouting");
        pit = new JButton("To Pit Scouting");
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setBounds(0, 0, 250, 100);
        main.setLayout(new FlowLayout());
        main.add(panel);
        main.add(match);
        main.add(pit);
        main.pack();
        main.setVisible(true);

        //match button will go to the MatchScouting GUI
        match.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){  
                new MatchScouting(myName);
                main.dispose();		
            }
        });

        //pit button will go to the PitScouting GUI
        pit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){  
                new PitScouting(myName);
                main.dispose();		
            }
        });	
    }
}