/*
 * Copyright 2011, Zettabyte Storage LLC
 * 
 * This file is part of Vash.
 * 
 * Vash is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Vash is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with Vash.  If not, see <http://www.gnu.org/licenses/>.
 */
package test;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import test.operation.TestOperationIntegration;
import vash.ImageParameters;
import vash.Output;
import vash.OutputParameters;
import vash.Tree;
import vash.TreeParameters;

public class TestTreeBuilder {
	private final int GALLERY_SIZE = 100;
	private final int WIDTH = 128;
	private final int HEIGHT = 128;

	public void runStaticTest(String algo) {
		TreeParameters tp;
		Tree tree;

		// ensure we have an output directory
		File treeTgt = new File(String.format("./gallery-%s/trees/", algo));
		if(!treeTgt.exists()) {
			Assert.assertTrue(treeTgt.mkdirs());
		}
		
		for(int i = 0; i < GALLERY_SIZE; i++) {
			tp = new TreeParameters(String.format("%03d", i), algo);
			tree = new Tree(tp);

			// write out the tree we built
			try {
				tree.show(String.format("./gallery-%s/trees/%03d.txt", algo, i));
			} catch(IOException e) {
				fail(e.toString());
			}

			ImageParameters ip = new ImageParameters(WIDTH, HEIGHT);
			tree.setGenerationParameters(ip);
			byte[] actual = tree.generateCurrentFrame();
			TestOperationIntegration.compare(actual, WIDTH, HEIGHT, String.format("./test/reference-%s/%03d.png", algo, i), null);
			
			OutputParameters op = new OutputParameters(String.format("./gallery-%s/%03d.png", algo, i), WIDTH, HEIGHT);
			Output out = new Output(op, tree);
			out.generate();
		}
	}

	@Test
	public void testBuildTree1Fast() {
		runStaticTest("1-fast");
	}

	@Test
	public void testBuildTree1() {
		runStaticTest("1");
	}

}
