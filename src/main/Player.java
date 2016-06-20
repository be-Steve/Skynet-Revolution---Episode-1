package main;

import java.util.*;
import java.io.*;
import java.math.*;

class Player {
	private static Map<Integer,Gate> gates;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int numberOfGates = in.nextInt(); 
        int numberOfLinks = in.nextInt(); 
        int numberOfGateaways = in.nextInt();
        gates = new HashMap<Integer, Gate>();
        
        for (int i = 0; i < numberOfLinks; i++) {
        	int gateANumber = in.nextInt(); 
        	int gateBNumber = in.nextInt(); 
        	
        	addGateInGatesListIfNew(gateANumber);
        	addGateInGatesListIfNew(gateBNumber);
        	
        	addLinkBetweenGates(gateANumber, gateBNumber);
        }
        setGateawaysInGates(in, numberOfGateaways);
        
        while (true) {
            Gate skynetGate = gates.get(in.nextInt()); 
        	if (skynetGate.isLinkedToAGateAway()) {
        		cutLink(skynetGate.getGateNumber(), skynetGate.getLinkedGateAway().getGateNumber());
        	}
        	else {
        		cutLink(skynetGate.getGateNumber(), (int) skynetGate.getLinkedGates().toArray()[0]);
        	}
        }
    }

	private static void setGateawaysInGates(Scanner in, int numberOfGateaways) {
		for (int i = 0; i < numberOfGateaways; i++) {
        	gates.get(in.nextInt()).setGateaway(true);
        }
	}
    
    private static void addLinkBetweenGates(int gateANumber, int gateBNumber) {
    	gates.get(gateANumber).addLinkedGate(gates.get(gateBNumber));
    	gates.get(gateBNumber).addLinkedGate(gates.get(gateANumber));
	}

	private static void addGateInGatesListIfNew(int gateNumber) {
    	if (gates.get(gateNumber) == null){
    		Gate gate = new Gate(gateNumber);
        	gates.put(gateNumber, gate);
    	}
    }
    private static void cutLink(int gateA, int gateB) {
    	System.out.println(""+gateA + " " + gateB);
    }
    
    private static class Gate {
	    
	    private int gateNumber;
	    private Set<Integer> linksToGates = new HashSet<Integer>();
	    boolean gateaway = false;
	    
	    public Gate(int gateNumber) {
	        this.gateNumber = gateNumber;
	    }
	
	    public boolean isLinkedToAGateAway() {
	    	for (Integer gate : linksToGates) {
	    		if (gates.get(gate).isGateaway()) {
	    			return true;
	    		}
	    	}
	    	return false;
		}
	    
	    public Set<Integer> getLinkedGates() {
	    	return linksToGates;
	    }
	    
	    public Gate getLinkedGateAway() {
	    	for (Integer gate : linksToGates) {
	    		if (gates.get(gate).isGateaway()) {
	    			return gates.get(gate);
	    		}
	    	}
	    	return null;
		}

		public int getGateNumber() {
	    	return gateNumber;
	    }
	    
	    public boolean isGateaway() {
	    	return gateaway;
	    }
	    
	    public void setGateaway(boolean gateaway) {
	    	this.gateaway = gateaway;
	    }
	    
	    public void addLinkedGate(Gate linkedGate) {
	    	linksToGates.add(linkedGate.getGateNumber());
	    }
	}
}