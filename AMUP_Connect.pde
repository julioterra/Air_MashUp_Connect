import processing.serial.*;
import processing.core.PApplet;
import promidi.*;
import controlP5.*;

public class AMUP_Connect  extends PApplet {

    ControlP5 controlP5;

    int MIDI_channels = 5;
    boolean amup_connection_established;
    boolean new_serial;
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

            setupSerialMidiConnection();
            setupVolumeChannels();
            setupInterface();
            setup_done = true;
	}

    public void draw() {
       if (AMUP_Serial.amup_connection_established) {
           serial2midi();
       }
    }


   /************************
    ** SETUP FUNCTIONS
    ************************/

    void setupVolumeChannels() {
        for (int i = 0; i < amup_components.length; i++) {
            if (i == 0) amup_components[i] = new AMUP_Volume_Master(i);
            else amup_components[i] = new AMUP_Volume_Channel(i, i);
        }
    }    
    
    void setupSerialMidiConnection() {
        AMUP_MIDI.setup_MIDI(this, MIDI_channels);
        AMUP_Serial.setup_Serial(this, 57600);
        input_stream = new ArrayList<String>();  
    }

    void setupInterface() {
        frameRate(25);
        controlP5 = new ControlP5(this);
        controlP5.addToggle("serial_connect", 100,280,20,20).setId(4);
    }


   /************************
    ** RUNTIME FUNCTIONS
    ************************/

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

    public void serialMsgRouter(Serial_Msg new_msg) {
      if (new_msg.component < amup_components.length) {
            amup_components[new_msg.component].convertSerial(new_msg);
        }        
    }

      public void serialEvent(Serial serial_port) {
          String serial_input = AMUP_Serial.port.readString();
//          String serial_input = serial_connection.readString();
          input_stream.add(serial_input);
          last_serial_connection = millis();
//          if ((millis() - start_time) > interval) input_stream.add(serial_input);  
//          else serial_connection.clear();
      }


      public void keyPressed() {
        if (key == 'S' || key == 's'){
            AMUP_Serial.port.write('S');
            println("starting serial connection");
            if (AMUP_Serial.amup_connection_established == false) AMUP_Serial.amup_connection_established = true;
        }
      }

}

