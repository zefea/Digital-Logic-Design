/*
 * Digital Logic Design 
 * Project Part1
 * 
 * 
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Assembler {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String fileName = "input.txt";
		String[][] finput = readInputFile(fileName);

		FileWriter out = new FileWriter("output.txt");
		out.write("v2.0 raw\n");
		convertToHex(finput, out);
		out.close();
	}

	// read input file
	public static String[][] readInputFile(String fileName) {
		int maxNum = 4;
		ArrayList<String> input = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(new File(fileName));
			while (scanner.hasNextLine()) {
				input.add(scanner.nextLine());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String[][] finput = new String[input.size()][maxNum];
		for (int i = 0; i < input.size(); i++) {

			int endIndex = input.get(i).indexOf(" ");
			String temp = input.get(i);
			for (int j = 0; j < maxNum; j++) {

				if (endIndex == -1) {
					endIndex = 0;
					finput[i][j] = temp;
				} else if (endIndex == 0)
					finput[i][j] = "NULL";
				else {

					String temp2 = temp.substring(0, endIndex);
					temp = temp.substring(endIndex + 1);
					finput[i][j] = temp2;
					endIndex = temp.indexOf(",");
				}
				// System.out.println(finput[i][j]);
			}
			// System.out.println(" ");
		}
		return finput;
		// System.out.println(" ");
	}

	// convert to hex
	public static void convertToHex(String input[][], FileWriter out) throws Exception {

		String hex = "";
		String binary = "";

		for (int i = 0; i < input.length; i++) {
			System.out.println("**************************");
			System.out.println(input[i][0]);
			String opCode = input[i][0];

			if (opCode.equalsIgnoreCase("LD") || opCode.equalsIgnoreCase("ST")) {

				if (opCode.equalsIgnoreCase("LD"))
					binary = "0100";
				else
					binary = "0101";

				String register = Integer.toBinaryString(Integer.parseInt(String.valueOf(input[i][1].charAt(1))));
				register = extendZero(register, 3);
				binary += register;

				int value = Integer.parseInt(input[i][2]);
				String address = signExtend(value, 9);
				binary += address;

			} else if (opCode.equalsIgnoreCase("ADDI") || opCode.equalsIgnoreCase("ANDI")) {

				if (opCode.equalsIgnoreCase("ADDI"))
					binary = "0011";
				else
					binary = "0001";

				String DR = Integer.toBinaryString(Integer.parseInt(String.valueOf(input[i][1].charAt(1))));
				DR = extendZero(DR, 3);

				binary += DR;
				String SR = Integer.toBinaryString(Integer.parseInt(String.valueOf(input[i][2].charAt(1))));

				SR = extendZero(SR, 3);
				binary += SR;

				int immediateValue = Integer.parseInt(input[i][3]);
				String immediate = signExtend(immediateValue, 6);
				binary += immediate;
			} else if (opCode.equalsIgnoreCase("ADD") || opCode.equalsIgnoreCase("AND")) {

				if (opCode.equalsIgnoreCase("ADD"))
					binary = "0010";
				else
					binary = "0000";

				String DR = Integer.toBinaryString(Integer.parseInt(String.valueOf(input[i][1].charAt(1))));
				DR = extendZero(DR, 3);

				binary += DR;
				String SR1 = Integer.toBinaryString(Integer.parseInt(String.valueOf(input[i][2].charAt(1))));

				SR1 = extendZero(SR1, 3);
				binary += SR1;

				String SR2 = Integer.toBinaryString(Integer.parseInt(String.valueOf(input[i][3].charAt(1))));
				SR2 = extendZero(SR2, 6);
				binary += SR2;

			} else if (opCode.equalsIgnoreCase("CMP")) {

				binary = "0110";
				binary += "0000";

				String SR1 = Integer.toBinaryString(Integer.parseInt(String.valueOf(input[i][1].charAt(1))));

				SR1 = extendZero(SR1, 4);
				binary += SR1;

				String SR2 = Integer.toBinaryString(Integer.parseInt(String.valueOf(input[i][2].charAt(1))));
				SR2 = extendZero(SR2, 4);
				binary += SR2;

			} else if ((opCode.charAt(0)) == 'J') {

				if (opCode.equalsIgnoreCase("JUMP"))
					binary = "1000";
				else if (opCode.equalsIgnoreCase("JE"))
					binary = "1001";
				else if (opCode.equalsIgnoreCase("JA"))
					binary = "1010";
				else if (opCode.equalsIgnoreCase("JB"))
					binary = "1011";
				else if (opCode.equalsIgnoreCase("JBE"))
					binary = "1100";
				else if (opCode.equalsIgnoreCase("JAE"))
					binary = "1101";

				int value = Integer.parseInt(input[i][1]);
				String address = signExtend(value, 12);
				binary += address;

			}

			// System.out.println(input[i][0] + " binary: " + binary);
			hex = binaryToHex(binary);

			System.out.println("hex: " + hex);
			out.write(hex + "\n");
		}
	}

	// binary number to hexadecimal
	public static String binaryToHex(String binary) {
		String hex = "";

		hex += Integer.toHexString(Integer.parseInt(binary.substring(0, 4), 2));
		hex += Integer.toHexString(Integer.parseInt(binary.substring(4, 8), 2));
		hex += Integer.toHexString(Integer.parseInt(binary.substring(8, 12), 2));
		hex += Integer.toHexString(Integer.parseInt(binary.substring(12, 16), 2));
		return hex;
	}

	public static String signExtend(Integer number, int bitLength) {

		String result = "";
		String binary = Integer.toBinaryString(number);
		// System.out.println("binary: " + binary);
		if (number < 0)
			result = extendOne(binary, bitLength);
		else
			result = extendZero(binary, bitLength);

		// System.out.println("result: " + result);
		return result;
	}

	// extend positive number with 0's
	public static String extendZero(String binary, int bitLength) {
		String result = "";
		for (int j = 0; j < bitLength - binary.length(); j++) {
			result += "0";
		}
		result += binary;
		return result;
	}

	// extend negative number with 1's
	public static String extendOne(String number, int bitLength) {
		String result = "";
		result = number.substring(32 - bitLength, 32);
		// System.out.println("result = " + result);

		return result;
	}

}