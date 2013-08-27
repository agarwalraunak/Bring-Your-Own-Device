package com.device.model;

/**
 * @author raunak
 *
 */
public class KerberosSession extends Session {
	
	private TGT tgt;

	public KerberosSession(String sessionID, TGT tgt) {
		super(sessionID);
		this.tgt = tgt;
	}
	/**
	 * @return <code>TGT</code> the tgt
	 */
	public TGT getTgt() {
		return tgt;
	}

	/**
	 * @param <code>TGT</code> tgt the tgt to set
	 */
	public void setTgt(TGT tgt) {
		this.tgt = tgt;
	}
	

}
