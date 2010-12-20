import processing.serial.*;
import processing.core.PApplet;
import promidi.*;
import controlP5.*;

public class AMUP_Connect  extends PApplet {

    static ControlP5 controlP5;
    controlP5.Textarea current_serial_messages;
    controlP5.Textarea current_midi_messages;
    controlP5.Textarea current_serial_list;    
    controlP5.Toggle serial_connection_toggle;
    final int left_margin = 20;
    final int top_margin = 20;

    int MIDI_channels = 5;
    boolean new_midi;
    boolean new_serial;

    int master_channel =   0;
    int volume_channel_1 = 1;  
    int volume_channel_2 = 2;  
    int volume_channel_3 = 3;  
    int volume_channel_4 = 4;  
    
    AMUP_Component[] amup_components = new AMUP_Component[5];
    ArrayList<String> input_stream;

    public void setup() {
        size(200,  600);
        background(0x000000);
        setupMidi();
        setupVolumeChannels();
        setupInterface();
    }

    public void draw() {
      background(0x000000);
      serial_connection_toggle.setColorActive(AMUP_Serial.status_color);

      if (AMUP_Serial.amup_connection_requested) {
           convert_serial2midi();
           serial_checkStatus();  
       } else if (millis() - AMUP_Serial.amup_connection_time > AMUP_Serial.amup_connect_delay) {
           AMUP_Serial.connectAMUP();  
       }

      current_serial_list.setText(AMUP_Serial.port_string);
      current_serial_messages.setText(AMUP_Serial.status_string);
      current_midi_messages.setText(AMUP_MIDI.status_string);
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
    
    void setupMidi() {
        println("set up MIDI");
        AMUP_MIDI.setup_MIDI(this, MIDI_channels);
        input_stream = new ArrayList<String>();  
        new_midi = false;
    }

   void setupSerial() {
        println("set up serial");
        AMUP_Serial.setupSerial(this);     
   }
  
    void setupInterface() {
        frameRate(25);
        controlP5 = new ControlP5(this);
        serial_connection_toggle = controlP5.addToggle("serial_connection_toggle", left_margin, top_margin , 20, 20);
        current_serial_messages = controlP5.addTextarea("current_serial_messages", "waiting to start connection", left_margin, top_margin + 50, 150, 120);
        current_serial_list = controlP5.addTextarea("current_serial_list", "waiting to start connection", left_margin, top_margin + 330, 150, 120);
        current_midi_messages = controlP5.addTextarea("current_midi_messages", "waiting to start connection", left_margin, top_margin + 190, 150, 120);

        current_serial_messages.setColorBackground(0xff555555);
        current_serial_list.setColorBackground(0xff555555);
        current_midi_messages.setColorBackground(0xff555555);

        controlP5.controller("serial_connection_toggle").setColorBackground(0xffcc0000);
        controlP5.controller("serial_connection_toggle").setColorActive(0xffcccc00);
        controlP5.controller("serial_connection_toggle").setCaptionLabel("Connect to AMUP Box");

    }
    

   /************************
    ** RUNTIME FUNCTIONS
    ************************/

    void convert_serial2midi() {
        if (input_stream.size() > 0) {
            for (int i = input_stream.size() - 1; i >= 0; i--) { 
                String current_string = input_stream.get(0);
                String [] msg_components = trim(current_string.split(" "));
                if (msg_components.length == 3) {

                    if (AMUP_MIDI.debug_code) print("convert_serial2midi number " + i + " [serial message unparsed] " + current_string); 
                    if (AMUP_MIDI.debug_code) println("    convert_serial2midi [parsed reading] - component: " + msg_components[0] + " message ID: " + msg_components[1] + " value : " + msg_components[2]);

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

    public void serial_checkStatus() {
        if (!AMUP_Serial.serialActive(this) && AMUP_Serial.amup_auto_connection) {
            setupSerial();
        }
    }

   /************************
    ** DISPLAY FUNCTIONS
    ************************/

    public void serial_connection_toggle(int theValue) {
       if(theValue == 0) {
           AMUP_Serial.closeSerial();
           println("************ connection closed ************");
       } else {
           setupSerial();
           println("************ connection openned ************");
       }
    }

    
   /************************
    ** CALLBACK FUNCTIONS
    ************************/

      public void serialEvent(Serial serial_port) {
//          String serial_input = serial_connection.readString();
          String serial_input = AMUP_Serial.readSerial(this);
          input_stream.add(serial_input);
          AMUP_Serial.last_serial_connection = millis();
      }

      public void keyPressed() {
        if (key == 'S' || key == 's'){
            AMUP_Serial.port.write('S');
//            serial_connection.write('S');
            println("starting serial connection");
            if (AMUP_Serial.amup_connection_established == false) AMUP_Serial.amup_connection_established = true;
        }
      }

}

