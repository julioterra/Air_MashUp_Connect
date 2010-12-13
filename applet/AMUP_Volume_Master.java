import processing.core.PApplet;

public class AMUP_Volume_Master extends AMUP_Component{
	public int volume;
	
	public AMUP_Volume_Master(int component) {
		super(1, component);
		this.volume = 0;
	}

	public void convertSerial(int sensor, int value) {
		this.volume = (int)(PApplet.map(value, 0, 1024, 0, 127));
		MIDI_Msg[] midi_msg = new MIDI_Msg[1]; 
		midi_msg[0] = new MIDI_Msg(AMUP_MIDI.master_volume[0], AMUP_MIDI.master_volume[1], this.volume); 
	}	

	public MIDI_Msg[] convertSerial(int value) {
		this.volume = (int)(PApplet.map(value, 0, 1024, 0, 127));
		MIDI_Msg[] midi_msg = new MIDI_Msg[1]; 
		midi_msg[0] = new MIDI_Msg(AMUP_MIDI.master_volume[0], AMUP_MIDI.master_volume[1], this.volume); 
		return midi_msg;
	}	
}

