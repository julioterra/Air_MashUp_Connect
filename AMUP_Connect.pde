import processing.serial.*;
import processing.core.PApplet;
import promidi.*;
import controlP5.*;

public class AMUP_Connect  extends PApplet {

        int MIDI_channels = 5;
        boolean first_contact;
        boolean new_serial;
        int contact;
        long start_time;
        long interval = 5000;
        long last_serial_connection = millis();

        int master_channel =   0;
        int volume_channel_1 = 1;  
        int volume_channel_2 = 2;  
        int volume_channel_3 = 3;  
        int volume_channel_4 = 4;  
        
        boolean setup_done = false;
        boolean new_midi = false;
  
	Serial serial_connection;
	AMUP_Component[] amup_components = new AMUP_Component[5];
        ArrayList<String> input_stream;

	public void setup() {
            size(200, 200);
            first_contact = false;
            contact = 0;
            AMUP_MIDI.setup_MIDI(this, MIDI_channels);
            input_stream = new ArrayList<String>();  

            for (int i = 0; i < Serial.list().length; i++) { println(Serial.list()[i]); }
            serial_connection = new Serial(this, Serial.list()[0], 57600);
  	    serial_connection.bufferUntil(10); 

            amup_components[0] = new AMUP_Volume_Master(0);
            amup_components[1] = new AMUP_Volume_Channel(1, 1);
            amup_components[2] = new AMUP_Volume_Channel(2, 2);
            amup_components[3] = new AMUP_Volume_Channel(3, 3);
            amup_components[4] = new AMUP_Volume_Channel(4, 4);
            
            delay(500);
            
            setup_done = true;
	}

    public void draw() {
       if (first_contact) {
           setStartTime();
           serial2midi();
       }
    }

    void serial2midi() {
        if (input_stream.size() > 0) {
            for (int i = input_stream.size() - 1; i >= 1; i--) { 
                String current_string = input_stream.get(0);
                if (AMUP_MIDI.debug_code) print("serial2midi number " + i + " [serial message unparsed] " + current_string); 
                String [] msg_components = trim(current_string.split(" "));
                if (msg_components.length == 3) {
                    if (AMUP_MIDI.debug_code) println("    serial2midi [parsed reading] - component: " + msg_components[0] + " message ID: " + msg_components[1] + " value : " + msg_components[2]);
                    serialMsgRouter(new Serial_Msg(int(msg_components[0]), int(msg_components[1]), int(msg_components[2])));
                }
                input_stream.remove(0);      
            }
        }
    }  

      void setStartTime() {
          if (contact == 0) { 
             contact = 1;
             start_time = millis();
           }
      }

        public void serialMsgRouter(Serial_Msg new_msg) {
          if (new_msg.component < amup_components.length) {
                amup_components[new_msg.component].convertSerial(new_msg);
            }        
        }

      public void serialEvent(Serial serial_port) {
          String serial_input = serial_connection.readString();
          last_serial_connection = millis();
          if ((millis() - start_time) > interval) input_stream.add(serial_input);  
          else serial_connection.clear();

      }


      public void keyPressed() {
        if (key == 'S' || key == 's'){
            serial_connection.write('S');
            println("starting serial connection");
            if (first_contact == false) first_contact = true;
        }
      }

}

