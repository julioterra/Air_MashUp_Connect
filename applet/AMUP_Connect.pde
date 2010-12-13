import processing.serial.*;
import processing.core.PApplet;
import rwmidi.MidiInput;
import rwmidi.MidiOutput;
import rwmidi.RWMidi;
import promidi.*;

public class AMUP_Connect  extends PApplet {

        int MIDI_channels = 5;

        int master_channel =   0;
        int volume_channel_1 = 1;  
        int volume_channel_2 = 2;  
        int volume_channel_3 = 3;  
        int volume_channel_4 = 4;  
        
        boolean setup_done = false;
        boolean new_midi = false;
  
	Serial serial_connection;
	MidiInput input;
	MidiOutput output;
	AMUP_Component[] amup_components = new AMUP_Component[5];
        ArrayList<String> input_stream;

	public void setup() {

            AMUP_MIDI.setup_MIDI(this, MIDI_channels);
            input_stream = new ArrayList<String>();  

            for (int i = 0; i < Serial.list().length; i++) { println(Serial.list()[i]); }
            serial_connection = new Serial(this, Serial.list()[0], 115200);
  	    serial_connection.bufferUntil(10); 

            input = RWMidi.getInputDevices()[0].createInput(this);
  	    output = RWMidi.getOutputDevices()[0].createOutput();
  
//            volume_channels[0] = (AMUP_Component)(new AMUP_Volume_Master(0));
            amup_components[0] = new AMUP_Volume_Master(0);
            amup_components[1] = new AMUP_Volume_Channel(1, 1);
            amup_components[2] = new AMUP_Volume_Channel(2, 2);
            amup_components[3] = new AMUP_Volume_Channel(3, 3);
            amup_components[4] = new AMUP_Volume_Channel(4, 4);
            
            delay(500);
            
            setup_done = true;
	}

	public void draw() {
            if (input_stream.size() > 0) {
                String current_string = input_stream.get(0);
                if (AMUP_MIDI.debug_code) print("Serial Reading Unparsed - full message " + current_string);
    
                String [] msg_components = trim(current_string.split(" "));
                if (msg_components.length == 3) {
                    if (AMUP_MIDI.debug_code) println("    : parsed reading - component: " + msg_components[0] + " message ID: " + msg_components[1] + " value : " + msg_components[2]);
                    serialMsgRouter(new Serial_Msg(int(msg_components[0]), int(msg_components[1]), int(msg_components[2])));
                }
                input_stream.remove(0);      
            }
  	
	}
	
        public void serialMsgRouter(Serial_Msg new_msg) {
            if (new_msg.component < amup_components.length) {
                amup_components[new_msg.component].convertSerial(new_msg);
            }          
        }

	public void serialEvent(Serial serial_port) {
              input_stream.add(serial_connection.readString());   
	}


      public void keyPressed() {
              serial_connection.write('S');
      }

}

