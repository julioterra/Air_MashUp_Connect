import promidi.*;
import processing.core.PApplet;

public class AMUP_MIDI {

                public static boolean debug_code = false;
                public static MidiIO midiIO;
                public static MidiOut[] midiOut;

		// Master Midi State Variables 
		public static int current_volume = 			0;
		public static int current_scene =			1;

		// each component uses a different MIDI channel
	
		// Master Midi Channel Messages
		// message numbers 100 and higher
		public final static int[] master_volume =		{1, 100};
		public final static int[] scene_scroll =		{1, 101};
		public final static int[] loop_begin =  		{1, 102, 127};
		public final static int[] loop_end =  			{1, 103, 127};
		public final static int[] loop_on_off = 		{1, 104, 127};
                public final static int[] killer_switch =               {1, 105};

		// MIDI Message ID: Joint Audio Channel Volume Specific Messages
		public final static int[] joint_A_volume = 		{1, 80};		// value sent must be above 65 to select channel
		public final static int[] joint_B_volume = 		{1, 81};		// value sent must be above 65 to select channel

		// MIDI Message ID: Volume Channel Specific Messages
		// Messages IDs from 0 to 25 are reserved for the volume channel specific messages
		public final static int channel_select = 		20;		// value sent must be above 65 to select channel
		public final static int channel_volume = 		0;		// value sent ranges between 0 and 127
		public final static int filter_A = 			1;		// value sent ranges between 0 and 127
		public final static int filter_B = 			2;		// value sent ranges between 0 and 127
		public final static int monitor = 			3;		// value sent must be above 65 to select monitor
		public final static int select_clip = 			7;		// value sent must be above 65 to select clip
		public final static int eq_high =			9;		// value sent ranges between 0 and 127
		public final static int eq_mid	=			10;		// value sent ranges between 0 and 127	
		public final static int eq_low	=			11;  	// value sent ranges between 0 and 127

		// MIDI Message ID: Filter Control Specific Messages
		// Messages IDs from 0 to 25 are reserved for the volume channel specific messages
		final static int filter_select_1 =	 		1;		// value sent must be above 65 to select channel
		final static int filter_select_2 =	 		2;		// value sent must be above 65 to select channel
		final static int filter_select_3 =	 		3;		// value sent must be above 65 to select channel
		final static int filter_select_4 =	 		4;		// value sent must be above 65 to select channel
		final static int filter_select_1_param_1 =		34;		// value sent ranges between 0 and 127
		final static int filter_select_1_param_2 =		35;		// value sent ranges between 0 and 127
		final static int filter_select_1_param_3 =		36;		// value sent ranges between 0 and 127
		final static int filter_select_1_param_4 =		37;		// value sent ranges between 0 and 127
		final static int filter_select_2_param_1 =		38;		// value sent ranges between 0 and 127
		final static int filter_select_2_param_2 =		39;		// value sent ranges between 0 and 127
		final static int filter_select_2_param_3 =		40;		// value sent ranges between 0 and 127
		final static int filter_select_2_param_4 =		41;		// value sent ranges between 0 and 127
		final static int filter_select_3_param_1 =		38;		// value sent ranges between 0 and 127
		final static int filter_select_3_param_2 =		39;		// value sent ranges between 0 and 127
		final static int filter_select_3_param_3 =		40;		// value sent ranges between 0 and 127
		final static int filter_select_3_param_4 =		41;		// value sent ranges between 0 and 127
		final static int filter_select_4_param_1 =		38;		// value sent ranges between 0 and 127
		final static int filter_select_4_param_2 =		39;		// value sent ranges between 0 and 127
		final static int filter_select_4_param_3 =		40;		// value sent ranges between 0 and 127
		final static int filter_select_4_param_4 =		41;		// value sent ranges between 0 and 127
        final static int midi_filterA = 			1;		// value sent ranges between 0 and 127
  	final static int midi_filterB = 			2;		// value sent ranges between 0 and 127    
		
	public final static int HIGH =				127;
	public final static int LOW =				0;
	public final static int values_MAX =			127;
	public final static int values_MIN =			0;
	
        static int status_color =         0xffcccc00;
        static String status_string =     "MIDI MESSAGES\nwaiting to start connection \n";

        public static void setup_MIDI(PApplet processing_app, int midi_channels) {
            midiIO = MidiIO.getInstance(processing_app);
            midiIO.printDevices();
            midiOut = new MidiOut [midi_channels];
            for (int i = 0; i < midi_channels; i++) {
                midiOut[i] = midiIO.getMidiOut(0+i, "AMUP_Connect_Port");
            }
        }

        public static void send_MIDI(MIDI_Msg[] midi_msgs) {
           if (midi_msgs.length > 0) {
                if (debug_code) PApplet.println("******* MIDI Messages Sent ********* "); 
                for (int i = 0; i < midi_msgs.length; i ++ ) {   
                    Controller control_msg = new Controller(midi_msgs[i].message, midi_msgs[i].value);
                    midiOut[midi_msgs[i].channel-1].sendController(control_msg);
                    if (debug_code) PApplet.println("msg " + i + " channel: " + midi_msgs[i].channel + " msg: " + midi_msgs[i].message + " value: " + midi_msgs[i].value );                
                    updateMessageStatus(midi_msgs[i].channel + ", " + midi_msgs[i].message + ", " + midi_msgs[i].value + "\n");
                }
            }
        }

        public static void updateMessageStatus(String input) {
            if(status_string.length() > 17) status_string = status_string.substring(14);
            status_string = "MIDI MESSAGES\n" + input + status_string;
            if (status_string.length() > 500) status_string = status_string.substring(0, 499);
        }

}

