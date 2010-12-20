import processing.core.PApplet;
import processing.serial.*;


public class AMUP_Serial {

	// Serial Message Component Numbers
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
	final static int joint_A_volume =	2;                	
	final static int joint_B_volume =	3;                	

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

        final static int connection_interval =  4000; 
        final static int amup_connect_delay =   1000;
        final static String init_command =      "amup_connected";    
        
        static Serial port;

        static boolean serial_connection_established =   false;
        static long last_serial_connection;

        static int amup_connection_time;
        static boolean amup_auto_connection =            false;
        static boolean amup_connection_requested =       false;
        static boolean amup_connection_established =     false;

        static int status_color =       0xffcccc00;
        static String status_string =  "SERIAL MESSAGES\nwaiting to start connection \n";
        static String port_string =  "SERIAL PORTS\n";

        public static void setupSerial(PApplet processing_app) {
            PApplet.println("starting serial connection");
            for (int i = 0; i < Serial.list().length; i++) { 
//                updateMessagePorts(Serial.list()[i]); 
                processing_app.println(Serial.list()[i]);
            }
            port = new Serial(processing_app, Serial.list()[0], 57600);
            port.bufferUntil(10); 
            amup_auto_connection = true;
            amup_connection_established = false;
            serial_connection_established = true;
            amup_connection_requested = false;
            amup_connection_time = processing_app.millis();
            last_serial_connection = processing_app.millis();
            updateMessageStatus("starting serial connection\n");
            setStatusColor();
        }

        
        public static void connectAMUP() {
          if (serial_connection_established && !amup_connection_requested) {
            port.write('S');
            updateMessageStatus("starting amup connection\n");
            amup_connection_requested = true;

            if (AMUP_MIDI.debug_code) PApplet.println("starting amup connection");
          }
        }

        public static String readSerial(PApplet processing_app) {
            String input = port.readString();  
            String trimmed_input = PApplet.trim(input);

            if (trimmed_input.equals(init_command)) {              // check if input equals the initialize command that confirms the connection was established
                amup_connection_time = processing_app.millis();
                amup_connection_established = true;
                updateMessageStatus("amup connection confirmed\n");
                input = "";
            } else if (amup_connection_established) {
                if (!trimmed_input.equals("confirm")) updateMessageStatus(input);
            }
            return input;
        }

        public static void updateMessageStatus(String input) {
            if(status_string.length() > 17) status_string = status_string.substring(16);
            else status_string = "";
            status_string = "SERIAL MESSAGES\n" + input + status_string;
            if (status_string.length() > 500) status_string = status_string.substring(0, 499);
        }

        public static void updateMessagePorts(String input) {
            if(status_string.length() > 14) port_string = port_string.substring(13);
            else port_string = "";
            port_string = "SERIAL PORTS\n" + input + port_string;
            if (port_string.length() > 500) port_string = port_string.substring(0, 499);
        }

        public static boolean serialActive(PApplet processing_app) {
          if (processing_app.millis() - last_serial_connection > connection_interval) {
                amup_connection_established = false;
                serial_connection_established = false;
                setStatusColor();
                port.stop();
                return false; 
            }
            setStatusColor();
            return true;
        }

        public static void setStatusColor() {
            if (amup_connection_established) status_color = 0xff00cc00;  
            else status_color = 0xffcccc00;  
        }

        public static void closeSerial() {
            port_string =  "";
            port.stop();
            amup_auto_connection = false;
            amup_connection_established = false;
            serial_connection_established = false;
            amup_connection_requested = false;
            setStatusColor();
        }

}


