import processing.core.PApplet;

public class AMUP_Component {
	public int midi_channel;
	public int component;

	public AMUP_Component(int midi_channel, int component) {
		super();
		this.midi_channel = midi_channel;
		this.component = component;
	}

	public AMUP_Component() {
		super();
		this.midi_channel = -1;
		this.component = -1;
	}
	
	public void convertSerial(Serial_Msg serial_msg) {
		MIDI_Msg[] midi_msg = {new MIDI_Msg(0, 0, 0)};
		if (serial_msg.component == this.component) {
		    convertSerial(serial_msg.sensor, serial_msg.value);
		}
	}
	
	public void convertSerial(int sensor, int value) {
		MIDI_Msg[] midi_msg = {new MIDI_Msg(0, 0, 0)};
	}

}

