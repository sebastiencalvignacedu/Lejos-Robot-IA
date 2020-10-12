import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.robotics.RegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.utility.Delay;


/**
 * <p>
 * A program that uses a color sensor to follow a line on the ground.
 * </p>
 *
 * <p>
 * This is a skeleton class that includes a basic outline of a strategy
 * for line finding.  You are welcome to adapt it however you wish (you
 * don't need to follow the template exactly).  However, if you are at
 * all confused, I would recommend using my template until you have a
 * good understanding of what's going on before you make major changes.
 * </p>
 *
 * @author Your Name
 *
 */
public class LineFollower {

        /** The EV3 brick we're controlling */
        private EV3 brick;

        /** The motor on the left side of the robot */
        private RegulatedMotor lMotor;

        /** The motor on the right side of the robot */
        private RegulatedMotor rMotor;

        /** The raw EV3 Color Sensor object */
        private EV3ColorSensor colorSensor;

        /** The calibrated output from the color sensor */
        private SampleProvider calibrated; // use this to fetch samples


        /**
         * <p>
         * Main entry method.  Initializes program and starts the robot running.
         * </p>
         *
         * @param args Command-line arguments (not used)
         */
        public static void main(String[] args) {
                LineFollower lf = new LineFollower(
                                LocalEV3.get(), // brick
                                "A",            // left motor port
                                "D",            // right motor port
                                "S4"            // color sensor port
                                );
                lf.go(); // start it running
        }


        /**
         * <p>
         * Constructs a new LineFollower that's ready to run.
         * </p>
         *
         * @param pBrick The EV3 brick to control
         * @param lPort The name of the port the left motor is connected to
         * @param rPort The name of the port the right motor is connected to
         * @param cPort The name of the port the color sensor is connected to
         */
        public LineFollower(EV3 pBrick, String lPort, String rPort, String cPort) {

                // you do not need to edit this method, but are welcome to
                // if you want to add features

                super();

                // permanently store the brick in our instance variable
                brick = pBrick;

                // establish a fail-safe: pressing Escape quits
                brick.getKey("Escape").addKeyListener(new KeyListener() {
                        @Override
                        public void keyPressed(Key k) {
                        }

                        @Override
                        public void keyReleased(Key k) {
                                System.exit(0);
                        }
                });

                // Now connect the motors
                lMotor = new EV3LargeRegulatedMotor(brick.getPort(lPort));
                rMotor = new EV3LargeRegulatedMotor(brick.getPort(rPort));

                // Connect the color sensor
                colorSensor = new EV3ColorSensor(brick.getPort(cPort));
        }


        /**
         * <p>
         * Main execution method.  Starts the robot running and repeats the
         * forward/search pattern until the robot can no longer find the line
         * (which may be forever).
         * </p>
         *
         * <p>
         * Precondition: None
         * </p>
         *
         * <p>
         * Postcondition: The program has ended
         * </p>
         */
        private void go() {
                // you do not need to edit this method, but you are
                // welcome to make changes if you want to add features

                // only calibrate once, at the start
                calibrate();

                // so long as we can keep finding the line...
                while (findLine()) {
                        // go forward until we lose it
                        followLine();
                }
        }


        /**
         * <p>
         * Prompts the user to place the robot near the line, then
         * calls the findLine() method below.  While samples
         * are being taken, they will automatically calibrate the sensor
         * with minimum and maximum values.
         * </p>
         */
        private void calibrate() {
                // You do not need to edit this method.  However, in order for
                // this method to work, you must complete the findLine()
                // method below.  You may also modify this method if you wish
                // to add features.

                // clear the screen
                brick.getTextLCD().clear();

                brick.getTextLCD().drawString("Calibrate:", 0, 0);
                brick.getTextLCD().drawString("Place the robot", 0, 1);
                brick.getTextLCD().drawString("on or near the", 0, 2);
                brick.getTextLCD().drawString("line start.", 0, 3);
                brick.getTextLCD().drawString("Then press the", 0, 5);
                brick.getTextLCD().drawString("Enter key", 0, 6);

                brick.getKey("Enter").waitForPressAndRelease();

                // put the sensor in "red" mode to shine a light and
                // read back a brightness value
                //
                // The two numbers are the min and max scaled values
                // you want to receive (you can make them whatever you want
                // but the default is to return numbers between 0 and 1).
                //
                // The boolean determines if the filter "clamps" values out
                // of range to the minimum and maximum provided.  If set to
                // true, you will always get values in the range you specify.
                // If false, a poor calibration may result in readings coming
                // back outside of range.
                NormalizationFilter nf = new NormalizationFilter
                                (colorSensor.getRedMode(), 0, 1, true);

                calibrated = nf;

                // signal the filter to start the calibration process
                nf.startCalibration();

                findInitialLine();

                // end the collection of calibration data
                nf.stopCalibration();

                brick.getTextLCD().clear();
        }


