package org.usfirst.frc.team3006.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class Robot extends SampleRobot {
	Joystick stick = new Joystick(0);
	Victor motorleft0 = new Victor(2);
	Victor motorleft1 = new Victor(3);
	Victor motorright0 = new Victor(0);
	Victor motorright1 = new Victor(1);
	CANTalon windowMotor = new CANTalon(2);
	Victor valve = new Victor(6);
	Compressor compressor;
	private final float speed = 1F;
	private final float deadzone = 0.1F;
	private int counter = 0;
	private int counter2 = 0;
	private boolean pressed = false;
	private  boolean compressing = false;
	
	public Robot() {
		valve.set(0);
		compressor = new Compressor(1);
	}
	
	@Override
	public void autonomous() {
		Move(-1 * speed, -1 * speed);
		Timer.delay(5.0);
		Move(0, 0);
	}

	@Override
	public void operatorControl() {
		while (isOperatorControl() && isEnabled()) {
			if(compressing)
			{
				counter2++;
				counter2 %= 6000;
			}
			
			if(stick.getRawButton(5) && counter2 == 0 && !compressing)
			{
				compressor.start();
				compressing = true;
			}
			else if(counter2 == 0)
			{
				compressor.stop();
				compressing = false;
			}
			
			
			double motor0value = stick.getRawAxis(1);
			double motor1value = stick.getRawAxis(3);
			
			if(motor0value <= deadzone && motor0value >=  (-1 *deadzone))
        	{
        		motor0value = 0.0;
        	}
        	if(motor1value <= deadzone && motor1value >=  (-1 *deadzone))
        	{
        		motor1value = 0.0;
        	}      	

        	if(valve.get() == 1)
        	{
        		counter ++;
        		counter %= 5;
        	}
        	
        	//FIXME: Change to the actual button you want.
        	if(stick.getRawButton(7) && counter == 0 && !pressed)
        	{
        		valve.set(1);
        		pressed = true;
        	}
        	else if(counter == 0)
        	{
        		valve.set(0);
        	}
        	
        	if(!stick.getRawButton(7) && pressed)
        	{
        		pressed = false;
        	}
        	
        	//FIXME: Add forward and backward along with different button configuration
        	if(stick.getRawButton(8))
        	{
        		windowMotor.set( -1);
        	}
        	else if(stick.getRawButton(6))
        	{
        		windowMotor.set(1);
        	}
        	else
        	{
        		windowMotor.set(0);
        	}
        	
        	Move(motor0value * speed, motor1value * speed);
        	
        	Timer.delay(0.005); // wait for a motor update time
		}
		compressor.stop();
		
	}

	
	private void Move(double motorLeft, double motorRight)
    {
    	motorleft0.set(motorLeft * -1);
    	//motorleft1.set(motorLeft * -1);
    	motorright0.set(motorRight);
    	//motorright1.set(motorRight);
    }
	
}
