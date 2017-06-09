/*******************************************************************************
 * Copyright (c) 2015, 2016 FRESCO (http://github.com/aicis/fresco).
 *
 * This file is part of the FRESCO project.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * FRESCO uses SCAPI - http://crypto.biu.ac.il/SCAPI, Crypto++, Miracl, NTL,
 * and Bouncy Castle. Please see these projects for any further licensing issues.
 *******************************************************************************/
package dk.alexandra.fresco.suite.spdz.utils;

import dk.alexandra.fresco.framework.ProtocolProducer;
import dk.alexandra.fresco.framework.value.OInt;
import dk.alexandra.fresco.framework.value.SInt;
import dk.alexandra.fresco.framework.value.SIntFactory;
import dk.alexandra.fresco.lib.field.integer.BasicNumericFactory;
import dk.alexandra.fresco.lib.helper.ParallelProtocolProducer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * 
 * @author kasper
 *	Class containing all global variables and helper methods 
 */
public class Util {
	
	private static BigInteger p = null; //Should be set by an initiation call
	private static BigInteger p_half;
	private static int size = 0; //should be set by an initiation call
	public static int EXP_PIPE_SIZE = 200+1; //R^-1, R, R^2, ..., R^200		
	
	public static final String ENCODING = "UTF-8";	
	
	public static BigInteger getModulus() {
		if(p == null) {
			throw new IllegalStateException("You need to set the modulus before you can retrieve it.");
		}
		return p;
	}
	
	public static void setModulus(BigInteger p) {		
		Util.p = p;
		Util.p_half = p.divide(BigInteger.valueOf(2));
		Util.size = p.toByteArray().length;
	}
	
	public static int getModulusSize() {
		return size;
	}

	private MessageDigest H;
	
	public MessageDigest getHashFunction(){
		if(H != null){
			return H;
		}
		try{
			H = MessageDigest.getInstance("SHA-256");
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return H;
	}
	
//	public static ReadableByteChannel getInputStream(String resource) throws IOException{
//		File f = new File(resource);
//		if (!f.exists()) {
//			throw new IllegalStateException("Resource not found :  " + resource);
//		}
//		FileInputStream fileInputStream = new FileInputStream(f);
//		FileChannel is = fileInputStream.getChannel();
//		return is;
//	}
	
	public static BigInteger convertRepresentation(BigInteger b) {
		BigInteger actual = b.mod(p);
		if (actual.compareTo(p_half) > 0) {
			actual = actual.subtract(p);
		}
		return actual;
	}

	public static InputStream getInputStream(String resource) throws IOException{
		File f = new File(resource);
		if (!f.exists()) {
			throw new IllegalStateException("Resource not found :  " + resource);
		}
		FileInputStream fileInputStream = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fileInputStream, 100000*1024);
		return bis;
	}
	
	/**
	 * Returns the numbers: M, M^2, M^3, ..., M^maxBitSize
	 * @param M the number for exponentiation
	 * @param maxBitSize number of exp to get
	 * @return M, M^2, M^3, ..., M^maxBitSize
	 */
	public static BigInteger[] getClearExpPipe(BigInteger M, int maxBitSize){
		BigInteger[] Ms = new BigInteger[maxBitSize];
		Ms[0] = M;
		for(int i = 1; i < Ms.length; i++){
			Ms[i] = Ms[i-1].multiply(M).mod(p);
		}
		return Ms;
	}
	
	public static OInt[][] oIntFill(OInt[][] matrix, BasicNumericFactory factory) {
		for(OInt[] vector: matrix) {
			vector = oIntFill(vector, factory);
		}
		return matrix;
	}
	
	public static OInt[] oIntFill(OInt[] vector, BasicNumericFactory factory) {
		for(int i = 0; i < vector.length; i++) {
			vector[i] = factory.getOInt();
		}
		return vector;
	}
	
	public static SInt[][] sIntFillRemaining(SInt[][] matrix, BasicNumericFactory factory) {
		for(SInt[] vector: matrix) {
			vector = sIntFill(vector, factory);
		}
		return matrix;
	}


  public static SInt[] sIntFill(SInt[] vector, SIntFactory factory) {
		for(int i = 0; i < vector.length; i++) {
			vector[i] = factory.getSInt();
		}
		return vector;
	}
	
	public static BigInteger[][] randomFill(BigInteger[][] matrix, int bitLenght, Random rand) {
		for(BigInteger[] vector: matrix) {
			vector = randomFill(vector, bitLenght, rand);
		}
		return matrix;
	}
	
	public static BigInteger[] randomFill(BigInteger[] vector, int bitLength, Random rand) {
		for(int i = 0; i < vector.length; i++) {
			vector[i] = new BigInteger(bitLength, rand); 
			vector[i] = vector[i].subtract(BigInteger.valueOf(2).pow(bitLength-1)).mod(dk.alexandra.fresco.suite.spdz.utils.Util.p);
		}
		return vector;
	}
	
	public static BigInteger[][] zeroFill(BigInteger[][] matrix) {
		for(BigInteger[] vector: matrix) {
			vector = zeroFill(vector);
		}
		return matrix;
	}
	
	public static BigInteger[] zeroFill(BigInteger[] vector) {
		for(int i = 0; i < vector.length; i++) {
			vector[i] = BigInteger.valueOf(0);
		}
		return vector;
	}
	
	public static ProtocolProducer makeInputProtocols(BigInteger[][] values, int[][] pattern, SInt[][] matrix, BasicNumericFactory factory) {
		if (matrix.length != values.length || values.length != pattern.length || 
				values[0].length != matrix[0].length || values[0].length != pattern[0].length) {
			throw new RuntimeException("Input Dimensions are not equal");
		}
		ParallelProtocolProducer par = new ParallelProtocolProducer();
		for(int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (pattern[i][j] != 0) {
					par.append(factory.getCloseProtocol(values[i][j], matrix[i][j], pattern[i][j]));
				}
			}			
		}
		return par;
	}
	
	public static ProtocolProducer makeInputProtocols(BigInteger[] values, int[] pattern, SInt[] vector, BasicNumericFactory factory) {
		if (vector.length != values.length || vector.length != pattern.length) {throw new RuntimeException("Inputs are not equal length");}
		ParallelProtocolProducer input = new ParallelProtocolProducer();
		for(int i = 0; i < vector.length; i++) {
			if (pattern[i] != 0) {
				input.append(factory.getCloseProtocol(values[i], vector[i], pattern[i]));
			}
		}
		return input;
	}
	
	public static BigInteger getRandomNumber(Random rand) {
		byte[] bytes = new byte[size];
		rand.nextBytes(bytes);
		return new BigInteger(bytes).mod(p);
	}
}