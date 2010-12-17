import processing.core.PApplet;

public class AMUP_Volume_Master extends AMUP_Component{
	public int volume;
        public int killer_switch;
	
	public AMUP_Volume_Master(int component) {
		super(1, component);
		this.volume = 0;
                this.killer_switch = AMUP_MIDI.LOW;
	}

	public void convertSerial(int sensor, int value) {
            switch(sensor) {
                    case AMUP_Serial.master_volume:
                            AMUP_MIDI.send_MIDI(master_volume(value));
                        break;
                        
                    case AMUP_Serial.killer_switch:
                            AMUP_MIDI.send_MIDI(killer_switch(value));
                        break;
            }
	}	

	protected MIDI_Msg[] master_volume(int value) {
		this.volume = value;
		MIDI_Msg[] midi_msg = new MIDI_Msg[1]; 
		midi_msg[0] = new MIDI_Msg(AMUP_MIDI.master_volume[0], AMUP_MIDI.master_volume[1], this.volume); 
		return midi_msg;
	}

	protected MIDI_Msg[] killer_switch(int value) {
		this.killer_switch = value;
		MIDI_Msg[] midi_msg = new MIDI_Msg[1]; 
		midi_msg[0] = new MIDI_Msg(AMUP_MIDI.killer_switch[0], AMUP_MIDI.killer_switch[1], this.killer_switch); 
		return midi_msg;
	}


}