        /**
         * <p>
         * Turns the robot (not the motors) by the specified number
         * of degrees, changing the relative heading of the robot.
         * A degrees value of 360 would cause the robot to turn in a
         * complete circle.
         * </p>
         *
         * @param degrees The change in heading for the robot.  Positive values
         *                turn the robot left; negative values turn it right.
         * @param immediateReturn If true, the method returns as soon as the
         *                        motors have started.  If false, the method
         *                        waits until the rotation of the robot has
         *                        completed and the motors have stopped.
         */
        private void turn(int degrees, boolean immediateReturn) {
                // This method is used by other methods in the program to turn
                // the robot back and forth.  You should complete it early on
                // so you can calibrate and find the line.
        }


        /**
         * <p>
         * Turns the robot around in a 360-degree circle.  As the robot
         * turns, it should take readings from the color sensor and store
         * the value it believes to be the line (brightest/darkest,
         * depending on the color of line being used), as well as the
         * rotation of the motors at that point.
         * </p>
         *
         * <p>
         * When the circle is complete, the robot should turn back to
         * the point where the line was observed, then stop
         * its motors.
         * </p>
         *
         * <p>
         * Precondition: The robot is positioned so it is on the line,
         * or will rotate over the line once when it turns.
         * </p>
         *
         * <p>
         * Postconditions: The robot has rotated so that the color sensor
         * is at the location where it was over the line.
         * </p>
         */
        private void findInitialLine() {
                // You will need to complete this method in order to
                // calibrate the sensor and get your robot in the correct
                // starting position

                // Use the "calibrated" variable as your sample source
                // to call fetchSample() on

                // Turn using turn(), and take samples while its turning

                // When you've finished turning, turn back to the location
                // where you saw the best reading.
                //
                // Hint: see the following methods that your motors support:
                // http://web.suffieldacademy.org/cs/lejos_ev3_api/lejos/robotics/Encoder.html#getTachoCount--
                // http://web.suffieldacademy.org/cs/lejos_ev3_api/lejos/robotics/Encoder.html#resetTachoCount--
        }


        /**
         * <p>
         * Moves the robot forward, constantly taking readings from
         * the color sensor.  When the color sensor shows that the
         * robot is no longer on the line, stop the motors.
         * </p>
         *
         * <p>
         * Precondition: The robot is on the line.
         * </p>
         *
         * <p>
         * Postcondition: The robot has moved forward until it no
         * longer detected the line.  The motors are stopped.
         * </p>
         */
        private void followLine() {
                // You will need to complete this method to have the robot
                // move forward and attempt to follow the line

                // turn the motors on

                // loop and take samples until they show you're not on the line

                // stop the motors
        }


        /**
         * <p>
         * Searches for the line by turning the robot back in forth in
         * progressively larger sweeps until it finds the line or gives
         * up (can't find).  You may want to give up after turning in
         * a complete circle and still not being able to find the line.
         * </p>
         *
         * <p>
         * Precondition: The motors are stopped.
         * </p>
         *
         * <p>
         * Postcondition: The robot is once again on the line (returns true)
         *                or has given up looking (returns false).
         * </p>
         *
         * @return boolean True, if the robot found the line again.
         *                 False if the robot could not find the line.
         */
        private boolean findLine() {

                // This is sample code that will cause the robot to wait
                // until it "sees" the line.  It does NOT move the motors,
                // so you'll have to pick your robot up and put it on the
                // line for it to continue.
                //
                // You should replace this code with your own code that
                // moves the motors and causes the robot to search for the
                // line by itself.
                float[] sample = new float[calibrated.sampleSize()];
                sample[0] = -1;
                while (sample[0] > 0.5) {
                    calibrated.fetchSample(sample, 0);
                }
                return true;

                // You will need to complete this method in order to have the
                // robot search for the line once it loses it

                // use the turn() method for your turns

                // keep turning until you've either found the line again,
                // or turned so much that you give up (returning false)

        }


}