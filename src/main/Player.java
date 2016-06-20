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
            Gate gateToCut = getGateWithTheMostSimpleLinkedGateaways();
        	if (skynetGate.isLinkedToAGateAway()) {
        		cutLink(skynetGate.getGateNumber(), skynetGate.getLinkedGateAway().getGateNumber());
        	}
        	else if (skynetGate.getLinkToKillSkynet() != null) {
        		cutLink(skynetGate.getLinkToKillSkynet().getGateNumber() , 
        				skynetGate.getGateNumber());
        	}
        	else if (gateToCut != null && gateToCut.getRandomLinkedNoGateAway() != null){
            	cutLink(gateToCut.getGateNumber(),gateToCut.getRandomLinkedNoGateAway().getGateNumber());
        	}
        	else {
        		gateToCut = cutRandomLink();
            	cutLink(gateToCut.getGateNumber(),gateToCut.getRandomLink().getGateNumber());        		
        	}
        }
    }
	private static Gate cutRandomLink() {
		for (Gate gate : gates.values()) {
			if (gate.getRandomLink()!= null) {
				return gate;
			}
		}
		return null;
	}
	//System.err.println
    private static Gate getGateWithTheMostSimpleLinkedGateaways() {
    	int maxSimpleLinkedGateaways = 0;
    	Gate gateWithMaxSimpleLinkedGateawatys = null;
    	for (Gate gate : gates.values()) {
    		int tempMaxSimpleLinkedGateaways = gate.getAmountOfSimpleLinkedGateaways();
    		if (maxSimpleLinkedGateaways < tempMaxSimpleLinkedGateaways) {
    			maxSimpleLinkedGateaways = tempMaxSimpleLinkedGateaways;
    			gateWithMaxSimpleLinkedGateawatys = gate;
    		}
    	}
    	return gateWithMaxSimpleLinkedGateawatys;
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
    	gates.get(gateA).removeGate(gateB);
    	gates.get(gateB).removeGate(gateA);
    }
    
    private static class Gate {
	    
	    private int gateNumber;
	    private Set<Integer> linksToGates = new HashSet<Integer>();
	    boolean gateaway = false;
	    
	    public Gate(int gateNumber) {
	        this.gateNumber = gateNumber;
	    }
	    
	    public Gate getLinkToKillSkynet() {
			if (hasOnlyOneLink() && getRandomLink().getLinkedGates().size() == 2) {
				return getRandomLink();
			}
			else if (getLinkedGates().size() == 2) {
				Gate gateFirstSide = gates.get(getLinkedGates().toArray()[0]);
				Gate gateSecondSide = gates.get(getLinkedGates().toArray()[1]);
				if (gateFirstSide.closedRecursiveLink(getGateNumber()) && gateSecondSide.getRandomLink().getLinkedGates().size() == 2){
					return gateSecondSide;
				}
				else if (gateSecondSide.closedRecursiveLink(getGateNumber()) && gateFirstSide.getRandomLink().getLinkedGates().size() == 2){
					return gateFirstSide;
				}
			}
			return null;
		}
	    
	    private boolean closedRecursiveLink(int sourceGate) {
	    	if (hasOnlyOneLink()) {
	    		return true;
	    	}
	    	else if (getLinkedGates().size() == 2){
	    		for (int gateNumber : getLinkedGates()) {
	    			if (gateNumber != sourceGate) {
	    				return true;
	    			}
	    		}
	    	}
	    	return false;
	    }

		public void removeGate(int gateB) {
			linksToGates.remove(gateB);
		}

		public int getAmountOfSimpleLinkedGateaways() {
	    	return getRecursiveAmountOfSimpleLinkedGateaways(0);
	    }
	    
	    private int getRecursiveAmountOfSimpleLinkedGateaways(int fromGate) {
	    	int amountOfSimpleLinkedGateaways=0;
	    	if (hasOnlyOneLinkWithoutGateawaysAndNotFromSourceGate()) {
	    	for (Integer gate : linksToGates) {
	    		if (gate != fromGate) {
	    			if (gates.get(gate).isGateaway()) {
	    				amountOfSimpleLinkedGateaways++;
	    			}
	    			else {
	    				amountOfSimpleLinkedGateaways += gates.get(gate).getRecursiveAmountOfSimpleLinkedGateaways(getGateNumber());
	    			}
	    		}
	    	}
	    	}
	    	return amountOfSimpleLinkedGateaways;
	    }
	
	    private boolean hasOnlyOneLinkWithoutGateawaysAndNotFromSourceGate() {
			return isLinkedToAGateAway()? linksToGates.size()<=3 : linksToGates.size()<=2;
		}
	    
	    public boolean hasOnlyOneLink() {
	    	return linksToGates.size() == 1;
	    }
	    
	    public Integer getLinkedGateNumberNotMatching(int gateNumber) {
	    	for (Integer gate : linksToGates) {
	    		if (gate != gateNumber)
	    			return gate;
	    		}
	    	return null;
	    }
	    
	    public Gate getRandomLinkedNoGateAway() {
	    	for (Integer gate : linksToGates) {
	    		if (!gates.get(gate).isGateaway()) {
	    			return gates.get(gate);
	    		}
	    	}
	    	return null;
	    }
	    
	    public Gate getRandomLink() {
	    	for (Integer gate : linksToGates) {
	    			return gates.get(gate);
	    	}
	    	return null;
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