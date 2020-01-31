package com.github.sarxos.example1;

//imports
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.Row;

//This class is specialized to work for the 5866 Scouting App. 
//This class handles all the excell capabilities needed. 
public class ExcelWork{

	//private instance variables
	private String myName;
	private int rownum;
	private Map<String, Object[]> data;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	//default constructor
	public ExcelWork(String name){
		myName = name;
		rownum = 2;
	//Blank workbook
	workbook = new XSSFWorkbook();
	//Create a blank sheet
	sheet = workbook.createSheet(myName);
	//sheet with the column headers needed
	data = new TreeMap<String, Object[]>();
	data.put("1", new Object[] {"Team Number", "Team Name", "Balls Shot", "Stage", "Rotation Control", "Position Control", "Hang", "notes"});
	}

	//constructor for PitScouting
	public ExcelWork(String name, Boolean isPit){
		myName = name;
		rownum = 2;
	//Blank workbook
	workbook = new XSSFWorkbook();
	//Create a blank sheet
	sheet = workbook.createSheet(myName);
	//sheet with the column headers needed
	data = new TreeMap<String, Object[]>();
	data.put("1", new Object[] {"Team Number", "Team Name", "Weight", "Achievements", "Struggles", "Veterans", "Adjustable Hang", "Observations"});
	}

	//puts match data in data treemap
	public void putInMatchData(int teamNumber, String teamName, int vara, int varb, String varc, String vard, String vare, String notes){
		data.put(rownum + "",new Object[] {teamNumber, teamName, vara, varb, varc, vard, vare, notes});
		rownum++;
	}

	//puts in pit data in data treemap
	public void putInPitData(int teamNumber, String teamName, int vara, String varb, String varc, int vard, String vare, String observations){
		
		data.put(rownum + "",new Object[] {teamNumber, teamName, vara, varb, varc, vard, vare, observations});
		rownum++;
	}

	//puts data from data treemap in spreadsheet
	public void putInSheet(){

	int rown = 0; 
	Set<String> keyset = data.keySet();
	for (String key : keyset)
		{
		Row row = sheet.createRow(rown++);
		Object [] objArr = data.get(key);
		int cellnum = 0;
			for (Object obj : objArr)
			{
		   		Cell cell = row.createCell(cellnum++);
			   	if(obj instanceof String)
					cell.setCellValue((String)obj);
				else if(obj instanceof Integer)
					cell.setCellValue((Integer)obj);
			}
		}
	}   
			 
	//Writes spreadsheet to a file. The file is located in project directory.
	public void writeToFile(){

    try{
		//Write the workbook in file system
		FileOutputStream out = new FileOutputStream(new File(myName + ".xlsx"));
		workbook.write(out);
		out.close();
		System.out.println(myName + ".xlsx written successfully on disk.");
		} 
	catch (Exception f){
		f.printStackTrace();
		}
	}

	//name accessor
	public String getName(){
		return myName;
	}

	//row accessor
	public int getRowNum(){
		return rownum;
	}

	//data accessor
	public Map<String, Object[]> getData(){
		return data;
	}   

	//sheet accessor
	public XSSFSheet getSheet(){
		return sheet;
	}

	//adds a row to the sheet
	public void addRow(){
		rownum++;
	}
}