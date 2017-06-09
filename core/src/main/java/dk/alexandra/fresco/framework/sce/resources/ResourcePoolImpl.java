/*
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
package dk.alexandra.fresco.framework.sce.resources;

import dk.alexandra.fresco.framework.network.Network;
import dk.alexandra.fresco.framework.sce.resources.storage.StreamedStorage;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Container for resources needed by runtimes (protocol suites).
 *
 * @author Kasper Damgaard
 */
public class ResourcePoolImpl implements ResourcePool {

  private int myId;
  private int noOfPlayers;
  protected Network network;
  private StreamedStorage streamedStorage;
  protected Random random;
  private SecureRandom secRand;

  public ResourcePoolImpl(int myId, int noOfPlayers, Network network,
      StreamedStorage streamedStorage, Random random, SecureRandom secRand) {
    this.myId = myId;
    this.noOfPlayers = noOfPlayers;
    this.network = network;
    this.streamedStorage = streamedStorage;
    this.random = random;
    this.secRand = secRand;
  }

  @Override
  public Network getNetwork() {
    return this.network;
  }

  @Override
  public StreamedStorage getStreamedStorage() {
    return this.streamedStorage;
  }

  @Override
  public Random getRandom() {
    return this.random;
  }

  @Override
  public SecureRandom getSecureRandom() {
    return this.secRand;
  }

  @Override
  public int getMyId() {
    return this.myId;
  }

  @Override
  public int getNoOfParties() {
    return this.noOfPlayers;
  }

}