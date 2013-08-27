/**
 * 
 */
package com.device.util.hashing;

import com.device.util.hashing.HashUtilImpl.HashingTechqniue;

/**
 * @author raunak
 *
 */
public interface IHashUtil {

	
	/**
	 * @param <code>String</code> input
	 * @param <code>HashingTechnique</code> technique
	 * @return <code>byte[]</code>
	 */
	public byte[] getHash(String input, HashingTechqniue technique);
	
	/**
	 * @param <code>String</code> input
	 * @return <code>byte[]</code>
	 */
	public byte[] stringToByte(String input);
	
	/**
	 * @param <code>byte[]</code> input
	 * @return <code>String</code>
	 */
	public String bytetoBase64String(byte[] input);
	
	/**
	 * @return <code>byte[]</code>
	 */
	public byte[] generateSalt();
	
	/**
	 * @return <code>String</code>
	 */
	String getSessionKey();
	
	/**
	 * @param <code>String</code> input
	 * @param <code>HashingTechnique</code> technique
	 * @param <code>byte[]</code> salt
	 * @return  <code>byte[]</code>
	 */
	byte[] getHashWithSalt(String input, HashingTechqniue technique, byte[] salt);
}
