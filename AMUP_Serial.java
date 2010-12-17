import processing.core.PApplet;
import processing.serial.*;


public class AMUP_Serial {

	// Serial Message Component IDs
	final static int master_channel =	0;
	final static int volume_channel_1 =	1;                	
	final static int volume_channel_2 = 	2;                 	
	final static int volume_channel_3 = 	3;           	
	final static int volume_channel_4 = 	4;             	
	final static int filter_channel_1 =	5;                  	
	final static int filter_channel_2 =	6;                  	

	// Serial Message Sensor IDs for master channel
        final static int master_volume =        0;
	final static int killer_switch =	1;                	

	// Serial Message Sensor IDs for volume channels
	final static int loop_begin = 		0;                 	
	final static int loop_end = 		1;                 	
	final static int loop_on_off = 		2;           	
	final static int monitor = 		3;             	
	final static int cross_a =		4;                  	
	final static int cross_b = 		5;
	final static int vol_lock =		6;                 	
	final static int button_select	=	7;           	
	final static int eq_high =		8;                   	
	final static int eq_mid = 		9;                    	
	final static int eq_low =		10;                   	
	final static int rotary_select = 	11;  
	final static int proximity = 	        12;
	
	final static int digital_LOW =		0;
	final static int digital_HIGH = 	1;
	final static int analog_LOW = 		0;
	final static int analog_HIGH = 		1024;

        static boolean amup_connection_established;
        static Serial port;
  
        public static void setup_Serial(PApplet processing_app, int speed) {
            for (int i = 0; i < Serial.list().length; i++) { processing_app.println(Serial.list()[i]); }
            port = new Serial(processing_app, Serial.list()[0], speed);
            port.bufferUntil(10); 
            amup_connection_established = false;
        }

}


