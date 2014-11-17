package carnegie.bioinfo.common.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MathUtil {

	static public Random random = new Random();

	// for test
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	}

	// min number = 1 ~
	public static int getRandomIntNumber(double maxNumber) {
		int randomNumber = (int) Math.floor((random.nextFloat()) * maxNumber) + 1;

		return randomNumber;
	}

	public static double getRandomDoubleNumber(double maxNumber) {
		double randomNumber = random.nextDouble() * maxNumber;
		return randomNumber;
	}

	public static double getAvg(List<Double> aSPLlist) {
		double avg = 0;

		if (aSPLlist.size() == 0)
			return avg;

		int count = 0;
		double total = 0;
		Iterator<Double> iterator = aSPLlist.iterator();
		while (iterator.hasNext()) {
			Double concentration = iterator.next();
			count++;
			total = total + concentration;
		}

		avg = total / count;
		return avg;
	}
}
