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
package dk.alexandra.fresco.lib.compare.zerotest;

import dk.alexandra.fresco.framework.Computation;
import dk.alexandra.fresco.framework.builder.BuilderFactoryNumeric;
import dk.alexandra.fresco.framework.builder.ComputationBuilder;
import dk.alexandra.fresco.framework.builder.ProtocolBuilder.SequentialProtocolBuilder;
import dk.alexandra.fresco.framework.value.SInt;

/**
 * testing for equality with zero for a bitLength-bit number (positive or negative)
 *
 * @author ttoft
 */
public class ZeroTestProtocol4    implements ComputationBuilder<SInt> {

  private final BuilderFactoryNumeric factoryNumeric;
  private final int bitLength;
  private final Computation<SInt> input;

  public ZeroTestProtocol4(
      BuilderFactoryNumeric factoryNumeric,
      int bitLength, Computation<SInt> input) {
    this.factoryNumeric = factoryNumeric;
    this.bitLength = bitLength;
    this.input = input;
  }

  @Override
  public Computation<SInt> build(SequentialProtocolBuilder builder) {
    Computation<SInt> reduced = builder.createSequentialSub(
        new ZeroTestReducer(bitLength, input));
    return builder.createSequentialSub(
        new ZeroTestBruteforce(factoryNumeric, bitLength, reduced));
  }
}
