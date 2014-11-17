package carnegie.bioinfo.common.util;

import java.util.Random;

class RandDemo {
    public static void main(String args[]) {
	int numOfRan = 10000;
	double sd = 3;
	double mean = 11;
	
	double calculatedSd = 0;
	Random r = new Random();
	double val;
	double sum = 0;
	int bell[] = new int[10];
	for (int i = 0; i < numOfRan; i++) {
	    val = r.nextGaussian()*sd+mean;
	    double temp = Math.pow(mean-val,2);
	    calculatedSd += temp;
	    sum += val;
	    double t = -2;
	    for (int x = 0; x < 10; x++, t += 0.5)
		if (val < t) {
		    bell[x]++;
		    break;
		}
	}
	System.out.println("Average of values: " + (sum / numOfRan));
	System.out.println("SD: " + Math.sqrt(calculatedSd/numOfRan));
	// display bell curve, sideways
	for (int i = 0; i < 10; i++) {
	    for (int x = bell[i]; x > 0; x--)
		System.out.print("*");
	    System.out.println();
	}
    }
}
