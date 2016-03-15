 package bot.model;

import java.io.*;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.Sound;
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
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.B, 43.3).offset(72);
		WheeledChassis chassis = new WheeledChassis(new Wheel []{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
		botPilot = new MovePilot(chassis);
	}
	
	public void driveRoom()
	{
		while (LocalEV3.get().getKeys().waitForAnyPress() != LocalEV3.get().getKeys().ID_ESCAPE)
		{
			shorterBegin();
		}
		
//		ultrasonicSamples = new float [distanceSensor.sampleSize()];
//		distanceSensor.fetchSample(ultrasonicSamples, 0);
//		if (ultrasonicSamples[0] < 3)
//		{
//			displayMessage("Short Begin");
//			shorterBegin();
//		}
//		else
//		{
//			displayMessage("Long Begin");
//			longerBegin();
//		}
//		
//		displayMessage("driveRoom");
	}
	
	private void shorterBegin()
	{
		botPilot.travel(914.4); //From door
		botPilot.rotate(50);
		botPilot.travel(3657.6); //Along front of room
		botPilot.rotate(-60);
		botPilot.travel(6010.4); //Long straight 7010.4?
		botPilot.rotate(60);
		botPilot.travel(5181.6); //To other door
		botPilot.stop();
	}
	
	private void longerBegin()
	{
		botPilot.travel(5181.6); //From door
		botPilot.rotate(-60);
		botPilot.travel(7010.4); //Long straight
		botPilot.rotate(60);
		botPilot.travel(3657.6); //Along front of room
		botPilot.rotate(-50);
		botPilot.travel(914.4); //To other door
		botPilot.stop();
	}
	
	public void avoidanceMethod()
	{
//		while (LocalEV3.get().getKeys().waitForAnyPress() != LocalEV3.get().getKeys().ID_ESCAPE)
//		{
//			double distance = (Math.random() * 100) % 23;
//			double angle = (Math.random() * 360);
//			boolean isPositive = ((int) (Math.random() * 2) % 2 == 0);
//			distanceSensor.fetchSample(ultrasonicSamples, 0);
//			
//			if(ultrasonicSamples[0] < 17)
//			{
//				botPilot.travel(-distance);
//				botPilot.rotate(angle);
//			}
//			else
//			{
//				botPilot.rotate(-angle);
//				botPilot.travel(distance);
//			}
//		}

		//OR
		
		while (LocalEV3.get().getKeys().waitForAnyPress() != LocalEV3.get().getKeys().ID_ESCAPE)
		{
			distanceSensor.fetchSample(ultrasonicSamples, 0);
			while (ultrasonicSamples[0] < 1)
			{
				botPilot.travel(5);
			}
			botPilot.stop();
			botPilot.rotate(90);
			botPilot.travel(5);
		}
		
	}
	
	public void playMusic()
	{
		final File soundFile = new File("imperial.wav");
        Sound.playSample(soundFile, 100);
        Delay.msDelay(4000);
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