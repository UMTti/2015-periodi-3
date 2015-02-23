# JPEG-like compressing algorithm 

JPEG based image compression written in Java. My filename extension is .gpeg . Actually the same parts as in JPEG and now ready are:

1. Discrete cosine transformation
2. Quantization

3. Huffman coding is done with constructing a trie, not with statistical way as JPEG does. 

Source code is in jpeg/src folder.

#Usage

At the moment, this program requires that both width % 8 = 0 and height % 8 = 0. I'm working with support to all images.

* Compress .rgb -image
```
java -jar "/home/username/JPEG/jpeg/dist/jpeg.jar" image.rgb image.gpeg height width

```
* Show .gpeg image 

```
java -jar "/home/username/JPEG/jpeg/dist/jpeg.jar" image.gpeg height width
```
