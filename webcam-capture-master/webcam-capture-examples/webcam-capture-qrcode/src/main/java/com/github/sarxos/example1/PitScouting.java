package com.github.sarxos.example1;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.*;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

//This class is a near copy of MatchScouting, but handles the different fields of pit scouting
//Handles the GUI, Reads QRCodes, then writes QRCode data to an excell document
public class PitScouting extends JFrame implements Runnable, ThreadFactory {

	//private instance variables
	private static final long serialVersionUID = 6441489157408381878L;
	private Executor executor = Executors.newSingleThreadExecutor(this);
	private Webcam webcam = null;
	private WebcamPanel panel = null;
	private JTextArea textarea = null;
	private JButton submit = null;
	private JButton back;
	private String teamName, varB, varC, varE, notes;
	private int teamNumber, varA, varD;
	private ExcelWork excel;
	private String myName;

	//constructor
	public PitScouting(String name) {
		super();
		myName = name;
		setLayout(new FlowLayout());
		setTitle("Pit Scouting");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension size = WebcamResolution.QVGA.getSize();
		webcam = Webcam.getWebcams().get(0);
		webcam.setViewSize(size);
		panel = new WebcamPanel(webcam);
		panel.setPreferredSize(size);
		panel.setFPSDisplayed(true);
		textarea = new JTextArea();
		textarea.setEditable(false);
		textarea.setPreferredSize(size);
		submit = new JButton("Submit");
		back = new JButton("Back");
		add(panel);
		add(textarea);
		add(submit);
		add(back);
		pack();
		setVisible(true);
		excel = new ExcelWork(myName, true);

		//back button will go back to Selection Screen
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){  
				new SelectScreen(myName);	
				webcam.close();
				dispose();
			}
		});
		
		executor.execute(this);
	}

	//what this class will do when ran
	@Override
	public void run() {

		do {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Result result = null;
			BufferedImage image = null;

			//if webcam is on
			if (webcam.isOpen()) {
				//if webcam can see an image
				if ((image = webcam.getImage()) == null) {
					continue;
				}

				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

				//try to decode QRCodes infront of webcam
				try {
					result = new MultiFormatReader().decode(bitmap);
				} catch (NotFoundException e) {
					// fall thru, it means there is no QR code in image
				}
			}

			//QRCode was decoded and there was data inside
			if (result != null) {
				//sets data in textfield and prints to console
				textarea.setText(result.getText());
				System.out.println(result.getText());

				// gets team number
				teamNumber = Integer.parseInt(result.getText().substring(10, result.getText().indexOf("\n")));
				// gets team name
				teamName = result.getText().substring(result.getText().indexOf("\n") + 11,
						StringUtils.ordinalIndexOf(result.getText(), "\n", 2));
				// gets Weight
				varA = Integer.parseInt(result.getText().substring(StringUtils.ordinalIndexOf(result.getText(), "\n", 2) + 15,
						StringUtils.ordinalIndexOf(result.getText(), "\n", 3)));
				// gets Achievements
				varB = result.getText().substring(StringUtils.ordinalIndexOf(result.getText(), "\n", 3) + 22,
						StringUtils.ordinalIndexOf(result.getText(), "\n", 4));
				// gets Struggles
				varC = result.getText().substring(StringUtils.ordinalIndexOf(result.getText(), "\n", 4) + 19,
						StringUtils.ordinalIndexOf(result.getText(), "\n", 5));
				// gets veterans
				varD = Integer.parseInt(result.getText().substring(StringUtils.ordinalIndexOf(result.getText(), "\n", 5) + 11,
						StringUtils.ordinalIndexOf(result.getText(), "\n", 6)));
				// gets Adjustable Hang
				varE = result.getText().substring(StringUtils.ordinalIndexOf(result.getText(), "\n", 6) + 17,
						StringUtils.ordinalIndexOf(result.getText(), "\n", 7));
				// gets observations
				notes = result.getText().substring(StringUtils.ordinalIndexOf(result.getText(), "\n", 7) + 14);
				excel.putInPitData(teamNumber, teamName, varA, varB, varC, varD, varE, notes);
				excel.putInSheet();

				//5 second delay to prevent copies of data in excell document
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//adds empty row for next QRCode
				excel.addRow();
			}
				//submit will write the excell document to a file.
				//File is stored in project directory, then the project will close
				submit.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){  
						excel.writeToFile();
						System.exit(0);			
					}
				});	
	}
	//fall thru
	while (true);
}

	//needed to use interface runnable
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "example-runner");
		t.setDaemon(true);
		return t;
	}
}