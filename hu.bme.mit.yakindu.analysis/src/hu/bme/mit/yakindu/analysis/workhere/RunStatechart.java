package hu.bme.mit.yakindu.analysis.workhere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;



public class RunStatechart {
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();
		String command = reader.readLine();
		while(!command.matches("exit")) {
			switch(command) {
			case "begin":
				s.raiseBegin();
				s.runCycle();
				print(s);
				command = reader.readLine();
				break;
			case "light":
				s.raiseLight();
				s.runCycle();
				print(s);
				command = reader.readLine();
				break;
			case "dark":
				s.raiseDark();
				s.runCycle();
				print(s);
				command = reader.readLine();
				break;
			case "exit":
				break;
			default:
				System.out.println("Unknown command");	
				print(s);
				command = reader.readLine();
				break;
			}
		}
		System.out.println("Final variables:");
		print(s);
			System.exit(0);
	}

	public static void print(IExampleStatemachine s) {

		System.out.println("Y = " + s.getSCInterface().getYellowTime());
		System.out.println("R = " + s.getSCInterface().getRedTime());
	}
}






/*public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();
		String command = reader.readLine();
		if(command.matches("start")) {
			System.out.println("Clock started");
			print(s);
			s.raiseStart();
			s.runCycle();	
			command = reader.readLine();
			while(!command.matches("exit")) {
				
				switch(command) {
				case "black":
					s.raiseBlack();
					s.runCycle();
					print(s);
					command = reader.readLine();
					break;
				case "white":
					s.raiseWhite();
					s.runCycle();
					print(s);
					command = reader.readLine();
					break;
				case "exit":
					break;					
				default:
					System.out.println("Unknown command");	
					print(s);
					command = reader.readLine();
					break;
				}				
			}
			System.out.println("Final time:");
			print(s);
			System.exit(0);
			
		}

	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
*/
