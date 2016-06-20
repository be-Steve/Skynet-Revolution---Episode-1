package main;

import java.util.*;
import java.io.*;
import java.math.*;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int numberOfGates = in.nextInt(); 
        int numberOfLinks = in.nextInt(); 
        int numberOfGateaways = in.nextInt();
        for (int i = 0; i < numberOfLinks; i++) {
            int gateA = in.nextInt(); 
            int gateB = in.nextInt();
        }
        for (int i = 0; i < numberOfGateaways; i++) {
            int gateaway = in.nextInt(); 
        }
        while (true) {
            int skynetPosition = in.nextInt(); 
            cutLink();
        }
    }
    
    private static void cutLink() {
    	System.out.println("1 2");
    }
}