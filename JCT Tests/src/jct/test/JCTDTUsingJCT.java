/**
 * @author Mathieu Lemoine
 * @created 2009-03-11 (水)
 *
 * Licensed under 3-clause BSD License:
 * Copyright © 2009, Mathieu Lemoine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of Mathieu Lemoine nor the
 *    names of contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY Mathieu Lemoine ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Mathieu Lemoine BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jct.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import jct.kernel.IJCTRootNode;
import jct.test.rsc.Constant;
import jct.test.rsc.jct.JCTConstant;
import jct.tools.JCTPrettyPrinter;
import jct.util.collection.IndirectCollection;
import junit.framework.Assert;
import junit.framework.TestCase;

public final class JCTDTUsingJCT extends TestCase {
	private final File srcFiles[] = new File[JCTConstant.files.length];
	private final File outputFiles[] = new File[JCTConstant.files.length];
	private final File expectedFiles[] = new File[JCTConstant.files.length];
	private final File serializedFile;
	private final File garbageDir;

	private static String getFileContent(final File file) {
		try {
			final char characters[] = new char[(int) file.length()];
			final FileInputStream fis = new FileInputStream(file);
			final int length =
				new BufferedReader(new InputStreamReader(fis)).read(
					characters,
					0,
					characters.length);
			final String result = new String(characters, 0, length);
			fis.close();
			return result;
		}
		catch (final IOException e) {
			Assert.fail("Cannot read file " + file.getAbsolutePath());
		}
		throw new RuntimeException(
			"Assert.fail() failed to fail... (Cannot read file + "
					+ file.getAbsolutePath() + ")");
	}
	public JCTDTUsingJCT(final String name) {
		super(name);
		this.garbageDir = new File(Constant.TMP_PATH);
		this.serializedFile =
			new File(Constant.RSC_PATH + JCTConstant.SER_FILE);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		if (!this.garbageDir.exists() && !this.garbageDir.mkdirs()) {
			Assert.fail("Cannot create garbage directory (" + Constant.TMP_PATH
					+ ") !");
		}

		for (int i = 0; i < JCTConstant.files.length; ++i) {
			this.srcFiles[i] =
				new File(Constant.SRC_PATH + JCTConstant.files[i]);
			this.outputFiles[i] =
				new File(Constant.TMP_PATH + JCTConstant.files[i]);
			this.expectedFiles[i] =
				new File(Constant.RSC_PATH + JCTConstant.files[i]);
		}
	}

	public void testCreatorAndPrettyPrinter() {
		try {
			final FileInputStream fis =
				new FileInputStream(this.serializedFile);
			// TODO Fix test... crappy test...
			final ObjectInputStream ois = new ObjectInputStream(fis) {
				@Override
				protected ObjectStreamClass readClassDescriptor()
						throws IOException, ClassNotFoundException {
					ObjectStreamClass resultClassDescriptor =
						super.readClassDescriptor();

					if (resultClassDescriptor.getName().equals(
						"util.collection.IndirectCollection"))
						resultClassDescriptor =
							ObjectStreamClass.lookup(IndirectCollection.class);

					return resultClassDescriptor;
				}
			};
			final IJCTRootNode jct = (IJCTRootNode) ois.readObject();
			ois.close();
			fis.close();

			jct.accept(new JCTPrettyPrinter(this.garbageDir));

			for (int i = 0; i < JCTConstant.files.length; ++i)
				Assert.assertEquals(
					"Difference between files "
							+ this.outputFiles[i].getCanonicalPath() + " and "
							+ this.expectedFiles[i].getCanonicalPath() + " ("
							+ i + ')',
					JCTDTUsingJCT.getFileContent(this.expectedFiles[i]),
					JCTDTUsingJCT.getFileContent(this.outputFiles[i]));
		}
		catch (final ClassNotFoundException e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
		catch (final IOException e) {
			Assert.fail(e.getMessage());
		}
	}
}
