public class MIDI_Msg {
	public int channel = 0;
	public int message = 0;
	public int value = 0;

	public MIDI_Msg(int channel, int message, int value) {
		super();
		this.channel = channel;
		this.message = message;
		this.value = value;
	} 
	
}
