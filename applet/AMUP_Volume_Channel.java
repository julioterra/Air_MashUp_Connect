import processing.core.PApplet;

public class AMUP_Volume_Channel extends AMUP_Component {
	// Channel Specific Midi State Variables 
	public int loop_status;  // 0 = no loop; 1 = begin loop set; 2 = looping
	public int eq_high;
	public int eq_mid;
	public int eq_low;
	public int filterA;
	public boolean filterA_on;
	public int filterB;
	public boolean filterB_on;
	public int volume;
	public boolean vol_lock;
	public boolean monitor;
	public int select_clip;
	MIDI_Msg focus_msg;

	public AMUP_Volume_Channel(int midi_channel, int component) {
		super(midi_channel, component);
		loop_status = 0;
		eq_high = 0;
		eq_mid = 0;
		eq_low = 0;
		filterA = 0;
		filterA_on = false;
		filterB = 0;
		filterB_on = false;
		volume = 0;
		vol_lock = false;
		monitor = false;
		select_clip = AMUP_MIDI.HIGH;
//		MIDI_Msg focus_msg = new MIDI_Msg(this.midi_channel, AMUP_MIDI.channel_select, AMUP_MIDI.HIGH);
	}
	
	public void convertSerial(int sensor, int value) {

            switch(sensor) {
                case AMUP_Serial.loop_begin:
                    if (value == AMUP_MIDI.HIGH) {
                        AMUP_MIDI.send_MIDI(loop_begin());
                    }
                    break;
 
                case AMUP_Serial.loop_end:
                    if (value == AMUP_MIDI.HIGH) {
                        AMUP_MIDI.send_MIDI(loop_end());
                    }
                    break;

                case AMUP_Serial.loop_on_off:
                    if (value == AMUP_MIDI.HIGH) {
                        AMUP_MIDI.send_MIDI(loop_on_off());
                    }
                    break;
 
                case AMUP_Serial.monitor:
                    if (value == AMUP_MIDI.HIGH) {
                        AMUP_MIDI.send_MIDI(monitor());
                    }
                    break;
 
                case AMUP_Serial.cross_a:
                    AMUP_MIDI.send_MIDI(crossfade(AMUP_Serial.cross_a, value));
                    break;
 
                case AMUP_Serial.cross_b:
                    AMUP_MIDI.send_MIDI(crossfade(AMUP_Serial.cross_b, value));
                    break;

                case AMUP_Serial.vol_lock:
                    vol_lock(value);
                    break;

                case AMUP_Serial.button_select:
                    if (value == AMUP_MIDI.HIGH) {
                        AMUP_MIDI.send_MIDI(button_select());
                    }
                    break;

                case AMUP_Serial.eq_high:
                    AMUP_MIDI.send_MIDI(eq_high(value));
                    break;

                case AMUP_Serial.eq_mid:
                    AMUP_MIDI.send_MIDI(eq_mid(value));
                    break;

                case AMUP_Serial.eq_low:
                    AMUP_MIDI.send_MIDI(eq_low(value));
                    break;
                    
                case AMUP_Serial.rotary_select:
                    AMUP_MIDI.send_MIDI(rotary_select(value));
                    break;

                case AMUP_Serial.proximity:
                    AMUP_MIDI.send_MIDI(proximity(value));
                    break;
                                      
 
            }
            
//		if (sensor == AMUP_Serial.loop_begin) { return loop_begin(); }
//		else 
//                if (sensor == AMUP_Serial.loop_end) { return loop_end(); }
//		else if (sensor == AMUP_Serial.loop_on_off) { return loop_on_off(); }
//		else if (sensor == AMUP_Serial.monitor) { return monitor(); }
//		else if (sensor == AMUP_Serial.cross_a) { return crossfade(0, value); }
//		else if (sensor == AMUP_Serial.cross_b) { return crossfade(1, value); }
//		else if (sensor == AMUP_Serial.vol_lock) { return vol_lock(value); }
//		else if (sensor == AMUP_Serial.button_select) { return button_select(); }
//		else if (sensor == AMUP_Serial.proximity_sensor) { return proximity_sensor(value); }
//		else if (sensor == AMUP_Serial.eq_high) { return eq_high(value); }
//		else if (sensor == AMUP_Serial.eq_mid) { return eq_mid(value); }
//		else if (sensor == AMUP_Serial.eq_low) { return eq_low(value); }
//		else if (sensor == AMUP_Serial.rotarySelect) { return rotary_select(value); }  
		
}
      

