package com.example.scoutingprogram5866;

public class InData {
    private String teamNum,teamName, varA, varB, varC, varD, varE, notes;

    public InData(){
        teamNum ="";
        teamName ="";
        varA ="";
        varB ="";
        varC ="";
        varD ="";
        varE ="";
        notes ="";
    }

    public InData(String teamNum, String teamName, String varA, String varB, String varC, String varD, String varE, String notes) {
        this.teamNum = teamNum;
        this.teamName = teamName;
        this.varA = varA;
        this.varB = varB;
        this.varC = varC;
        this.varD = varD;
        this.varE = varE;
        this.notes = notes;
    }


    public void setTeamNum(String teamNum) {
        this.teamNum = teamNum;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setVarA(String varA) {
        this.varA = varA;
    }

    public void setVarB(String varB) {
        this.varB = varB;
    }

    public void setVarC(String varC) {
        this.varC = varC;
    }

    public void setVarD(String varD) {
        this.varD = varD;
    }

    public void setVarE(String varE) {
        this.varE = varE;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String printData(){
        return "Team num: " + teamNum +
                "\nTeam name: " + teamName +
                "\nVar a: " + varA +
                "\nVar b: " + varB +
                "\nVar c: " + varC +
                "\nVar d: " + varD +
                "\nVar e: " + varE +
                "\nNotes: " +  notes;
    }
}
