public class AMUP_Volume_Filter extends AMUP_Component{
	public int volume;
	public int filter_number;
	
	public AMUP_Volume_Filter(int midi_channel, int component, int filter_number) {
		super(midi_channel, component);
		this.volume = 0;
		this.filter_number = filter_number % 2;
	}

	public void convertSerial(int sensor, int value) {
		this.volume = value;
		MIDI_Msg[] midi_msg = new MIDI_Msg[1]; 
		if (filter_number == 0) { 
			midi_msg[0] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.volume_filter_A, this.volume); 
		}		
		if (filter_number == 1) { 
			midi_msg[0] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.volume_filter_B, this.volume);
		}		
	}
	
}