	protected MIDI_Msg[] loop_begin() {
		MIDI_Msg[] midi_msg = new MIDI_Msg[2];
                  midi_msg[0] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.channel_select, AMUP_MIDI.HIGH); 
                  midi_msg[1] = new MIDI_Msg(AMUP_MIDI.loop_begin[0], AMUP_MIDI.loop_begin[1], AMUP_MIDI.loop_begin[2]);
      		  this.loop_status = 1;
		return midi_msg;
	}
	
	protected MIDI_Msg[] loop_end() {
		MIDI_Msg[] midi_msg = new MIDI_Msg[2];
                      midi_msg[0] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.channel_select, AMUP_MIDI.HIGH); 
                      midi_msg[1] = new MIDI_Msg(AMUP_MIDI.loop_end[0], AMUP_MIDI.loop_end[1],AMUP_MIDI.loop_end[2]);
                  if (this.loop_status == 1 || this.loop_status == 2) {
                      this.loop_status = 2;
                  } else { this.loop_status = 0; }
                  return midi_msg;
	}

	protected MIDI_Msg[] loop_on_off() {
		MIDI_Msg[] midi_msg = new MIDI_Msg[2];
                midi_msg[0] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.channel_select, AMUP_MIDI.HIGH); 
                midi_msg[1] = new MIDI_Msg(AMUP_MIDI.loop_on_off[0], AMUP_MIDI.loop_on_off[1], AMUP_MIDI.HIGH);
    		if (this.loop_status == 2 || this.loop_status == 1) {
      	            loop_status = 0;
                } else {
		    loop_status = 2;
                }
		return midi_msg;
	}

	protected MIDI_Msg[] monitor() {
		MIDI_Msg[] midi_msg = new MIDI_Msg[1];
		if (monitor) {
			midi_msg[0] = new MIDI_Msg(this.component, AMUP_MIDI.monitor, AMUP_MIDI.LOW);
			this.monitor = false;
		} else {
          		midi_msg[0] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.monitor, AMUP_MIDI.HIGH);
  			this.monitor = true;
                }
		return midi_msg;
	}

	protected MIDI_Msg[] crossfade(int channel, int value) {
                channel = channel % 2;
  		MIDI_Msg[] midi_msg = new MIDI_Msg[3];
  		midi_msg[0] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.channel_volume, volume);
		midi_msg[1] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.filter_A, AMUP_MIDI.LOW);
		midi_msg[2] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.filter_B, AMUP_MIDI.LOW);				
		this.filterA_on = false;
		this.filterB_on = false;
		if (value == AMUP_MIDI.HIGH) {
			if (channel == 0) {
				midi_msg[0] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.filter_A, volume);
				midi_msg[1] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.channel_volume, AMUP_MIDI.LOW);
				midi_msg[2] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.filter_B, AMUP_MIDI.LOW);				
				this.filterA_on = true;
				this.filterB_on = false;
			} else if (channel == 1) {
				midi_msg[0] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.filter_B, volume);				
				midi_msg[1] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.channel_volume, AMUP_MIDI.LOW);
				midi_msg[2] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.filter_A, AMUP_MIDI.LOW);
				this.filterA_on = false;
				this.filterB_on = true;
			}			
		}
		return midi_msg;
	}

	protected void vol_lock(int value) {
		MIDI_Msg[] midi_msg = new MIDI_Msg[0];
		if (value == AMUP_Serial.digital_HIGH) { vol_lock = true; } 
                else { vol_lock = false; }
//		return midi_msg;
	}

	protected MIDI_Msg[] button_select() {
		MIDI_Msg [] midi_msg = { new MIDI_Msg(this.midi_channel, AMUP_MIDI.select_clip, AMUP_MIDI.HIGH) };
		return midi_msg;
	}
	
	protected MIDI_Msg[] proximity(int value) {
		this.volume = value;

//		MIDI_Msg[] midi_msg = new MIDI_Msg[3];
//		midi_msg[0] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.channel_volume, volume);
//		midi_msg[1] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.filter_A, AMUP_MIDI.LOW);
//		midi_msg[2] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.filter_B, AMUP_MIDI.LOW);			
//		if (this.filterA_on) {
//			midi_msg[1] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.filter_A, volume);
//			midi_msg[0] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.channel_volume, AMUP_MIDI.LOW);
//			midi_msg[2] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.filter_B, AMUP_MIDI.LOW);			
//		}
//		if (this.filterB_on) {
//			midi_msg[2] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.filter_B, volume);			
//			midi_msg[0] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.channel_volume, AMUP_MIDI.LOW);
//			midi_msg[1] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.filter_A, AMUP_MIDI.LOW);
//		}

		MIDI_Msg[] midi_msg = new MIDI_Msg[1];
		midi_msg[0] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.channel_volume, volume);
		if (this.filterA_on) {
			midi_msg[0] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.filter_A, volume);
		}
		if (this.filterB_on) {
			midi_msg[0] = new MIDI_Msg(this.midi_channel, AMUP_MIDI.filter_B, volume);			
		}

		return midi_msg;
	}

	protected MIDI_Msg[] eq_high(int value) {
		this.eq_high = value;
		MIDI_Msg[] midi_msg = { new MIDI_Msg(this.midi_channel, AMUP_MIDI.eq_high, this.eq_high) };		
		return midi_msg;
	}

	protected MIDI_Msg[] eq_mid(int value) {
		this.eq_mid = value;
		MIDI_Msg[] midi_msg = { new MIDI_Msg(this.midi_channel, AMUP_MIDI.eq_mid, this.eq_mid) };		
		return midi_msg;
	}

	protected MIDI_Msg[] eq_low(int value) {
		this.eq_low = value;
		MIDI_Msg[] midi_msg = { new MIDI_Msg(this.midi_channel, AMUP_MIDI.eq_low, this.eq_low) };		
		return midi_msg;
	}
	
	protected MIDI_Msg[] rotary_select(int value) {
		AMUP_MIDI.current_scene += value;
                if (AMUP_MIDI.current_scene < 0) AMUP_MIDI.current_scene = 0;
                else if (AMUP_MIDI.current_scene > 127) AMUP_MIDI.current_scene = 127;
		MIDI_Msg[] midi_msg = { 
				new MIDI_Msg(AMUP_MIDI.scene_scroll[0], AMUP_MIDI.scene_scroll[1], AMUP_MIDI.current_scene)
		};		
		return midi_msg;
	}
	
}
