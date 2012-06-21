/*
Copyright (C) 2012 Christoph Husse

Permission is hereby granted, free of charge, to any person obtaining a copy of this 
software and associated documentation files (the "Software"), to deal in the Software 
without restriction, including without limitation the rights to use, copy, modify, merge, 
publish, distribute, sublicense, and/or sell copies of the Software, and to permit 
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or 
substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS IN THE SOFTWARE. 
 */
package uebung05;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import javax.imageio.ImageIO;

public class Framework {

	private static class CallStackEntry
	{
		public int x;
		public int y;
		public int parentLabel;
		
		public CallStackEntry(int _x, int _y, int _parentLabel)
		{
			x = _x;
			y = _y;
			parentLabel = _parentLabel;
		}
	}
	
	static void processLabeling(String inputFile, String outputFile, ILabeler labeler) throws Exception {
			File src = new File(inputFile);
			
			if(!src.exists())
			{
				System.out.println("[ERROR]: File \"" + src.getCanonicalPath() + "\" does not exist!");
				return;
			}
			
			File dst = new File(outputFile);
			dst.delete();
			
			// read input image...
			BufferedImage img = ImageIO.read(src);
			float[] pixels = img.getData().getPixels(0, 0, img.getWidth(), img.getHeight(), (float[])null);
			int[][] image = new int[img.getWidth()][img.getHeight()];
			int[][] label = new int[image.length][image[0].length];
			boolean[][] visited = new boolean[image.length][image[0].length];
			
			for(int x = 0, i = 0; x < img.getWidth(); x++){
				for(int y = 0; y < img.getHeight(); y++){
					label[x][y] = i;
					image[x][y] = (int)pixels[i++];
				}
			}
			
			// apply labeling...
			labeler.process(image, label);
			
			// hacky greedy algorithm for minimal graph coloring
			HashMap<Integer,Integer> distinct = new HashMap<Integer,Integer>();
			HashSet<Integer> coloring = new HashSet<Integer>();
			HashSet<Integer> availColors = new HashSet<Integer>();
			HashSet<Integer> allColors = new HashSet<Integer>();
			for(int x = 0, i = 0; x < img.getWidth(); x++){
				for(int y = 0; y < img.getHeight(); y++){
					if(!distinct.containsKey(label[x][y]))
						distinct.put(label[x][y], i++);
				}
			}
			
			for(int x = 0; x < img.getWidth(); x++){
				for(int y = 0; y < img.getHeight(); y++){
					
					if(distinct.get(label[x][y]) < 0)
						continue; // already colored
					
					coloring.clear();
					
					Stack<CallStackEntry> callStack = new Stack<CallStackEntry>();
					
					callStack.push(new CallStackEntry(x, y, label[x][y]));
					
					while(!callStack.isEmpty())
					{
						int ix = callStack.peek().x;
						int iy = callStack.peek().y;
						int parentLabel = callStack.pop().parentLabel;
						
						if((ix < 0) || (iy < 0) || (ix >= visited.length) || (iy >= visited[0].length) || visited[ix][iy])
							continue;
						
						int current = label[ix][iy];
						
						if(current != parentLabel)
						{
							int color = distinct.get(current);
							
							if(color < 0)
								coloring.add(color);
							
							continue;
						}
						else
						{
							visited[ix][iy] = true;
							
							callStack.push(new CallStackEntry(ix + 1, iy, current));
							callStack.push(new CallStackEntry(ix - 1, iy, current));
							callStack.push(new CallStackEntry(ix, iy + 1, current));
							callStack.push(new CallStackEntry(ix, iy - 1, current));
						}
					}
					
					// pick a free color or add a new one
					availColors.addAll(allColors);
					
					for(int color : coloring){
						availColors.remove(-color);
					}
					
					int color;
					if(availColors.isEmpty())
						allColors.add(color = allColors.size() + 1);
					else
						color = availColors.iterator().next();
					
					distinct.put(label[x][y], -color);
				}
			}
			
			Arrays.fill(pixels, 0);
			float lumFactor = (allColors.size() <= 1) ? 0 : 255.0f / (allColors.size() - 1);
			
			for(int x = 0, i = 0; x < img.getWidth(); x++){
				for(int y = 0; y < img.getHeight(); y++){
					pixels[i++] = (-distinct.get(label[x][y])-1) * lumFactor;
				}
			}
			
			// write final grayscale label map to file...
			img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
			img.getRaster().setPixels(0, 0, img.getWidth(), img.getHeight(), pixels);
			ImageIO.write(img, "PNG", dst);
			
			
			System.out.println("[SUCCEEDED]");
	}

    public static void main(String[] args) throws Exception {
		if(args.length != 2) {
	    	System.out.println("[ERROR]: Requires exactly two arguments, the input file and the output file!");
	    	System.exit(1);
		}
	Framework.processLabeling(args[0], args[1], new YourCode());
    }	
}
