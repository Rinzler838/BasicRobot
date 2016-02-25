package bot.model;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class EV3Bot
{
	private String botMessage;
	private int xPosition;
	private int yPosition;
	private long waitTime;
	
	private MovePilot botPilot;
	private EV3UltrasonicSensor distanceSensor;
	private EV3TouchSensor backTouch;
	private float [] ultrasonicSamples;
	private float [] touchSamplples;
	
	public EV3Bot()
	{
		this.botMessage = "Bot";
		this.xPosition = 50;
		this.yPosition = 50;
		this.waitTime = 4000;
		
		distanceSensor = new EV3UltrasonicSensor(LocalEV3.get().getPort("S1"));
		backTouch = new EV3TouchSensor(LocalEV3.get().getPort("S2"));
		
		setupPilot();
		displayMessage();
	}
	
	private void setupPilot()
	{
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A,  43.3).offset(-72);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.B, 43.3).offset(-72);
		WheeledChassis chassis = new WheeledChassis(new Wheel []{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
		botPilot = new MovePilot(chassis);
	}
	
	private void driveRoom()
	{
		ultrasonicSamples = new float [distanceSensor.sampleSize()];
		distanceSensor.fetchSample(ultrasonicSamples, 0);
		if (ultrasonicSamples[0] < 18)
		{
			//Short
			botPilot.travel(7.62);
			botPilot.rotate(-90);
			botPilot.travel(30.48);
			botPilot.rotate(90);
			botPilot.travel(58.42);
			botPilot.rotate(-90);
			botPilot.travel(43.18);
			botPilot.stop();
		}
		else
		{
			//Long
			botPilot.travel(43.18);
			botPilot.rotate(90);
			botPilot.travel(58.42);
			botPilot.rotate(-90);
			botPilot.travel(30.48);
			botPilot.rotate(90);
			botPilot.travel(7.62);
			botPilot.rotate(360);
			botPilot.rotate(-360);
			botPilot.stop();
		}
		
		displayMessage("driveRoom");
	}
	
	private void displayMessage()
	{
		LCD.drawString(botMessage, xPosition, yPosition);
		Delay.msDelay(waitTime);
	}
	
	private void displayMessage(String message)
	{
		LCD.drawString(message, xPosition, yPosition);
		Delay.msDelay(waitTime);
	}
}