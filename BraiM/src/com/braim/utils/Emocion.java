package com.braim.utils;

import com.braim.deezer.data.Track;

public class Emocion {
	
	private int arousal;
	private int valence;
	public int getArousal() {
		return arousal;
	}
	public void setArousal(int arousal) {
		this.arousal = arousal;
	}
	public int getValence() {
		return valence;
	}
	public void setValence(int valence) {
		this.valence = valence;
	}
	
	
	public Emocion(int arousal, int valence, Track track) {
		super();
		this.arousal = arousal;
		this.valence = valence;
	}
	
	

}
