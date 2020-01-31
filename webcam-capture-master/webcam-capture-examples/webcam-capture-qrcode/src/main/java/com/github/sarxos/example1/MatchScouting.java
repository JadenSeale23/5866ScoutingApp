package com.github.sarxos.example1;

//imports
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

//This class builds the GUI, Scans QRCodes, and puts data from the QRCodes into an Excel Spreadsheet  
public class MatchScouting extends JFrame implements Runnable, ThreadFactory {

	//private instance variables
	private static final long serialVersionUID = 6441489157408381878L;
	private Executor executor = Executors.newSingleThreadExecutor(this);
	private Webcam webcam = null;
	private WebcamPanel panel = null;
	private JTextArea textarea = null;
	private JButton submit = null;
	private JButton back;
	private String teamName, rotation, position, hang, notes;
	private int teamNumber, ballsScored, stage;
	private ExcelWork excel;
	private String myName;

	//default constructor
	public MatchScouting(String name) {
		super();
		myName = name;
		setLayout(new FlowLayout());
		setTitle("Match Scouting");
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

		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){  
				new SelectScreen(myName);
				webcam.close();	
				dispose();
			}
		});

		excel = new ExcelWork(myName);
		executor.execute(this);
	}

	//defines what happens when this class is ran
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

			//if the webcam is on
			if (webcam.isOpen()) {

				//if the webcam can see an image
				if ((image = webcam.getImage()) == null) {
					continue;
				}

				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

				//trys to decode a QRCode infront the webcam
				try {
					result = new MultiFormatReader().decode(bitmap);
				} catch (NotFoundException e) {
					// fall thru, it means there is no QR code in image
				}
			}

			//if the QRCode is scanned and has data
			if (result != null) {

				//puts the scanned data in a text area on the side and prints to console
				textarea.setText(result.getText());
				System.out.println(result.getText());

				// gets team number
				teamNumber = Integer.parseInt(result.getText().substring(10, result.getText().indexOf("\n")));
				// gets team name
				teamName = result.getText().substring(result.getText().indexOf("\n") + 12,
						StringUtils.ordinalIndexOf(result.getText(), "\n", 2));
				// gets var a
				ballsScored= Integer.parseInt(result.getText().substring(StringUtils.ordinalIndexOf(result.getText(), "\n", 2) + 15,
						StringUtils.ordinalIndexOf(result.getText(), "\n", 3)));
				// gets var b
				stage = Integer.parseInt(result.getText().substring(StringUtils.ordinalIndexOf(result.getText(), "\n", 3) + 8,
						StringUtils.ordinalIndexOf(result.getText(), "\n", 4)));
				// gets var c
				rotation = result.getText().substring(StringUtils.ordinalIndexOf(result.getText(), "\n", 4) + 19,
						StringUtils.ordinalIndexOf(result.getText(), "\n", 5));
				// gets var d
				position= result.getText().substring(StringUtils.ordinalIndexOf(result.getText(), "\n", 5) + 19,
						StringUtils.ordinalIndexOf(result.getText(), "\n", 6));
				// gets var e
				hang = result.getText().substring(StringUtils.ordinalIndexOf(result.getText(), "\n", 6) + 7,
						StringUtils.ordinalIndexOf(result.getText(), "\n", 7));
				// gets notes
				notes = result.getText().substring(StringUtils.ordinalIndexOf(result.getText(), "\n", 7) + 8);
				//puts the data from the QRCode into the excell document
				excel.putInMatchData(teamNumber, teamName, ballsScored, stage, rotation, position, hang, notes);
				excel.putInSheet();
				//5 second pause to prevent copies of data in excell document
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//add an empty row for next QRCode
				excel.addRow();
			}
				//submit button will write the excell document to a file.
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

	//needed to override to use interface runnable
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "example-runner");
		t.setDaemon(true);
		return t;
	}
	
}