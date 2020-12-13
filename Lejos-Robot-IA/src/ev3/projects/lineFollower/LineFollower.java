package ev3.projects.lineFollower;

import Pellier_lejos_exemple.ColorSensor;
import Pellier_lejos_exemple.TouchSensor;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.robotics.Color;

public class LineFollower 
{ 

    public static void main(String[] args)
    {
        float    colorValue;
        Couleur couleur;
        
        System.out.println("Line Follower\n");
        
        coleur
        color.setFloodLight(Color.RED);
        color.setFloodLight(true);

        Button.LEDPattern(4);    // flash green led and 
        Sound.beepSequenceUp();  // make sound when ready.

        System.out.println("Press any key to start");
        
        Button.waitForAnyPress();
        
        motorA.setPower(40);
        motorB.setPower(40);
       
        // drive waiting for touch sensor or escape key to stop driving.

        while (!touch.isTouched() && Button.ESCAPE.isUp()) 
        {
            colorValue = color.getRed();
            
            Lcd.clear(7);
            Lcd.print(7,  "value=%.3f", colorValue);

            if (colorValue > .100)
            {
                motorA.setPower(40);
                motorB.setPower(20);
            }
            else
            {
                motorA.setPower(20);    
                motorB.setPower(40);
            }
        }
       
        // stop motors with brakes on.
        motorA.stop();
        motorB.stop();

        // free up resources.
        motorA.close();
        motorB.close();
        touch.close();
        color.close();
       
        Sound.beepSequence(); // we are done.
    }
}